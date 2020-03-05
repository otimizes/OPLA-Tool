package br.ufpr.dinf.gres.core.persistence;

import br.ufpr.dinf.gres.core.jmetal4.database.Database;
import br.ufpr.dinf.gres.common.exceptions.MissingConfigurationException;
import br.ufpr.dinf.gres.core.jmetal4.metrics.*;
import br.ufpr.dinf.gres.core.jmetal4.results.ExecutionResults;
import br.ufpr.dinf.gres.core.jmetal4.results.ExperimentResults;
import br.ufpr.dinf.gres.core.jmetal4.results.FunResults;
import br.ufpr.dinf.gres.core.jmetal4.results.InfoResults;
import br.ufpr.dinf.gres.core.jmetal4.util.Id;
import br.ufpr.dinf.gres.persistence.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MetricsPersistence {

    @Autowired
    private InfoService infoService;



    public MetricsPersistence() {
    }

    public void saveInfoAll(List<InfoResults> infoResults) {
        infoService.save(infoResults);
    }

    public void saveFunAll(List<FunResults> funResults) {
        FunsResultPersistence funsPersistence = new FunsResultPersistence(
                this.allMetricsPersistenceDependencies.getConnection());
        try {
            for (FunResults fun : funResults)
                funsPersistence.persistFunsDatas(fun);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        funsPersistence = null;
    }

    public ExperimentResults createExperimentOnDb(String PLAName, String algorithm, String description, String hash) {
        ExperimentResults experiement = null;
        try {
            experiement = new ExperimentResults(PLAName, algorithm, description, hash);
            experiement.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return experiement;
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
    private void persisteWocsclass(List<Wocsclass> wocsC){
        if (!wocsC.isEmpty()) {
            for (Wocsclass wc : wocsC)
                this.allMetricsPersistenceDependencies.getWocsclassPersistence().save(wc);
        }

        wocsC = null;
    }

    private void persisteWocsinterface(List<Wocsinterface> wocsI){
        if (!wocsI.isEmpty()) {
            for (Wocsinterface wi : wocsI)
                this.allMetricsPersistenceDependencies.getWocsinterfacePersistence().save(wi);
        }

        wocsI = null;
    }

    private void persisteCbcs(List<Cbcs> cBcs){
        if (!cBcs.isEmpty()) {
            for (Cbcs cbcs : cBcs)
                this.allMetricsPersistenceDependencies.getCbcsPersistence().save(cbcs);
        }

        cBcs = null;
    }


    private void persisteSsc(List<Ssc> sSc){
        if (!sSc.isEmpty()) {
            for (Ssc ssc : sSc)
                this.allMetricsPersistenceDependencies.getSscPersistence().save(ssc);
        }

        sSc = null;
    }

    private void persisteSvc(List<Svc> sVc){
        if (!sVc.isEmpty()) {
            for (Svc svc : sVc)
                this.allMetricsPersistenceDependencies.getSvcPersistence().save(svc);
        }

        sVc = null;
    }

    private void persisteAv(List<Av> aV){
        if (!aV.isEmpty()) {
            for (Av av : aV)
                this.allMetricsPersistenceDependencies.getAvPersistence().save(av);
        }

        aV = null;
    }

    //addYni

    private void persistePlaExtensibility(List<PLAExtensibility> plaExt) {
        if (!plaExt.isEmpty()) {
            for (PLAExtensibility ext : plaExt)
                this.allMetricsPersistenceDependencies.getPlaExtensibilityPersistence().save(ext);
        }

        plaExt = null;
    }

    private void persisteConventional(List<Conventional> conventionals) {
        if (!conventionals.isEmpty()) {
            for (Conventional conv : conventionals)
                this.allMetricsPersistenceDependencies.getConventionalPersistence().save(conv);
        }
        conventionals = null;
    }

    private void persisteFeatureDriven(List<FeatureDriven> featuresDriven) {
        if (!featuresDriven.isEmpty()) {
            for (FeatureDriven fd : featuresDriven)
                this.allMetricsPersistenceDependencies.getFeatureDrivenPersistence().save(fd);
        }
        featuresDriven = null;
    }

    private void persisteElegance(List<Elegance> elegances) {
        if (!elegances.isEmpty()) {
            for (Elegance elegance : elegances)
                this.allMetricsPersistenceDependencies.getElegancePersistence().save(elegance);
        }
        elegances = null;
    }

    public void saveObjectivesNames(List<String> selectedMetrics, String experimentId) throws Exception {

        String names = getNames(selectedMetrics);

        StringBuilder query = new StringBuilder();
        query.append("insert into map_objectives_names (id, names, experiment_id) values(");
        query.append(Id.generateUniqueId());
        query.append(",");
        query.append("'");
        query.append(names);
        query.append("'");
        query.append(",");
        query.append(experimentId);
        query.append(")");

        try {
            Statement statement = Database.getConnection().createStatement();
            statement.executeUpdate(query.toString());
            statement.close();
        } catch (SQLException | ClassNotFoundException | MissingConfigurationException e) {
            e.printStackTrace();
        }

    }

    private String getNames(List<String> selectedMetrics) {
        StringBuilder names = new StringBuilder();
        for (String name : selectedMetrics) {
            names.append(name);
            names.append(" ");
        }

        return names.toString().trim();
    }

}