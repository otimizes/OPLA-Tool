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
    //addYni
    private WocsclassPersistence wocsClassPersistence;
    private WocsinterfacePersistence wocsInterfacePersistence;
    private CbcsPersistence cBcsPersistence;
    private SscPersistence sScPersistence;
    private SvcPersistence sVcPersistence;
    private AvPersistence aVPersistence;
    //addYni

    public AllMetricsPersistenceDependency(Connection connection) {
        this.connection = connection;
        this.infosPersistence = new InfosResultPersistence(connection);
        this.funsPersistence = new FunsResultPersistence(connection);
        this.elegancePersistence = new ElegancePersistence(connection);
        this.featureDrivenPersistence = new FeatureDrivenPersistence(connection);
        this.conventionalPersistence = new ConventionalPersistence(connection);
        this.plaExtensibilityPersistence = new PLAExtensibilityPersistence(connection);
        //addYni
        this.wocsClassPersistence = new WocsclassPersistence(connection);
        this.wocsInterfacePersistence = new WocsinterfacePersistence(connection);
        this.cBcsPersistence = new CbcsPersistence(connection);
        this.sScPersistence = new SscPersistence(connection);
        this.sVcPersistence = new SvcPersistence(connection);
        this.aVPersistence = new AvPersistence(connection);
        //addYni
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

    //addYni
    public WocsclassPersistence getWocsclassPersistence() {
        return wocsClassPersistence;
    }

    public WocsinterfacePersistence getWocsinterfacePersistence() {
        return wocsInterfacePersistence;
    }

    public CbcsPersistence getCbcsPersistence() {
        return cBcsPersistence;
    }

    public SscPersistence getSscPersistence() {
        return sScPersistence;
    }

    public SvcPersistence getSvcPersistence() {
        return sVcPersistence;
    }

    public AvPersistence getAvPersistence() {
        return aVPersistence;
    }

    //addYni

}
