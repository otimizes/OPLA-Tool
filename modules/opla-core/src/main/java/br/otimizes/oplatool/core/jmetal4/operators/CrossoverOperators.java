package br.otimizes.oplatool.core.jmetal4.operators;

import br.otimizes.oplatool.core.jmetal4.operators.crossover.PLAComplementaryCrossover;
import br.otimizes.oplatool.core.jmetal4.operators.crossover.PLAFeatureDrivenCrossover;

/**
 * Feature crossover operators enum
 * <p>
 * {@link PLAFeatureDrivenCrossover PLA Crossover,}
 * {@link PLAComplementaryCrossover PLA Complementary Crossover}
 */
public enum CrossoverOperators implements IOperators {

    PLA_FEATURE_DRIVEN_CROSSOVER {
        @Override
        public IOperator getOperator() {
            return new PLAFeatureDrivenCrossover();
        }
    },
    PLA_COMPLEMENTARY_CROSSOVER {
        @Override
        public IOperator getOperator() {
            return new PLAComplementaryCrossover();
        }
    }
}
