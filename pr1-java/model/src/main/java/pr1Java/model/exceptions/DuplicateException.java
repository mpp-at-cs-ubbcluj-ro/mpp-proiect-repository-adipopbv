package pr1Java.model.exceptions;

public class DuplicateException extends Exception {
    public DuplicateException() {
        super("duplicate element");
    }

    public DuplicateException(String message) {
        super(message);
    }
}
