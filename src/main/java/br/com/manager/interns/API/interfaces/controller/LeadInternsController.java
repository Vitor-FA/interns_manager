package br.com.manager.interns.API.interfaces.controller;

import br.com.manager.interns.API.domains.CreatedAndNotCreatedObjects;
import br.com.manager.interns.API.domains.DeletedAndNotDeletedObjects;
import br.com.manager.interns.API.interfaces.dto.PostInternAssociation;
import br.com.manager.interns.API.service.impl.LeadInternsServiceImpl;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/leads/{leadId}/interns")
public class LeadInternsController {
  private final LeadInternsServiceImpl leadInternsService;

  @PatchMapping
  public ResponseEntity<CreatedAndNotCreatedObjects> postBuddysInterns(
      @RequestBody PostInternAssociation postInternAssociation,
      @PathVariable(value = "leadId") UUID leadId) {
    return leadInternsService.createLeadIntern(leadId, postInternAssociation.getInternsIds());
  }

  @DeleteMapping
  public ResponseEntity<DeletedAndNotDeletedObjects> deleteBuddysInterns(
      @RequestBody PostInternAssociation postInternAssociation,
      @PathVariable(value = "leadId") UUID leadId) {
    return leadInternsService.deleteLeadIntern(leadId, postInternAssociation.getInternsIds());
  }
}
