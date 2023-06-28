package ent.otego.saurus.core.model;

import ent.otego.saurus.integration.model.Subscriber;
import jakarta.persistence.*;
import java.util.*;
import lombok.*;

@Entity
@Table(name = "custom_leaderboards")
@Getter
@Setter
@NoArgsConstructor
public class CustomLeaderboard {//переименовать в ChatLeaderboards

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(name = "custom_leaderboards_map_info",
            joinColumns = @JoinColumn(name = "custom_leaderboards_id"),
            inverseJoinColumns = @JoinColumn(name = "map_info_id"))
    private Set<MapInfo> maps;

    @ManyToMany
    @JoinTable(name = "custom_leaderboards_player_accounts",
            joinColumns = @JoinColumn(name = "custom_leaderboard_id"),
            inverseJoinColumns = @JoinColumn(name = "player_account_id"))
    private Set<PlayerAccount> players;

    @OneToOne
    @JoinColumn(name = "subscriber_id")
    private Subscriber subscriber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomLeaderboard that = (CustomLeaderboard) o;
        return id.equals(that.id) && subscriber.equals(that.subscriber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subscriber);
    }
}
