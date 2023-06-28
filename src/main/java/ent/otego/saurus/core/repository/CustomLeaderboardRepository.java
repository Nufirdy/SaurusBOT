package ent.otego.saurus.core.repository;

import ent.otego.saurus.core.model.*;
import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface CustomLeaderboardRepository extends JpaRepository<CustomLeaderboard, UUID> {

    CustomLeaderboard findBySubscriberId(long chatId);

    @Query("SELECT DISTINCT mi FROM MapInfo mi JOIN CustomLeaderboard cl ON mi MEMBER OF cl.maps "
           + "WHERE :player MEMBER OF cl.players")
    Set<MapInfo> findAllTrackedMapsForPlayer(@Param("player") PlayerAccount playerAccount);
}
