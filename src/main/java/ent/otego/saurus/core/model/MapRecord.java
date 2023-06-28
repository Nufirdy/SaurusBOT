package ent.otego.saurus.core.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;
import lombok.*;

@Entity
@Table(name = "map_record")
@Getter
@Setter
@NoArgsConstructor
public class MapRecord {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "account_id", nullable = false)
    private PlayerAccount account;

    @ManyToOne(optional = false)
    private MapInfo mapInfo;

    private String filename;

    private String url;

    private Integer medal;

    private int score;

    private int time;

    private int respawnCount;

    private Instant timestamp;

    private boolean removed;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapRecord mapRecord = (MapRecord) o;
        return id.equals(mapRecord.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MapRecord{" +
               "id=" + id +
               ", account=" + account +
               ", mapInfo=" + mapInfo +
               ", filename='" + filename + '\'' +
               ", url='" + url + '\'' +
               ", medal=" + medal +
               ", score=" + score +
               ", time=" + time +
               ", respawnCount=" + respawnCount +
               ", timestamp=" + timestamp +
               ", removed=" + removed +
               '}';
    }
}
