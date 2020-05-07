package br.ufpr.dinf.gres.api.gateway;

import br.ufpr.dinf.gres.api.dto.OptimizationDto;

public interface IGateway {
    void execute(OptimizationDto optimizationDto);

}
