package br.ufpr.dinf.gres.core.persistence;

import br.ufpr.dinf.gres.core.jmetal4.core.Solution;
import br.ufpr.dinf.gres.core.jmetal4.core.SolutionSet;
import br.ufpr.dinf.gres.core.jmetal4.metrics.ObjectiveFunctions;
import br.ufpr.dinf.gres.core.jmetal4.util.NonDominatedSolutionList;
import br.ufpr.dinf.gres.domain.entity.*;
import br.ufpr.dinf.gres.domain.entity.objectivefunctions.ObjectiveFunctionDomain;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.service.*;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class Persistence {

    private final ExperimentService experimentService;
    private final ExperimentConfigurationService experimentConfigurationService;
    private final MapObjectiveNameService mapObjectiveNameService;
    private final ExecutionService executionService;
    private final ObjectiveService objectiveService;
    private final InfoService infoService;
    private final DistanceEuclideanService distanceEuclideanService;
    private final ApplicationContext applicationContext;

    public Persistence(ObjectiveService objectiveService, InfoService infoService, ExperimentService experimentService,
                       DistanceEuclideanService distanceEuclideanService,
                       ExecutionService executionService,
                       ExperimentConfigurationService experimentConfigurationService, MapObjectiveNameService mapObjectiveNameService, ApplicationContext applicationContext) {
        this.experimentService = experimentService;
        this.experimentConfigurationService = experimentConfigurationService;
        this.mapObjectiveNameService = mapObjectiveNameService;
        this.executionService = executionService;
        this.objectiveService = objectiveService;
        this.infoService = infoService;
        this.distanceEuclideanService = distanceEuclideanService;
        this.applicationContext = applicationContext;
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

    public void save(Map<String, List<ObjectiveFunctionDomain>> allMetrics, List<String> list) {
        for (Map.Entry<String, List<ObjectiveFunctionDomain>> stringListEntry : allMetrics.entrySet()) {
            ObjectiveFunctions key = ObjectiveFunctions.valueOf(stringListEntry.getKey());
            for (ObjectiveFunctionDomain ObjectiveFunctionDomain : stringListEntry.getValue()) {
                BaseService bean = (BaseService) applicationContext.getBean(key.name() + "ObjectiveFunctionService");
                bean.save(ObjectiveFunctionDomain);
            }
        }
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