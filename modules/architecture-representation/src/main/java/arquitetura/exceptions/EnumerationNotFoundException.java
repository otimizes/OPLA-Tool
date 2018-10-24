package arquitetura.exceptions;

/**
 * @author edipofederle
 */
public class EnumerationNotFoundException extends Exception {

    private static final long serialVersionUID = -1004223350097394943L;

    public EnumerationNotFoundException(String name) {
        super(name);
    }

}
