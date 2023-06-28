package ent.otego.saurus.core.model;

import jakarta.persistence.*;
import java.util.*;
import lombok.*;

@Entity
@Table(name = "player_accounts")
@Getter
@Setter
@NoArgsConstructor
public class PlayerAccount {

    @Id
    private UUID id;

    private String displayName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerAccount that = (PlayerAccount) o;
        return id.equals(that.id) && Objects.equals(displayName, that.displayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, displayName);
    }

    @Override
    public String toString() {
        return "PlayerAccount{" +
               "id=" + id +
               ", displayName='" + displayName + '\'' +
               '}';
    }
}
