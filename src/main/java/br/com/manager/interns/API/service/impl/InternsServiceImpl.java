package br.com.manager.interns.API.service.impl;

import br.com.manager.interns.API.domains.InternsDomain;
import br.com.manager.interns.API.exception.EmailAlreadyExistException;
import br.com.manager.interns.API.exception.ResourceNotFoundException;
import br.com.manager.interns.API.interfaces.dto.PostInterns;
import br.com.manager.interns.API.interfaces.dto.PutInterns;
import br.com.manager.interns.API.interfaces.dto.ResponseInterns;
import br.com.manager.interns.API.mapper.ResponseInternsMapper;
import br.com.manager.interns.API.repository.InternsRepository;
import br.com.manager.interns.API.service.InternsService;
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
public class InternsServiceImpl implements InternsService {

  private ModelMapper modelMapper = new ModelMapper();

  @Autowired
  private InternsRepository internsRepository;

  @Override
  @Transactional
  @CacheEvict(allEntries = true)
  public void postInterns(PostInterns postInterns) {
    checkIfEmailIsUnique(postInterns.getEmail());

    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    var internDomain = modelMapper.map(postInterns, InternsDomain.class);

    internsRepository.save(internDomain);
  }

  @Override
  @Transactional
  @Cacheable(unless = "#result.isEmpty()")
  public Page<ResponseInterns> getAllInterns(Pageable pageable, List<String> expand) {
    return this.internsRepository.findAll(pageable).map(interns ->
        ResponseInternsMapper.convertToInternsResponse(interns, expand));
  }

  @Override
  @Cacheable
  @Transactional
  public ResponseInterns getInternsById(UUID internId, List<String> expand) {
    return this.internsRepository.findById(internId).stream()
        .map(interns -> ResponseInternsMapper.convertToInternsResponse(interns, expand))
        .findFirst().orElseThrow(() -> new ResourceNotFoundException("Intern"));
  }

  @Override
  @Transactional
  @CacheEvict(allEntries = true)
  public void deleteInterns(UUID internId) {
    var intern = findInternById(internId);

    internsRepository.delete(intern);
  }

  @Override
  @Transactional
  @CacheEvict(allEntries = true)
  public void putInterns(UUID internId, PutInterns putInterns) {
    var mappedIntern = modelMapper.map(putInterns, InternsDomain.class);
    var intern = findInternById(internId);

    if (!intern.getEmail().equals(mappedIntern.getEmail())) {
      checkIfEmailIsUnique(mappedIntern.getEmail());
    }

    mappedIntern.setId(intern.getId());

    internsRepository.save(mappedIntern);
  }

  private void checkIfEmailIsUnique(String email) {

    var emailCadastrado = this.internsRepository
        .findByEmail(email).isPresent();

    if (emailCadastrado) {
      throw new EmailAlreadyExistException();

    }
  }

  private InternsDomain findInternById(UUID internId) {
    return internsRepository.findById(internId).orElseThrow(() ->
        new ResourceNotFoundException("Intern"));
  }
}
