package br.com.manager.interns.API.service;

import br.com.manager.interns.API.domains.BuddysDomain;
import br.com.manager.interns.API.interfaces.dto.BuddysDTO;
import br.com.manager.interns.API.interfaces.dto.PutBuddysDTO;
import br.com.manager.interns.API.interfaces.dto.ResponseBuddys;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BuddysService {

  void postBuddys(BuddysDTO buddysDTO);

  Page<ResponseBuddys> getAllBuddys(Pageable pageable, List<String> expand);

  ResponseBuddys getBuddysById(UUID buddyId, List<String> expand);

  void deleteBuddys(UUID buddyId);

  void putBuddys(UUID buddyId, PutBuddysDTO putBuddysDTO);

}
