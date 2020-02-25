package br.ufpr.dinf.gres.oplaapi;

import arquitetura.io.*;
import arquitetura.util.UserHome;
import br.ufpr.dinf.gres.loglog.LogLog;
import br.ufpr.dinf.gres.loglog.LogLogData;
import br.ufpr.dinf.gres.loglog.Logger;
import br.ufpr.dinf.gres.oplaapi.dto.OptimizationDto;
import jmetal4.experiments.OPLAConfigs;
import jmetal4.experiments.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
public class OptimizationService {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(OptimizationService.class);
    private static final LogLog VIEW_LOGGER = Logger.getLogger();


    public Mono<OptimizationInfo> executeNSGAII(OptimizationDto optimizationDto) {
        Thread thread = new Thread(() -> {
            OPLAThreadScope.mainThreadId.set(Thread.currentThread().getId());
            OPLAThreadScope.setConfig(optimizationDto.getConfig());
            executeNSGAIIAlgorithm(optimizationDto);
        });
        thread.start();
        return Mono.just(new OptimizationInfo(thread.getId(), "", OptimizationInfoStatus.RUNNING)).subscribeOn(Schedulers.elastic());
    }

    public Mono<OptimizationInfo> executePAES(OptimizationDto optimizationDto) {
        Thread thread = new Thread(() -> {
            OPLAThreadScope.mainThreadId.set(Thread.currentThread().getId());
            OPLAThreadScope.setConfig(optimizationDto.getConfig());
            executePAESAlgorithm(optimizationDto);
        });
        thread.start();
        return Mono.just(new OptimizationInfo(OPLAThreadScope.mainThreadId.get(), "", OptimizationInfoStatus.RUNNING)).subscribeOn(Schedulers.elastic());
    }

    private void executeNSGAIIAlgorithm(OptimizationDto optimizationDto) {

        Logger.addListener(() -> {
            String s = LogLogData.printLog();
            if (OPLALogs.lastLogs.get(OPLAThreadScope.mainThreadId.get()) != null && OPLALogs.lastLogs.get(OPLAThreadScope.mainThreadId.get()).size() >= 100) {
                OPLALogs.lastLogs.get(OPLAThreadScope.mainThreadId.get()).clear();
            }
            OPLALogs.add(new OptimizationInfo(OPLAThreadScope.mainThreadId.get(), s, OptimizationInfoStatus.RUNNING));
        });

        LOGGER.info("set configuration path");
        ReaderConfig.setPathToConfigurationFile(UserHome.getPathToConfigFile());
        ReaderConfig.load();

        LOGGER.info("Create NSGA Config");
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

        // Se mutação estiver marcada, pega os operadores selecionados ,e seta a probabilidade de mutacao
        if (optimizationDto.getMutation()) {
            LOGGER.info("Configure Mutation Operator");
            List<String> mutationsOperators = optimizationDto.getMutationOperators();
            configs.setMutationOperators(mutationsOperators);
            configs.setMutationProbability(optimizationDto.getMutationProbability());
        }

        configs.setPlas(optimizationDto.getInputArchitecture());
        configs.setNumberOfRuns(optimizationDto.getNumberRuns());
        configs.setPopulationSize(optimizationDto.getPopulationSize());
        configs.setMaxEvaluations(optimizationDto.getMaxEvaluations());

        // Se crossover estiver marcado, configura probabilidade
        // Caso contrario desativa
        if (optimizationDto.getCrossover()) {
            LOGGER.info("Configure Crossover Probability");
            configs.setCrossoverProbability(optimizationDto.getCrossoverProbability());
        } else {
            configs.disableCrossover();
        }

        // OPA-Patterns Configurations
        if (optimizationDto.getMutationOperators().contains(FeatureMutationOperators.DESIGN_PATTERNS.getOperatorName())) {
            // joao
            LOGGER.info("Instanciando o campo do Patterns - oplatool classe nsgaii");
            String[] array = new String[optimizationDto.getPatterns().size()];
            configs.setPatterns(optimizationDto.getPatterns().toArray(array));
//                configs.setDesignPatternStrategy(VolatileConfs.getScopePatterns()); TODO

        }

        List<String> operadores = configs.getMutationOperators();

        for (int i = 0; i < operadores.size(); i++) {
            if (operadores.get(i) == "DesignPatterns") {
                operadores.remove(i);
            }
        }
        configs.setMutationOperators(operadores);
        // operadores convencionais ok
        // operadores padroes ok
        // String[] padroes = configs.getPatterns();

        // Configura onde o db esta localizado
        configs.setPathToDb(UserHome.getPathToDb());

        // Instancia a classe de configuracao da OPLA.java
        LOGGER.info("Create OPLA Config");
        OPLAConfigs oplaConfig = new OPLAConfigs();

        // Funcoes Objetivo
        oplaConfig.setSelectedObjectiveFunctions(optimizationDto.getObjectiveFunctions());

        // Add as confs de OPLA na classe de configuracoes gerais.
        configs.setOplaConfigs(oplaConfig);

        // Utiliza a classe Initializer do NSGAII passando as configs.
        NSGAII_OPLA_FeatMutInitializer nsgaii = new NSGAII_OPLA_FeatMutInitializer(configs);

        // Executa
        LOGGER.info("Execução NSGAII");
        try {
            nsgaii.run();
        } catch (Exception e) {
            e.printStackTrace();
            OPLALogs.lastLogs.get(OPLAThreadScope.mainThreadId.get()).clear();
            OPLALogs.add(new OptimizationInfo(OPLAThreadScope.mainThreadId.get(), "ERROR", OptimizationInfoStatus.COMPLETE));
        }
        LOGGER.info("Fim Execução NSGAII");
        OPLALogs.lastLogs.get(OPLAThreadScope.mainThreadId.get()).clear();
        OPLALogs.add(new OptimizationInfo(OPLAThreadScope.mainThreadId.get(), "Fin", OptimizationInfoStatus.COMPLETE));
    }

    private void executePAESAlgorithm(OptimizationDto optimizationDto) {

        Logger.addListener(() -> {
            String s = LogLogData.printLog();
            if (OPLALogs.lastLogs.get(OPLAThreadScope.mainThreadId.get()) != null && OPLALogs.lastLogs.get(OPLAThreadScope.mainThreadId.get()).size() >= 100) {
                OPLALogs.lastLogs.get(OPLAThreadScope.mainThreadId.get()).clear();
            }
            OPLALogs.add(new OptimizationInfo(OPLAThreadScope.mainThreadId.get(), s, OptimizationInfoStatus.RUNNING));
        });

        ReaderConfig.setPathToConfigurationFile(UserHome.getPathToConfigFile());
        ReaderConfig.load();

        PaesConfigs configs = new PaesConfigs();
        configs.setDescription(optimizationDto.getDescription());

        //Se mutação estiver marcada, pega os operadores selecionados
        //,e seta a probabilidade de mutacao
        if (optimizationDto.getMutation()) {
            List<String> mutationsOperators = optimizationDto.getMutationOperators();
            configs.setMutationOperators(mutationsOperators);
            configs.setMutationProbability(optimizationDto.getMutationProbability());
        }

        configs.setPlas(optimizationDto.getInputArchitecture());
        configs.setNumberOfRuns(optimizationDto.getNumberRuns());
        configs.setMaxEvaluations(optimizationDto.getMaxEvaluations());
        configs.setArchiveSize(optimizationDto.getArchiveSize());


        //Se crossover estiver marcado, configura probabilidade
        //Caso contrario desativa
        if (optimizationDto.getCrossover()) {
            configs.setCrossoverProbability(optimizationDto.getCrossoverProbability());
        } else {
            configs.disableCrossover();
        }

        //OPA-Patterns Configurations
        if (optimizationDto.getMutationOperators().contains(FeatureMutationOperators.DESIGN_PATTERNS.getOperatorName())) {
            String[] array = new String[optimizationDto.getPatterns().size()];
            configs.setPatterns(optimizationDto.getPatterns().toArray(array));
//            configs.setDesignPatternStrategy(VolatileConfs.getScopePatterns());
        }

        //Configura onde o db esta localizado
        configs.setPathToDb(UserHome.getPathToDb());

        //Instancia a classe de configuracao da OPLA.java
        OPLAConfigs oplaConfig = new OPLAConfigs();


        oplaConfig.setSelectedObjectiveFunctions(optimizationDto.getObjectiveFunctions());

        //Add as confs de OPLA na classe de configuracoes gerais.
        configs.setOplaConfigs(oplaConfig);

        //Utiliza a classe Initializer do NSGAII passando as configs.
        PAES_OPLA_FeatMutInitializer paes = new PAES_OPLA_FeatMutInitializer(configs);

        //Executa
        try {
            paes.run();
        } catch (Exception e) {
            e.printStackTrace();
            OPLALogs.lastLogs.get(OPLAThreadScope.mainThreadId.get()).clear();
            OPLALogs.add(new OptimizationInfo(OPLAThreadScope.mainThreadId.get(), "ERROR", OptimizationInfoStatus.COMPLETE));
        }
        LOGGER.info("Fim Execução NSGAII");
        OPLALogs.lastLogs.get(OPLAThreadScope.mainThreadId.get()).clear();
        OPLALogs.add(new OptimizationInfo(OPLAThreadScope.mainThreadId.get(), "Fin", OptimizationInfoStatus.COMPLETE));
    }
}
