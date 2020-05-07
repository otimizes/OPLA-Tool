package br.ufpr.dinf.gres.api.resource;

import br.ufpr.dinf.gres.api.dto.OptimizationDto;
import br.ufpr.dinf.gres.api.utils.Interactions;
import br.ufpr.dinf.gres.api.utils.OpenPLA;
import br.ufpr.dinf.gres.architecture.io.*;
import br.ufpr.dinf.gres.architecture.util.UserHome;
import br.ufpr.dinf.gres.core.jmetal4.core.OPLASolutionSet;
import br.ufpr.dinf.gres.core.jmetal4.core.Solution;
import br.ufpr.dinf.gres.core.jmetal4.core.SolutionSet;
import br.ufpr.dinf.gres.core.jmetal4.experiments.*;
import br.ufpr.dinf.gres.domain.OPLAThreadScope;
import br.ufpr.dinf.gres.loglog.LogLog;
import br.ufpr.dinf.gres.loglog.LogLogData;
import br.ufpr.dinf.gres.loglog.Logger;
import br.ufpr.dinf.gres.patterns.strategies.scopeselection.impl.ElementsWithSameDesignPatternSelection;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.util.List;

@Service
public class OptimizationService {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(OptimizationService.class);
    private static final LogLog VIEW_LOGGER = Logger.getLogger();

    private final NSGAIIOPLAFeatMutInitializer nsgaii;
    private final PAESOPLAFeatMutInitializer paes;
    private final InteractiveEmail interactiveEmail;

    public OptimizationService(NSGAIIOPLAFeatMutInitializer nsgaii, PAESOPLAFeatMutInitializer paes, InteractiveEmail interactiveEmail) {
        this.nsgaii = nsgaii;
        this.paes = paes;
        this.interactiveEmail = interactiveEmail;
    }

    Mono<OptimizationInfo> executeNSGAII(OptimizationDto optimizationDto) {
        String token = OPLAThreadScope.token.get();
        Thread thread = new Thread(() -> {
            configureThreadScope(optimizationDto, token);
            executeNSGAIIAlgorithm(optimizationDto);
        });
        thread.start();
        return Mono.just(new OptimizationInfo(thread.getId(), "", OptimizationInfoStatus.RUNNING)).subscribeOn(Schedulers.elastic());
    }

    Mono<OptimizationInfo> executePAES(OptimizationDto optimizationDto) {
        String token = OPLAThreadScope.token.get();
        Thread thread = new Thread(() -> {
            configureThreadScope(optimizationDto, token);
            executePAESAlgorithm(optimizationDto);
        });
        thread.start();
        return Mono.just(new OptimizationInfo(OPLAThreadScope.mainThreadId.get(), "", OptimizationInfoStatus.RUNNING)).subscribeOn(Schedulers.elastic());
    }

    private void executeNSGAIIAlgorithm(OptimizationDto optimizationDto) {
        addListener();
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
        setOperators(optimizationDto, configs);
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
            nsgaii.run(configs);
        } catch (Exception e) {
            e.printStackTrace();
            log("ERROR");
        }
        log("Fin");
    }

    private void executePAESAlgorithm(OptimizationDto optimizationDto) {
        addListener();
        ReaderConfig.setPathToConfigurationFile(UserHome.getPathToConfigFile());
        ReaderConfig.load();
        PaesConfigs configs = new PaesConfigs();
        configs.setDescription(optimizationDto.getDescription());
        configs.setPlas(optimizationDto.getInputArchitecture());
        configs.setNumberOfRuns(optimizationDto.getNumberRuns());
        configs.setMaxEvaluations(optimizationDto.getMaxEvaluations());
        configs.setArchiveSize(optimizationDto.getArchiveSize());
        setOperators(optimizationDto, configs);
        configs.setPathToDb(UserHome.getPathToDb());
        OPLAConfigs oplaConfig = new OPLAConfigs();
        oplaConfig.setSelectedObjectiveFunctions(optimizationDto.getObjectiveFunctions());
        configs.setOplaConfigs(oplaConfig);
        try {
            paes.run(configs);
        } catch (Exception e) {
            log("ERROR");
        }
        log("Fin");
    }

    private void setOperators(OptimizationDto optimizationDto, ExperimentCommonConfigs configs) {
        if (optimizationDto.getMutation()) {
            List<String> mutationsOperators = optimizationDto.getMutationOperators();
            configs.setMutationOperators(mutationsOperators);
            configs.setMutationProbability(optimizationDto.getMutationProbability());
        }
        if (optimizationDto.getCrossover()) {
            configs.setCrossoverProbability(optimizationDto.getCrossoverProbability());
        } else {
            configs.disableCrossover();
        }
        if (optimizationDto.getMutationOperators().contains(FeatureMutationOperators.DESIGN_PATTERNS.getOperatorName())) {
            String[] array = new String[optimizationDto.getPatterns().size()];
            configs.setPatterns(optimizationDto.getPatterns().toArray(array));
            configs.setDesignPatternStrategy(new ElementsWithSameDesignPatternSelection(optimizationDto.getScopeSelection().get()));
        }
    }

    File downloadAlternative(Long threadId, Integer id) {
        SolutionSet solutionSet = Interactions.get(threadId).solutionSet;
        Solution solution = solutionSet.get(id);
        String plaNameOnAnalyses = "Interaction_" + threadId + "_" + id + "_" + solution.getAlternativeArchitecture().getName();
        String dirOnAnalyses = OPLAConfigThreadScope.config.get().getDirectoryToExportModels() + OPLAThreadScope.token.get() + System.getProperty("file.separator") + "interaction/";
        boolean delete = deleteDirectory(new File(dirOnAnalyses));
        boolean create = new File(dirOnAnalyses).mkdir();
        SolutionSet solutionSet1 = new SolutionSet();
        solutionSet1.setCapacity(1);
        solutionSet1.add(solution);
        ((OPLASolutionSet) solutionSet1).saveVariablesToFile(OPLAThreadScope.token.get() + System.getProperty("file.separator") + "interaction/" + plaNameOnAnalyses);
        File file = new File(dirOnAnalyses);
        return file;
    }

    void openAlternative(Long threadId, Integer id) {
        File file = downloadAlternative(threadId, id);
        File[] files = file.listFiles();
        File fileToOpen = files[0];
        String pathSmarty = OPLAConfigThreadScope.config.get().getPathSmarty();
        OpenPLA.executeJar(pathSmarty, fileToOpen.getAbsolutePath());
    }

    private boolean deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDirectory(children[i]);
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public File downloadAllAlternative(Long threadId) {
        SolutionSet solutionSet = Interactions.get(threadId).solutionSet;
        String plaNameOnAnalyses = "Interaction_" + threadId + "_" + "_" + solutionSet.get(0).getAlternativeArchitecture().getName();
        String dirOnAnalyses = OPLAConfigThreadScope.config.get().getDirectoryToExportModels() + OPLAThreadScope.token.get() + System.getProperty("file.separator") + "interaction/";
        boolean delete = deleteDirectory(new File(dirOnAnalyses));
        boolean create = new File(dirOnAnalyses).mkdir();
        ((OPLASolutionSet) solutionSet).saveVariablesToFile(OPLAThreadScope.token.get() + System.getProperty("file.separator") + "interaction/" + plaNameOnAnalyses);
        File file = new File(dirOnAnalyses);
        return file;
    }

    private void log(String error) {
        OPLALogs.lastLogs.get(OPLAThreadScope.mainThreadId.get()).clear();
        OPLALogs.add(new OptimizationInfo(OPLAThreadScope.mainThreadId.get(), error, OptimizationInfoStatus.COMPLETE));
    }

    private void addListener() {
        Logger.addListener(() -> {
            String s = LogLogData.printLog();
            if (OPLALogs.lastLogs.get(OPLAThreadScope.mainThreadId.get()) != null && OPLALogs.lastLogs.get(OPLAThreadScope.mainThreadId.get()).size() >= 100) {
                OPLALogs.lastLogs.get(OPLAThreadScope.mainThreadId.get()).clear();
            }
            OPLALogs.add(new OptimizationInfo(OPLAThreadScope.mainThreadId.get(), s, OptimizationInfoStatus.RUNNING));
        });
    }

    private void configureThreadScope(OptimizationDto optimizationDto, String token) {
        OPLAThreadScope.token.set(token);
        OPLAThreadScope.mainThreadId.set(Thread.currentThread().getId());
        OPLAConfigThreadScope.userDir.set(optimizationDto.getConfig().getDirectoryToExportModels() + OPLAThreadScope.token.get() + System.getProperty("file.separator"));
        OPLAConfigThreadScope.pla.set(OPLAConfigThreadScope.userDir.get() + optimizationDto.getInputArchitecture());
        optimizationDto.getConfig().setPathToProfile(optimizationDto.getConfig().getDirectoryToExportModels() + OPLAThreadScope.token.get() + System.getProperty("file.separator") + optimizationDto.getConfig().getPathToProfile());
        optimizationDto.getConfig().setPathToProfileRelationships(optimizationDto.getConfig().getDirectoryToExportModels() + OPLAThreadScope.token.get() + System.getProperty("file.separator") + optimizationDto.getConfig().getPathToProfileRelationships());
        optimizationDto.getConfig().setPathToProfilePatterns(optimizationDto.getConfig().getDirectoryToExportModels() + OPLAThreadScope.token.get() + System.getProperty("file.separator") + optimizationDto.getConfig().getPathToProfilePatterns());
        optimizationDto.getConfig().setPathToProfileConcern(optimizationDto.getConfig().getDirectoryToExportModels() + OPLAThreadScope.token.get() + System.getProperty("file.separator") + optimizationDto.getConfig().getPathToProfileConcern());
        OPLAConfigThreadScope.setConfig(optimizationDto.getConfig());
        optimizationDto.setInputArchitecture(OPLAConfigThreadScope.pla.get());
        optimizationDto.setInteractiveFunction(solutionSet -> interactiveEmail.run(solutionSet, optimizationDto));
    }
}
