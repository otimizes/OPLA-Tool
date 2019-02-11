/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.operator.impl;

import arquitetura.exceptions.ConcernNotFoundException;
import arquitetura.helpers.UtilResources;
import arquitetura.representation.*;
import arquitetura.representation.relationship.AssociationRelationship;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.RealizationRelationship;
import arquitetura.representation.relationship.Relationship;
import br.ufpr.inf.opla.patterns.operator.AbstractMutationOperator;
import jmetal4.core.Solution;
import jmetal4.operators.mutation.PLAFeatureMutation;
import jmetal4.problems.OPLA;
import jmetal4.util.Configuration;
import jmetal4.util.JMException;
import jmetal4.util.PseudoRandom;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.logging.Level;

/**
 * @author giovaniguizzo
 */
public class PLAMutation extends AbstractMutationOperator {

    private static final long serialVersionUID = 1L;
    static Logger LOGGER = LogManager.getLogger(PLAFeatureMutation.class.getName());

    public PLAMutation(Map<String, Object> parameters) {
        super(parameters);
    }

    @Override
    protected boolean hookMutation(Solution solution, Double probability) throws Exception {
        return doMutation(probability, solution);
    }

    public boolean doMutation(double probability, Solution solution) throws Exception {
        String scope = "sameComponent"; //"allComponents" usar "sameComponent" para que a troca seja realizada dentro do mesmo componente da arquitetura
        String scopeLevels = "allLevels"; //usar "oneLevel" para não verificar a presença de interesses nos atributos e métodos

        int r = PseudoRandom.randInt(0, 5);
        switch (r) {
            case 0:
                return FeatureMutation(probability, solution, scopeLevels);
            case 1:
                return MoveMethodMutation(probability, solution, scope);
            case 2:
                return MoveAttributeMutation(probability, solution, scope);
            case 3:
                return MoveOperationMutation(probability, solution);
            case 4:
                return AddClassMutation(probability, solution, scope);
            case 5:
                return AddManagerClassMutation(probability, solution);
        }
        return false;
    }

    //--------------------------------------------------------------------------
    //método para verificar se algum dos relacionamentos recebidos é generalização
    private boolean searchForGeneralizations(arquitetura.representation.Class cls) {
        for (Relationship relationship : cls.getRelationships()) {
            if (relationship instanceof GeneralizationRelationship) {
                if (((GeneralizationRelationship) relationship).getChild().equals(cls) || ((GeneralizationRelationship) relationship).getParent().equals(cls)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean MoveAttributeMutation(double probability, Solution solution, String scope) throws JMException {
        LOGGER.info("Executando MoveAttributeMutation");
        try {
            if (PseudoRandom.randDouble() < probability) {
                if (solution.getDecisionVariables()[0].getVariableType() == java.lang.Class.forName(Architecture.ARCHITECTURE_TYPE)) {
                    Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
                    if ("sameComponent".equals(scope)) {
                        List<arquitetura.representation.Class> ClassesComp = new ArrayList<arquitetura.representation.Class>(randomObject(new ArrayList<arquitetura.representation.Package>(arch.getAllPackages())).getAllClasses());
                        if (ClassesComp.size() > 1) {
                            arquitetura.representation.Class targetClass = randomObject(ClassesComp);
                            arquitetura.representation.Class sourceClass = randomObject(ClassesComp);
                            if ((sourceClass != null) && (!searchForGeneralizations(sourceClass))
                                    && (sourceClass.getAllAttributes().size() > 1)
                                    && (sourceClass.getAllMethods().size() > 1)
                                    && (!isVarPoint(arch, sourceClass))
                                    && (!isVariant(arch, sourceClass))
                                    && (!isOptional(arch, sourceClass))) {
                                if ((targetClass != null) && (!(targetClass.equals(sourceClass)))) {
                                    moveAttribute(arch, targetClass, sourceClass);
                                }
                            }
                        }
                        ClassesComp.clear();
                    } else {
                        if ("allComponents".equals(scope)) {
                            arquitetura.representation.Package sourceComp = randomObject(new ArrayList<arquitetura.representation.Package>(arch.getAllPackages()));
                            List<arquitetura.representation.Class> ClassesSourceComp = new ArrayList<arquitetura.representation.Class>(sourceComp.getAllClasses());
                            if (ClassesSourceComp.size() >= 1) {
                                arquitetura.representation.Class sourceClass = randomObject(ClassesSourceComp);
                                if ((sourceClass != null) && (!searchForGeneralizations(sourceClass))
                                        && (sourceClass.getAllAttributes().size() > 1)
                                        && (sourceClass.getAllMethods().size() > 1)
                                        && (!isVarPoint(arch, sourceClass))
                                        && (!isVariant(arch, sourceClass))
                                        && (!isOptional(arch, sourceClass))) {
                                    arquitetura.representation.Package targetComp = randomObject(new ArrayList<arquitetura.representation.Package>(arch.getAllPackages()));
                                    if (checkSameLayer(sourceComp, targetComp)) {
                                        List<arquitetura.representation.Class> ClassesTargetComp = new ArrayList<arquitetura.representation.Class>(targetComp.getAllClasses());
                                        if (ClassesTargetComp.size() >= 1) {
                                            arquitetura.representation.Class targetClass = randomObject(ClassesTargetComp);
                                            if ((targetClass != null) && (!(targetClass.equals(sourceClass)))) {
                                                moveAttribute(arch, targetClass, sourceClass);
                                            }
                                        }
                                    }
                                }
                            }
                            ClassesSourceComp.clear();
                        }
                    }
                } else {
                    Configuration.logger_.log(Level.SEVERE, "MoveAttributeMutation.doMutation: invalid type. " + "{0}", solution.getDecisionVariables()[0].getVariableType());
                    java.lang.Class<String> cls = java.lang.String.class;
                    String name = cls.getName();
                    throw new JMException("Exception in " + name + ".doMutation()");
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void moveAttribute(Architecture arch, arquitetura.representation.Class targetClass, arquitetura.representation.Class sourceClass) throws JMException, Exception {
        List<Attribute> attributesClass = new ArrayList<Attribute>(sourceClass.getAllAttributes());
        if (attributesClass.size() >= 1) {
            if (sourceClass.moveAttributeToClass(randomObject(attributesClass), targetClass)) {
                createAssociation(arch, targetClass, sourceClass);
            }
        }

        attributesClass.clear();
    }

    //Add por Édipo
    private void createAssociation(Architecture arch, arquitetura.representation.Class targetClass, arquitetura.representation.Class sourceClass) {
        arch.addRelationship(new AssociationRelationship(targetClass, sourceClass));
    }

    //--------------------------------------------------------------------------
    public boolean MoveMethodMutation(double probability, Solution solution, String scope) throws JMException {
        if (PseudoRandom.randDouble() < probability) {
            final Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
            if ("sameComponent".equals(scope)) {
                final arquitetura.representation.Package sourceComp = randomObject(new ArrayList<arquitetura.representation.Package>(arch.getAllPackages()));
                List<arquitetura.representation.Class> ClassesComp = new ArrayList<arquitetura.representation.Class>(sourceComp.getAllClasses());
                removeClassesInPatternStructureFromArray(ClassesComp);
                if (ClassesComp.size() > 1) {
                    final arquitetura.representation.Class targetClass = randomObject(ClassesComp);
                    final arquitetura.representation.Class sourceClass = randomObject(ClassesComp);
                    if ((sourceClass != null) && (!searchForGeneralizations(sourceClass))
                            && (sourceClass.getAllAttributes().size() > 1)
                            && (sourceClass.getAllMethods().size() > 1)
                            && (!isVarPoint(arch, sourceClass))
                            && (!isVariant(arch, sourceClass))
                            && (!isOptional(arch, sourceClass))) {
                        if ((targetClass != null) && (!(targetClass.equals(sourceClass)))) {
                            moveMethod(arch, targetClass, sourceClass, sourceComp, sourceComp);
                        }
                    }
                }
                ClassesComp.clear();
            } else {
                if ("allComponents".equals(scope)) {
                    final arquitetura.representation.Package sourceComp = randomObject(new ArrayList<arquitetura.representation.Package>(arch.getAllPackages()));
                    final List<arquitetura.representation.Class> ClassesSourceComp = new ArrayList<arquitetura.representation.Class>(sourceComp.getAllClasses());
                    removeClassesInPatternStructureFromArray(ClassesSourceComp);
                    if (ClassesSourceComp.size() >= 1) {
                        final arquitetura.representation.Class sourceClass = randomObject(ClassesSourceComp);
                        if ((sourceClass != null) && (!searchForGeneralizations(sourceClass))
                                && (sourceClass.getAllAttributes().size() > 1)
                                && (sourceClass.getAllMethods().size() > 1)
                                && (!isVarPoint(arch, sourceClass))
                                && (!isVariant(arch, sourceClass))
                                && (!isOptional(arch, sourceClass))) {
                            final arquitetura.representation.Package targetComp = randomObject(new ArrayList<arquitetura.representation.Package>(arch.getAllPackages()));
                            if (checkSameLayer(sourceComp, targetComp)) {
                                final List<arquitetura.representation.Class> ClassesTargetComp = new ArrayList<arquitetura.representation.Class>(targetComp.getAllClasses());
                                if (ClassesTargetComp.size() >= 1) {
                                    final arquitetura.representation.Class targetClass = randomObject(ClassesTargetComp);
                                    if ((targetClass != null) && (!(targetClass.equals(sourceClass)))) {
                                        moveMethod(arch, targetClass, sourceClass, targetComp, sourceComp);
                                    }
                                }
                            }
                        }
                    }
                    ClassesSourceComp.clear();
                }
            }
            return true;
        }
        return false;
    }

    private void moveMethod(Architecture arch, arquitetura.representation.Class targetClass, arquitetura.representation.Class sourceClass, arquitetura.representation.Package targetComp, arquitetura.representation.Package sourceComp) {
        final List<Method> MethodsClass = new ArrayList<Method>(sourceClass.getAllMethods());
        if (MethodsClass.size() >= 1) {
            if (sourceClass.moveMethodToClass(randomObject(MethodsClass), targetClass)) {
                createAssociation(arch, targetClass, sourceClass);
            }
        }

        MethodsClass.clear();
    }

    //--------------------------------------------------------------------------
    public boolean MoveOperationMutation(double probability, Solution solution) throws JMException {
        LOGGER.info("Executando MoveOperationMutation");
        try {
            if (PseudoRandom.randDouble() < probability) {
                if (solution.getDecisionVariables()[0].getVariableType() == java.lang.Class.forName(Architecture.ARCHITECTURE_TYPE)) {
                    Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);

                    arquitetura.representation.Package sourceComp = randomObject(new ArrayList<arquitetura.representation.Package>(arch.getAllPackages()));
                    arquitetura.representation.Package targetComp = randomObject(new ArrayList<arquitetura.representation.Package>(arch.getAllPackages()));

                    if (checkSameLayer(sourceComp, targetComp)) {
                        List<Interface> InterfacesSourceComp = new ArrayList<Interface>();
                        List<Interface> InterfacesTargetComp = new ArrayList<Interface>();

                        InterfacesSourceComp.addAll(sourceComp.getImplementedInterfaces());
                        removeInterfacesInPatternStructureFromArray(InterfacesSourceComp);

                        InterfacesTargetComp.addAll(targetComp.getImplementedInterfaces());

                        if ((InterfacesSourceComp.size() >= 1) && (InterfacesTargetComp.size() >= 1)) {
                            Interface targetInterface = randomObject(InterfacesTargetComp);
                            Interface sourceInterface = randomObject(InterfacesSourceComp);

                            if (targetInterface != sourceInterface) {
                                List<Method> OpsInterface = new ArrayList<Method>();
                                OpsInterface.addAll(sourceInterface.getOperations());
                                if (OpsInterface.size() >= 1) {
                                    sourceInterface.moveOperationToInterface(randomObject(OpsInterface), targetInterface);
                                    for (Element implementor : sourceInterface.getImplementors()) {
                                        if (implementor instanceof arquitetura.representation.Package) {
                                            arch.addImplementedInterface(targetInterface, (arquitetura.representation.Package) implementor);
                                        }
                                        if (implementor instanceof arquitetura.representation.Class) {
                                            arch.addImplementedInterface(targetInterface, (arquitetura.representation.Class) implementor);
                                        }
                                    }
                                    OpsInterface.clear();
                                }
                            }
                        }
                        InterfacesTargetComp.clear();
                        InterfacesSourceComp.clear();

                    }
                } else {
                    Configuration.logger_.log(Level.SEVERE, "MoveOperationMutation.doMutation: invalid type. "
                            + "{0}", solution.getDecisionVariables()[0].getVariableType());
                    java.lang.Class<String> cls = java.lang.String.class;
                    String name = cls.getName();
                    throw new JMException("Exception in " + name + ".doMutation()");
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //--------------------------------------------------------------------------
    public boolean AddClassMutation(double probability, Solution solution, String scope) throws JMException {
        LOGGER.info("Executando AddClassMutation ");
        try {
            if (PseudoRandom.randDouble() < probability) {
                if (solution.getDecisionVariables()[0].getVariableType() == java.lang.Class.forName(Architecture.ARCHITECTURE_TYPE)) {
                    Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
                    arquitetura.representation.Package sourceComp = randomObject(new ArrayList<arquitetura.representation.Package>(arch.getAllPackages()));
                    List<arquitetura.representation.Class> ClassesComp = new ArrayList<arquitetura.representation.Class>(sourceComp.getAllClasses());
                    removeClassesInPatternStructureFromArray(ClassesComp);

                    if (ClassesComp.size() >= 1) {
                        arquitetura.representation.Class sourceClass = randomObject(ClassesComp);
                        if ((sourceClass != null) && (!searchForGeneralizations(sourceClass))
                                && (sourceClass.getAllAttributes().size() > 1)
                                && (sourceClass.getAllMethods().size() > 1)
                                && (!isVarPoint(arch, sourceClass))
                                && (!isVariant(arch, sourceClass))
                                && (!isOptional(arch, sourceClass))) {
                            if (PseudoRandom.randInt(0, 1) == 0) { //attribute
                                List<Attribute> AttributesClass = new ArrayList<Attribute>(sourceClass.getAllAttributes());
                                if (AttributesClass.size() >= 1) {
                                    if ("sameComponent".equals(scope)) {
                                        moveAttributeToNewClass(arch, sourceClass, AttributesClass, sourceComp.createClass("Class" + OPLA.contClass_++, false));
                                    } else {
                                        if ("allComponents".equals(scope)) {
                                            arquitetura.representation.Package targetComp = randomObject(new ArrayList<arquitetura.representation.Package>(arch.getAllPackages()));
                                            if (checkSameLayer(sourceComp, targetComp)) {
                                                moveAttributeToNewClass(arch, sourceClass, AttributesClass, targetComp.createClass("Class" + OPLA.contClass_++, false));
                                            }
                                        }
                                    }
                                    AttributesClass.clear();
                                }
                            } else { //method
                                List<Method> MethodsClass = new ArrayList<Method>(sourceClass.getAllMethods());
                                if (MethodsClass.size() >= 1) {
                                    if ("sameComponent".equals(scope)) {
                                        moveMethodToNewClass(arch, sourceClass, MethodsClass, sourceComp.createClass("Class" + OPLA.contClass_++, false));
                                    } else {
                                        if ("allComponents".equals(scope)) {
                                            arquitetura.representation.Package targetComp = randomObject(new ArrayList<arquitetura.representation.Package>(arch.getAllPackages()));
                                            if (checkSameLayer(sourceComp, targetComp)) {
                                                moveMethodToNewClass(arch, sourceClass, MethodsClass, targetComp.createClass("Class" + OPLA.contClass_++, false));
                                            }
                                        }
                                    }
                                    MethodsClass.clear();
                                }
                            }
                        }
                    }
                    ClassesComp.clear();

                } else {
                    Configuration.logger_.log(Level.SEVERE, "AddClassMutation.doMutation: invalid type. "
                            + "{0}", solution.getDecisionVariables()[0].getVariableType());
                    java.lang.Class<String> cls = java.lang.String.class;
                    String name = cls.getName();
                    throw new JMException("Exception in " + name + ".doMutation()");
                }
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void removeClassesInPatternStructureFromArray(List<arquitetura.representation.Class> ClassesComp) {
        for (int i = 0; i < ClassesComp.size(); i++) {
            arquitetura.representation.Class klass = ClassesComp.get(i);
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

    private void moveMethodToNewClass(Architecture arch, arquitetura.representation.Class sourceClass, List<Method> MethodsClass, arquitetura.representation.Class newClass) throws Exception {
        Method targetMethod = randomObject(MethodsClass);
        sourceClass.moveMethodToClass(targetMethod, newClass);
        //if (targetMethod.isAbstract()) targetMethod.setAbstract(false);
        for (Concern con : targetMethod.getOwnConcerns()) {
            newClass.addConcern(con.getName());
        }
        createAssociation(arch, newClass, sourceClass);
    }

    //	private void moveMethodAllComponents(Architecture arch, Class sourceClass, List<Method> MethodsClass, Class newClass) throws JMException {
    //		Method targetMethod = randomObject (MethodsClass);
    //		sourceClass.moveMethodToClass(targetMethod, newClass);
    //		//if (targetMethod.isAbstract()) targetMethod.setAbstract(false);
    //		for (Concern con: targetMethod.getOwnConcerns()){
    //			try {
    //				newClass.addConcern(con.getName());
    //			} catch (ConcernNotFoundException e) {
    //				e.printStackTrace();
    //			}
    //		}
    //		AssociationRelationship newRelationship = new AssociationRelationship(newClass, sourceClass);
    //		arch.getAllRelationships().add(newRelationship);
    //	}
    private void moveAttributeToNewClass(Architecture arch, arquitetura.representation.Class sourceClass, List<Attribute> AttributesClass, arquitetura.representation.Class newClass) throws Exception {
        Attribute targetAttribute = randomObject(AttributesClass);
        sourceClass.moveAttributeToClass(targetAttribute, newClass);
        for (Concern con : targetAttribute.getOwnConcerns()) {
            newClass.addConcern(con.getName());
        }
        createAssociation(arch, newClass, sourceClass);

    }

    //--------------------------------------------------------------------------
    public boolean AddManagerClassMutation(double probability, Solution solution) throws JMException {
        LOGGER.info("Executando AddManagerClassMutation");
        try {
            if (PseudoRandom.randDouble() < probability) {
                if (solution.getDecisionVariables()[0].getVariableType() == java.lang.Class.forName(Architecture.ARCHITECTURE_TYPE)) {
                    Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);

                    arquitetura.representation.Package sourceComp = randomObject(new ArrayList<arquitetura.representation.Package>(arch.getAllPackages()));
                    List<Interface> InterfacesComp = new ArrayList<Interface>();
                    InterfacesComp.addAll(sourceComp.getImplementedInterfaces());

                    removeInterfacesInPatternStructureFromArray(InterfacesComp);

                    if (InterfacesComp.size() >= 1) {
                        Interface sourceInterface = randomObject(InterfacesComp);
                        List<Method> OpsInterface = new ArrayList<Method>();
                        OpsInterface.addAll(sourceInterface.getOperations());
                        if (OpsInterface.size() >= 1) {
                            Method op = randomObject(OpsInterface);

                            arquitetura.representation.Package newComp = arch.createPackage("Package" + OPLA.contComp_ + getSuffix(sourceComp));
                            OPLA.contComp_++;
                            Interface newInterface = newComp.createInterface("Interface" + OPLA.contInt_++);

                            sourceInterface.moveOperationToInterface(op, newInterface);

                            for (Element implementor : sourceInterface.getImplementors()) {
                                if (implementor instanceof arquitetura.representation.Package) {
                                    arch.addImplementedInterface(newInterface, (arquitetura.representation.Package) implementor);
                                }
                                if (implementor instanceof arquitetura.representation.Class) {
                                    arch.addImplementedInterface(newInterface, (arquitetura.representation.Class) implementor);
                                }
                            }
                            for (Concern con : op.getOwnConcerns()) {
                                newInterface.addConcern(con.getName());
                            }
                        }
                        OpsInterface.clear();
                    }
                } else {
                    Configuration.logger_.log(
                            Level.SEVERE, "AddManagerClassMutation.doMutation: invalid type. "
                                    + "{0}", solution.getDecisionVariables()[0].getVariableType());
                    java.lang.Class<String> cls = java.lang.String.class;
                    String name = cls.getName();
                    throw new JMException("Exception in " + name + ".doMutation()");
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //--------------------------------------------------------------------------
    public boolean FeatureMutation(double probability, Solution solution, String scope) throws JMException {
        try {
            if (PseudoRandom.randDouble() < probability) {
                if (solution.getDecisionVariables()[0].getVariableType().toString().equals("class " + Architecture.ARCHITECTURE_TYPE)) {

                    final Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
                    final List<arquitetura.representation.Package> allComponents = new ArrayList<arquitetura.representation.Package>(arch.getAllPackages());
                    if (!allComponents.isEmpty()) {
                        final arquitetura.representation.Package selectedComp = randomObject(allComponents);
                        List<Concern> concernsSelectedComp = new ArrayList<Concern>(selectedComp.getAllConcerns());
                        if (concernsSelectedComp.size() > 1) { // = somente para testes
                            final Concern selectedConcern = randomObject(concernsSelectedComp);
                            List<arquitetura.representation.Package> allComponentsAssignedOnlyToConcern = new ArrayList<arquitetura.representation.Package>(searchComponentsAssignedToConcern(selectedConcern, allComponents));
                            if (allComponentsAssignedOnlyToConcern.isEmpty()) {
                                OPLA.contComp_++;
                                modularizeConcernInComponent(allComponents, arch.createPackage("Package" + OPLA.contComp_ + getSuffix(selectedComp)), selectedConcern, arch);
                            } else {
                                if (allComponentsAssignedOnlyToConcern.size() == 1) {
                                    modularizeConcernInComponent(allComponents, allComponentsAssignedOnlyToConcern.get(0), selectedConcern, arch);
                                } else {
                                    modularizeConcernInComponent(allComponents, randomObject(allComponentsAssignedOnlyToConcern), selectedConcern, arch);
                                }
                            }
                            allComponentsAssignedOnlyToConcern.clear();
                        }
                        concernsSelectedComp.clear();
                        allComponents.clear();
                    }

                } else {
                    Configuration.logger_.log(Level.SEVERE, "FeatureMutation.doMutation: invalid type. " + "{0}", solution.getDecisionVariables()[0].getVariableType());
                    java.lang.Class<String> cls = java.lang.String.class;
                    String name = cls.getName();
                    throw new JMException("Exception in " + name + ".doMutation()");
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private List<arquitetura.representation.Package> searchComponentsAssignedToConcern(Concern concern, List<arquitetura.representation.Package> allComponents) {
        final List<arquitetura.representation.Package> allComponentsAssignedToConcern = new ArrayList<arquitetura.representation.Package>();
        for (arquitetura.representation.Package component : allComponents) {
            final Set<Concern> numberOfConcernsForPackage = getNumberOfConcernsFor(component);
            if (numberOfConcernsForPackage.size() == 1 && (numberOfConcernsForPackage.contains(concern))) {
                allComponentsAssignedToConcern.add(component);
            }
        }
        return allComponentsAssignedToConcern;
    }

    private Set<Concern> getNumberOfConcernsFor(arquitetura.representation.Package pkg) {
        final Set<Concern> listOfOwnedConcern = new HashSet<Concern>();

        for (arquitetura.representation.Class klass : pkg.getAllClasses()) {
            listOfOwnedConcern.addAll(klass.getOwnConcerns());
        }
        for (Interface inte : pkg.getAllInterfaces()) {
            listOfOwnedConcern.addAll(inte.getOwnConcerns());
        }

        return listOfOwnedConcern;
    }

    private void modularizeConcernInComponent(List<arquitetura.representation.Package> allComponents, arquitetura.representation.Package targetComponent, Concern concern, Architecture arch) {
        try {
            Iterator<arquitetura.representation.Package> itrComp = allComponents.iterator();
            while (itrComp.hasNext()) {
                arquitetura.representation.Package comp = itrComp.next();
                if (!comp.equals(targetComponent) && checkSameLayer(comp, targetComponent)) {
                    final Set<Interface> allInterfaces = new HashSet<Interface>(comp.getAllInterfaces());
                    allInterfaces.addAll(comp.getImplementedInterfaces());

                    Iterator<Interface> itrInterface = allInterfaces.iterator();
                    while (itrInterface.hasNext()) {
                        Interface interfaceComp = itrInterface.next();
                        if (interfaceComp.getOwnConcerns().size() == 1 && interfaceComp.containsConcern(concern)) {
                            moveInterfaceToComponent(interfaceComp, targetComponent, comp, arch, concern); // EDIPO TESTADO
                        } else if (!interfaceComp.getPatternsOperations().hasPatternApplied()) {
                            List<Method> operationsInterfaceComp = new ArrayList<Method>(interfaceComp.getOperations());
                            Iterator<Method> itrOperation = operationsInterfaceComp.iterator();
                            while (itrOperation.hasNext()) {
                                Method operation = itrOperation.next();
                                if (operation.getOwnConcerns().size() == 1 && operation.containsConcern(concern)) {
                                    moveOperationToComponent(operation, interfaceComp, targetComponent, comp, arch, concern);
                                }
                            }
                        }
                    }

                    allInterfaces.clear();
                    final List<arquitetura.representation.Class> allClasses = new ArrayList<arquitetura.representation.Class>(comp.getAllClasses());
                    Iterator<arquitetura.representation.Class> ItrClass = allClasses.iterator();
                    while (ItrClass.hasNext()) {
                        arquitetura.representation.Class classComp = ItrClass.next();
                        if (comp.getAllClasses().contains(classComp)) {
                            if ((classComp.getOwnConcerns().size() == 1) && (classComp.containsConcern(concern))) {
                                if (!searchForGeneralizations(classComp)) //realiza a muta����o em classe que n��oo est��o numa hierarquia de heran��a
                                {
                                    moveClassToComponent(classComp, targetComponent, comp, arch, concern);
                                } else {
                                    moveHierarchyToComponent(classComp, targetComponent, comp, arch, concern); //realiza a muta����o em classes est��o numa hierarquia de herarquia
                                }
                            } else {
                                if (!searchForGeneralizations(classComp)) {
                                    if (!isVarPointOfConcern(arch, classComp, concern) && !isVariantOfConcern(arch, classComp, concern)) {
                                        final List<Attribute> attributesClassComp = new ArrayList<Attribute>(classComp.getAllAttributes());
                                        Iterator<Attribute> irtAttribute = attributesClassComp.iterator();
                                        while (irtAttribute.hasNext()) {
                                            Attribute attribute = irtAttribute.next();
                                            if (attribute.getOwnConcerns().size() == 1 && attribute.containsConcern(concern)) {
                                                moveAttributeToComponent(attribute, classComp, targetComponent, comp, arch, concern);
                                            }
                                        }
                                        attributesClassComp.clear();
                                        if (!classComp.getPatternsOperations().hasPatternApplied()) {
                                            final List<Method> methodsClassComp = new ArrayList<Method>(classComp.getAllMethods());
                                            Iterator<Method> irtMethod = methodsClassComp.iterator();
                                            while (irtMethod.hasNext()) {
                                                Method method = irtMethod.next();
                                                if (method.getOwnConcerns().size() == 1 && method.containsConcern(concern)) {
                                                    moveMethodToComponent(method, classComp, targetComponent, comp, arch, concern);
                                                }
                                            }
                                            methodsClassComp.clear();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    allClasses.clear();
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void moveClassToComponent(arquitetura.representation.Class classComp, arquitetura.representation.Package targetComp, arquitetura.representation.Package sourceComp, Architecture architecture, Concern concern) {
        sourceComp.moveClassToPackage(classComp, targetComp);
    }

    private void moveAttributeToComponent(Attribute attribute, arquitetura.representation.Class classComp, arquitetura.representation.Package targetComp, arquitetura.representation.Package sourceComp, Architecture architecture, Concern concern) throws ConcernNotFoundException {
        final arquitetura.representation.Class targetClass = findOrCreateClassWithConcern(targetComp, concern);
        classComp.moveAttributeToClass(attribute, targetClass);
        createAssociation(architecture, targetClass, classComp);
    }

    private void moveMethodToComponent(Method method, arquitetura.representation.Class classComp, arquitetura.representation.Package targetComp, arquitetura.representation.Package sourceComp, Architecture architecture, Concern concern) throws ConcernNotFoundException {
        final arquitetura.representation.Class targetClass = findOrCreateClassWithConcern(targetComp, concern);
        classComp.moveMethodToClass(method, targetClass);
        createAssociation(architecture, targetClass, classComp);
    }

    //add por Édipo
    private arquitetura.representation.Class findOrCreateClassWithConcern(arquitetura.representation.Package targetComp, Concern concern) throws ConcernNotFoundException {
        arquitetura.representation.Class targetClass = null;
        for (arquitetura.representation.Class cls : targetComp.getAllClasses()) {
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

    private void moveInterfaceToComponent(Interface interfaceComp, arquitetura.representation.Package targetComp, arquitetura.representation.Package sourceComp, Architecture architecture, Concern concernSelected) {
        if (!sourceComp.moveInterfaceToPackage(interfaceComp, targetComp)) {
            architecture.moveElementToPackage(interfaceComp, targetComp);
        }

        for (Element implementor : interfaceComp.getImplementors()) {
            if (implementor instanceof arquitetura.representation.Package) {
                if (targetComp.getAllClasses().size() == 1) {
                    final arquitetura.representation.Class klass = targetComp.getAllClasses().iterator().next();
                    for (Concern concern : klass.getOwnConcerns()) {
                        if (interfaceComp.containsConcern(concern)) {
                            architecture.removeImplementedInterface(interfaceComp, sourceComp);
                            addExternalInterface(targetComp, architecture, interfaceComp);
                            addImplementedInterface(targetComp, architecture, interfaceComp, klass);
                        }
                    }
                    return;
                } else if (targetComp.getAllClasses().size() > 1) {
                    final List<arquitetura.representation.Class> targetClasses = allClassesWithConcerns(concernSelected, targetComp.getAllClasses());
                    final arquitetura.representation.Class klass = randonClass(targetClasses);
                    architecture.removeImplementedInterface(interfaceComp, sourceComp);
                    addExternalInterface(targetComp, architecture, interfaceComp);
                    addImplementedInterface(targetComp, architecture, interfaceComp, klass);
                    return;
                } else {
                    //Busca na arquitetura como um todo
                    final List<arquitetura.representation.Class> targetClasses = allClassesWithConcerns(concernSelected, architecture.getAllClasses());
                    final arquitetura.representation.Class klass = randonClass(targetClasses);
                    architecture.removeImplementedInterface(interfaceComp, sourceComp);
                    addExternalInterface(targetComp, architecture, interfaceComp);
                    addImplementedInterface(targetComp, architecture, interfaceComp, klass);
                }
            }
        }
    }

    private arquitetura.representation.Class randonClass(List<arquitetura.representation.Class> targetClasses) {
        Collections.shuffle(targetClasses);
        arquitetura.representation.Class randonKlass = targetClasses.get(0);
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
    private List<arquitetura.representation.Class> allClassesWithConcerns(Concern c, Set<arquitetura.representation.Class> allClasses) {
        List<arquitetura.representation.Class> klasses = new ArrayList<arquitetura.representation.Class>();
        for (arquitetura.representation.Class klass : allClasses) {
            for (Concern concernKlass : klass.getOwnConcerns()) {
                if (concernKlass.getName().equalsIgnoreCase(c.getName())) {
                    klasses.add(klass);
                }
            }
        }
        return klasses;
    }

    private void moveOperationToComponent(Method operation, Interface sourceInterface, arquitetura.representation.Package targetComp, arquitetura.representation.Package sourceComp, Architecture architecture, Concern concern) throws ConcernNotFoundException {
        Interface targetInterface = null;
        targetInterface = searchForInterfaceWithConcern(concern, targetComp);

        if (targetInterface == null) {
            targetInterface = targetComp.createInterface("Interface" + OPLA.contInt_++);
            sourceInterface.moveOperationToInterface(operation, targetInterface);
            targetInterface.addConcern(concern.getName());
        } else {
            sourceInterface.moveOperationToInterface(operation, targetInterface);
        }

        addRelationship(sourceInterface, targetComp, sourceComp, architecture, concern, targetInterface);
    }

    private void addRelationship(Interface sourceInterface, arquitetura.representation.Package targetComp, arquitetura.representation.Package sourceComp, Architecture architecture, Concern concern, Interface targetInterface) {
        for (Element implementor : sourceInterface.getImplementors()) {
            // Se quem estiver implementando a interface que teve a operacao movida for um pacote.
            if (implementor instanceof arquitetura.representation.Package) {
                /**
                 * Verifica se o pacote tem somente um classe, recupera a mesma
                 * e verifica se a interface destino (targetInterface) possui
                 * algum interesse da classe recuperada. Caso tiver, remove
                 * implemented interface (sourceInterface) de sourceComp.
                 * Adiciona a interface tergetInterface em seu pacote ou na
                 * arquitetura Verifica se já existe um relacionamento de
                 * realização entre targetInterface e klass, caso não tiver
                 * adiciona targetInterface como sendo implemenda por klass.
                 */
                if (targetComp.getAllClasses().size() == 1) {
                    final arquitetura.representation.Class klass = targetComp.getAllClasses().iterator().next();
                    for (Concern c : klass.getOwnConcerns()) {
                        if (targetInterface.containsConcern(c)) {
                            architecture.removeImplementedInterface(sourceInterface, sourceComp);
                            addExternalInterface(targetComp, architecture, targetInterface);
                            addImplementedInterface(targetComp, architecture, targetInterface, klass);
                            return;
                        }
                    }

                    /**
                     * Caso o pacote destino tiver mais de uma classe. Busca
                     * dentre essas classes todas com o interesse em questão
                     * (concern), e seleciona um aleatoriamente. Remove
                     * implemented interface (sourceInterface) de sourceComp.
                     * Adiciona a interface tergetInterface em seu pacote ou na
                     * arquitetura Verifica se já existe um relacionamento de
                     * realização entre targetInterface e klass, caso não tiver
                     * adiciona targetInterface como sendo implemenda por klass.
                     */
                } else if (targetComp.getAllClasses().size() > 1) {
                    final List<arquitetura.representation.Class> targetClasses = allClassesWithConcerns(concern, targetComp.getAllClasses());
                    final arquitetura.representation.Class klass = randonClass(targetClasses);
                    architecture.removeImplementedInterface(sourceInterface, sourceComp);
                    addExternalInterface(targetComp, architecture, targetInterface);
                    addImplementedInterface(targetComp, architecture, targetInterface, klass);
                    return;
                } else {
                    /**
                     * Caso o pacote for vazio, faz um busca nas classes da
                     * arquitetura como um todo.
                     */
                    final List<arquitetura.representation.Class> targetClasses = allClassesWithConcerns(concern, architecture.getAllClasses());
                    final arquitetura.representation.Class klass = randonClass(targetClasses);
                    if (klass != null) {
                        architecture.removeImplementedInterface(sourceInterface, sourceComp);
                        addExternalInterface(targetComp, architecture, targetInterface);
                        addImplementedInterface(targetComp, architecture, targetInterface, klass);
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
            if (implementor instanceof arquitetura.representation.Class) {
                architecture.removeImplementedInterface(sourceInterface, sourceComp);
                addExternalInterface(targetComp, architecture, targetInterface);
                addImplementedInterface(targetComp, architecture, targetInterface, (arquitetura.representation.Class) implementor);
            }
        }
    }

    private void addImplementedInterface(arquitetura.representation.Package targetComp, Architecture architecture, Interface targetInterface, arquitetura.representation.Class klass) {
        if (!packageTargetHasRealization(targetComp, targetInterface)) {
            architecture.addImplementedInterface(targetInterface, klass);
        }
    }

    private void addExternalInterface(arquitetura.representation.Package targetComp, Architecture architecture, Interface targetInterface) {
        final String packageNameInterface = UtilResources.extractPackageName(targetInterface.getNamespace().trim());
        if (packageNameInterface.equalsIgnoreCase("model")) {
            architecture.addExternalInterface(targetInterface);
        } else {
            targetComp.addExternalInterface(targetInterface);
        }
    }

    private boolean packageTargetHasRealization(arquitetura.representation.Package targetComp, Interface targetInterface) {
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

    //	//Édipo
    //	private void addConcernToNewInterface(Concern concern, Interface targetInterface, Interface sourceInterface) {
    //		Set<Concern> interfaceConcerns = sourceInterface.getOwnConcerns();
    //		try {
    //			for(Concern c : interfaceConcerns)
    //				targetInterface.addConcern(c.getName());
    //		} catch (ConcernNotFoundException e) {
    //			e.printStackTrace();
    //		}
    //
    //		for(Method operation : sourceInterface.getOperations()){
    //			Set<Concern> operationConcerns = operation.getOwnConcerns();
    //			for(Method o : targetInterface.getOperations()){
    //				for(Concern c : operationConcerns){
    //					try {
    //						o.addConcern(c.getName());
    //					} catch (ConcernNotFoundException e) {
    //						e.printStackTrace();
    //					}
    //				}
    //			}
    //
    //		}
    //	}
    //Édipo Método
    private Interface searchForInterfaceWithConcern(Concern concern, arquitetura.representation.Package targetComp) {
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

    //-------------------------------------------------------------------------------------------------
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

    //-------------------------------------------------------------------------------------------------
    //Thelma: método adicionado para verificar se os componentes nos quais as mutacoes serao realizadas estao na mesma camada da arquitetura
    private boolean checkSameLayer(arquitetura.representation.Package source, arquitetura.representation.Package target) {
        boolean sameLayer = false;
        if ((source.getName().endsWith("Mgr") && target.getName().endsWith("Mgr"))
                || (source.getName().endsWith("Ctrl") && target.getName().endsWith("Ctrl"))
                || (source.getName().endsWith("GUI") && target.getName().endsWith("GUI"))) {
            sameLayer = true;
        }
        return sameLayer;
    }

    //-------------------------------------------------------------------------------------------------
    //Thelma: método adicionado para retornar o sufixo do nome do componente
    private String getSuffix(arquitetura.representation.Package comp) {
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

    //-------------------------------------------------------------------------------------------------
    //Thelma: método adicionado para verificar se a classe tem uma variabilidade relativa ao concern
    private boolean isVarPointOfConcern(Architecture arch, arquitetura.representation.Class cls, Concern concern) {
        boolean isVariationPointConcern = false;
        Collection<Variability> variabilities = arch.getAllVariabilities();
        for (Variability variability : variabilities) {
            VariationPoint varPoint = variability.getVariationPoint();
            if (varPoint != null) {
                arquitetura.representation.Class classVP = (arquitetura.representation.Class) varPoint.getVariationPointElement();
                if (classVP.equals(cls) && variability.getName().equals(concern.getName())) {
                    isVariationPointConcern = true;
                }
            }
        }
        return isVariationPointConcern;
    }

    //-------------------------------------------------------------------------------------------------
    //Thelma: método adicionado para verificar se a classe é variant de uma variabilidade relativa ao concern
    private boolean isVariantOfConcern(Architecture arch, arquitetura.representation.Class cls, Concern concern) {
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
     * @param classComp    - Classe selecionada
     * @param targetComp   - Pacote destino
     * @param sourceComp   - Pacote de origem
     * @param architecture - arquiteutra
     * @param concern      - interesse sendo modularizado
     */
    private void moveHierarchyToComponent(arquitetura.representation.Class classComp, arquitetura.representation.Package targetComp, arquitetura.representation.Package sourceComp, Architecture architecture, Concern concern) {
        architecture.forGeneralization().moveGeneralizationToPackage(getGeneralizationRelationshipForClass(classComp), targetComp);
    }

    //EDIPO Identifica quem é o parent para a classComp

    /**
     * Dado um {@link Element} retorna a {@link GeneralizationRelationship} no
     * qual o mesmo pertence.
     *
     * @param element
     * @return {@link GeneralizationRelationship}
     */
    private GeneralizationRelationship getGeneralizationRelationshipForClass(Element element) {
        for (Relationship r : ((arquitetura.representation.Class) element).getRelationships()) {
            if (r instanceof GeneralizationRelationship) {
                GeneralizationRelationship g = (GeneralizationRelationship) r;
                if (g.getParent().equals(element) || (g.getChild().equals(element))) {
                    return g;
                }
            }
        }
        return null;
    }

    // verificar se a classe é variant de uma variabilidade
    private boolean isOptional(Architecture arch, arquitetura.representation.Class cls) {
        boolean isOptional = false;
        if (cls.getVariantType() != null) {
            if (cls.getVariantType().toString().equalsIgnoreCase("optional")) {
                return true;
            }
        }
        return isOptional;
    }
    //-------------------------------------------------------------------------------------------------
    // verificar se a classe é variant de uma variabilidade

    private boolean isVariant(Architecture arch, arquitetura.representation.Class cls) {
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

    private boolean isVarPoint(Architecture arch, arquitetura.representation.Class cls) {
        boolean isVariationPoint = false;
        Collection<Variability> variabilities = arch.getAllVariabilities();
        for (Variability variability : variabilities) {
            VariationPoint varPoint = variability.getVariationPoint();
            if (varPoint != null) {
                arquitetura.representation.Class classVP = (arquitetura.representation.Class) varPoint.getVariationPointElement();
                if (classVP.equals(cls)) {
                    isVariationPoint = true;
                }
            }
        }
        return isVariationPoint;
    }

}
