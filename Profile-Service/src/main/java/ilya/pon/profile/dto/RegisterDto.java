package ilya.pon.profile.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterDto (
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String email,
        @NotBlank String username,
        @NotBlank String password
){}