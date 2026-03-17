package ilya.pon.profile.service;

import ilya.pon.profile.configuration.properties.KeycloakProperties;
import ilya.pon.profile.exception.AuthenticationFailedException;
import ilya.pon.profile.dto.LoginDto;
import ilya.pon.profile.dto.ResponseLoginDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
@AllArgsConstructor
public class LoginService {
    private final KeycloakProperties properties;
    private final RestClient restClient;

    @Transactional
    public ResponseLoginDto login(LoginDto dto) {
        String tokenUrl = String.format("%s/realms/%s/protocol/openid-connect/token",
                properties.getServerUrl(), properties.getRealm());

        return restClient.post()
                .uri(tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(buildLoginParams(dto.username(), dto.password()))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new AuthenticationFailedException("Wrong data were received");
                })
                .body(ResponseLoginDto.class);
    }

    private MultiValueMap<String, String> buildLoginParams(String username, String password) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", properties.getClientId());
        params.add("client_secret", properties.getClientSecret());
        params.add("username", username);
        params.add("password", password);
        return params;
    }
}