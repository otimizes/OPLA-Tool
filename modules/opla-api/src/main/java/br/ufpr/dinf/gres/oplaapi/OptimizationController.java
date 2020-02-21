package br.ufpr.dinf.gres.oplaapi;

import arquitetura.io.ReaderConfig;
import br.ufpr.dinf.gres.loglog.LogLog;
import br.ufpr.dinf.gres.loglog.LogLogData;
import br.ufpr.dinf.gres.loglog.Logger;
import br.ufpr.dinf.gres.oplaapi.util.UserHome;
import jmetal4.experiments.FeatureMutationOperators;
import jmetal4.experiments.NSGAIIConfig;
import jmetal4.experiments.NSGAII_OPLA_FeatMutInitializer;
import jmetal4.experiments.OPLAConfigs;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OptimizationController {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(OptimizationController.class);


    private static final LogLog VIEW_LOGGER = Logger.getLogger();
    private static Map<Long, List<String>> lastLogs = new HashMap<>();
    private Long id = null;

    @RequestMapping("/")
    public String index() {
        return "OPLA-Tool";
    }

    private class OptimizationStatus {
        private Long percent;

        OptimizationStatus(Long percent) {
            this.percent = percent;
        }

        public Long getPercent() {
            return percent;
        }

        public void setPercent(Long percent) {
            this.percent = percent;
        }
    }

    @GetMapping(value = "/optimization-info/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<OptimizationInfo> optimizationInfo(@PathVariable Long id) {
        return Flux.interval(Duration.ofMillis(500)).take(50).onBackpressureBuffer(50)
                .map(str -> {
                    if (lastLogs.get(id) != null && !lastLogs.get(id).isEmpty()) {
                        return new OptimizationInfo(id, lastLogs.get(id).remove(0));
                    }
                    return new OptimizationInfo(id, "");
                });
    }

    @GetMapping("/nsgaii-test")
    public OptimizationInfo executeNSGAII() {
        return executeNSGAII(new OptimizationDto());
    }

    @PostMapping("/nsgaii")
    public OptimizationInfo executeNSGAII(@RequestBody OptimizationDto optimizationDto) {
        Thread thread = new Thread(() -> {
            execute(optimizationDto);
        });
        id = Thread.currentThread().getId();
        thread.start();
        return new OptimizationInfo(id);
    }

    public void execute(OptimizationDto optimizationDto) {

        Logger.addListener(() -> {
            String s = LogLogData.printLog();
            if (lastLogs.get(id) != null && lastLogs.get(id).size() > 100) {
                lastLogs.get(id).clear();
            }
            lastLogs.computeIfAbsent(id, k -> new ArrayList<>()).add(s);
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
        nsgaii.run();
        LOGGER.info("Fim Execução NSGAII");
    }

}
