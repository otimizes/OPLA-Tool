package br.ufpr.dinf.gres.api.gateway;

import br.ufpr.dinf.gres.api.dto.OptimizationDto;
import br.ufpr.dinf.gres.architecture.io.ReaderConfig;
import br.ufpr.dinf.gres.architecture.util.UserHome;
import br.ufpr.dinf.gres.core.jmetal4.experiments.OPLAConfigs;
import br.ufpr.dinf.gres.core.jmetal4.experiments.PAESOPLAFeatMut;
import br.ufpr.dinf.gres.core.jmetal4.experiments.PaesConfigs;
import br.ufpr.dinf.gres.loglog.Logger;
import org.springframework.stereotype.Component;

@Component
public class PAESGateway implements IGateway {
    private final PAESOPLAFeatMut paes;

    public PAESGateway(PAESOPLAFeatMut paes) {
        this.paes = paes;
    }

    public void execute(OptimizationDto optimizationDto) {
        GatewayUtils.addListener();
        ReaderConfig.setPathToConfigurationFile(UserHome.getPathToConfigFile());
        ReaderConfig.load();
        PaesConfigs configs = new PaesConfigs();
        configs.setLogger(Logger.getLogger());
        configs.activeLogs();
        configs.setDescription(optimizationDto.getDescription());
        configs.setPlas(optimizationDto.getInputArchitecture());
        configs.setNumberOfRuns(optimizationDto.getNumberRuns());
        configs.setMaxEvaluations(optimizationDto.getMaxEvaluations());
        configs.setArchiveSize(optimizationDto.getArchiveSize());
        GatewayUtils.setOperators(optimizationDto, configs);
        configs.setPathToDb(UserHome.getPathToDb());
        OPLAConfigs oplaConfig = new OPLAConfigs();
        oplaConfig.setSelectedObjectiveFunctions(optimizationDto.getObjectiveFunctions());
        configs.setOplaConfigs(oplaConfig);
        try {
            paes.execute(configs);
        } catch (Exception e) {
            GatewayUtils.log("ERROR");
        }
        GatewayUtils.log("Fin");
    }


}
