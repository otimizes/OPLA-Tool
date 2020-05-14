package br.ufpr.dinf.gres.architecture.papyrus.touml;

import br.ufpr.dinf.gres.architecture.exceptions.CustonTypeNotFound;
import br.ufpr.dinf.gres.architecture.exceptions.InvalidMultiplictyForAssociationException;
import br.ufpr.dinf.gres.architecture.exceptions.NodeNotFound;
import br.ufpr.dinf.gres.architecture.helpers.XmiHelper;
import br.ufpr.dinf.gres.architecture.representation.Package;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Package operations
 *
 * @author edipofederle<edipofederle @ gmail.com>
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
        br.ufpr.dinf.gres.architecture.papyrus.touml.Document.executeTransformation(documentManager, new Transformation() {

            public void useTransformation() {
                pkg = documentManager.getDocUml().createElement("packagedElement");
                pkg.setAttribute("xmi:type", "uml:Package");
                pkg.setAttribute("xmi:id", pack.getId());
                pkg.setAttribute("name", pack.getName());
                umlModelChild.appendChild(pkg);

                notation.createXmiForPackageInNotationFile(pack.getId(), pack);
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
    public PackageOperations withClass(final List<String> ids, Package pack) throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
        for (final String _id : ids)
            move(_id, pack.getId());
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
            br.ufpr.dinf.gres.architecture.papyrus.touml.Document.executeTransformation(documentManager, new Transformation() {

                        //Primeiramente é olhado para o arquivo .uml e movido a classe para o pacote.
                        final Node classToMove = findByID(documentManager.getDocUml(), _id, "packagedElement");
                        final Node packageToAdd = findByID(documentManager.getDocUml(), id, "packagedElement");

                        //Agora buscamos no arquivo .notaiton
                        Node classToMoveNotation = findByIDInNotationFile(documentManager.getDocNotation(), _id);
                        Node packageToAddNotation = findByIDInNotationFile(documentManager.getDocNotation(), id);

                        public void useTransformation() {
                            packageToAdd.appendChild(classToMove);
                            int length = packageToAddNotation.getChildNodes().getLength();
                            for (int i = 0; i < length; i++) {
                                if (packageToAddNotation.getChildNodes().item(i).getAttributes().getNamedItem("type") != null && "7016".equals(packageToAddNotation.getChildNodes().item(i).getAttributes().getNamedItem("type").getNodeValue())) {
                                    packageToAddNotation.getChildNodes().item(i).appendChild(classToMoveNotation);
//                                    packageToAddNotation.appendChild(classToMoveNotation);
                                }
                            }
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