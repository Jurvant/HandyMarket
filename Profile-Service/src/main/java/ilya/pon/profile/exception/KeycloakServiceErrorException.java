package ilya.pon.profile.exception;

public class KeycloakServiceErrorException extends RuntimeException {
    public KeycloakServiceErrorException(String message) {
        super(message);
    }
}
