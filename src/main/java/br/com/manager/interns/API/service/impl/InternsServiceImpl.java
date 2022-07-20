package br.com.manager.interns.API.service.impl;

import br.com.manager.interns.API.domains.InternsDomain;
import br.com.manager.interns.API.interfaces.dto.InternsDTO;
import br.com.manager.interns.API.interfaces.dto.PutInternsDTO;
import br.com.manager.interns.API.interfaces.dto.ResponseInterns;
import br.com.manager.interns.API.mapper.ResponseInternsMapper;
import br.com.manager.interns.API.repository.InternsRepository;
import br.com.manager.interns.API.service.InternsService;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.internal.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class InternsServiceImpl implements InternsService {

  private ModelMapper modelMapper = new ModelMapper();

  @Autowired
  private InternsRepository internsRepository;

  @Override
  public void postInterns(InternsDTO internsDTO) {
    checkIfEmailIsUnique(internsDTO.getEmail());

    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    var internDomain = modelMapper.map(internsDTO, InternsDomain.class);

    internsRepository.save(internDomain);
  }

  @Override
  public Page<ResponseInterns> getAllInterns(Pageable pageable, List<String> expand) {
    return this.internsRepository.findAll(pageable).map(interns ->
        ResponseInternsMapper.convertToInternsResponse(interns, expand));
  }

  @Override
  public ResponseInterns getInternsById(UUID internId, List<String> expand) {
    return this.internsRepository.findById(internId).stream()
        .map(interns -> ResponseInternsMapper.convertToInternsResponse(interns, expand))
        .findFirst().orElseThrow(RuntimeException::new);
  }

  @Override
  public void deleteInterns(UUID internId) {
    var intern = findInternById(internId);

    internsRepository.delete(intern);
  }

  @Override
  public void putInterns(UUID internId, PutInternsDTO putInternsDTO) {
    var mappedIntern = modelMapper.map(putInternsDTO, InternsDomain.class);
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
      throw new RuntimeException("Email já cadastrado");

    }
  }

  private InternsDomain findInternById(UUID internId) {
    return internsRepository.findById(internId).orElseThrow(RuntimeException::new);
  }
}
