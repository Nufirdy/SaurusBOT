package ent.otego.saurus.nadeo;

import ent.otego.saurus.nadeo.client.TrackmaniaOfficialApiClient;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrackmaniaOfficialApiService {

    private TrackmaniaOfficialApiClient client;

    @Autowired
    public TrackmaniaOfficialApiService(TrackmaniaOfficialApiClient client) {
        this.client = client;
    }

    public Map<UUID, String> getDisplayNames(List<UUID> accountIds) {
        return client.getDisplayNames(accountIds);
    }

    public String getDisplayName(UUID accountId) {
        return getDisplayNames(List.of(accountId)).get(accountId);
    }
}
