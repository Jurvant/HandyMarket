package ilya.pon.profile.service;

import ilya.pon.profile.configuration.properties.KeycloakProperties;
import ilya.pon.profile.domain.UserProfile;
import ilya.pon.profile.dto.RegisterDto;
import ilya.pon.profile.exception.ExistingUniqueFieldException;
import ilya.pon.profile.exception.KeycloakServiceErrorException;
import ilya.pon.profile.mapper.RegisterProfileMapper;
import ilya.pon.profile.repository.UserProfileRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.UUID;

@Service
@Validated
@AllArgsConstructor
public class RegisterService {

    private final UserProfileRepository userProfileRepository;
    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;
    private final RegisterProfileMapper registerMapper;

    @Transactional
    public void register(@NotNull @Valid RegisterDto dto) {
        UserRepresentation user = getUserRepresentation(dto);
        String keycloakId = null;

        try {
            try (Response response = keycloak.realm(keycloakProperties.getRealm()).users().create(user)) {
                if (response.getStatus() == 201) {
                    keycloakId = CreatedResponseUtil.getCreatedId(response);
                } else if (response.getStatus() == 409) {
                    throw new ExistingUniqueFieldException("User with this email or username already exists");
                } else {
                    throw new KeycloakServiceErrorException("Keycloak error: " + response.getStatus());
                }
            }
            UserProfile userProfile = registerMapper.toEntity(dto);
            userProfile.setId(UUID.fromString(keycloakId));
            userProfileRepository.save(userProfile);
        } catch (Exception e) {
            if (keycloakId != null) {
                keycloak.realm(keycloakProperties.getRealm())
                        .users()
                        .get(keycloakId)
                        .remove();
            }
            throw e;
        }
    }

    private @NonNull UserRepresentation getUserRepresentation(RegisterDto dto) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());

        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(dto.password());
        user.setCredentials(Collections.singletonList(passwordCred));
        return user;
    }
}