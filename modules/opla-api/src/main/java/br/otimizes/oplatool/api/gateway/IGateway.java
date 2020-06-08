package br.otimizes.oplatool.api.gateway;

import br.otimizes.oplatool.api.dto.OptimizationDto;

public interface IGateway {
    void execute(OptimizationDto optimizationDto);

}
