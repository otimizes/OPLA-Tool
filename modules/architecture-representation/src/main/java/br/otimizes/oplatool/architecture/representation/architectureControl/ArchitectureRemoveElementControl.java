package br.otimizes.oplatool.architecture.representation.architectureControl;

import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * This class has the implementation of all the method from Architecture that is used to remove an specific element from architecture
 */
public class ArchitectureRemoveElementControl {
    public ArchitectureRemoveElementControl() {
    }

    private static final ArchitectureRemoveElementControl INSTANCE = new ArchitectureRemoveElementControl();

    public static ArchitectureRemoveElementControl getInstance() {
        return INSTANCE;
    }

    /**
     * remove an interface using its id from the list of interfaces. To remove, create a new list of interfaces that
     * not contain the specific interface and replace the original list
     * the method applies this technique because some times hash cannot remove an element even if exists in hash
     *
     * @param architecture - target architecture
     * @param id           - id of target interface
     */
    public void removeInterfaceByID(Architecture architecture, String id) {
        Set<Interface> newHash = new HashSet<>();
        for (Interface i : architecture.getEditableListInterfaces()) {
            if (!i.getId().equals(id)) {
                newHash.add(i);
            } else {
                architecture.getRelationshipHolder().removeRelatedRelationships(i);
            }
        }
        architecture.addAllInterfaces(newHash);
        for (Package pkg : architecture.getEditableListPackages()) {
            pkg.removeInterfaceByID(id);
        }
    }

    /**
     * remove an package from architecture
     *
     * @param architecture - target architecture
     * @param p            -target package
     */
    public void removePackage(Architecture architecture, Package p) {
        if (p.isTotalyFreezed()) return;
        for (Element element : p.getElements()) {
            architecture.getRelationshipHolder().removeRelatedRelationships(element);
        }
        architecture.getRelationshipHolder().removeRelatedRelationships(p);
        architecture.getEditableListPackages().remove(p);
    }

    /**
     * remove an interface from the list of interfaces using method removeInterfaceFromArch.
     *
     * @param architecture    - target architecture
     * @param targetInterface - target interface
     */
    public void removeInterface(Architecture architecture, Interface targetInterface) {
        if (targetInterface.isTotalyFreezed()) return;
        targetInterface.removeInterfaceFromRequiredOrImplemented();
        architecture.getRelationshipHolder().removeRelatedRelationships(targetInterface);
        if (removeInterfaceFromArch(architecture, targetInterface)) {
            System.out.println("Interface" + targetInterface.getName() + " removed");
        }
    }

    /**
     * remove an interface using traditional method of hash
     * if cannot removed by hash, remove an interface using its id from the list of interfaces. To remove, create a
     * new list of interfaces that not contain the specific interface and replace the original list
     * the method applies this technique because some times hash cannot remove an element even if exists in hash
     *
     * @param architecture      - target architecture
     * @param interfaceToRemove - target interface
     */
    private boolean removeInterfaceFromArch(Architecture architecture, Interface interfaceToRemove) {
        if (!interfaceToRemove.isTotalyFreezed()) {
            if (architecture.getEditableListInterfaces().remove(interfaceToRemove))
                return true;
            for (Package p : architecture.getEditableListPackages()) {
                if (p.removeInterface(interfaceToRemove))
                    return true;
            }
        }
        if (ArchitectureFindElementControl.getInstance().findElementById(architecture, interfaceToRemove.getId()) != null) {
            removeInterfaceByID(architecture, interfaceToRemove.getId());
            return true;
        }
        return false;
    }

    /**
     * remove an class using pkg.removeClass ou class.remove
     *
     * @param architecture - target architecture
     * @param klass        - target class
     */
    public void removeClass(Architecture architecture, Element klass) {
        if (klass.isTotalyFreezed()) return;
        architecture.getRelationshipHolder().removeRelatedRelationships(klass);
        if (architecture.getEditableListClasses().remove(klass)) {
            System.out.println("Class " + klass.getName() + "(" + klass.getId() + ") removed");
        }
        for (Package pkg : architecture.getEditableListPackages()) {
            if (pkg.getAllClasses().contains(klass)) {
                if (pkg.removeClass(klass)) {
                    System.out.println("Class " + klass.getName() + "(" + klass.getId() + ") removed. Package(" + pkg.getName() + ")");
                }
            }
        }
    }

    /**
     * remove an subpackage using its id
     *
     * @param parentPackage - parent of subpackage
     * @param id            - id of target subpackage
     */
    public void removeSubPackageByID(Package parentPackage, String id) {
        for (Package subPkg : parentPackage.getNestedPackages()) {
            if (id.equalsIgnoreCase(subPkg.getId())) {
                parentPackage.getNestedPackages().remove(subPkg);
                return;
            }
            removeSubPackageByID(subPkg, id);
        }
    }

    /**
     * remove an relationship from qruitecture
     *
     * @param architecture - target architecture
     * @param as           - relationship to remove
     */
    public boolean removeRelationship(Architecture architecture, Relationship as) {
        System.out.println("removeRelationship()");
        if (as == null) return false;
        if (architecture.getRelationshipHolder().removeRelationship(as)) {
            System.out.println("Relationship : " + as.getType() + " removed");
            return true;
        } else {
            System.out.println("Trying to remove " + as.getType());
            return false;
        }
    }

    public void removeImplementedInterface(Architecture architecture, Interface inter, Package pkg) {
        if (inter.isTotalyFreezed() || pkg.isTotalyFreezed()) return;
        pkg.removeImplementedInterface(inter);
        architecture.getRelationshipHolder().removeRelatedRelationships(inter);
    }

    public void removeImplementedInterface(Architecture architecture, Class foo, Interface inter) {
        if (foo.isTotalyFreezed() || inter.isTotalyFreezed()) return;
        foo.removeImplementedInterface(inter);
        architecture.getRelationshipHolder().removeRelatedRelationships(inter);
    }

    public void removeRequiredInterface(Architecture architecture, Interface supplier, Package client) {
        if (supplier.isTotalyFreezed() || client.isTotalyFreezed()) return;
        client.removeRequiredInterface(supplier);
        architecture.getRelationshipHolder().removeRelatedRelationships(supplier);
    }

    public void removeRequiredInterface(Architecture architecture, Interface supplier, Class client) {
        if (supplier.isTotalyFreezed() || client.isTotalyFreezed()) return;
        client.removeRequiredInterface(supplier);
        architecture.getRelationshipHolder().removeRelatedRelationships(supplier);
    }

    /**
     * remove an element of architecture without remove its relationship.
     * can be used when move an element
     *
     * @param architecture - target architecture
     * @param element      - target element
     */
    public boolean removeOnlyElement(Architecture architecture, Element element) {
        if (!element.isTotalyFreezed()) {
            if (element instanceof Class) {
                if (architecture.getEditableListClasses().remove(element)) {
                    System.out.println("Class " + element.getName() + " removed from package " + architecture.getName());
                    return true;
                }
            } else if (element instanceof Interface) {
                if (architecture.getEditableListInterfaces().remove(element)) {
                    System.out.println("Interface " + element.getName() + " removed from package " + architecture.getName());
                    return true;
                }
            }
        }
        return false;
    }
}
