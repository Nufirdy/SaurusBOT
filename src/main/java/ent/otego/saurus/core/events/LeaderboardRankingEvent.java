package ent.otego.saurus.core.events;

import ent.otego.saurus.core.model.*;
import ent.otego.saurus.integration.model.Subscriber;
import java.util.List;
import org.springframework.context.ApplicationEvent;

public class LeaderboardRankingEvent extends ApplicationEvent {

    private final Subscriber subscriber;
    private final List<MapRecord> oldLeaderboard;
    private final List<MapRecord> newLeaderboard;

    public LeaderboardRankingEvent(
            Subscriber subscriber,
            List<MapRecord> oldLeaderboard,
            List<MapRecord> newLeaderboard
    ) {
        super(subscriber);
        this.subscriber = subscriber;
        this.oldLeaderboard = oldLeaderboard;
        this.newLeaderboard = newLeaderboard;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public List<MapRecord> getOldLeaderboard() {
        return oldLeaderboard;
    }

    public List<MapRecord> getNewLeaderboard() {
        return newLeaderboard;
    }
}
