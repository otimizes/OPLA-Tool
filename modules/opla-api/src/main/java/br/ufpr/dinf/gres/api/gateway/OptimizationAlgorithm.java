package br.ufpr.dinf.gres.api.gateway;

public enum OptimizationAlgorithm implements IOptimizationAlgorithm {
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
    };
}
