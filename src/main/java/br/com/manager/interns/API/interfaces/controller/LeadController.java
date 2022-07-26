package br.com.manager.interns.API.interfaces.controller;

import br.com.manager.interns.API.interfaces.dto.LeadDTO;
import br.com.manager.interns.API.interfaces.dto.PutLeadDTO;
import br.com.manager.interns.API.interfaces.dto.ResponseLead;
import br.com.manager.interns.API.service.impl.LeadServiceImpl;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
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
    leadService.postLead(LeadDTO);
    return ResponseEntity.ok().build();
  }

  @GetMapping
  public ResponseEntity<Page<ResponseLead>> getAllLead(
      @PositiveOrZero @Valid @RequestParam(value = "_page") Integer page,
      @PositiveOrZero @Valid @RequestParam(value = "_size") Integer size,
      @RequestParam(value = "_expand", required = false) List<String> expand
  ) {
    Pageable pageable = PageRequest.of(page, size);

    var leadResponse = leadService.getAllLead(pageable, expand);

    return ResponseEntity.ok(leadResponse);
  }

  @GetMapping("/{leadId}")
  public ResponseEntity<ResponseLead> getLeadById(
      @PathVariable(value = "leadId") UUID leadId,
      @RequestParam(value = "_expand", required = false) List<String> expand
  ) {
    var responseLead = leadService.getLeadById(leadId, expand);
    return ResponseEntity.ok(responseLead);
  }

  @DeleteMapping("/{leadId}")
  public ResponseEntity<Void> deleteLead(
      @PathVariable(value = "leadId")UUID leadId
  ) {
    leadService.deleteLead(leadId);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{leadId}")
  public ResponseEntity<Void> putLead(
      @PathVariable(value = "leadId")UUID leadId,
      @Valid @RequestBody PutLeadDTO putLeadDTO
  ) {
    leadService.putLead(leadId, putLeadDTO);
    return ResponseEntity.noContent().build();
  }

}
