package ent.otego.saurus.nadeo.model;

import java.util.*;

public record LeaderboardTops(UUID zoneId,
                              String zoneName,
                              List<LeaderboardRecord> top) {
}
