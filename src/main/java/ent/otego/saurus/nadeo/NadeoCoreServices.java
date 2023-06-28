package ent.otego.saurus.nadeo;

import ent.otego.saurus.nadeo.client.CoreServicesClient;
import ent.otego.saurus.nadeo.model.MapRecordDTO;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NadeoCoreServices {

    private CoreServicesClient coreServicesClient;

    @Autowired
    public NadeoCoreServices(CoreServicesClient coreServicesClient) {
        this.coreServicesClient = coreServicesClient;
    }

    public List<MapRecordDTO> getMapRecords(
            List<UUID> accountIdList,
            List<UUID> mapIdList
    ) {
        List<MapRecordDTO> mapRecords = new ArrayList<>();
        String accountIdsString = accountIdList.stream()
                .map(UUID::toString)
                .collect(Collectors.joining(","));
        List<List<UUID>> partitionedMapIds =
                ListUtils.partition(mapIdList, 200 - accountIdList.size());
        for (List<UUID> partitionedMapId : partitionedMapIds) {
            String mapIdsString = partitionedMapId.stream()
                    .map(UUID::toString)
                    .collect(Collectors.joining(","));
            mapRecords.addAll(coreServicesClient.getMapRecords(accountIdsString, mapIdsString));
        }
        return mapRecords;
    }

    public List<MapRecordDTO> getMapRecord(UUID accountId, UUID mapId) {
        return getMapRecords(List.of(accountId), List.of(mapId));
    }
}
