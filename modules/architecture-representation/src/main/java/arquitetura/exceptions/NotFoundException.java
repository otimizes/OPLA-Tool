package arquitetura.exceptions;

public class NotFoundException extends Exception {

    private static final long serialVersionUID = -9151182069087467849L;

    public NotFoundException(String message) {
        super(message);
    }
}
