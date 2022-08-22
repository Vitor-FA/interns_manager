package br.com.manager.interns.API.service.impl;

import br.com.manager.interns.API.domains.CreatedAndNotCreatedObjects;
import br.com.manager.interns.API.domains.DeletedAndNotDeletedObjects;
import br.com.manager.interns.API.domains.InternsDomain;
import br.com.manager.interns.API.domains.LeadDomain;
import br.com.manager.interns.API.exception.AlreadyAssociatedException;
import br.com.manager.interns.API.exception.AssociationNotFoundException;
import br.com.manager.interns.API.exception.ResourceNotFoundException;
import br.com.manager.interns.API.repository.InternsRepository;
import br.com.manager.interns.API.repository.LeadRepository;
import br.com.manager.interns.API.service.LeadInternsService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
@Slf4j
@CacheConfig(cacheNames = {"buddys", "interns", "leads"})
public class LeadInternsServiceImpl implements LeadInternsService {

  @Autowired
  private LeadRepository leadRepository;

  @Autowired
  private InternsRepository internsRepository;

  @Override
  @Transactional
  @CacheEvict(allEntries = true)
  public ResponseEntity<CreatedAndNotCreatedObjects> createLeadIntern(
      UUID leadId,
      List<UUID> internsId) throws ResourceNotFoundException, AlreadyAssociatedException {
    var leadDomain = findLead(leadId);

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
        throw new AlreadyAssociatedException("Lead", "Intern");
      }
    });
  }

  @Override
  @Transactional
  @CacheEvict(allEntries = true)
  public ResponseEntity<DeletedAndNotDeletedObjects> deleteLeadIntern(
      UUID leadId,
      List<UUID> internsId) throws ResourceNotFoundException, AssociationNotFoundException {
    var leadDomain = findLead(leadId);

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
      List<InternsDomain> internsDomains) throws ResourceNotFoundException {
    var containsInterns = new ArrayList<Boolean>();
    internsDomains.forEach(intern -> {
      if (!leadDomain.getInterns().contains(intern)) {
        containsInterns.add(false);
      }
    });

    if (!containsInterns.isEmpty()) {
      throw new AssociationNotFoundException("Lead", "Intern");
    }
  }

  private LeadDomain findLead(UUID leadId) throws ResourceNotFoundException {
    return leadRepository.findById(leadId).orElseThrow(() -> new ResourceNotFoundException("Lead"));
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
