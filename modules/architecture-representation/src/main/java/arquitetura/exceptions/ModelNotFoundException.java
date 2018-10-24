package arquitetura.exceptions;

/**
 * @author edipofederle
 */
public class ModelNotFoundException extends Exception {

    private static final long serialVersionUID = -2051728272268586455L;

    public ModelNotFoundException(String message) {
        super(message);
    }

}
