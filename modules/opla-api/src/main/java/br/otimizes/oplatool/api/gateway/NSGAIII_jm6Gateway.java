package br.otimizes.oplatool.api.gateway;

import br.otimizes.oplatool.api.dto.OptimizationDto;
import br.otimizes.oplatool.core.jmetal4.experiments.base.NSGAIII_jm6Configs;
import br.otimizes.oplatool.core.jmetal4.experiments.base.NSGAIII_jm6OPLABase;

import org.springframework.stereotype.Component;

@Component
public class NSGAIII_jm6Gateway implements IGateway {
    private final NSGAIII_jm6OPLABase base;

    public NSGAIII_jm6Gateway(NSGAIII_jm6OPLABase base) {
        this.base = base;
    }

    public void execute(OptimizationDto optimizationDto) {
        GatewayUtils.addListener();
        NSGAIII_jm6Configs configs = new NSGAIII_jm6Configs();
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
