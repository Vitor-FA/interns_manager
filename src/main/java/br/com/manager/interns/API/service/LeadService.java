package br.com.manager.interns.API.service;

import br.com.manager.interns.API.domains.InternsDomain;
import br.com.manager.interns.API.domains.LeadDomain;
import br.com.manager.interns.API.interfaces.dto.InternsDTO;
import br.com.manager.interns.API.interfaces.dto.LeadDTO;
import br.com.manager.interns.API.interfaces.dto.ResponseInterns;
import br.com.manager.interns.API.interfaces.dto.ResponseLead;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LeadService {

  void postLead(LeadDTO leadDTO);

  Page<ResponseLead> getAllLead(Pageable pageable, List<String> expand);

  LeadDomain getLeadById(UUID leadId);

  void deleteLead(UUID leadId);

  void patchLead(UUID leadId, LeadDTO leadDTO);

  void putLead(UUID leadId, LeadDTO leadDTO);
}
