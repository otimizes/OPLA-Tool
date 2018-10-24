package persistence;

import java.sql.Connection;

public class AllMetricsPersistenceDependency {

    private Connection connection;
    private InfosResultPersistence infosPersistence;
    private FunsResultPersistence funsPersistence;
    private ElegancePersistence elegancePersistence;
    private FeatureDrivenPersistence featureDrivenPersistence;
    private ConventionalPersistence conventionalPersistence;
    private PLAExtensibilityPersistence plaExtensibilityPersistence;

    public AllMetricsPersistenceDependency(Connection connection) {
        this.connection = connection;
        this.infosPersistence = new InfosResultPersistence(connection);
        this.funsPersistence = new FunsResultPersistence(connection);
        this.elegancePersistence = new ElegancePersistence(connection);
        this.featureDrivenPersistence = new FeatureDrivenPersistence(connection);
        this.conventionalPersistence = new ConventionalPersistence(connection);
        this.plaExtensibilityPersistence = new PLAExtensibilityPersistence(connection);
    }

    public Connection getConnection() {
        return connection;
    }

    public InfosResultPersistence getInfosPersistence() {
        return infosPersistence;
    }

    public FunsResultPersistence getFunsPersistence() {
        return funsPersistence;
    }

    public ElegancePersistence getElegancePersistence() {
        return elegancePersistence;
    }

    public FeatureDrivenPersistence getFeatureDrivenPersistence() {
        return featureDrivenPersistence;
    }

    public ConventionalPersistence getConventionalPersistence() {
        return conventionalPersistence;
    }

    public PLAExtensibilityPersistence getPlaExtensibilityPersistence() {
        return plaExtensibilityPersistence;
    }

}
