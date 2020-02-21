package arquitetura.exceptions;

public class PackageNotFound extends Exception {

    private static final long serialVersionUID = -5516983766889104389L;

    public PackageNotFound(String message) {
        super(message);
    }

}
