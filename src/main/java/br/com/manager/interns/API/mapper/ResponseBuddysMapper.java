package br.com.manager.interns.API.mapper;

import br.com.manager.interns.API.domains.BuddysDomain;
import br.com.manager.interns.API.enums.BuddysEnum;
import br.com.manager.interns.API.interfaces.dto.ResponseBuddys;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ResponseBuddysMapper {
  private static ResponseBuddys buddysSkipExpand(BuddysDomain buddys) {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    PropertyMap<BuddysDomain, ResponseBuddys> buddysResponse = new PropertyMap<>() {
      @Override
      protected void configure() {
        skip(destination.getInterns());
      }
    };

    modelMapper.addMappings(buddysResponse);

    return modelMapper.map(buddys, ResponseBuddys.class);
  }

  private static ResponseBuddys getExpandedBuddysObject(
      BuddysEnum keyword, BuddysDomain buddys, ResponseBuddys responseBuddys
  ) {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    if (keyword.equals(BuddysEnum.INTERNS)) {
      responseBuddys.setInterns(buddys.getInterns());
    }

    return responseBuddys;
  }

  public static ResponseBuddys convertToBuddysResponse(
      BuddysDomain buddys, List<String> expand
  ) {
    ResponseBuddys responseBuddys = buddysSkipExpand(buddys);

    var expandsBuddys = Arrays.stream(BuddysEnum.values())
        .map(Enum::name)
        .toList();

    if (!ObjectUtils.isEmpty(expand)) {
      expand.stream()
          .filter(expandsBuddys::contains)
          .forEach(key -> {
            var keywordsExpanding =
                !ObjectUtils.isEmpty(expand) ? Enum.valueOf(BuddysEnum.class, key) : null;

            getExpandedBuddysObject(keywordsExpanding, buddys, responseBuddys);
          });
    }

    return responseBuddys;
  }
}
