package arquitetura.exceptions;


public class ConcernCannotBeAppliedToPackagesException extends Exception {

    private static final long serialVersionUID = 89328465703288552L;

    public ConcernCannotBeAppliedToPackagesException() {
        super("Concerns Cannot be applied to packages");
    }


}
