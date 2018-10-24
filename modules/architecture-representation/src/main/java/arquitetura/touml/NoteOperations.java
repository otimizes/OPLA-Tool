package arquitetura.touml;

import arquitetura.helpers.UtilResources;
import arquitetura.helpers.XmiHelper;
import arquitetura.representation.Architecture;
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

    public NoteOperations createNote() {
        final NoteNode noteNode = new NoteNode(documentManager);
        this.id = UtilResources.getRandonUUID();
        arquitetura.touml.Document.executeTransformation(documentManager, new Transformation() {
            public void useTransformation() {
                noteNode.createNote(id);
            }
        });

        return this;
    }

    public String build() {
        return this.id;
    }

    public NoteOperations addVariability(final String idNote, final VariabilityStereotype variability) {
        arquitetura.touml.Document.executeTransformation(documentManager, new Transformation() {
            public void useTransformation() {
                elementXmiGenerator.createStereotypeVariability(idNote, variability);
            }
        });
        if (variability.getIdPackageOwner() != null)
            moveToPakage(variability.getIdPackageOwner());
        return this;
    }

    private NoteOperations moveToPakage(final String idklass) {
        arquitetura.touml.Document.executeTransformation(documentManager, new Transformation() {
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