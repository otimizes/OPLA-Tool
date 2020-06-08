package br.otimizes.oplatool.api.gateway;

import br.otimizes.oplatool.api.dto.OptimizationDto;
import br.otimizes.oplatool.core.jmetal4.experiments.base.PAESOPLABase;
import br.otimizes.oplatool.core.jmetal4.experiments.base.PAESConfigs;
import org.springframework.stereotype.Component;

@Component
public class PAESGateway implements IGateway {
    private final PAESOPLABase base;

    public PAESGateway(PAESOPLABase base) {
        this.base = base;
    }

    public void execute(OptimizationDto optimizationDto) {
        GatewayUtils.addListener();
        PAESConfigs configs = new PAESConfigs();
        configs.setArchiveSize(optimizationDto.getArchiveSize());
        GatewayUtils.setConfigs(optimizationDto, configs);
        try {
            base.execute(configs);
        } catch (Exception e) {
            GatewayUtils.log("ERROR");
        }
        GatewayUtils.log("Fin");
    }



}
