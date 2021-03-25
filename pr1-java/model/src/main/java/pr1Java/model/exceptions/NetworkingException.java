package pr1Java.model.exceptions;

public class NetworkingException extends Exception {
    public NetworkingException() {
        super("networking error");
    }

    public NetworkingException(String message) {
        super(message);
    }
}
