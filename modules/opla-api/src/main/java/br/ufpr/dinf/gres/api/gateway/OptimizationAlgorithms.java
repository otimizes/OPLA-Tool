package br.ufpr.dinf.gres.api.gateway;

public enum OptimizationAlgorithms implements IOptimizationAlgorithm {
    NSGAII {
        @Override
        public Class getType() {
            return NSGAGateway.class;
        }
    },
    PAES {
        @Override
        public Class getType() {
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
