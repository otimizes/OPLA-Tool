package br.otimizes.oplatool.patterns.designpatterns;

import br.otimizes.oplatool.patterns.models.Scope;

/**
 * The Class Facade.
 */
public class Facade extends DesignPattern {

    private static volatile Facade INSTANCE;

    private Facade() {
        super("Facade", "Structural");
    }

    public static synchronized Facade getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Facade();
        }
        return INSTANCE;
    }

    @Override
    public boolean verifyPS(Scope scope) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean verifyPSPLA(Scope scope) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean apply(Scope scope) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
