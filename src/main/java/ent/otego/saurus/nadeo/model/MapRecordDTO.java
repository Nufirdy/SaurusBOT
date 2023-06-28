package ent.otego.saurus.nadeo.model;

import java.time.Instant;
import java.util.UUID;

public record MapRecordDTO(UUID accountId,
                           String filename,
                           String gameMode,
                           String gameModeCustomData,
                           UUID mapId,
                           UUID mapRecordId,
                           Integer medal,
                           RecordScore recordScore,
                           boolean removed,
                           String scopeId,
                           String scopeType,
                           Instant timestamp,
                           String url) {
}
