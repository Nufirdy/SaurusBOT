package ent.otego.saurus.nadeo;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ent.otego.saurus.nadeo.security.*;
import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.endpoint.*;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.*;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.*;

@Configuration
public class WebClientConfig {

    public static String USER_AGENT = "Saurus - record tracker bot / nufirdy@gmail.com";

    @Bean
    public OAuth2AuthorizedClientProvider authorizedClientProvider(
            ObjectMapper objectMapper,
            @Value("${nadeo.service.core}") String tokenApiBaseUrl,
            @Value("${nadeo.security.refresh-token-endpoint}") String refreshTokenApiPath
    ) {
        RestTemplate restTemplate =
                new RestTemplate(List.of(new MappingJackson2HttpMessageConverter(objectMapper)));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());

        DefaultClientCredentialsTokenResponseClient tokenResponseClient =
                new DefaultClientCredentialsTokenResponseClient();
        tokenResponseClient.setRequestEntityConverter(new NadeoAcessTokenRequestEntityConverter());
        tokenResponseClient.setRestOperations(restTemplate);

        DefaultRefreshTokenTokenResponseClient refreshTokenResponseClient =
                new DefaultRefreshTokenTokenResponseClient();
        String refreshTokenUri = tokenApiBaseUrl + refreshTokenApiPath;
        refreshTokenResponseClient.setRequestEntityConverter(
                new NadeoRefreshTokenRequestEntityConverter(refreshTokenUri));
        refreshTokenResponseClient.setRestOperations(restTemplate);

        NadeoAuthAuthorizedClientProvider nadeoProvider =
                new NadeoAuthAuthorizedClientProvider(tokenResponseClient,
                        refreshTokenResponseClient);

        return new NadeoDelegationOAuth2AuthorizedClientProvider(nadeoProvider);
    }

    @Bean
    @Autowired
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService,
            OAuth2AuthorizedClientProvider authorizedClientProvider
    ) {
        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, authorizedClientService);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        authorizedClientManager.setContextAttributesMapper(authorizeRequest -> {
            Map<String, Object> contextAttributes = new HashMap<>();
            ClientRegistration clientRegistration =
                    clientRegistrationRepository.findByRegistrationId(
                            authorizeRequest.getClientRegistrationId());
            if (clientRegistration.getAuthorizationGrantType().getValue().equals("password")) {
                contextAttributes.put(OAuth2AuthorizationContext.USERNAME_ATTRIBUTE_NAME,
                        clientRegistration.getClientId());
                contextAttributes.put(OAuth2AuthorizationContext.PASSWORD_ATTRIBUTE_NAME,
                        clientRegistration.getClientSecret());
            }
            return contextAttributes;
        });

        return authorizedClientManager;
    }

    @Bean
    @Autowired
    public WebClient tmApiClient(OAuth2AuthorizedClientManager authorizedClientManager,
            @Value("${nadeo.service.api}") String nadeoApiUrl) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        return WebClient.builder()
                .apply(oauth2Client.oauth2Configuration())
                .defaultRequest(requestHeadersSpec -> requestHeadersSpec.attributes
                        (clientRegistrationId("tm-api")))
                .baseUrl(nadeoApiUrl)
                .defaultHeader("User-Agent", USER_AGENT)
                .build();
    }

    @Bean
    @Autowired
    public WebClient coreServicesClient(OAuth2AuthorizedClientManager authorizedClientManager,
            @Value("${nadeo.service.core}") String coreServicesUrl) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        return WebClient.builder()
                .apply(oauth2Client.oauth2Configuration())
                .defaultRequest(requestHeadersSpec -> {
                    requestHeadersSpec.attributes(clientRegistrationId("NadeoServices"));
                })
                .filter((request, next) -> {
                    ClientRequest replaceRequest = replaceRequest(request);
                    return next.exchange(replaceRequest);
                })
                .baseUrl(coreServicesUrl)
                .defaultHeader("User-Agent", USER_AGENT)
                .build();
    }

    @Bean
    @Autowired
    public WebClient liveServicesClient(OAuth2AuthorizedClientManager authorizedClientManager,
            @Value("${nadeo.service.live}") String liveServicesUrl) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        return WebClient.builder()
                .apply(oauth2Client.oauth2Configuration())
                .defaultRequest(requestHeadersSpec -> {
                    requestHeadersSpec.attributes(clientRegistrationId("NadeoLiveServices"));
                })
                .filter((request, next) -> {
                    ClientRequest replaceRequest = replaceRequest(request);
                    return next.exchange(replaceRequest);
                })
                .baseUrl(liveServicesUrl)
                .defaultHeader("User-Agent", USER_AGENT)
                .build();
    }

    @Bean
    public Module oauthTokensDeserializerModule() {
        SimpleModule module = new SimpleModule("OAuth2AccessTokenResponse-deserializer-module");
        module.addDeserializer(OAuth2AccessTokenResponse.class,
                new OAuth2AccessTokenResponseDeserializer());

        return module;
    }

    private ClientRequest replaceRequest(ClientRequest request) {
        List<String> authHeader = request.headers().get("Authorization");
        String token = authHeader.get(0).replace("Bearer ", "");
        String nadeoToken = "nadeo_v1 t=" + token;

        return ClientRequest.from(request)
                .headers(httpHeaders -> {
                    HttpHeaders replacedAuthHeaders = new HttpHeaders(httpHeaders);
                    replacedAuthHeaders.set("Authorization", nadeoToken);
                })
                .build();
    }
}
