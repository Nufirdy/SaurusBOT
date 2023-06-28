package ent.otego.saurus.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "campaign_media")
@Getter
@Setter
@NoArgsConstructor
public class CampaignMedia {

    @Id
    @JoinColumn(name = "id")
    long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    private Campaign campaign;

    private String buttonBackgroundUrl;

    private String buttonForegroundUrl;

    private String decalUrl;

    private String popUpBackgroundUrl;

    private String popUpImageUrl;

    private String liveButtonBackgroundUrl;

    private String liveButtonForegroundUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CampaignMedia that = (CampaignMedia) o;
        return Objects.equals(buttonBackgroundUrl, that.buttonBackgroundUrl)
               && Objects.equals(buttonForegroundUrl, that.buttonForegroundUrl)
               && Objects.equals(decalUrl, that.decalUrl)
               && Objects.equals(popUpBackgroundUrl, that.popUpBackgroundUrl)
               && Objects.equals(popUpImageUrl, that.popUpImageUrl)
               && Objects.equals(liveButtonBackgroundUrl, that.liveButtonBackgroundUrl)
               && Objects.equals(liveButtonForegroundUrl, that.liveButtonForegroundUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, campaign);
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
        id = campaign.getId();
    }
}
