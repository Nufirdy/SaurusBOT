package ent.otego.saurus.core;

import static java.util.stream.Collectors.*;

import ent.otego.saurus.core.events.*;
import ent.otego.saurus.core.mappers.*;
import ent.otego.saurus.core.model.*;
import ent.otego.saurus.core.repository.*;
import ent.otego.saurus.nadeo.NadeoCoreServices;
import ent.otego.saurus.nadeo.model.MapRecordDTO;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.*;
import java.util.Map.Entry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "saurus.module.core.disabled", havingValue = "true")
@Transactional
public class CustomLeaderboardsTracker {

    private final NadeoCoreServices coreServices;

    private final MapRecordMapper mapRecordMapper;

    private final EntityManager entityManager;
    private final CustomLeaderboardRepository leaderboardRepository;
    private final MapRecordRepository mapRecordRepository;
    private final PlayerAccountRepository accountRepository;
    private final MapInfoRepository mapInfoRepository;

    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public CustomLeaderboardsTracker(
            NadeoCoreServices coreServices,
            MapRecordMapper mapRecordMapper,
            EntityManager entityManager,
            CustomLeaderboardRepository leaderboardRepository,
            MapRecordRepository mapRecordRepository,
            PlayerAccountRepository accountRepository,
            MapInfoRepository mapInfoRepository,
            ApplicationEventPublisher eventPublisher
    ) {
        this.coreServices = coreServices;
        this.mapRecordMapper = mapRecordMapper;
        this.entityManager = entityManager;
        this.leaderboardRepository = leaderboardRepository;
        this.mapRecordRepository = mapRecordRepository;
        this.accountRepository = accountRepository;
        this.mapInfoRepository = mapInfoRepository;
        this.eventPublisher = eventPublisher;
    }

    public void updateAllCustomLeaderboards() {
        List<CustomLeaderboard> leaderboards = leaderboardRepository.findAll();
        for (CustomLeaderboard leaderboard : leaderboards) {
            updateCustomLeaderboard(leaderboard);
        }
    }

    //разделить на отдельные методы, один трекает рекорды игрока, другой изменение позиций в
    // таблицах чатов
    public void updateCustomLeaderboard(CustomLeaderboard leaderboard) {
        List<UUID> accountIdList = new ArrayList<>();
        List<UUID> mapIdList = new ArrayList<>();
        Map<UUID, MapInfo> mapInfoMap = new HashMap<>();
        for (PlayerAccount player : leaderboard.getPlayers()) {
            accountIdList.add(player.getId());
        }
        for (MapInfo map : leaderboard.getMaps()) {
            mapIdList.add(map.getId());
            mapInfoMap.put(map.getId(), map);
        }
        if (accountIdList.isEmpty() || mapIdList.isEmpty()) {
            return;
        }
        Map<MapInfo, List<MapRecord>> recordsOnMap =
                coreServices.getMapRecords(accountIdList, mapIdList).stream()
                        .map(mapRecordDto -> {
                            MapRecord mapRecord =
                                    mapRecordMapper.mapRecordDTOToEntity(mapRecordDto);
                            mapRecord.setMapInfo(mapInfoMap.get(mapRecordDto.mapId()));
                            mapRecord.setAccount(accountRepository.getReferenceById(
                                    mapRecordDto.accountId()));
                            return mapRecord;
                        })
                        .collect(groupingBy(MapRecord::getMapInfo));

        for (Entry<MapInfo, List<MapRecord>> entry : recordsOnMap.entrySet()) {
            List<MapRecord> oldCustomLeaderboard =
                    mapRecordRepository.getCustomLeaderboardRecords(
                            accountIdList,
                            entry.getKey().getId());

            Map<PlayerAccount, MapRecord> oldRecords = oldCustomLeaderboard
                    .stream()
                    .collect(toMap(MapRecord::getAccount, mapRecord -> mapRecord));

            for (MapRecord newRecord :
                    entry.getValue()) {
                PlayerAccount playerAccount = newRecord.getAccount();
                if (oldRecords.get(playerAccount) == null
                    || newRecord.getTime() < oldRecords.get(playerAccount).getTime()) {
                    entityManager.merge(newRecord);
                    eventPublisher.publishEvent(new PersonalBestEvent(newRecord));
                }
            }

            List<MapRecord> updatedLeaderboard =
                    mapRecordRepository.getCustomLeaderboardRecords(
                            accountIdList,
                            entry.getKey().getId());
            if (!oldCustomLeaderboard.equals(updatedLeaderboard)) {
                eventPublisher.publishEvent(
                        new LeaderboardRankingEvent(leaderboard.getSubscriber(), oldCustomLeaderboard,
                                updatedLeaderboard));
            }
        }
    }

    public void updatePlayersRecords() {
        Set<PlayerAccount> allTrackedPlayers = accountRepository.findAllTrackedPlayers();
        for (PlayerAccount player : allTrackedPlayers) {
            Set<MapInfo> allTrackedMapsForPlayer =
                    leaderboardRepository.findAllTrackedMapsForPlayer(player);
            List<UUID> mapIdList = allTrackedMapsForPlayer.stream()
                    .map(MapInfo::getId)
                    .toList();
            List<MapRecordDTO> mapRecords =
                    coreServices.getMapRecords(List.of(player.getId()), mapIdList);
            for (MapRecordDTO mapRecordDto : mapRecords) {
                MapInfo mapInfo = mapInfoRepository.findById(mapRecordDto.mapId())
                        .orElseThrow();
                PlayerAccount playerAccount =
                        accountRepository.findById(mapRecordDto.accountId()).orElseThrow();
                Optional<MapRecord> personalBest =
                        mapRecordRepository.findFastestRecordByPlayerOnMap(mapInfo, playerAccount);
                if (personalBest.isPresent()
                    && personalBest.get().getTime() < mapRecordDto.recordScore().time()) {
                    MapRecord newPersonalBest = mapRecordMapper.mapRecordDTOToEntity(mapRecordDto);
                    newPersonalBest.setMapInfo(mapInfo);
                    newPersonalBest.setAccount(playerAccount);
                    entityManager.merge(newPersonalBest);
                    eventPublisher.publishEvent(new PersonalBestEvent(newPersonalBest));
                }
            }
        }
    }
}
