package br.otimizes.oplatool.core.jmetal4.operators;

import br.otimizes.oplatool.core.jmetal4.operators.crossover.*;

/**
 * Feature crossover operators enum
 * <p>
 * {@link PLAFeatureDrivenCrossoverOldVersion PLA Crossover,}
 * {@link PLAComplementaryCrossoverOldVersion PLA Complementary Crossover}
 * {@link PLAModularCrossover3 PLA Modular Crossover}
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
    },
    PLA_MODULAR_CROSSOVER {
        @Override
        public IOperator getOperator() {
            return new PLAModularCrossover3();
        }
    }
}
