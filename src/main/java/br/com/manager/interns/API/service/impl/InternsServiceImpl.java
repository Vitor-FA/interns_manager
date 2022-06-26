package br.com.manager.interns.API.service.impl;

import br.com.manager.interns.API.domains.InternsDomain;
import br.com.manager.interns.API.interfaces.dto.InternsDTO;
import br.com.manager.interns.API.interfaces.dto.ResponseInterns;
import br.com.manager.interns.API.service.InternsService;
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
public class InternsServiceImpl implements InternsService {

  @Override
  public void postInterns(InternsDTO internsDTO) {

  }

  @Override
  public Page<ResponseInterns> getAllInterns(Pageable pageable, List<String> expand) {
    return null;
  }

  @Override
  public InternsDomain getInternsById(UUID internId) {
    return null;
  }

  @Override
  public void deleteInterns(UUID internId) {

  }

  @Override
  public void patchInterns(UUID internId, InternsDTO internsDTO) {

  }

  @Override
  public void putInterns(UUID internId, InternsDTO internsDTO) {

  }
}
