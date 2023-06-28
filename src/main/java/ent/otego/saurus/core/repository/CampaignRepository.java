package ent.otego.saurus.core.repository;

import ent.otego.saurus.core.model.Campaign;
import java.util.List;
import org.springframework.data.jpa.repository.*;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    Campaign findTopByOrderByPublicationTimestampDesc();

    @Query("SELECT c FROM Campaign c JOIN FETCH c.playlist ORDER BY c.publicationTimestamp DESC")
    List<Campaign> findAllAndFetchPlaylistEager();
}
