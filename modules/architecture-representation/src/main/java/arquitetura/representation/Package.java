package arquitetura.representation;

import arquitetura.helpers.UtilResources;
import arquitetura.representation.relationship.RelationshiopCommons;
import arquitetura.representation.relationship.Relationship;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * {@link Package} representa um elemento Pacote dentro da arquitetura.
 * <p>
 * Pacotes podem conter {@link Class}'s {@link Interface} e outros {@link Package}'s
 *
 * @author edipofederle <edipofederle@gmail.com>
 */
public class Package extends Element {
	
	private static final Logger LOGGER = Logger.getLogger(Package.class);

    private static final long serialVersionUID = -3080328928563871488L;
    public Set<Package> nestedPackages = new HashSet<Package>();
    private Set<Class> classes = new HashSet<Class>();
    private Set<Interface> interfaces = new HashSet<Interface>();
    private Set<Interface> implementedInterfaces = new HashSet<Interface>();
    private Set<Interface> requiredInterfaces = new HashSet<Interface>();
    private RelationshipsHolder relationshipHolder;

    /**
     * Construtor Para um Elemento do Tipo Pacote
     *
     * @param architecture     - A qual arquitetura pertence
     * @param name             - Nome do Pacote
     * @param isVariationPoint - Se o mesmo é um ponto de variação
     * @param variantType      - Qual o tipo ( {@link VariantType} ) da variante
     * @param parent           - Qual o {@link Element} pai
     */
    public Package(RelationshipsHolder relationshipHolder, String name, Variant variantType, String namespace, String id) {
        super(name, variantType, "package", namespace, id);
        setRelationshipHolder(relationshipHolder);
    }

    public Package(RelationshipsHolder relationshipHolder, String name) {
        this(relationshipHolder, name, null, UtilResources.createNamespace(ArchitectureHolder.getName(), name), UtilResources.getRandonUUID());
    }

    public Package(RelationshipsHolder relationshipHolder, String name, String id) {
        this(relationshipHolder, name, null, UtilResources.createNamespace(ArchitectureHolder.getName(), name), id);
    }

    /**
     * Retorna todas as {@link Interface}  do pacote.
     *
     * @return
     */
    public Set<Interface> getAllInterfaces() {
        return this.interfaces;
    }

    /**
     * Retorna todas {@link Class} que pertencem ao pacote.
     *
     * @return List<{@link Class}>
     */
    public Set<Class> getAllClasses() {
        return this.classes;
    }

    /**
     * Retorna todos {@link Package} dentro do pacote em questão.
     *
     * @return List<{@link Package}>
     */
    public Set<Package> getNestedPackages() {
        return this.nestedPackages;
    }

    public void addImplementedInterface(Interface interfacee) {
        implementedInterfaces.add(interfacee);
    }

    /**
     * Retorna as interfaces implementadas pelo pacote e as interfaces
     * implementadas pelas classes que estão dentro do pacote.
     *
     * @return {@link Set} imutável
     */
    public Set<Interface> getImplementedInterfaces() {
        Set<Interface> implementedInterfecesForClassIntoPackage = new HashSet<Interface>();
        Set<Interface> implementedInterfacesForPackage = new HashSet<Interface>(this.implementedInterfaces);

        for (Class klass : this.getAllClasses())
            implementedInterfecesForClassIntoPackage.addAll(klass.getImplementedInterfaces());

        implementedInterfacesForPackage.addAll(implementedInterfecesForClassIntoPackage);
        return implementedInterfacesForPackage;
    }

    public Set<Interface> getOnlyInterfacesImplementedByPackage() {
        return implementedInterfaces;
    }

    public Set<Interface> getOnlyInterfacesRequiredByPackage() {
        return requiredInterfaces;
    }

    public void addRequiredInterface(Interface interfacee) {
        requiredInterfaces.add(interfacee);
    }

    public Set<Interface> getRequiredInterfaces() {
        Set<Interface> requiredInterfacesForPackage = new HashSet<Interface>();
        Set<Interface> implementedInterfacesForPackage = new HashSet<Interface>(this.requiredInterfaces);
        for (Class klass : this.getAllClasses())
            requiredInterfacesForPackage.addAll(klass.getRequiredInterfaces());

        implementedInterfacesForPackage.addAll(requiredInterfacesForPackage);
        return implementedInterfacesForPackage;
    }

    /**
     * Cria uma classe e adiciona no pacote em questão.
     *
     * @param className  - Nome da classe
     * @param isAbstract - abstrata ou não
     * @return {@link Class}
     * @throws Exception
     */
    public Class createClass(String className, boolean isAbstract) {
        Class c = new Class(getRelationshipHolder(), className, isAbstract, this.getName());
        this.classes.add(c);
        return c;
    }

    /**
     * Cria uma interface dentro do pacote
     *
     * @param name
     * @return
     */
    public Interface createInterface(String name) {
        Interface inter = new Interface(getRelationshipHolder(), name, this);
        this.interfaces.add(inter);
        return inter;
    }

    /**
     * Cria uma interface dentro do pacote. Com ID especifico.
     *
     * @param name
     * @return
     */
    public Interface createInterface(String name, String id) {
        Interface inter = new Interface(getRelationshipHolder(), name, id, this);
        this.interfaces.add(inter);
        return inter;
    }

    @Override
    public Set<Concern> getAllConcerns() {
        Set<Concern> concerns = new HashSet<Concern>();

        for (Element klass : this.classes)
            concerns.addAll(klass.getAllConcerns());
        for (Element inter : this.interfaces)
            concerns.addAll(inter.getAllConcerns());
        for (Interface interfc : getImplementedInterfaces())
            concerns.addAll(interfc.getAllConcerns());

        return concerns;
    }

    @Override
    public Set<Concern> getOwnConcerns() {
        Set<Concern> concerns = new HashSet<Concern>();

        for (Element klass : this.classes)
            concerns.addAll(klass.getOwnConcerns());
        for (Element inter : this.interfaces)
            concerns.addAll(inter.getOwnConcerns());
        for (Interface interfc : getImplementedInterfaces())
            concerns.addAll(interfc.getOwnConcerns());

        return concerns;
    }

    public void moveClassToPackage(Class klass, Package packageToMove) {
        packageToMove.addExternalClass(klass);
        this.classes.remove(klass);
        updateNamespace(klass, packageToMove.getName());
    }

    private void updateNamespace(Element klass, String packageName) {
        klass.setNamespace(ArchitectureHolder.getName() + "::" + packageName);
    }

    public boolean moveInterfaceToPackage(Interface inter, Package packageToMove) {
        if (!interfaces.contains(inter)) return false;
        packageToMove.addExternalInterface(inter);
        this.interfaces.remove(inter);
        updateNamespace(inter, packageToMove.getName());
        return true;
    }

    public void addExternalClass(Class klass) {
        classes.add(klass);
    }

    public void addExternalInterface(Interface inter) {
        interfaces.add(inter);
    }

    public boolean removeClass(Element klass) {
        getRelationshipHolder().removeRelatedRelationships(klass);
        if (this.classes.remove(klass)) {
            LOGGER.info("Classe: " + klass.getName() + " removida do pacote: " + this.getName());
            return true;
        }
        return false;
    }

    public boolean removeInterface(Element interfacee) {
        ((Interface) interfacee).removeInterfaceFromRequiredOrImplemented();
        getRelationshipHolder().removeRelatedRelationships(interfacee);
        if (this.interfaces.remove(interfacee)) {
            LOGGER.info("Interface: " + interfacee.getName() + " removida do pacote: " + this.getName());
            return true;
        }
        return false;
    }

    public boolean removeImplementedInterface(Interface interface_) {
        if (!implementedInterfaces.contains(interface_)) return false;
        implementedInterfaces.remove(interface_);
        return true;
    }

    public boolean removeRequiredInterface(Interface supplier) {
        if (!requiredInterfaces.contains(supplier)) return false;
        requiredInterfaces.remove(supplier);
        return true;
    }

    /**
     * Retorna um Set contendo as classes e as interfaces do pacote
     *
     * @return
     */
    public Set<Element> getElements() {
        Set<Element> elementsPackage = new HashSet<Element>();
        for (Class k : this.classes)
            elementsPackage.add(k);
        for (Interface i : this.interfaces)
            elementsPackage.add(i);

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
        if (element instanceof Class) {
            if (this.classes.remove(element)) {
                LOGGER.info("Classe: " + element.getName() + " removida do pacote: " + this.getName());
                return true;
            }
        } else if (element instanceof Interface) {
            if (this.interfaces.remove(element)) {
                LOGGER.info("Interface: " + element.getName() + " removida do pacote: " + this.getName());
                return true;
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
        return RelationshiopCommons.getRelationships(relationshipHolder.getRelationships(), this);
    }

}