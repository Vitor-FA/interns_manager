package br.com.manager.interns.API.service.impl;

import br.com.manager.interns.API.domains.InternsDomain;
import br.com.manager.interns.API.domains.LeadDomain;
import br.com.manager.interns.API.interfaces.dto.InternsDTO;
import br.com.manager.interns.API.interfaces.dto.LeadDTO;
import br.com.manager.interns.API.interfaces.dto.ResponseInterns;
import br.com.manager.interns.API.interfaces.dto.ResponseLead;
import br.com.manager.interns.API.service.LeadService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@SuperBuilder
@AllArgsConstructor
@Slf4j
public class LeadServiceImpl implements LeadService {

  @Override
  public void postLead(LeadDTO leadDTO) {

  }

  @Override
  public Page<ResponseLead> getAllLead(Pageable pageable, List<String> expand) {
    return null;
  }

  @Override
  public LeadDomain getLeadById(UUID leadId) {
    return null;
  }

  @Override
  public void deleteLead(UUID leadId) {

  }

  @Override
  public void patchLead(UUID leadId, LeadDTO leadDTO) {

  }

  @Override
  public void putLead(UUID leadId, LeadDTO leadDTO) {

  }

}
