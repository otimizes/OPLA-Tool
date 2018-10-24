package jmetal4.operators.mutation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import arquitetura.exceptions.ConcernNotFoundException;
import arquitetura.helpers.UtilResources;
import arquitetura.representation.Architecture;
import arquitetura.representation.Attribute;
import arquitetura.representation.Class;
import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import arquitetura.representation.Package;
import arquitetura.representation.Variability;
import arquitetura.representation.Variant;
import arquitetura.representation.VariationPoint;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.RealizationRelationship;
import arquitetura.representation.relationship.Relationship;
import jmetal4.core.Solution;
import jmetal4.problems.OPLA;
import jmetal4.util.Configuration;
import jmetal4.util.JMException;
import jmetal4.util.PseudoRandom;

public class PLAFeatureMutation extends Mutation {

    private static final long serialVersionUID = 9039316729379302747L;
    static Logger LOGGER = LogManager.getLogger(PLAFeatureMutation.class.getName());

    private Double mutationProbability = null;
    private List<String> mutationOperators;

    public PLAFeatureMutation(HashMap<String, Object> parameters, List<String> mutationOperators) {
        super(parameters);
        this.mutationOperators = mutationOperators;

        if (parameters.get("probability") != null) {
            mutationProbability = (Double) parameters.get("probability");
        }
    }

    public void doMutation(double probability, Solution solution) throws Exception {
        // "allComponents" usar "sameComponent para que a troca seja realizada
        // dentro do mesmo componente da arquitetura
        String scope = "sameComponent";
        // usar "oneLevel" para nao verificara presenca de interesses nos
        // atributos e metodos
        String scopeLevels = "allLevels";

        int r = PseudoRandom.randInt(0, this.mutationOperators.size() - 1);

        HashMap<Integer, String> operatorMap = new HashMap<>();
        for (int i = 0; i < this.mutationOperators.size(); i++)
            operatorMap.put(i, this.mutationOperators.get(i));

        String selectedOperator = operatorMap.get(r);

        if (selectedOperator.equals("featureMutation")) {
            java.lang.reflect.Method featureMut = PLAFeatureMutation.class.getMethod(selectedOperator, double.class,
                    Solution.class, String.class);
            featureMut.invoke(this, probability, solution, scopeLevels);
        }

        List<String> withScope = Arrays.asList("moveMethodMutation", "addClassMutation", "moveAttributeMutation");
        if (withScope.contains(selectedOperator)) {
            java.lang.reflect.Method featureMut = PLAFeatureMutation.class.getMethod(selectedOperator, double.class,
                    Solution.class, String.class);
            featureMut.invoke(this, probability, solution, scope);
        }

        List<String> withoutScope = Arrays.asList("moveOperationMutation", "addManagerClassMutation");
        if (withoutScope.contains(selectedOperator)) {
            java.lang.reflect.Method featureMut = PLAFeatureMutation.class.getMethod(selectedOperator, double.class,
                    Solution.class);
            featureMut.invoke(this, probability, solution);
        }

        withScope = null;
        withoutScope = null;
    }

    // --------------------------------------------------------------------------
    // m��todo para verificar se algum dos relacionamentos recebidos ��
    // generaliza����o
    private boolean searchForGeneralizations(Class cls) {
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

    // joao verifica se existe padrao na classe
    private boolean searchPatternsClass(Class cls) {
        boolean ap = cls.getPatternsOperations().hasPatternApplied();
        if (ap) {
            return false;
        }
        return true;
    }
    // joao^

    // joao verifica se existe padrao na classe
    private boolean searchPatternsInterface(Interface inter) {
        boolean ap = inter.getPatternsOperations().hasPatternApplied();
        if (ap) {
            System.out.println("patterns aplicado");
            return false;
        }
        return true;
    }
    // joao^

    public void moveAttributeMutation(double probability, Solution solution, String scope) throws JMException {
        LOGGER.info("Executando MoveAttributeMutation");
        try {
            if (PseudoRandom.randDouble() < probability) {
                if (solution.getDecisionVariables()[0].getVariableType() == java.lang.Class
                        .forName(Architecture.ARCHITECTURE_TYPE)) {
                    Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
                    if ("sameComponent".equals(scope)) {
                        List<Class> ClassesComp = new ArrayList<Class>(
                                randomObject(new ArrayList<Package>(arch.getAllPackages())).getAllClasses());
                        if (ClassesComp.size() > 1) {
                            Class targetClass = randomObject(ClassesComp);
                            Class sourceClass = randomObject(ClassesComp);
                            // joao\
                            if (searchPatternsClass(targetClass) && searchPatternsClass(sourceClass)) {
                                if ((sourceClass != null) && (!searchForGeneralizations(sourceClass))
                                        && (sourceClass.getAllAttributes().size() > 1)
                                        && (sourceClass.getAllMethods().size() > 1) && (!isVarPoint(arch, sourceClass))
                                        && (!isVariant(arch, sourceClass)) && (!isOptional(arch, sourceClass))) {
                                    if ((targetClass != null) && (!(targetClass.equals(sourceClass)))) {
                                        moveAttribute(arch, targetClass, sourceClass);
                                    }
                                }
                            }
                        }
                        ClassesComp.clear();
                    } else {
                        if ("allComponents".equals(scope)) {
                            Package sourceComp = randomObject(new ArrayList<Package>(arch.getAllPackages()));
                            List<Class> ClassesSourceComp = new ArrayList<Class>(sourceComp.getAllClasses());
                            if (ClassesSourceComp.size() >= 1) {
                                Class sourceClass = randomObject(ClassesSourceComp);
                                // joao\
                                if (searchPatternsClass(sourceClass)) {
                                    if ((sourceClass != null) && (!searchForGeneralizations(sourceClass))
                                            && (sourceClass.getAllAttributes().size() > 1)
                                            && (sourceClass.getAllMethods().size() > 1)
                                            && (!isVarPoint(arch, sourceClass)) && (!isVariant(arch, sourceClass))
                                            && (!isOptional(arch, sourceClass))) {
                                        Package targetComp = randomObject(
                                                new ArrayList<Package>(arch.getAllPackages()));
                                        if (checkSameLayer(sourceComp, targetComp)) {
                                            List<Class> ClassesTargetComp = new ArrayList<Class>(
                                                    targetComp.getAllClasses());
                                            if (ClassesTargetComp.size() >= 1) {
                                                Class targetClass = randomObject(ClassesTargetComp);
                                                if ((targetClass != null) && (!(targetClass.equals(sourceClass)))) {
                                                    moveAttribute(arch, targetClass, sourceClass);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            ClassesSourceComp.clear();
                        }
                    }
                } else {
                    Configuration.logger_.log(Level.SEVERE, "MoveAttributeMutation.doMutation: invalid type. " + "{0}",
                            solution.getDecisionVariables()[0].getVariableType());
                    java.lang.Class<String> cls = java.lang.String.class;
                    String name = cls.getName();
                    throw new JMException("Exception in " + name + ".doMutation()");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void moveAttribute(Architecture arch, Class targetClass, Class sourceClass) throws JMException, Exception {
        List<Attribute> attributesClass = new ArrayList<Attribute>(sourceClass.getAllAttributes());
        // joao\
        if (searchPatternsClass(targetClass) && searchPatternsClass(sourceClass)) {
            if (attributesClass.size() >= 1) {
                if (sourceClass.moveAttributeToClass(randomObject(attributesClass), targetClass)) {
                    createAssociation(arch, targetClass, sourceClass);
                }
            }
        }
        attributesClass.clear();
    }

    // Add por ��dipo
    private void createAssociation(Architecture arch, Class targetClass, Class sourceClass) {
        arch.addRelationship(new AssociationRelationship(targetClass, sourceClass));
    }

    // --------------------------------------------------------------------------
    public void moveMethodMutation(double probability, Solution solution, String scope) throws JMException {
        if (PseudoRandom.randDouble() < probability) {
            final Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
            if ("sameComponent".equals(scope)) {
                final Package sourceComp = randomObject(new ArrayList<Package>(arch.getAllPackages()));
                List<Class> ClassesComp = new ArrayList<Class>(sourceComp.getAllClasses());

                removeClassesInPatternStructureFromArray(ClassesComp);
                if (ClassesComp.size() > 1) {
                    final Class targetClass = randomObject(ClassesComp);
                    final Class sourceClass = randomObject(ClassesComp);
                    // joao\
                    if (searchPatternsClass(targetClass) && searchPatternsClass(sourceClass)) {
                        if ((sourceClass != null) && (!searchForGeneralizations(sourceClass))
                                && (sourceClass.getAllAttributes().size() > 1)
                                && (sourceClass.getAllMethods().size() > 1) && (!isVarPoint(arch, sourceClass))
                                && (!isVariant(arch, sourceClass)) && (!isOptional(arch, sourceClass))) {
                            if ((targetClass != null) && (!(targetClass.equals(sourceClass)))) {
                                moveMethod(arch, targetClass, sourceClass, sourceComp, sourceComp);
                            }
                        }
                    }
                }
                ClassesComp.clear();
            } else {
                if ("allComponents".equals(scope)) {
                    final Package sourceComp = randomObject(new ArrayList<Package>(arch.getAllPackages()));
                    final List<Class> ClassesSourceComp = new ArrayList<Class>(sourceComp.getAllClasses());
                    removeClassesInPatternStructureFromArray(ClassesSourceComp);
                    if (ClassesSourceComp.size() >= 1) {
                        final Class sourceClass = randomObject(ClassesSourceComp);
                        // joao\
                        if (searchPatternsClass(sourceClass)) {
                            if ((sourceClass != null) && (!searchForGeneralizations(sourceClass))
                                    && (sourceClass.getAllAttributes().size() > 1)
                                    && (sourceClass.getAllMethods().size() > 1) && (!isVarPoint(arch, sourceClass))
                                    && (!isVariant(arch, sourceClass)) && (!isOptional(arch, sourceClass))) {
                                final Package targetComp = randomObject(new ArrayList<Package>(arch.getAllPackages()));
                                if (checkSameLayer(sourceComp, targetComp)) {
                                    final List<Class> ClassesTargetComp = new ArrayList<Class>(
                                            targetComp.getAllClasses());
                                    if (ClassesTargetComp.size() >= 1) {
                                        final Class targetClass = randomObject(ClassesTargetComp);
                                        if ((targetClass != null) && (!(targetClass.equals(sourceClass)))) {
                                            moveMethod(arch, targetClass, sourceClass, targetComp, sourceComp);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    ClassesSourceComp.clear();
                }
            }
        }
    }

    private void moveMethod(Architecture arch, Class targetClass, Class sourceClass, Package targetComp,
                            Package sourceComp) {
        final List<Method> MethodsClass = new ArrayList<Method>(sourceClass.getAllMethods());
        if (MethodsClass.size() >= 1) {
            if (sourceClass.moveMethodToClass(randomObject(MethodsClass), targetClass)) {
                // joao\
                if (searchPatternsClass(sourceClass)) {
                    createAssociation(arch, targetClass, sourceClass);
                }
            }
        }

        MethodsClass.clear();
    }

    // --------------------------------------------------------------------------
    public void moveOperationMutation(double probability, Solution solution) throws JMException {
        LOGGER.info("Executando MoveOperationMutation");
        try {
            if (PseudoRandom.randDouble() < probability) {
                if (solution.getDecisionVariables()[0].getVariableType() == java.lang.Class
                        .forName(Architecture.ARCHITECTURE_TYPE)) {
                    Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);

                    Package sourceComp = randomObject(new ArrayList<Package>(arch.getAllPackages()));
                    Package targetComp = randomObject(new ArrayList<Package>(arch.getAllPackages()));

                    if (checkSameLayer(sourceComp, targetComp)) {
                        List<Interface> InterfacesSourceComp = new ArrayList<Interface>();
                        List<Interface> InterfacesTargetComp = new ArrayList<Interface>();

                        InterfacesSourceComp.addAll(sourceComp.getImplementedInterfaces());
                        removeInterfacesInPatternStructureFromArray(InterfacesSourceComp);

                        InterfacesTargetComp.addAll(targetComp.getImplementedInterfaces());

                        if ((InterfacesSourceComp.size() >= 1) && (InterfacesTargetComp.size() >= 1)) {
                            Interface targetInterface = randomObject(InterfacesTargetComp);
                            Interface sourceInterface = randomObject(InterfacesSourceComp);
                            // joao\
                            if (searchPatternsInterface(targetInterface) && searchPatternsInterface(sourceInterface)) {
                                if (targetInterface != sourceInterface) {
                                    List<Method> OpsInterface = new ArrayList<Method>();
                                    OpsInterface.addAll(sourceInterface.getOperations());
                                    if (OpsInterface.size() >= 1) {
                                        sourceInterface.moveOperationToInterface(randomObject(OpsInterface),
                                                targetInterface);
                                        for (Element implementor : sourceInterface.getImplementors()) {
                                            if (implementor instanceof Package) {
                                                arch.addImplementedInterface(targetInterface, (Package) implementor);
                                            }
                                            if (implementor instanceof Class) {
                                                arch.addImplementedInterface(targetInterface, (Class) implementor);
                                            }
                                        }
                                        OpsInterface.clear();
                                    }
                                }
                            } // ^
                        }
                        InterfacesTargetComp.clear();
                        InterfacesSourceComp.clear();

                    }
                } else {
                    Configuration.logger_.log(Level.SEVERE, "MoveOperationMutation.doMutation: invalid type. " + "{0}",
                            solution.getDecisionVariables()[0].getVariableType());
                    java.lang.Class<String> cls = java.lang.String.class;
                    String name = cls.getName();
                    throw new JMException("Exception in " + name + ".doMutation()");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --------------------------------------------------------------------------
    // tem um metodo para excluir padroes da classe
    // creio que aqui pede deixar como esta
    public void addClassMutation(double probability, Solution solution, String scope) throws JMException {
        LOGGER.info("Executando AddClassMutation ");
        try {
            if (PseudoRandom.randDouble() < probability) {
                if (solution.getDecisionVariables()[0].getVariableType() == java.lang.Class
                        .forName(Architecture.ARCHITECTURE_TYPE)) {
                    Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);

                    Package sourceComp = randomObject(new ArrayList<Package>(arch.getAllPackages()));
                    List<Class> classesPackage = new ArrayList<Class>(sourceComp.getAllClasses());
                    removeClassesInPatternStructureFromArray(classesPackage);
                    if (classesPackage.size() >= 1) {
                        Class sourceClass = randomObject(classesPackage);
                        if ((sourceClass != null) && (!searchForGeneralizations(sourceClass))
                                && (sourceClass.getAllAttributes().size() > 1)
                                && (sourceClass.getAllMethods().size() > 1) && (!isVarPoint(arch, sourceClass))
                                && (!isVariant(arch, sourceClass)) && (!isOptional(arch, sourceClass))) {
                            // joao\
                            if (searchPatternsClass(sourceClass)) {
                                if (PseudoRandom.randInt(0, 1) == 0) { // attribute
                                    List<Attribute> AttributesClass = new ArrayList<Attribute>(
                                            sourceClass.getAllAttributes());
                                    if (AttributesClass.size() >= 1) {
                                        if ("sameComponent".equals(scope)) {
                                            moveAttributeToNewClass(arch, sourceClass, AttributesClass,
                                                    sourceComp.createClass("Class" + OPLA.contClass_++, false));
                                        } else {
                                            if ("allComponents".equals(scope)) {
                                                Package targetComp = randomObject(
                                                        new ArrayList<Package>(arch.getAllPackages()));
                                                if (checkSameLayer(sourceComp, targetComp)) {
                                                    moveAttributeToNewClass(arch, sourceClass, AttributesClass,
                                                            targetComp.createClass("Class" + OPLA.contClass_++, false));
                                                }
                                            }
                                        }
                                        AttributesClass.clear();
                                    }
                                } // joao^
                            } else { // method
                                // joao\
                                if (searchPatternsClass(sourceClass)) {
                                    List<Method> MethodsClass = new ArrayList<Method>(sourceClass.getAllMethods());
                                    if (MethodsClass.size() >= 1) {
                                        if ("sameComponent".equals(scope)) {
                                            moveMethodToNewClass(arch, sourceClass, MethodsClass,
                                                    sourceComp.createClass("Class" + OPLA.contClass_++, false));
                                        } else {
                                            if ("allComponents".equals(scope)) {
                                                Package targetComp = randomObject(
                                                        new ArrayList<Package>(arch.getAllPackages()));
                                                if (checkSameLayer(sourceComp, targetComp)) {
                                                    moveMethodToNewClass(arch, sourceClass, MethodsClass,
                                                            targetComp.createClass("Class" + OPLA.contClass_++, false));
                                                }
                                            }
                                        }
                                        MethodsClass.clear();
                                    }
                                } // joao^
                            }
                        }
                    }
                    classesPackage.clear();

                } else {
                    Configuration.logger_.log(Level.SEVERE, "AddClassMutation.doMutation: invalid type. " + "{0}",
                            solution.getDecisionVariables()[0].getVariableType());
                    java.lang.Class<String> cls = java.lang.String.class;
                    String name = cls.getName();
                    throw new JMException("Exception in " + name + ".doMutation()");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeClassesInPatternStructureFromArray(List<Class> ClassesComp) {
        for (int i = 0; i < ClassesComp.size(); i++) {
            Class klass = ClassesComp.get(i);
            if (klass.getPatternsOperations().hasPatternApplied()) {
                ClassesComp.remove(i);
                i--;
            }
        }
    }

    private void removeInterfacesInPatternStructureFromArray(List<Interface> InterfacesSourceComp) {
        for (int i = 0; i < InterfacesSourceComp.size(); i++) {
            Interface anInterface = InterfacesSourceComp.get(i);
            if (anInterface.getPatternsOperations().hasPatternApplied()) {
                InterfacesSourceComp.remove(i);
                i--;
            }
        }
    }

    private void moveMethodToNewClass(Architecture arch, Class sourceClass, List<Method> MethodsClass, Class newClass)
            throws Exception {
        // joao\
        // if (searchPatternsClass(sourceClass)) {
        Method targetMethod = randomObject(MethodsClass);
        sourceClass.moveMethodToClass(targetMethod, newClass);
        // if (targetMethod.isAbstract()) targetMethod.setAbstract(false);
        for (Concern con : targetMethod.getOwnConcerns()) {
            newClass.addConcern(con.getName());
        }
        createAssociation(arch, newClass, sourceClass);
        // }
    }

    // private void moveMethodAllComponents(Architecture arch, Class
    // sourceClass, List<Method> MethodsClass, Class newClass) throws
    // JMException {
    // Method targetMethod = randomObject (MethodsClass);
    // sourceClass.moveMethodToClass(targetMethod, newClass);
    // //if (targetMethod.isAbstract()) targetMethod.setAbstract(false);
    // for (Concern con: targetMethod.getOwnConcerns()){
    // try {
    // newClass.addConcern(con.getName());
    // } catch (ConcernNotFoundException e) {
    // e.printStackTrace();
    // }
    // }
    // AssociationRelationship newRelationship = new
    // AssociationRelationship(newClass, sourceClass);
    // arch.getAllRelationships().add(newRelationship);
    // }
    private void moveAttributeToNewClass(Architecture arch, Class sourceClass, List<Attribute> AttributesClass,
                                         Class newClass) throws Exception {
        // if (searchPatternsClass(sourceClass)) {
        Attribute targetAttribute = randomObject(AttributesClass);
        sourceClass.moveAttributeToClass(targetAttribute, newClass);
        for (Concern con : targetAttribute.getOwnConcerns()) {
            newClass.addConcern(con.getName());
        }
        createAssociation(arch, newClass, sourceClass);
        // }
    }

    // --------------------------------------------------------------------------
    public void addManagerClassMutation(double probability, Solution solution) throws JMException {
        LOGGER.info("Executando AddManagerClassMutation");
        try {
            if (PseudoRandom.randDouble() < probability) {
                if (solution.getDecisionVariables()[0].getVariableType() == java.lang.Class
                        .forName(Architecture.ARCHITECTURE_TYPE)) {
                    Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);

                    Package sourceComp = randomObject(new ArrayList<Package>(arch.getAllPackages()));
                    List<Interface> InterfacesComp = new ArrayList<Interface>();
                    InterfacesComp.addAll(sourceComp.getImplementedInterfaces());

                    removeInterfacesInPatternStructureFromArray(InterfacesComp);

                    if (InterfacesComp.size() >= 1) {
                        Interface sourceInterface = randomObject(InterfacesComp);
                        // joao\
                        if (searchPatternsInterface(sourceInterface)) {
                            List<Method> OpsInterface = new ArrayList<Method>();
                            OpsInterface.addAll(sourceInterface.getOperations());
                            if (OpsInterface.size() >= 1) {
                                Method op = randomObject(OpsInterface);

                                Package newComp = arch
                                        .createPackage("Package" + OPLA.contComp_ + getSuffix(sourceComp));
                                OPLA.contComp_++;
                                Interface newInterface = newComp.createInterface("Interface" + OPLA.contInt_++);

                                sourceInterface.moveOperationToInterface(op, newInterface);

                                for (Element implementor : sourceInterface.getImplementors()) {
                                    if (implementor instanceof Package) {
                                        arch.addImplementedInterface(newInterface, (Package) implementor);
                                    }
                                    if (implementor instanceof Class) {
                                        arch.addImplementedInterface(newInterface, (Class) implementor);
                                    }
                                }
                                for (Concern con : op.getOwnConcerns()) {
                                    newInterface.addConcern(con.getName());
                                }
                            }
                            OpsInterface.clear();
                        } // joao^
                    }
                } else {
                    Configuration.logger_.log(Level.SEVERE,
                            "AddManagerClassMutation.doMutation: invalid type. " + "{0}",
                            solution.getDecisionVariables()[0].getVariableType());
                    java.lang.Class<String> cls = java.lang.String.class;
                    String name = cls.getName();
                    throw new JMException("Exception in " + name + ".doMutation()");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --------------------------------------------------------------------------
    public void featureMutation(double probability, Solution solution, String scope) throws JMException {
        try {
            if (PseudoRandom.randDouble() < probability) {
                if (solution.getDecisionVariables()[0].getVariableType().toString()
                        .equals("class " + Architecture.ARCHITECTURE_TYPE)) {

                    final Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
                    final List<Package> allComponents = new ArrayList<Package>(arch.getAllPackages());
                    if (!allComponents.isEmpty()) {
                        final Package selectedComp = randomObject(allComponents);
                        List<Concern> concernsSelectedComp = new ArrayList<Concern>(selectedComp.getAllConcerns());
                        if (concernsSelectedComp.size() > 1) { // = somente para
                            // testes
                            final Concern selectedConcern = randomObject(concernsSelectedComp);
                            List<Package> allComponentsAssignedOnlyToConcern = new ArrayList<Package>(
                                    searchComponentsAssignedToConcern(selectedConcern, allComponents));
                            if (allComponentsAssignedOnlyToConcern.isEmpty()) {
                                OPLA.contComp_++;
                                modularizeConcernInComponent(allComponents,
                                        arch.createPackage("Package" + OPLA.contComp_ + getSuffix(selectedComp)),
                                        selectedConcern, arch);
                            } else {
                                if (allComponentsAssignedOnlyToConcern.size() == 1) {
                                    modularizeConcernInComponent(allComponents,
                                            allComponentsAssignedOnlyToConcern.get(0), selectedConcern, arch);
                                } else {
                                    modularizeConcernInComponent(allComponents,
                                            randomObject(allComponentsAssignedOnlyToConcern), selectedConcern, arch);
                                }
                            }
                            allComponentsAssignedOnlyToConcern.clear();
                        }
                        concernsSelectedComp.clear();
                        allComponents.clear();
                    }

                } else {
                    Configuration.logger_.log(Level.SEVERE, "FeatureMutation.doMutation: invalid type. " + "{0}",
                            solution.getDecisionVariables()[0].getVariableType());
                    java.lang.Class<String> cls = java.lang.String.class;
                    String name = cls.getName();
                    throw new JMException("Exception in " + name + ".doMutation()");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Package> searchComponentsAssignedToConcern(Concern concern, List<Package> allComponents) {
        final List<Package> allComponentsAssignedToConcern = new ArrayList<Package>();
        for (Package component : allComponents) {
            final Set<Concern> numberOfConcernsForPackage = getNumberOfConcernsFor(component);
            if (numberOfConcernsForPackage.size() == 1 && (numberOfConcernsForPackage.contains(concern))) {
                allComponentsAssignedToConcern.add(component);
            }
        }
        return allComponentsAssignedToConcern;
    }

    private Set<Concern> getNumberOfConcernsFor(Package pkg) {
        final Set<Concern> listOfOwnedConcern = new HashSet<Concern>();

        for (Class klass : pkg.getAllClasses()) {
            listOfOwnedConcern.addAll(klass.getOwnConcerns());
        }
        for (Interface inte : pkg.getAllInterfaces()) {
            listOfOwnedConcern.addAll(inte.getOwnConcerns());
        }

        return listOfOwnedConcern;
    }

    private void modularizeConcernInComponent(List<Package> allComponents, Package targetComponent, Concern concern,
                                              Architecture arch) {
        try {
            Iterator<Package> itrComp = allComponents.iterator();
            while (itrComp.hasNext()) {
                Package comp = itrComp.next();
                if (!comp.equals(targetComponent) && checkSameLayer(comp, targetComponent)) {
                    final Set<Interface> allInterfaces = new HashSet<Interface>(comp.getAllInterfaces());
                    allInterfaces.addAll(comp.getImplementedInterfaces());

                    Iterator<Interface> itrInterface = allInterfaces.iterator();
                    while (itrInterface.hasNext()) {
                        Interface interfaceComp = itrInterface.next();
                        // JOAO\
                        if (searchPatternsInterface(interfaceComp)) {
                            if (interfaceComp.getOwnConcerns().size() == 1 && interfaceComp.containsConcern(concern)) {
                                moveInterfaceToComponent(interfaceComp, targetComponent, comp, arch, concern); // EDIPO
                                // TESTADO
                            } else if (!interfaceComp.getPatternsOperations().hasPatternApplied()) {
                                List<Method> operationsInterfaceComp = new ArrayList<Method>(
                                        interfaceComp.getOperations());
                                Iterator<Method> itrOperation = operationsInterfaceComp.iterator();
                                while (itrOperation.hasNext()) {
                                    Method operation = itrOperation.next();
                                    if (operation.getOwnConcerns().size() == 1 && operation.containsConcern(concern)) {
                                        moveOperationToComponent(operation, interfaceComp, targetComponent, comp, arch,
                                                concern);
                                    }
                                }
                            }
                        } // JOAO^
                    }

                    allInterfaces.clear();
                    final List<Class> allClasses = new ArrayList<Class>(comp.getAllClasses());
                    Iterator<Class> ItrClass = allClasses.iterator();
                    while (ItrClass.hasNext()) {
                        Class classComp = ItrClass.next();
                        //JOAO\
                        if (searchPatternsClass(classComp)) {
                            if (comp.getAllClasses().contains(classComp)) {
                                if ((classComp.getOwnConcerns().size() == 1) && (classComp.containsConcern(concern))) {
                                    if (!searchForGeneralizations(classComp)) // realiza
                                    // a
                                    // muta������������o
                                    // em
                                    // classe
                                    // que
                                    // n������oo
                                    // est������o
                                    // numa
                                    // hierarquia
                                    // de
                                    // heran������a
                                    {
                                        moveClassToComponent(classComp, targetComponent, comp, arch, concern);
                                    } else {
                                        moveHierarchyToComponent(classComp, targetComponent, comp, arch, concern); // realiza
                                        // a
                                        // muta������������o
                                        // em
                                        // classes
                                        // est������o
                                        // numa
                                        // hierarquia
                                        // de
                                        // herarquia
                                    }
                                } else {
                                    if (!searchForGeneralizations(classComp)) {
                                        if (!isVarPointOfConcern(arch, classComp, concern)
                                                && !isVariantOfConcern(arch, classComp, concern)) {
                                            final List<Attribute> attributesClassComp = new ArrayList<Attribute>(
                                                    classComp.getAllAttributes());
                                            Iterator<Attribute> irtAttribute = attributesClassComp.iterator();
                                            while (irtAttribute.hasNext()) {
                                                Attribute attribute = irtAttribute.next();
                                                if (attribute.getOwnConcerns().size() == 1
                                                        && attribute.containsConcern(concern)) {
                                                    moveAttributeToComponent(attribute, classComp, targetComponent,
                                                            comp, arch, concern);
                                                }
                                            }
                                            attributesClassComp.clear();
                                            if (!classComp.getPatternsOperations().hasPatternApplied()) {
                                                final List<Method> methodsClassComp = new ArrayList<Method>(
                                                        classComp.getAllMethods());
                                                Iterator<Method> irtMethod = methodsClassComp.iterator();
                                                while (irtMethod.hasNext()) {
                                                    Method method = irtMethod.next();
                                                    if (method.getOwnConcerns().size() == 1
                                                            && method.containsConcern(concern)) {
                                                        moveMethodToComponent(method, classComp, targetComponent, comp,
                                                                arch, concern);
                                                    }
                                                }
                                                methodsClassComp.clear();
                                            }
                                        }
                                    }
                                }
                            }
                        }//JOAO^
                    }
                    allClasses.clear();
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void moveClassToComponent(Class classComp, Package targetComp, Package sourceComp,
                                      Architecture architecture, Concern concern) {
        // joao\
        if (searchPatternsClass(classComp)) {
            sourceComp.moveClassToPackage(classComp, targetComp);
        }
    }

    private void moveAttributeToComponent(Attribute attribute, Class classComp, Package targetComp, Package sourceComp,
                                          Architecture architecture, Concern concern) throws ConcernNotFoundException {
        // joao\
        if (searchPatternsClass(classComp)) {
            final Class targetClass = findOrCreateClassWithConcern(targetComp, concern);
            classComp.moveAttributeToClass(attribute, targetClass);
            createAssociation(architecture, targetClass, classComp);
        }
    }

    private void moveMethodToComponent(Method method, Class classComp, Package targetComp, Package sourceComp,
                                       Architecture architecture, Concern concern) throws ConcernNotFoundException {
        // joao\
        if (searchPatternsClass(classComp)) {
            final Class targetClass = findOrCreateClassWithConcern(targetComp, concern);
            classComp.moveMethodToClass(method, targetClass);
            createAssociation(architecture, targetClass, classComp);
        }
    }

    // add por ��dipo
    private Class findOrCreateClassWithConcern(Package targetComp, Concern concern) throws ConcernNotFoundException {
        Class targetClass = null;
        for (Class cls : targetComp.getAllClasses()) {
            if (cls.containsConcern(concern)) {
                targetClass = cls;
            }
        }

        if (targetClass == null) {
            targetClass = targetComp.createClass("Class" + OPLA.contClass_++, false);
            targetClass.addConcern(concern.getName());
        }
        return targetClass;
    }

    private void moveInterfaceToComponent(Interface interfaceComp, Package targetComp, Package sourceComp,
                                          Architecture architecture, Concern concernSelected) {
        // joao\
        if (searchPatternsInterface(interfaceComp)) {
            if (!sourceComp.moveInterfaceToPackage(interfaceComp, targetComp)) {
                architecture.moveElementToPackage(interfaceComp, targetComp);
            }

            for (Element implementor : interfaceComp.getImplementors()) {
                if (implementor instanceof Package) {
                    if (targetComp.getAllClasses().size() == 1) {
                        final Class klass = targetComp.getAllClasses().iterator().next();
                        for (Concern concern : klass.getOwnConcerns()) {
                            if (interfaceComp.containsConcern(concern)) {
                                architecture.removeImplementedInterface(interfaceComp, sourceComp);
                                addExternalInterface(targetComp, architecture, interfaceComp);
                                addImplementedInterface(targetComp, architecture, interfaceComp, klass);
                            }
                        }
                        return;
                    } else if (targetComp.getAllClasses().size() > 1) {
                        final List<Class> targetClasses = allClassesWithConcerns(concernSelected,
                                targetComp.getAllClasses());
                        final Class klass = randonClass(targetClasses);
                        architecture.removeImplementedInterface(interfaceComp, sourceComp);
                        addExternalInterface(targetComp, architecture, interfaceComp);
                        addImplementedInterface(targetComp, architecture, interfaceComp, klass);
                        return;
                    } else {
                        // Busca na arquitetura como um todo
                        final List<Class> targetClasses = allClassesWithConcerns(concernSelected,
                                architecture.getAllClasses());
                        final Class klass = randonClass(targetClasses);
                        architecture.removeImplementedInterface(interfaceComp, sourceComp);
                        addExternalInterface(targetComp, architecture, interfaceComp);
                        addImplementedInterface(targetComp, architecture, interfaceComp, klass);
                    }
                }
            }
        }
    }

    private Class randonClass(List<Class> targetClasses) {
        Collections.shuffle(targetClasses);
        Class randonKlass = targetClasses.get(0);
        return randonKlass;
    }

    /**
     * Retorna todas as classes que tiverem algum dos concerns presentes na
     * lista ownConcerns.
     *
     * @param ownConcerns
     * @param allClasses
     * @return
     */
    private List<Class> allClassesWithConcerns(Concern c, Set<Class> allClasses) {
        List<Class> klasses = new ArrayList<Class>();
        for (Class klass : allClasses) {
            for (Concern concernKlass : klass.getOwnConcerns()) {
                if (concernKlass.getName().equalsIgnoreCase(c.getName())) {
                    klasses.add(klass);
                }
            }
        }
        return klasses;
    }

    private void moveOperationToComponent(Method operation, Interface sourceInterface, Package targetComp,
                                          Package sourceComp, Architecture architecture, Concern concern) throws ConcernNotFoundException {
        Interface targetInterface = null;
        // joao\
        if (searchPatternsInterface(sourceInterface)) {
            targetInterface = searchForInterfaceWithConcern(concern, targetComp);

            if (targetInterface == null) {
                targetInterface = targetComp.createInterface("Interface" + OPLA.contInt_++);
                sourceInterface.moveOperationToInterface(operation, targetInterface);
                targetInterface.addConcern(concern.getName());
            } else {
                sourceInterface.moveOperationToInterface(operation, targetInterface);
            }

            addRelationship(sourceInterface, targetComp, sourceComp, architecture, concern, targetInterface);
        } // joao^
    }

    // duvida
    private void addRelationship(Interface sourceInterface, Package targetComp, Package sourceComp,
                                 Architecture architecture, Concern concern, Interface targetInterface) {
        for (Element implementor : sourceInterface.getImplementors()) {
            // Se quem estiver implementando a interface que teve a operacao
            // movida for um pacote.
            if (implementor instanceof Package) {
                /**
                 * Verifica se o pacote tem somente um classe, recupera a mesma
                 * e verifica se a interface destino (targetInterface) possui
                 * algum interesse da classe recuperada. Caso tiver, remove
                 * implemented interface (sourceInterface) de sourceComp.
                 * Adiciona a interface tergetInterface em seu pacote ou na
                 * arquitetura Verifica se j�� existe um relacionamento de
                 * realiza����o entre targetInterface e klass, caso n��o tiver
                 * adiciona targetInterface como sendo implemenda por klass.
                 */
                if (targetComp.getAllClasses().size() == 1) {
                    final Class klass = targetComp.getAllClasses().iterator().next();
                    // joao
                    if (searchPatternsClass(klass)) {
                        for (Concern c : klass.getOwnConcerns()) {
                            // joao
                            if (searchPatternsInterface(targetInterface)) {
                                if (targetInterface.containsConcern(c)) {
                                    architecture.removeImplementedInterface(sourceInterface, sourceComp);
                                    addExternalInterface(targetComp, architecture, targetInterface);
                                    addImplementedInterface(targetComp, architecture, targetInterface, klass);
                                    return;
                                }
                            }
                        }
                    }

                    /**
                     * Caso o pacote destino tiver mais de uma classe. Busca
                     * dentre essas classes todas com o interesse em quest��o
                     * (concern), e seleciona um aleatoriamente. Remove
                     * implemented interface (sourceInterface) de sourceComp.
                     * Adiciona a interface tergetInterface em seu pacote ou na
                     * arquitetura Verifica se j�� existe um relacionamento de
                     * realiza����o entre targetInterface e klass, caso n��o
                     * tiver adiciona targetInterface como sendo implemenda por
                     * klass.
                     */
                } else if (targetComp.getAllClasses().size() > 1) {
                    final List<Class> targetClasses = allClassesWithConcerns(concern, targetComp.getAllClasses());
                    final Class klass = randonClass(targetClasses);
                    // joao
                    if (searchPatternsClass(klass)) {
                        architecture.removeImplementedInterface(sourceInterface, sourceComp);
                        addExternalInterface(targetComp, architecture, targetInterface);
                        addImplementedInterface(targetComp, architecture, targetInterface, klass);
                        return;
                    }
                } else {
                    /**
                     * Caso o pacote for vazio, faz um busca nas classes da
                     * arquitetura como um todo.
                     */
                    final List<Class> targetClasses = allClassesWithConcerns(concern, architecture.getAllClasses());
                    final Class klass = randonClass(targetClasses);
                    if (searchPatternsClass(klass)) {
                        if (klass != null) {
                            architecture.removeImplementedInterface(sourceInterface, sourceComp);
                            addExternalInterface(targetComp, architecture, targetInterface);
                            addImplementedInterface(targetComp, architecture, targetInterface, klass);
                        }
                    }
                }
            }

            /**
             * Recupera quem estava implementando a interface que teve a
             * operacao movida e cria uma realizacao entre a interface que
             * recebeu a operacao (targetInterface) e quem tava implementando a
             * interface que teve a operacao movida (sourceInterface).
             *
             */
            if (implementor instanceof Class) {
                // joao\
                if (searchPatternsInterface(sourceInterface) && searchPatternsInterface(targetInterface)) {
                    architecture.removeImplementedInterface(sourceInterface, sourceComp);
                    addExternalInterface(targetComp, architecture, targetInterface);
                    addImplementedInterface(targetComp, architecture, targetInterface, (Class) implementor);
                }
            }
        }
    }

    private void addImplementedInterface(Package targetComp, Architecture architecture, Interface targetInterface,
                                         Class klass) {
        if (!packageTargetHasRealization(targetComp, targetInterface)) {
            architecture.addImplementedInterface(targetInterface, klass);
        }
    }

    private void addExternalInterface(Package targetComp, Architecture architecture, Interface targetInterface) {
        final String packageNameInterface = UtilResources.extractPackageName(targetInterface.getNamespace().trim());
        if (packageNameInterface.equalsIgnoreCase("model")) {
            architecture.addExternalInterface(targetInterface);
        } else {
            targetComp.addExternalInterface(targetInterface);
        }
    }

    private boolean packageTargetHasRealization(Package targetComp, Interface targetInterface) {
        for (Relationship r : targetComp.getRelationships()) {
            if (r instanceof RealizationRelationship) {
                final RealizationRelationship realization = (RealizationRelationship) r;
                if (realization.getSupplier().equals(targetInterface)) {
                    return true;
                }
            }
        }

        return false;
    }

    // //��dipo
    // private void addConcernToNewInterface(Concern concern, Interface
    // targetInterface, Interface sourceInterface) {
    // Set<Concern> interfaceConcerns = sourceInterface.getOwnConcerns();
    // try {
    // for(Concern c : interfaceConcerns)
    // targetInterface.addConcern(c.getName());
    // } catch (ConcernNotFoundException e) {
    // e.printStackTrace();
    // }
    //
    // for(Method operation : sourceInterface.getOperations()){
    // Set<Concern> operationConcerns = operation.getOwnConcerns();
    // for(Method o : targetInterface.getOperations()){
    // for(Concern c : operationConcerns){
    // try {
    // o.addConcern(c.getName());
    // } catch (ConcernNotFoundException e) {
    // e.printStackTrace();
    // }
    // }
    // }
    //
    // }
    // }

    // ��dipo M��todo
    private Interface searchForInterfaceWithConcern(Concern concern, Package targetComp) {
        for (Interface itf : targetComp.getImplementedInterfaces()) {
            if (itf.containsConcern(concern)) {
                return itf;
            }
        }

        for (Interface itf : targetComp.getAllInterfaces()) {
            if (itf.containsConcern(concern)) {
                return itf;
            }
        }

        return null;
    }

    public Object execute(Object object) throws Exception {
        Solution solution = (Solution) object;
        Double probability = (Double) getParameter("probability");

        if (probability == null) {
            Configuration.logger_.severe("FeatureMutation.execute: probability not specified");
            java.lang.Class<String> cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        }

        this.doMutation(mutationProbability, solution);

        if (!this.isValidSolution(((Architecture) solution.getDecisionVariables()[0]))) {
            Architecture clone;
            clone = ((Architecture) solution.getDecisionVariables()[0]).deepClone();
            solution.getDecisionVariables()[0] = clone;
            OPLA.contDiscardedSolutions_++;
        }

        return solution;
    }

    // -------------------------------------------------------------------------------------------------
    public <T> T randomObject(List<T> allObjects) {
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

    // -------------------------------------------------------------------------------------------------
    // Thelma: m��todo adicionado para verificar se os componentes nos quais as
    // mutacoes serao realizadas estao na mesma camada da arquitetura
    private boolean checkSameLayer(Package source, Package target) {
        boolean sameLayer = false;
        if ((source.getName().endsWith("Mgr") && target.getName().endsWith("Mgr"))
                || (source.getName().endsWith("Ctrl") && target.getName().endsWith("Ctrl"))
                || (source.getName().endsWith("GUI") && target.getName().endsWith("GUI"))) {
            sameLayer = true;
        }
        return sameLayer;
    }

    // -------------------------------------------------------------------------------------------------
    // Thelma: m��todo adicionado para retornar o sufixo do nome do componente
    private String getSuffix(Package comp) {
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

    // -------------------------------------------------------------------------------------------------
    // Thelma: m��todo adicionado para verificar se a classe tem uma
    // variabilidade relativa ao concern
    private boolean isVarPointOfConcern(Architecture arch, Class cls, Concern concern) {
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

    // -------------------------------------------------------------------------------------------------
    // Thelma: m��todo adicionado para verificar se a classe �� variant de uma
    // variabilidade relativa ao concern
    private boolean isVariantOfConcern(Architecture arch, Class cls, Concern concern) {
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

    /**
     * metodo que move a hierarquia de classes para um outro componente que esta
     * modularizando o interesse concern
     *
     *
     * @param classComp
     *            - Classe selecionada
     * @param targetComp
     *            - Pacote destino
     * @param sourceComp
     *            - Pacote de origem
     * @param architecture
     *            - arquiteutra
     * @param concern
     *            - interesse sendo modularizado
     */
    private void moveHierarchyToComponent(Class classComp, Package targetComp, Package sourceComp,
                                          Architecture architecture, Concern concern) {
        architecture.forGeneralization().moveGeneralizationToPackage(getGeneralizationRelationshipForClass(classComp),
                targetComp);
    }

    // EDIPO Identifica quem �� o parent para a classComp
    /**
     * Dado um {@link Element} retorna a {@link GeneralizationRelationship} no
     * qual o mesmo pertence.
     *
     * @param element
     * @return {@link GeneralizationRelationship}
     */
    private GeneralizationRelationship getGeneralizationRelationshipForClass(Element element) {
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

    // verificar se a classe �� variant de uma variabilidade
    private boolean isOptional(Architecture arch, Class cls) {
        boolean isOptional = false;
        if (cls.getVariantType() != null) {
            if (cls.getVariantType().toString().equalsIgnoreCase("optional")) {
                return true;
            }
        }
        return isOptional;
    }

    // -------------------------------------------------------------------------------------------------
    // verificar se a classe �� variant de uma variabilidade

    private boolean isVariant(Architecture arch, Class cls) {
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

    private boolean isVarPoint(Architecture arch, Class cls) {
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

    // Thelma - Dez2013 m��todo adicionado
    // verify if the architecture contains a valid PLA design, i.e., if there is
    // not any interface without relationships in the architecture.
    private boolean isValidSolution(Architecture solution) {
        boolean isValid = true;
        List<Interface> allInterfaces = new ArrayList<Interface>(solution.getAllInterfaces());
        if (!allInterfaces.isEmpty()) {
            for (Interface itf : allInterfaces) {
                if ((itf.getImplementors().isEmpty()) && (itf.getDependents().isEmpty())
                        && (!itf.getOperations().isEmpty())) {
                    return false;
                }
            }
        }
        return isValid;
    }

    public List<String> getMutationOperators() {
        return mutationOperators;
    }

}