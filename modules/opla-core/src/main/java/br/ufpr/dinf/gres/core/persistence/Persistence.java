package br.ufpr.dinf.gres.core.persistence;

import br.ufpr.dinf.gres.common.exceptions.MissingConfigurationException;
import br.ufpr.dinf.gres.core.jmetal4.database.Database;
import br.ufpr.dinf.gres.core.jmetal4.metrics.*;
import br.ufpr.dinf.gres.core.jmetal4.results.ExecutionResults;
import br.ufpr.dinf.gres.core.jmetal4.results.ExperimentResults;
import br.ufpr.dinf.gres.core.jmetal4.results.InfoResults;
import br.ufpr.dinf.gres.core.jmetal4.util.Id;
import br.ufpr.dinf.gres.domain.entity.Experiment;
import br.ufpr.dinf.gres.domain.entity.ExperimentConfiguration;
import br.ufpr.dinf.gres.domain.entity.Info;
import br.ufpr.dinf.gres.domain.entity.MapObjectiveName;
import br.ufpr.dinf.gres.persistence.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Statement;
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


    public Persistence(AvMetricService avMetricService, ExperimentService experimentService, ExperimentConfigurationService experimentConfigurationService, MapObjectiveNameService mapObjectiveNameService, SscMetricService sscMetricService, WocsclassMetricService wocsclassMetricService, InfoService infoService, CbcsMetricService cbcsMetricService, WocsinterfaceMetricService wocsinterfaceMetricService, ConventionalMetricService conventionalMetricService, DistanceEuclideanService distanceEuclideanService, EleganceMetricService eleganceMetricService, SvcMetricService svcMetricService, ExecutionService executionService, PLAExtensibilityMetricService plaExtensibilityMetricService, ObjectiveService objectiveService) {
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
    }

    public void saveInfoAll(List<InfoResults> infoResults) {
        List<Info> collect = infoResults.stream().map(InfoResults::newPersistentInstance).collect(Collectors.toList());
        infoService.saveAll(collect);
    }

    public ExperimentResults createExperimentOnDb(String PLAName, String algorithm, String description, String hash) {
        ExperimentResults experimentResults = new ExperimentResults(PLAName, algorithm, description, hash);
        Experiment save = experimentService.save(experimentResults.newPersistentInstance());
        return experimentResults;
    }

    public void persisteMetrics(ExecutionResults executionResults) {
        persisteElegance(executionResults.getAllMetrics().getElegance());
        persisteFeatureDriven(executionResults.getAllMetrics().getFeatureDriven());
        persisteConventional(executionResults.getAllMetrics().getConventional());
        persistePlaExtensibility(executionResults.getAllMetrics().getPlaExtensibility());
        //addYni
        persisteWocsclass(executionResults.getAllMetrics().getWocsclass());
        persisteWocsinterface(executionResults.getAllMetrics().getWocsinterface());
        persisteCbcs(executionResults.getAllMetrics().getCbcs());
        persisteSsc(executionResults.getAllMetrics().getSsc());
        persisteSvc(executionResults.getAllMetrics().getSvc());
        persisteAv(executionResults.getAllMetrics().getAv());
        //addYni
    }

    public void persisteMetrics(AllMetrics allMetrics, List<String> list) {
        if (list.contains("elegance"))
            persisteElegance(allMetrics.getElegance());
        if (list.contains("featureDriven"))
            persisteFeatureDriven(allMetrics.getFeatureDriven());
        if (list.contains("conventional"))
            persisteConventional(allMetrics.getConventional());
        if (list.contains("PLAExtensibility"))
            persistePlaExtensibility(allMetrics.getPlaExtensibility());
        //addYni
        if (list.contains("wocsclass"))
            persisteWocsclass(allMetrics.getWocsclass());
        if (list.contains("wocsinterface"))
            persisteWocsinterface(allMetrics.getWocsinterface());
        if (list.contains("cbcs"))
            persisteCbcs(allMetrics.getCbcs());
        if (list.contains("ssc"))
            persisteSsc(allMetrics.getSsc());
        if (list.contains("svc"))
            persisteSvc(allMetrics.getSvc());
        if (list.contains("av"))
            persisteAv(allMetrics.getAv());
        //addYni
    }

    //addYni
    private void persisteWocsclass(List<Wocsclass> wocsC) {

    }

    private void persisteWocsinterface(List<Wocsinterface> wocsI) {
    }

    private void persisteCbcs(List<Cbcs> cBcs) {
    }


    private void persisteSsc(List<Ssc> sSc) {
    }

    private void persisteSvc(List<Svc> sVc) {
    }

    private void persisteAv(List<Av> aV) {
    }

    //addYni

    private void persistePlaExtensibility(List<PLAExtensibility> plaExt) {
    }

    private void persisteConventional(List<Conventional> conventionals) {
    }

    private void persisteFeatureDriven(List<FeatureDriven> featuresDriven) {
    }

    private void persisteElegance(List<Elegance> elegances) {
    }

    public void saveObjectivesNames(List<String> selectedMetrics, String experimentId) throws Exception {
        String names = getNames(selectedMetrics);
        MapObjectiveName mapObjectiveName = new MapObjectiveName();
        mapObjectiveName.setId(Long.valueOf(Id.generateUniqueId()));
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

    public void create(ExperimentConfs conf) {
        ExperimentConfiguration experimentConfiguration = new ExperimentConfiguration();
        experimentConfiguration.setId(Integer.valueOf(Id.generateUniqueId()));
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

    public void savedistance(HashMap<String, Double> calcula, String experiementId) {

    }

    public void persist(ExecutionResults executionResults) {

    }
}