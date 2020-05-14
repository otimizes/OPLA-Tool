package br.ufpr.dinf.gres.api.gateway;

/**
 * Feature mutation operators enum
 * <p>
 * {@link NSGAGateway NSGAII Gateway,}
 * {@link br.ufpr.dinf.gres.core.jmetal4.experiments.base.NSGAIIOPLABase NSGAII OPLABase,}
 * {@link br.ufpr.dinf.gres.core.jmetal4.metaheuristics.nsgaII.NSGAII NSGAII,}
 * {@link PAESGateway PAES Gateway,}
 * {@link br.ufpr.dinf.gres.core.jmetal4.experiments.base.PAESOPLABase PAES OPLABase,}
 * {@link br.ufpr.dinf.gres.core.jmetal4.metaheuristics.paes.PAES PAES,}
 * {@link BestOf2Gateway BestOf2 Gateway,}
 * {@link br.ufpr.dinf.gres.core.jmetal4.experiments.base.BestOf2OPLABase BestOf2 OPLABase,}
 * {@link br.ufpr.dinf.gres.core.jmetal4.metaheuristics.memetic.Bestof2 BestOf2,}
 * {@link BestOf12Gateway BestOf12 Gateway,}
 * {@link br.ufpr.dinf.gres.core.jmetal4.experiments.base.BestOf12OPLABase BestOf12 OPLABase,}
 * {@link br.ufpr.dinf.gres.core.jmetal4.metaheuristics.memetic.Bestof12 BestOf12,}
 * {@link NoChoiceGateway NoChoice Gateway,}
 * {@link br.ufpr.dinf.gres.core.jmetal4.experiments.base.NoChoiceOPLABase NoChoice OPLABase,}
 * {@link br.ufpr.dinf.gres.core.jmetal4.metaheuristics.memetic.NoChoice NoChoice,}
 * {@link UntilBestGateway UntilBest Gateway,}
 * {@link br.ufpr.dinf.gres.core.jmetal4.experiments.base.UntilBestOPLABase UntilBest OPLABase,}
 * {@link br.ufpr.dinf.gres.core.jmetal4.metaheuristics.memetic.UntilBest UntilBest}
 */
public enum OptimizationAlgorithms implements IOptimizationAlgorithm {
    NSGAII {
        @Override
        public Class<? extends IGateway> getType() {
            return NSGAGateway.class;
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
}
