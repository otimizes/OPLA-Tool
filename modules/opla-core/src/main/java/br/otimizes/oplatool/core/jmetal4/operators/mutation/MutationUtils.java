package br.otimizes.oplatool.core.jmetal4.operators.mutation;

import br.otimizes.oplatool.architecture.exceptions.ConcernNotFoundException;
import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.relationship.AssociationRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.GeneralizationRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import br.otimizes.oplatool.core.jmetal4.util.PseudoRandom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utils to handle to the mutations
 */
public class MutationUtils {

    /**
     * Get a random element from list of elements
     *
     * @param allObjects list of elements
     * @param <T>        type of element
     * @return random element
     */
    public static <T> T randomObject(List<T> allObjects) {
        int numObjects = allObjects.size();
        int key;
        T object;
        if (numObjects == 0) {
            object = null;
        } else {
            key = PseudoRandom.randInt(0, numObjects - 1);
            object = allObjects.get(key);
        }
        return object;
    }

    /**
     * Get random package from architecture
     *
     * @param architecture architecture
     * @return random package
     */
    public static Package getRandomPackage(Architecture architecture) {
        Package sourceComp = randomObject(new ArrayList<Package>(architecture.getAllPackages()));
        while (sourceComp.isTotalyFreezed()) {
            sourceComp = randomObject(new ArrayList<Package>(architecture.getAllPackages()));
        }
        return sourceComp;
    }

    /**
     * Get random package from list of elements
     *
     * @param packages packages
     * @return random package
     */
    public static Package getRandomPackage(List<Package> packages) {
        Package selectedCompElem = randomObject(packages);
        while (selectedCompElem.isTotalyFreezed()) {
            selectedCompElem = randomObject(packages);
        }
        return selectedCompElem;
    }

    /**
     * Get random interface from list of elements
     *
     * @param interfaces interfaces
     * @return random interface
     */
    public static Interface getRandomInterface(List<Interface> interfaces) {
        Interface targetInterface = randomObject(interfaces);
        while (targetInterface.isTotalyFreezed()) {
            targetInterface = randomObject(interfaces);
        }
        return targetInterface;
    }

    /**
     * Get random class from list of classes
     *
     * @param classes classes
     * @return random class
     */
    public static Class getRandomClass(List<Class> classes) {
        Class sourceClassElem = randomObject(classes);
        while (sourceClassElem.isTotalyFreezed()) {
            sourceClassElem = randomObject(classes);
        }
        return sourceClassElem;
    }

    /**
     * Get random method from list of methods
     *
     * @param methods methods
     * @return random method
     */
    public static Method getRandomMethod(List<Method> methods) {
        Method method = randomObject(methods);
        while (method.isTotalyFreezed()) {
            method = randomObject(methods);
        }
        return method;
    }

    /**
     * Remove classes that has a pattern applied
     *
     * @param classes classes
     */
    public static void removeClassesInPatternStructureFromArray(List<Class> classes) {
        for (int i = 0; i < classes.size(); i++) {
            Class klass = classes.get(i);
            if (klass.getPatternsOperations().hasPatternApplied()) {
                classes.remove(i);
                i--;
            }
        }
    }

    /**
     * Remove interfaces that has a pattern applied
     *
     * @param interfaces interfaces
     */
    public static void removeInterfacesInPatternStructureFromArray(List<Interface> interfaces) {
        for (int i = 0; i < interfaces.size(); i++) {
            Interface anInterface = interfaces.get(i);
            if (anInterface.getPatternsOperations().hasPatternApplied()) {
                interfaces.remove(i);
                i--;
            }
        }
    }

    /**
     * Move method to new class
     *
     * @param architecture architecture
     * @param sourceClass  source class
     * @param methodsClass list of methods to move
     * @param targetClass  target class
     * @throws Exception default exception
     */
    public static void moveMethodToNewClass(Architecture architecture, Class sourceClass, List<Method> methodsClass, Class targetClass)
            throws Exception {
        Method targetMethod = getRandomMethod(methodsClass);
        sourceClass.moveMethodToClass(targetMethod, targetClass);
        for (Concern con : targetMethod.getOwnConcerns()) {
            targetClass.addConcern(con.getName());
        }
        createAssociation(architecture, targetClass, sourceClass);
    }

    /**
     * Verify if the class has generalizations
     *
     * @param clazz class
     * @return has generalizations
     */
    public static boolean searchForGeneralizations(Class clazz) {
        for (Relationship relationship : clazz.getRelationships()) {
            if (relationship instanceof GeneralizationRelationship) {
                if (((GeneralizationRelationship) relationship).getChild().equals(clazz)
                        || ((GeneralizationRelationship) relationship).getParent().equals(clazz)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Verify if the class has patterns
     *
     * @param clazz class
     * @return has patterns
     */
    public static boolean searchPatternsClass(Class clazz) {
        boolean ap = clazz.getPatternsOperations().hasPatternApplied();
        return !ap;
    }

    /**
     * Verify if the interface has patterns
     *
     * @param inter interface
     * @return has patterns
     */
    public static boolean searchPatternsInterface(Interface inter) {
        boolean ap = inter.getPatternsOperations().hasPatternApplied();
        return !ap;
    }

    /**
     * Move attribute between classes
     *
     * @param arch        architecture
     * @param targetClass target class
     * @param sourceClass source class
     * @throws Exception default exception
     */
    public static void moveAttribute(Architecture arch, Class targetClass, Class sourceClass) throws Exception {
        List<Attribute> attributesClass = new ArrayList<Attribute>(sourceClass.getAllAttributes().stream().filter(c -> !c.isTotalyFreezed()).collect(Collectors.toList()));
        if (searchPatternsClass(targetClass) && searchPatternsClass(sourceClass)) {
            if (attributesClass.size() >= 1) {
                if (sourceClass.moveAttributeToClass(randomObject(attributesClass), targetClass)) {
                    createAssociation(arch, targetClass, sourceClass);
                }
            }
        }
        attributesClass.clear();
    }

    /**
     * Create association between classes
     *
     * @param arch        architecture
     * @param targetClass target class
     * @param sourceClass source class
     */
    public static void createAssociation(Architecture arch, Class targetClass, Class sourceClass) {
        arch.addRelationship(new AssociationRelationship(targetClass, sourceClass));
    }

    /**
     * Move method between classes
     *
     * @param arch        architecture
     * @param targetClass target class
     * @param sourceClass source class
     */
    public static void moveMethod(Architecture arch, Class targetClass, Class sourceClass) {
        final List<Method> MethodsClass = new ArrayList<Method>(sourceClass.getAllMethods().stream().filter(c -> !c.isTotalyFreezed()).collect(Collectors.toList()));
        if (MethodsClass.size() >= 1) {
            if (sourceClass.moveMethodToClass(getRandomMethod(MethodsClass), targetClass)) {
                if (searchPatternsClass(sourceClass)) {
                    createAssociation(arch, targetClass, sourceClass);
                }
            }
        }
        MethodsClass.clear();
    }

    /**
     * Move attributes to target class
     *
     * @param arch        architecture
     * @param sourceClass source class
     * @param attributes  attributes
     * @param targetClass target class
     * @throws Exception default exception
     */
    public static void moveAttributeToNewClass(Architecture arch, Class sourceClass, List<Attribute> attributes,
                                               Class targetClass) throws Exception {
        Attribute targetAttribute = randomObject(attributes);
        sourceClass.moveAttributeToClass(targetAttribute, targetClass);
        for (Concern con : targetAttribute.getOwnConcerns()) {
            targetClass.addConcern(con.getName());
        }
        createAssociation(arch, targetClass, sourceClass);
    }

    /**
     * Verify if is same layer of packages
     *
     * @param source source package
     * @param target target package
     * @return is same layer
     */
    public static boolean checkSameLayer(Package source, Package target) {
        boolean sameLayer = false;
        if ((source.getName().endsWith("Mgr") && target.getName().endsWith("Mgr"))
                || (source.getName().endsWith("Ctrl") && target.getName().endsWith("Ctrl"))
                || (source.getName().endsWith("GUI") && target.getName().endsWith("GUI"))) {
            sameLayer = true;
        }
        return sameLayer;
    }

    /**
     * Get package suffix
     *
     * @param pack package
     * @return package suffix
     */
    public static String getSuffix(Package pack) {
        String suffix;
        if (pack.getName().endsWith("Mgr")) {
            suffix = "Mgr";
        } else if (pack.getName().endsWith("Ctrl")) {
            suffix = "Ctrl";
        } else if (pack.getName().endsWith("GUI")) {
            suffix = "GUI";
        } else {
            suffix = "";
        }
        return suffix;
    }

    /**
     * Verify if the concern is variation point
     *
     * @param arch    architecture
     * @param clazz   class
     * @param concern concern
     * @return is variation point
     */
    public static boolean isVarPointOfConcern(Architecture arch, Class clazz, Concern concern) {
        boolean isVariationPointConcern = false;
        Collection<Variability> variabilities = arch.getAllVariabilities();
        for (Variability variability : variabilities) {
            VariationPoint varPoint = variability.getVariationPoint();
            if (varPoint != null) {
                Class classVP = (Class) varPoint.getVariationPointElement();
                if (classVP.equals(clazz) && variability.getName().equals(concern.getName())) {
                    isVariationPointConcern = true;
                }
            }
        }
        return isVariationPointConcern;
    }

    /**
     * Verify if the concern is variant
     *
     * @param arch    architecture
     * @param clazz   class
     * @param concern concern
     * @return is variant
     */
    public static boolean isVariantOfConcern(Architecture arch, Class clazz, Concern concern) {
        boolean isVariantConcern = false;
        Collection<Variability> variabilities = arch.getAllVariabilities();
        for (Variability variability : variabilities) {
            VariationPoint varPoint = variability.getVariationPoint();
            if (varPoint != null) {
                for (Variant variant : varPoint.getVariants()) {
                    if (variant.getVariantElement().equals(clazz) && variability.getName().equals(concern.getName())) {
                        isVariantConcern = true;
                    }
                }
            } else {
                if (clazz.getVariantType() != null) {
                    if (clazz.getVariantType().equalsIgnoreCase("optional")) {
                        isVariantConcern = true;
                    }
                }
            }
        }
        //variabilities.clear();
        return isVariantConcern;
    }

    /**
     * Move generalization to other package
     *
     * @param classComp    class generalization
     * @param targetComp   target package
     * @param architecture architecture
     */
    public static void moveHierarchyToComponent(Class classComp, Package targetComp, Architecture architecture) {
        architecture.forGeneralization().moveGeneralizationToPackage(getGeneralizationRelationshipForClass(classComp),
                targetComp);
    }

    /**
     * Get generalizations of class
     *
     * @param clazz class
     * @return generalizations
     */
    public static GeneralizationRelationship getGeneralizationRelationshipForClass(Class clazz) {
        for (Relationship r : clazz.getRelationships()) {
            if (r instanceof GeneralizationRelationship) {
                GeneralizationRelationship g = (GeneralizationRelationship) r;
                if (g.getParent().equals(clazz) || (g.getChild().equals(clazz))) {
                    return g;
                }
            }
        }
        return null;
    }

    /**
     * Verify if the class is optional
     *
     * @param clazz class
     * @return is optional
     */
    public static boolean isOptional(Class clazz) {
        boolean isOptional = false;
        if (clazz.getVariantType() != null) {
            if (clazz.getVariantType().equalsIgnoreCase("optional")) {
                return true;
            }
        }
        return isOptional;
    }

    /**
     * Verify if is variant
     *
     * @param arch  architecture
     * @param clazz class
     * @return is variant
     */
    public static boolean isVariant(Architecture arch, Class clazz) {
        boolean isVariant = false;
        Collection<Variability> variabilities = arch.getAllVariabilities();
        for (Variability variability : variabilities) {
            VariationPoint varPoint = variability.getVariationPoint();
            if (varPoint != null) {
                for (Variant variant : varPoint.getVariants()) {
                    if (variant.getVariantElement().equals(clazz)) {
                        isVariant = true;
                    }
                }
            }
        }
        return isVariant;
    }

    /**
     * Verify if is variation point
     *
     * @param arch  architecture
     * @param clazz class
     * @return is variation point
     */
    public static boolean isVariationPoint(Architecture arch, Class clazz) {
        boolean isVariationPoint = false;
        Collection<Variability> variabilities = arch.getAllVariabilities();
        for (Variability variability : variabilities) {
            VariationPoint varPoint = variability.getVariationPoint();
            if (varPoint != null) {
                Class classVP = (Class) varPoint.getVariationPointElement();
                if (classVP.equals(clazz)) {
                    isVariationPoint = true;
                }
            }
        }
        return isVariationPoint;
    }

    /**
     * Verify if is a valid solution
     *
     * @param solution solution
     * @return is valid solution
     */
    public static boolean isValidSolution(Architecture solution) {
        List<Interface> allInterfaces = new ArrayList<Interface>(solution.getAllInterfaces());
        if (!allInterfaces.isEmpty()) {
            for (Interface itf : allInterfaces) {
                if ((itf.getImplementors().isEmpty()) && (itf.getDependents().isEmpty())
                        && (!itf.getMethods().isEmpty())) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Apply class to interface
     *
     * @param arch  architecture
     * @param clazz class
     * @throws ConcernNotFoundException default exception
     */
    public static void applyToClass(Architecture arch, Class clazz) throws ConcernNotFoundException {
        Set<Concern> allConcerns = clazz.getAllConcernsWithoutImplementedInterfaces();
        if (allConcerns.size() > 1) {
            List<Package> collect = arch.getAllPackages().stream().filter(p -> p.getAllClasses().contains(clazz)).collect(Collectors.toList());
            Package aPackage = collect.size() > 0 ? collect.get(0) : null;
            if (VerifyIfIsHomonimo(clazz) || aPackage == null) return;

            // Passo 2
            Concern major = getMajorConcern(clazz);

//                            Passo 3
            for (Concern concern : allConcerns.stream().filter(co -> !co.equals(major)).collect(Collectors.toList())) {
                Class newClass = findOrCreateClassWithConcernWithConcernName(aPackage, concern, clazz);
                if (!arch.getAllClasses().contains(newClass)) {
                    arch.addExternalClass(newClass);
                }

//                                Passo 4
                RelationshipsHolder relationshipsHolder = new RelationshipsHolder();
                AssociationRelationship associationRelationship = new AssociationRelationship(clazz, newClass);
                relationshipsHolder.addRelationship(associationRelationship);
                clazz.setRelationshipHolder(relationshipsHolder);
                arch.addRelationship(associationRelationship);

                RelationshipsHolder relationshipsHolder2 = new RelationshipsHolder();
                AssociationRelationship associationRelationship2 = new AssociationRelationship(newClass, clazz);
                relationshipsHolder2.addRelationship(associationRelationship2);
                newClass.setRelationshipHolder(relationshipsHolder2);
                arch.addRelationship(associationRelationship2);

//                applyToClass(arch, c);

            }
        }
    }

    /**
     * Verify if is homonimo
     *
     * @param clazz class
     * @return is homonimo
     */
    public static boolean VerifyIfIsHomonimo(Class clazz) {
        // Identifico se contem subclasse
        boolean pci = clazz.getRelationships().stream().filter(r -> {
            if (r instanceof GeneralizationRelationship) {
                Class child = (Class) ((GeneralizationRelationship) r).getChild();
                return child != clazz;
            }
            return false;
        }).count() > 0;

        if (pci) {
//                           Passo 1
            boolean homonimo = false;
            for (Attribute attribute : clazz.getAllAttributes()) {
                int count = 0;
                for (Relationship relationship : clazz.getRelationships()) {
                    if (relationship instanceof GeneralizationRelationship) {
                        Class child = (Class) ((GeneralizationRelationship) relationship).getChild();
                        long count1 = child.getAllAttributes().stream().filter(attr -> attr.getName().equals(attribute.getName())).count();
                        if (count1 > 0) count++;
                    }
                }
                homonimo = count >= clazz.getGeneralizations().size();
            }
            if (homonimo) return true;
        }
        return false;
    }

    /**
     * Apply interface to architecture
     *
     * @param arch   architecture
     * @param interf interface
     * @throws ConcernNotFoundException default exception
     */
    public static void applyToInterface(Architecture arch, Interface interf) throws ConcernNotFoundException {
        Set<Concern> allConcerns = interf.getAllConcerns();
        if (allConcerns.size() > 1) {
            List<Package> collect = arch.getAllPackages().stream().filter(p -> p.getAllInterfaces().contains(interf)).collect(Collectors.toList());
            Package aPackage = collect.size() > 0 ? collect.get(0) : null;
            if (aPackage == null) return;

            // Passo 2
            Concern major = getMajorConcern(interf);

            for (Concern concern : allConcerns.stream().filter(co -> !co.equals(major)).collect(Collectors.toList())) {
                Interface newClass = findOrCreateInterfaceWithConcernWithConcernName(aPackage, concern, interf);
                if (!arch.getAllInterfaces().contains(newClass)) {
                    arch.addExternalInterface(newClass);
                }

//                                Passo 4
                RelationshipsHolder relationshipsHolder = new RelationshipsHolder();
                AssociationRelationship associationRelationship = new AssociationRelationship(interf, newClass);
                relationshipsHolder.addRelationship(associationRelationship);
                interf.setRelationshipHolder(relationshipsHolder);
                arch.addRelationship(associationRelationship);

                RelationshipsHolder relationshipsHolder2 = new RelationshipsHolder();
                AssociationRelationship associationRelationship2 = new AssociationRelationship(newClass, interf);
                relationshipsHolder2.addRelationship(associationRelationship2);
                newClass.setRelationshipHolder(relationshipsHolder2);
                arch.addRelationship(associationRelationship2);
//                applyToInterface(arch, c);
            }
        }
    }

    public static Interface findOrCreateInterfaceWithConcernWithConcernName(Package targetComp, Concern concern, Interface origin) throws ConcernNotFoundException {
        Set<Method> operations = origin.getModifiableOperations().stream().filter(attr -> attr.getAllConcerns().contains(concern)).collect(Collectors.toSet());

        Interface targetClass = targetComp.createInterface(origin.getName() + concern.getName());
        for (Method method : operations) {
            origin.removeOperation(method);
            targetClass.addExternalOperation(method);
        }
        origin.removeConcern(concern.getName());
        targetClass.addConcern(concern.getName());
        return targetClass;
    }

    /**
     * Get major concern by class
     *
     * @param clazz class
     * @return concern
     */
    public static Concern getMajorConcern(Class clazz) {
        long count = 0;
        Concern major = null;
        for (Concern concern : clazz.getAllConcerns()) {
            long count1 = clazz.getAllAttributes().stream().filter(attr -> attr.getAllConcerns().contains(concern)).count();
            long count2 = clazz.getAllMethods().stream().filter(method -> method.getAllConcerns().contains(concern)).count();
            long count3 = clazz.getAllAbstractMethods().stream().filter(method -> method.getAllConcerns().contains(concern)).count();
            long ct = count1 + count2 + count3;
            if (ct >= count) {
                count = ct;
                major = concern;
            }
        }
        return major;
    }

    /**
     * Get major concern by interface
     *
     * @param interf interface
     * @return concern
     */
    public static Concern getMajorConcern(Interface interf) {
        long count = 0;
        Concern major = null;
        for (Concern concern : interf.getAllConcerns()) {
            long count1 = interf.getMethods().stream().filter(attr -> attr.getAllConcerns().contains(concern)).count();
            long ct = count1;
            if (ct >= count) {
                count = ct;
                major = concern;
            }
        }
        return major;
    }

    /**
     * Find or create a class with a concern according its name
     *
     * @param targetComp target
     * @param concern    concern
     * @param origin     origin
     * @return found class
     * @throws ConcernNotFoundException default exception
     */
    public static Class findOrCreateClassWithConcernWithConcernName(Package targetComp, Concern concern, Class origin) throws ConcernNotFoundException {
        Set<Attribute> attrs = origin.getAllModifiableAttributes().stream().filter(attr -> attr.getAllConcerns().contains(concern)).collect(Collectors.toSet());
        Set<Method> methods = origin.getAllModifiableMethods().stream().filter(attr -> attr.getAllConcerns().contains(concern)).collect(Collectors.toSet());
        Set<Method> absmethods = origin.getAllModifiableAbstractMethods().stream().filter(attr -> attr.getAllConcerns().contains(concern)).collect(Collectors.toSet());

        Class targetClass = targetComp.createClass(origin.getName() + concern.getName(), false);
        for (Attribute attr : attrs) {
            targetClass.addExternalAttribute(attr);
            origin.removeAttribute(attr);
        }
        for (Method method : methods) {
            targetClass.addExternalMethod(method);
            origin.removeMethod(method);
        }
        for (Method method : absmethods) {
            targetClass.addExternalMethod(method);
            origin.removeMethod(method);
        }
        origin.removeConcern(concern.getName());
        targetClass.addConcern(concern.getName());
        return targetClass;
    }

}
