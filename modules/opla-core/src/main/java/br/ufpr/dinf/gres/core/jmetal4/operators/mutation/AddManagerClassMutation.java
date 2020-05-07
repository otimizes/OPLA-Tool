package br.ufpr.dinf.gres.core.jmetal4.operators.mutation;

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

public class AddManagerClassMutation implements IMutationOperator {

    @Override
    public void execute(double probability, Solution solution, String scope) {
        try {
            if (PseudoRandom.randDouble() < probability) {
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
                                        .createPackage("Package" + OPLA.contComp_ + MutationUtils.getSuffix(sourceComp));
                                OPLA.contComp_++;
                                Interface newInterface = newComp.createInterface("Interface" + OPLA.contInt_++);

                                sourceInterface.moveOperationToInterface(op, newInterface);

                                for (Element implementor : sourceInterface.getImplementors()) {
                                    if (implementor instanceof Package) {
                                        arch.addImplementedInterface(newInterface, (Package) implementor);
                                    }
                                    if (implementor instanceof br.ufpr.dinf.gres.architecture.representation.Class) {
                                        arch.addImplementedInterface(newInterface, (br.ufpr.dinf.gres.architecture.representation.Class) implementor);
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
    }

}
