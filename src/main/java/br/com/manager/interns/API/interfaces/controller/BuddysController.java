package br.com.manager.interns.API.interfaces.controller;

import br.com.manager.interns.API.interfaces.dto.PostBuddys;
import br.com.manager.interns.API.interfaces.dto.PutBuddys;
import br.com.manager.interns.API.interfaces.dto.ResponseBuddys;
import br.com.manager.interns.API.service.impl.BuddysServiceImpl;
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
@RequestMapping(path = "/buddys")
public class BuddysController {

  private final BuddysServiceImpl buddysService;

  @PostMapping
  public ResponseEntity<Void> postBuddys(
      @Valid @RequestBody PostBuddys postBuddys
  ) {
    var buddy = buddysService.postBuddys(postBuddys);
    return ResponseEntity.status(HttpStatus.CREATED)
        .header("id", buddy.getId().toString())
        .build();
  }

  @GetMapping
  public ResponseEntity<Page<ResponseBuddys>> getAllBuddys(
      @PositiveOrZero @Valid @RequestParam(value = "_page") Integer page,
      @PositiveOrZero @Valid @RequestParam(value = "_size") Integer size,
      @RequestParam(value = "_expand", required = false) List<String> expand
  ) {
    Pageable pageable = PageRequest.of(page, size);

    var buddys = buddysService.getAllBuddys(pageable, expand);

    return ResponseEntity.ok(buddys);
  }

  @GetMapping("/{buddyId}")
  public ResponseEntity<ResponseBuddys> getBuddysById(
      @PathVariable(value = "buddyId")UUID buddyId,
      @RequestParam(value = "_expand", required = false) List<String> expand
  ) {
    var buddy = buddysService.getBuddysById(buddyId, expand);
    return ResponseEntity.ok(buddy);
  }

  @DeleteMapping("/{buddyId}")
  public ResponseEntity<Void> deleteBuddys(
      @PathVariable(value = "buddyId")UUID buddyId
  ) {
    buddysService.deleteBuddys(buddyId);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{buddyId}")
  public ResponseEntity<Void> putBuddys(
      @PathVariable(value = "buddyId")UUID buddyId,
      @Valid @RequestBody PutBuddys buddysDTO
  ) {
    buddysService.putBuddys(buddyId, buddysDTO);
    return ResponseEntity.noContent().build();
  }

}
