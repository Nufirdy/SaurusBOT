package ent.otego.saurus.nadeo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.UUID;

public record MapInfo(String uid,
                      UUID mapId,
                      String name,
                      UUID author,
                      UUID submitter,
                      Integer authorTime,
                      Integer goldTime,
                      Integer silverTime,
                      Integer bronzeTime,
                      Integer nbLaps,
                      boolean valid,
                      String downloadUrl,
                      String thumbnailUrl,
                      Instant uploadTimestamp,
                      Instant updateTimestamp,
                      String fileSize,
                      @JsonProperty("public") boolean public0,
                      boolean favorite,
                      boolean playable,
                      String mapStyle,
                      String mapType,
                      String collectionName) {
}
