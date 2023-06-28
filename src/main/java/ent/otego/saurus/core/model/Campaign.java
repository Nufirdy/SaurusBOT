package ent.otego.saurus.core.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;
import lombok.*;

@Entity
@Table(name = "campaigns")
@Getter
@Setter
@NoArgsConstructor
public class Campaign {

    @Id
    private Long id;

    private UUID seasonUid;

    private String name;

    private String color;

    private Integer useCase;

    private Integer clubId;

    private UUID leaderboardGroupUid;

    private Instant publicationTimestamp;

    private Instant startTimestamp;

    private Instant endTimestamp;

    private Instant rankingSentTimestamp;

    private boolean published;

    @OneToMany(targetEntity = MapInfo.class, mappedBy = "campaign", cascade = {CascadeType.MERGE,
            CascadeType.PERSIST})
    @OrderBy("name")
    private List<MapInfo> playlist;

    @OneToOne(mappedBy = "campaign",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private CampaignMedia media;

    private Instant editionTimestamp;

    public void setPlaylist(List<MapInfo> mapInfos) {
        mapInfos.forEach(mapInfo -> mapInfo.setCampaign(this));
        playlist = List.copyOf(mapInfos);
    }

    public void setMedia(CampaignMedia media) {
        media.setCampaign(this);
        this.media = media;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Campaign campaign = (Campaign) o;
        return id.equals(campaign.id) && name.equals(campaign.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
