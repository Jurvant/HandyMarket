package ilya.pon.profile.exception;

public class ExistingUniqueFieldException extends RuntimeException {
    public ExistingUniqueFieldException(String message) {
        super(message);
    }
}
