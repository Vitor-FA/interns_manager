package br.com.manager.interns.API.domains;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreatedAndNotCreatedObjects {

  List<UUID> notCreated;

  List<?> created;

  public static CreatedAndNotCreatedObjects getCreatedAndNotCreatedObjectsBuilded(
      List<UUID> notFoundInternsList,
      List<InternsDomain> foundInternsList) {
    return CreatedAndNotCreatedObjects.builder()
        .created(foundInternsList)
        .notCreated(notFoundInternsList)
        .build();
  }

}
