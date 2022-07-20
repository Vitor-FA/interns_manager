package br.com.manager.interns.API.interfaces.commun;

import java.util.UUID;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class InternsCommon {

  private UUID id;

  @Size(max = 90)
  private String name;

  @Size(max = 255)
  private String email;

}
