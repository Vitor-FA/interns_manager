package br.com.manager.interns.API.service;

import br.com.manager.interns.API.domains.InternsDomain;
import br.com.manager.interns.API.interfaces.dto.InternsDTO;
import br.com.manager.interns.API.interfaces.dto.ResponseInterns;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InternsService {

  void postInterns(InternsDTO internsDTO);

  Page<ResponseInterns> getAllInterns(Pageable pageable, List<String> expand);

  InternsDomain getInternsById(UUID internId);

  void deleteInterns(UUID internId);

  void patchInterns(UUID internId, InternsDTO internsDTO);

  void putInterns(UUID internId, InternsDTO internsDTO);

}
