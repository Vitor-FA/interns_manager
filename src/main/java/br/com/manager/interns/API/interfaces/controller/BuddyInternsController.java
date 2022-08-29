package br.com.manager.interns.API.interfaces.controller;

import br.com.manager.interns.API.domains.CreatedAndNotCreatedObjects;
import br.com.manager.interns.API.domains.DeletedAndNotDeletedObjects;
import br.com.manager.interns.API.interfaces.dto.PostInternAssociation;
import br.com.manager.interns.API.service.impl.BuddysInternsServiceImpl;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/buddys/{buddysId}/interns")
public class BuddyInternsController {

  private final BuddysInternsServiceImpl buddysInternsService;

  @PostMapping
  public ResponseEntity<CreatedAndNotCreatedObjects> postBuddysInterns(
      @RequestBody PostInternAssociation postInternAssociation,
      @PathVariable(value = "buddysId") UUID buddysId) {
    return buddysInternsService.createBuddyIntern(buddysId, postInternAssociation.getInternsIds());
  }

  @DeleteMapping
  public ResponseEntity<DeletedAndNotDeletedObjects> deleteBuddysInterns(
      @RequestBody PostInternAssociation postInternAssociation,
      @PathVariable(value = "buddysId") UUID buddysId) {
    return buddysInternsService.deleteBuddyIntern(buddysId, postInternAssociation.getInternsIds());
  }

}
