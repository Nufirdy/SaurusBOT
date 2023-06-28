package ent.otego.saurus.core.model;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "tracked_map")
@Getter
@Setter
@NoArgsConstructor
public class TrackedMap {

    @Id
    private UUID id;

    @MapsId
    @OneToOne
    private MapInfo mapInfo;

}
