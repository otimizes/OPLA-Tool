package br.otimizes.oplatool.architecture.representation;

import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import br.otimizes.oplatool.architecture.representation.relationship.RelationshipCommons;
import com.rits.cloning.Cloner;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * {@link Package} represents a package
 * <p>
 * Packages can have {@link Class}'s {@link Interface} and another {@link Package}'s
 *
 * @author edipofederle <edipofederle@gmail.com>
 */
public class Package extends Element {

    private static final Logger LOGGER = Logger.getLogger(Package.class);
    private static final long serialVersionUID = -3080328928563871488L;
    public Set<Package> nestedPackages = new HashSet<>();
    private final Set<Class> classes = new HashSet<>();
    private final Set<Interface> interfaces = new HashSet<>();
    private final Set<Interface> implementedInterfaces = new HashSet<>();
    private final Set<Interface> requiredInterfaces = new HashSet<>();
    private RelationshipsHolder relationshipHolder;

    public Package deepCopy() {
        try {
            return this.deepClone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Package deepClone() throws CloneNotSupportedException {
        Cloner cloner = new Cloner();
        Package pkg = cloner.deepClone(this);
        cloner = null;
        return pkg;
    }

    public void matchImplementedInterface(Architecture architecture) {
        Class.matchInterfaces(architecture, implementedInterfaces);
    }

    public void matchRequiredInterface(Architecture architecture) {
        Class.matchInterfaces(architecture, requiredInterfaces);
    }

    public void removeClassByID(String id) {
        Architecture.addAllClassesThatMatchToId(id, this.classes);
    }

    public void removeAllClass() {
        this.classes.clear();
    }

    public void removeAllInterfaces() {
        this.interfaces.clear();
    }

    public Package(RelationshipsHolder relationshipHolder, String name, Variant variantType, String namespace, String id) {
        super(name, variantType, "package", namespace, id);
        setRelationshipHolder(relationshipHolder);
    }

    public Package(RelationshipsHolder relationshipHolder, String name) {
        this(relationshipHolder, name, null, UtilResources.createNamespace(ArchitectureHolder.getName(), name),
                UtilResources.getRandomUUID());
    }

    public Package(RelationshipsHolder relationshipHolder, String name, String id) {
        this(relationshipHolder, name, null, UtilResources.createNamespace(ArchitectureHolder.getName(), name), id);
    }

    public Set<Interface> getAllInterfaces() {
        return this.interfaces;
    }

    public Set<Class> getAllClasses() {
        return this.classes;
    }

    public Set<Package> getNestedPackages() {
        return this.nestedPackages;
    }

    public void addImplementedInterface(Interface implementedInterface) {
        implementedInterfaces.add(implementedInterface);
    }

    public Set<Interface> getImplementedInterfaces() {
        Set<Interface> implementedInterfacesForClassIntoPackage = new HashSet<>();
        Set<Interface> implementedInterfacesForPackage = new HashSet<>(this.implementedInterfaces);
        for (Class klass : this.getAllClasses())
            implementedInterfacesForClassIntoPackage.addAll(klass.getImplementedInterfaces());
        implementedInterfacesForPackage.addAll(implementedInterfacesForClassIntoPackage);
        return implementedInterfacesForPackage;
    }

    public Set<Interface> getOnlyInterfacesImplementedByPackage() {
        return implementedInterfaces;
    }

    public Set<Interface> getOnlyInterfacesRequiredByPackage() {
        return requiredInterfaces;
    }

    public void addRequiredInterface(Interface requiredInterface) {
        requiredInterfaces.add(requiredInterface);
    }

    public Set<Interface> getRequiredInterfaces() {
        Set<Interface> requiredInterfacesForPackage = new HashSet<>();
        Set<Interface> implementedInterfacesForPackage = new HashSet<>(this.requiredInterfaces);
        for (Class klass : this.getAllClasses())
            requiredInterfacesForPackage.addAll(klass.getRequiredInterfaces());
        implementedInterfacesForPackage.addAll(requiredInterfacesForPackage);
        return implementedInterfacesForPackage;
    }

    public Class createClass(String className, boolean isAbstract) {
        Class classWithRelationship = new Class(getRelationshipHolder(), className, isAbstract, this.getName());
        this.classes.add(classWithRelationship);
        return classWithRelationship;
    }

    public Interface createInterface(String name) {
        Interface interfaceWithRelationship = new Interface(getRelationshipHolder(), name, this);
        this.interfaces.add(interfaceWithRelationship);
        return interfaceWithRelationship;
    }

    public Interface createInterface(String name, String id) {
        Interface interfaceWithRelationship = new Interface(getRelationshipHolder(), name, id, this);
        this.interfaces.add(interfaceWithRelationship);
        return interfaceWithRelationship;
    }

    @Override
    public Set<Concern> getAllConcerns() {
        Set<Concern> concerns = new HashSet<>();
        for (Element classFromPackage : this.classes)
            concerns.addAll(classFromPackage.getAllConcerns());
        for (Element interfaceFromPackage : this.interfaces)
            concerns.addAll(interfaceFromPackage.getAllConcerns());
        for (Interface implementedInterface : getImplementedInterfaces())
            concerns.addAll(implementedInterface.getAllConcerns());
        return concerns;
    }

    public Set<Concern> getConcernsOnlyFromElementWithoutMethodOrAttribute() {
        return super.getOwnConcerns();
    }

    @Override
    public Set<Concern> getOwnConcerns() {
        Set<Concern> concerns = new HashSet<>();
        for (Element classFromPackage : this.classes)
            concerns.addAll(classFromPackage.getOwnConcerns());
        for (Element interfaceFromPackage : this.interfaces)
            concerns.addAll(interfaceFromPackage.getOwnConcerns());
        for (Interface implementedInterface : getImplementedInterfaces())
            concerns.addAll(implementedInterface.getOwnConcerns());
        return concerns;
    }

    public void moveClassToPackage(Class classToMove, Package packageToMove) {
        if (classToMove.isTotalyFreezed() || packageToMove.isTotalyFreezed()) return;
        packageToMove.addExternalClass(classToMove);
        this.classes.remove(classToMove);
        updateNamespace(classToMove, packageToMove.getName());
    }

    private void updateNamespace(Element klass, String packageName) {
        klass.setNamespace(ArchitectureHolder.getName() + "::" + packageName);
    }

    public boolean moveInterfaceToPackage(Interface interfaceToMove, Package packageToMove) {
        if (interfaceToMove.isTotalyFreezed() || packageToMove.isTotalyFreezed()) return false;
        if (!interfaces.contains(interfaceToMove)) return false;
        if (packageToMove.findInterfaceByID(interfaceToMove.getId())) return false;
        packageToMove.addExternalInterface(interfaceToMove);
        this.interfaces.remove(interfaceToMove);
        this.removeInterfaceByID(interfaceToMove.getId());
        updateNamespace(interfaceToMove, packageToMove.getName());
        return true;
    }

    public boolean findInterfaceByID(String id) {
        for (Interface interfaceToMatch : interfaces) {
            if (interfaceToMatch.getId().equals(id))
                return true;
        }
        return false;
    }

    public void addExternalClass(Class externalClass) {
        classes.add(externalClass);
    }

    public void addExternalInterface(Interface externalInterface) {
        if (findInterfaceByID(externalInterface.getId())) return;
        interfaces.add(externalInterface);
    }

    public boolean removeClass(Element classToRemove) {
        if (!classToRemove.isTotalyFreezed()) {
            getRelationshipHolder().removeRelatedRelationships(classToRemove);
            if (this.classes.remove(classToRemove)) {
                LOGGER.info("Class: " + classToRemove.getName() + " removed from package: " + this.getName());
                return true;
            }
        }
        return false;
    }

    public boolean removeInterface(Element interfaceToRemove) {
        if (!interfaceToRemove.isTotalyFreezed()) {
            ((Interface) interfaceToRemove).removeInterfaceFromRequiredOrImplemented();
            getRelationshipHolder().removeRelatedRelationships(interfaceToRemove);
            if (this.interfaces.remove(interfaceToRemove)) {
                LOGGER.info("Interface: " + interfaceToRemove.getName() + " removed from package: " + this.getName());
                return true;
            } else {
                if (findInterfaceByID(interfaceToRemove.getId())) {
                    removeInterfaceByID(interfaceToRemove.getId());
                    return true;
                }
            }
        }
        return false;
    }

    public void removeInterfaceByID(String id) {
        Set<Interface> newHash = new HashSet<>();
        for (Interface interfaceFromPackage : this.interfaces) {
            if (!interfaceFromPackage.getId().equals(id)) {
                newHash.add(interfaceFromPackage);
            } else {
                relationshipHolder.removeRelatedRelationships(interfaceFromPackage);
            }
        }
        this.interfaces.clear();
        this.interfaces.addAll(newHash);
    }

    public boolean removeImplementedInterface(Interface implementedInterface) {
        if (implementedInterface.isTotalyFreezed()) return false;
        if (!implementedInterfaces.contains(implementedInterface)) return false;
        implementedInterfaces.remove(implementedInterface);
        return true;
    }

    public boolean removeRequiredInterface(Interface supplier) {
        if (supplier.isTotalyFreezed()) return false;
        if (!requiredInterfaces.contains(supplier)) return false;
        requiredInterfaces.remove(supplier);
        return true;
    }

    public Set<Element> getElements() {
        Set<Element> elementsPackage = new HashSet<>();
        elementsPackage.addAll(this.classes);
        elementsPackage.addAll(this.interfaces);
        return elementsPackage;
    }

    public void setElements(List<? extends Element> elements) {
        for (Element element : elements)
            getElements().add(element);
    }

    @Override
    public boolean containsConcern(Concern concern) {
        return this.getAllConcerns().contains(concern);
    }

    public boolean removeOnlyElement(Element element) {
        if (!element.isTotalyFreezed()) {
            if (element instanceof Class) {
                if (this.classes.remove(element)) {
                    LOGGER.info("Class: " + element.getName() + " removed from package: " + this.getName());
                    return true;
                }
            } else if (element instanceof Interface) {
                if (this.interfaces.remove(element)) {
                    LOGGER.info("Interface: " + element.getName() + " removed from package: " + this.getName());
                    return true;
                } else {
                    if (findInterfaceByID(element.getId())) {
                        removeInterfaceByID(element.getId());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public RelationshipsHolder getRelationshipHolder() {
        return relationshipHolder;
    }

    public void setRelationshipHolder(RelationshipsHolder relationshipHolder) {
        this.relationshipHolder = relationshipHolder;
    }

    public Set<Relationship> getRelationships() {
        return RelationshipCommons.getRelationships(relationshipHolder.getRelationships(), this);
    }

    @Override
    public boolean isFreezeByDM() {
        return super.isFreezeByDM();
    }
}