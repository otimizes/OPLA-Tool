package br.ufpr.dinf.gres.core.jmetal4.operators.mutation;

import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.architecture.representation.*;
import br.ufpr.dinf.gres.common.Configuration;
import br.ufpr.dinf.gres.common.exceptions.JMException;
import br.ufpr.dinf.gres.core.jmetal4.core.Solution;
import br.ufpr.dinf.gres.core.jmetal4.util.PseudoRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class MoveOperationMutation implements IMutationOperator {

    @Override
    public void execute(double probability, Solution solution, String scope) {
        try {
            if (PseudoRandom.randDouble() < probability) {
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
                                        sourceInterface.moveOperationToInterface(MutationUtils.getRandomMethod(OpsInterface),
                                                targetInterface);
                                        for (Element implementor : sourceInterface.getImplementors()) {
                                            if (implementor instanceof Package) {
                                                arch.addImplementedInterface(targetInterface, (Package) implementor);
                                            }
                                            if (implementor instanceof br.ufpr.dinf.gres.architecture.representation.Class) {
                                                arch.addImplementedInterface(targetInterface, (br.ufpr.dinf.gres.architecture.representation.Class) implementor);
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
    }

}
