package br.ufpr.dinf.gres.core.persistence;

import br.ufpr.dinf.gres.common.exceptions.MissingConfigurationException;
import br.ufpr.dinf.gres.core.jmetal4.database.Database;
import br.ufpr.dinf.gres.core.jmetal4.metrics.*;
import br.ufpr.dinf.gres.core.jmetal4.results.ExecutionResults;
import br.ufpr.dinf.gres.core.jmetal4.results.ExperimentResults;
import br.ufpr.dinf.gres.core.jmetal4.results.InfoResults;
import br.ufpr.dinf.gres.core.jmetal4.util.Id;
import br.ufpr.dinf.gres.persistence.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Persistence {


    public Persistence() {
    }

    public void saveInfoAll(List<InfoResults> infoResults) {
    }

    public ExperimentResults createExperimentOnDb(String PLAName, String algorithm, String description, String hash) {
        return null;
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