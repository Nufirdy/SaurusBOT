package ent.otego.saurus;

import static org.junit.Assert.*;

import ent.otego.saurus.core.model.*;
import ent.otego.saurus.core.repository.*;
import ent.otego.saurus.integration.model.*;
import ent.otego.saurus.integration.repository.SubscriptionsRepository;
import ent.otego.saurus.integration.telegram.TelegramChat;
import java.util.*;
import java.util.stream.Collectors;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=validate",
        "spring.datasource.url=jdbc:postgresql://localhost:5432/test-saurus"
})
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class JpaRepositoriesTest {

    @Autowired
    private MapRecordRepository mapRecordRepository;
    @Autowired
    private MapInfoRepository mapInfoRepository;
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private SubscriptionsRepository subscriptionsRepository;
    @Autowired
    private PlayerAccountRepository playerAccountRepository;
    @Autowired
    private CustomLeaderboardRepository customLeaderboardRepository;

    @Test
    @Sql("classpath:sql/records.sql")
    public void testGetFastestRecord() {
        MapInfo mapInfo =
                mapInfoRepository.findById(UUID.fromString("0212f42d-bb92-4c26-8059-5ca7a5d7cfb6"))
                        .orElseThrow();
        MapRecord mapRecord = mapRecordRepository.findFastestByMap(mapInfo);
        assertEquals(69900, mapRecord.getTime());
    }

    @Test
    @Sql("classpath:sql/records.sql")
    public void testFindFastestRecordByPlayerOnMap() {
        PlayerAccount playerAccount =
                createPlayerAccount(UUID.fromString("4bb5de94-1093-4b17-8776-afe9773fee4a"));
        MapInfo mapInfo =
                mapInfoRepository.findById(UUID.fromString("0212f42d-bb92-4c26-8059-5ca7a5d7cfb6"))
                        .orElseThrow();
        MapRecord record =
                mapRecordRepository.findFastestRecordByPlayerOnMap(mapInfo, playerAccount)
                        .orElseThrow();
        assertEquals(71123, record.getTime());
    }

    @Test
    @Sql("classpath:sql/campaigns.sql")
    public void testGetByLatestPublication() {
        Campaign latestCampaign = campaignRepository.findTopByOrderByPublicationTimestampDesc();
        Campaign expected = new Campaign();
        expected.setId(34109L);
        expected.setName("Winter 2023");
        assertEquals(expected, latestCampaign);
    }

    @Test
    @Sql("classpath:sql/subscriptions.sql")
    public void testGetSubscriptions() {
        Subscriber chat1 = new TelegramChat();
        chat1.setId(1L);
        chat1.setPrivate(true);
        ((TelegramChat) chat1).setChatId(100);
        Subscriber chat2 = new TelegramChat();
        chat2.setId(2L);
        chat2.setPrivate(false);
        ((TelegramChat) chat2).setChatId(101);

        Subscription sub1 = new Subscription();
        sub1.setSubscriptionType(SubscriptionType.NEW_WORLD_RECORD);
        sub1.setSubscriber(chat1);
        sub1.setEnabled(true);
        Subscription sub2 = new Subscription();
        sub2.setSubscriptionType(SubscriptionType.NEW_WORLD_RECORD);
        sub2.setSubscriber(chat2);
        sub2.setEnabled(true);

        List<Subscription> expected = List.of(sub1, sub2);

        List<Subscription> actual =
                subscriptionsRepository.findAllSubscribersBySubscriptionTypeAndPlatform(
                        SubscriptionType.NEW_WORLD_RECORD, TelegramChat.class);
        Assert.assertEquals(expected, actual);
    }

    @Test
    @Sql("classpath:sql/records.sql")
    public void testGetCustomLeaderboard() {
        List<UUID> accountIds = new ArrayList<>();
        accountIds.add(UUID.fromString("ed14ac85-1252-4cc7-8efd-49cd72938f9d"));
        accountIds.add(UUID.fromString("4bb5de94-1093-4b17-8776-afe9773fee4a"));
        accountIds.add(UUID.fromString("2343ca67-a77c-47c8-83bd-74f99c6f0a37"));
        accountIds.add(UUID.fromString("2a13aa7d-992d-4a7c-a3c5-d29b08b7f8cb"));

        MapInfo mapInfo = new MapInfo();
        mapInfo.setId(UUID.fromString("0212f42d-bb92-4c26-8059-5ca7a5d7cfb6"));

        List<MapRecord> customLeaderboard =
                mapRecordRepository.getCustomLeaderboardRecords(accountIds, mapInfo.getId());

        for (MapRecord mapRecord : customLeaderboard) {
            assertNotNull(mapRecord);
        }
        assertTrue(customLeaderboard.get(0).getTime() < customLeaderboard.get(1).getTime());
    }

    @Test
    @Sql("classpath:sql/records.sql")
    public void testFindAllTrackedPlayers() {
        Set<UUID> expected = new HashSet<>();
        expected.add(UUID.fromString("ed14ac85-1252-4cc7-8efd-49cd72938f9d"));
        expected.add(UUID.fromString("4bb5de94-1093-4b17-8776-afe9773fee4a"));
        expected.add(UUID.fromString("2343ca67-a77c-47c8-83bd-74f99c6f0a37"));

        Set<UUID> actual = playerAccountRepository.findAllTrackedPlayers()
                .stream()
                .map(PlayerAccount::getId)
                .collect(Collectors.toSet());

        assertEquals(expected, actual);
    }

    @Test
    @Sql("classpath:sql/records.sql")
    public void testFindAllTrackedMapsForPlayer() {
        PlayerAccount playerAccount =
                createPlayerAccount(UUID.fromString("4bb5de94-1093-4b17-8776-afe9773fee4a"));
        Set<UUID> expected = new HashSet<>();
        expected.add(UUID.fromString("0212f42d-bb92-4c26-8059-5ca7a5d7cfb6"));
        expected.add(UUID.fromString("a3943be7-7203-46e3-92dd-6b3f7e374023"));

        Set<UUID> actual = customLeaderboardRepository.findAllTrackedMapsForPlayer(playerAccount)
                .stream()
                .map(MapInfo::getId)
                .collect(Collectors.toSet());

        assertEquals(expected, actual);
    }

    private PlayerAccount createPlayerAccount(UUID id) {
        PlayerAccount playerAccount = new PlayerAccount();
        playerAccount.setId(id);
        return playerAccount;
    }
}
