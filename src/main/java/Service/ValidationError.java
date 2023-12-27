package Service;

public class ValidationError extends Exception {
    ValidationError(String message) {
        super(message);
    }
}
