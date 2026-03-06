package ilya.pon.profile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResponseLoginDto (
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("expires_in") Long expiresIn,
        @JsonProperty("token_type") String tokenType
){}