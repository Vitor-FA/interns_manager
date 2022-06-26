package br.com.manager.interns.API.interfaces.controller;

import br.com.manager.interns.API.interfaces.dto.LeadDTO;
import br.com.manager.interns.API.service.impl.LeadServiceImpl;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/leads")
public class LeadController {

  private final LeadServiceImpl leadService;

  @PostMapping
  public ResponseEntity<Void> postLead(
      @Valid @RequestBody LeadDTO LeadDTO
  ) {
    return ResponseEntity.ok().build();
  }

  @GetMapping
  public ResponseEntity<Void> getAllLead(
      @PositiveOrZero @Valid @RequestParam(value = "_page") Integer page,
      @PositiveOrZero @Valid @RequestParam(value = "_size") Integer size,
      @RequestParam(value = "_expand", required = false) List<String> expand
  ) {
    Pageable pageable = PageRequest.of(page, size);

    return ResponseEntity.ok().build();
  }

  @GetMapping("/{leadId}")
  public ResponseEntity<Void> getLeadById(
      @PathVariable(value = "leadId") UUID leadId
  ) {
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{leadId}")
  public ResponseEntity<Void> deleteLead(
      @PathVariable(value = "leadId")UUID leadId
  ) {
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/{leadId}")
  public ResponseEntity<Void> patchLead(
      @PathVariable(value = "leadId")UUID leadId,
      @Valid @RequestBody LeadDTO LeadDTO
  ) {
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{leadId}")
  public ResponseEntity<Void> putLead(
      @PathVariable(value = "leadId")UUID leadId,
      @Valid @RequestBody LeadDTO LeadDTO
  ) {
    return ResponseEntity.noContent().build();
  }

}
