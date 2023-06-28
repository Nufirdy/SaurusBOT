package ent.otego.saurus.core.mappers;

import ent.otego.saurus.core.model.CampaignMedia;
import org.mapstruct.Mapper;

public interface CampaignMediaMapper {

    CampaignMedia campaignMediaToCampaignMediaEntity(
            ent.otego.saurus.nadeo.model.campaign.CampaignMedia campaignMedia);
}
