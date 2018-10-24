package arquitetura.exceptions;

public class NotSuppportedOperation extends Exception {

    private static final long serialVersionUID = 6566410646647790299L;

    public NotSuppportedOperation(String message) {
        super(message);
    }

}
