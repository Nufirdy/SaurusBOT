package ent.otego.saurus.integration.repository;

import ent.otego.saurus.integration.model.*;
import ent.otego.saurus.integration.model.Subscription.SubscriptionIdClass;
import java.util.List;
import org.springframework.data.jpa.repository.*;

public interface SubscriptionsRepository extends JpaRepository<Subscription, SubscriptionIdClass> {

    @Query("SELECT sb FROM Subscription sb JOIN sb.subscriber sr WHERE sb.enabled=true "
           + "AND sb.subscriptionType=:subscriptionType AND TYPE(sb.subscriber)=:type")
    List<Subscription> findAllSubscribersBySubscriptionTypeAndPlatform(
            SubscriptionType subscriptionType,
            Class<? extends Subscriber> type
    );

    List<Subscription> findAllBySubscriptionType(SubscriptionType subscriptionType);
}
