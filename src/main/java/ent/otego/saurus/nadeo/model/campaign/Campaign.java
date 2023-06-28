package ent.otego.saurus.nadeo.model.campaign;

import ent.otego.saurus.nadeo.model.campaign.CampaignPlaylistItem;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record Campaign(Long id,
                       UUID seasonUid,
                       String name,
                       String color,
                       Integer useCase,
                       Integer clubId,
                       UUID leaderboardGroupUid,
                       Instant publicationTimestamp,
                       Instant startTimestamp,
                       Instant endTimestamp,
                       Instant rankingSentTimestamp,
                       Integer year,
                       Integer week,
                       Integer day,
                       Integer monthYear,
                       Integer month,
                       Integer monthDay,
                       boolean published,
                       List<CampaignPlaylistItem> playlist,
                       List<LatestSeasonItem> latestSeasons,
                       List<CampaignCategory> categories,
                       CampaignMedia media,
                       Instant editionTimestamp) {
}
