package ent.otego.saurus.integration.model;

import jakarta.persistence.*;
import java.util.*;
import lombok.*;

@Entity
@Table(name = "subscribers")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
public class Subscriber {//переименовать в чат

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isPrivate;

    @OneToMany(mappedBy = "subscriber")//TODO нужен ли cascade?
    private Set<Subscription> subscriptions;

    @Column(nullable = false)
    private Locale locale = Locale.UK;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscriber that = (Subscriber) o;
        return id == that.id && isPrivate == that.isPrivate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
