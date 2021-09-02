package br.otimizes.oplatool.core.persistence;

import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctions;
import br.otimizes.oplatool.core.jmetal4.util.NonDominatedSolutionList;
import br.otimizes.oplatool.domain.entity.*;
import br.otimizes.oplatool.domain.entity.objectivefunctions.ObjectiveFunctionDomain;
import br.otimizes.oplatool.persistence.base.BaseService;
import br.otimizes.oplatool.persistence.service.*;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Persistence service to integrate the core with repository
 */
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

    public Persistence(ObjectiveService objectiveService,
                       InfoService infoService,
                       ExperimentService experimentService,
                       DistanceEuclideanService distanceEuclideanService,
                       ExecutionService executionService,
                       ExperimentConfigurationService experimentConfigurationService,
                       MapObjectiveNameService mapObjectiveNameService,
                       ApplicationContext applicationContext) {
        this.experimentService = experimentService;
        this.experimentConfigurationService = experimentConfigurationService;
        this.mapObjectiveNameService = mapObjectiveNameService;
        this.executionService = executionService;
        this.objectiveService = objectiveService;
        this.infoService = infoService;
        this.distanceEuclideanService = distanceEuclideanService;
        this.applicationContext = applicationContext;
    }

    /**
     * Save all info results
     *
     * @param infoResults info results
     * @return saved info
     */
    public List<Info> save(List<Info> infoResults) {
        for (Info info : infoResults) {
            Objective objective = new Objective();
            objective.setExecution(info.getExecution());
            objective.setExperiment(info.getExperiment());
            objective.setIsAll(info.getIsAll());
            objective.setObjectives(info.getObjectives().replace("[", "")
                    .replace("]", "").replace(",", "|"));
            objective.setSolutionName(info.getName());
            objectiveService.save(objective);
        }
        return infoService.saveAll(infoResults);
    }

    /**
     * Save experiment
     *
     * @param plaName     pla name
     * @param algorithm   optimization algorithm
     * @param description experiment description
     * @param hash        OPLA Thread Scope hash
     * @return saved experiment
     */
    public Experiment save(String plaName, String algorithm, String description, String hash) {
        Experiment experimentResults = new Experiment(plaName, algorithm, description, hash);
        Experiment save = experimentService.save(experimentResults);
        experimentResults.setId(String.valueOf(save.getId()));
        return experimentResults;
    }

    /**
     * Save objective functions
     *
     * @param objectiveFunctions objective functions
     */
    public void save(Map<String, List<ObjectiveFunctionDomain>> objectiveFunctions) {
        for (Map.Entry<String, List<ObjectiveFunctionDomain>> stringListEntry : objectiveFunctions.entrySet()) {
            ObjectiveFunctions key = ObjectiveFunctions.valueOf(stringListEntry.getKey());
            for (ObjectiveFunctionDomain ObjectiveFunctionDomain : stringListEntry.getValue()) {
                BaseService bean = (BaseService) applicationContext.getBean(key.name() + "ObjectiveFunctionService");
                bean.save(ObjectiveFunctionDomain);
            }
        }
    }

    /**
     * Save objective functions names
     *
     * @param objectiveFunctions objective functions
     * @param experimentId       experiment Id
     * @return Objective functions name data
     */
    public MapObjectiveName saveObjectivesNames(List<String> objectiveFunctions, String experimentId) {
        String names = getNames(objectiveFunctions);
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

    /**
     * Save experiment configs
     *
     * @param conf configs
     * @return saved configs
     */
    public ExperimentConfiguration save(ExperimentConfigurations conf) {
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
        experimentConfiguration.setObjectives(Arrays.toString(conf.getConfigs().getObjectiveFunctions().toArray()));
        return experimentConfigurationService.save(experimentConfiguration);
    }

    /**
     * Save Euclidean Distance
     *
     * @param results      results
     * @param experimentId experiment id
     * @return saved ED
     */
    public List<DistanceEuclidean> saveEuclideanDistance(HashMap<String, Double> results, String experimentId) {
        Experiment experiment = experimentService.getOne(experimentId);
        List<DistanceEuclidean> collect = results.entrySet().stream().map(entry -> {
            DistanceEuclidean distanceEuclidean = new DistanceEuclidean();
            distanceEuclidean.setSolutionName(entry.getKey());
            distanceEuclidean.setEd(entry.getValue());
            distanceEuclidean.setExpediment(experiment);
            return distanceEuclidean;
        }).collect(Collectors.toList());
        return distanceEuclideanService.saveAll(collect);
    }

    /**
     * Save execution
     *
     * @param execution execution
     * @return saved execution
     */
    public Execution save(Execution execution) {
        return executionService.save(execution);
    }

    /**
     * Get solution set by experiment
     *
     * @param experimentID experiment id
     * @return solution set
     */
    public SolutionSet queryNonDominatedSolutionsByExperiment(String experimentID) {
        List<Objective> byExperiment = objectiveService.findByExperiment(experimentID);
        SolutionSet solutionSet = new NonDominatedSolutionList();

        for (Objective objective : byExperiment) {
            int count = 0;
            String[] line = objective.getObjectives().split("\\|");
            Solution solution = new Solution(line.length);
            solution.setSolutionName(objective.getSolutionName());
            for (String word : line) {
                solution.setObjective(count, Double.parseDouble(word));
                count++;
            }
            solutionSet.add(solution);
        }
        return solutionSet;
    }
}