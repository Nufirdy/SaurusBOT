package ent.otego.saurus.nadeo.model;

import ent.otego.saurus.nadeo.model.campaign.Campaign;

import java.util.Date;
import java.util.List;

public record CampaignResponse(Integer itemCount,
                               List<Campaign> campaignList,
                               Date nextRequestTimestamp,
                               Integer relativeNextRequest) {
}
