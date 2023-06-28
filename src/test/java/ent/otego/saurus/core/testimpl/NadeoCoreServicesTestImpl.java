package ent.otego.saurus.core.testimpl;

import ent.otego.saurus.core.Fixtures;
import ent.otego.saurus.nadeo.NadeoCoreServices;
import ent.otego.saurus.nadeo.client.CoreServicesClient;
import ent.otego.saurus.nadeo.model.MapRecordDTO;
import java.util.*;

public class NadeoCoreServicesTestImpl extends NadeoCoreServices {

    private final Set<MapRecordDTO> mapRecordDTOs;

    public NadeoCoreServicesTestImpl(CoreServicesClient coreServicesClient) {
        super(coreServicesClient);
        mapRecordDTOs = new HashSet<>();
        mapRecordDTOs.add(Fixtures.NEW_MAP_RECORD_PLAYER_A_ON_A);
        mapRecordDTOs.add(Fixtures.NEW_MAP_RECORD_PLAYER_B_ON_A);
        mapRecordDTOs.add(Fixtures.NEW_MAP_RECORD_PLAYER_C_ON_A);
        mapRecordDTOs.add(Fixtures.NEW_MAP_RECORD_PLAYER_B_ON_B);
        mapRecordDTOs.add(Fixtures.NEW_MAP_RECORD_PLAYER_D_ON_B);
    }

    @Override
    public List<MapRecordDTO> getMapRecords(List<UUID> accountIdList, List<UUID> mapIdList) {
        return mapRecordDTOs.stream()
                .filter(mapRecordDTO -> accountIdList.contains(mapRecordDTO.accountId()))
                .filter(mapRecordDTO -> mapIdList.contains(mapRecordDTO.mapId()))
                .toList();
    }
}
