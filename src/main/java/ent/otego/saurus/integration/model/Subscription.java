package ent.otego.saurus.integration.model;

import ent.otego.saurus.integration.model.Subscription.SubscriptionIdClass;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import lombok.*;

@Entity
@IdClass(SubscriptionIdClass.class)
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
public class Subscription {

    @Id
    private SubscriptionType subscriptionType;

    @Id
    @ManyToOne
    @JoinColumn(name = "id")
    private Subscriber subscriber;

    private boolean enabled;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return enabled == that.enabled
               && subscriptionType == that.subscriptionType
               && subscriber.equals(that.subscriber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscriptionType, subscriber);
    }

    @Getter
    @Setter
    public static class SubscriptionIdClass implements Serializable {

        private SubscriptionType subscriptionType;

        private long subscriber;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SubscriptionIdClass that = (SubscriptionIdClass) o;
            return subscriber == that.subscriber && subscriptionType == that.subscriptionType;
        }

        @Override
        public int hashCode() {
            return Objects.hash(subscriptionType, subscriber);
        }
    }
}

