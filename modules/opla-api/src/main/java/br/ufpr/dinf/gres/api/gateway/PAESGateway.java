package br.ufpr.dinf.gres.api.gateway;

import br.ufpr.dinf.gres.api.dto.OptimizationDto;
import br.ufpr.dinf.gres.architecture.io.ReaderConfig;
import br.ufpr.dinf.gres.architecture.util.UserHome;
import br.ufpr.dinf.gres.core.jmetal4.experiments.base.PAESOPLABase;
import br.ufpr.dinf.gres.core.jmetal4.experiments.base.PAESConfigs;
import org.springframework.stereotype.Component;

@Component
public class PAESGateway implements IGateway {
    private final PAESOPLABase base;

    public PAESGateway(PAESOPLABase base) {
        this.base = base;
    }

    public void execute(OptimizationDto optimizationDto) {
        GatewayUtils.addListener();
        ReaderConfig.setPathToConfigurationFile(UserHome.getPathToConfigFile());
        ReaderConfig.load();
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
