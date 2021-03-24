package pr1Java.model.exceptions;

public class SignInException extends Exception {
    public SignInException() {
        super("sign in error");
    }

    public SignInException(String message) {
        super(message);
    }
}
