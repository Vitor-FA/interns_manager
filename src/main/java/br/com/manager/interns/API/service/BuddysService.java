package br.com.manager.interns.API.service;

import br.com.manager.interns.API.domains.BuddysDomain;
import br.com.manager.interns.API.interfaces.dto.PostBuddys;
import br.com.manager.interns.API.interfaces.dto.PutBuddys;
import br.com.manager.interns.API.interfaces.dto.ResponseBuddys;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BuddysService {

  BuddysDomain postBuddys(PostBuddys postBuddys);

  Page<ResponseBuddys> getAllBuddys(Pageable pageable, List<String> expand);

  ResponseBuddys getBuddysById(UUID buddyId, List<String> expand);

  void deleteBuddys(UUID buddyId);

  void putBuddys(UUID buddyId, PutBuddys putBuddys);

}
