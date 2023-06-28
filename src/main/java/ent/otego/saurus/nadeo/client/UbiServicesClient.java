package ent.otego.saurus.nadeo.client;

import ent.otego.saurus.nadeo.model.UbiUserAuthenticationResponse;

public interface UbiServicesClient {

    UbiUserAuthenticationResponse authenticateUser();
}
