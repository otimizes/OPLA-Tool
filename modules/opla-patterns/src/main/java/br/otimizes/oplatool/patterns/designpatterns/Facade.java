package br.otimizes.oplatool.patterns.designpatterns;

import br.otimizes.oplatool.patterns.models.Scope;

/**
 * The Class Facade.
 */
public class Facade extends DesignPattern {

    /** The instance. */
    private static volatile Facade INSTANCE;

    /**
     * Instantiates a new facade.
     */
    private Facade() {
        super("Facade", "Structural");
    }

    /**
     * Gets the single instance of Facade.
     *
     * @return single instance of Facade
     */
    public static synchronized Facade getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Facade();
        }
        return INSTANCE;
    }

    /**
     * Verify PS.
     *
     * @param scope the scope
     * @return true, if PS
     */
    @Override
    public boolean verifyPS(Scope scope) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Verify PSPLA.
     *
     * @param scope the scope
     * @return true, if PSPLA
     */
    @Override
    public boolean verifyPSPLA(Scope scope) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Apply.
     *
     * @param scope the scope
     * @return true, if successful
     */
    @Override
    public boolean apply(Scope scope) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
