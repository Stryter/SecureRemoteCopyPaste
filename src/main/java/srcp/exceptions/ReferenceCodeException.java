package srcp.exceptions;

/**
 * Created by Josh on 9/19/2015.
 */
public class ReferenceCodeException extends Exception {
    private static final long serialVersionUID = 0x0001L;

    public ReferenceCodeException() {
    }

    public ReferenceCodeException(String message) {
        super(message);
    }

    public ReferenceCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReferenceCodeException(Throwable cause) {
        super(cause);
    }
}
