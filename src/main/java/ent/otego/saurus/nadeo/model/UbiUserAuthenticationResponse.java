package ent.otego.saurus.nadeo.model;

import java.net.Inet4Address;
import java.time.LocalDateTime;
import java.util.UUID;

public record UbiUserAuthenticationResponse(String platformType,
                                            String ticket,
                                            String twoFactorAuthenticationTicket,
                                            UUID profileId,
                                            UUID userId,
                                            String nameOnPlatform,
                                            String environment,
                                            LocalDateTime expiration,
                                            UUID spaceId,
                                            Inet4Address clientIp,
                                            String clientIpCountry,
                                            LocalDateTime serverTime,
                                            UUID sessionId,
                                            String sessionKey,
                                            String rememberMeTicket) {

}
