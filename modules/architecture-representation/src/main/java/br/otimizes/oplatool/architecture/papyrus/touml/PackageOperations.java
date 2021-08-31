package br.otimizes.oplatool.architecture.papyrus.touml;

import br.otimizes.oplatool.architecture.exceptions.CustonTypeNotFound;
import br.otimizes.oplatool.architecture.exceptions.NodeNotFound;
import br.otimizes.oplatool.architecture.helpers.XmiHelper;
import br.otimizes.oplatool.architecture.representation.Package;
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

    private final DocumentManager documentManager;
    private final Node umlModelChild;
    private Element pkg;
    private final ClassNotation notation;
    private String id;
    private String packageReceive;

    public PackageOperations(DocumentManager documentManager) {
        this.documentManager = documentManager;
        this.umlModelChild = documentManager.getDocUml().getElementsByTagName("uml:Model").item(0);
        Node notationChildren = documentManager.getDocNotation().getElementsByTagName("notation:Diagram").item(0);
        notation = new ClassNotation(this.documentManager, notationChildren);
    }


    public PackageOperations createPackage(final Package pack) {
        setIdPackage(pack.getId());
        Document.executeTransformation(documentManager, () -> {
            pkg = documentManager.getDocUml().createElement("packagedElement");
            pkg.setAttribute("xmi:type", "uml:Package");
            pkg.setAttribute("xmi:id", pack.getId());
            pkg.setAttribute("name", pack.getName());
            umlModelChild.appendChild(pkg);
            notation.createXmiForPackageInNotationFile(pack.getId(), pack);
        });

        return this;
    }

    private void setIdPackage(String randomUUID) {
        this.id = randomUUID;
    }

    public Map<String, String> build() throws CustonTypeNotFound, NodeNotFound {
        Map<String, String> classesInfo = new HashMap<>();
        classesInfo.put("packageId", this.id);
        return classesInfo;
    }

    public PackageOperations withClass(final List<String> ids, Package pack) {
        for (final String _id : ids)
            move(_id, pack.getId());
        return this;
    }


    public PackageOperations withClass(final String idklass) {
        move(idklass, null);
        return this;
    }


    public PackageOperations withId(String packageOwner) {
        this.packageReceive = packageOwner;
        return this;
    }


    private void move(final String _id, final String idPackage) {
        if (idPackage != null)
            id = idPackage;

        if (id != null) {
            Document.executeTransformation(documentManager, new Transformation() {
                        final Node classToMove = findByID(documentManager.getDocUml(), _id, "packagedElement");
                        final Node packageToAdd = findByID(documentManager.getDocUml(), id, "packagedElement");

                        final Node classToMoveNotation = findByIDInNotationFile(documentManager.getDocNotation(), _id);
                        final Node packageToAddNotation = findByIDInNotationFile(documentManager.getDocNotation(), id);

                        public void useTransformation() {
                            if (packageToAdd != null)
                                packageToAdd.appendChild(classToMove);
                            if (packageToAddNotation != null) {
                                int length = packageToAddNotation.getChildNodes().getLength();
                                for (int i = 0; i < length; i++) {
                                    if (packageToAddNotation.getChildNodes().item(i).getAttributes().getNamedItem("type") != null
                                            && "7016".equals(packageToAddNotation.getChildNodes().item(i)
                                            .getAttributes().getNamedItem("type").getNodeValue())) {
                                        packageToAddNotation.getChildNodes().item(i).appendChild(classToMoveNotation);
                                    }
                                }
                            }
                        }
                    }
            );
        }
    }

    public void add(String id2) {
        move(id2, packageReceive);
    }

    public void add(String id2, String packageToReceive) {
        move(id2, packageReceive);
    }
}