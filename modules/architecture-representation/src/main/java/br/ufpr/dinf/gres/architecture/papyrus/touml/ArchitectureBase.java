package br.ufpr.dinf.gres.architecture.papyrus.touml;

import br.ufpr.dinf.gres.architecture.exceptions.ModelIncompleteException;
import br.ufpr.dinf.gres.architecture.exceptions.ModelNotFoundException;
import br.ufpr.dinf.gres.architecture.exceptions.SMartyProfileNotAppliedToModelExcepetion;

/**
 * Architecture base class
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public abstract class ArchitectureBase {

    public static DocumentManager givenADocument(String outputModelName) throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion {
        DocumentManager documentManager = new DocumentManager(outputModelName);
        return documentManager;
    }

}
