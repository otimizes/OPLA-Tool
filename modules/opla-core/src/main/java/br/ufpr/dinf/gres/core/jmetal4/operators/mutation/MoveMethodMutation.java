package br.ufpr.dinf.gres.core.jmetal4.operators.mutation;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.core.jmetal4.core.Solution;
import br.ufpr.dinf.gres.core.jmetal4.util.PseudoRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Mutation operator that move methods
 */
public class MoveMethodMutation implements IOperator<Solution> {

    @Override
    public Solution execute(Map<String, Object> parameters, Solution solution, String scope) {
        if (PseudoRandom.randDouble() < ((Double) parameters.get("probability"))) {
            final Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
            if ("sameComponent".equals(scope)) {
                Package sourceCompElem = MutationUtils.getRandomPackage(arch);
                final Package sourceComp = sourceCompElem;
                List<Class> ClassesComp = new ArrayList<Class>(sourceComp.getAllClasses().stream().filter(c -> !c.isTotalyFreezed()).collect(Collectors.toList()));

                MutationUtils.removeClassesInPatternStructureFromArray(ClassesComp);
                if (ClassesComp.size() > 1) {
                    final Class targetClass = MutationUtils.getRandomClass(ClassesComp);
                    final Class sourceClass = MutationUtils.getRandomClass(ClassesComp);

                    if (MutationUtils.searchPatternsClass(targetClass) && MutationUtils.searchPatternsClass(sourceClass)) {
                        if ((sourceClass != null) && (!MutationUtils.searchForGeneralizations(sourceClass))
                                && (sourceClass.getAllAttributes().size() > 1)
                                && (sourceClass.getAllMethods().size() > 1) && (!MutationUtils.isVariationPoint(arch, sourceClass))
                                && (!MutationUtils.isVariant(arch, sourceClass)) && (!MutationUtils.isOptional(sourceClass))) {
                            if ((targetClass != null) && (!(targetClass.equals(sourceClass)))) {
                                MutationUtils.moveMethod(arch, targetClass, sourceClass);
                            }
                        }
                    }
                }
                ClassesComp.clear();
            } else {
                if ("allComponents".equals(scope)) {
                    final Package sourceComp = MutationUtils.getRandomPackage(arch);
                    final List<Class> ClassesSourceComp = new ArrayList<Class>(sourceComp.getAllClasses().stream().filter(i -> !i.isTotalyFreezed()).collect(Collectors.toList()));
                    MutationUtils.removeClassesInPatternStructureFromArray(ClassesSourceComp);
                    if (ClassesSourceComp.size() >= 1) {
                        final Class sourceClass = MutationUtils.getRandomClass(ClassesSourceComp);
                        if (MutationUtils.searchPatternsClass(sourceClass)) {
                            if ((sourceClass != null) && (!MutationUtils.searchForGeneralizations(sourceClass))
                                    && (sourceClass.getAllAttributes().size() > 1)
                                    && (sourceClass.getAllMethods().size() > 1) && (!MutationUtils.isVariationPoint(arch, sourceClass))
                                    && (!MutationUtils.isVariant(arch, sourceClass)) && (!MutationUtils.isOptional(sourceClass))) {
                                final Package targetComp = MutationUtils.getRandomPackage(arch);
                                if (MutationUtils.checkSameLayer(sourceComp, targetComp)) {
                                    final List<Class> ClassesTargetComp = new ArrayList<Class>(
                                            targetComp.getAllClasses().stream().filter(c -> !c.isTotalyFreezed()).collect(Collectors.toList()));
                                    if (ClassesTargetComp.size() >= 1) {
                                        final Class targetClass = MutationUtils.getRandomClass(ClassesTargetComp);
                                        if ((targetClass != null) && (!(targetClass.equals(sourceClass)))) {
                                            MutationUtils.moveMethod(arch, targetClass, sourceClass);
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
        return solution;
    }
}
