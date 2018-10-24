package arquitetura.touml;

import arquitetura.exceptions.CustonTypeNotFound;
import arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import arquitetura.exceptions.NodeNotFound;
import arquitetura.helpers.XmiHelper;
import arquitetura.representation.Package;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class PackageOperations extends XmiHelper {

    private DocumentManager documentManager;
    private Node umlModelChild;
    private Node notatioChildren;
    private Element pkg;
    private ClassNotation notation;
    private String id;
    private String packageRecieve;

    public PackageOperations(DocumentManager documentManager) {
        this.documentManager = documentManager;
        this.umlModelChild = documentManager.getDocUml().getElementsByTagName("uml:Model").item(0);
        this.notatioChildren = documentManager.getDocNotation().getElementsByTagName("notation:Diagram").item(0);
        notation = new ClassNotation(this.documentManager, notatioChildren);
    }


    public PackageOperations createPacakge(final Package pack) throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
        setIdPackage(pack.getId());
        arquitetura.touml.Document.executeTransformation(documentManager, new Transformation() {

            public void useTransformation() {
                pkg = documentManager.getDocUml().createElement("packagedElement");
                pkg.setAttribute("xmi:type", "uml:Package");
                pkg.setAttribute("xmi:id", pack.getId());
                pkg.setAttribute("name", pack.getName());
                umlModelChild.appendChild(pkg);

                notation.createXmiForPackageInNotationFile(pack.getId());
            }

        });

        return this;
    }

    private void setIdPackage(String randonUUID) {
        this.id = randonUUID;
    }

    public Map<String, String> build() throws CustonTypeNotFound, NodeNotFound {
        Map<String, String> classesInfo = new HashMap<String, String>();
        classesInfo.put("packageId", this.id);
        return classesInfo;
    }

    /**
     * @param ids
     * @return
     * @throws CustonTypeNotFound
     * @throws NodeNotFound
     * @throws InvalidMultiplictyForAssociationException
     */
    public PackageOperations withClass(final List<String> ids) throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
        for (final String _id : ids)
            move(_id, null);
        return this;
    }


    public PackageOperations withClass(final String idklass) throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
        move(idklass, null);
        return this;
    }


    public PackageOperations withId(String packageOwner) {
        this.packageRecieve = packageOwner;
        return this;
    }


    private void move(final String _id, final String idPackage) {
        if (idPackage != null)
            id = idPackage;

        if (id != null) {
            arquitetura.touml.Document.executeTransformation(documentManager, new Transformation() {

                        //Primeiramente é olhado para o arquivo .uml e movido a classe para o pacote.
                        final Node classToMove = findByID(documentManager.getDocUml(), _id, "packagedElement");
                        final Node packageToAdd = findByID(documentManager.getDocUml(), id, "packagedElement");

                        //Agora buscamos no arquivo .notaiton
                        Node classToMoveNotation = findByIDInNotationFile(documentManager.getDocNotation(), _id);
                        Node packageToAddNotation = findByIDInNotationFile(documentManager.getDocNotation(), id);

                        public void useTransformation() {
                            packageToAdd.appendChild(classToMove);
                            packageToAddNotation.appendChild(classToMoveNotation);
                        }
                    }
            );
        }
    }


    public void add(String id2) {
        move(id2, packageRecieve);
    }

    public void add(String id2, String packageToRecieve) {
        move(id2, packageRecieve);
    }

}