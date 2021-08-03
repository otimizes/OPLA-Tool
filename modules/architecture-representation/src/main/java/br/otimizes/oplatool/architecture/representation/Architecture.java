package br.otimizes.oplatool.architecture.representation;

import br.otimizes.oplatool.architecture.flyweights.VariabilityFlyweight;
import br.otimizes.oplatool.architecture.flyweights.VariantFlyweight;
import br.otimizes.oplatool.architecture.flyweights.VariationPointFlyweight;
import br.otimizes.oplatool.architecture.generate.GenerateArchitecture;
import br.otimizes.oplatool.architecture.generate.GenerateArchitectureSMarty;
import br.otimizes.oplatool.architecture.helpers.StatisticalMethodsHelper;
import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.representation.architectureControl.ArchitectureFindElementControl;
import br.otimizes.oplatool.architecture.representation.architectureControl.ArchitectureRemoveElementControl;
import br.otimizes.oplatool.architecture.representation.relationship.*;
import br.otimizes.oplatool.common.Variable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rits.cloning.Cloner;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Architecture representation class
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
@JsonIgnoreProperties({"elements", "elementsWithPackages", "allConcerns", "allPackages", "allMethods", "packages",
        "allPackagesAllowedModification", "interfaces", "allInterfaces", "classes", "allClasses",
        "allVariationPoints", "allVariants", "allVariabilities", "freezedElements", "LOGGER", "relationshipHolder",
        "attributes", "allAttributes", "upperBound", "lowerBound", "variableType", "value", "concerns", "types",
        "variationPoints", "variabilities", "variants", "editableListPackages", "editableListClasses", "editableListPackages",
        "editableListInterfaces", "relationships", "implementedInterfaces", "createClass", "allModifiableInterfaces", "allModifiableClasses"})
public class Architecture extends Variable {

    private static final Logger LOGGER = Logger.getLogger(Architecture.class);
    private static final long serialVersionUID = -7764906574709840088L;
    public static String ARCHITECTURE_TYPE = "br.otimizes.oplatool.architecture.representation.Architecture";
    private Cloner cloner;
    private Set<Package> packages = new HashSet<>();
    private final Set<Class> classes = new HashSet<>();
    private final Set<Interface> interfaces = new HashSet<>();
    private String name;
    private boolean appliedPatterns;
    private final RelationshipsHolder relationshipHolder = new RelationshipsHolder();
    private ArrayList<Concern> concerns = new ArrayList<>();
    private ArrayList<TypeSmarty> types = new ArrayList<>();
    private boolean isSMarty = false;
    private boolean toSMarty = false;
    private String projectID = "5b729c3f25e758ce87cf8d710761283c";
    private String projectName = "Project0";
    private String projectVersion = "1.0";
    private String diagramID = "DIAGRAM#1";
    private String diagramName = "DIAGRAM#1";
    private final List<VariationPoint> variationPoints = new ArrayList<>();
    private final List<Variability> variabilities = new ArrayList<>();
    private final List<Variant> variants = new ArrayList<>();
    private int linkOverloadViolation = 0;
    private int exceedLink = 0;

    public Architecture(String name) {
        setName(name);
    }

    public ArrayList<String> getClassesWithoutRelationship() {
        ArrayList<String> lstWithoutRelationship = new ArrayList<>();
        for (Class c : this.getAllClasses()) {
            if (c.getRelationships().size() == 0) {
                lstWithoutRelationship.add(c.getId());
            }
        }
        return lstWithoutRelationship;
    }

    public ArrayList<String> getInterfacesWithoutRelationship() {
        ArrayList<String> lstWithoutRelationship = new ArrayList<>();
        for (Interface i : this.getAllInterfaces()) {
            if (i.getRelationships().size() == 0) {
                lstWithoutRelationship.add(i.getId());
            }
        }
        return lstWithoutRelationship;
    }

    public void clearArchitecture() {
        this.classes.clear();
        this.interfaces.clear();
        this.packages.clear();
        this.relationshipHolder.clearLists();
    }

    public void matchRequiredAndImplementedInterface() {
        for (Package pkg : getAllPackages()) {
            pkg.matchImplementedInterface(this);
            pkg.matchRequiredInterface(this);
        }
        for (Class clazz : getAllClasses()) {
            clazz.matchImplementedInterface(this);
            clazz.matchRequiredInterface(this);
        }
    }

    public void removeAllPackages() {
        this.packages = new HashSet<>();
    }

    public void removePackageByID(String id) {
        Set<Package> newHash = new HashSet<>();
        for (Package packageToMatch : this.packages) {
            if (!packageToMatch.getId().equals(id)) {
                newHash.add(packageToMatch);
            }
        }
        this.packages.clear();
        this.packages.addAll(newHash);
    }

    public void removeMethodByID(String id) {
        for (Interface interfaceToRemoveMethod : getAllInterfaces()) {
            interfaceToRemoveMethod.removeOperationByID(id);
        }
        for (Class classToRemoveMethod : getAllClasses()) {
            classToRemoveMethod.removeMethodByID(id);
        }
    }

    public void removeMethodOfClassByID(String id) {
        for (Class classToRemoveMethod : getAllClasses()) {
            classToRemoveMethod.removeMethodByID(id);
        }
    }

    public void removeOperationOfInterfaceByID(String id) {
        for (Interface interfaceToRemoveMethod : getAllInterfaces()) {
            interfaceToRemoveMethod.removeOperationByID(id);
        }
    }

    public void removeAttributeByID(String id) {
        for (Class classToRemoveAttribute : getAllClasses()) {
            classToRemoveAttribute.removeAttributeByID(id);
        }
    }

    public Element findClassOrInterfaceOfMethodByID(String id) {
        for (Interface interfaceToMatch : getAllInterfaces()) {
            for (Method method : interfaceToMatch.getMethods()) {
                if (method.getId().equals(id))
                    return interfaceToMatch;
            }
        }
        for (Class classToMatch : getAllClasses()) {
            for (Method method : classToMatch.getAllMethods()) {
                if (method.getId().equals(id))
                    return classToMatch;
            }
        }
        return null;
    }

    public Class findClassOfMethodByID(String id) {
        for (Class clazzToMatch : getAllClasses()) {
            for (Method method : clazzToMatch.getAllMethods()) {
                if (method.getId().equals(id))
                    return clazzToMatch;
            }
        }
        return null;
    }

    public Interface findInterfaceOfOperationByID(String id) {
        for (Interface interfaceToMatch : getAllInterfaces()) {
            for (Method method : interfaceToMatch.getMethods()) {
                if (method.getId().equals(id))
                    return interfaceToMatch;
            }
        }
        return null;
    }

    public Class findClassOfAttributeByID(String id) {
        for (Class classToMatch : getAllClasses()) {
            for (Attribute attribute : classToMatch.getAllAttributes()) {
                if (attribute.getId().equals(id))
                    return classToMatch;
            }
        }
        return null;
    }

    public void removeClassByID(String id) {
        addAllClassesThatMatchToId(id, this.classes);
        for (Package pkg : this.packages) {
            pkg.removeClassByID(id);
        }
    }

    static void addAllClassesThatMatchToId(String id, Set<Class> classes) {
        Set<Class> newHash = new HashSet<>();
        for (Class classToMatch : classes) {
            if (!classToMatch.getId().equals(id)) {
                newHash.add(classToMatch);
            }
        }
        classes.clear();
        classes.addAll(newHash);
    }

    public Package findPackageOfInterface(Interface targetInterface) {
        String packageName = UtilResources.extractPackageName(targetInterface.getNamespace());
        return findPackageByName(packageName);
    }

    public ArrayList<Concern> getConcerns() {
        return concerns;
    }

    public void setConcerns(ArrayList<Concern> concerns) {
        this.concerns = concerns;
        ConcernHolder.INSTANCE.getConcerns().clear();
        ConcernHolder.INSTANCE.allowedConcerns().clear();
        for (Concern c : this.concerns) {
            if (!c.getPrimitive()) {
                ConcernHolder.INSTANCE.getConcerns().put(c.getName(), c);
                ConcernHolder.INSTANCE.allowedConcerns().add(c);
            }
        }
    }

    public Concern findConcernByName(String name) {
        return ArchitectureFindElementControl.getInstance().findConcernByName(this, name);
    }

    public ArrayList<Interface> getDuplicatedInterface() {
        ArrayList<String> matchedInterfaces = new ArrayList<>();
        ArrayList<Interface> duplicatedInterfaces = new ArrayList<>();
        for (Interface interfaceFromArchitecture : getInterfaces()) {
            if (matchedInterfaces.contains(interfaceFromArchitecture.getId())) {
                duplicatedInterfaces.add(interfaceFromArchitecture);
            }
            matchedInterfaces.add((interfaceFromArchitecture.getId()));
        }
        for (Package pkg : getAllPackages()) {
            for (Interface interfaceFromPackages : pkg.getAllInterfaces()) {
                if (matchedInterfaces.contains(interfaceFromPackages.getId())) {
                    duplicatedInterfaces.add(interfaceFromPackages);
                }
                matchedInterfaces.add((interfaceFromPackages.getId()));
            }
        }
        return duplicatedInterfaces;
    }

    public ArrayList<TypeSmarty> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<TypeSmarty> types) {
        this.types = types;
    }

    public TypeSmarty findTypeSMartyByID(String id) {
        return ArchitectureFindElementControl.getInstance().findTypeSMartyByID(this, id);
    }

    public TypeSmarty findTypeSMartyByName(String name) {
        return ArchitectureFindElementControl.getInstance().findTypeSMartyByName(this, name);
    }

    public TypeSmarty findReturnTypeSMartyByName(String name) {
        return ArchitectureFindElementControl.getInstance().findReturnTypeSMartyByName(this, name);
    }

    public boolean isSMarty() {
        return isSMarty;
    }

    public void setSMarty(boolean SMarty) {
        isSMarty = SMarty;
    }

    public boolean isToSMarty() {
        return toSMarty;
    }

    public void setToSMarty(boolean toSMarty) {
        this.toSMarty = toSMarty;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectVersion() {
        return projectVersion;
    }

    public void setProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
    }

    public String getDiagramID() {
        return diagramID;
    }

    public void setDiagramID(String diagramID) {
        this.diagramID = diagramID;
    }

    public String getDiagramName() {
        return diagramName;
    }

    public void setDiagramName(String diagramName) {
        this.diagramName = diagramName;
    }

    public Element findElementByNameInPackageAndSubPackage(String elementName) {
        return ArchitectureFindElementControl.getInstance().findElementByNameInPackageAndSubPackage(this, elementName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name : "";
    }

    public List<Element> getElements() {
        final List<Element> elements = new ArrayList<>();
        for (Package pkg : getAllPackages())
            elements.addAll(pkg.getElements());
        elements.addAll(this.classes);
        elements.addAll(this.interfaces);
        return elements;
    }

    public List<Element> getElementsWithPackages() {
        final List<Element> elementsAllowingModification = new ArrayList<>();
        for (Package pkg : getAllPackagesAllowedModification()) {
            elementsAllowingModification.add(pkg);
            elementsAllowingModification.addAll(pkg.getElements());
        }
        elementsAllowingModification.addAll(this.classes);
        elementsAllowingModification.addAll(this.interfaces);
        return elementsAllowingModification;
    }

    public List<Element> findElementByNumberId(Double idElem) {
        List<Element> elementsWithPackages = getElementsWithPackages();
        return elementsWithPackages.stream().filter(e -> Double.valueOf(e.getNumberId())
                .equals(idElem)).collect(Collectors.toList());
    }

    public List<Concern> getAllConcerns() {
        final List<Concern> concerns = new ArrayList<>();
        for (Map.Entry<String, Concern> entry : ConcernHolder.INSTANCE.getConcerns().entrySet()) {
            concerns.add(entry.getValue());
        }
        return concerns;
    }

    public List<String> getAllComments() {
        List<String> comments = getAllClasses().stream().filter(Element::hasComments)
                .map(Element::getStringComments).collect(Collectors.toList());
        comments.addAll(getAllPackages().stream().filter(Element::hasComments)
                .map(Element::getStringComments).collect(Collectors.toList()));
        comments.addAll(getAllInterfaces().stream().filter(Element::hasComments)
                .map(Element::getStringComments).collect(Collectors.toList()));
        return comments;
    }

    public Set<Package> getAllPackages() {
        return Collections.unmodifiableSet(this.packages);
    }

    public Set<Package> getEditableListPackages() {
        return this.packages;
    }

    public List<Attribute> getAllAttributes() {
        List<Attribute> attributes = new ArrayList<>();
        for (Class classFromPackages : this.getAllClasses()) {
            attributes.addAll(classFromPackages.getAllAttributes());
        }
        return attributes;
    }

    public List<Method> getAllMethods() {
        List<Method> attrs = new ArrayList<>();
        for (Class classFromPackages : this.getAllClasses()) {
            attrs.addAll(classFromPackages.getAllMethods());
        }
        for (Interface interfaceFromPackages : this.getAllInterfaces()) {
            attrs.addAll(interfaceFromPackages.getMethods());
        }
        return attrs;
    }

    public List<Package> getAllPackagesAllowedModification() {
        return new ArrayList<>(this.packages);
    }

    public Set<Interface> getInterfaces() {
        return Collections.unmodifiableSet(this.interfaces);
    }

    public Set<Interface> getEditableListInterfaces() {
        return this.interfaces;
    }

    public Set<Interface> getAllInterfaces() {
        final Set<Interface> interfaces = new HashSet<>();
        for (Package pkg : this.packages)
            interfaces.addAll(pkg.getAllInterfaces());
        interfaces.addAll(this.interfaces);
        return Collections.unmodifiableSet(interfaces);
    }

    public Set<Class> getClasses() {
        return Collections.unmodifiableSet(this.classes);
    }

    public Set<Class> getEditableListClasses() {
        return this.classes;
    }

    public Set<Class> getAllClasses() {
        final Set<Class> classes = new HashSet<>();
        for (Package pkg : this.packages)
            classes.addAll(pkg.getAllClasses());
        classes.addAll(this.classes);
        return Collections.unmodifiableSet(classes);
    }

    public Element findElementByName(String name, String type) {
        return ArchitectureFindElementControl.getInstance().findElement(this, name, type);
    }

    public List<Class> findClassByName(String className) {
        return ArchitectureFindElementControl.getInstance().findClassByName(this, className);
    }

    public Element findElementByName(String elementName) {
        return ArchitectureFindElementControl.getInstance().findElementByName(this, elementName);
    }

    public Interface findInterfaceByName(String interfaceName) {
        return ArchitectureFindElementControl.getInstance().findInterfaceByName(this, interfaceName);
    }

    public Package findPackageByName(String packageName) {
        return ArchitectureFindElementControl.getInstance().findPackageByName(this, packageName);
    }

    public void removeInterfaceByID(String id) {
        ArchitectureRemoveElementControl.getInstance().removeInterfaceByID(this, id);
    }

    public Package findPackageByID(String id) {
        return ArchitectureFindElementControl.getInstance().findPackageByID(this, id);
    }

    public Package createPackage(String packageName) {
        Package pkg = new Package(getRelationshipHolder(), packageName);
        this.packages.add(pkg);
        return pkg;
    }

    public Package createPackage(String packageName, String id) {
        Package pkg = new Package(getRelationshipHolder(), packageName, id);
        this.packages.add(pkg);
        return pkg;
    }

    public void removePackage(Package p) {
        ArchitectureRemoveElementControl.getInstance().removePackage(this, p);
    }

    public Interface createInterface(String interfaceName) {
        Interface newInterface = new Interface(getRelationshipHolder(), interfaceName);
        this.addExternalInterface(newInterface);
        return newInterface;
    }

    public Interface createInterface(String interfaceName, String id) {
        Interface newInterface = new Interface(getRelationshipHolder(), interfaceName, id);
        this.addExternalInterface(newInterface);
        return newInterface;
    }

    public Class createClass(String klassName, boolean isAbstract) {
        Class classWithRelationship = new Class(getRelationshipHolder(), klassName, isAbstract);
        this.addExternalClass(classWithRelationship);
        return classWithRelationship;
    }

    public void removeInterface(Interface interfaceToRemove) {
        ArchitectureRemoveElementControl.getInstance().removeInterface(this, interfaceToRemove);
    }

    public void removeClass(Element classToRemove) {
        ArchitectureRemoveElementControl.getInstance().removeClass(this, classToRemove);
    }

    public List<VariationPoint> getAllVariationPoints() {
        if (isSMarty) {
            return variationPoints;
        }
        return VariationPointFlyweight.getInstance().getVariationPoints();
    }

    public List<Variant> getAllVariants() {
        if (isSMarty) {
            return variants;
        }
        return VariantFlyweight.getInstance().getVariants();
    }

    public List<Variability> getAllVariabilities() {
        if (isSMarty) {
            return variabilities;
        }
        return VariabilityFlyweight.getInstance().getVariabilities();
    }

    public Class findClassById(String classId) {
        return ArchitectureFindElementControl.getInstance().findClassById(this, classId);
    }

    public Interface findInterfaceById(String interfaceId) {
        return ArchitectureFindElementControl.getInstance().findInterfaceById(this, interfaceId);
    }

    public void addExternalInterface(Interface externalInterface) {
        if (interfaces.add(externalInterface))
            LOGGER.info("Interface: " + externalInterface.getName() + " added on architecture");
        else
            LOGGER.info("Tried to add: " + externalInterface.getName() + ", but it couldn't");
    }

    public void moveElementToPackage(Element elementToMove, Package destinationPackage) {
        if (destinationPackage.getElements().contains(elementToMove)) {
            return;
        }
        for (Class classFromArchitecture : this.classes) {
            if (classFromArchitecture.getId().equals(elementToMove.getId()))
                return;
        }
        for (Interface interfaceFromArchitecture : this.interfaces) {
            if (interfaceFromArchitecture.getId().equals(elementToMove.getId()))
                return;
        }
        String oldPackageName = UtilResources.extractPackageName(elementToMove.getNamespace());
        if (this.packages.contains(destinationPackage)) {
            if (oldPackageName.equals("model")) {
                addClassOrInterface(elementToMove, destinationPackage);
                this.removeOnlyElement(elementToMove);
            } else {
                Package oldPackage = this.findPackageByName(oldPackageName);
                if (oldPackage != null) {
                    addClassOrInterface(elementToMove, destinationPackage);
                    oldPackage.removeOnlyElement(elementToMove);
                }
            }
        }
        elementToMove.setNamespace(ArchitectureHolder.getName() + "::" + destinationPackage.getName());
    }

    public void movePackageToParent(String packageID, String parentID) {
        Package origin = findPackageByID(packageID);
        Package newParent = findPackageByID(parentID);
        for (Package pkg : this.packages) {
            if (packageID.equalsIgnoreCase(pkg.getId())) {
                this.packages.remove(origin);
                newParent.getNestedPackages().add(origin);
                return;
            }
            for (Package subP : pkg.getNestedPackages()) {
                if (packageID.equalsIgnoreCase(pkg.getId())) {
                    subP.getNestedPackages().remove(pkg);
                    newParent.getNestedPackages().add(origin);
                    return;
                }
                removeSubPackageByID(subP, packageID);
            }
        }
    }

    public void removeSubPackageByID(Package subPackage, String id) {
        ArchitectureRemoveElementControl.getInstance().removeSubPackageByID(subPackage, id);
    }

    public void addClassOrInterface(Element element, Package pkg) {
        if (element instanceof Class) {
            pkg.addExternalClass((Class) element);
        } else if (element instanceof Interface) {
            pkg.addExternalInterface((Interface) element);
        }
    }

    public OperationsOverGeneralization forGeneralization() {
        return new OperationsOverGeneralization(this);
    }

    public OperationsOverAbstraction forAbstraction() {
        return new OperationsOverAbstraction(this);
    }

    public boolean removeRelationship(Relationship as) {
        return ArchitectureRemoveElementControl.getInstance().removeRelationship(this, as);
    }

    public OperationsOverUsage forUsage() {
        return new OperationsOverUsage(this);
    }

    public Variable deepCopy() {
        return this.deepClone();
    }

    public Architecture deepClone() {
        if (cloner == null)
            cloner = new Cloner();
        return cloner.deepClone(this);
    }

    public boolean addImplementedInterface(Interface supplier, Class client) {
        if (!client.isTotalyFreezed()) {
            if (!doesHaveRelationship(supplier, client)) {
                if (addRelationship(new RealizationRelationship(client, supplier, "", UtilResources.getRandomUUID()))) {
                    LOGGER.info("ImplementedInterface: " + supplier.getName() + " added on class: " + client.getName());
                    return true;
                } else {
                    LOGGER.info("Tried to add interface " + supplier.getName() + " as interface implemented by class: " + client.getName());
                    return false;
                }
            }
        }
        return false;
    }

    private boolean doesHaveRelationship(Interface supplier, Element client) {
        for (Relationship relationship : relationshipHolder.getAllRelationships()) {
            if (relationship instanceof RealizationRelationship)
                if (((RealizationRelationship) relationship).getClient().equals(client)
                        && ((RealizationRelationship) relationship).getSupplier().equals(supplier))
                    return true;
            if (relationship instanceof DependencyRelationship)
                if (((DependencyRelationship) relationship).getClient().equals(client)
                        && ((DependencyRelationship) relationship).getSupplier().equals(supplier))
                    return true;
        }
        return false;
    }

    public boolean addImplementedInterface(Interface supplier, Package client) {
        if (!client.isTotalyFreezed()) {
            if (!doesHaveRelationship(supplier, client)) {
                if (addRelationship(new RealizationRelationship(client, supplier, "", UtilResources.getRandomUUID()))) {
                    LOGGER.info("Implemented Interface: " + supplier.getName() + " added on package: " + client.getName());
                    return true;
                } else {
                    LOGGER.info("Tried to add interface " + supplier.getName() + " as interface implemented on package: " + client.getName());
                    return false;
                }
            }
        }
        return false;
    }

    public void removeImplementedInterface(Interface interfaceToRemove, Package sourcePackage) {
        ArchitectureRemoveElementControl.getInstance().removeImplementedInterface(this, interfaceToRemove, sourcePackage);
    }

    public void removeImplementedInterface(Class foo, Interface inter) {
        ArchitectureRemoveElementControl.getInstance().removeImplementedInterface(this, foo, inter);
    }

    public void addRequiredInterface(Interface supplier, Class client) {
        if (!doesHaveRelationship(supplier, client)) {
            if (addRelationship(new DependencyRelationship(supplier, client, "", UtilResources.getRandomUUID())))
                LOGGER.info("Required Interface: " + supplier.getName() + " added on: " + client.getName());
            else
                LOGGER.info("Tried to add Required Interface: " + supplier.getName() + " on : " + client.getName() + " but it couldn't");
        }
    }

    public void addRequiredInterface(Interface supplier, Package client) {
        if (!doesHaveRelationship(supplier, client)) {
            if (addRelationship(new DependencyRelationship(supplier, client, "", UtilResources.getRandomUUID())))
                LOGGER.info("Required Interface: " + supplier.getName() + " added on: " + client.getName());
            else
                LOGGER.info("Tried to add Required Interface: " + supplier.getName() + " a : " + client.getName() + " but it couldn't");
        }
    }

    public void deleteClassRelationships(Class classToRemoveRelationship) {
        Collection<Relationship> relationships = new ArrayList<>(classToRemoveRelationship.getRelationships());
        for (Relationship relationship : relationships) {
            this.removeRelationship(relationship);
        }
    }

    public boolean addRelationship(Relationship relationship) {
        if (!relationshipHolder.haveRelationship(relationship)) {
            if (relationshipHolder.addRelationship(relationship)) {
                LOGGER.info("Relationship: " + relationship.getType() + " added on architecture ("
                        + UtilResources.detailLogRelationship(relationship) + ")");
                return true;
            } else {
                LOGGER.info("Tried to add relationship: " + relationship.getType() + " on architecture, but it couldn't");
                return false;
            }
        }
        return false;
    }

    public Package findPackageOfClass(Class targetClass) {
        return ArchitectureFindElementControl.getInstance().findPackageOfClass(this, targetClass);
    }

    public Package findPackageOfElementID(String id) {
        return ArchitectureFindElementControl.getInstance().findPackageOfElement(this, id);
    }

    public void save(Architecture architecture, String pathToSave, String indexOfSolution) {
        if (this.toSMarty) {
            GenerateArchitectureSMarty generate = new GenerateArchitectureSMarty();
            generate.generate(architecture, pathToSave + architecture.getName() + indexOfSolution);
        } else {
            GenerateArchitecture generate = new GenerateArchitecture();
            generate.generate(architecture, pathToSave + architecture.getName() + indexOfSolution);
        }
    }

    public void saveToSMarty(Architecture architecture, String pathToSave) {
        GenerateArchitectureSMarty generate = new GenerateArchitectureSMarty();
        generate.generate(architecture, pathToSave);
    }

    public Element findElementById(String id) {
        return ArchitectureFindElementControl.getInstance().findElementById(this, id);
    }

    public Element findMethodById(String id) {
        return ArchitectureFindElementControl.getInstance().findMethodById(this, id);
    }

    public Element findAttributeById(String id) {
        return ArchitectureFindElementControl.getInstance().findAttributeById(this, id);
    }

    public void addPackage(Package packageToAdd) {
        if (this.packages.add(packageToAdd))
            LOGGER.info("Package: " + packageToAdd.getName() + " added on architecture");
        else
            LOGGER.info("Tried to add the package: " + packageToAdd.getName() + " on architecture, but it couldn't");
    }

    public void addExternalClass(Class klass) {
        if (this.classes.add(klass))
            LOGGER.info("Class: " + klass.getName() + " added on architecture");
        else
            LOGGER.info("Tried to add class: " + klass.getName() + " on architecture, but it couldn't");
    }

    public void removeRequiredInterface(Interface supplier, Package client) {
        ArchitectureRemoveElementControl.getInstance().removeRequiredInterface(this, supplier, client);
    }

    public void removeRequiredInterface(Interface supplier, Class client) {
        ArchitectureRemoveElementControl.getInstance().removeRequiredInterface(this, supplier, client);
    }

    public boolean removeOnlyElement(Element element) {
        return ArchitectureRemoveElementControl.getInstance().removeOnlyElement(this, element);
    }

    public void setCloner(Cloner cloner) {
        this.cloner = cloner;
    }

    public RelationshipsHolder getRelationshipHolder() {
        return relationshipHolder;
    }

    public boolean isAppliedPatterns() {
        return appliedPatterns;
    }

    public void setAppliedPatterns(boolean b) {
        this.appliedPatterns = b;
    }

    public void addAllClasses(Set<Class> classes) {
        this.classes.clear();
        this.classes.addAll(classes);
    }

    public void addAllPackages(Set<Package> packages) {
        this.packages.clear();
        this.packages.addAll(packages);
    }

    public void addAllInterfaces(Set<Interface> interfaces) {
        this.interfaces.clear();
        this.interfaces.addAll(interfaces);
    }

    @Override
    public String toString() {
        return "Architecture{" +
                "packages=" + packages +
                ", classes=" + classes +
                ", interfaces=" + interfaces +
                ", name='" + name + '\'' +
                ", appliedPatterns=" + appliedPatterns +
                ", relationshipHolder=" + relationshipHolder +
                '}';
    }

    public static Double median(List<Integer> values) {
        List<Integer> sortedValues = values.stream().sorted().collect(Collectors.toList());
        return sortedValues.size() % 2 == 0 ? (sortedValues.get((sortedValues.size() / 2) - 1)
                + sortedValues.get((sortedValues.size() / 2))) / 2 : Double.valueOf(sortedValues.get((int) Math.floor(sortedValues.size() / 2)));
    }

    public static Double mean(List<Integer> values) {
        return (double) (values.stream().mapToInt(Integer::intValue).sum() / values.size());
    }

    public static Double sum(List<Integer> values) {
        return (double) (values.stream().mapToInt(Integer::intValue).sum());
    }

    public String getDetailedString() {
        return this.getDetailedString(true);
    }

    public String getDetailedString(boolean withAttributes) {
        List<Element> freezedElements = getFreezedElements();
        StringBuilder str = new StringBuilder();
        str.append("Packages: ").append(getAllPackages()).append(", qtdPackages: ").append(getAllPackages().size())
                .append(", qtdClasses: ").append(getAllClasses().size())
                .append(", qtdInterfaces: ").append(getAllInterfaces().size())
                .append(", qtdFreezedElements: ").append(freezedElements.size())
                .append(", \nfreezedElements: ").append(freezedElements.stream().map(s -> s.getName() + ":" + s.getTypeElement())
                .collect(Collectors.toList()))
                .append(", \nqtdAggregation: ").append(getRelationshipHolder().getAllAgragations().size())
                .append(", \ngetAllCompositions: ").append(getRelationshipHolder().getAllCompositions().size())
                .append(", \ngetAllDependencies: ")
                .append(getRelationshipHolder().getAllDependencies().size())
                .append(", \ngetAllAssociations: ").append(getRelationshipHolder().getAllAssociations().size())
                .append(", \ngetAllGeneralizations: ").append(getRelationshipHolder().getAllGeneralizations().size())
                .append(", \ngetAllRelationships: ").append(getRelationshipHolder().getAllRelationships().size()).append("\n");
        if (withAttributes) {
            str.append("    Classes: \n");
            str.append(getAllClasses().stream().map(clazz -> {
                return "       " + clazz.getName() +
                        " \n           qtdAttributes: " + clazz.getAllAttributes().size() +
                        " \n           qtdAbstractsMethods: " + clazz.getAllAbstractMethods().size() +
                        " \n           qtdConcerns: " + clazz.getAllConcerns().size() +
                        " \n           qtdAssociations: " + clazz.getAllAssociationClass().size() +
                        " \n           qtdMethods: " + clazz.getAllMethods().size() +
                        " \n           attributes: " + clazz.getAllAttributes().toString() +
                        " \n           methods: " + clazz.getAllMethods().toString() + "\n";
            }).collect(Collectors.joining()));
        }
        return str.toString();
    }

    public List<Element> getFreezedElements() {
        return getElementsWithPackages().stream().filter(Element::isFreezeByDM).collect(Collectors.toList());
    }

    public String toStringFreezedElements() {
        return getFreezedElements().stream().map(e -> e.getName() + ":" + e.getTypeElement()).collect(Collectors.toList()).toString();
    }

    public void addElement(Element element) {
        if (element instanceof Class) {
            addExternalClass((Class) element);
        } else if (element instanceof Interface) {
            addExternalInterface((Interface) element);
        } else if (element instanceof Package) {
            addPackage((Package) element);
        }
    }

    public static Logger getLOGGER() {
        return LOGGER;
    }

    public Set<Interface> getAllModifiableInterfaces() {
        final Set<Interface> interfaces = new HashSet<>();
        for (Package p : this.packages)
            interfaces.addAll(p.getAllInterfaces());

        interfaces.addAll(this.interfaces);
        return interfaces;
    }

    public Set<Class> getAllModifiableClasses() {
        final Set<Class> modifiableClasses = new HashSet<>();
        for (Package p : this.packages)
            modifiableClasses.addAll(p.getAllClasses());

        modifiableClasses.addAll(this.classes);
        return modifiableClasses;
    }

    public ArrayList<Integer> getLinkOverload(Element element) {
        ArrayList<Integer> linkOverload = new ArrayList<>();
        int input = 0;
        int output = 0;
        int both = 0;

        if (element instanceof Linkable) {
            Linkable classElement = (Linkable) element;
            for (Relationship relationship : classElement.getRelationships()) {
                if (relationship instanceof DependencyRelationship) {
                    DependencyRelationship dependencyRelationship = (DependencyRelationship) relationship;
                    if (dependencyRelationship.getClient().getId().equals(classElement.getId())) {
                        input++;
                    } else {
                        output++;
                    }
                }
                if (relationship instanceof RealizationRelationship) {
                    RealizationRelationship realizationRelationship = (RealizationRelationship) relationship;
                    if (realizationRelationship.getClient().getId().equals(classElement.getId())) {
                        output++;
                    } else {
                        input++;
                    }
                }
                if (relationship instanceof AbstractionRelationship) {
                    AbstractionRelationship abstractionRelationship = (AbstractionRelationship) relationship;
                    if (abstractionRelationship.getClient().getId().equals(classElement.getId())) {
                        output++;
                    } else {
                        input++;
                    }
                }
                if (relationship instanceof AssociationRelationship) {
                    both++;
                }
                if (relationship instanceof AssociationClassRelationship) {
                    both++;
                }
            }
        }

        linkOverload.add(input);
        linkOverload.add(output);
        linkOverload.add(both);
        return linkOverload;
    }

    public ArrayList<Integer> getTHZLinkOverload() {
        ArrayList<Integer> linkOverload = new ArrayList<>();
        ArrayList<Integer> inputLink = new ArrayList<>();
        ArrayList<Integer> inputOut = new ArrayList<>();
        ArrayList<Integer> bothLink = new ArrayList<>();
        ArrayList<Integer> listOfOverloads;

        for (Class classToGetOverload : this.getAllClasses()) {
            listOfOverloads = getLinkOverload(classToGetOverload);
            inputLink.add(listOfOverloads.get(0));
            inputOut.add(listOfOverloads.get(1));
            bothLink.add(listOfOverloads.get(2));
        }
        for (Interface interfaceToGetOverload : this.getAllInterfaces()) {
            listOfOverloads = getLinkOverload(interfaceToGetOverload);
            inputLink.add(listOfOverloads.get(0));
            inputOut.add(listOfOverloads.get(1));
            bothLink.add(listOfOverloads.get(2));
        }
        linkOverload.add(getThz(inputLink));
        linkOverload.add(getThz(inputOut));
        linkOverload.add(getThz(bothLink));
        return linkOverload;
    }


    public int getThz(ArrayList<Integer> list) {
        Double mean = 0.0;
        for (Integer n : list) {
            mean += n;
        }
        mean = mean / list.size();
        System.out.println(("Mean:") + mean);
        Double std = StatisticalMethodsHelper.getStandardDeviation(list);
        System.out.println(("Standard Deviation:" + std));
        double THzb = mean + std;
        System.out.println("Sum:" + THzb);
        return (int) Math.ceil(THzb);
    }

    public void verifyIfLinkOverloadAnomalyExists(ArrayList<Integer> thrz) {
        List<Element> detectedLinksOverload = new ArrayList<>();
        List<Integer> linkOverloadsFromElement = new ArrayList<>();
        linkOverloadsFromElement.addAll(getIntegers(thrz, detectedLinksOverload, linkOverloadsFromElement, this.getAllClasses()));
        linkOverloadsFromElement.addAll(getIntegers(thrz, detectedLinksOverload, linkOverloadsFromElement, this.getAllInterfaces()));
        linkOverloadViolation = detectedLinksOverload.size();
        detectedLinksOverload.clear();
        linkOverloadsFromElement.clear();
        verifyIfLinkOverloadExceedTHZ(thrz);
    }

    private List<Integer> getIntegers(ArrayList<Integer> thrz, List<Element> detectedLinksOverload,
                                      List<Integer> linkOverloadsFromElement, Set<? extends Element> elements) {
        for (Element class_ : elements) {
            linkOverloadsFromElement = getLinkOverload(class_);
            if (linkOverloadsFromElement.get(0) > thrz.get(0)) {
                detectedLinksOverload.add(class_);
                continue;
            }
            if (linkOverloadsFromElement.get(1) > thrz.get(1)) {
                detectedLinksOverload.add(class_);
                continue;
            }
            if (linkOverloadsFromElement.get(2) > thrz.get(2)) {
                detectedLinksOverload.add(class_);
            }
        }
        return linkOverloadsFromElement;
    }

    public void verifyIfLinkOverloadExceedTHZ(ArrayList<Integer> thrz) {
        exceedLink = 0;
        ArrayList<Integer> linkOverloadsFromElement = new ArrayList<>();
        linkOverloadsFromElement.addAll(getIntegers(thrz, linkOverloadsFromElement, this.getAllClasses()));
        linkOverloadsFromElement.addAll(getIntegers(thrz, linkOverloadsFromElement, this.getAllInterfaces()));
        linkOverloadsFromElement.clear();
    }

    private ArrayList<Integer> getIntegers(ArrayList<Integer> thrz, ArrayList<Integer> linkOverloadsFromElement,
                                           Set<? extends Element> elements) {
        for (Element class_ : elements) {
            linkOverloadsFromElement = getLinkOverload(class_);
            if (linkOverloadsFromElement.get(0) > thrz.get(0)) {
                exceedLink += (linkOverloadsFromElement.get(0) - thrz.get(0));
            }
            boolean hasAnomaly = linkOverloadsFromElement.get(1) > thrz.get(1);
            if (hasAnomaly) {
                exceedLink += (linkOverloadsFromElement.get(1) - thrz.get(1));
            }
            if (linkOverloadsFromElement.get(2) > thrz.get(2)) {
                exceedLink += (linkOverloadsFromElement.get(2) - thrz.get(2));
            }
        }
        return linkOverloadsFromElement;
    }

    public int getLinkOverloadViolation() {
        return linkOverloadViolation;
    }

    public void setLinkOverloadViolation(int linkOverloadViolation) {
        this.linkOverloadViolation = linkOverloadViolation;
    }

    public int getExceedLink() {
        return exceedLink;
    }

    public void setExceedLink(int exceedLink) {
        this.exceedLink = exceedLink;
    }
}