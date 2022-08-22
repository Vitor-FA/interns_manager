package br.com.manager.interns.API.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.manager.interns.API.domains.BuddysDomain;
import br.com.manager.interns.API.exception.EmailAlreadyExistException;
import br.com.manager.interns.API.exception.ResourceNotFoundException;
import br.com.manager.interns.API.interfaces.dto.PostBuddys;
import br.com.manager.interns.API.interfaces.dto.PutBuddys;
import br.com.manager.interns.API.repository.BuddysRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class BuddysServiceImplTest {

  @InjectMocks
  private BuddysServiceImpl buddysService;

  @Mock
  private BuddysRepository buddysRepository;

  @Spy
  private ModelMapper modelMapper;

  private BuddysDomain buddysDomain;

  private PostBuddys postBuddys;

  private PutBuddys putBuddys;

  private UUID buddyUUID = UUID.randomUUID();

  @BeforeEach
  private void beforeEach() {
    buddysDomain = BuddysDomain.builder()
        .id(UUID.randomUUID())
        .name("Test")
        .email("Test@email.com")
        .build();

    postBuddys = PostBuddys.builder()
        .email("Test@email.com")
        .name("Test")
        .build();

    putBuddys = PutBuddys.builder()
        .email("PutTest@email.com")
        .name("Test")
        .build();
  }

  @DisplayName("Must create a buddy successfully")
  @Test
  void mustCreateBuddySuccessfully() {
    when(modelMapper.map(postBuddys, BuddysDomain.class)).thenReturn(buddysDomain);

    assertDoesNotThrow(() -> buddysService.postBuddys(postBuddys));

    verify(buddysRepository, times(1)).save(buddysDomain);
  }

  @DisplayName("Must throw EmailAlreadyExistException when creating a buddy with email already in use")
  @Test
  void mustThrowEmailAlreadyExistExceptionWhenCreatingBuddyWithEmailAlreadyInUse() {
    when(buddysRepository.findByEmail(postBuddys.getEmail())).thenReturn(Optional.of(buddysDomain));

    assertThrows(EmailAlreadyExistException.class, () -> buddysService.postBuddys(postBuddys));

    verify(buddysRepository, times(0)).save(buddysDomain);
  }

  @DisplayName("Must get all buddys")
  @Test
  void mustGetAllBuddys() {
    Pageable pageable = PageRequest.of(0, 1);

    when(buddysRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(buddysDomain)));

    var response = assertDoesNotThrow(
        () -> buddysService.getAllBuddys(pageable, List.of()));

    verify(buddysRepository, times(1)).findAll(pageable);

    assertEquals(1, response.getTotalElements());
    assertEquals(buddysDomain.getId(), response.getContent().get(0).getId());
  }

  @DisplayName("Must get buddy by Id successfully")
  @Test
  void mustGetBuddySuccessfully() {
    when(buddysRepository.findById(buddyUUID)).thenReturn(Optional.of(buddysDomain));

    var response = assertDoesNotThrow(
        () -> buddysService.getBuddysById(buddyUUID, List.of()));

    verify(buddysRepository, times(1)).findById(buddyUUID);

    assertEquals(buddysDomain.getId(), response.getId());
  }

  @DisplayName("Must throw ResourceNotFoundException when buddy is not found")
  @Test
  void mustThrowResourceNotFoundExceptionWhenBuddyIsNotFound() {
    var emptyList = List.of("");
    var response = assertThrows(
        ResourceNotFoundException.class, () -> buddysService.getBuddysById(buddyUUID,
            emptyList));

    verify(buddysRepository, times(1)).findById(buddyUUID);

    var errorMessage = "Buddy with this ID was not found";

    assertEquals(response.getMessage(), errorMessage);
  }

  @DisplayName("Must delete buddy successfully")
  @Test
  void mustDeleteBuddySuccessfully() {
    when(buddysRepository.findById(buddyUUID)).thenReturn(Optional.of(buddysDomain));

    assertDoesNotThrow(() -> buddysService.deleteBuddys(buddyUUID));

    verify(buddysRepository, times(1)).delete(buddysDomain);
  }

  @DisplayName("Should PUT a buddy")
  @Test
  void shouldPutABuddy() {
    when(modelMapper.map(putBuddys, BuddysDomain.class)).thenReturn(buddysDomain);
    when(buddysRepository.findById(buddyUUID)).thenReturn(Optional.of(buddysDomain));

    assertDoesNotThrow(() -> buddysService.putBuddys(buddyUUID, putBuddys));

    verify(buddysRepository, times(1)).save(buddysDomain);
  }

  @DisplayName("Must throw EmailAlreadyExistException when trying to put an intern "
      + "with an existing email")
  @Test
  void mustThrowEmailAlreadyExistExceptionWhenTryingToPutInternWithExistingEmail() {
    when(buddysRepository.findById(buddyUUID)).thenReturn(Optional.of(buddysDomain));
    when(buddysRepository.findByEmail(putBuddys.getEmail())).thenReturn(Optional.of(buddysDomain));

    var response = assertThrows(
        EmailAlreadyExistException.class,
        () -> buddysService.putBuddys(buddyUUID, putBuddys));

    verify(buddysRepository, times(0)).save(buddysDomain);

    var errorMessage = "Email already in use";

    assertEquals(errorMessage, response.getMessage());
  }

}