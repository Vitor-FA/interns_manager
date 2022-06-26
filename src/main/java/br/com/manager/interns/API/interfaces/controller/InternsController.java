package br.com.manager.interns.API.interfaces.controller;

import br.com.manager.interns.API.interfaces.dto.BuddysDTO;
import br.com.manager.interns.API.interfaces.dto.InternsDTO;
import br.com.manager.interns.API.service.impl.BuddysServiceImpl;
import br.com.manager.interns.API.service.impl.InternsServiceImpl;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import lombok.NoArgsConstructor;
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
@RequestMapping(path = "/interns")
public class InternsController {

  private final InternsServiceImpl internsService;

  @PostMapping
  public ResponseEntity<Void> postInterns(
      @Valid @RequestBody InternsDTO InternsDTO
  ) {
    return ResponseEntity.ok().build();
  }

  @GetMapping
  public ResponseEntity<Void> getAllInterns(
      @PositiveOrZero @Valid @RequestParam(value = "_page") Integer page,
      @PositiveOrZero @Valid @RequestParam(value = "_size") Integer size,
      @RequestParam(value = "_expand", required = false) List<String> expand
  ) {
    Pageable pageable = PageRequest.of(page, size);

    return ResponseEntity.ok().build();
  }

  @GetMapping("/{internId}")
  public ResponseEntity<Void> getInternsById(
      @PathVariable(value = "internId") UUID internId
  ) {
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{internId}")
  public ResponseEntity<Void> deleteInterns(
      @PathVariable(value = "internId")UUID internId
  ) {
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/{internId}")
  public ResponseEntity<Void> patchInterns(
      @PathVariable(value = "internId")UUID internId,
      @Valid @RequestBody InternsDTO InternsDTO
  ) {
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{internId}")
  public ResponseEntity<Void> putInterns(
      @PathVariable(value = "internId")UUID internId,
      @Valid @RequestBody InternsDTO InternsDTO
  ) {
    return ResponseEntity.noContent().build();
  }

}
