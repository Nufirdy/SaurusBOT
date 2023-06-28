package ent.otego.saurus.integration;

import ent.otego.saurus.core.CustomLeaderboardsService;
import ent.otego.saurus.core.events.NewCampaignEvent;
import ent.otego.saurus.core.model.CustomLeaderboard;
import ent.otego.saurus.integration.model.*;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SubscriberService {

    private final EntityManager entityManager;
    private final CustomLeaderboardsService customLeaderboardsService;

    @Autowired
    public SubscriberService(EntityManager entityManager,
            CustomLeaderboardsService customLeaderboardsService
    ) {
        this.entityManager = entityManager;
        this.customLeaderboardsService = customLeaderboardsService;
    }

    @Transactional
    public void onSubscription(Subscriber subscriber) {
        //добавить дефолтные подписки в зависимости от типа канала (приватный/публичный)
        //if (!subscriber.isPrivate()) {
        //    Subscription worldRecordsSub = new Subscription();
        //    worldRecordsSub.setSubscriber(subscriber);
        //    worldRecordsSub.setSubscriptionType(SubscriptionType.NEW_WORLD_RECORD);
        //
        //}
        customLeaderboardsService.createNewLeaderboard(subscriber);
    }

    public void onDesubsciption() {
        //выключить конкретный тип подписки с канала при получении соответствующей команды
    }

    public void onLeave() {
        //при удалении бота с канала, выключить все подписки
    }
}
