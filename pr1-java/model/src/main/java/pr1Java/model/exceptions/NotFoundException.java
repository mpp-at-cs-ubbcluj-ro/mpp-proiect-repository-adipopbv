package pr1Java.model.exceptions;

public class NotFoundException extends Exception {
    public NotFoundException() {
        super("element not found");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
