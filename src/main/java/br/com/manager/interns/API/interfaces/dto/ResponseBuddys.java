package br.com.manager.interns.API.interfaces.dto;

import br.com.manager.interns.API.domains.InternsDomain;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class ResponseBuddys extends BuddysDTO{

  private List<InternsDomain> interns;

}
