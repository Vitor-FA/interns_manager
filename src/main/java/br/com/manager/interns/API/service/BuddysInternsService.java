package br.com.manager.interns.API.service;

import br.com.manager.interns.API.domains.CreatedAndNotCreatedObjects;
import br.com.manager.interns.API.domains.DeletedAndNotDeletedObjects;
import java.util.List;
import java.util.UUID;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;

public interface BuddysInternsService {

  ResponseEntity<CreatedAndNotCreatedObjects> createBuddyIntern(UUID buddyId, List<UUID> internsId)
      throws NotFoundException;

  ResponseEntity<DeletedAndNotDeletedObjects> deleteBuddyIntern(UUID buddyId, List<UUID> internsId)
      throws NotFoundException;

}
