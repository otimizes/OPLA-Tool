package arquitetura.exceptions;

public class VariationPointElementTypeErrorException extends Exception {

    private static final long serialVersionUID = -6721836759281498379L;

    public VariationPointElementTypeErrorException() {
        super("Variation Point Must be a Interface or a Class Element");
    }

}
