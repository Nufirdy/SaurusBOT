package ent.otego.saurus.core;

import ent.otego.saurus.core.events.*;
import ent.otego.saurus.core.mappers.*;
import ent.otego.saurus.core.model.*;
import ent.otego.saurus.core.repository.*;
import ent.otego.saurus.nadeo.*;
import ent.otego.saurus.nadeo.model.LeaderboardRecord;
import ent.otego.saurus.nadeo.model.campaign.CampaignPlaylistItem;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Transactional
public class TrackmaniaDataTracker {

    private NadeoLiveServices liveServices;
    private NadeoCoreServices coreServices;
    private TrackmaniaOfficialApiService officialApiService;

    private MapInfoMapper mapInfoMapper;
    private CampaignMapper campaignMapper;
    private MapRecordMapper mapRecordMapper;

    private EntityManager entityManager;
    private PlayerAccountRepository playerAccountRepository;
    private TrackedMapsRepository trackedMapsRepository;
    private MapRecordRepository mapRecordRepository;
    private MapInfoRepository mapInfoRepository;

    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public TrackmaniaDataTracker(
            NadeoLiveServices liveServices,
            NadeoCoreServices coreServices,
            TrackmaniaOfficialApiService officialApiService,
            MapInfoMapper mapInfoMapper,
            CampaignMapper campaignMapper,
            MapRecordMapper mapRecordMapper,
            EntityManager entityManager,
            PlayerAccountRepository playerAccountRepository,
            TrackedMapsRepository trackedMapsRepository,
            MapRecordRepository mapRecordRepository,
            MapInfoRepository mapInfoRepository,
            ApplicationEventPublisher eventPublisher
    ) {
        this.liveServices = liveServices;
        this.coreServices = coreServices;
        this.officialApiService = officialApiService;
        this.mapInfoMapper = mapInfoMapper;
        this.campaignMapper = campaignMapper;
        this.mapRecordMapper = mapRecordMapper;
        this.entityManager = entityManager;
        this.playerAccountRepository = playerAccountRepository;
        this.trackedMapsRepository = trackedMapsRepository;
        this.mapRecordRepository = mapRecordRepository;
        this.mapInfoRepository = mapInfoRepository;
        this.eventPublisher = eventPublisher;

        updateCampaigns();
    }

    public void updateCampaigns() {
        List<ent.otego.saurus.nadeo.model.campaign.Campaign> campaigns =
                liveServices.getAllCampaigns();
        for (ent.otego.saurus.nadeo.model.campaign.Campaign campaign : campaigns) {
            Campaign campaignEntity = campaignMapper.campaignToCampaignEntity(campaign);

            List<String> mapUidList = campaign.playlist().stream()
                    .map(CampaignPlaylistItem::mapUid)
                    .toList();
            List<MapInfo> mapInfos = liveServices.getMapInfos(mapUidList).stream()
                    .map(mapInfoMapper::mapInfoToMapInfoEntity)
                    .toList();
            campaignEntity.setPlaylist(mapInfos);

            entityManager.merge(campaignEntity);

            for (MapInfo mapInfo : mapInfos) {
                TrackedMap trackedMap = new TrackedMap();
                trackedMap.setId(mapInfo.getId());
                trackedMap.setMapInfo(mapInfo);

                entityManager.merge(trackedMap);
            }
            LocalDate publicationDate =
                    LocalDate.ofInstant(campaignEntity.getPublicationTimestamp(), ZoneId.of("+0"));
            if (publicationDate.equals(LocalDate.now(ZoneId.of("+0")))) {
                eventPublisher.publishEvent(new NewCampaignEvent(campaignEntity));
            }
        }
    }

    public void updateMapRecords() {
        List<TrackedMap> trackedMaps = trackedMapsRepository.findAll();
        Map<MapInfo, LeaderboardRecord> topRecords = trackedMaps.stream()
                .map(TrackedMap::getMapInfo)
                .collect(Collectors.toMap(mapInfo -> mapInfo,
                        mapInfo -> liveServices.getLeaderboards(
                                mapInfo.getCampaign().getLeaderboardGroupUid(),
                                mapInfo.getUid(), 1, 0).get(0)));

        List<MapRecord> mapRecords = topRecords.entrySet().stream()
                .map(entry -> Pair.of(entry.getKey(),
                        coreServices.getMapRecord(entry.getValue().accountId(),
                                entry.getKey().getId()).get(0)))
                .map(pair -> {
                    MapRecord mapRecord = mapRecordMapper.mapRecordDTOToEntity(pair.getRight());
                    mapRecord.setMapInfo(pair.getLeft());
                    mapRecord.setAccount(getOrCreatePlayerAccount(pair.getRight().accountId()));
                    return mapRecord;
                })
                .toList();

        for (MapRecord mapRecord : mapRecords) {
            MapRecord prevRecord = mapRecordRepository.findFastestByMap(mapRecord.getMapInfo());
            if (prevRecord != null && prevRecord.getTime() > mapRecord.getTime()) {
                log.info("New record: map {}, player {}, time {}", mapRecord.getMapInfo().getName(),
                        mapRecord.getAccount().getDisplayName(), mapRecord.getTime());
                eventPublisher.publishEvent(new NewWorldRecordEvent(mapRecord));
            }
            entityManager.merge(mapRecord);
        }
    }

    private PlayerAccount getOrCreatePlayerAccount(UUID accountId) {
        PlayerAccount playerAccount =
                playerAccountRepository.findById(accountId)
                        .orElseGet(PlayerAccount::new);
        String displayName = officialApiService.getDisplayName(accountId);

        playerAccount.setId(accountId);
        playerAccount.setDisplayName(displayName);

        return playerAccount;
    }
}

