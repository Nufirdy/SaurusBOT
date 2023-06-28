package ent.otego.saurus.core.repository;

import ent.otego.saurus.core.model.PlayerAccount;
import java.util.*;
import org.springframework.data.jpa.repository.*;

public interface PlayerAccountRepository extends JpaRepository<PlayerAccount, UUID> {

    @Query("SELECT DISTINCT pl FROM PlayerAccount pl JOIN CustomLeaderboard cl ON pl MEMBER OF cl.players")
    Set<PlayerAccount> findAllTrackedPlayers();
}
