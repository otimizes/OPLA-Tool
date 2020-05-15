package br.ufpr.dinf.gres.core.jmetal4.operators.mutation;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.common.Configuration;
import br.ufpr.dinf.gres.common.exceptions.JMException;
import br.ufpr.dinf.gres.core.jmetal4.core.Solution;
import br.ufpr.dinf.gres.core.jmetal4.operators.IOperator;
import br.ufpr.dinf.gres.core.jmetal4.util.PseudoRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Mutation operator that move attributes
 */
public class MoveAttributeMutation implements IOperator<Solution> {

    @Override
    public Solution execute(Map<String, Object> parameters, Solution solution, String scope) {
        try {
            if (PseudoRandom.randDouble() < ((Double) parameters.get("probability"))) {
                if (solution.getDecisionVariables()[0].getVariableType() == java.lang.Class
                        .forName(Architecture.ARCHITECTURE_TYPE)) {
                    Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
                    if ("sameComponent".equals(scope)) {
                        List<Class> ClassesComp = new ArrayList<Class>(
                                MutationUtils.randomObject(new ArrayList<Package>(arch.getAllPackages())).getAllClasses().stream().filter(c -> !c.isTotalyFreezed()).collect(Collectors.toList()));
                        if (ClassesComp.size() > 1) {
                            final Class targetClass = MutationUtils.getRandomClass(ClassesComp);
                            final Class sourceClass = MutationUtils.getRandomClass(ClassesComp);
                            if (MutationUtils.searchPatternsClass(targetClass) && MutationUtils.searchPatternsClass(sourceClass)) {
                                if ((sourceClass != null) && (!MutationUtils.searchForGeneralizations(sourceClass))
                                        && (sourceClass.getAllAttributes().size() > 1)
                                        && (sourceClass.getAllMethods().size() > 1) && (!MutationUtils.isVariationPoint(arch, sourceClass))
                                        && (!MutationUtils.isVariant(arch, sourceClass)) && (!MutationUtils.isOptional(sourceClass))) {
                                    if ((targetClass != null) && (!(targetClass.equals(sourceClass)))) {
                                        MutationUtils.moveAttribute(arch, targetClass, sourceClass);
                                    }
                                }
                            }
                        }
                        ClassesComp.clear();
                    } else {
                        if ("allComponents".equals(scope)) {
                            Package sourceComp = MutationUtils.getRandomPackage(arch);
                            List<Class> ClassesSourceComp = new ArrayList<Class>(sourceComp.getAllClasses().stream().filter(c -> !c.isTotalyFreezed()).collect(Collectors.toList()));
                            if (ClassesSourceComp.size() >= 1) {
                                Class sourceClass = MutationUtils.getRandomClass(ClassesSourceComp);
                                if (MutationUtils.searchPatternsClass(sourceClass)) {
                                    if ((sourceClass != null) && (!MutationUtils.searchForGeneralizations(sourceClass))
                                            && (sourceClass.getAllAttributes().size() > 1)
                                            && (sourceClass.getAllMethods().size() > 1)
                                            && (!MutationUtils.isVariationPoint(arch, sourceClass)) && (!MutationUtils.isVariant(arch, sourceClass))
                                            && (!MutationUtils.isOptional(sourceClass))) {
                                        Package targetComp = MutationUtils.getRandomPackage(arch);
                                        if (MutationUtils.checkSameLayer(sourceComp, targetComp)) {
                                            List<Class> ClassesTargetComp = new ArrayList<Class>(
                                                    targetComp.getAllClasses().stream().filter(c -> !c.isTotalyFreezed()).collect(Collectors.toList()));
                                            if (ClassesTargetComp.size() >= 1) {
                                                Class targetClass = MutationUtils.getRandomClass(ClassesTargetComp);
                                                if ((targetClass != null) && (!(targetClass.equals(sourceClass)))) {
                                                    MutationUtils.moveAttribute(arch, targetClass, sourceClass);
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
        return solution;
    }
}
