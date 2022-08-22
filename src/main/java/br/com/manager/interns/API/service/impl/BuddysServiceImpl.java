package br.com.manager.interns.API.service.impl;

import br.com.manager.interns.API.domains.BuddysDomain;
import br.com.manager.interns.API.exception.EmailAlreadyExistException;
import br.com.manager.interns.API.exception.ResourceNotFoundException;
import br.com.manager.interns.API.interfaces.dto.PostBuddys;
import br.com.manager.interns.API.interfaces.dto.PutBuddys;
import br.com.manager.interns.API.interfaces.dto.ResponseBuddys;
import br.com.manager.interns.API.mapper.ResponseBuddysMapper;
import br.com.manager.interns.API.repository.BuddysRepository;
import br.com.manager.interns.API.service.BuddysService;
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
public class BuddysServiceImpl  implements BuddysService {

  private ModelMapper modelMapper = new ModelMapper();

  @Autowired
  private BuddysRepository buddysRepository;

  @Override
  @Transactional
  @CacheEvict(allEntries = true)
  public void postBuddys(PostBuddys postBuddys) {
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    checkIfEmailIsUnique(postBuddys.getEmail());

    var buddyDomain = modelMapper.map(postBuddys, BuddysDomain.class);

    buddysRepository.save(buddyDomain);
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(unless = "#result.isEmpty()")
  public Page<ResponseBuddys> getAllBuddys(Pageable pageable, List<String> expand) {
    return this.buddysRepository.findAll(pageable).map(buddys ->
        ResponseBuddysMapper.convertToBuddysResponse(buddys, expand));
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable
  public ResponseBuddys getBuddysById(UUID buddyId, List<String> expand) {
    return this.buddysRepository.findById(buddyId).stream()
        .map(buddys -> ResponseBuddysMapper.convertToBuddysResponse(buddys, expand))
        .findFirst().orElseThrow(() -> new ResourceNotFoundException("Buddy"));
  }

  @Override
  @Transactional
  @CacheEvict(allEntries = true)
  public void deleteBuddys(UUID buddyId) {
    var buddys = findBuddyById(buddyId);

    buddysRepository.delete(buddys);
  }

  @Override
  @Transactional
  @CacheEvict(allEntries = true)
  public void putBuddys(UUID buddyId, PutBuddys putBuddys) {
    var mappedBuddy = modelMapper.map(putBuddys, BuddysDomain.class);
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
      throw new EmailAlreadyExistException();

    }
  }

  private BuddysDomain findBuddyById(UUID buddyId) {
    return buddysRepository.findById(buddyId).orElseThrow(() -> new ResourceNotFoundException("Buddy"));
  }
}
