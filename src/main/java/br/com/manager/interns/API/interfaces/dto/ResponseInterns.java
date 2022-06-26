package br.com.manager.interns.API.interfaces.dto;

import br.com.manager.interns.API.domains.BuddysDomain;
import br.com.manager.interns.API.domains.LeadDomain;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class ResponseInterns extends InternsDTO{

  private List<BuddysDomain> buddys;

  private LeadDomain lead;

}
