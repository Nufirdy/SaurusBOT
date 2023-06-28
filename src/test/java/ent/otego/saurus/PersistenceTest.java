package ent.otego.saurus;

import ent.otego.saurus.core.model.*;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=validate"
})
public class PersistenceTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    @Sql("classpath:sql/campaign_and_media.sql")
    public void entityExistsException() {
        Campaign detachedCampaign = createCampaign();
        CampaignMedia detachedCampaignMedia = createCampaignMedia();
        detachedCampaign.setMedia(detachedCampaignMedia);

        Campaign persistedCampaign = entityManager.find(Campaign.class, detachedCampaign.getId());
        entityManager.detach(persistedCampaign);

        entityManager.merge(detachedCampaign);
        entityManager.flush();
        entityManager.detach(detachedCampaign);
        entityManager.detach(detachedCampaignMedia);

        Campaign newVersion = entityManager.find(Campaign.class, detachedCampaign.getId());
        Assert.assertNotNull(persistedCampaign.getRankingSentTimestamp());
        Assert.assertNull(newVersion.getRankingSentTimestamp());
    }

    private static Campaign createCampaign() {
        Campaign campaign = new Campaign();
        campaign.setId(30853L);
        campaign.setClubId(0);
        campaign.setColor("");
        campaign.setEditionTimestamp(Instant.parse("2022-09-28T14:49:40.00Z"));
        campaign.setEndTimestamp(Instant.parse("2023-01-01T16:00:00.00Z"));
        campaign.setLeaderboardGroupUid(UUID.fromString("67331a95-921c-4486-993b-3c45956ace22"));
        campaign.setName("Fall 2022");
        campaign.setPublicationTimestamp(Instant.parse("2022-10-01T15:00:00.00Z"));
        campaign.setPublished(true);
        campaign.setSeasonUid(UUID.fromString("67331a95-921c-4486-993b-3c45956ace22"));
        campaign.setStartTimestamp(Instant.parse("2022-10-01T15:00:00.00Z"));
        campaign.setUseCase(0);
        return campaign;
    }

    private static CampaignMedia createCampaignMedia() {
        CampaignMedia campaignMedia = new CampaignMedia();
        campaignMedia.setId(30853L);
        campaignMedia.setDecalUrl("");
        campaignMedia.setButtonForegroundUrl("");
        campaignMedia.setButtonBackgroundUrl("");
        campaignMedia.setLiveButtonForegroundUrl("");
        campaignMedia.setLiveButtonBackgroundUrl("");
        campaignMedia.setPopUpBackgroundUrl("");
        campaignMedia.setPopUpImageUrl("");
        return campaignMedia;
    }
}
