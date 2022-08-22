package br.com.manager.interns.API.mapper;

import br.com.manager.interns.API.domains.LeadDomain;
import br.com.manager.interns.API.enums.LeadEnum;
import br.com.manager.interns.API.interfaces.dto.ResponseLead;
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
public class ResponseLeadMapper {

  private static ResponseLead leadSkipExpand(LeadDomain lead) {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    PropertyMap<LeadDomain, ResponseLead> leadResponse = new PropertyMap<LeadDomain, ResponseLead>() {
      @Override
      protected void configure() {
        skip(destination.getInterns());
      }
    };

    modelMapper.addMappings(leadResponse);

    return modelMapper.map(lead, ResponseLead.class);
  }

  private static ResponseLead getExpandedInternsObject(
      LeadEnum keyword, LeadDomain lead, ResponseLead responseLead
  ) {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    if (keyword.equals(LeadEnum.INTERNS)) {
      responseLead.setInterns(lead.getInterns());
    }

    return responseLead;
  }

  public static ResponseLead convertToLeadResponse(LeadDomain lead, List<String> expand) {
    ResponseLead responseLead = leadSkipExpand(lead);

    var expandLead = Arrays.stream(LeadEnum.values())
        .map(Enum::name)
        .toList();

    if (!ObjectUtils.isEmpty(expand)) {
      expand.stream()
          .filter(expandLead::contains)
          .forEach(key -> {
            var keywordsExpanding =
                !ObjectUtils.isEmpty(expand) ? Enum.valueOf(LeadEnum.class, key) : null;

            getExpandedInternsObject(keywordsExpanding, lead, responseLead);
          });
    }

    return responseLead;
  }
}
