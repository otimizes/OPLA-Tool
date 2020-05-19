package br.ufpr.dinf.gres.api.gateway;

public interface IOptimizationAlgorithm {
    Class<? extends IGateway> getType();
}
