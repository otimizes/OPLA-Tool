package arquitetura.touml;

import arquitetura.exceptions.ModelIncompleteException;
import arquitetura.exceptions.ModelNotFoundException;
import arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public abstract class ArchitectureBase {

    public static DocumentManager givenADocument(String outputModelName) throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion {
        DocumentManager documentManager = new DocumentManager(outputModelName);
        return documentManager;
    }

}
