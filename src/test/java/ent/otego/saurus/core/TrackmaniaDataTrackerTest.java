package ent.otego.saurus.core;

import static org.junit.Assert.*;

import ent.otego.saurus.core.events.NewCampaignEvent;
import java.util.Optional;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TrackmaniaDataTrackerTestConfig.class)
@RecordApplicationEvents
public class TrackmaniaDataTrackerTest {

    @Autowired
    private TrackmaniaDataTracker dataTracker;
    @Autowired
    private ApplicationEvents applicationEvents;

    @Test
    public void updateCampaigns() {

        dataTracker.updateCampaigns();

        Assert.assertEquals(1, applicationEvents.stream().count());
        NewCampaignEvent event = (NewCampaignEvent) applicationEvents.stream().findFirst()
                .orElseThrow();
        Assert.assertEquals( , event.getNewCampaign().getName());
    }
}