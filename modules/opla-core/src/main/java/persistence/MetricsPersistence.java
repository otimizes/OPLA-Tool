package persistence;

import database.Database;
import exceptions.MissingConfigurationException;
import metrics.*;
import results.Execution;
import results.Experiment;
import results.FunResults;
import results.InfoResult;
import utils.Id;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MetricsPersistence {

    private AllMetricsPersistenceDependency allMetricsPersistenceDependencies;

    public MetricsPersistence(AllMetricsPersistenceDependency allMetricsPersistenceDependencies) {
        this.allMetricsPersistenceDependencies = allMetricsPersistenceDependencies;
    }

    public void saveInfoAll(List<InfoResult> infoResults) {
        InfosResultPersistence infosPersistence = new InfosResultPersistence( this.allMetricsPersistenceDependencies.getConnection());
        try {
            for (InfoResult info : infoResults)
                infosPersistence.persistInfoDatas(info);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        infosPersistence = null;
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

    public Experiment createExperimentOnDb(String PLAName, String algorithm, String description) {
        Experiment experiement = null;
        try {
            experiement = new Experiment(PLAName, algorithm, description);
            experiement.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return experiement;
    }

    public void persisteMetrics(Execution execution) {
        persisteElegance(execution.getAllMetrics().getElegance());
        persisteFeatureDriven(execution.getAllMetrics().getFeatureDriven());
        persisteConventional(execution.getAllMetrics().getConventional());
        persistePlaExtensibility(execution.getAllMetrics().getPlaExtensibility());
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
    }

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