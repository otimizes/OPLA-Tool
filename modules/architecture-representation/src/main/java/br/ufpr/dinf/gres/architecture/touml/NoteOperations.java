package br.ufpr.dinf.gres.architecture.touml;

import br.ufpr.dinf.gres.architecture.helpers.UtilResources;
import br.ufpr.dinf.gres.architecture.helpers.XmiHelper;
import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.VariationPoint;
import org.w3c.dom.Node;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class NoteOperations extends XmiHelper {

    private DocumentManager documentManager;
    private ElementXmiGenerator elementXmiGenerator;
    private String id;

    public NoteOperations(DocumentManager documentManager, Architecture a) {
        this.documentManager = documentManager;
        this.elementXmiGenerator = new ElementXmiGenerator(documentManager, a);

    }

    public NoteOperations createNote(VariationPoint variationPointForVariability) {
        final NoteNode noteNode = new NoteNode(documentManager);
        this.id = UtilResources.getRandonUUID();
        br.ufpr.dinf.gres.architecture.touml.Document.executeTransformation(documentManager, new Transformation() {
            public void useTransformation() {
                noteNode.createNote(id, variationPointForVariability);
            }
        });

        return this;
    }

    public String build() {
        return this.id;
    }

    public NoteOperations addVariability(final String idNote, final VariabilityStereotype variability) {
        br.ufpr.dinf.gres.architecture.touml.Document.executeTransformation(documentManager, new Transformation() {
            public void useTransformation() {
                elementXmiGenerator.createStereotypeVariability(idNote, variability);
            }
        });
        if (variability.getIdPackageOwner() != null)
            moveToPakage(variability.getIdPackageOwner());
        return this;
    }

    private NoteOperations moveToPakage(final String idklass) {
        br.ufpr.dinf.gres.architecture.touml.Document.executeTransformation(documentManager, new Transformation() {
            public void useTransformation() {
                // Primeiramente é olhado para o arquivo .uml e movido a
                // classe para o pacote.
                final Node classToMove = findByID(documentManager.getDocUml(), idklass, "packagedElement");
                final Node packageToAdd = findByID(documentManager.getDocUml(), id, "ownedComment");
                classToMove.appendChild(packageToAdd);
            }
        });
        return this;
    }

}