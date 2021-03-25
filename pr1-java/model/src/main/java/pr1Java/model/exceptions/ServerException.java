package pr1Java.model.exceptions;

public class ServerException extends Exception {
    public ServerException() {
        super("server error");
    }

    public ServerException(String message) {
        super(message);
    }
}
