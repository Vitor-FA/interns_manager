package br.com.manager.interns.API.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.manager.interns.API.domains.InternsDomain;
import br.com.manager.interns.API.exception.EmailAlreadyExistException;
import br.com.manager.interns.API.exception.ResourceNotFoundException;
import br.com.manager.interns.API.interfaces.dto.PostInterns;
import br.com.manager.interns.API.interfaces.dto.PutInterns;
import br.com.manager.interns.API.repository.InternsRepository;
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
class InternsServiceImplTest {

  @InjectMocks
  private InternsServiceImpl internsService;

  @Mock
  private InternsRepository internsRepository;

  @Spy
  private ModelMapper modelMapper;

  private InternsDomain internsDomain;

  private PostInterns postInterns;

  private PutInterns putInterns;

  private UUID internUUID = UUID.randomUUID();

  @BeforeEach
  private void beforeEach() {
    internsDomain = InternsDomain.builder()
        .id(UUID.randomUUID())
        .name("Test")
        .email("Test@email.com")
        .build();

    postInterns = PostInterns.builder()
        .email("Test@email.com")
        .name("Test")
        .build();

    putInterns = PutInterns.builder()
        .email("PutTest@email.com")
        .name("Test")
        .build();
  }

  @DisplayName("Must create a intern successfully")
  @Test
  void mustCreateInternSuccessfully() {
    when(modelMapper.map(postInterns, InternsDomain.class)).thenReturn(internsDomain);

    assertDoesNotThrow(() -> internsService.postInterns(postInterns));

    verify(internsRepository, times(1)).save(internsDomain);
  }

  @DisplayName("Must throw EmailAlreadyExistException when creating a intern with email already in use")
  @Test
  void mustThrowEmailAlreadyExistExceptionWhenCreatingInternWithEmailAlreadyInUse() {
    when(internsRepository.findByEmail(postInterns.getEmail())).thenReturn(
        Optional.of(internsDomain));

    assertThrows(EmailAlreadyExistException.class, () -> internsService.postInterns(postInterns));

    verify(internsRepository, times(0)).save(internsDomain);
  }

  @DisplayName("Must get all interns")
  @Test
  void mustGetAllInterns() {
    Pageable pageable = PageRequest.of(0, 1);

    when(internsRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(internsDomain)));

    var response = assertDoesNotThrow(
        () -> internsService.getAllInterns(pageable, List.of()));

    verify(internsRepository, times(1)).findAll(pageable);

    assertEquals(1, response.getTotalElements());
    assertEquals(internsDomain.getId(), response.getContent().get(0).getId());
  }

  @DisplayName("Must get intern by Id successfully")
  @Test
  void mustGetInternSuccessfully() {
    when(internsRepository.findById(internUUID)).thenReturn(Optional.of(internsDomain));

    var response = assertDoesNotThrow(
        () -> internsService.getInternsById(internUUID, List.of()));

    verify(internsRepository, times(1)).findById(internUUID);

    assertEquals(internsDomain.getId(), response.getId());
  }

  @DisplayName("Must throw ResourceNotFoundException when intern is not found")
  @Test
  void mustThrowResourceNotFoundExceptionWhenInternIsNotFound() {
    var emptyList = List.of("");
    var response = assertThrows(
        ResourceNotFoundException.class,
        () -> internsService.getInternsById(internUUID, emptyList));

    verify(internsRepository, times(1)).findById(internUUID);

    var errorMessage = "Intern with this ID was not found";

    assertEquals(response.getMessage(), errorMessage);
  }

  @DisplayName("Must delete intern successfully")
  @Test
  void mustDeleteInternSuccessfully() {
    when(internsRepository.findById(internUUID)).thenReturn(Optional.of(internsDomain));

    assertDoesNotThrow(() -> internsService.deleteInterns(internUUID));

    verify(internsRepository, times(1)).delete(internsDomain);
  }

  @DisplayName("Should PUT an intern")
  @Test
  void shouldPutAnIntern() {
    when(modelMapper.map(putInterns, InternsDomain.class)).thenReturn(internsDomain);
    when(internsRepository.findById(internUUID)).thenReturn(Optional.of(internsDomain));

    assertDoesNotThrow(() -> internsService.putInterns(internUUID, putInterns));

    verify(internsRepository, times(1)).save(internsDomain);
  }

  @DisplayName("Must throw EmailAlreadyExistException when trying to put an intern "
      + "with an existing email")
  @Test
  void mustThrowEmailAlreadyExistExceptionWhenTryingToPutInternWithExistingEmail() {
    when(internsRepository.findById(internUUID)).thenReturn(Optional.of(internsDomain));
    when(internsRepository.findByEmail(putInterns.getEmail())).thenReturn(Optional.of(internsDomain));

    var response = assertThrows(
        EmailAlreadyExistException.class,
        () -> internsService.putInterns(internUUID, putInterns));

    verify(internsRepository, times(0)).save(internsDomain);

    var errorMessage = "Email already in use";

    assertEquals(errorMessage, response.getMessage());
  }
}