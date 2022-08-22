package br.com.manager.interns.API.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.manager.interns.API.domains.LeadDomain;
import br.com.manager.interns.API.exception.EmailAlreadyExistException;
import br.com.manager.interns.API.exception.ResourceNotFoundException;
import br.com.manager.interns.API.interfaces.dto.PostLead;
import br.com.manager.interns.API.interfaces.dto.PutLead;
import br.com.manager.interns.API.repository.LeadRepository;
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
class LeadServiceImplTest {

  @InjectMocks
  private LeadServiceImpl leadService;

  @Mock
  private LeadRepository leadRepository;

  @Spy
  private ModelMapper modelMapper;

  private LeadDomain leadDomain;

  private PostLead postLead;

  private PutLead putLead;

  private UUID leadUUID = UUID.randomUUID();

  @BeforeEach
  private void beforeEach() {
    leadDomain = LeadDomain.builder()
        .id(UUID.randomUUID())
        .name("Test")
        .email("Test@email.com")
        .build();

    postLead = PostLead.builder()
        .email("Test@email.com")
        .name("Test")
        .build();

    putLead = PutLead.builder()
        .email("PutTest@email.com")
        .name("Test")
        .build();
  }

  @DisplayName("Must create a lead successfully")
  @Test
  void mustCreateLeadSuccessfully() {
    when(modelMapper.map(postLead, LeadDomain.class)).thenReturn(leadDomain);

    assertDoesNotThrow(() -> leadService.postLead(postLead));

    verify(leadRepository, times(1)).save(leadDomain);
  }

  @DisplayName("Must throw EmailAlreadyExistException when creating a lead with email already in use")
  @Test
  void mustThrowEmailAlreadyExistExceptionWhenCreatingLeadWithEmailAlreadyInUse() {
    when(leadRepository.findByEmail(postLead.getEmail())).thenReturn(Optional.of(leadDomain));

    assertThrows(EmailAlreadyExistException.class, () -> leadService.postLead(postLead));

    verify(leadRepository, times(0)).save(leadDomain);
  }

  @DisplayName("Must get all leads")
  @Test
  void mustGetAllLeads() {
    Pageable pageable = PageRequest.of(0, 1);

    when(leadRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(leadDomain)));

    var response = assertDoesNotThrow(
        () -> leadService.getAllLead(pageable, List.of()));

    verify(leadRepository, times(1)).findAll(pageable);

    assertEquals(1, response.getTotalElements());
    assertEquals(leadDomain.getId(), response.getContent().get(0).getId());
  }

  @DisplayName("Must get lead by Id successfully")
  @Test
  void mustGetLeadSuccessfully() {
    when(leadRepository.findById(leadUUID)).thenReturn(Optional.of(leadDomain));

    var response = assertDoesNotThrow(
        () -> leadService.getLeadById(leadUUID, List.of()));

    verify(leadRepository, times(1)).findById(leadUUID);

    assertEquals(leadDomain.getId(), response.getId());
  }

  @DisplayName("Must throw ResourceNotFoundException when lead is not found")
  @Test
  void mustThrowResourceNotFoundExceptionWhenLeadIsNotFound() {
    var emptyList = List.of("");
    var response = assertThrows(
        ResourceNotFoundException.class, () -> leadService.getLeadById(leadUUID, emptyList));

    verify(leadRepository, times(1)).findById(leadUUID);

    var errorMessage = "Lead with this ID was not found";

    assertEquals(response.getMessage(), errorMessage);
  }

  @DisplayName("Must delete lead successfully")
  @Test
  void mustDeleteLeadSuccessfully() {
    when(leadRepository.findById(leadUUID)).thenReturn(Optional.of(leadDomain));

    assertDoesNotThrow(() -> leadService.deleteLead(leadUUID));

    verify(leadRepository, times(1)).delete(leadDomain);
  }

  @DisplayName("Should PUT a lead")
  @Test
  void shouldPutALead() {
    when(modelMapper.map(putLead, LeadDomain.class)).thenReturn(leadDomain);
    when(leadRepository.findById(leadUUID)).thenReturn(Optional.of(leadDomain));

    assertDoesNotThrow(() -> leadService.putLead(leadUUID, putLead));

    verify(leadRepository, times(1)).save(leadDomain);
  }

  @DisplayName("Must throw EmailAlreadyExistException when trying to put an intern "
      + "with an existing email")
  @Test
  void mustThrowEmailAlreadyExistExceptionWhenTryingToPutInternWithExistingEmail() {
    when(leadRepository.findById(leadUUID)).thenReturn(Optional.of(leadDomain));
    when(leadRepository.findByEmail(putLead.getEmail())).thenReturn(Optional.of(leadDomain));

    var response = assertThrows(
        EmailAlreadyExistException.class,
        () -> leadService.putLead(leadUUID, putLead));

    verify(leadRepository, times(0)).save(leadDomain);

    var errorMessage = "Email already in use";

    assertEquals(errorMessage, response.getMessage());
  }

}