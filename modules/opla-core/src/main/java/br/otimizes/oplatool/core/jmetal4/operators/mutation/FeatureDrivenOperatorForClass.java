package br.otimizes.oplatool.core.jmetal4.operators.mutation;

import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.common.Configuration;
import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.operators.IOperator;
import br.otimizes.oplatool.core.jmetal4.util.PseudoRandom;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;

/**
 * Feature-driven Operator for Class
 */
public class FeatureDrivenOperatorForClass implements IOperator<Solution> {

    @Override
    public Solution execute(Map<String, Object> parameters, Solution solution, String scope) {
        try {
            if (PseudoRandom.randDouble() < ((Double) parameters.get("probability"))) {
                if (solution.getDecisionVariables()[0].getVariableType().toString()
                        .equals("class " + Architecture.ARCHITECTURE_TYPE)) {

                    final Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
                    MutationUtils.applyToClass(arch, MutationUtils.randomObject(new ArrayList<>(arch.getAllModifiableClasses())));
                    MutationUtils.applyToInterface(arch, MutationUtils.randomObject(new ArrayList<>(arch.getAllModifiableInterfaces())));
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
        return solution;
    }

}
