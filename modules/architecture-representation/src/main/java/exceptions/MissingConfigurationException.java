package exceptions;

public class MissingConfigurationException extends Exception {

    private static final long serialVersionUID = -4716869773531515559L;

    public MissingConfigurationException(String message) {
        super(message);
    }

}
