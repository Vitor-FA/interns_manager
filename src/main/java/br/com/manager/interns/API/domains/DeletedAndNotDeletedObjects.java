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
public class DeletedAndNotDeletedObjects {

  List<UUID> notDeleted;

  List<?> deleted;

  public static DeletedAndNotDeletedObjects getDeletedAndNotDeletedObjects(
      List<UUID> notFoundInternsList,
      List<InternsDomain> foundInternsList) {
    return DeletedAndNotDeletedObjects.builder()
        .deleted(foundInternsList)
        .notDeleted(notFoundInternsList)
        .build();
  }

}
