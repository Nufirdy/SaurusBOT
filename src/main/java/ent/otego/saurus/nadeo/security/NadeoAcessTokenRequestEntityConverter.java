package ent.otego.saurus.nadeo.security;

import ent.otego.saurus.nadeo.WebClientConfig;
import java.net.URI;
import java.util.*;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.endpoint.*;
import org.springframework.web.util.UriComponentsBuilder;

public class NadeoAcessTokenRequestEntityConverter
        implements Converter<OAuth2ClientCredentialsGrantRequest, RequestEntity<?>> {

    @Override
    public RequestEntity<?> convert(OAuth2ClientCredentialsGrantRequest source) {
        Map<String, String> audience = new HashMap<>();
        audience.put("audience", source.getClientRegistration().getRegistrationId());
        HttpHeaders httpHeaders = createHeaders(source);
        URI uri = UriComponentsBuilder
                .fromUriString(source.getClientRegistration().getProviderDetails().getTokenUri())
                .build()
                .toUri();
        return new RequestEntity<>(audience, httpHeaders, HttpMethod.POST, uri);
    }

    private HttpHeaders createHeaders(OAuth2ClientCredentialsGrantRequest source) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBasicAuth(source.getClientRegistration().getClientId(),
                source.getClientRegistration().getClientSecret());
        httpHeaders.set("User-Agent", WebClientConfig.USER_AGENT);
        return httpHeaders;
    }
}
