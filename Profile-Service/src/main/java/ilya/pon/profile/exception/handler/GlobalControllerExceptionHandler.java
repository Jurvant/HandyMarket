package ilya.pon.profile.exception.handler;

import ilya.pon.profile.dto.exception.ErrorDetailsDto;
import ilya.pon.profile.exception.AuthenticationFailedException;
import ilya.pon.profile.exception.ExistingUniqueFieldException;
import ilya.pon.profile.exception.KeycloakServiceErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<?> handleAuthenticationFailedException(AuthenticationFailedException e) {
        ErrorDetailsDto error = new ErrorDetailsDto("INVALID_CREDENTIALS", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExistingUniqueFieldException.class)
    public ResponseEntity<?> handleRegistrationUniqueDataException(ExistingUniqueFieldException e) {
        ErrorDetailsDto error = new ErrorDetailsDto("EXISTING_UNIQUE_DATA", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(KeycloakServiceErrorException.class)
    public ResponseEntity<?> handleRegistrationKeycloakServiceException(KeycloakServiceErrorException e) {
        ErrorDetailsDto error = new ErrorDetailsDto("EXISTING_UNIQUE_DATA", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
}