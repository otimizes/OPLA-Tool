package arquitetura.exceptions;

public class MethodNotFoundException extends Exception {

    private static final long serialVersionUID = 1810808461167467818L;

    public MethodNotFoundException(String message) {
        super(message);
    }
}
