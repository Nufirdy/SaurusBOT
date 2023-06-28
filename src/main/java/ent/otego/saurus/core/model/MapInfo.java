package ent.otego.saurus.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "map_info")
@Getter
@Setter
@NoArgsConstructor
public class MapInfo {

    @Id
    private UUID id;

    @ManyToOne
    private Campaign campaign;

    private String uid;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "author")
    private PlayerAccount author;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "submitter")
    private PlayerAccount submitter;

    private Integer authorTime;

    private Integer goldTime;

    private Integer silverTime;

    private Integer bronzeTime;

    private Integer nbLaps;

    private boolean valid;

    private String downloadUrl;

    private String thumbnailUrl;

    private Instant uploadTimestamp;

    private Instant updateTimestamp;

    private String fileSize;

    @Column(name = "public")
    private boolean public0;

    private boolean playable;

    private String mapStyle;

    private String mapType;

    private String collectionName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapInfo mapInfo = (MapInfo) o;
        return id.equals(mapInfo.id) && uid.equals(mapInfo.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uid);
    }

    @Override
    public String toString() {
        return "MapInfo{" +
               "id=" + id +
               ", campaign=" + campaign +
               ", uid='" + uid + '\'' +
               ", name='" + name + '\'' +
               ", author=" + author +
               ", authorTime=" + authorTime +
               ", goldTime=" + goldTime +
               ", silverTime=" + silverTime +
               ", bronzeTime=" + bronzeTime +
               '}';
    }
}
