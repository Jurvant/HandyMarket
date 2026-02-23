package ilya.pon.listing.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record ImageCreateDto(
        String description,
        @NotNull @Valid
        String url) {}