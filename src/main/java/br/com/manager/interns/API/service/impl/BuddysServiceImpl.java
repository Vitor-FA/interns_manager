package br.com.manager.interns.API.service.impl;

import br.com.manager.interns.API.domains.BuddysDomain;
import br.com.manager.interns.API.domains.InternsDomain;
import br.com.manager.interns.API.interfaces.dto.BuddysDTO;
import br.com.manager.interns.API.interfaces.dto.PutBuddysDTO;
import br.com.manager.interns.API.interfaces.dto.ResponseBuddys;
import br.com.manager.interns.API.mapper.ResponseBuddysMapper;
import br.com.manager.interns.API.repository.BuddysRepository;
import br.com.manager.interns.API.service.BuddysService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class BuddysServiceImpl  implements BuddysService {

  private ModelMapper modelMapper = new ModelMapper();

  @Autowired
  private BuddysRepository buddysRepository;

  @Override
  public void postBuddys(BuddysDTO buddysDTO) {
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    var buddyDomain = modelMapper.map(buddysDTO, BuddysDomain.class);

    buddysRepository.save(buddyDomain);
  }

  @Override
  public Page<ResponseBuddys> getAllBuddys(Pageable pageable, List<String> expand) {
    return this.buddysRepository.findAll(pageable).map(buddys ->
        ResponseBuddysMapper.convertToBuddysResponse(buddys, expand));
  }

  @Override
  public ResponseBuddys getBuddysById(UUID buddyId, List<String> expand) {
    return this.buddysRepository.findById(buddyId).stream()
        .map(buddys -> ResponseBuddysMapper.convertToBuddysResponse(buddys, expand))
        .findFirst().orElseThrow(RuntimeException::new);
  }

  @Override
  public void deleteBuddys(UUID buddyId) {
    var buddys = findBuddyById(buddyId);

    buddysRepository.delete(buddys);
  }

  @Override
  public void putBuddys(UUID buddyId, PutBuddysDTO putBuddysDTO) {
    var mappedBuddy = modelMapper.map(putBuddysDTO, BuddysDomain.class);
    var buddy = findBuddyById(buddyId);

    if (!buddy.getEmail().equals(mappedBuddy.getEmail())) {
      checkIfEmailIsUnique(mappedBuddy.getEmail());
    }

    mappedBuddy.setId(buddy.getId());

    buddysRepository.save(mappedBuddy);
  }

  private void checkIfEmailIsUnique(String email) {

    var emailCadastrado = this.buddysRepository
        .findByEmail(email).isPresent();

    if (emailCadastrado) {
      throw new RuntimeException("Email já cadastrado");

    }
  }

  private BuddysDomain findBuddyById(UUID buddyId) {
    return buddysRepository.findById(buddyId).orElseThrow(RuntimeException::new);
  }
}
