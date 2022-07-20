package br.com.manager.interns.API.interfaces.dto;

import br.com.manager.interns.API.domains.InternsDomain;
import br.com.manager.interns.API.interfaces.commun.BuddysCommon;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@JsonInclude(Include.NON_NULL)
public class ResponseBuddys extends BuddysCommon {

  private List<InternsDomain> interns;

}
