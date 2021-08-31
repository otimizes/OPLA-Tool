package br.otimizes.oplatool.core.jmetal4.operators.mutation;

import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.common.Configuration;
import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.operators.IOperator;
import br.otimizes.oplatool.core.jmetal4.util.PseudoRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Mutation operator that move operations
 */
public class MoveOperationMutation implements IOperator<Solution> {

    @Override
    public Solution execute(Map<String, Object> parameters, Solution solution, String scope) {
        try {
            if (PseudoRandom.randDouble() < ((Double) parameters.get("probability"))) {
                if (solution.getDecisionVariables()[0].getVariableType() == java.lang.Class
                        .forName(Architecture.ARCHITECTURE_TYPE)) {
                    Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);

                    Package sourceComp = MutationUtils.getRandomPackage(arch);
                    Package targetComp = MutationUtils.getRandomPackage(arch);

                    if (MutationUtils.checkSameLayer(sourceComp, targetComp)) {
                        List<Interface> InterfacesSourceComp = new ArrayList<Interface>();
                        List<Interface> InterfacesTargetComp = new ArrayList<Interface>();

                        InterfacesSourceComp.addAll(sourceComp.getImplementedInterfaces().stream().filter(i -> !i.isTotalyFreezed()).collect(Collectors.toList()));
                        MutationUtils.removeInterfacesInPatternStructureFromArray(InterfacesSourceComp);

                        InterfacesTargetComp.addAll(targetComp.getImplementedInterfaces().stream().filter(i -> !i.isTotalyFreezed()).collect(Collectors.toList()));

                        if ((InterfacesSourceComp.size() >= 1) && (InterfacesTargetComp.size() >= 1)) {
                            Interface targetInterface = MutationUtils.getRandomInterface(InterfacesTargetComp);
                            Interface sourceInterface = MutationUtils.getRandomInterface(InterfacesSourceComp);
                            // joao\
                            if (MutationUtils.searchPatternsInterface(targetInterface) && MutationUtils.searchPatternsInterface(sourceInterface)) {
                                if (targetInterface != sourceInterface) {
                                    List<Method> OpsInterface = new ArrayList<Method>();
                                    OpsInterface.addAll(sourceInterface.getMethods().stream().filter(c -> !c.isTotalyFreezed()).collect(Collectors.toList()));
                                    if (OpsInterface.size() >= 1) {
                                        sourceInterface.moveMethodToInterface(MutationUtils.getRandomMethod(OpsInterface),
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
                            }
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
        return solution;
    }

}
