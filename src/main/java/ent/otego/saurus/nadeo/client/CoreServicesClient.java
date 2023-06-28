package ent.otego.saurus.nadeo.client;

import ent.otego.saurus.nadeo.model.*;
import java.util.List;

public interface CoreServicesClient {

    NadeoJWT getJWTForServer(
            String basicAuth,
            CoreServiceJWTRequestBody audience
    );

    NadeoJWT refreshAccessToken(String refreshToken);

    /**
     * Предел длины запроса ~8217 символов, что примерно равно 220 uuid
     * @param accountIdList
     * @param mapIdList
     * @return
     */
    List<MapRecordDTO> getMapRecords(
            String accountIdList, String mapIdList
    );
}
