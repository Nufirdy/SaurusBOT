package ent.otego.saurus.core.events;

import ent.otego.saurus.core.model.Campaign;
import org.springframework.context.ApplicationEvent;

public class NewCampaignEvent extends ApplicationEvent {

    private final Campaign newCampaign;

    public NewCampaignEvent(Campaign newCampaign) {
        super(newCampaign);
        this.newCampaign = newCampaign;
    }

    public Campaign getNewCampaign() {
        return newCampaign;
    }
}
