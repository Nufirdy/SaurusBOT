package ent.otego.saurus.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Workaround class to use scheduled with transactional
 */
@Component
@ConditionalOnProperty(name = "saurus.module.core.disabled", havingValue = "true")
public class TrackmaniaDataScheduler {

    private final TrackmaniaDataTracker trackmaniaDataTracker;
    private final CustomLeaderboardsTracker customLeaderboardsTracker;
    private final TaskScheduler scheduler;

    @Autowired
    public TrackmaniaDataScheduler(
            TrackmaniaDataTracker trackmaniaDataTracker,
            CustomLeaderboardsTracker customLeaderboardsTracker,
            TaskScheduler scheduler
    ) {
        this.trackmaniaDataTracker = trackmaniaDataTracker;
        this.customLeaderboardsTracker = customLeaderboardsTracker;
        this.scheduler = scheduler;
    }

    @Scheduled(cron = "0 5 15 1 4,7,10 *", zone = "+0")
    @Scheduled(cron = "0 5 16 1 1 *", zone = "+0")
    private void scheduleCampaignUpdates() {
        trackmaniaDataTracker.updateCampaigns();
    }

    //@Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void scheduleRecordsUpdate() {
        trackmaniaDataTracker.updateMapRecords();
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void scheduleCustomLeaderboardsUpdate() {
        customLeaderboardsTracker.updateAllCustomLeaderboards();
    }
}
