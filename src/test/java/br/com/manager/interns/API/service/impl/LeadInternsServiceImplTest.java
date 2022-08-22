package br.com.manager.interns.API.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.manager.interns.API.domains.CreatedAndNotCreatedObjects;
import br.com.manager.interns.API.domains.InternsDomain;
import br.com.manager.interns.API.domains.LeadDomain;
import br.com.manager.interns.API.exception.AlreadyAssociatedException;
import br.com.manager.interns.API.exception.AssociationNotFoundException;
import br.com.manager.interns.API.repository.InternsRepository;
import br.com.manager.interns.API.repository.LeadRepository;
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
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeadInternsServiceImplTest {

  @InjectMocks
  private LeadInternsServiceImpl leadInternsService;

  @Mock
  private LeadRepository leadRepository;

  @Mock
  private InternsRepository internsRepository;

  private LeadDomain leadDomain;

  private InternsDomain internsDomain;

  private LeadDomain leadWithRelationship;

  private InternsDomain internWithRelationship;

  private final UUID internsUUID = UUID.randomUUID();

  private final UUID leadUUID = UUID.randomUUID();

  @BeforeEach
  private void beforeEach() {
    internsDomain = InternsDomain.builder()
        .id(UUID.randomUUID())
        .name("Test")
        .email("Test@email.com")
        .buddys(new ArrayList<>())
        .build();

    leadDomain = LeadDomain.builder()
        .id(UUID.randomUUID())
        .name("Test")
        .email("Test@email.com")
        .interns(new ArrayList<>())
        .build();

    internWithRelationship = InternsDomain.builder()
        .id(UUID.randomUUID())
        .name("Test")
        .email("Test@email.com")
        .lead(leadDomain)
        .buddys(new ArrayList<>())
        .build();

    leadWithRelationship = LeadDomain.builder()
        .id(UUID.randomUUID())
        .name("Test")
        .email("Test@email.com")
        .interns(new ArrayList<>())
        .build();
  }

  @DisplayName("Must create a relationship between interns and lead")
  @Test
  void mustCreateRelationshipBetweenInternsAndLead() {
    when(leadRepository.findById(leadUUID)).thenReturn(Optional.of(leadDomain));
    when(internsRepository.findById(internsUUID)).thenReturn(Optional.of(internsDomain));

    var createdAndNotCreatedObjects =
        CreatedAndNotCreatedObjects.getCreatedAndNotCreatedObjectsBuilded(
            List.of(),
            List.of(internsDomain)
        );

    var response =
        assertDoesNotThrow(() -> leadInternsService.createLeadIntern(
            leadUUID, List.of(internsUUID)));

    verify(leadRepository, times(1))
        .save(leadDomain);

    verify(internsRepository, times(1))
        .saveAll(List.of(internsDomain));

    assertEquals(createdAndNotCreatedObjects, response.getBody());
    assertEquals(leadDomain.getInterns(), List.of(internsDomain));

  }

  @DisplayName("Must Throw AlreadyAssociatedException when they are already associated")
  @Test
  void mustThrowAlreadyAssociatedExceptionWhenAlreadyAssociated() {
    leadWithRelationship.addInterns(List.of(internWithRelationship));
    when(leadRepository.findById(leadUUID)).thenReturn(Optional.of(leadWithRelationship));
    when(internsRepository.findById(internsUUID)).thenReturn(Optional.of(internWithRelationship));

    var erroMessage = "Already exists association between Lead and Intern";
    var internsUUIDList = List.of(internsUUID);

    var response =
        assertThrows(AlreadyAssociatedException.class, () -> leadInternsService.createLeadIntern(
            leadUUID, internsUUIDList));

    verify(leadRepository, times(0))
        .save(leadWithRelationship);
    verify(internsRepository, times(0))
        .saveAll(List.of(internWithRelationship));

    assertEquals(response.getMessage(), erroMessage);
  }

  @DisplayName("Must delete a relationship between lead and intern successfully")
  @Test
  void mustDeleteRelationshipSuccessfully() {
    leadWithRelationship.addInterns(List.of(internWithRelationship));
    when(leadRepository.findById(leadUUID)).thenReturn(Optional.of(leadWithRelationship));
    when(internsRepository.findById(internsUUID)).thenReturn(Optional.of(internWithRelationship));

    var response =
        assertDoesNotThrow(() -> leadInternsService.deleteLeadIntern(
            leadUUID, List.of(internsUUID)));

    verify(leadRepository, times(1))
        .save(leadWithRelationship);
    verify(internsRepository, times(1))
        .saveAll(List.of(internWithRelationship));

    assertEquals(response.getBody().getDeleted(), List.of(internWithRelationship));
  }

  @DisplayName("Must throw AssociationNotFoundException when deleting an association that does not exist.")
  @Test
  void mustThrowAssociationNotFoundExceptionWhenDeletingAnAssociationThatDoesNotExist() {
    when(leadRepository.findById(leadUUID)).thenReturn(Optional.of(leadDomain));
    when(internsRepository.findById(internsUUID)).thenReturn(Optional.of(internsDomain));

    var internsUUIDList = List.of(internsUUID);

    var response =
        assertThrows(AssociationNotFoundException.class, () -> leadInternsService.deleteLeadIntern(
            leadUUID, internsUUIDList));

    verify(leadRepository, times(0))
        .save(leadDomain);
    verify(internsRepository, times(0))
        .saveAll(List.of(internWithRelationship));

    var errorMessage = "Association between Lead and Intern was not found";

    assertEquals(response.getMessage(), errorMessage);
  }
}
