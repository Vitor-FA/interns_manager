package br.com.manager.interns.API.service.impl;

import br.com.manager.interns.API.domains.BuddysDomain;
import br.com.manager.interns.API.interfaces.dto.BuddysDTO;
import br.com.manager.interns.API.interfaces.dto.ResponseBuddys;
import br.com.manager.interns.API.service.BuddysService;
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
public class BuddysServiceImpl  implements BuddysService {

  @Override
  public void postBuddys(BuddysDTO buddysDTO) {

  }

  @Override
  public Page<ResponseBuddys> getAllBuddys(Pageable pageable, List<String> expand) {
    return null;
  }

  @Override
  public BuddysDomain getBuddysById(UUID buddyId) {
    return null;
  }

  @Override
  public void deleteBuddys(UUID buddyId) {

  }

  @Override
  public void patchBuddys(UUID buddyId, BuddysDTO buddysDTO) {

  }

  @Override
  public void putBuddys(UUID buddyId, BuddysDTO buddysDTO) {

  }
}
