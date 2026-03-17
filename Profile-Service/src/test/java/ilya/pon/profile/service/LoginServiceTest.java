package ilya.pon.profile.service;

import ilya.pon.profile.configuration.properties.KeycloakProperties;
import ilya.pon.profile.dto.LoginDto;
import ilya.pon.profile.dto.ResponseLoginDto;
import ilya.pon.profile.exception.AuthenticationFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    LoginService loginService;

    @Mock
    KeycloakProperties properties;

    MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        mockServer = MockRestServiceServer.bindTo(builder).build();
        loginService = new LoginService(properties, builder.build());
    }

    @Test
    void shouldLogin() {
        LoginDto loginDto = new LoginDto("username", "password");

        when(properties.getServerUrl()).thenReturn("http://serverUrl");
        when(properties.getRealm()).thenReturn("realm");
        when(properties.getClientId()).thenReturn("clientId");
        when(properties.getClientSecret()).thenReturn("clientSecret");

        String expectedResponse = """
                {
                    "access_token": "token",
                    "refresh_token": "refresh",
                    "expires_in": 1,
                    "token_type": "Bearer"
                }
                """;

        mockServer.expect(requestTo("http://serverUrl/realms/realm/protocol/openid-connect/token"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_FORM_URLENCODED))
                .andRespond(withSuccess(expectedResponse, MediaType.APPLICATION_JSON));

        ResponseLoginDto response = loginService.login(loginDto);

        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo("token");

        mockServer.verify();
    }

    @Test
    void shouldFailLogin() {
        LoginDto loginDto = new LoginDto("wrong_user", "wrong_pass");

        when(properties.getServerUrl()).thenReturn("http://serverUrl");
        when(properties.getRealm()).thenReturn("realm");
        when(properties.getClientId()).thenReturn("clientId");
        when(properties.getClientSecret()).thenReturn("clientSecret");

        mockServer.expect(requestTo("http://serverUrl/realms/realm/protocol/openid-connect/token"))
                .andRespond(withStatus(HttpStatus.UNAUTHORIZED));

        assertThatThrownBy(() -> loginService.login(loginDto))
                .isInstanceOf(AuthenticationFailedException.class)
                .hasMessage("Wrong data were received");

        mockServer.verify();
    }
}