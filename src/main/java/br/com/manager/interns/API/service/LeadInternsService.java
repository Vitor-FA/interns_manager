package br.com.manager.interns.API.service;

import br.com.manager.interns.API.domains.CreatedAndNotCreatedObjects;
import br.com.manager.interns.API.domains.DeletedAndNotDeletedObjects;
import br.com.manager.interns.API.exception.AlreadyAssociatedException;
import br.com.manager.interns.API.exception.AssociationNotFoundException;
import br.com.manager.interns.API.exception.ResourceNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface LeadInternsService {

  ResponseEntity<CreatedAndNotCreatedObjects> createLeadIntern(UUID leadId, List<UUID> internsId)
      throws ResourceNotFoundException, AlreadyAssociatedException;

  ResponseEntity<DeletedAndNotDeletedObjects> deleteLeadIntern(UUID leadId, List<UUID> internsId)
      throws ResourceNotFoundException, AssociationNotFoundException;
}
