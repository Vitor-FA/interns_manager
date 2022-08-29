package br.com.manager.interns.API.service;

import br.com.manager.interns.API.domains.InternsDomain;
import br.com.manager.interns.API.interfaces.dto.PostInterns;
import br.com.manager.interns.API.interfaces.dto.PutInterns;
import br.com.manager.interns.API.interfaces.dto.ResponseInterns;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InternsService {

  InternsDomain postInterns(PostInterns postInterns);

  Page<ResponseInterns> getAllInterns(Pageable pageable, List<String> expand);

  ResponseInterns getInternsById(UUID internId, List<String> expand);

  void deleteInterns(UUID internId);

  void putInterns(UUID internId, PutInterns internsDTO);

}
