package br.otimizes.oplatool.architecture.representation;

import br.otimizes.oplatool.architecture.exceptions.AttributeNotFoundException;
import br.otimizes.oplatool.architecture.exceptions.MethodNotFoundException;
import br.otimizes.oplatool.architecture.flyweights.VariantFlyweight;
import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.papyrus.touml.Types.Type;
import br.otimizes.oplatool.architecture.papyrus.touml.VisibilityKind;
import br.otimizes.oplatool.architecture.representation.relationship.*;
import com.rits.cloning.Cloner;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class representation
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class Class extends Element implements Linkable, Functioning {

    private static final long serialVersionUID = -5450511036321846093L;

    static Logger LOGGER = LogManager.getLogger(Class.class.getName());
    private final Set<Attribute> attributes = new HashSet<>();
    private final Set<Method> methods = new HashSet<>();
    private boolean isAbstract;
    private final Set<Interface> implementedInterfaces = new HashSet<>();
    private final Set<Interface> requiredInterfaces = new HashSet<>();

    private PatternsOperations patternsOperations;
    private RelationshipsHolder relationshipHolder;

    public Class(RelationshipsHolder relationshipHolder, String name, Variant variantType, boolean isAbstract,
                 String namespace, String id) {
        super(name, variantType, "klass", namespace, id);
        setAbstract(isAbstract);
        setRelationshipHolder(relationshipHolder);
        this.setPatternOperations(new PatternsOperations());
    }

    public Class(RelationshipsHolder relationshipHolder, String name, boolean isAbstract) {
        this(relationshipHolder, name, null, isAbstract, UtilResources.createNamespace(ArchitectureHolder.getName(),
                name), UtilResources.getRandomUUID());
        this.setPatternOperations(new PatternsOperations());
    }

    public Class(RelationshipsHolder relationshipHolder, String name, boolean isAbstract, String packageName) {
        this(relationshipHolder, name, null, isAbstract, UtilResources.createNamespace(ArchitectureHolder.getName(),
                packageName), UtilResources.getRandomUUID());
        this.setPatternOperations(new PatternsOperations());
    }

    public void matchImplementedInterface(Architecture architecture) {
        matchInterfaces(architecture, implementedInterfaces);
    }

    static void matchInterfaces(Architecture architecture, Set<Interface> interfaces) {
        if (interfaces == null)
            return;
        if (interfaces.size() == 0)
            return;
        ArrayList<String> listId = new ArrayList<>();
        for (Interface inter : interfaces) {
            listId.add(inter.getId());
        }
        interfaces.clear();
        for (String id : listId) {
            Interface architectureInterfaceById = architecture.findInterfaceById(id);
            if (architectureInterfaceById != null) {
                interfaces.add(architectureInterfaceById);
            }
        }
    }

    public void matchRequiredInterface(Architecture architecture) {
        matchInterfaces(architecture, requiredInterfaces);
    }

    public Attribute findAttributeById(String id) {
        for (Attribute attribute : getAllAttributes())
            if (attribute.getId().equalsIgnoreCase(id))
                return attribute;
        return null;
    }

    public Method findMethodById(String id) {
        for (Method method : getAllMethods()) {
            if (method.getId().equalsIgnoreCase(id)) {
                return method;
            }
        }
        return null;
    }

    public void removeMethodByID(String id) {
        Set<Method> newHash = new HashSet<>();
        for (Method method : this.methods) {
            if (!method.getId().equals(id)) {
                newHash.add(method);
            }
        }
        this.methods.clear();
        this.methods.addAll(newHash);
    }

    public void removeAttributeByID(String id) {
        Set<Attribute> newHash = new HashSet<>();
        for (Attribute attribute : this.attributes) {
            if (!attribute.getId().equals(id)) {
                newHash.add(attribute);
            }
        }
        this.attributes.clear();
        this.attributes.addAll(newHash);
    }

    public boolean hasGeneralization() {
        for (Relationship relationship : this.getRelationships()) {
            if (relationship instanceof GeneralizationRelationship) {
                GeneralizationRelationship generalizationRelationship = (GeneralizationRelationship) relationship;
                if ((generalizationRelationship.getParent() != null) && (generalizationRelationship.getChild() != null)) {
                    if (generalizationRelationship.getParent().getId().equals(this.getId())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Attribute createAttribute(String name, Type type, VisibilityKind visibility) {
        String id = UtilResources.getRandomUUID();
        Attribute attribute = new Attribute(name, visibility.toString(), type.getName(), ArchitectureHolder.getName() + "::"
                + this.getName(), id);
        addExternalAttribute(attribute);
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attributes.add(attribute);
    }

    public Set<Attribute> getAllAttributes() {
        if (attributes.isEmpty())
            return Collections.emptySet();
        return Collections.unmodifiableSet(attributes);
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public Set<Method> getAllMethods() {
        if (methods.isEmpty())
            return Collections.emptySet();
        return Collections.unmodifiableSet(methods);
    }

    public Set<Method> getModifiableMethods() {
        return methods;
    }

    public Attribute findAttributeByName(String name) throws AttributeNotFoundException {
        String message = "Attribute '" + name + "' not found in the class '" + this.getName() + "'.\n";
        for (Attribute attribute : getAllAttributes())
            if (name.equalsIgnoreCase(attribute.getName()))
                return attribute;
        LOGGER.info(message);
        throw new AttributeNotFoundException(message);
    }

    public boolean moveAttributeToClass(Attribute attribute, Class destinationClass) {
        if (!destinationClass.addExternalAttribute(attribute))
            return false;

        if (!removeAttribute(attribute)) {
            destinationClass.removeAttribute(attribute);
            return false;
        }
        attribute.setNamespace(ArchitectureHolder.getName() + "::" + destinationClass.getName());
        LOGGER.info("Moved attribute: " + attribute.getName() + " from " + this.getName() + " to "
                + destinationClass.getName());
        return true;
    }

    public boolean moveMethodToClass(Method method, Class destinationClass) {
        if (!destinationClass.addExternalMethod(method))
            return false;
        if (!removeMethod(method)) {
            destinationClass.removeMethod(method);
            return false;
        }
        method.setNamespace(ArchitectureHolder.getName() + "::" + destinationClass.getName());
        LOGGER.info("Moved method: " + method.getName() + " from " + this.getName() + " to "
                + destinationClass.getName());
        return true;
    }

    public Method createMethod(String name, String type, boolean isAbstract, List<ParameterMethod> parameters) {
        if (!methodExistsOnClass(name, type)) {
            String id = UtilResources.getRandomUUID();
            Method method = new Method(name, type, this.getName(), isAbstract, id);
            if (parameters != null)
                method.getParameters().addAll(parameters);
            methods.add(method);
            return method;
        }
        return null;
    }

    private boolean methodExistsOnClass(String name, String type) {
        for (Method method : getAllMethods()) {
            if ((name.equalsIgnoreCase(method.getName())) && (type.equalsIgnoreCase(method.getReturnType()))) {
                LOGGER.info("Method '" + name + ":" + type + "' already exists in the class: '" + this.getName() + "'.\n");
                return true;
            }
        }
        return false;
    }

    public Method findMethodByName(String name) throws MethodNotFoundException {
        String message = "Method '" + name + "' not found in the class '" + this.getName() + "'.\n";
        for (Method method : getAllMethods())
            if ((name.equalsIgnoreCase(method.getName())))
                return method;

        LOGGER.info(message);
        throw new MethodNotFoundException(message);
    }

    public boolean addExternalMethod(Method method) {
        if (methods.add(method)) {
            LOGGER.info("Method  " + method.getName() + " added in the class " + this.getName());
            return true;
        } else {
            LOGGER.info("Tried to remove the method: " + method.getName() + " from class: " + this.getName() + " but couldn't");
            return false;
        }

    }

    public boolean addExternalAttribute(Attribute attribute) {
        if (this.attributes.add(attribute)) {
            LOGGER.info("Attribute: " + attribute.getName() + " added in the class: " + this.getName());
            return true;
        } else {
            LOGGER.info("Tried to remove the method: " + attribute.getName() + " from class: " + this.getName() + " but couldn't");
            return false;
        }
    }

    public boolean removeMethod(Method method) {
        if (this.methods.remove(method)) {
            LOGGER.info("Method: " + method.getName() + " removed of class: " + this.getName());
            return true;
        } else {
            LOGGER.info("Tried to remove the method: " + method.getName() + " from class: " + this.getName()
                    + " but couldn't");
            return false;
        }
    }

    public boolean removeAttribute(Attribute attribute) {
        if (this.attributes.remove(attribute)) {
            LOGGER.info("Attribute: " + attribute.getName() + " removed from class: " + this.getName());
            return true;
        } else {
            LOGGER.info("Tried to remove the attribute: " + attribute.getName() + " from class: " + this.getName()
                    + " but couldn't");
            return false;
        }
    }

    public List<Method> getAllAbstractMethods() {
        List<Method> abstractMethods = new ArrayList<>();
        for (Method method : getAllMethods())
            if (method.isAbstract())
                abstractMethods.add(method);
        return abstractMethods;
    }


    public void updateId(String id) {
        super.id = id;
    }

    public Object getAllStereotype() {
        return null;
    }

    public String getVariantType() {
        for (Variant variant : VariantFlyweight.getInstance().getVariants()) {
            if (variant.getName().equalsIgnoreCase(this.getName()))
                return variant.getVariantType();
        }
        return null;
    }

    @Override
    public Set<Concern> getAllConcerns() {
        Set<Concern> concerns = new HashSet<>(getOwnConcerns());
        for (Method method : getAllMethods())
            concerns.addAll(method.getAllConcerns());
        for (Attribute attribute : getAllAttributes())
            concerns.addAll(attribute.getAllConcerns());
        for (Interface implementedInterface : this.getImplementedInterfaces())
            concerns.addAll(implementedInterface.getAllConcerns());
        return Collections.unmodifiableSet(concerns);
    }

    public List<AssociationClassRelationship> getAllAssociationClass() {
        List<AssociationClassRelationship> associationsClasses = new ArrayList<>();
        for (Relationship relationship : getRelationshipHolder().getRelationships()) {
            if (relationship instanceof AssociationClassRelationship) {
                AssociationClassRelationship associationClassRelationship = (AssociationClassRelationship) relationship;
                if (associationClassRelationship.getAssociationClass().equals(this))
                    associationsClasses.add((AssociationClassRelationship) relationship);
                for (MemberEnd member : associationClassRelationship.getMembersEnd()) {
                    if (member.getType().equals(this))
                        associationsClasses.add((AssociationClassRelationship) relationship);
                }
            }
        }
        return associationsClasses;
    }

    public Set<Interface> getImplementedInterfaces() {
        return implementedInterfaces;
    }

    public Set<Interface> getRequiredInterfaces() {
        return requiredInterfaces;
    }

    public void removeImplementedInterface(Interface inter) {
        this.implementedInterfaces.remove(inter);
    }

    public boolean removeRequiredInterface(Interface supplier) {
        if (!requiredInterfaces.contains(supplier))
            return false;
        requiredInterfaces.remove(supplier);
        return true;
    }

    public void addImplementedInterface(Interface supplierElement) {
        this.implementedInterfaces.add(supplierElement);
    }

    public void addRequiredInterface(Interface supplier) {
        this.requiredInterfaces.add(supplier);
    }

    public Class deepCopy() {
        try {
            return this.deepClone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Class deepClone() throws CloneNotSupportedException {
        Cloner cloner = new Cloner();
        Class klass = cloner.deepClone(this);
        cloner = null;
        return klass;
    }

    public RelationshipsHolder getRelationshipHolder() {
        return relationshipHolder;
    }

    public void setRelationshipHolder(RelationshipsHolder relationshipHolder) {
        this.relationshipHolder = relationshipHolder;
    }

    public Set<Relationship> getRelationships() {
        if (relationshipHolder == null) return new HashSet<>();
        return RelationshipCommons.getRelationships(relationshipHolder.getRelationships(), this);
    }

    public void setPatternOperations(PatternsOperations patternOperations) {
        this.patternsOperations = patternOperations;
    }

    public PatternsOperations getPatternsOperations() {
        return this.patternsOperations;
    }

    public Set<Relationship> getGeneralizations() {
        return getRelationships().stream().filter(r -> r instanceof GeneralizationRelationship).collect(Collectors.toSet());
    }

    public Set<Concern> getAllConcernsWithoutImplementedInterfaces() {
        Set<Concern> concerns = new HashSet<>(getOwnConcerns());
        for (Method method : getAllMethods())
            concerns.addAll(method.getAllConcerns());
        for (Attribute attribute : getAllAttributes())
            concerns.addAll(attribute.getAllConcerns());
        return concerns;
    }

    public Set<Attribute> getAllModifiableAttributes() {
        if (attributes.isEmpty())
            return Collections.emptySet();
        return attributes;
    }

    public Set<Method> getAllModifiableMethods() {
        if (methods.isEmpty())
            return Collections.emptySet();
        return methods;
    }

    public List<Method> getAllModifiableAbstractMethods() {
        List<Method> abstractMethods = new ArrayList<>();
        for (Method method : getAllModifiableMethods())
            if (method.isAbstract())
                abstractMethods.add(method);
        return abstractMethods;
    }

    public Set<Concern> getPriConcerns() {
        Set<Concern> concerns = new HashSet<>(getOwnConcerns());
        for (Method method : getAllMethods()) {
            for (Concern stereotype : method.getAllConcerns()) {
                if (!listContainConcern(concerns, stereotype))
                    concerns.add(stereotype);
            }
        }
        for (Attribute attribute : getAllAttributes()) {
            for (Concern stereotype : attribute.getAllConcerns()) {
                if (!listContainConcern(concerns, stereotype))
                    concerns.add(stereotype);
            }
        }
        return concerns;
    }

    public boolean listContainConcern(Set<Concern> listConcerns, Concern concern) {
        for (Concern concernFromList : listConcerns) {
            if (concernFromList.getName().equalsIgnoreCase(concern.getName()))
                return true;
        }
        return false;
    }
}