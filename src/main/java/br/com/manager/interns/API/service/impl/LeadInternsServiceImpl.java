package br.com.manager.interns.API.service.impl;

import br.com.manager.interns.API.domains.BuddysDomain;
import br.com.manager.interns.API.domains.CreatedAndNotCreatedObjects;
import br.com.manager.interns.API.domains.DeletedAndNotDeletedObjects;
import br.com.manager.interns.API.domains.InternsDomain;
import br.com.manager.interns.API.domains.LeadDomain;
import br.com.manager.interns.API.repository.BuddysRepository;
import br.com.manager.interns.API.repository.InternsRepository;
import br.com.manager.interns.API.repository.LeadRepository;
import br.com.manager.interns.API.service.LeadInternsService;
import java.nio.channels.AlreadyConnectedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Service
@Slf4j
public class LeadInternsServiceImpl implements LeadInternsService {

  @Autowired
  private LeadRepository leadRepository;

  @Autowired
  private InternsRepository internsRepository;

  @Override
  @Transactional
  public ResponseEntity<CreatedAndNotCreatedObjects> createLeadIntern(
      UUID LeadId,
      List<UUID> internsId) throws NotFoundException {
    var leadDomain = findLead(LeadId);

    var notFoundInternsList = getNotFoundInterns(internsId);
    var foundInternsList = getFoundInterns(internsId);

    verifyIfInternAlreadyHaveTheSpecifiedLead(foundInternsList);

    setInternLead(foundInternsList, leadDomain);
    leadDomain.addInterns(foundInternsList);

    var createdAndNotCreatedObjects =
        CreatedAndNotCreatedObjects.getCreatedAndNotCreatedObjectsBuilded(
            notFoundInternsList,
            foundInternsList
        );

    leadRepository.save(leadDomain);
    internsRepository.saveAll(foundInternsList);

    return ResponseEntity.ok(createdAndNotCreatedObjects);
  }

  public void setInternLead(List<InternsDomain> internsDomains, LeadDomain leadDomain) {
    internsDomains.forEach(internsDomain -> internsDomain.setLead(leadDomain));
  }

  private void verifyIfInternAlreadyHaveTheSpecifiedLead(
      List<InternsDomain> internsDomains) {
    internsDomains.forEach(internDomain -> {
      var alreadyHaveLead = Optional.ofNullable(internDomain.getLead());
      if (alreadyHaveLead.isPresent()) {
        throw new AlreadyConnectedException();
      }
    });
  }

  @Override
  @Transactional
  public ResponseEntity<DeletedAndNotDeletedObjects> deleteLeadIntern(
      UUID LeadId,
      List<UUID> internsId) throws NotFoundException {
    var leadDomain = findLead(LeadId);

    var notFoundInternsList = getNotFoundInterns(internsId);
    var foundInternsList = getFoundInterns(internsId);

    verifyIfTheLeadHaveTheInterns(leadDomain, foundInternsList);

    removeInternsLead(foundInternsList);
    leadDomain.removeInterns(foundInternsList);

    var deletedAndNotDeletedObjects =
        DeletedAndNotDeletedObjects.getDeletedAndNotDeletedObjects(
            notFoundInternsList,
            foundInternsList
        );

    leadRepository.save(leadDomain);
    internsRepository.saveAll(foundInternsList);

    return ResponseEntity.ok(deletedAndNotDeletedObjects);
  }

  public void removeInternsLead(List<InternsDomain> internsDomains) {
    internsDomains.forEach(InternsDomain::removeLead);
  }

  private void verifyIfTheLeadHaveTheInterns(
      LeadDomain leadDomain,
      List<InternsDomain> internsDomains) throws NotFoundException {
    var containsInterns = new ArrayList<Boolean>();
    internsDomains.forEach(intern -> {
      if (!leadDomain.getInterns().contains(intern)) {
        containsInterns.add(false);
      }
    });

    if (!containsInterns.isEmpty()) {
      throw new NotFoundException();
    }
  }

  private LeadDomain findLead(UUID leadId) throws NotFoundException {
    return leadRepository.findById(leadId).orElseThrow(NotFoundException::new);
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
