package Repository;

public class DuplicateIdException extends RepoException {
    DuplicateIdException(String message) {
        super(message);
    }
}
