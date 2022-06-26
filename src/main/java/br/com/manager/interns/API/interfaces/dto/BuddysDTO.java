package br.com.manager.interns.API.interfaces.dto;

import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BuddysDTO {

  @Size(max = 90)
  private String name;

  @Size(max = 255)
  private String email;

}
