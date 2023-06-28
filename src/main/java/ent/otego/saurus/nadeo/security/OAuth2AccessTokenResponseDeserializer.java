package ent.otego.saurus.nadeo.security;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.lang.reflect.*;
import java.time.Instant;
import java.util.*;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;

public class OAuth2AccessTokenResponseDeserializer extends StdDeserializer<OAuth2AccessTokenResponse> {

    public OAuth2AccessTokenResponseDeserializer() {
        super(OAuth2AccessTokenResponse.class);
    }

    @Override
    public OAuth2AccessTokenResponse deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = p.getCodec().readTree(p);
        String accessTokenValue = node.get("accessToken").asText();
        String refreshTokenValue = node.get("refreshToken").asText();

        String[] accessTokenChunks = accessTokenValue.split("\\.");
        String[] refreshTokenChunks = refreshTokenValue.split("\\.");

        ObjectMapper objectMapper = (ObjectMapper) p.getCodec();
        TypeReference<HashMap<String, String>> typeRef = new TypeReference<>() {};

        HashMap<String, String> accessTokenDecoded =
                objectMapper.readValue(Base64.getUrlDecoder().decode(accessTokenChunks[1].getBytes()), typeRef);
        HashMap<String, String> refreshTokenDecoded =
                objectMapper.readValue(Base64.getUrlDecoder().decode(refreshTokenChunks[1].getBytes()), typeRef);

        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                accessTokenValue,
                Instant.ofEpochSecond(Long.parseLong(accessTokenDecoded.get("iat"))),
                Instant.ofEpochSecond(Long.parseLong(accessTokenDecoded.get("exp"))));
        OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(refreshTokenValue,
                Instant.ofEpochSecond(Long.parseLong(refreshTokenDecoded.get("iat"))),
                Instant.ofEpochSecond(Long.parseLong(refreshTokenDecoded.get("exp"))));

        Constructor<OAuth2AccessTokenResponse> constructor;
        OAuth2AccessTokenResponse accessTokenResponse;
        try {
            constructor = OAuth2AccessTokenResponse.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            accessTokenResponse = constructor.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        Field accessTokenField;
        Field refreshTokenField;
        Field additionalParametersField;
        try {
            accessTokenField = accessTokenResponse.getClass().getDeclaredField("accessToken");
            refreshTokenField = accessTokenResponse.getClass().getDeclaredField("refreshToken");
            additionalParametersField = accessTokenResponse.getClass().getDeclaredField(
                    "additionalParameters");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        accessTokenField.setAccessible(true);
        refreshTokenField.setAccessible(true);
        additionalParametersField.setAccessible(true);

        try {
            accessTokenField.set(accessTokenResponse, accessToken);
            refreshTokenField.set(accessTokenResponse, refreshToken);
            additionalParametersField.set(accessTokenResponse, Collections.emptyMap());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return accessTokenResponse;
    }
}
