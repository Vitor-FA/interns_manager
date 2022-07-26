package br.com.manager.interns.API.service.impl;

import br.com.manager.interns.API.domains.BuddysDomain;
import br.com.manager.interns.API.domains.CreatedAndNotCreatedObjects;
import br.com.manager.interns.API.domains.DeletedAndNotDeletedObjects;
import br.com.manager.interns.API.domains.InternsDomain;
import br.com.manager.interns.API.repository.BuddysRepository;
import br.com.manager.interns.API.repository.InternsRepository;
import br.com.manager.interns.API.service.BuddysInternsService;
import java.lang.reflect.Array;
import java.nio.channels.AlreadyConnectedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Service
@Slf4j
@CacheConfig(cacheNames = {"buddys", "interns", "leads"}, keyGenerator = "CustomKeyGenerator")
public class BuddysInternsServiceImpl implements BuddysInternsService {

  @Autowired
  private BuddysRepository buddysRepository;

  @Autowired
  private InternsRepository internsRepository;

  @Override
  @Transactional
  @CacheEvict(allEntries = true)
  public ResponseEntity<CreatedAndNotCreatedObjects> createBuddyIntern(
      UUID buddyId,
      List<UUID> internsId) throws NotFoundException {
    var buddyDomain = findBuddy(buddyId);

    var notFoundInternsList = getNotFoundInterns(internsId);
    var foundInternsList = getFoundInterns(internsId);

    verifyIfInternIsAlreadyHaveTheSpecifiedBuddy(buddyDomain, foundInternsList);

    buddyDomain.addInterns(foundInternsList);

    var createdAndNotCreatedObjects =
        CreatedAndNotCreatedObjects.getCreatedAndNotCreatedObjectsBuilded(
            notFoundInternsList,
            foundInternsList
        );

    buddysRepository.save(buddyDomain);

    return ResponseEntity.ok(createdAndNotCreatedObjects);
  }

  private void verifyIfInternIsAlreadyHaveTheSpecifiedBuddy(
      BuddysDomain buddyDomain,
      List<InternsDomain> internsDomains) {
    internsDomains.forEach(internDomain -> {
      var alreadyHaveBuddy = internDomain.getBuddys().contains(buddyDomain);
      if (alreadyHaveBuddy) {
        throw new AlreadyConnectedException();
      }
    });
  }

  @Override
  @Transactional
  @CacheEvict(allEntries = true)
  public ResponseEntity<DeletedAndNotDeletedObjects> deleteBuddyIntern(
      UUID buddyId,
      List<UUID> internsId) throws NotFoundException {
    var buddyDomain = findBuddy(buddyId);

    var notFoundInternsList = getNotFoundInterns(internsId);
    var foundInternsList = getFoundInterns(internsId);

    verifyIfTheBuddyHaveTheInterns(buddyDomain, foundInternsList);

    buddyDomain.removeInterns(foundInternsList);

    var deletedAndNotDeletedObjects =
        DeletedAndNotDeletedObjects.getDeletedAndNotDeletedObjects(
            notFoundInternsList,
            foundInternsList
        );

    buddysRepository.save(buddyDomain);

    return ResponseEntity.ok(deletedAndNotDeletedObjects);
  }

  private void verifyIfTheBuddyHaveTheInterns(
      BuddysDomain buddyDomain,
      List<InternsDomain> internsDomains) throws NotFoundException {
    var containsInterns = new ArrayList<Boolean>();
    internsDomains.forEach(intern -> {
      if (!buddyDomain.getInterns().contains(intern)) {
        containsInterns.add(false);
      }
    });

    if (!containsInterns.isEmpty()) {
      throw new NotFoundException();
    }
  }

  private BuddysDomain findBuddy(UUID buddyId) throws NotFoundException {
    return buddysRepository.findById(buddyId).orElseThrow(NotFoundException::new);
  }

  private List<InternsDomain> getFoundInterns(List<UUID> internsId) {
    var foundInternsList = new ArrayList<InternsDomain>();
    internsId.forEach(internId -> {
      var internDomain = internsRepository.findById(internId).orElse(null);
      if (Objects.nonNull(internDomain)) {
        foundInternsList.add(internDomain);
      }
    });
    return foundInternsList;
  }

  private List<UUID> getNotFoundInterns(List<UUID> internsId) {
    var notFoundInternsList = new ArrayList<UUID>();
    internsId.forEach(internId -> {
      var internDomain = internsRepository.findById(internId).orElse(null);
      if (Objects.isNull(internDomain)) {
        notFoundInternsList.add(internId);
      }
    });
    return notFoundInternsList;
  }
}
