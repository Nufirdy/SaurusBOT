package ent.otego.saurus.nadeo.security;

import java.time.Instant;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.core.OAuth2Token;

public class NadeoDelegationOAuth2AuthorizedClientProvider
        implements OAuth2AuthorizedClientProvider {

    ClientCredentialsOAuth2AuthorizedClientProvider tmApiProvider;
    NadeoAuthAuthorizedClientProvider nadeoProvider;

    public NadeoDelegationOAuth2AuthorizedClientProvider(
            NadeoAuthAuthorizedClientProvider nadeoAuthAuthorizedClientProvider
    ) {
        tmApiProvider = new ClientCredentialsOAuth2AuthorizedClientProvider();
        nadeoProvider = nadeoAuthAuthorizedClientProvider;
    }

    @Override
    public OAuth2AuthorizedClient authorize(OAuth2AuthorizationContext context) {
        String registrationId = context.getClientRegistration().getRegistrationId();
        if (registrationId.equals("tm-api")) {
            return tmApiProvider.authorize(context);
        }

        return nadeoProvider.authorize(context);
    }
}
