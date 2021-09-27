package br.otimizes.oplatool.core.jmetal4.operators.crossover;

import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.representation.relationship.GeneralizationRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;

import java.util.*;

/**
 * Crossover utils
 */
public class CrossoverUtils {

    private static final CrossoverUtils INSTANCE = new CrossoverUtils();

    public static CrossoverUtils getInstance() {
        return INSTANCE;
    }

    public void removeArchitecturalElementsRealizingFeature(Concern feature, Architecture offspring, String scope) {
        List<Package> allComponents = new ArrayList<>(offspring.getAllPackages());
        if (!allComponents.isEmpty()) {
            for (Package comp : allComponents) {
                if (comp.containsConcern(feature) && comp.getOwnConcerns().size() == 1) {
                    List<Interface> allInterfacesComp = new ArrayList<>(comp.getImplementedInterfaces());
                    if (!allInterfacesComp.isEmpty()) {
                        for (Interface interfaceComp : allInterfacesComp) {
                            offspring.removeInterface(interfaceComp);
                        }
                    }
                    this.removeClassesComponent(comp, offspring, scope);
                    offspring.removePackage(comp);

                } else {
                    this.removeInterfacesComponentRealizingFeature(comp, feature, offspring);
                    this.removeClassesComponentRealizingFeature(comp, feature, offspring, scope);
                }
            }
        }
    }

    public static boolean searchForGeneralizations(Class cls) {
        Collection<Relationship> relationships = cls.getRelationships();
        for (Relationship relationship : relationships) {
            if (relationship instanceof GeneralizationRelationship) {
                GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
                if (generalization.getChild().equals(cls) || generalization.getParent().equals(cls))
                    return true;
            }
        }
        return false;
    }

    private void removeClassesComponent(Package comp, Architecture offspring, String scope) {
        List<Class> allClasses = new ArrayList<>(comp.getAllClasses());
        if (!allClasses.isEmpty()) {
            for (Class classComp : allClasses) {
                if (comp.getAllClasses().contains(classComp)) {
                    if (!searchForGeneralizations(classComp)) {
                        comp.removeClass(classComp);
                    } else {
                        removeHierarchyOfComponent(classComp, comp, offspring);
                    }
                }
            }
        }
    }

    private void removeHierarchyOfComponent(Class cls, Package comp, Architecture architecture) {
        Class parent = cls;
        while (CrossoverOperations.isChild(parent)) {
            parent = CrossoverOperations.getParent(parent);
        }
        removeChildrenOfComponent(parent, comp, architecture);
    }

    private void removeChildrenOfComponent(Element parent, Package comp, Architecture architecture) {
        Collection<Element> children = getChildren(parent);
        for (Element child : children) {
            removeChildrenOfComponent(child, comp, architecture);
        }
        if (comp.getAllClasses().contains(parent)) {
            comp.removeClass(parent);
        } else {
            for (Package auxComp : architecture.getAllPackages()) {
                if (auxComp.getAllClasses().contains(parent)) {
                    auxComp.removeClass(parent);
                    break;
                }
            }
        }
    }

    private Set<Element> getChildren(Element cls) {
        GeneralizationRelationship g = getGeneralizationForClass(cls);
        if (g == null)
            return Collections.emptySet();
        return g.getAllChildrenForGeneralClass();
    }

    private GeneralizationRelationship getGeneralizationForClass(Element cls) {
        for (Relationship relationship : ((Class) cls).getRelationships()) {
            if (relationship instanceof GeneralizationRelationship) {
                GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
                if (generalization.getParent().equals(cls))
                    return (GeneralizationRelationship) relationship;
            }
        }
        return null;
    }

    private void removeInterfacesComponentRealizingFeature(Package comp, Concern feature, Architecture offspring) {
        List<Interface> allInterfaces = new ArrayList<>(comp.getImplementedInterfaces());
        if (!allInterfaces.isEmpty()) {
            for (Interface interfaceComp : allInterfaces) {
                if (interfaceComp.containsConcern(feature) && interfaceComp.getOwnConcerns().size() == 1)
                    offspring.removeInterface(interfaceComp);
                else
                    removeOperationsOfInterfaceRealizingFeature(interfaceComp, feature);
            }
        }
    }


    private void removeOperationsOfInterfaceRealizingFeature(Interface interfaceComp, Concern feature) {
        List<Method> operationsInterfaceComp = new ArrayList<>(interfaceComp.getMethods());
        if (!operationsInterfaceComp.isEmpty()) {
            for (Method operation : operationsInterfaceComp) {
                if (operation.containsConcern(feature) && operation.getOwnConcerns().size() == 1)
                    interfaceComp.removeMethod(operation);
            }
        }
    }

    private void removeClassesComponentRealizingFeature(Package comp, Concern feature, Architecture offspring, String scope) {
        List<Class> allClasses = new ArrayList<>(comp.getAllClasses());
        if (!allClasses.isEmpty()) {
            for (Class classComp : allClasses) {
                if ((classComp.containsConcern(feature)) && (classComp.getOwnConcerns().size() == 1)) {
                    if (!searchForGeneralizations(classComp)) {
                        comp.removeClass(classComp);
                    } else {
                        removeHierarchyOfComponent(classComp, comp, offspring);
                    }
                } else {
                    if (scope.equals("allLevels")) {
                        removeAttributesOfClassRealizingFeature(classComp, feature);
                        removeMethodsOfClassRealizingFeature(classComp, feature);
                    }
                }
            }
        }
    }

    private void removeAttributesOfClassRealizingFeature(Class cls, Concern feature) {
        List<Attribute> attributesClassComp = new ArrayList<>(cls.getAllAttributes());
        if (!attributesClassComp.isEmpty()) {
            for (Attribute attribute : attributesClassComp) {
                if (attribute.containsConcern(feature) && attribute.getOwnConcerns().size() == 1) {
                    if (!searchForGeneralizations(cls))
                        cls.removeAttribute(attribute);
                }
            }
        }
    }

    private void removeMethodsOfClassRealizingFeature(Class cls, Concern feature) {
        List<Method> methodsClassComp = new ArrayList<>(cls.getAllMethods());
        if (!methodsClassComp.isEmpty()) {
            for (Method method : methodsClassComp) {
                if (method.containsConcern(feature) && method.getOwnConcerns().size() == 1) {
                    if (!searchForGeneralizations(cls))
                        cls.removeMethod(method);
                }
            }
        }
    }

    public void restoreMissingElements(Architecture father, Solution childSolution) {
        Architecture child = (Architecture) childSolution.getDecisionVariables()[0];
        Architecture fatherDiff = father.deepClone();
        Architecture fatherClassPackage = father.deepClone();
        Architecture fatherInterfacePackage = father.deepClone();
        try {
            List<Class> diffListClass = getDiffClasses(father, child, fatherDiff);
            setDiffClasses(father, child, fatherClassPackage, diffListClass);
            List<Interface> allInterfacesChild = new ArrayList<>(child.getAllInterfaces());
            List<Interface> allInterfacesFather = new ArrayList<>(fatherDiff.getAllInterfaces());
            List<Interface> diffListInterface = getDiffListOfInterfaces(allInterfacesChild, allInterfacesFather);
            if (diffListInterface.size() > 0) {
                for (Interface interfaceDiff : diffListInterface) {
                    Interface interfaceChild = child.findInterfaceById(interfaceDiff.getId());
                    setDiffInterfaceChildOnArchitecture(father, child, fatherInterfacePackage, interfaceDiff, interfaceChild);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fatherDiff.clearArchitecture();
        fatherClassPackage.clearArchitecture();
        fatherInterfacePackage.clearArchitecture();
    }

    private List<Class> getDiffClasses(Architecture father, Architecture child, Architecture fatherDiff) {
        List<Class> diffListClass = new ArrayList<>();
        List<Class> allClassesChild = new ArrayList<>(child.getAllClasses());
        List<Class> allClassesFather = new ArrayList<>(fatherDiff.getAllClasses());
        for (Class selectedClass : allClassesFather) {
            for (Class selectedClassChild : allClassesChild) {
                for (Attribute atribute : selectedClassChild.getAllAttributes()) {
                    selectedClass.removeAttributeByID(atribute.getId());
                }
                for (Method method : selectedClassChild.getAllMethods()) {
                    selectedClass.removeMethodByID(method.getId());
                }
            }
            boolean addClass = false;
            if (selectedClass.getAllMethods() != null) {
                if (selectedClass.getAllMethods().size() > 0) {
                    diffListClass.add(selectedClass);
                    addClass = true;
                }
            }
            if (!addClass) {
                if (selectedClass.getAllAttributes() != null) {
                    if (selectedClass.getAllAttributes().size() > 0) {
                        diffListClass.add(selectedClass);
                        addClass = true;
                    }
                }
                if (!addClass) {
                    if (selectedClass.hasGeneralization()) {
                        Class c = child.findClassById(selectedClass.getId());
                        if (c == null) {
                            Architecture fatherClassPackageDiff = geFatherClassPackageAtArchitecture(father, child, selectedClass);
                            fatherClassPackageDiff.clearArchitecture();
                        }
                    }
                }
            }
        }
        return diffListClass;
    }

    private Architecture geFatherClassPackageAtArchitecture(Architecture father, Architecture child, Class selectedClass) {
        Architecture fatherClassPackageDiff = father.deepClone();
        Package pkgPai = fatherClassPackageDiff.findPackageOfElementID(selectedClass.getId());

        Package pkgFilho = child.findPackageByID(pkgPai.getId());
        if (pkgFilho != null) {
            selectedClass.setRelationshipHolder(child.getRelationshipHolder());
            child.addClassOrInterface(selectedClass, pkgFilho);
        } else {
            ArrayList<String> idsToClear = new ArrayList<>();
            for (Class cL : pkgPai.getAllClasses()) {
                if (!cL.getId().equals(selectedClass.getId()))
                    idsToClear.add(cL.getId());
            }
            for (String sl : idsToClear) {
                pkgPai.removeClassByID(sl);
            }
            idsToClear.clear();
            for (Interface cL : pkgPai.getAllInterfaces()) {
                idsToClear.add(cL.getId());
            }
            for (String sl : idsToClear) {
                pkgPai.removeInterfaceByID(sl);
            }
            pkgPai.setRelationshipHolder(child.getRelationshipHolder());
            selectedClass.setRelationshipHolder(child.getRelationshipHolder());
            child.addPackage(pkgPai);
        }
        return fatherClassPackageDiff;
    }

    private void setDiffClasses(Architecture father, Architecture child, Architecture fatherClassPackage, List<Class> diffListClass) {
        if (diffListClass.size() > 0) {
            for (Class classDiff : diffListClass) {
                Class classChild = child.findClassById(classDiff.getId());
                if (classChild == null) {
                    Package diffPackage = father.findPackageOfElementID(classDiff.getId());
                    if (diffPackage == null) {

                        classDiff.setRelationshipHolder(child.getRelationshipHolder());
                        child.addExternalClass(classDiff);
                    } else {
                        Package childPackage = child.findPackageOfElementID(classDiff.getId());
                        if (childPackage == null) {
                            Package newPackageChild = fatherClassPackage.findPackageByID(diffPackage.getId());
                            newPackageChild.removeAllClass();
                            newPackageChild.removeAllInterfaces();
                            newPackageChild.addExternalClass(classDiff);
                            classDiff.setRelationshipHolder(child.getRelationshipHolder());
                            newPackageChild.setRelationshipHolder(child.getRelationshipHolder());
                            child.addPackage(newPackageChild);
                        } else {
                            classDiff.setRelationshipHolder(child.getRelationshipHolder());
                            child.addClassOrInterface(classDiff, childPackage);
                        }
                    }
                } else {
                    for (Method m : classDiff.getAllMethods()) {
                        classChild.addExternalMethod(m);
                    }
                    for (Attribute a : classDiff.getAllAttributes()) {
                        classChild.addExternalAttribute(a);
                    }
                }
            }
        }
    }

    private void setDiffInterfaceChildOnArchitecture(Architecture father, Architecture child, Architecture fatherInterfacePackage,
                                                     Interface interfaceDiff, Interface interfaceChild) {
        if (interfaceChild == null) {
            Package diffPackage = father.findPackageOfElementID(interfaceDiff.getId());
            if (diffPackage == null) {
                interfaceDiff.setRelationshipHolder(child.getRelationshipHolder());
                child.addExternalInterface(interfaceDiff);
            } else {
                Package childPackage = child.findPackageOfElementID(interfaceDiff.getId());
                if (childPackage == null) {
                    Package newPackageChild = fatherInterfacePackage.findPackageByID(diffPackage.getId());
                    newPackageChild.removeAllClass();
                    newPackageChild.removeAllInterfaces();
                    newPackageChild.addExternalInterface(interfaceDiff);
                    interfaceDiff.setRelationshipHolder(child.getRelationshipHolder());

                    newPackageChild.setRelationshipHolder(child.getRelationshipHolder());

                    child.addPackage(newPackageChild);
                } else {
                    interfaceDiff.setRelationshipHolder(child.getRelationshipHolder());
                    child.addClassOrInterface(interfaceDiff, childPackage);
                }
            }
        } else {
            for (Method m : interfaceDiff.getMethods()) {
                interfaceChild.addExternalMethod(m);
            }
        }
    }

    private List<Interface> getDiffListOfInterfaces(List<Interface> allInterfacesChild, List<Interface> allInterfacesFather) {
        List<Interface> diffListInterface = new ArrayList<>();
        for (Interface selectedInterface : allInterfacesFather) {
            for (Interface selectedInterfaceChild : allInterfacesChild) {
                for (Method method : selectedInterfaceChild.getMethods()) {
                    selectedInterface.removeMethodByID(method.getId());
                }
            }
            if (selectedInterface.getMethods() != null) {
                if (selectedInterface.getMethods().size() > 0) {
                    diffListInterface.add(selectedInterface);
                }
            }
        }
        return diffListInterface;
    }


    public void removeDuplicateElements(Architecture child) {
        try {
            ArrayList<String> lstMethodAttributeOperationID = new ArrayList<>();
            ArrayList<String> lstClassInterfaceID = new ArrayList<>();
            ArrayList<String> lstMethodAndAttributeRemove = new ArrayList<>();
            ArrayList<String> lstOperationRemove = new ArrayList<>();
            ArrayList<String> lstClassRemove = new ArrayList<>();
            ArrayList<String> lstInterfaceRemove = new ArrayList<>();
            ArrayList<String> lstPackageRemove = new ArrayList<>();
            for (Class classChild : child.getAllClasses()) {
                lstMethodAndAttributeRemove.clear();
                setMethodsToRemove(child, lstMethodAttributeOperationID, lstClassInterfaceID, lstMethodAndAttributeRemove, classChild);
                setAttributesToRemove(child, lstMethodAttributeOperationID, lstClassInterfaceID, lstMethodAndAttributeRemove, classChild);
                for (String elementId : lstMethodAndAttributeRemove) {
                    classChild.removeAttributeByID(elementId);
                    classChild.removeMethodByID(elementId);
                }

            }
            setClassesToRemove(child, lstClassRemove);
            for (String classId : lstClassRemove) {
                child.removeClassByID(classId);
            }
            lstClassInterfaceID.clear();
            lstMethodAttributeOperationID.clear();
            for (Interface interfaceChild : child.getAllInterfaces()) {
                lstOperationRemove.clear();
                setMethodsToRemove(child, lstMethodAttributeOperationID, lstClassInterfaceID, lstOperationRemove, interfaceChild);
                for (String elementId : lstOperationRemove) {
                    interfaceChild.removeMethodByID(elementId);
                }
            }
            setLastInterfacesToRemove(child, lstInterfaceRemove);
            for (String classId : lstInterfaceRemove) {
                child.removeInterfaceByID(classId);
            }
            setLastPackagesToRemove(child, lstPackageRemove);
            for (String classId : lstPackageRemove) {
                child.removePackageByID(classId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMethodsToRemove(Architecture child, ArrayList<String> lstMethodAttributeOperationID,
                                    ArrayList<String> lstClassInterfaceID, ArrayList<String> lstMethodAndAttributeRemove, Class classChild) {
        int countConcernEqualNew;
        int countNewElemRelatedWithConcern;
        boolean ex;
        int countDiffConcernsList;
        int countConcernEqualList;
        int countDiffConcernsNew;
        int countCurrentElemRelatedWithConcern;
        int posReplic;
        for (Element classMethod : classChild.getAllMethods()) {
            ex = false;
            for (String d : lstMethodAttributeOperationID) {
                if (d.equals(classMethod.getId())) {
                    ex = true;
                    break;
                }
            }
            if (!ex) {
                lstMethodAttributeOperationID.add(classMethod.getId());
                lstClassInterfaceID.add(classChild.getId());
            } else {
                posReplic = 0;
                while (!lstMethodAttributeOperationID.get(posReplic).equals(classMethod.getId())) {
                    posReplic++;
                }
                if (lstClassInterfaceID.get(posReplic).equals(classChild.getId())) {
                    lstMethodAndAttributeRemove.add(classMethod.getId());
                } else {
                    Class existClass = child.findClassById(lstClassInterfaceID.get(posReplic));
                    Method method1 = existClass.findMethodById(lstMethodAttributeOperationID.get(posReplic));
                    if (method1 == null) {
                        lstClassInterfaceID.remove(posReplic);
                        lstMethodAttributeOperationID.remove(posReplic);
                    } else {
                        countConcernEqualNew = countConcerns(classMethod.getOwnConcerns(), classChild.getOwnConcerns());
                        countConcernEqualList = countConcerns(method1.getOwnConcerns(), existClass.getOwnConcerns());
                        if (countConcernEqualNew < countConcernEqualList) {
                            lstMethodAndAttributeRemove.add(classMethod.getId());
                        } else {
                            if (countConcernEqualNew > countConcernEqualList) {
                                existClass.removeMethodByID(classMethod.getId());
                                lstClassInterfaceID.remove(posReplic);
                                lstMethodAttributeOperationID.remove(posReplic);
                                lstMethodAttributeOperationID.add(classMethod.getId());
                                lstClassInterfaceID.add(classChild.getId());
                            } else {
                                countDiffConcernsNew = (classMethod.getOwnConcerns().size() + classChild.getOwnConcerns().size()) - (countConcernEqualNew * 2);
                                countDiffConcernsList = (method1.getOwnConcerns().size() + existClass.getOwnConcerns().size()) - (countConcernEqualList * 2);
                                if (countDiffConcernsList < countDiffConcernsNew) {
                                    lstMethodAndAttributeRemove.add(classMethod.getId());
                                } else {
                                    if (countDiffConcernsList > countDiffConcernsNew) {
                                        existClass.removeMethodByID(classMethod.getId());
                                        lstClassInterfaceID.remove(posReplic);
                                        lstMethodAttributeOperationID.remove(posReplic);
                                        lstMethodAttributeOperationID.add(classMethod.getId());
                                        lstClassInterfaceID.add(classChild.getId());
                                    } else {
                                        countNewElemRelatedWithConcern = countElemRelatedWithConcern(classMethod.getOwnConcerns(), classChild);
                                        countCurrentElemRelatedWithConcern = countElemRelatedWithConcern(method1.getOwnConcerns(), existClass);
                                        if (countNewElemRelatedWithConcern <= countCurrentElemRelatedWithConcern) {
                                            lstMethodAndAttributeRemove.add(classMethod.getId());
                                        } else {
                                            existClass.removeMethodByID(classMethod.getId());
                                            lstClassInterfaceID.remove(posReplic);
                                            lstMethodAttributeOperationID.remove(posReplic);
                                            lstMethodAttributeOperationID.add(classMethod.getId());
                                            lstClassInterfaceID.add(classChild.getId());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void setAttributesToRemove(Architecture child, ArrayList<String> lstMethodAttributeOperationID,
                                       ArrayList<String> lstClassInterfaceID, ArrayList<String> lstMethodAndAttributeRemove, Class classChild) {
        int countConcernEqualNew;
        int countNewElemRelatedWithConcern;
        boolean ex;
        int countDiffConcernsList;
        int countConcernEqualList;
        int countDiffConcernsNew;
        int countCurrentElemRelatedWithConcern;
        int posReplic;
        for (Element classAttribute : classChild.getAllAttributes()) {
            ex = false;
            for (String d : lstMethodAttributeOperationID) {
                if (d.equals(classAttribute.getId())) {
                    ex = true;
                    break;
                }
            }
            if (!ex) {
                lstClassInterfaceID.add(classChild.getId());
                lstMethodAttributeOperationID.add(classAttribute.getId());
            } else {
                posReplic = 0;
                while (!lstMethodAttributeOperationID.get(posReplic).equals(classAttribute.getId())) {
                    posReplic++;
                }
                if (lstClassInterfaceID.get(posReplic).equals(classChild.getId())) {
                    lstMethodAndAttributeRemove.add(classAttribute.getId());
                } else {
                    Class existClass = child.findClassById(lstClassInterfaceID.get(posReplic));
                    Attribute attribute1 = existClass.findAttributeById(lstMethodAttributeOperationID.get(posReplic));
                    if (attribute1 == null) {
                        lstClassInterfaceID.remove(posReplic);
                        lstMethodAttributeOperationID.remove(posReplic);
                    } else {
                        countConcernEqualNew = countConcerns(classAttribute.getOwnConcerns(), classChild.getOwnConcerns());
                        countConcernEqualList = countConcerns(attribute1.getOwnConcerns(), existClass.getOwnConcerns());
                        if (countConcernEqualNew < countConcernEqualList) {
                            lstMethodAndAttributeRemove.add(classAttribute.getId());
                        } else {
                            if (countConcernEqualNew > countConcernEqualList) {
                                existClass.removeAttributeByID(classAttribute.getId());
                                lstClassInterfaceID.remove(posReplic);
                                lstMethodAttributeOperationID.remove(posReplic);
                                lstClassInterfaceID.add(classChild.getId());
                                lstMethodAttributeOperationID.add(classAttribute.getId());
                            } else {
                                countDiffConcernsNew = (classAttribute.getOwnConcerns().size() + classChild.getOwnConcerns().size()) - (countConcernEqualNew * 2);
                                countDiffConcernsList = (attribute1.getOwnConcerns().size() + existClass.getOwnConcerns().size()) - (countConcernEqualList * 2);
                                if (countDiffConcernsList < countDiffConcernsNew) {
                                    lstMethodAndAttributeRemove.add(classAttribute.getId());
                                } else {
                                    if (countDiffConcernsList > countDiffConcernsNew) {
                                        existClass.removeAttributeByID(classAttribute.getId());
                                        lstClassInterfaceID.remove(posReplic);
                                        lstMethodAttributeOperationID.remove(posReplic);
                                        lstClassInterfaceID.add(classChild.getId());
                                        lstMethodAttributeOperationID.add(classAttribute.getId());
                                    } else {
                                        countNewElemRelatedWithConcern = countElemRelatedWithConcern(classAttribute.getOwnConcerns(), classChild);
                                        countCurrentElemRelatedWithConcern = countElemRelatedWithConcern(attribute1.getOwnConcerns(), existClass);
                                        if (countNewElemRelatedWithConcern <= countCurrentElemRelatedWithConcern) {
                                            lstMethodAndAttributeRemove.add(classAttribute.getId());
                                        } else {
                                            existClass.removeAttributeByID(classAttribute.getId());
                                            lstClassInterfaceID.remove(posReplic);
                                            lstMethodAttributeOperationID.remove(posReplic);
                                            lstClassInterfaceID.add(classChild.getId());
                                            lstMethodAttributeOperationID.add(classAttribute.getId());
                                        }
                                    }
                                }
                            }
                        }
                    }
                    existClass = null;
                    attribute1 = null;
                }
            }
        }
    }

    private void setClassesToRemove(Architecture child, ArrayList<String> lstClassRemove) {
        for (Class cr : child.getAllClasses()) {
            if (cr.getAllAttributes() == null && cr.getAllMethods() == null) {
                if (!cr.hasGeneralization())
                    lstClassRemove.add(cr.getId());
            } else {
                if (cr.getAllAttributes() != null && cr.getAllMethods() != null) {
                    if (cr.getAllAttributes().size() + cr.getAllMethods().size() == 0) {
                        if (!cr.hasGeneralization())
                            lstClassRemove.add(cr.getId());
                    }
                }
            }
        }
    }

    private void setMethodsToRemove(Architecture child, ArrayList<String> lstMethodAttributeOperationID,
                                    ArrayList<String> lstClassInterfaceID, ArrayList<String> lstOperationRemove, Interface interfaceChild) {
        int countDiffConcernsList;
        int countDiffConcernsNew;
        int countConcernEqualList;
        boolean ex;
        int countCurrentElemRelatedWithConcern;
        int countNewElemRelatedWithConcern;
        int posReplic;
        int countConcernEqualNew;
        for (Element interfaceOperation : interfaceChild.getMethods()) {
            ex = false;
            for (String d : lstMethodAttributeOperationID) {
                if (d.equals(interfaceOperation.getId())) {
                    ex = true;
                    break;
                }
            }
            if (!ex) {
                lstClassInterfaceID.add(interfaceChild.getId());
                lstMethodAttributeOperationID.add(interfaceOperation.getId());
            } else {
                posReplic = 0;
                while (!lstMethodAttributeOperationID.get(posReplic).equals(interfaceOperation.getId())) {
                    posReplic++;
                }
                if (lstClassInterfaceID.get(posReplic).equals(interfaceChild.getId())) {
                    lstOperationRemove.add(interfaceOperation.getId());
                } else {
                    Interface existInterface = child.findInterfaceById(lstClassInterfaceID.get(posReplic));
                    Method operation1 = null;
                    try {
                        operation1 = existInterface.findMethodById(interfaceOperation.getId());
                    } catch (Exception excp) {
                        excp.printStackTrace();
                    }


                    if (operation1 == null) {
                        lstClassInterfaceID.remove(posReplic);
                        lstMethodAttributeOperationID.remove(posReplic);
                    } else {
                        countConcernEqualNew = countConcerns(interfaceOperation.getOwnConcerns(), interfaceChild.getOwnConcerns());
                        countConcernEqualList = countConcerns(operation1.getOwnConcerns(), existInterface.getOwnConcerns());
                        if (countConcernEqualNew < countConcernEqualList) {
                            lstOperationRemove.add(interfaceOperation.getId());
                        } else {
                            if (countConcernEqualNew > countConcernEqualList) {
                                existInterface.removeMethodByID(interfaceOperation.getId());

                                lstClassInterfaceID.add(interfaceChild.getId());
                                lstMethodAttributeOperationID.add(interfaceOperation.getId());
                            } else {
                                countDiffConcernsNew = (interfaceOperation.getOwnConcerns().size() + interfaceChild.getOwnConcerns().size()) - (countConcernEqualNew * 2);
                                countDiffConcernsList = (operation1.getOwnConcerns().size() + existInterface.getOwnConcerns().size()) - (countConcernEqualList * 2);
                                if (countDiffConcernsList < countDiffConcernsNew) {
                                    lstOperationRemove.add(interfaceOperation.getId());
                                } else {
                                    if (countDiffConcernsList > countDiffConcernsNew) {
                                        existInterface.removeMethodByID(interfaceOperation.getId());

                                        lstClassInterfaceID.remove(posReplic);
                                        lstMethodAttributeOperationID.remove(posReplic);
                                        lstClassInterfaceID.add(interfaceChild.getId());
                                        lstMethodAttributeOperationID.add(interfaceOperation.getId());
                                    } else {
                                        countNewElemRelatedWithConcern = countElemInterfaceRelatedWithConcern(interfaceOperation.getOwnConcerns(), interfaceChild);
                                        countCurrentElemRelatedWithConcern = countElemInterfaceRelatedWithConcern(operation1.getOwnConcerns(), existInterface);
                                        if (countNewElemRelatedWithConcern <= countCurrentElemRelatedWithConcern) {
                                            lstOperationRemove.add(interfaceOperation.getId());
                                        } else {
                                            existInterface.removeMethodByID(interfaceOperation.getId());

                                            lstClassInterfaceID.remove(posReplic);
                                            lstMethodAttributeOperationID.remove(posReplic);

                                            lstClassInterfaceID.add(interfaceChild.getId());
                                            lstMethodAttributeOperationID.add(interfaceOperation.getId());

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void setLastInterfacesToRemove(Architecture child, ArrayList<String> lstInterfaceRemove) {
        for (Interface cr : child.getAllInterfaces()) {
            if (cr.getMethods() == null) {
                if (cr.getDependents().size() == 0 && cr.getImplementors().size() == 0)
                    lstInterfaceRemove.add(cr.getId());
            } else {
                if (cr.getMethods().size() == 0) {
                    if (cr.getDependents().size() == 0 && cr.getImplementors().size() == 0)
                        lstInterfaceRemove.add(cr.getId());
                }
            }
        }
    }

    private void setLastPackagesToRemove(Architecture child, ArrayList<String> lstPackageRemove) {
        for (Package cr : child.getAllPackages()) {
            if (cr.getAllInterfaces() == null && cr.getAllClasses() == null) {
                lstPackageRemove.add(cr.getId());
            } else {
                if (cr.getAllInterfaces() != null && cr.getAllClasses() != null) {
                    if ((cr.getAllInterfaces().size() + cr.getAllClasses().size()) == 0) {
                        lstPackageRemove.add(cr.getId());
                    }
                }
            }
        }
    }

    public int countConcerns(Set<Concern> elementConcern, Set<Concern> parentConcern) {
        if (elementConcern.size() == 0 || parentConcern.size() == 0) {
            return 0;
        }
        int count = 0;
        for (Concern elem : elementConcern) {
            for (Concern paren : parentConcern) {
                if (elem.equals(paren)) {
                    count++;
                }
            }
        }

        return count;
    }

    public int countElemRelatedWithConcern(Set<Concern> elementConcern, Class parentClassConcern) {
        if (elementConcern.size() == 0) {
            return 0;
        }
        int count = 0;
        for (Method elem : parentClassConcern.getAllMethods()) {
            if (elem.getOwnConcerns().containsAll(elementConcern) && elem.getOwnConcerns().size() == elementConcern.size()) {
                count++;
            }
        }
        return count;
    }

    public int countElemInterfaceRelatedWithConcern(Set<Concern> elementConcern, Interface parentInterfaceConcern) {
        if (elementConcern.size() == 0) {
            return 0;
        }
        int count = 0;
        for (Method elem : parentInterfaceConcern.getMethods()) {
            if (elem.getOwnConcerns().containsAll(elementConcern) && elem.getOwnConcerns().size() == elementConcern.size()) {
                count++;
            }
        }
        return count;
    }


    public static ArrayList<Integer> getElementAmount(Solution solution) {
        ArrayList<Integer> countArchElements;
        countArchElements = new ArrayList<>();
        countArchElements.add(0);
        countArchElements.add(0);
        countArchElements.add(0);
        try {
            int tempAtr = 0;
            int tempMet = 0;
            int tempOP = 0;
            Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
            List<Class> allClasses = new ArrayList<>(arch.getAllClasses());
            for (Class selectedClass : allClasses) {
                tempAtr = tempAtr + selectedClass.getAllAttributes().size();
                tempMet = tempMet + selectedClass.getAllMethods().size();
            }
            List<Interface> allInterface = new ArrayList<>(arch.getAllInterfaces());
            for (Interface selectedInterface : allInterface) {
                tempOP = tempOP + selectedInterface.getMethods().size();
            }
            countArchElements.set(0, tempAtr);
            countArchElements.set(1, tempMet);
            countArchElements.set(2, tempOP);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return countArchElements;
    }

    public static Solution[] selectionComplementary(SolutionSet pop) {
        ArrayList<ArrayList<Solution>> lstFitness = new ArrayList<>();
        int numberOfObjectives = pop.get(0).numberOfObjectives();
        for (int i = 0; i < numberOfObjectives; i++) {
            ArrayList<Solution> arrayList = new ArrayList<>();
            lstFitness.add(arrayList);
        }
        for (Solution s : pop.getSolutionSet()) {
            for (int i = 0; i < numberOfObjectives; i++) {
                lstFitness.get(i).add(s);
            }
        }
        for (int i = 0; i < numberOfObjectives; i++) {
            sortFitnessSolution(lstFitness.get(i), i);
        }
        Random generator = new Random();
        Solution[] parent = new Solution[2];

        int lstFitness1Selected = 0;
        int lstFitness2Selected = 0;
        if (numberOfObjectives == 2) {
            lstFitness2Selected = 1;
        }
        if (numberOfObjectives > 2) {
            lstFitness1Selected = generator.nextInt(numberOfObjectives);
            lstFitness2Selected = generator.nextInt(numberOfObjectives);
            while (lstFitness1Selected == lstFitness2Selected) {
                lstFitness1Selected = generator.nextInt(numberOfObjectives);
            }
        }

        ArrayList<Integer> weightsList = new ArrayList<>();
        int solutionAmount = pop.getSolutionSet().size();
        int weight = solutionAmount * 2;
        weightsList.add(weight);

        for (int i = 1; i < solutionAmount; i++) {
            weight = (solutionAmount - i) + weightsList.get(i - 1);
            weightsList.add(weight);
        }
        int maxWeight = weightsList.get(weightsList.size() - 1);
        int posFitness1 = 0;
        int posFitness2 = 0;

        if (numberOfObjectives == 1) {
            int rnd = generator.nextInt(maxWeight) + 1;
            for (int pos = 0; pos < solutionAmount; pos++) {
                if (weightsList.get(pos) >= rnd) {
                    posFitness1 = pos;
                    break;
                }
            }
            rnd = generator.nextInt(maxWeight) + 1;
            for (int pos = 0; pos < solutionAmount; pos++) {
                if (weightsList.get(pos) >= rnd) {
                    posFitness2 = pos;
                    break;
                }
            }
            while (posFitness1 == posFitness2) {
                rnd = generator.nextInt(maxWeight) + 1;
                for (int pos = 0; pos < solutionAmount; pos++) {
                    if (weightsList.get(pos) >= rnd) {
                        posFitness2 = pos;
                        break;
                    }
                }
            }
        } else {
            int rnd = generator.nextInt(maxWeight) + 1;
            for (int pos = 0; pos < solutionAmount; pos++) {
                if (weightsList.get(pos) >= rnd) {
                    posFitness1 = pos;
                    break;
                }
            }
            rnd = generator.nextInt(maxWeight) + 1;
            for (int pos = 0; pos < solutionAmount; pos++) {
                if (weightsList.get(pos) >= rnd) {
                    posFitness2 = pos;
                    break;
                }
            }
        }

        parent[0] = lstFitness.get(lstFitness1Selected).get(posFitness1);
        parent[1] = lstFitness.get(lstFitness2Selected).get(posFitness2);

        for (int i = 1; i < numberOfObjectives; i++) {
            lstFitness.get(i).clear();
        }
        lstFitness.clear();

        return parent;
    }

    public static void sortFitnessSolution(ArrayList<Solution> listFitness, int objective) {
        for (int i = 0; i < listFitness.size() - 1; i++) {
            for (int j = i + 1; j < listFitness.size(); j++) {
                if (listFitness.get(i).getObjective(objective) > listFitness.get(j).getObjective(objective)) {
                    Solution aux = listFitness.get(i);
                    listFitness.set(i, listFitness.get(j));
                    listFitness.set(j, aux);
                }
            }
        }
    }
}