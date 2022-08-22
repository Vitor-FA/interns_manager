package br.com.manager.interns.API.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.manager.interns.API.domains.BuddysDomain;
import br.com.manager.interns.API.domains.CreatedAndNotCreatedObjects;
import br.com.manager.interns.API.domains.InternsDomain;
import br.com.manager.interns.API.exception.AlreadyAssociatedException;
import br.com.manager.interns.API.exception.AssociationNotFoundException;
import br.com.manager.interns.API.exception.ResourceNotFoundException;
import br.com.manager.interns.API.repository.BuddysRepository;
import br.com.manager.interns.API.repository.InternsRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
class BuddysInternsServiceImplTest {

  @InjectMocks
  private BuddysInternsServiceImpl buddysInternsService;

  @Mock
  private BuddysRepository buddysRepository;

  @Mock
  private InternsRepository internsRepository;

  private BuddysDomain buddysDomain;

  private InternsDomain internsDomain;

  private BuddysDomain buddyWithRelationship;

  private InternsDomain internWithRelationship;

  private final UUID internsUUID = UUID.randomUUID();

  private final UUID buddysUUID = UUID.randomUUID();

  @BeforeEach
  public void beforeEach() {
    internsDomain = InternsDomain.builder()
        .id(UUID.randomUUID())
        .name("Test")
        .email("Test@email.com")
        .buddys(new ArrayList<>())
        .build();

    buddysDomain = BuddysDomain.builder()
        .id(UUID.randomUUID())
        .name("Test")
        .email("Test@email.com")
        .interns(new ArrayList<>())
        .build();

    internWithRelationship = InternsDomain.builder()
        .id(UUID.randomUUID())
        .name("Test")
        .email("Test@email.com")
        .buddys(new ArrayList<>())
        .build();

    buddyWithRelationship = BuddysDomain.builder()
        .id(UUID.randomUUID())
        .name("Test")
        .email("Test@email.com")
        .interns(new ArrayList<>())
        .build();
  }

  @DisplayName("Must create a relationship between interns and buddys")
  @Test
  void mustCreateRelationshipBetweenInternsAndBuddys() {
    when(buddysRepository.findById(buddysUUID)).thenReturn(Optional.of(buddysDomain));
    when(internsRepository.findById(internsUUID)).thenReturn(Optional.of(internsDomain));

    var createdAndNotCreatedObjects =
        CreatedAndNotCreatedObjects.getCreatedAndNotCreatedObjectsBuilded(
        List.of(),
        List.of(internsDomain)
    );

    var response =
        assertDoesNotThrow(() -> buddysInternsService.createBuddyIntern(
            buddysUUID, List.of(internsUUID)));

    verify(buddysRepository, times(1))
        .save(buddysDomain);

    assertEquals(createdAndNotCreatedObjects, response.getBody());
    assertEquals(buddysDomain.getInterns(), List.of(internsDomain));

  }

  @DisplayName("Must Throw AlreadyAssociatedException when they are already associated")
  @Test
  void mustThrowAlreadyAssociatedExceptionWhenAlreadyAssociated() {
    internWithRelationship.addBuddy(buddyWithRelationship);
    buddyWithRelationship.addInterns(List.of(internWithRelationship));
    when(buddysRepository.findById(buddysUUID)).thenReturn(Optional.of(buddyWithRelationship));
    when(internsRepository.findById(internsUUID)).thenReturn(Optional.of(internWithRelationship));

    var errorMessage = "Already exists association between Buddy and Intern";

    try {
      buddysInternsService.createBuddyIntern(buddysUUID, List.of(internsUUID));
    } catch (AlreadyAssociatedException e) {
      verify(buddysRepository, times(0)).save(buddysDomain);

      assertEquals(e.getMessage(), errorMessage);
    }
  }

  @DisplayName("Must delete a relationship between buddy and intern successfully")
  @Test
  void mustDeleteRelationshipSuccessfully() {
    internWithRelationship.addBuddy(buddyWithRelationship);
    buddyWithRelationship.addInterns(List.of(internWithRelationship));
    when(buddysRepository.findById(buddysUUID)).thenReturn(Optional.of(buddyWithRelationship));
    when(internsRepository.findById(internsUUID)).thenReturn(Optional.of(internWithRelationship));

    var response =
        assertDoesNotThrow(() -> buddysInternsService.deleteBuddyIntern(
            buddysUUID, List.of(internsUUID)));

    verify(buddysRepository, times(1))
        .save(buddyWithRelationship);

    assertEquals(
        Objects.requireNonNull(response.getBody()).getDeleted(), List.of(internWithRelationship));
  }

  @DisplayName("Must throw AssociationNotFoundException when deleting an association that does not exist.")
  @Test
  void mustThrowAssociationNotFoundExceptionWhenDeletingAnAssociationThatDoesNotExist() {
    when(buddysRepository.findById(buddysUUID)).thenReturn(Optional.of(buddysDomain));
    when(internsRepository.findById(internsUUID)).thenReturn(Optional.of(internsDomain));

    var errorMessage = "Association between Buddy and Intern was not found";

    try {
      buddysInternsService.deleteBuddyIntern(buddysUUID, List.of(internsUUID));
    } catch (AssociationNotFoundException e) {
      verify(buddysRepository, times(0)).save(buddysDomain);

      assertEquals(e.getMessage(), errorMessage);
    }
  }

  @DisplayName("Must throw ResourceNotFoundException when buddy is not found")
  @Test
  void mustThrowResourceNotFoundExceptionWhenBuddyIsNotFound() {
    try {
      buddysInternsService.deleteBuddyIntern(
          buddysUUID, List.of(internsUUID));
    } catch (ResourceNotFoundException e) {
      verify(buddysRepository, times(0))
          .save(buddyWithRelationship);

      var errorMessage = "Buddy with this ID was not found";

      assertEquals(e.getMessage(), errorMessage);
    }
  }
}
