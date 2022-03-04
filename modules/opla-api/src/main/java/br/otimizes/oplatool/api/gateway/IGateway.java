package br.otimizes.oplatool.api.gateway;

import br.otimizes.oplatool.api.dto.OptimizationDto;
import br.otimizes.oplatool.core.jmetal4.experiments.ExperimentCommonConfigs;
import br.otimizes.oplatool.core.jmetal4.experiments.base.AlgorithmBase;

public interface IGateway<T extends ExperimentCommonConfigs> extends AlgorithmBase<T> {
    void execute(OptimizationDto optimizationDto);
}
