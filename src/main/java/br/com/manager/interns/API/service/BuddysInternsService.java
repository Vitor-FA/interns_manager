package br.com.manager.interns.API.service;

import br.com.manager.interns.API.domains.CreatedAndNotCreatedObjects;
import br.com.manager.interns.API.domains.DeletedAndNotDeletedObjects;
import br.com.manager.interns.API.exception.AlreadyAssociatedException;
import br.com.manager.interns.API.exception.AssociationNotFoundException;
import br.com.manager.interns.API.exception.ResourceNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface BuddysInternsService {

  ResponseEntity<CreatedAndNotCreatedObjects> createBuddyIntern(UUID buddyId, List<UUID> internsId)
      throws AlreadyAssociatedException, ResourceNotFoundException;

  ResponseEntity<DeletedAndNotDeletedObjects> deleteBuddyIntern(UUID buddyId, List<UUID> internsId)
      throws AssociationNotFoundException, ResourceNotFoundException;

}
