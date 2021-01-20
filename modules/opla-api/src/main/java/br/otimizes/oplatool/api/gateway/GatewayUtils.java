package br.otimizes.oplatool.api.gateway;

import br.otimizes.oplatool.api.dto.OptimizationDto;
import br.otimizes.oplatool.api.utils.Interactions;
import br.otimizes.oplatool.architecture.io.OPLALogs;
import br.otimizes.oplatool.architecture.io.OptimizationInfo;
import br.otimizes.oplatool.architecture.io.OptimizationInfoStatus;
import br.otimizes.oplatool.architecture.smarty.util.SaveStringToFile;
import br.otimizes.oplatool.domain.config.UserHome;
import br.otimizes.oplatool.core.jmetal4.experiments.ExperimentCommonConfigs;
import br.otimizes.oplatool.core.jmetal4.experiments.OPLAConfigs;
import br.otimizes.oplatool.core.jmetal4.operators.MutationOperators;
import br.otimizes.oplatool.domain.OPLAThreadScope;
import br.ufpr.dinf.gres.loglog.LogLogData;
import br.ufpr.dinf.gres.loglog.Logger;
import br.otimizes.oplatool.patterns.strategies.scopeselection.impl.ElementsWithSameDesignPatternSelection;

public class GatewayUtils {

    public static void log(String error) {
        OPLALogs.lastLogs.get(OPLAThreadScope.hash.get()).clear();
        Interactions.interactions.remove(OPLAThreadScope.hash.get());
        OPLALogs.add(new OptimizationInfo(OPLAThreadScope.hashOnPosteriori.get(), OPLAThreadScope.mainThreadId.get(), error, OptimizationInfoStatus.COMPLETE));
        SaveStringToFile.getInstance().deleteTempFolder();
        SaveStringToFile.getInstance().moveProjectFinishPosteriori();
    }

    public static void addListener() {
        Logger.addListener(() -> {
            String s = LogLogData.printLog();
            if (OPLALogs.lastLogs.get(OPLAThreadScope.hash.get()) != null && OPLALogs.lastLogs.get(OPLAThreadScope.hash.get()).size() >= 100) {
                OPLALogs.lastLogs.get(OPLAThreadScope.hash.get()).clear();
            }
            OPLALogs.add(new OptimizationInfo(OPLAThreadScope.mainThreadId.get(), s, OptimizationInfoStatus.RUNNING, 1));
        });
    }


    public static void setOperators(OptimizationDto optimizationDto, ExperimentCommonConfigs configs) {
        if (optimizationDto.getMutation()) {
            configs.setMutationOperators(optimizationDto.getMutationOperators());
            configs.setMutationProbability(optimizationDto.getMutationProbability());
        }
        if (optimizationDto.getCrossover()) {
            configs.setCrossoverProbability(optimizationDto.getCrossoverProbability());
            configs.setCrossoverOperators(optimizationDto.getCrossoverOperators());
        } else {
            configs.disableCrossover();
        }
        if (optimizationDto.getMutationOperators().contains(MutationOperators.DESIGN_PATTERNS.toString())) {
            String[] array = new String[optimizationDto.getPatterns().size()];
            configs.setPatterns(optimizationDto.getPatterns().toArray(array));
            configs.setDesignPatternStrategy(new ElementsWithSameDesignPatternSelection(optimizationDto.getScopeSelection().get()));
        }
    }

    public static void setConfigs(OptimizationDto optimizationDto, ExperimentCommonConfigs configs) {
        configs.setLogger(Logger.getLogger());
        configs.activeLogs();
        configs.setDescription(optimizationDto.getDescription());
        configs.setPlas(optimizationDto.getInputArchitecture());
        configs.setNumberOfRuns(optimizationDto.getNumberRuns());
        configs.setMaxEvaluations(optimizationDto.getMaxEvaluations());
        configs.setArchitectureBuilder(optimizationDto.getArchitectureBuilder());
        GatewayUtils.setOperators(optimizationDto, configs);
        configs.setPathToDb(UserHome.getPathToDb());
        OPLAConfigs oplaConfig = new OPLAConfigs();
        oplaConfig.setSelectedObjectiveFunctions(optimizationDto.getObjectiveFunctions());
        configs.setOplaConfigs(oplaConfig);

        configs.setInteractive(optimizationDto.getInteractive());
        configs.setInteractiveFunction(optimizationDto.getInteractiveFunction());
        configs.setMaxInteractions(optimizationDto.getMaxInteractions());
        configs.setFirstInteraction(optimizationDto.getFirstInteraction());
        configs.setIntervalInteraction(optimizationDto.getIntervalInteraction());
        configs.setClusteringMoment(optimizationDto.getClusteringMoment());
        configs.setClusteringAlgorithm(optimizationDto.getClusteringAlgorithm());
    }


}
