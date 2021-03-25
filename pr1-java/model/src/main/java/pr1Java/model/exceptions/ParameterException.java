package pr1Java.model.exceptions;

public class ParameterException extends Exception {
    public ParameterException() {
        super("parameter error");
    }

    public ParameterException(String message) {
        super(message);
    }
}
