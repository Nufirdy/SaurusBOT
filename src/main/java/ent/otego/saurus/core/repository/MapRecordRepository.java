package ent.otego.saurus.core.repository;

import ent.otego.saurus.core.model.*;
import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface MapRecordRepository extends JpaRepository<MapRecord, UUID> {

    @Query("SELECT mr FROM MapRecord mr WHERE mr.mapInfo = :mapInfo ORDER BY mr.time "
           + "ASC LIMIT 1")
    MapRecord findFastestByMap(@Param("mapInfo") MapInfo mapInfo);

    @Query("SELECT mr FROM MapRecord mr WHERE mr.mapInfo = :mapInfo AND mr.account = :player "
           + "ORDER BY mr.time ASC LIMIT 1")
    Optional<MapRecord> findFastestRecordByPlayerOnMap(
            @Param("mapInfo") MapInfo mapInfo,
            @Param("player") PlayerAccount playerAccount
    );

    @Query(nativeQuery = true,
            value = """
                    SELECT DISTINCT ON (time) id,
                                              account_id,
                                              filename,
                                              medal,
                                              removed,
                                              respawn_count,
                                              score,
                                              min(time) OVER (PARTITION BY account_id, map_info_id) AS time,
                                              timestamp,
                                              url,
                                              map_info_id
                    FROM map_record
                    WHERE account_id IN :accounts
                      AND map_info_id = :mapInfo
                    ORDER BY time
                    """)
    List<MapRecord> getCustomLeaderboardRecords(
            @Param("accounts") Collection<UUID> accounts,
            @Param("mapInfo") UUID mapInfo
    );
}
