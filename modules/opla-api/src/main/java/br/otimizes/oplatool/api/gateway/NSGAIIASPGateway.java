package br.otimizes.oplatool.api.gateway;

import br.otimizes.oplatool.api.dto.OptimizationDto;
import br.otimizes.oplatool.core.jmetal4.experiments.base.NSGAIIAPSOPLABase;
import br.otimizes.oplatool.core.jmetal4.experiments.base.NSGAIIConfigs;
import org.springframework.stereotype.Component;

@Component
public class NSGAIIASPGateway implements IGateway {
    private final NSGAIIAPSOPLABase base;

    public NSGAIIASPGateway(NSGAIIAPSOPLABase base) {
        this.base = base;
    }

    public void execute(OptimizationDto optimizationDto) {
        GatewayUtils.addListener();
        NSGAIIConfigs configs = new NSGAIIConfigs();
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
