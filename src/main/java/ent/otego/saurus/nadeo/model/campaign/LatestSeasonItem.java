package ent.otego.saurus.nadeo.model.campaign;

import java.time.Instant;
import java.util.UUID;

public record LatestSeasonItem(UUID uid,
                               String name,
                               Instant startTimestamp,
                               Instant endTimestamp,
                               Integer relativeStart,
                               Integer relativeEnd,
                               Integer campaignId,
                               boolean active) {
}
