package br.com.manager.interns.API.mapper;

import br.com.manager.interns.API.domains.BuddysDomain;
import br.com.manager.interns.API.domains.InternsDomain;
import br.com.manager.interns.API.domains.LeadDomain;
import br.com.manager.interns.API.enums.InternsEnum;
import br.com.manager.interns.API.interfaces.dto.ResponseInterns;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@NoArgsConstructor
@Slf4j
public class ResponseInternsMapper {

  private static ResponseInterns internsSkipExpand(InternsDomain interns) {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    PropertyMap<InternsDomain, ResponseInterns> internsResponse = new PropertyMap<
        InternsDomain, ResponseInterns>() {
      @Override
      protected void configure() {
        skip(destination.getBuddys());
        skip(destination.getLead());
      }
    };

   modelMapper.addMappings(internsResponse);

   return modelMapper.map(interns, ResponseInterns.class);
  }

  private static ResponseInterns getExpandedInternsObject(
      InternsEnum keyword, InternsDomain interns, ResponseInterns responseInterns
  ) {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    if (keyword.equals(InternsEnum.BUDDYS)) {
      responseInterns.setBuddys(interns.getBuddys());
    }

    if (keyword.equals(InternsEnum.LEAD)) {
      responseInterns.setLead(interns.getLead());
    }

    return responseInterns;
  }

  public static ResponseInterns convertToInternsResponse(
      InternsDomain interns, List<String> expand
  ) {
    ResponseInterns responseInterns = internsSkipExpand(interns);

    var expandsInterns = Arrays.stream(InternsEnum.values())
        .map(Enum::name)
        .toList();

    if (!ObjectUtils.isEmpty(expand)) {
      expand.stream()
          .filter(expandsInterns::contains)
          .forEach(key -> {
            var keywordsExpanding =
                !ObjectUtils.isEmpty(expand) ? Enum.valueOf(InternsEnum.class, key) : null;

            getExpandedInternsObject(keywordsExpanding, interns, responseInterns);
          });
    }

    return responseInterns;
  }

}
