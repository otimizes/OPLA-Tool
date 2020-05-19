package br.ufpr.dinf.gres.core.jmetal4.operators;

import br.ufpr.dinf.gres.core.jmetal4.operators.crossover.PLAComplementaryCrossover;
import br.ufpr.dinf.gres.core.jmetal4.operators.crossover.PLACrossover;

/**
 * Feature crossover operators enum
 * <p>
 * {@link PLACrossover PLA Crossover,}
 * {@link PLAComplementaryCrossover PLA Complementary Crossover}
 */
public enum CrossoverOperators implements IOperators {

    PLA_CROSSOVER {
        @Override
        public IOperator getOperator() {
            return new PLACrossover();
        }
    },
    PLA_COMPLEMENTARY_CROSSOVER {
        @Override
        public IOperator getOperator() {
            return new PLAComplementaryCrossover();
        }
    }
}
