package br.otimizes.oplatool.core.jmetal4.operators.crossover;

import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.architectureControl.ArchitectureFindElementControl;
import br.otimizes.oplatool.architecture.representation.relationship.GeneralizationRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import br.otimizes.oplatool.core.jmetal4.core.Solution;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Crossover utils
 */
public class CrossoverUtils {

    private static final CrossoverUtils INSTANCE = new CrossoverUtils();
    public static CrossoverUtils getInstance() {
        return INSTANCE;
    }

    public boolean removeArchitecturalElementsRealizingFeature(Concern feature, Architecture offspring, String scope) {
        boolean ok = true;
        List<Package> allComponents = new ArrayList<Package>(offspring.getAllPackages());

        if (!allComponents.isEmpty()) {
            Iterator<Package> iteratorComponents = allComponents.iterator();
            while (iteratorComponents.hasNext()) {
                Package comp = iteratorComponents.next();
                if (comp.containsConcern(feature) && comp.getOwnConcerns().size() == 1) {
                    List<Interface> allInterfacesComp = new ArrayList<Interface>(comp.getImplementedInterfaces());
                    if (!allInterfacesComp.isEmpty()) {
                        Iterator<Interface> iteratorInterfaces = allInterfacesComp.iterator();
                        while (iteratorInterfaces.hasNext()) {
                            Interface interfaceComp = iteratorInterfaces.next();
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
        return ok;
    }

    private boolean thereIsHierarchyInDifferentComponents(Package comp, Architecture architecture) {
        boolean sameComponent = false;
        for (Class class_ : comp.getAllClasses()) {
            if (this.searchForGeneralizations(class_))
                if (!(this.isHierarchyInASameComponent(class_, architecture))) {
                    sameComponent = true;
                    return true;
                }
        }
        return sameComponent;
    }

    private boolean searchForGeneralizations(Class cls) {

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

    private boolean isHierarchyInASameComponent(Class class_, Architecture architecture) {
        boolean sameComponent = true;
        Class parent = class_;
        Package componentOfClass = null;
        componentOfClass = architecture.findPackageOfClass(class_);
        Package componentOfParent = componentOfClass;
        while (CrossoverOperations.isChild(parent)) {
            parent = CrossoverOperations.getParent(parent);
            componentOfParent = architecture.findPackageOfClass(parent);
            if (!(componentOfClass.equals(componentOfParent))) {
                sameComponent = false;
                return false;
            }
        }
        return sameComponent;
    }

    private void removeClassesComponent(Package comp, Architecture offspring, String scope) {
        List<Class> allClasses = new ArrayList<Class>(comp.getAllClasses());
        if (!allClasses.isEmpty()) {
            Iterator<Class> iteratorClasses = allClasses.iterator();
            while (iteratorClasses.hasNext()) {
                Class classComp = iteratorClasses.next();
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
        List<Interface> allInterfaces = new ArrayList<Interface>(comp.getImplementedInterfaces());

        if (!allInterfaces.isEmpty()) {
            Iterator<Interface> iteratorInterfaces = allInterfaces.iterator();
            while (iteratorInterfaces.hasNext()) {
                Interface interfaceComp = iteratorInterfaces.next();
                if (interfaceComp.containsConcern(feature) && interfaceComp.getOwnConcerns().size() == 1)
                    offspring.removeInterface(interfaceComp);
                else
                    removeOperationsOfInterfaceRealizingFeature(interfaceComp, feature);
            }
        }
    }


    private void removeOperationsOfInterfaceRealizingFeature(Interface interfaceComp, Concern feature) {
        List<Method> operationsInterfaceComp = new ArrayList<Method>(interfaceComp.getMethods());
        if (!operationsInterfaceComp.isEmpty()) {
            Iterator<Method> iteratorOperations = operationsInterfaceComp.iterator();
            while (iteratorOperations.hasNext()) {
                Method operation = iteratorOperations.next();
                if (operation.containsConcern(feature) && operation.getOwnConcerns().size() == 1)
                    interfaceComp.removeOperation(operation);
            }
        }
    }

    private void removeClassesComponentRealizingFeature(Package comp, Concern feature, Architecture offspring, String scope) {
        List<Class> allClasses = new ArrayList<Class>(comp.getAllClasses());

        if (!allClasses.isEmpty()) {
            Iterator<Class> iteratorClasses = allClasses.iterator();
            while (iteratorClasses.hasNext()) {
                Class classComp = iteratorClasses.next();
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
        List<Attribute> attributesClassComp = new ArrayList<Attribute>(cls.getAllAttributes());
        if (!attributesClassComp.isEmpty()) {
            Iterator<Attribute> iteratorAttributes = attributesClassComp.iterator();
            while (iteratorAttributes.hasNext()) {
                Attribute attribute = iteratorAttributes.next();
                if (attribute.containsConcern(feature) && attribute.getOwnConcerns().size() == 1) {
                    if (!searchForGeneralizations(cls))
                        cls.removeAttribute(attribute);
                }
            }
        }
    }

    private void removeMethodsOfClassRealizingFeature(Class cls, Concern feature) {
        List<Method> methodsClassComp = new ArrayList<Method>(cls.getAllMethods());
        if (!methodsClassComp.isEmpty()) {
            Iterator<Method> iteratorMethods = methodsClassComp.iterator();
            while (iteratorMethods.hasNext()) {
                Method method = iteratorMethods.next();
                if (method.containsConcern(feature) && method.getOwnConcerns().size() == 1) {
                    if (!searchForGeneralizations(cls))
                        cls.removeMethod(method);
                }
            }
        }
    }

    public void restoreMissingElements2(Architecture father, Solution childSolution) {

        Architecture child = (Architecture)childSolution.getDecisionVariables()[0];


        Architecture fatherDiff = father.deepClone();

        Architecture fatherClassPackage = father.deepClone();
        Architecture fatherInterfacePackage = father.deepClone();


        try {
            List<Class> allClassesChild = new ArrayList<>(child.getAllClasses());

            List<Class> allClassesFather = new ArrayList<>(fatherDiff.getAllClasses());

            List<Class> diffListClass = new ArrayList<>();

            for(Class selectedClass: allClassesFather){

                for(Class selectedClassChild: allClassesChild){
                    for(Attribute atribute : selectedClassChild.getAllAttributes()) {
                        selectedClass.removeAttributeByID(atribute.getId());
                    }
                    for(Method method : selectedClassChild.getAllMethods()) {
                        selectedClass.removeMethodByID(method.getId());
                    }
                }
                boolean addClass = false;
                if(selectedClass.getAllMethods() != null){
                    if(selectedClass.getAllMethods().size() > 0){
                        diffListClass.add(selectedClass);
                        addClass = true;
                    }
                }
                if(addClass == false){
                    if(selectedClass.getAllAttributes() != null){
                        if(selectedClass.getAllAttributes().size() > 0){
                            diffListClass.add(selectedClass);
                            addClass = true;
                        }
                    }

                    if(!addClass){
                        if(selectedClass.hasGeneralization()){
                            Class c = child.findClassById(selectedClass.getId());
                            if(c == null){
                                Architecture fatherClassPackageDiff = father.deepClone();
                                Package pkgPai = fatherClassPackageDiff.findPackageOfElementID(selectedClass.getId());

                                Package pkgFilho = child.findPackageByID(pkgPai.getId());
                                if(pkgFilho != null){
                                    selectedClass.setRelationshipHolder(child.getRelationshipHolder());
                                    child.addClassOrInterface(selectedClass,pkgFilho);
                                }else{
                                    ArrayList<String> id_limpar = new ArrayList<>();
                                    for(Class cL :pkgPai.getAllClasses()){
                                        if(!cL.getId().equals(selectedClass.getId()))
                                            id_limpar.add(cL.getId());
                                    }
                                    for(String sl : id_limpar){
                                        pkgPai.removeClassByID(sl);
                                    }
                                    id_limpar.clear();
                                    for(Interface cL :pkgPai.getAllInterfaces()){
                                        id_limpar.add(cL.getId());
                                    }
                                    for(String sl : id_limpar){
                                        pkgPai.removeInterfaceByID(sl);
                                    }
                                    pkgPai.setRelationshipHolder(child.getRelationshipHolder());
                                    selectedClass.setRelationshipHolder(child.getRelationshipHolder());
                                    child.addPackage(pkgPai);

                                }
                                fatherClassPackageDiff.clearArchitecture();

                            }
                        }
                    }

                }


            }
            if(diffListClass.size() > 0){
                for(Class classDiff: diffListClass){


                    Class classChild = child.findClassById(classDiff.getId());

                    if(classChild == null){

                        br.otimizes.oplatool.architecture.representation.Package diffPackage = father.findPackageOfElementID(classDiff.getId());
                        if(diffPackage == null){

                            classDiff.setRelationshipHolder(child.getRelationshipHolder());
                            child.addExternalClass(classDiff);
                        }else{

                            br.otimizes.oplatool.architecture.representation.Package childPackage = child.findPackageOfElementID(classDiff.getId());

                            if(childPackage == null){

                                br.otimizes.oplatool.architecture.representation.Package newPackageChild = fatherClassPackage.findPackageByID(diffPackage.getId());

                                newPackageChild.removeAllClass();
                                newPackageChild.removeAllInterfaces();
                                newPackageChild.addExternalClass(classDiff);

                                classDiff.setRelationshipHolder(child.getRelationshipHolder());


                                newPackageChild.setRelationshipHolder(child.getRelationshipHolder());
                                child.addPackage(newPackageChild);

                            }
                            else{
                                classDiff.setRelationshipHolder(child.getRelationshipHolder());
                                child.addClassOrInterface(classDiff,childPackage);
                            }
                        }
                    }else{
                        for(Method m : classDiff.getAllMethods()){
                            classChild.addExternalMethod(m);
                        }
                        for(Attribute a : classDiff.getAllAttributes()){
                            classChild.addExternalAttribute(a);
                        }
                    }

                }
            }

            List<Interface> allInterfacesChild = new ArrayList<>(child.getAllInterfaces());

            List<Interface> allInterfacesFather = new ArrayList<>(fatherDiff.getAllInterfaces());

            List<Interface> diffListInterface = new ArrayList<>();
            for(Interface selectedInterface: allInterfacesFather){
                for(Interface selectedInterfaceChild: allInterfacesChild){
                    for(Method method : selectedInterfaceChild.getOperations()) {
                        selectedInterface.removeOperationByID(method.getId());
                    }
                }
                if(selectedInterface.getOperations() != null){
                    if(selectedInterface.getOperations().size() > 0){
                        diffListInterface.add(selectedInterface);
                    }
                }
            }
            if(diffListInterface.size() > 0){
                for(Interface interfaceDiff: diffListInterface){
                    Interface interfaceChild = child.findInterfaceById(interfaceDiff.getId());
                    if(interfaceChild == null){
                        br.otimizes.oplatool.architecture.representation.Package diffPackage = father.findPackageOfElementID(interfaceDiff.getId());
                        if(diffPackage == null){
                            interfaceDiff.setRelationshipHolder(child.getRelationshipHolder());
                            child.addExternalInterface(interfaceDiff);
                        }else{
                            br.otimizes.oplatool.architecture.representation.Package childPackage = child.findPackageOfElementID(interfaceDiff.getId());
                            if(childPackage == null){
                                br.otimizes.oplatool.architecture.representation.Package newPackageChild = fatherInterfacePackage.findPackageByID(diffPackage.getId());
                                newPackageChild.removeAllClass();
                                newPackageChild.removeAllInterfaces();
                                newPackageChild.addExternalInterface(interfaceDiff);
                                interfaceDiff.setRelationshipHolder(child.getRelationshipHolder());

                                newPackageChild.setRelationshipHolder(child.getRelationshipHolder());

                                child.addPackage(newPackageChild);
                            }
                            else{
                                interfaceDiff.setRelationshipHolder(child.getRelationshipHolder());
                                child.addClassOrInterface(interfaceDiff,childPackage);
                            }
                        }
                    }else{
                        for(Method m : interfaceDiff.getOperations()){
                            interfaceChild.addExternalOperation(m);
                        }
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        fatherDiff.clearArchitecture();
        fatherClassPackage.clearArchitecture();
        fatherInterfacePackage.clearArchitecture();
        fatherClassPackage = null;
        fatherInterfacePackage = null;
        fatherDiff = null;


    }



    public void removeDuplicateElements(Architecture child) {

        try {

            boolean ex;
            int countConcernEqualNew;
            int countConcernEqualList;
            int countDiffConcernsNew;
            int countDiffConcernsList;
            int countNewElemRelatedWithConcern;
            int countCurrentElemRelatedWithConcern;
            int posReplic;


            ArrayList<String> lstMethodAttributeOperationID = new ArrayList<>();
            ArrayList<String> lstClassInterfaceID = new ArrayList<>();


            ArrayList<String> lstMethodAndAttributeRemove = new ArrayList<>();
            ArrayList<String> lstOperationRemove = new ArrayList<>();
            ArrayList<String> lstClassRemove = new ArrayList<>();
            ArrayList<String> lstInterfaceRemove = new ArrayList<>();
            ArrayList<String> lstPackageRemove = new ArrayList<>();
            for(Class classChild : child.getAllClasses() ){

                lstMethodAndAttributeRemove.clear();


                for(Element classMethod : classChild.getAllMethods()){
                    ex = false;
                    for(String d : lstMethodAttributeOperationID){
                        if(d.equals(classMethod.getId())) {
                            ex = true;
                        }
                    }
                    if(!ex){

                        lstMethodAttributeOperationID.add(classMethod.getId());
                        lstClassInterfaceID.add(classChild.getId());


                    }
                    else{
                        posReplic = 0;
                        while(!lstMethodAttributeOperationID.get(posReplic).equals(classMethod.getId())){
                            posReplic++;
                        }

                        if(lstClassInterfaceID.get(posReplic).equals(classChild.getId())){
                            lstMethodAndAttributeRemove.add(classMethod.getId());
                        }else {


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

                for(Element classAttribute : classChild.getAllAttributes()){
                    ex = false;
                    for(String d : lstMethodAttributeOperationID){
                        if(d.equals(classAttribute.getId()))
                            ex = true;
                    }
                    if(!ex){
                        lstClassInterfaceID.add(classChild.getId());
                        lstMethodAttributeOperationID.add(classAttribute.getId());
                    }
                    else{
                        posReplic = 0;
                        while(!lstMethodAttributeOperationID.get(posReplic).equals(classAttribute.getId())){
                            posReplic++;
                        }
                        if(lstClassInterfaceID.get(posReplic).equals(classChild.getId())){
                            lstMethodAndAttributeRemove.add(classAttribute.getId());
                        }else {
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
                for(String id_Element : lstMethodAndAttributeRemove){
                    classChild.removeAttributeByID(id_Element);
                    classChild.removeMethodByID(id_Element);
                }

            }

            for(Class cr : child.getAllClasses()){
                if(cr.getAllAttributes() == null && cr.getAllMethods() == null){
                    if(!cr.hasGeneralization())
                        lstClassRemove.add(cr.getId());
                }else{
                    if(cr.getAllAttributes() != null && cr.getAllMethods() != null){
                        if(cr.getAllAttributes().size() + cr.getAllMethods().size() == 0){
                            if(!cr.hasGeneralization())
                                lstClassRemove.add(cr.getId());
                        }
                    }
                }
            }
            for(String id_class : lstClassRemove){
                child.removeClassByID(id_class);
            }

            lstClassInterfaceID.clear();
            lstMethodAttributeOperationID.clear();

            for(Interface interfaceChild : child.getAllInterfaces() ){

                lstOperationRemove.clear();
                for(Element interfaceOperation : interfaceChild.getOperations()){
                    ex = false;
                    for(String d : lstMethodAttributeOperationID){
                        if(d.equals(interfaceOperation.getId()))
                            ex = true;
                    }
                    if(!ex){
                        lstClassInterfaceID.add(interfaceChild.getId());
                        lstMethodAttributeOperationID.add(interfaceOperation.getId());
                    }
                    else{
                        posReplic = 0;
                        while(!lstMethodAttributeOperationID.get(posReplic).equals(interfaceOperation.getId())){
                            posReplic++;
                        }
                        if(lstClassInterfaceID.get(posReplic).equals(interfaceChild.getId())){
                            lstOperationRemove.add(interfaceOperation.getId());
                        }else {
                            Interface existInterface = child.findInterfaceById(lstClassInterfaceID.get(posReplic));
                            Method operation1 = existInterface.findOperationById(interfaceOperation.getId());


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
                                        existInterface.removeOperationByID(interfaceOperation.getId());

                                        lstClassInterfaceID.add(interfaceChild.getId());
                                        lstMethodAttributeOperationID.add(interfaceOperation.getId());
                                    } else {
                                        countDiffConcernsNew = (interfaceOperation.getOwnConcerns().size() + interfaceChild.getOwnConcerns().size()) - (countConcernEqualNew * 2);
                                        countDiffConcernsList = (operation1.getOwnConcerns().size() + existInterface.getOwnConcerns().size()) - (countConcernEqualList * 2);
                                        if (countDiffConcernsList < countDiffConcernsNew) {
                                            lstOperationRemove.add(interfaceOperation.getId());
                                        } else {
                                            if (countDiffConcernsList > countDiffConcernsNew) {
                                                existInterface.removeOperationByID(interfaceOperation.getId());

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
                                                    existInterface.removeOperationByID(interfaceOperation.getId());

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

                for(String id_Element : lstOperationRemove){
                    interfaceChild.removeOperationByID(id_Element);
                }
            }

            for(Interface cr : child.getAllInterfaces()){
                if(cr.getOperations() == null){
                    if(cr.getDependents().size() == 0 && cr.getImplementors().size() == 0)
                        lstInterfaceRemove.add(cr.getId());
                }else{
                    if(cr.getOperations().size() == 0){
                        if(cr.getDependents().size() == 0 && cr.getImplementors().size() == 0)
                            lstInterfaceRemove.add(cr.getId());
                    }
                }
            }
            for(String id_class : lstInterfaceRemove){
                child.removeInterfaceByID(id_class);
            }

            for(Package cr : child.getAllPackages()){
                if(cr.getAllInterfaces() == null && cr.getAllClasses() == null){
                    lstPackageRemove.add(cr.getId());
                }else{
                    if(cr.getAllInterfaces() != null && cr.getAllClasses() != null){
                        if((cr.getAllInterfaces().size()+ cr.getAllClasses().size())==0){
                            lstPackageRemove.add(cr.getId());
                        }
                    }
                }
            }
            for(String id_class : lstPackageRemove){
                child.removePackageByID(id_class);
            }


            lstClassInterfaceID = null;
            lstMethodAttributeOperationID = null;
            lstInterfaceRemove = null;
            lstClassRemove = null;
            lstMethodAndAttributeRemove = null;
            lstPackageRemove = null;
            lstOperationRemove = null;

        }

        catch (Exception e){
            e.printStackTrace();
        }

    }

    public int countConcerns(Set<Concern> elementConcern, Set<Concern> parentConcern){
        if(elementConcern.size() == 0 || parentConcern.size() == 0){
            return 0;
        }
        int count = 0;
        for(Concern elem : elementConcern){
            for(Concern paren : parentConcern){
                if(elem.equals(paren)){
                    count++;
                }
            }
        }

        return count;
    }

    public int countElemRelatedWithConcern(Set<Concern> elementConcern, Class parentClassConcern){
        if(elementConcern.size() == 0){
            return 0;
        }
        int count = 0;
        for(Method elem : parentClassConcern.getAllMethods()){
            if(elem.getOwnConcerns().containsAll(elementConcern) && elem.getOwnConcerns().size() == elementConcern.size()){
                count++;
            }

        }

        return count;
    }

    public int countElemInterfaceRelatedWithConcern(Set<Concern> elementConcern, Interface parentInterfaceConcern){
        if(elementConcern.size() == 0){
            return 0;
        }
        int count = 0;
        for(Method elem : parentInterfaceConcern.getOperations()){
            if(elem.getOwnConcerns().containsAll(elementConcern) && elem.getOwnConcerns().size() == elementConcern.size()){
                count++;
            }

        }

        return count;
    }

    public  void SaveLogText(String filename, String text){
        try {

            FileWriter fw = new FileWriter(filename, true);

            fw.write(text);
            fw.write("\n");
            fw.close();
        }
        catch(IOException ioe)
        {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }

    public ArrayList<Integer> CountArchElements(Solution solution){

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
            for(Class selectedClass: allClasses){
                tempAtr = tempAtr + selectedClass.getAllAttributes().size();
                tempMet = tempMet + selectedClass.getAllMethods().size();
            }

            List<Interface> allInterface = new ArrayList<>(arch.getAllInterfaces());
            for(Interface selectedInterface: allInterface){
                tempOP = tempOP + selectedInterface.getOperations().size();
            }

            countArchElements.set(0,tempAtr);
            countArchElements.set(1,tempMet);
            countArchElements.set(2,tempOP);

        }catch (Exception e){
            e.printStackTrace();
        }

        return  countArchElements;
    }

}