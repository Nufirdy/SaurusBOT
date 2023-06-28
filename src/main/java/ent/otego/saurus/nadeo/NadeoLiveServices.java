package ent.otego.saurus.nadeo;

import ent.otego.saurus.nadeo.client.LiveServicesClient;
import ent.otego.saurus.nadeo.model.*;
import ent.otego.saurus.nadeo.model.campaign.Campaign;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class NadeoLiveServices {

    private static String PERSONAL_BEST = "Personal_Best";

    private LiveServicesClient liveServicesClient;

    @Autowired
    public NadeoLiveServices(LiveServicesClient liveServicesClient) {
        this.liveServicesClient = liveServicesClient;
    }

    public List<Campaign> getAllCampaigns() {
        int campaignsAmount = (LocalDateTime.now().getYear() - 2019) * 4;
        CampaignResponse response = liveServicesClient.getCampaigns(campaignsAmount, 0);
        return response.campaignList();
    }

    public Campaign getLatestCampaign() {
        CampaignResponse response = liveServicesClient.getCampaigns(1, 0);
        return response.campaignList().get(0);
    }

    public List<MapInfo> getMapInfos(List<String> mapUidList) {
        String mapUidsString = mapUidList.stream()
                .map(Objects::toString)
                .collect(Collectors.joining(","));
        return liveServicesClient.getMultipleMapsInfo(mapUidsString)
                .mapList();
    }

    public List<LeaderboardRecord> getLeaderboards(UUID campaignId, String mapUid, int length, int offset) {
        if (campaignId == null) {
            return getLeaderboards(mapUid, length, offset);
        }
        MapLeaderboards leaderboard = liveServicesClient.getMapleaderboards(
                campaignId.toString(), mapUid, length, true, offset);

        return leaderboard.tops().stream()
                .flatMap(leaderboardTops -> leaderboardTops.top().stream())
                .toList();
    }

    public List<LeaderboardRecord> getLeaderboards(String mapUid, int length, int offset) {
        MapLeaderboards leaderboard = liveServicesClient.getMapleaderboards(
                PERSONAL_BEST, mapUid, length, true, offset);
        return leaderboard.tops().stream()
                .flatMap(leaderboardTops -> leaderboardTops.top().stream())
                .toList();
    }
}
