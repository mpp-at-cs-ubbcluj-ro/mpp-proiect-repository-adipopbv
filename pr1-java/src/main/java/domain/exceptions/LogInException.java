package domain.exceptions;

public class LogInException extends Exception {
    public LogInException() {
        super("log in error");
    }

    public LogInException(String message) {
        super(message);
    }
}
