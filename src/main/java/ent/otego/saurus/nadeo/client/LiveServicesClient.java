package ent.otego.saurus.nadeo.client;

import ent.otego.saurus.nadeo.model.*;

public interface LiveServicesClient {

    CampaignResponse getCampaigns(int length, int offset);

    MultipleMapsInfo getMultipleMapsInfo(String mapUidList);

    MapLeaderboards getMapleaderboards(
            String groupId, String mapUid, int length, boolean onlyWorld, int offset
    );
}
