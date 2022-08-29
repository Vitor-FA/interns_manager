package br.com.manager.interns.API.interfaces.controller;

import br.com.manager.interns.API.interfaces.dto.PostInterns;
import br.com.manager.interns.API.interfaces.dto.PutInterns;
import br.com.manager.interns.API.interfaces.dto.ResponseInterns;
import br.com.manager.interns.API.service.impl.InternsServiceImpl;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
      @Valid @RequestBody PostInterns postInterns
  ) {
    var intern = internsService.postInterns(postInterns);
    return ResponseEntity.status(HttpStatus.CREATED)
        .header("id", intern.getId().toString())
        .build();
  }

  @GetMapping
  public ResponseEntity<Page<ResponseInterns>> getAllInterns(
      @PositiveOrZero @Valid @RequestParam(value = "_page") Integer page,
      @PositiveOrZero @Valid @RequestParam(value = "_size") Integer size,
      @RequestParam(value = "_expand", required = false) List<String> expand
  ) {
    Pageable pageable = PageRequest.of(page, size);

    var interns = internsService.getAllInterns(pageable, expand);

    return ResponseEntity.ok(interns);
  }

  @GetMapping("/{internId}")
  public ResponseEntity<ResponseInterns> getInternsById(
      @PathVariable(value = "internId") UUID internId,
      @RequestParam(value = "_expand", required = false) List<String> expand
  ) {
    var intern = internsService.getInternsById(internId, expand);
    return ResponseEntity.ok(intern);
  }

  @DeleteMapping("/{internId}")
  public ResponseEntity<Void> deleteInterns(
      @PathVariable(value = "internId")UUID internId
  ) {
    internsService.deleteInterns(internId);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{internId}")
  public ResponseEntity<Void> putInterns(
      @PathVariable(value = "internId")UUID internId,
      @Valid @RequestBody PutInterns putInterns
  ) {
    internsService.putInterns(internId, putInterns);
    return ResponseEntity.noContent().build();
  }

}
