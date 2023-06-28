package ent.otego.saurus.nadeo.security;

import ent.otego.saurus.nadeo.WebClientConfig;
import java.net.URI;
import java.util.List;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.endpoint.OAuth2RefreshTokenGrantRequest;
import org.springframework.web.util.UriComponentsBuilder;

public class NadeoRefreshTokenRequestEntityConverter implements Converter<OAuth2RefreshTokenGrantRequest, RequestEntity<?>> {

    private String refreshTokenUri;

    public NadeoRefreshTokenRequestEntityConverter(String refreshTokenUri) {
        this.refreshTokenUri = refreshTokenUri;
    }

    @Override
    public RequestEntity<?> convert(OAuth2RefreshTokenGrantRequest source) {
        HttpHeaders httpHeaders = createHeaders(source);
        URI uri = UriComponentsBuilder
                .fromUriString(refreshTokenUri)
                .build()
                .toUri();
        return new RequestEntity<>(null, httpHeaders, HttpMethod.POST, uri);
    }

    private HttpHeaders createHeaders(OAuth2RefreshTokenGrantRequest source) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "nadeo_v1 t=" + source.getRefreshToken().getTokenValue());
        httpHeaders.set("User-Agent", WebClientConfig.USER_AGENT);
        return httpHeaders;
    }
}
