package br.otimizes.oplatool.architecture.papyrus.touml;

import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.helpers.XmiHelper;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.VariationPoint;
import org.w3c.dom.Node;

/**
 * Note operations
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class NoteOperations extends XmiHelper {

    private final DocumentManager documentManager;
    private final ElementXmiGenerator elementXmiGenerator;
    private String id;

    public NoteOperations(DocumentManager documentManager, Architecture a) {
        this.documentManager = documentManager;
        this.elementXmiGenerator = new ElementXmiGenerator(documentManager, a);
    }

    public NoteOperations createNote(VariationPoint variationPointForVariability) {
        final NoteNode noteNode = new NoteNode(documentManager);
        this.id = UtilResources.getRandomUUID();
        Document.executeTransformation(documentManager, () -> noteNode.createNote(id, variationPointForVariability));

        return this;
    }

    public String build() {
        return this.id;
    }

    public NoteOperations addVariability(final String idNote, final VariabilityStereotype variability) {
        Document.executeTransformation(documentManager, () -> elementXmiGenerator.createStereotypeVariability(idNote, variability));
        if (variability.getIdPackageOwner() != null)
            moveToPakage(variability.getIdPackageOwner());
        return this;
    }

    private NoteOperations moveToPakage(final String idklass) {
        Document.executeTransformation(documentManager, () -> {
            final Node classToMove = findByID(documentManager.getDocUml(), idklass, "packagedElement");
            final Node packageToAdd = findByID(documentManager.getDocUml(), id, "ownedComment");
            if (classToMove != null) classToMove.appendChild(packageToAdd);
        });
        return this;
    }
}