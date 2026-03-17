package ilya.pon.profile.service;

import ilya.pon.profile.configuration.properties.KeycloakProperties;
import ilya.pon.profile.domain.UserProfile;
import ilya.pon.profile.dto.RegisterDto;
import ilya.pon.profile.exception.ExistingUniqueFieldException;
import ilya.pon.profile.mapper.RegisterProfileMapper;
import ilya.pon.profile.repository.UserProfileRepository;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Keycloak keycloak;

    @Mock
    private KeycloakProperties keycloakProperties;

    @Mock
    private RegisterProfileMapper registerMapper;

    @InjectMocks
    private RegisterService registerService;

    private RegisterDto registerDto;
    private final UUID userId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        registerDto = new RegisterDto(
                "firstName", "lastName", "email@test.com", "username", "password"
        );
        String realm = "test-realm";
        lenient().when(keycloakProperties.getRealm()).thenReturn(realm);
    }
    @Test
    void shouldRegisterUser() {
        Response realResponse = Response.status(Response.Status.CREATED)
                .location(URI.create("http://localhost/users/" + userId))
                .build();
        when(keycloak.realm(anyString()).users().create(any()))
                .thenReturn(realResponse);

        when(registerMapper.toEntity(any())).thenReturn(new UserProfile());
        registerService.register(registerDto);
        verify(userProfileRepository, times(1)).save(any(UserProfile.class));
    }

    @Test
    void shouldDetectDuplicity() {
        Response errorResponse = Response.status(Response.Status.CONFLICT).build();

        when(keycloak.realm(anyString()).users().create(any()))
                .thenReturn(errorResponse);
        assertThrows(ExistingUniqueFieldException.class, () -> {
            registerService.register(registerDto);
        });

        verify(userProfileRepository, never()).save(any());
    }
}