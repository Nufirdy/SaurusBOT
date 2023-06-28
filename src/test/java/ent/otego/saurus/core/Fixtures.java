package ent.otego.saurus.core;

import static java.util.UUID.randomUUID;

import ent.otego.saurus.core.model.*;
import ent.otego.saurus.core.model.MapInfo;
import ent.otego.saurus.integration.model.Subscriber;
import ent.otego.saurus.nadeo.model.*;
import java.time.Instant;
import java.util.*;
import org.apache.commons.lang3.RandomUtils;

public class Fixtures {


    public static final MapInfo MAP_A =
            createMapInfo(UUID.fromString("0212f42d-bb92-4c26-8059-5ca7a5d7cfb6"), "MAP A");
    public static final MapInfo MAP_B =
            createMapInfo(UUID.fromString("a3943be7-7203-46e3-92dd-6b3f7e374023"), "MAP B");

    public static final PlayerAccount PLAYER_A =
            createPlayerAccount(UUID.fromString("ed14ac85-1252-4cc7-8efd-49cd72938f9d"), "PLAYER A");
    public static final PlayerAccount PLAYER_B =
            createPlayerAccount(UUID.fromString("4bb5de94-1093-4b17-8776-afe9773fee4a"), "PLAYER B");
    public static final PlayerAccount PLAYER_C =
            createPlayerAccount(UUID.fromString("2343ca67-a77c-47c8-83bd-74f99c6f0a37"), "PLAYER C");
    public static final PlayerAccount PLAYER_D =
            createPlayerAccount(UUID.fromString("2a13aa7d-992d-4a7c-a3c5-d29b08b7f8cb"), "PLAYER D");

    public static final MapRecord MAP_RECORD_1_PLAYER_A_ON_A =
            createMapRecord(UUID.fromString("0212f42d-bb92-4c26-8059-5ca7a5d7cfb8"), PLAYER_A,
                    MAP_A, 69900);
    public static final MapRecord MAP_RECORD_2_PLAYER_A_ON_A =
            createMapRecord(UUID.fromString("0212f42d-bb92-4c26-8059-5ca7a5d7cfb7"), PLAYER_A,
                    MAP_A, 70000);
    public static final MapRecord MAP_RECORD_1_PLAYER_B_ON_A =
            createMapRecord(UUID.fromString("0212f42d-bb92-4c26-8059-5ca7a5d7cfb9"), PLAYER_B,
                    MAP_A, 71123);
    public static final MapRecord MAP_RECORD_2_PLAYER_B_ON_A =
            createMapRecord(UUID.fromString("0212f42d-bb92-4c26-8059-5ca7a5d7cfc0"), PLAYER_B,
                    MAP_A, 71200);
    public static final MapRecord MAP_RECORD_1_PLAYER_B_ON_B =
            createMapRecord(UUID.fromString("a3943be7-7203-46e3-92dd-6b3f7e374025"), PLAYER_B,
                    MAP_B, 42700);
    public static final MapRecord MAP_RECORD_1_PLAYER_C_ON_B =
            createMapRecord(UUID.fromString("a3943be7-7203-46e3-92dd-6b3f7e374024"), PLAYER_C,
                    MAP_B, 42701);

    public static final CustomLeaderboard LEADERBOARD_A = createCustomLeaderboardA();
    public static final CustomLeaderboard LEADERBOARD_B = createCustomLeaderboardB();

    public static final MapRecordDTO NEW_MAP_RECORD_PLAYER_A_ON_A =
            toMapRecordDto(createMapRecord(randomUUID(), PLAYER_A, MAP_A, 69882));
    public static final MapRecordDTO NEW_MAP_RECORD_PLAYER_B_ON_A =
            toMapRecordDto(createMapRecord(randomUUID(), PLAYER_B, MAP_A, 69900));
    public static final MapRecordDTO NEW_MAP_RECORD_PLAYER_B_ON_B =
            toMapRecordDto(createMapRecord(randomUUID(), PLAYER_B, MAP_B, 42680));
    public static final MapRecordDTO NEW_MAP_RECORD_PLAYER_C_ON_A =
            toMapRecordDto(createMapRecord(randomUUID(), PLAYER_C, MAP_A, 72003));
    public static final MapRecordDTO NEW_MAP_RECORD_PLAYER_D_ON_B =
            toMapRecordDto(createMapRecord(randomUUID(), PLAYER_D, MAP_B, 42603));

    private static CustomLeaderboard createCustomLeaderboardA() {
        Random random = new Random();
        CustomLeaderboard customLeaderboard = new CustomLeaderboard();
        customLeaderboard.setId(random.nextLong());

        Set<MapInfo> mapInfos = new HashSet<>();
        mapInfos.add(MAP_A);
        mapInfos.add(MAP_B);
        customLeaderboard.setMaps(mapInfos);

        Set<PlayerAccount> playerAccounts = new HashSet<>();
        playerAccounts.add(PLAYER_A);
        playerAccounts.add(PLAYER_B);
        playerAccounts.add(PLAYER_C);
        customLeaderboard.setPlayers(playerAccounts);

        customLeaderboard.setSubscriber(createSubscriber());

        return customLeaderboard;
    }

    private static CustomLeaderboard createCustomLeaderboardB() {
        Random random = new Random();
        CustomLeaderboard customLeaderboard = new CustomLeaderboard();
        customLeaderboard.setId(random.nextLong());

        Set<MapInfo> mapInfos = new HashSet<>();
        mapInfos.add(MAP_B);
        customLeaderboard.setMaps(mapInfos);

        Set<PlayerAccount> playerAccounts = new HashSet<>();
        playerAccounts.add(PLAYER_B);
        playerAccounts.add(PLAYER_C);
        playerAccounts.add(PLAYER_D);
        customLeaderboard.setPlayers(playerAccounts);

        customLeaderboard.setSubscriber(createSubscriber());

        return customLeaderboard;
    }

    private static MapInfo createMapInfo(UUID id, String name) {
        MapInfo mapInfo = new MapInfo();
        mapInfo.setId(id);
        mapInfo.setName(name);
        return mapInfo;
    }

    private static PlayerAccount createPlayerAccount(UUID id, String name) {
        PlayerAccount playerAccount = new PlayerAccount();
        playerAccount.setId(id);
        playerAccount.setDisplayName(name);
        return playerAccount;
    }

    private static Subscriber createSubscriber() {
        Subscriber subscriber = new Subscriber();
        subscriber.setId(RandomUtils.nextLong());
        return subscriber;
    }

    private static MapRecord createMapRecord(UUID id, PlayerAccount player, MapInfo map, int time) {
        MapRecord mapRecord = new MapRecord();
        mapRecord.setId(id);
        mapRecord.setAccount(player);
        mapRecord.setMapInfo(map);
        mapRecord.setTime(time);
        return mapRecord;
    }

    private static MapRecordDTO toMapRecordDto(MapRecord mapRecord) {
        return new MapRecordDTO(mapRecord.getAccount().getId(),
                "",
                "",
                "",
                mapRecord.getMapInfo().getId(),
                mapRecord.getId(),
                4,
                new RecordScore(mapRecord.getRespawnCount(), mapRecord.getScore(),
                        mapRecord.getTime()),
                false,
                "",
                "",
                Instant.now(),
                "");
    }
}
