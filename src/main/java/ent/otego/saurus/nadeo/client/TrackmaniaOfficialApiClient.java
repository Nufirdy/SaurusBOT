package ent.otego.saurus.nadeo.client;

import java.util.*;

public interface TrackmaniaOfficialApiClient {

    Map<UUID, String> getDisplayNames(List<UUID> accountIds);
}
