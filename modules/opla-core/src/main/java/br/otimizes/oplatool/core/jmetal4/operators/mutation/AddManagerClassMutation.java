package br.otimizes.oplatool.core.jmetal4.operators.mutation;

import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.common.Configuration;
import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.operators.IOperator;
import br.otimizes.oplatool.core.jmetal4.problems.OPLA;
import br.otimizes.oplatool.core.jmetal4.util.PseudoRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Mutation operator that add manager classes
 */
public class AddManagerClassMutation implements IOperator<Solution> {

    @Override
    public Solution execute(Map<String, Object> parameters, Solution solution, String scope) {
        try {
            if (PseudoRandom.randDouble() < ((Double) parameters.get("probability"))) {
                if (solution.getDecisionVariables()[0].getVariableType() == java.lang.Class
                        .forName(Architecture.ARCHITECTURE_TYPE)) {
                    Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);

                    Package sourceComp = MutationUtils.getRandomPackage(arch);

                    List<Interface> InterfacesComp = new ArrayList<Interface>();
                    InterfacesComp.addAll(sourceComp.getImplementedInterfaces().stream().filter(i -> !i.isTotalyFreezed()).collect(Collectors.toList()));

                    MutationUtils.removeInterfacesInPatternStructureFromArray(InterfacesComp);

                    if (InterfacesComp.size() >= 1) {
                        Interface sourceInterface = MutationUtils.getRandomInterface(InterfacesComp);
                        if (MutationUtils.searchPatternsInterface(sourceInterface)) {
                            List<Method> OpsInterface = new ArrayList<Method>();
                            OpsInterface.addAll(sourceInterface.getMethods().stream().filter(c -> !c.isTotalyFreezed()).collect(Collectors.toList()));
                            if (OpsInterface.size() >= 1) {
                                Method op = MutationUtils.getRandomMethod(OpsInterface);

                                Package newComp = arch
                                        .createPackage("Package" + OPLA.countPackage + MutationUtils.getSuffix(sourceComp));
                                OPLA.countPackage++;
                                Interface newInterface = newComp.createInterface("Interface" + OPLA.countInterface++);

                                sourceInterface.moveMethodToInterface(op, newInterface);

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
                        }
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
        return solution;
    }

}
