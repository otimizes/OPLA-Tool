package br.ufpr.dinf.gres.core.persistence;

import br.ufpr.dinf.gres.core.jmetal4.core.Solution;
import br.ufpr.dinf.gres.core.jmetal4.core.SolutionSet;
import br.ufpr.dinf.gres.core.jmetal4.experiments.Metrics;
import br.ufpr.dinf.gres.core.jmetal4.util.NonDominatedSolutionList;
import br.ufpr.dinf.gres.domain.entity.*;
import br.ufpr.dinf.gres.persistence.service.*;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Persistence {

    private final ExperimentService experimentService;
    private final ExperimentConfigurationService experimentConfigurationService;
    private final MapObjectiveNameService mapObjectiveNameService;
    private final InfoService infoService;
    private final AvMetricService avMetricService;
    private final CbcsMetricService cbcsMetricService;
    private final ConventionalMetricService conventionalMetricService;
    private final DistanceEuclideanService distanceEuclideanService;
    private final EleganceMetricService eleganceMetricService;
    private final ExecutionService executionService;
    private final ObjectiveService objectiveService;
    private final PLAExtensibilityMetricService plaExtensibilityMetricService;
    private final SscMetricService sscMetricService;
    private final SvcMetricService svcMetricService;
    private final WocsclassMetricService wocsclassMetricService;
    private final WocsinterfaceMetricService wocsinterfaceMetricService;
    private final FeatureDrivenMetricService featureDrivenMetricService;

    public Persistence(AvMetricService avMetricService, ExperimentService experimentService, ExperimentConfigurationService experimentConfigurationService, MapObjectiveNameService mapObjectiveNameService, SscMetricService sscMetricService, WocsclassMetricService wocsclassMetricService, InfoService infoService, CbcsMetricService cbcsMetricService, WocsinterfaceMetricService wocsinterfaceMetricService, ConventionalMetricService conventionalMetricService, DistanceEuclideanService distanceEuclideanService, EleganceMetricService eleganceMetricService, SvcMetricService svcMetricService, ExecutionService executionService, PLAExtensibilityMetricService plaExtensibilityMetricService, ObjectiveService objectiveService, FeatureDrivenMetricService featureDrivenMetricService) {
        this.avMetricService = avMetricService;
        this.experimentService = experimentService;
        this.experimentConfigurationService = experimentConfigurationService;
        this.mapObjectiveNameService = mapObjectiveNameService;
        this.sscMetricService = sscMetricService;
        this.wocsclassMetricService = wocsclassMetricService;
        this.infoService = infoService;
        this.cbcsMetricService = cbcsMetricService;
        this.wocsinterfaceMetricService = wocsinterfaceMetricService;
        this.conventionalMetricService = conventionalMetricService;
        this.distanceEuclideanService = distanceEuclideanService;
        this.eleganceMetricService = eleganceMetricService;
        this.svcMetricService = svcMetricService;
        this.executionService = executionService;
        this.plaExtensibilityMetricService = plaExtensibilityMetricService;
        this.objectiveService = objectiveService;
        this.featureDrivenMetricService = featureDrivenMetricService;
    }

    public List<Info> saveInfoAll(List<Info> infoResults) {
        for (Info info : infoResults) {
            Objective objective = new Objective();
            objective.setExecution(info.getExecution());
            objective.setExperiment(info.getExperiment());
            objective.setIsAll(info.getIsAll());
            objective.setObjectives(info.getObjectives().replace("[", "").replace("]", "").replace(",", "|"));
            objective.setSolutionName(info.getName());
            objectiveService.save(objective);
        }
        return infoService.saveAll(infoResults);
    }

    public Experiment saveExperiment(String PLAName, String algorithm, String description, String hash) {
        Experiment experimentResults = new Experiment(PLAName, algorithm, description, hash);
        Experiment save = experimentService.save(experimentResults);
        experimentResults.setId(String.valueOf(save.getId()));
        return experimentResults;
    }

    public void save(AllMetrics allMetrics, List<String> list) {
        if (list.contains(Metrics.ELEGANCE.toString()))
            eleganceMetricService.saveAll(allMetrics.getElegance());
        if (list.contains(Metrics.FM.toString()))
            featureDrivenMetricService.saveAll(allMetrics.getFm());
        if (list.contains(Metrics.CONVENTIONAL.toString()))
            conventionalMetricService.saveAll(allMetrics.getConventional());
        if (list.contains(Metrics.PLAEXTENSIBILITY.toString()))
            plaExtensibilityMetricService.saveAll(allMetrics.getPlaExtensibility());
        if (list.contains(Metrics.WOCSCLASS.toString()))
            wocsclassMetricService.saveAll(allMetrics.getWocsclass());
        if (list.contains(Metrics.WOCSINTERFACE.toString()))
            wocsinterfaceMetricService.saveAll(allMetrics.getWocsinterface());
        if (list.contains(Metrics.CBCS.toString()))
            cbcsMetricService.saveAll(allMetrics.getCbcs());
        if (list.contains(Metrics.SSC.toString()))
            sscMetricService.saveAll(allMetrics.getSsc());
        if (list.contains(Metrics.SVC.toString()))
            svcMetricService.saveAll(allMetrics.getSvc());
        if (list.contains(Metrics.AV.toString()))
            avMetricService.saveAll(allMetrics.getAv());
    }

    public MapObjectiveName saveObjectivesNames(List<String> selectedMetrics, String experimentId) throws Exception {
        String names = getNames(selectedMetrics);
        MapObjectiveName mapObjectiveName = new MapObjectiveName();
        mapObjectiveName.setNames(names);
        mapObjectiveName.setExperiment(experimentService.getOne(experimentId));
        return mapObjectiveNameService.save(mapObjectiveName);
    }

    private String getNames(List<String> selectedMetrics) {
        StringBuilder names = new StringBuilder();
        for (String name : selectedMetrics) {
            names.append(name);
            names.append(" ");
        }

        return names.toString().trim();
    }

    public ExperimentConfiguration save(ExperimentConfs conf) {
        ExperimentConfiguration experimentConfiguration = new ExperimentConfiguration();
        experimentConfiguration.setExperiment(experimentService.getOne(conf.getExperimentId()));
        experimentConfiguration.setNumberOfRuns((long) conf.getConfigs().getNumberOfRuns());
        experimentConfiguration.setMaxEvaluations(conf.getConfigs().getMaxEvaluations());
        experimentConfiguration.setCrossoverProb(conf.getConfigs().getCrossoverProbability());
        experimentConfiguration.setMutationProb(conf.getConfigs().getMutationProbability());
        experimentConfiguration.setPatterns(conf.getDesignPatternStrategy());
        experimentConfiguration.setAlgorithm(conf.getAlgorithm());
        experimentConfiguration.setMutationOperators(Arrays.toString(conf.getConfigs().getMutationOperators().toArray()));
        experimentConfiguration.setArchiveSize(conf.getArchiveSize());
        experimentConfiguration.setPopulationSize(conf.getPopulationSize());
        experimentConfiguration.setObjectives(Arrays.toString(conf.getConfigs().getObjectiveFuncions().toArray()));
        return experimentConfigurationService.save(experimentConfiguration);
    }

    public List<DistanceEuclidean> saveDistance(HashMap<String, Double> results, String experiementId) {
        Experiment experiment = experimentService.getOne(experiementId);
        List<DistanceEuclidean> collect = results.entrySet().stream().map(entry -> {
            DistanceEuclidean distanceEuclidean = new DistanceEuclidean();
            distanceEuclidean.setSolutionName(entry.getKey());
            distanceEuclidean.setEd(entry.getValue());
            distanceEuclidean.setExpediment(experiment);
            return distanceEuclidean;
        }).collect(Collectors.toList());
        return distanceEuclideanService.saveAll(collect);
    }

    public Execution save(Execution executionResults) {
        return executionService.save(executionResults);
    }


    public SolutionSet queryNonDominatedSolutinsFromExperiment(String experimentID) throws Exception {
        List<Objective> byExperiment = objectiveService.findByExperiment(experimentID);
        SolutionSet solutionSet = new NonDominatedSolutionList();

        for (Objective objective : byExperiment) {
            int count = 0;
            String[] line = objective.getObjectives().split("\\|");
            Solution solution = new Solution(line.length);
            solution.setSolutionName(objective.getSolutionName());

            for (int i = 0; i < line.length; i++) {
                solution.setObjective(count, Double.parseDouble(line[i]));
                count++;
            }
            solutionSet.add(solution);
        }
        return solutionSet;
    }
}