package br.ufpr.dinf.gres.core.jmetal4.operators.mutation;

import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.architecture.representation.*;
import br.ufpr.dinf.gres.architecture.representation.relationship.AssociationRelationship;
import br.ufpr.dinf.gres.architecture.representation.relationship.GeneralizationRelationship;
import br.ufpr.dinf.gres.architecture.representation.relationship.Relationship;
import br.ufpr.dinf.gres.core.jmetal4.util.PseudoRandom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
}
