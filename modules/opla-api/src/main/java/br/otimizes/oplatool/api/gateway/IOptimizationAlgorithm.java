package br.otimizes.oplatool.api.gateway;

public interface IOptimizationAlgorithm {
    Class<? extends IGateway> getType();
}
