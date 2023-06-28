package ent.otego.saurus.nadeo.client.impl;

import ent.otego.saurus.nadeo.client.LiveServicesClient;
import ent.otego.saurus.nadeo.model.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class LiveServicesClientImpl implements LiveServicesClient {

    private WebClient webClient;

    @Autowired
    public LiveServicesClientImpl(@Qualifier("liveServicesClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public CampaignResponse getCampaigns(int length, int offset) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/token/campaign/official")
                        .queryParam("length", length)
                        .queryParam("offset", offset)
                        .build())
                .retrieve()
                .bodyToMono(CampaignResponse.class)
                .block();
    }

    @Override
    public MultipleMapsInfo getMultipleMapsInfo(String mapUidList) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/token/map/get-multiple")
                        .queryParam("mapUidList", mapUidList)
                        .build())
                .retrieve()
                .bodyToMono(MultipleMapsInfo.class)
                .block();
    }

    @Override
    public MapLeaderboards getMapleaderboards(
            String groupId,
            String mapUid,
            int length,
            boolean onlyWorld,
            int offset
    ) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/token/leaderboard/group/{groupId}/map/{mapUid}/top")
                        .queryParam("length", length)
                        .queryParam("onlyWorld", onlyWorld)
                        .queryParam("offset", offset)
                        .build(groupId, mapUid))
                .retrieve()
                .bodyToMono(MapLeaderboards.class)
                .block();
    }
}
