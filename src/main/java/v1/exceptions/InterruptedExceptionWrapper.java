package v1.exceptions;

public class InterruptedExceptionWrapper extends RuntimeException{
    public InterruptedExceptionWrapper(Throwable cause) {
        super(cause);
    }
}
