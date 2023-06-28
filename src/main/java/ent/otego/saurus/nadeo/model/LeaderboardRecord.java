package ent.otego.saurus.nadeo.model;

import java.util.UUID;

public record LeaderboardRecord(UUID accountId,
                                UUID zoneId,
                                String zoneName,
                                Integer position,
                                Integer score) {
}
