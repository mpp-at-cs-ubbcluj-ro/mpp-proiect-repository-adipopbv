package domain.exceptions;

public class DatabaseException extends Exception {
    public DatabaseException() {
        super("database error");
    }

    public DatabaseException(String message) {
        super(message);
    }
}
