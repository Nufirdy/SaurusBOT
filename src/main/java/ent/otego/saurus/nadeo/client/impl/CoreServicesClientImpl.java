package ent.otego.saurus.nadeo.client.impl;

import ent.otego.saurus.nadeo.client.CoreServicesClient;
import ent.otego.saurus.nadeo.model.*;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CoreServicesClientImpl implements CoreServicesClient {

    private WebClient webClient;

    @Autowired
    public CoreServicesClientImpl(@Qualifier("coreServicesClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public NadeoJWT getJWTForServer(String basicAuth, CoreServiceJWTRequestBody audience) {
        return null;
    }

    @Override
    public NadeoJWT refreshAccessToken(String refreshToken) {
        return null;
    }

    @Override
    public List<MapRecordDTO> getMapRecords(String accountIdList, String mapIdList) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/mapRecords/")
                        .queryParam("accountIdList", accountIdList)
                        .queryParam("mapIdList", mapIdList)
                        .build())
                .retrieve()
                .bodyToFlux(MapRecordDTO.class)
                .collect(Collectors.toList())
                .block();
    }
}
