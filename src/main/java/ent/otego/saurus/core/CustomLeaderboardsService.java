package ent.otego.saurus.core;

import static ent.otego.saurus.integration.commands.addPlayer.AddPlayerResultEnum.*;

import ent.otego.saurus.core.events.NewCampaignEvent;
import ent.otego.saurus.core.exceptions.PlayerDoesNotExistException;
import ent.otego.saurus.core.model.*;
import ent.otego.saurus.core.repository.*;
import ent.otego.saurus.integration.commands.addPlayer.*;
import ent.otego.saurus.integration.model.Subscriber;
import ent.otego.saurus.nadeo.TrackmaniaOfficialApiService;
import jakarta.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class CustomLeaderboardsService {

    private final CustomLeaderboardRepository customLeaderboardRepository;
    private final MapRecordRepository mapRecordRepository;
    private final PlayerAccountRepository playerAccountRepository;
    private final CampaignRepository campaignRepository;

    private final TrackmaniaOfficialApiService officialApi;

    @Autowired
    public CustomLeaderboardsService(
            CustomLeaderboardRepository customLeaderboardRepository,
            MapRecordRepository mapRecordRepository,
            PlayerAccountRepository playerAccountRepository,
            CampaignRepository campaignRepository,
            TrackmaniaOfficialApiService officialApi
    ) {
        this.customLeaderboardRepository = customLeaderboardRepository;
        this.mapRecordRepository = mapRecordRepository;
        this.playerAccountRepository = playerAccountRepository;
        this.campaignRepository = campaignRepository;
        this.officialApi = officialApi;
    }

    public List<MapRecord> getCustomLeaderboard(Subscriber subscriber, UUID mapId) {
        CustomLeaderboard chatLeaderboard = customLeaderboardRepository.findBySubscriberId(subscriber.getId());
        Collection<UUID> accounts = chatLeaderboard.getPlayers().stream()
                .map(PlayerAccount::getId)
                .collect(Collectors.toSet());
        return mapRecordRepository.getCustomLeaderboardRecords(accounts, mapId);
    }

    public AddPlayerResult addPlayerToLeaderboard(Subscriber subscriber, UUID accountId) throws
            PlayerDoesNotExistException {
        PlayerAccount playerAccount = playerAccountRepository.findById(accountId)
                .orElseGet(() -> getAccountFromApi(accountId));
        if (playerAccount == null) {
            throw new PlayerDoesNotExistException();
        }
        CustomLeaderboard chatLeaderboard = customLeaderboardRepository.findBySubscriberId(subscriber.getId());
        boolean playerExisted = chatLeaderboard.getPlayers().add(playerAccount);
        if (playerExisted) {
            return new AddPlayerResult(ALREADY_EXISTS, playerAccount);
        } else {
            customLeaderboardRepository.save(chatLeaderboard);
            return new AddPlayerResult(SUCCESS, playerAccount);
        }
    }

    private PlayerAccount getAccountFromApi(UUID accountId) {
        String displayName = officialApi.getDisplayName(accountId);
        if (StringUtils.isBlank(displayName)) {
            return null;
        }
        PlayerAccount playerAccount = new PlayerAccount();
        playerAccount.setId(accountId);
        playerAccount.setDisplayName(displayName);
        return playerAccountRepository.save(playerAccount);
    }

    public void createNewLeaderboard(Subscriber subscriber) {
        CustomLeaderboard customLeaderboard = new CustomLeaderboard();
        customLeaderboard.setSubscriber(subscriber);
        List<Campaign> allCampaigns = campaignRepository.findAll();
        Set<MapInfo> leaderboardMaps = new HashSet<>();
        for (Campaign campaign : allCampaigns) {
            leaderboardMaps.addAll(campaign.getPlaylist());
        }
        customLeaderboard.setMaps(leaderboardMaps);
        customLeaderboardRepository.save(customLeaderboard);
    }

    @EventListener
    public void onNewCampaign(NewCampaignEvent event) {
        List<CustomLeaderboard> allLeaderboards = customLeaderboardRepository.findAll();
        for (CustomLeaderboard leaderboard : allLeaderboards) {
            leaderboard.getMaps().addAll(event.getNewCampaign().getPlaylist());
            customLeaderboardRepository.save(leaderboard);
        }
    }
}
