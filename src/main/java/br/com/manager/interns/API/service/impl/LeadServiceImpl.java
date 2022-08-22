package br.com.manager.interns.API.service.impl;

import br.com.manager.interns.API.domains.LeadDomain;
import br.com.manager.interns.API.exception.EmailAlreadyExistException;
import br.com.manager.interns.API.exception.ResourceNotFoundException;
import br.com.manager.interns.API.interfaces.dto.PostLead;
import br.com.manager.interns.API.interfaces.dto.PutLead;
import br.com.manager.interns.API.interfaces.dto.ResponseLead;
import br.com.manager.interns.API.mapper.ResponseLeadMapper;
import br.com.manager.interns.API.repository.LeadRepository;
import br.com.manager.interns.API.service.LeadService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Slf4j
@CacheConfig(cacheNames = {"buddys", "interns", "leads"}, keyGenerator = "CustomKeyGenerator")
public class LeadServiceImpl implements LeadService {

  private ModelMapper modelMapper = new ModelMapper();

  @Autowired
  private LeadRepository leadRepository;

  @Override
  @Transactional
  @CacheEvict(allEntries = true)
  public void postLead(PostLead postLead) {
    checkIfEmailIsUnique(postLead.getEmail());

    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    var leadDomain = modelMapper.map(postLead, LeadDomain.class);

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
        .findFirst().orElseThrow(() -> new ResourceNotFoundException("Lead"));
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
  public void putLead(UUID leadId, PutLead putLead) {
    var mappedLead = modelMapper.map(putLead, LeadDomain.class);
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
      throw new EmailAlreadyExistException();

    }
  }

  private LeadDomain findLeadById(UUID leadId) {
    return leadRepository.findById(leadId).orElseThrow(() -> new ResourceNotFoundException("Lead"));
  }

}
