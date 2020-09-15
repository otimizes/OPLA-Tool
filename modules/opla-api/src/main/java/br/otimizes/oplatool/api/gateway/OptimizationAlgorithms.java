package br.otimizes.oplatool.api.gateway;

import br.otimizes.oplatool.core.jmetal4.experiments.base.*;
import br.otimizes.oplatool.core.jmetal4.metaheuristics.memetic.Bestof12;
import br.otimizes.oplatool.core.jmetal4.metaheuristics.memetic.Bestof2;
import br.otimizes.oplatool.core.jmetal4.metaheuristics.memetic.NoChoice;
import br.otimizes.oplatool.core.jmetal4.metaheuristics.memetic.UntilBest;

/**
 * Feature mutation operators enum
 * <p>
 * {@link NSGAIIGateway NSGAII Gateway,}
 * {@link NSGAIIOPLABase NSGAII OPLABase,}
 * {@link br.otimizes.oplatool.core.jmetal4.metaheuristics.nsgaII.NSGAII NSGAII,}
 * {@link PAESGateway PAES Gateway,}
 * {@link PAESOPLABase PAES OPLABase,}
 * {@link br.otimizes.oplatool.core.jmetal4.metaheuristics.paes.PAES PAES,}
 * {@link BestOf2Gateway BestOf2 Gateway,}
 * {@link BestOf2OPLABase BestOf2 OPLABase,}
 * {@link Bestof2 BestOf2,}
 * {@link BestOf12Gateway BestOf12 Gateway,}
 * {@link BestOf12OPLABase BestOf12 OPLABase,}
 * {@link Bestof12 BestOf12,}
 * {@link NoChoiceGateway NoChoice Gateway,}
 * {@link NoChoiceOPLABase NoChoice OPLABase,}
 * {@link NoChoice NoChoice,}
 * {@link UntilBestGateway UntilBest Gateway,}
 * {@link UntilBestOPLABase UntilBest OPLABase,}
 * {@link UntilBest UntilBest,}
 * {@link NSGAIIASPGateway NSGAII NSGAIIASPGateway,}
 * {@link NSGAIIOPLABase NSGAII NSGAIIOPLABase,}
 * {@link br.otimizes.oplatool.core.jmetal4.metaheuristics.nsgaII.NSGAIIASP NSGAIIASP,}
 */
public enum OptimizationAlgorithms implements IOptimizationAlgorithm {
    NSGAII {
        @Override
        public Class<? extends IGateway> getType() {
            return NSGAIIGateway.class;
        }
    },
    PAES {
        @Override
        public Class<? extends IGateway> getType() {
            return PAESGateway.class;
        }
    },
    BEST_OF_2 {
        @Override
        public Class<? extends IGateway> getType() {
            return BestOf2Gateway.class;
        }
    },
    BEST_OF_12 {
        @Override
        public Class<? extends IGateway> getType() {
            return BestOf12Gateway.class;
        }
    },
    NO_CHOICE {
        @Override
        public Class<? extends IGateway> getType() {
            return NoChoiceGateway.class;
        }
    },
    UNTIL_BEST {
        @Override
        public Class<? extends IGateway> getType() {
            return UntilBestGateway.class;
        }
    },
    NSGAII_ASP {
        @Override
        public Class<? extends IGateway> getType() {
            return NSGAIIASPGateway.class;
        }
    },
}
