package v1.exceptions;

import java.sql.SQLException;

public class SQLExceptionWrapper extends RuntimeException{
    public SQLExceptionWrapper(Throwable cause) {
        super(cause);
    }
}
