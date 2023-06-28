package ent.otego.saurus.core.mappers;

import ent.otego.saurus.core.model.Campaign;
import ent.otego.saurus.nadeo.model.MapInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
public interface CampaignMapper {

    @Mapping(target = "playlist", ignore = true)
    @Mapping(target = "media", dependsOn = "id")
    Campaign campaignToCampaignEntity(ent.otego.saurus.nadeo.model.campaign.Campaign campaign);
}
