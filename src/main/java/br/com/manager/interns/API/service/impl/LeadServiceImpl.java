package br.com.manager.interns.API.service.impl;

import br.com.manager.interns.API.domains.InternsDomain;
import br.com.manager.interns.API.domains.LeadDomain;
import br.com.manager.interns.API.interfaces.dto.InternsDTO;
import br.com.manager.interns.API.interfaces.dto.LeadDTO;
import br.com.manager.interns.API.interfaces.dto.PutLeadDTO;
import br.com.manager.interns.API.interfaces.dto.ResponseInterns;
import br.com.manager.interns.API.interfaces.dto.ResponseLead;
import br.com.manager.interns.API.mapper.ResponseInternsMapper;
import br.com.manager.interns.API.mapper.ResponseLeadMapper;
import br.com.manager.interns.API.repository.InternsRepository;
import br.com.manager.interns.API.repository.LeadRepository;
import br.com.manager.interns.API.service.LeadService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@CacheConfig(cacheNames = {"buddys", "interns", "leads"}, keyGenerator = "CustomKeyGenerator")
public class LeadServiceImpl implements LeadService {

  private ModelMapper modelMapper = new ModelMapper();

  @Autowired
  private LeadRepository leadRepository;

  @Override
  @Transactional
  @CacheEvict(allEntries = true)
  public void postLead(LeadDTO leadDTO) {
    checkIfEmailIsUnique(leadDTO.getEmail());

    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    var leadDomain = modelMapper.map(leadDTO, LeadDomain.class);

    leadRepository.save(leadDomain);
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(unless = "#result.isEmpty()")
  public Page<ResponseLead> getAllLead(Pageable pageable, List<String> expand) {
    return this.leadRepository.findAll(pageable).map(lead ->
        ResponseLeadMapper.convertToLeadResponse(lead, expand));
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable()
  public ResponseLead getLeadById(UUID leadId, List<String> expand) {
    return this.leadRepository.findById(leadId).stream()
        .map(lead -> ResponseLeadMapper.convertToLeadResponse(lead, expand))
        .findFirst().orElseThrow(RuntimeException::new);
  }

  @Override
  @Transactional
  @CacheEvict(allEntries = true)
  public void deleteLead(UUID leadId) {
    var lead = findLeadById(leadId);

    leadRepository.delete(lead);
  }

  @Override
  @Transactional
  @CacheEvict(allEntries = true)
  public void putLead(UUID leadId, PutLeadDTO putLeadDTO) {
    var mappedLead = modelMapper.map(putLeadDTO, LeadDomain.class);
    var lead = findLeadById(leadId);

    if (!lead.getEmail().equals(mappedLead.getEmail())) {
      checkIfEmailIsUnique(mappedLead.getEmail());
    }

    mappedLead.setId(lead.getId());

    leadRepository.save(mappedLead);
  }

  private void checkIfEmailIsUnique(String email) {

    var emailCadastrado = this.leadRepository
        .findByEmail(email).isPresent();

    if (emailCadastrado) {
      throw new RuntimeException("Email já cadastrado");

    }
  }

  private LeadDomain findLeadById(UUID leadId) {
    return leadRepository.findById(leadId).orElseThrow(RuntimeException::new);
  }

}
