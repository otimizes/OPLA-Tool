package br.ufpr.dinf.gres.oplaapi;

import arquitetura.io.ReaderConfig;
import br.ufpr.dinf.gres.loglog.LogLog;
import br.ufpr.dinf.gres.loglog.LogLogData;
import br.ufpr.dinf.gres.loglog.Logger;
import br.ufpr.dinf.gres.oplaapi.config.ApplicationFile;
import br.ufpr.dinf.gres.oplaapi.config.ApplicationYamlConfig;
import br.ufpr.dinf.gres.oplaapi.util.Constants;
import br.ufpr.dinf.gres.oplaapi.util.UserHome;
import jmetal4.experiments.FeatureMutationOperators;
import jmetal4.experiments.NSGAIIConfig;
import jmetal4.experiments.NSGAII_OPLA_FeatMutInitializer;
import jmetal4.experiments.OPLAConfigs;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OptimizationController {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(OptimizationController.class);


    private static final LogLog VIEW_LOGGER = Logger.getLogger();
    private static Map<Long, List<OptimizationInfo>> lastLogs = new HashMap<>();
    private Long id = null;

    @RequestMapping("/")
    public String index() {
        return "OPLA-Tool";
    }

    @PostMapping(value = "/upload-pla")
    public ResponseEntity<List<String>> save(
            @RequestParam("file") List<MultipartFile> files) {

        String OUT_PATH = ApplicationFile.getInstance().getConfig().getDirectoryToExportModels().toString() + "/";
        List<String> paths = new ArrayList<>();

        try {
            for (MultipartFile mf : files) {
                byte[] bytes = mf.getBytes();
                String s = OUT_PATH + mf.getOriginalFilename();
                paths.add(s);
                createPathIfNotExists(s.substring(0, s.lastIndexOf(Constants.FILE_SEPARATOR)));
                Path path = Paths.get(s);
                Files.write(path, bytes);
            }

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(paths);
    }

    private void createPathIfNotExists(String s) {
        boolean mkdirs = new File(s).mkdirs();
    }


    @GetMapping(value = "/optimization-info/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<OptimizationInfo> optimizationInfo(@PathVariable Long id) {
        return Flux.interval(Duration.ofMillis(500)).take(50).onBackpressureBuffer(50)
                .map(str -> {
                    if (lastLogs.get(id) != null && !lastLogs.get(id).isEmpty()) {
                        OptimizationInfo optimizationInfo = lastLogs.get(id).get(0);
                        return OptimizationInfoStatus.COMPLETE.equals(optimizationInfo.status)
                                ? optimizationInfo : lastLogs.get(id).remove(0);
                    }
                    return new OptimizationInfo(id, "", OptimizationInfoStatus.RUNNING);
                });
    }

    @GetMapping("/nsgaii-test")
    public Mono<OptimizationInfo> executeNSGAII() {
        return executeNSGAII(new OptimizationDto());
    }

    @GetMapping("/config")
    public Mono<ApplicationYamlConfig> config() {
        return Mono.just(ApplicationFile.getInstance().getApplicationYaml()).subscribeOn(Schedulers.elastic());
    }

    @GetMapping("/dto")
    public Mono<OptimizationDto> dto() {
        return Mono.just(new OptimizationDto()).subscribeOn(Schedulers.elastic());
    }

    @PostMapping("/optimize")
    public Mono<OptimizationInfo> optimize(@RequestBody OptimizationDto optimizationDto) {
        switch (optimizationDto.getAlgorithm()) {
            case "NSGAII":
                return executeNSGAII(optimizationDto);
        }
        return executeNSGAII(optimizationDto);
    }

    @PostMapping("/nsgaii")
    public Mono<OptimizationInfo> executeNSGAII(@RequestBody OptimizationDto optimizationDto) {
        Thread thread = new Thread(() -> {
            execute(optimizationDto);
        });
        id = Thread.currentThread().getId();
        thread.start();
        return Mono.just(new OptimizationInfo(id, "", OptimizationInfoStatus.RUNNING)).subscribeOn(Schedulers.elastic());
    }

    public void execute(OptimizationDto optimizationDto) {

        Logger.addListener(() -> {
            String s = LogLogData.printLog();
            if (lastLogs.get(id) != null && lastLogs.get(id).size() >= 100) {
                lastLogs.get(id).clear();
            }
            lastLogs.computeIfAbsent(id, k -> new ArrayList<>()).add(new OptimizationInfo(id, s, OptimizationInfoStatus.RUNNING));
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
            lastLogs.get(id).clear();
            lastLogs.computeIfAbsent(id, k -> new ArrayList<>()).add(new OptimizationInfo(id, "ERROR", OptimizationInfoStatus.COMPLETE));
        }
        LOGGER.info("Fim Execução NSGAII");
        lastLogs.get(id).clear();
        lastLogs.computeIfAbsent(id, k -> new ArrayList<>()).add(new OptimizationInfo(id, "Fin", OptimizationInfoStatus.COMPLETE));
    }

}
