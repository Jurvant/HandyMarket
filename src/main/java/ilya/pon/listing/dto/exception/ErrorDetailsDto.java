package ilya.pon.listing.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDetailsDto {
    private String code;
    private String message;
}
