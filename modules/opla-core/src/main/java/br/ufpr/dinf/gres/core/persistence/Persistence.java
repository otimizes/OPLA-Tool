package br.ufpr.dinf.gres.core.persistence;

import br.ufpr.dinf.gres.core.jmetal4.core.Solution;
import br.ufpr.dinf.gres.core.jmetal4.core.SolutionSet;
import br.ufpr.dinf.gres.core.jmetal4.metrics.*;
import br.ufpr.dinf.gres.domain.entity.ExecutionResults;
import br.ufpr.dinf.gres.domain.entity.ExperimentResults;
import br.ufpr.dinf.gres.domain.entity.InfoResults;
import br.ufpr.dinf.gres.domain.util.IdUtil;
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

    public void saveInfoAll(List<InfoResults> infoResults) {
        List<Info> collect = infoResults.stream().map(InfoResults::newPersistentInstance).collect(Collectors.toList());
        infoService.saveAll(collect);
    }

    public ExperimentResults saveExperiment(String PLAName, String algorithm, String description, String hash) {
        ExperimentResults experimentResults = new ExperimentResults(PLAName, algorithm, description, hash);
        Experiment save = experimentService.save(experimentResults.newPersistentInstance());
        experimentResults.setId(String.valueOf(save.getId()));
        return experimentResults;
    }

    public void save(AllMetrics allMetrics, List<String> list) {
        if (list.contains("elegance"))
            saveElegance(allMetrics.getElegance());
        if (list.contains("featureDriven"))
            saveFeatureDriven(allMetrics.getFeatureDriven());
        if (list.contains("conventional"))
            saveConventional(allMetrics.getConventional());
        if (list.contains("PLAExtensibility"))
            savePlaExtensibility(allMetrics.getPlaExtensibility());
        if (list.contains("wocsclass"))
            saveWocsclass(allMetrics.getWocsclass());
        if (list.contains("wocsinterface"))
            persisteWocsinterface(allMetrics.getWocsinterface());
        if (list.contains("cbcs"))
            saveCbcs(allMetrics.getCbcs());
        if (list.contains("ssc"))
            saveSsc(allMetrics.getSsc());
        if (list.contains("svc"))
            saveSvc(allMetrics.getSvc());
        if (list.contains("av"))
            saveAv(allMetrics.getAv());
    }

    private void saveWocsclass(List<Wocsclass> wocsC) {
        wocsclassMetricService.saveAll(wocsC.stream().map(Wocsclass::newPersistentInstance).collect(Collectors.toList()));
    }

    private void persisteWocsinterface(List<Wocsinterface> wocsI) {
        wocsinterfaceMetricService.saveAll(wocsI.stream().map(Wocsinterface::newPersistentInstance).collect(Collectors.toList()));
    }

    private void saveCbcs(List<Cbcs> cBcs) {
        cbcsMetricService.saveAll(cBcs.stream().map(Cbcs::newPersistentInstance).collect(Collectors.toList()));
    }

    private void saveSsc(List<Ssc> sSc) {
        sscMetricService.saveAll(sSc.stream().map(Ssc::newPersistentInstance).collect(Collectors.toList()));
    }

    private void saveSvc(List<Svc> sVc) {
        svcMetricService.saveAll(sVc.stream().map(Svc::newPersistentInstance).collect(Collectors.toList()));
    }

    private void saveAv(List<Av> aV) {
        avMetricService.saveAll(aV.stream().map(Av::newPersistentInstance).collect(Collectors.toList()));
    }

    private void savePlaExtensibility(List<PLAExtensibility> plaExt) {
        plaExtensibilityMetricService.saveAll(plaExt.stream().map(PLAExtensibility::newPersistentInstance).collect(Collectors.toList()));
    }

    private void saveConventional(List<Conventional> conventionals) {
        conventionalMetricService.saveAll(conventionals.stream().map(Conventional::newPersistentInstance).collect(Collectors.toList()));
    }

    private void saveFeatureDriven(List<FeatureDriven> featuresDriven) {
        featureDrivenMetricService.saveAll(featuresDriven.stream().map(FeatureDriven::newPersistentInstance).collect(Collectors.toList()));
    }

    private void saveElegance(List<Elegance> elegances) {
        eleganceMetricService.saveAll(elegances.stream().map(Elegance::newPersistentInstance).collect(Collectors.toList()));
    }

    public void saveObjectivesNames(List<String> selectedMetrics, String experimentId) throws Exception {
        String names = getNames(selectedMetrics);
        MapObjectiveName mapObjectiveName = new MapObjectiveName();
        mapObjectiveName.setId(Long.valueOf(IdUtil.generateUniqueId()));
        mapObjectiveName.setNames(names);
        mapObjectiveName.setExperiment(experimentService.getOne(Long.valueOf(experimentId)));
        mapObjectiveNameService.save(mapObjectiveName);
    }

    private String getNames(List<String> selectedMetrics) {
        StringBuilder names = new StringBuilder();
        for (String name : selectedMetrics) {
            names.append(name);
            names.append(" ");
        }

        return names.toString().trim();
    }

    public void save(ExperimentConfs conf) {
        ExperimentConfiguration experimentConfiguration = new ExperimentConfiguration();
        experimentConfiguration.setId(Long.valueOf(IdUtil.generateUniqueId()));
        experimentConfiguration.setExperiment(experimentService.getOne(Long.valueOf(conf.getExperimentId())));
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
        experimentConfigurationService.save(experimentConfiguration);
    }

    public void saveDistance(HashMap<String, Double> results, String experiementId) {
        Experiment experiment = experimentService.getOne(Long.valueOf(experiementId));
        List<DistanceEuclidean> collect = results.entrySet().stream().map(entry -> {
            DistanceEuclidean distanceEuclidean = new DistanceEuclidean();
            distanceEuclidean.setSolutionName(entry.getKey());
            distanceEuclidean.setEd(entry.getValue());
            distanceEuclidean.setExpediment(experiment);
            return distanceEuclidean;
        }).collect(Collectors.toList());
        distanceEuclideanService.saveAll(collect);
    }

    public void save(ExecutionResults executionResults) {
        Execution execution = executionService.save(executionResults.newPersistentInstance());
        for (InfoResults infoResults : executionResults.getInfos()) {
            Objective objective = new Objective();
            objective.setExecution(execution);
            objective.setExperiment(execution.getExperiment());
            objective.setId(Long.valueOf(IdUtil.generateUniqueId()));
            objective.setIsAll(infoResults.getIsAll());
            objective.setObjectives(infoResults.getObjectives().replace("[", "").replace("]", "").replace(",", "|"));
            objective.setSolutionName(infoResults.getName());
            objectiveService.save(objective);
        }

    }

    public SolutionSet queryNonDominatedSolutinsFromExperiment(String experimentID) throws Exception {
        List<Objective> byExperiment = objectiveService.findByExperiment(Long.valueOf(experimentID));
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