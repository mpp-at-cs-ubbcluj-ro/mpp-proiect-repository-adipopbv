package pr1Java.model.exceptions;

public class DatabaseException extends Exception {
    public DatabaseException() {
        super("database error");
    }

    public DatabaseException(String message) {
        super(message);
    }
}
