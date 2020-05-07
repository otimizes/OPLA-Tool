package br.ufpr.dinf.gres.core.jmetal4.operators.mutation;

import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.architecture.representation.*;
import br.ufpr.dinf.gres.architecture.representation.relationship.AssociationRelationship;
import br.ufpr.dinf.gres.architecture.representation.relationship.GeneralizationRelationship;
import br.ufpr.dinf.gres.architecture.representation.relationship.Relationship;
import br.ufpr.dinf.gres.common.exceptions.JMException;
import br.ufpr.dinf.gres.core.jmetal4.util.PseudoRandom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MutationUtils {


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

    public static Package getRandomPackage(Architecture arch) {
        Package sourceComp = randomObject(new ArrayList<Package>(arch.getAllPackages()));
        while (sourceComp.isTotalyFreezed()) {
            sourceComp = randomObject(new ArrayList<Package>(arch.getAllPackages()));
        }
        return sourceComp;
    }

    public static Package getRandomPackage(List<Package> allComponents) {
        Package selectedCompElem = randomObject(allComponents);
        while (selectedCompElem.isTotalyFreezed()) {
            selectedCompElem = randomObject(allComponents);
        }
        return selectedCompElem;
    }

    public static Interface getRandomInterface(List<Interface> interfacesTargetComp) {
        Interface targetInterface = randomObject(interfacesTargetComp);
        while (targetInterface.isTotalyFreezed()) {
            targetInterface = randomObject(interfacesTargetComp);
        }
        return targetInterface;
    }

    public static Class getRandomClass(List<Class> classesPackage) {
        Class sourceClassElem = randomObject(classesPackage);
        while (sourceClassElem.isTotalyFreezed()) {
            sourceClassElem = randomObject(classesPackage);
        }
        return sourceClassElem;
    }

    public static Method getRandomMethod(List<Method> MethodsClass) {
        Method method = randomObject(MethodsClass);
        while (method.isTotalyFreezed()) {
            method = randomObject(MethodsClass);
        }
        return method;
    }


    public static void removeClassesInPatternStructureFromArray(List<Class> ClassesComp) {
        for (int i = 0; i < ClassesComp.size(); i++) {
            Class klass = ClassesComp.get(i);
            if (klass.getPatternsOperations().hasPatternApplied()) {
                ClassesComp.remove(i);
                i--;
            }
        }
    }

    public static void removeInterfacesInPatternStructureFromArray(List<Interface> InterfacesSourceComp) {
        for (int i = 0; i < InterfacesSourceComp.size(); i++) {
            Interface anInterface = InterfacesSourceComp.get(i);
            if (anInterface.getPatternsOperations().hasPatternApplied()) {
                InterfacesSourceComp.remove(i);
                i--;
            }
        }
    }

    public static void moveMethodToNewClass(Architecture arch, Class sourceClass, List<Method> MethodsClass, Class newClass)
            throws Exception {
        Method targetMethod = getRandomMethod(MethodsClass);
        sourceClass.moveMethodToClass(targetMethod, newClass);
        for (Concern con : targetMethod.getOwnConcerns()) {
            newClass.addConcern(con.getName());
        }
        createAssociation(arch, newClass, sourceClass);
    }


    public static boolean searchForGeneralizations(Class cls) {
        for (Relationship relationship : cls.getRelationships()) {
            if (relationship instanceof GeneralizationRelationship) {
                if (((GeneralizationRelationship) relationship).getChild().equals(cls)
                        || ((GeneralizationRelationship) relationship).getParent().equals(cls)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean searchPatternsClass(Class cls) {
        boolean ap = cls.getPatternsOperations().hasPatternApplied();
        if (ap) {
            return false;
        }
        return true;
    }

    public static boolean searchPatternsInterface(Interface inter) {
        boolean ap = inter.getPatternsOperations().hasPatternApplied();
        if (ap) {
            return false;
        }
        return true;
    }


    public static void moveAttribute(Architecture arch, Class targetClass, Class sourceClass) throws JMException, Exception {
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

    public static void createAssociation(Architecture arch, Class targetClass, Class sourceClass) {
        arch.addRelationship(new AssociationRelationship(targetClass, sourceClass));
    }


    public static void moveMethod(Architecture arch, Class targetClass, Class sourceClass, Package targetComp,
                                  Package sourceComp) {
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


    public static void moveAttributeToNewClass(Architecture arch, Class sourceClass, List<Attribute> AttributesClass,
                                               Class newClass) throws Exception {
        Attribute targetAttribute = randomObject(AttributesClass);
        sourceClass.moveAttributeToClass(targetAttribute, newClass);
        for (Concern con : targetAttribute.getOwnConcerns()) {
            newClass.addConcern(con.getName());
        }
        createAssociation(arch, newClass, sourceClass);
    }


    public static boolean checkSameLayer(Package source, Package target) {
        boolean sameLayer = false;
        if ((source.getName().endsWith("Mgr") && target.getName().endsWith("Mgr"))
                || (source.getName().endsWith("Ctrl") && target.getName().endsWith("Ctrl"))
                || (source.getName().endsWith("GUI") && target.getName().endsWith("GUI"))) {
            sameLayer = true;
        }
        return sameLayer;
    }

    public static String getSuffix(Package comp) {
        String suffix;
        if (comp.getName().endsWith("Mgr")) {
            suffix = "Mgr";
        } else if (comp.getName().endsWith("Ctrl")) {
            suffix = "Ctrl";
        } else if (comp.getName().endsWith("GUI")) {
            suffix = "GUI";
        } else {
            suffix = "";
        }
        return suffix;
    }

    public static boolean isVarPointOfConcern(Architecture arch, Class cls, Concern concern) {
        boolean isVariationPointConcern = false;
        Collection<Variability> variabilities = arch.getAllVariabilities();
        for (Variability variability : variabilities) {
            VariationPoint varPoint = variability.getVariationPoint();
            if (varPoint != null) {
                Class classVP = (Class) varPoint.getVariationPointElement();
                if (classVP.equals(cls) && variability.getName().equals(concern.getName())) {
                    isVariationPointConcern = true;
                }
            }
        }
        return isVariationPointConcern;
    }

    public static boolean isVariantOfConcern(Architecture arch, Class cls, Concern concern) {
        boolean isVariantConcern = false;
        Collection<Variability> variabilities = arch.getAllVariabilities();
        for (Variability variability : variabilities) {
            VariationPoint varPoint = variability.getVariationPoint();
            if (varPoint != null) {
                for (Variant variant : varPoint.getVariants()) {
                    if (variant.getVariantElement().equals(cls) && variability.getName().equals(concern.getName())) {
                        isVariantConcern = true;
                    }
                }
            } else {
                if (cls.getVariantType() != null) {
                    if (cls.getVariantType().equalsIgnoreCase("optional")) {
                        isVariantConcern = true;
                    }
                }
            }
        }
        variabilities.clear();
        return isVariantConcern;
    }

    public static void moveHierarchyToComponent(Class classComp, Package targetComp, Package sourceComp,
                                                Architecture architecture, Concern concern) {
        architecture.forGeneralization().moveGeneralizationToPackage(getGeneralizationRelationshipForClass(classComp),
                targetComp);
    }

    public static GeneralizationRelationship getGeneralizationRelationshipForClass(Element element) {
        for (Relationship r : ((Class) element).getRelationships()) {
            if (r instanceof GeneralizationRelationship) {
                GeneralizationRelationship g = (GeneralizationRelationship) r;
                if (g.getParent().equals(element) || (g.getChild().equals(element))) {
                    return g;
                }
            }
        }
        return null;
    }

    public static boolean isOptional(Architecture arch, Class cls) {
        boolean isOptional = false;
        if (cls.getVariantType() != null) {
            if (cls.getVariantType().toString().equalsIgnoreCase("optional")) {
                return true;
            }
        }
        return isOptional;
    }

    public static boolean isVariant(Architecture arch, Class cls) {
        boolean isVariant = false;
        Collection<Variability> variabilities = arch.getAllVariabilities();
        for (Variability variability : variabilities) {
            VariationPoint varPoint = variability.getVariationPoint();
            if (varPoint != null) {
                for (Variant variant : varPoint.getVariants()) {
                    if (variant.getVariantElement().equals(cls)) {
                        isVariant = true;
                    }
                }
            }
        }
        return isVariant;
    }

    public static boolean isVarPoint(Architecture arch, Class cls) {
        boolean isVariationPoint = false;
        Collection<Variability> variabilities = arch.getAllVariabilities();
        for (Variability variability : variabilities) {
            VariationPoint varPoint = variability.getVariationPoint();
            if (varPoint != null) {
                Class classVP = (Class) varPoint.getVariationPointElement();
                if (classVP.equals(cls)) {
                    isVariationPoint = true;
                }
            }
        }
        return isVariationPoint;
    }

    public static boolean isValidSolution(Architecture solution) {
        boolean isValid = true;
        List<Interface> allInterfaces = new ArrayList<Interface>(solution.getAllInterfaces());
        if (!allInterfaces.isEmpty()) {
            for (Interface itf : allInterfaces) {
                if ((itf.getImplementors().isEmpty()) && (itf.getDependents().isEmpty())
                        && (!itf.getMethods().isEmpty())) {
                    return false;
                }
            }
        }
        return isValid;
    }

}
