package br.ufpr.dinf.gres.core.jmetal4.operators.mutation;

import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.architecture.representation.*;
import br.ufpr.dinf.gres.common.Configuration;
import br.ufpr.dinf.gres.common.exceptions.JMException;
import br.ufpr.dinf.gres.core.jmetal4.core.Solution;
import br.ufpr.dinf.gres.core.jmetal4.problems.OPLA;
import br.ufpr.dinf.gres.core.jmetal4.util.PseudoRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Mutation operator that add classes
 */
public class AddClassMutation implements IMutationOperator {

    @Override
    public void execute(double probability, Solution solution, String scope) {
        try {
            if (PseudoRandom.randDouble() < probability) {
                if (solution.getDecisionVariables()[0].getVariableType() == java.lang.Class
                        .forName(Architecture.ARCHITECTURE_TYPE)) {
                    Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);

                    Package sourceComp = MutationUtils.getRandomPackage(arch);
                    List<Class> classesPackage = new ArrayList<Class>(sourceComp.getAllClasses().stream().filter(c -> !c.isTotalyFreezed()).collect(Collectors.toList()));
                    MutationUtils.removeClassesInPatternStructureFromArray(classesPackage);
                    if (classesPackage.size() >= 1) {
                        Class sourceClassElem = MutationUtils.getRandomClass(classesPackage);

                        final Class sourceClass = sourceClassElem;
                        if ((sourceClass != null) && (!MutationUtils.searchForGeneralizations(sourceClass))
                                && (sourceClass.getAllAttributes().size() > 1)
                                && (sourceClass.getAllMethods().size() > 1) && (!MutationUtils.isVariationPoint(arch, sourceClass))
                                && (!MutationUtils.isVariant(arch, sourceClass)) && (!MutationUtils.isOptional(sourceClass))) {
                            if (MutationUtils.searchPatternsClass(sourceClass)) {
                                if (PseudoRandom.randInt(0, 1) == 0) { // attribute
                                    List<Attribute> AttributesClass = new ArrayList<Attribute>(
                                            sourceClass.getAllAttributes().stream().filter(c -> !c.isTotalyFreezed()).collect(Collectors.toList()));
                                    if (AttributesClass.size() >= 1) {
                                        if ("sameComponent".equals(scope)) {
                                            MutationUtils.moveAttributeToNewClass(arch, sourceClass, AttributesClass,
                                                    sourceComp.createClass("Class" + OPLA.contClass_++, false));
                                        } else {
                                            if ("allComponents".equals(scope)) {
                                                Package targetComp = MutationUtils.getRandomPackage(arch);
                                                if (MutationUtils.checkSameLayer(sourceComp, targetComp)) {
                                                    MutationUtils.moveAttributeToNewClass(arch, sourceClass, AttributesClass,
                                                            targetComp.createClass("Class" + OPLA.contClass_++, false));
                                                }
                                            }
                                        }
                                        AttributesClass.clear();
                                    }
                                }
                            } else {
                                if (MutationUtils.searchPatternsClass(sourceClass)) {
                                    List<Method> MethodsClass = new ArrayList<Method>(sourceClass.getAllMethods().stream().filter(c -> !c.isTotalyFreezed()).collect(Collectors.toList()));
                                    if (MethodsClass.size() >= 1) {
                                        if ("sameComponent".equals(scope)) {
                                            MutationUtils.moveMethodToNewClass(arch, sourceClass, MethodsClass,
                                                    sourceComp.createClass("Class" + OPLA.contClass_++, false));
                                        } else {
                                            if ("allComponents".equals(scope)) {
                                                Package targetComp = MutationUtils.getRandomPackage(arch);
                                                if (MutationUtils.checkSameLayer(sourceComp, targetComp)) {
                                                    MutationUtils.moveMethodToNewClass(arch, sourceClass, MethodsClass,
                                                            targetComp.createClass("Class" + OPLA.contClass_++, false));
                                                }
                                            }
                                        }
                                        MethodsClass.clear();
                                    }
                                }
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

}
