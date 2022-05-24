package v1.exceptions;

public class IOExceptionWrapper extends RuntimeException{
    public IOExceptionWrapper(Throwable cause) {
        super(cause);
    }

    public IOExceptionWrapper(String message) {
        super(message);
    }
}
