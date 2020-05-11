package br.ufpr.dinf.gres.api.gateway;

import br.ufpr.dinf.gres.api.gateway.IGateway;

public interface IOptimizationAlgorithm {
    Class<? extends IGateway> getType();
}
