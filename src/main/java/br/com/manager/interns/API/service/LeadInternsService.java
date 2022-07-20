package br.com.manager.interns.API.service;

import br.com.manager.interns.API.domains.CreatedAndNotCreatedObjects;
import br.com.manager.interns.API.domains.DeletedAndNotDeletedObjects;
import java.util.List;
import java.util.UUID;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;

public interface LeadInternsService {

  ResponseEntity<CreatedAndNotCreatedObjects> createLeadIntern(UUID leadId, List<UUID> internsId)
      throws NotFoundException;

  ResponseEntity<DeletedAndNotDeletedObjects> deleteLeadIntern(UUID LeadId, List<UUID> internsId)
      throws NotFoundException;
}
