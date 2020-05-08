package br.ufpr.dinf.gres.architecture.representation.architectureControl;

import br.ufpr.dinf.gres.architecture.exceptions.ClassNotFound;
import br.ufpr.dinf.gres.architecture.helpers.UtilResources;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.architecture.representation.*;
import br.ufpr.dinf.gres.architecture.representation.relationship.Relationship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArchitectureRemoveElementControl {
    public ArchitectureRemoveElementControl() {
    }

    private static final ArchitectureRemoveElementControl INSTANCE = new ArchitectureRemoveElementControl();

    public static ArchitectureRemoveElementControl getInstance() {
        return INSTANCE;
    }

    public void removeInterfaceByID(Architecture architecture, String id) {
        Set<Interface> newHash = new HashSet<>();
        for (Interface i : architecture.getEditableListInterfaces()) {
            if (!i.getId().equals(id)) {
                newHash.add(i);
            } else {
                architecture.getRelationshipHolder().removeRelatedRelationships(i);
            }
        }
        architecture.addAllInterfaces(newHash); // replace allinterfaces by the new list
        for (Package pkg : architecture.getEditableListPackages()) {
            pkg.removeInterfaceByID(id);
        }
    }

    public void removePackage(Architecture architecture, Package p) {
        if (p.isTotalyFreezed()) return;
        /**
         * Remove qualquer relacionamento que os elementos do pacote
         * que esta sendo deletado possa ter.
         */
        for (Element element : p.getElements()) {
            architecture.getRelationshipHolder().removeRelatedRelationships(element);
        }
        //Remove os relacionamentos que o pacote possa pertencer
        architecture.getRelationshipHolder().removeRelatedRelationships(p);

        architecture.getEditableListPackages().remove(p);
        //LOGGER.info("Pacote:" + p.getName() + "removido");
    }

    public void removeInterface(Architecture architecture, Interface interfacee) {
        if (interfacee.isTotalyFreezed()) return;
        interfacee.removeInterfaceFromRequiredOrImplemented();
        architecture.getRelationshipHolder().removeRelatedRelationships(interfacee);
        if (removeInterfaceFromArch(architecture, interfacee)) {
            //LOGGER.info("Interface:" + interfacee.getName() + " removida da br.ufpr.dinf.gres.arquitetura");
            System.out.println("Interface:" + interfacee.getName() + " removida da br.ufpr.dinf.gres.arquitetura");
        }
    }

    private boolean removeInterfaceFromArch(Architecture architecture, Interface interfacee) {
        if (!interfacee.isTotalyFreezed()) {
            if (architecture.getEditableListInterfaces().remove(interfacee))
                return true;
            for (Package p : architecture.getEditableListPackages()) {
                if (p.removeInterface(interfacee))
                    return true;
            }
        }
        if(ArchitectureFindElementControl.getInstance().findElementById(architecture, interfacee.getId()) != null){
            removeInterfaceByID(architecture, interfacee.getId());
            return true;
        }
        return false;
    }

    public void removeClass(Architecture architecture, Element klass) {
        if (klass.isTotalyFreezed()) return;
        architecture.getRelationshipHolder().removeRelatedRelationships(klass);
        if (architecture.getEditableListClasses().remove(klass)) {
            //LOGGER.info("Classe " + klass.getName() + "(" + klass.getId() + ") removida da br.ufpr.dinf.gres.arquitetura");
            System.out.println("Classe " + klass.getName() + "(" + klass.getId() + ") removida da br.ufpr.dinf.gres.arquitetura");
        }
        for (Package pkg : architecture.getEditableListPackages()) {
            if (pkg.getAllClasses().contains(klass)) {
                if (pkg.removeClass(klass)) {
                    //LOGGER.info("Classe " + klass.getName() + "(" + klass.getId() + ") removida da br.ufpr.dinf.gres.arquitetura. Pacote(" + pkg.getName() + ")");
                    System.out.println("Classe " + klass.getName() + "(" + klass.getId() + ") removida da br.ufpr.dinf.gres.arquitetura. Pacote(" + pkg.getName() + ")");
                }
            }
        }
    }

    public void removeSubPackageByID(Package subPkg, String id) {
        for (Package pkg : subPkg.getNestedPackages()) {
            if (id.equalsIgnoreCase(pkg.getId())) {
                subPkg.getNestedPackages().remove(pkg);
                return;
            }
            removeSubPackageByID(pkg, id);
        }
    }

    public boolean removeRelationship(Architecture architecture, Relationship as) {
        //LOGGER.info("removeRelationship()");
        System.out.println("removeRelationship()");
        if (as == null) return false;
        if (architecture.getRelationshipHolder().removeRelationship(as)) {
            //LOGGER.info("Relacionamento : " + as.getType() + " removido da br.ufpr.dinf.gres.arquitetura");
            System.out.println("Relacionamento : " + as.getType() + " removido da br.ufpr.dinf.gres.arquitetura");
            return true;
        } else {
            //LOGGER.info("TENTOU remover Relacionamento : " + as.getType() + " da br.ufpr.dinf.gres.arquitetura porém não consegiu");
            System.out.println("TENTOU remover Relacionamento : " + as.getType() + " da br.ufpr.dinf.gres.arquitetura porém não consegiu");
            return false;
        }
    }

    public void removeImplementedInterface(Architecture architecture, Interface inter, Package pacote) {
        if (inter.isTotalyFreezed() || pacote.isTotalyFreezed()) return;
        pacote.removeImplementedInterface(inter);
        architecture.getRelationshipHolder().removeRelatedRelationships(inter);
    }

    public void removeImplementedInterface(Architecture architecture, Class foo, Interface inter) {
        if (foo.isTotalyFreezed() || inter.isTotalyFreezed()) return;
        foo.removeImplementedInterface(inter);
        architecture.getRelationshipHolder().removeRelatedRelationships(inter);
    }

    public void removeRequiredInterface(Architecture architecture, Interface supplier, Package client) {
        if (supplier.isTotalyFreezed() || client.isTotalyFreezed()) return;
        if (!client.removeRequiredInterface(supplier)) ;
        architecture.getRelationshipHolder().removeRelatedRelationships(supplier);
    }

    public void removeRequiredInterface(Architecture architecture, Interface supplier, Class client) {
        if (supplier.isTotalyFreezed() || client.isTotalyFreezed()) return;
        if (!client.removeRequiredInterface(supplier)) ;
        architecture.getRelationshipHolder().removeRelatedRelationships(supplier);
    }

    public boolean removeOnlyElement(Architecture architecture, Element element) {
        if (!element.isTotalyFreezed()) {
            if (element instanceof Class) {
                if (architecture.getEditableListClasses().remove(element)) {
                    //LOGGER.info("Classe: " + element.getName() + " removida do pacote: " + architecture.getName());
                    System.out.println("Classe: " + element.getName() + " removida do pacote: " + architecture.getName());
                    return true;
                }
            } else if (element instanceof Interface) {
                if (architecture.getEditableListInterfaces().remove(element)) {
                    //LOGGER.info("Interface: " + element.getName() + " removida do pacote: " + architecture.getName());
                    System.out.println("Interface: " + element.getName() + " removida do pacote: " + architecture.getName());
                    return true;
                }
            }
        }
        return false;
    }

}
