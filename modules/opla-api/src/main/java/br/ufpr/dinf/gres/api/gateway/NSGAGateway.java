package br.ufpr.dinf.gres.api.gateway;

import br.ufpr.dinf.gres.api.dto.OptimizationDto;
import br.ufpr.dinf.gres.architecture.io.ReaderConfig;
import br.ufpr.dinf.gres.architecture.util.UserHome;
import br.ufpr.dinf.gres.core.jmetal4.experiments.NSGAIIConfig;
import br.ufpr.dinf.gres.core.jmetal4.experiments.NSGAIIOPLAFeatMut;
import br.ufpr.dinf.gres.core.jmetal4.experiments.OPLAConfigs;
import br.ufpr.dinf.gres.loglog.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NSGAGateway implements IGateway {
    private final NSGAIIOPLAFeatMut nsgaiiFeatMut;

    public NSGAGateway(NSGAIIOPLAFeatMut nsgaiiFeatMut) {
        this.nsgaiiFeatMut = nsgaiiFeatMut;
    }

    public void execute(OptimizationDto optimizationDto) {
        GatewayUtils.addListener();
        ReaderConfig.setPathToConfigurationFile(UserHome.getPathToConfigFile());
        ReaderConfig.load();
        NSGAIIConfig configs = new NSGAIIConfig();
        configs.setLogger(Logger.getLogger());
        configs.activeLogs();
        configs.setDescription(optimizationDto.getDescription());
        configs.setInteractive(optimizationDto.getInteractive());
        configs.setInteractiveFunction(optimizationDto.getInteractiveFunction());
        configs.setMaxInteractions(optimizationDto.getMaxInteractions());
        configs.setFirstInteraction(optimizationDto.getFirstInteraction());
        configs.setIntervalInteraction(optimizationDto.getIntervalInteraction());
        configs.setClusteringMoment(optimizationDto.getClusteringMoment());
        configs.setClusteringAlgorithm(optimizationDto.getClusteringAlgorithm());
        configs.setPlas(optimizationDto.getInputArchitecture());
        configs.setNumberOfRuns(optimizationDto.getNumberRuns());
        configs.setPopulationSize(optimizationDto.getPopulationSize());
        configs.setMaxEvaluations(optimizationDto.getMaxEvaluations());
        GatewayUtils.setOperators(optimizationDto, configs);
        List<String> operadores = configs.getMutationOperators();
        for (int i = 0; i < operadores.size(); i++) {
            if (operadores.get(i).equals("DesignPatterns")) {
                operadores.remove(i);
            }
        }
        configs.setMutationOperators(operadores);
        configs.setPathToDb(UserHome.getPathToDb());
        OPLAConfigs oplaConfig = new OPLAConfigs();
        oplaConfig.setSelectedObjectiveFunctions(optimizationDto.getObjectiveFunctions());
        configs.setOplaConfigs(oplaConfig);
        try {
            nsgaiiFeatMut.execute(configs);
        } catch (Exception e) {
            e.printStackTrace();
            GatewayUtils.log("ERROR");
        }
        GatewayUtils.log("Fin");
    }


}
