package br.otimizes.oplatool.api.gateway;

import br.otimizes.oplatool.api.dto.OptimizationDto;
import br.otimizes.oplatool.core.jmetal4.experiments.base.NSGAIIIConfigs;
import br.otimizes.oplatool.core.jmetal4.experiments.base.NSGAIIIOPLABase;

import org.springframework.stereotype.Component;

@Component
public class NSGAIIIGateway implements IGateway {
    private final NSGAIIIOPLABase base;

    public NSGAIIIGateway(NSGAIIIOPLABase base) {
        this.base = base;
    }

    public void execute(OptimizationDto optimizationDto) {
        GatewayUtils.addListener();
        NSGAIIIConfigs configs = new NSGAIIIConfigs();
        GatewayUtils.setConfigs(optimizationDto, configs);
        configs.setPopulationSize(optimizationDto.getPopulationSize());
        try {
            base.execute(configs);
        } catch (Exception e) {
            e.printStackTrace();
            GatewayUtils.log("ERROR");
        }
        GatewayUtils.log("Fin");
    }
}
