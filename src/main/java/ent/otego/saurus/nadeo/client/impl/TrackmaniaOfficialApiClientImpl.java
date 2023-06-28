package ent.otego.saurus.nadeo.client.impl;

import ent.otego.saurus.nadeo.client.TrackmaniaOfficialApiClient;
import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class TrackmaniaOfficialApiClientImpl implements TrackmaniaOfficialApiClient {

    private WebClient webClient;

    @Autowired
    public TrackmaniaOfficialApiClientImpl(@Qualifier("tmApiClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Map<UUID, String> getDisplayNames(List<UUID> accountIds) {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/api/display-names");
                    for (UUID accountId : accountIds) {
                        uriBuilder.queryParam("accountId[]", accountId);
                    }
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<UUID, String>>() {})
                .block();
    }
}
