package ent.otego.saurus.nadeo.model;

import java.util.*;

public record MapLeaderboards(String groupUid,
                              String mapUid,
                              List<LeaderboardTops> tops) {
}
