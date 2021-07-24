package br.otimizes.oplatool.architecture.papyrus.touml;

import br.otimizes.oplatool.architecture.exceptions.ModelIncompleteException;
import br.otimizes.oplatool.architecture.exceptions.ModelNotFoundException;
import br.otimizes.oplatool.architecture.exceptions.SMartyProfileNotAppliedToModelException;

/**
 * Architecture base class
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public abstract class ArchitectureBase {

    public static DocumentManager givenADocument(String outputModelName) throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelException {
        DocumentManager documentManager = new DocumentManager(outputModelName);
        return documentManager;
    }

}
