package ent.otego.saurus.nadeo.security;

import java.time.*;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.endpoint.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.util.*;

public class NadeoAuthAuthorizedClientProvider implements OAuth2AuthorizedClientProvider {

    private OAuth2AccessTokenResponseClient<OAuth2ClientCredentialsGrantRequest>
            accessTokenResponseClient;

    private OAuth2AccessTokenResponseClient<OAuth2RefreshTokenGrantRequest>
            refreshTokenResponseClient;

    private Duration clockSkew = Duration.ofSeconds(60);

    private Clock clock = Clock.systemUTC();

    public NadeoAuthAuthorizedClientProvider(
            OAuth2AccessTokenResponseClient<OAuth2ClientCredentialsGrantRequest> accessTokenResponseClient,
            OAuth2AccessTokenResponseClient<OAuth2RefreshTokenGrantRequest> refreshTokenResponseClient
    ) {
        this.accessTokenResponseClient = accessTokenResponseClient;
        this.refreshTokenResponseClient = refreshTokenResponseClient;
    }

    @Override
    @Nullable
    public OAuth2AuthorizedClient authorize(OAuth2AuthorizationContext context) {
        Assert.notNull(context, "context cannot be null");
        ClientRegistration clientRegistration = context.getClientRegistration();
        OAuth2AuthorizedClient authorizedClient = context.getAuthorizedClient();
        if (!AuthorizationGrantType.CLIENT_CREDENTIALS.equals(
                clientRegistration.getAuthorizationGrantType())) {
            return null;
        }
        if (authorizedClient != null && !hasTokenExpired(authorizedClient.getAccessToken())) {
            return null;
        }

        OAuth2AccessTokenResponse tokenResponse;
        if (authorizedClient != null && hasTokenExpired(authorizedClient.getAccessToken())
            && authorizedClient.getRefreshToken() != null) {
            OAuth2RefreshTokenGrantRequest refreshTokenGrantRequest =
                    new OAuth2RefreshTokenGrantRequest(
                            authorizedClient.getClientRegistration(),
                            authorizedClient.getAccessToken(),
                            authorizedClient.getRefreshToken());
            tokenResponse = getTokenResponse(clientRegistration, refreshTokenGrantRequest);
        } else {
            OAuth2ClientCredentialsGrantRequest clientCredentialsGrantRequest =
                    new OAuth2ClientCredentialsGrantRequest(clientRegistration);
            tokenResponse = getTokenResponse(clientRegistration, clientCredentialsGrantRequest);
        }

        return new OAuth2AuthorizedClient(clientRegistration, context.getPrincipal().getName(),
                tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());
    }

    private OAuth2AccessTokenResponse getTokenResponse(
            ClientRegistration clientRegistration,
            OAuth2ClientCredentialsGrantRequest passwordGrantRequest
    ) {
        try {
            return this.accessTokenResponseClient.getTokenResponse(passwordGrantRequest);
        } catch (OAuth2AuthorizationException ex) {
            throw new ClientAuthorizationException(ex.getError(),
                    clientRegistration.getRegistrationId(), ex);
        }
    }

    private OAuth2AccessTokenResponse getTokenResponse(
            ClientRegistration clientRegistration,
            OAuth2RefreshTokenGrantRequest refreshTokenGrantRequest
    ) {
        try {
            return this.refreshTokenResponseClient.getTokenResponse(refreshTokenGrantRequest);
        } catch (OAuth2AuthorizationException ex) {
            throw new ClientAuthorizationException(ex.getError(),
                    clientRegistration.getRegistrationId(), ex);
        }
    }

    private boolean hasTokenExpired(OAuth2Token token) {
        return this.clock.instant().isAfter(token.getExpiresAt().minus(this.clockSkew));
    }
}
