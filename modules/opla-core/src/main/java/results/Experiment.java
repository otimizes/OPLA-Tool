/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package results;

import database.Database;
import metrics.*;
import utils.Id;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Classe que representa um experiemento.
 *
 * @author elf
 */
public class Experiment {

    private String id;
    private String name;
    private String description;
    private String algorithm;
    private String createdAt;
    private Collection<Execution> executions;

    /**
     * Use this if you don't cares about id generated. Otherwise use setters
     *
     * @param name
     * @param description
     * @throws Exception
     */
    public Experiment(String name, String algorithm, String description) throws Exception {
        this.name = name;
        this.algorithm = algorithm;
        this.description = description;
        this.id = givenId();
        this.createdAt = setCreatedAt();
    }

    /**
     * Return all Experiments.<br/>
     * </br>
     * Once this method is static, and maybe database URL isn't yet seted, you
     * need supply a database URL before. To do that:
     * <p>
     * <code>Database.setURL("path/to/db/file");</code>>
     *
     * @return {@link List} of {@link Experiment}
     * @throws SQLException
     * @throws Exception
     */
    public static List<Experiment> all() throws SQLException, Exception {

        String attrs[] = {"id", "name", "algorithm", "created_at", "description"};
        List<Experiment> experiements = new ArrayList<Experiment>();
        ResultSet r = null;
        Connection connection = Database.getConnection();
        Statement staementExperiement = connection.createStatement();

        r = staementExperiement.executeQuery("select * from experiments");

        while (r.next()) {
            Experiment exp = new Experiment(r.getString(attrs[1]), r.getString(attrs[2]), r.getString(attrs[3]));
            exp.setId(r.getString(attrs[0]));
            Collection<Execution> execs = buildExecutions(r.getString(attrs[0]), exp, connection);
            exp.setCreatedAt(r.getString(attrs[3]));
            exp.setExecutions(execs);
            exp.setDescription(r.getString(attrs[4]));
            experiements.add(exp);
        }
        r.close();
        staementExperiement.close();
        connection.close();

        return experiements;
    }

    private static Collection<Execution> buildExecutions(String experiementId, Experiment exp, Connection connection)
            throws Exception {
        Statement statamentExecution = connection.createStatement();

        ResultSet r = statamentExecution
                .executeQuery("select * from executions where experiement_id = " + experiementId);
        List<Execution> execs = new ArrayList<Execution>();

        while (r.next()) {
            AllMetrics allMetrics = new AllMetrics();
            Execution exec = new Execution(exp);

            exec.setId(r.getString("id"));
            List<FunResults> funs = buildFuns(connection, exp, exec);
            List<InfoResult> infos = buildInfos(connection, exp, exec);

            allMetrics.setConventional(buildConventionalMetrics(connection, exec, exp));
            allMetrics.setElegance(buildEleganceMetrics(connection, exec, exp));
            allMetrics.setFeatureDriven(buildFeatureDrivenMetrics(connection, exec, exp));
            allMetrics.setPlaExtensibility(buildPlaExtensibility(connection, exec, exp));

            exec.setTime(Long.parseLong(r.getString("time")));
            exec.setFuns(funs);
            exec.setInfos(infos);
            exec.setAllMetrics(allMetrics);
            execs.add(exec);
        }

        statamentExecution.close();

        return Collections.unmodifiableCollection(execs);
    }

    private static List<PLAExtensibility> buildPlaExtensibility(Connection connection, Execution exec, Experiment exp)
            throws SQLException {
        Statement plaExtStatement = connection.createStatement();
        StringBuilder query = new StringBuilder();
        query.append("select * from PLAExtensibilityMetrics where execution_id=");
        query.append(exec.getId());
        query.append(" OR (execution_id='' AND experiement_id=");
        query.append(exp.getId());
        query.append(")");

        ResultSet resultPLAExt = plaExtStatement.executeQuery(query.toString());

        List<PLAExtensibility> plaExtensibilities = new ArrayList<PLAExtensibility>();

        while (resultPLAExt.next()) {
            String idSolution = resultPLAExt.getString("id_solution");
            PLAExtensibility plaExt = new PLAExtensibility(idSolution, exec, exp);
            plaExt.setPlaExtensibility(getResultParseDouble(resultPLAExt, "plaExtensibility"));
            plaExt.setIsAll(getResultParseInteger(resultPLAExt, "is_all"));

            plaExtensibilities.add(plaExt);
        }

        return plaExtensibilities;
    }

    private static List<FeatureDriven> buildFeatureDrivenMetrics(Connection connection, Execution exec, Experiment exp)
            throws Exception {

        Statement featureDrivenStatement = connection.createStatement();
        StringBuilder query = new StringBuilder();
        query.append("select * from FeatureDrivenMetrics where execution_id=");
        query.append(exec.getId());
        query.append(" OR (execution_id='' AND experiement_id=");
        query.append(exp.getId());
        query.append(")");

        ResultSet resultFeatureDriven = featureDrivenStatement.executeQuery(query.toString());

        List<FeatureDriven> featuresDriven = new ArrayList<FeatureDriven>();

        while (resultFeatureDriven.next()) {
            FeatureDriven fd = new FeatureDriven(resultFeatureDriven.getString("id_solution"), exec, exp);
            fd.setCdac(getResultParseDouble(resultFeatureDriven, "cdac"));
            fd.setCdai(getResultParseDouble(resultFeatureDriven, "cdai"));
            fd.setCdao(getResultParseDouble(resultFeatureDriven, "cdao"));
            fd.setCibc(getResultParseDouble(resultFeatureDriven, "cibc"));
            fd.setIibc(getResultParseDouble(resultFeatureDriven, "iibc"));
            fd.setOobc(getResultParseDouble(resultFeatureDriven, "oobc"));
            fd.setLcc(getResultParseDouble(resultFeatureDriven, "lcc"));
            fd.setLccClass(getResultParseDouble(resultFeatureDriven, "lccClass"));
            fd.setCdaClass(getResultParseDouble(resultFeatureDriven, "cdaClass"));
            fd.setCibClass(getResultParseDouble(resultFeatureDriven, "cibClass"));
            fd.setIsAll(getResultParseInteger(resultFeatureDriven, "is_all"));

            featuresDriven.add(fd);

        }

        return featuresDriven;
    }

    private static List<Elegance> buildEleganceMetrics(Connection connection, Execution exec, Experiment exp)
            throws SQLException {
        Statement eleganceStatement = connection.createStatement();

        StringBuilder query = new StringBuilder();
        query.append("select * from EleganceMetrics where execution_id =");
        query.append(exec.getId());
        query.append(" OR (execution_id='' AND experiement_id=");
        query.append(exp.getId());
        query.append(")");

        ResultSet resultElegance = eleganceStatement.executeQuery(query.toString());
        List<Elegance> elegances = new ArrayList<Elegance>();

        while (resultElegance.next()) {
            Elegance elegance = new Elegance(resultElegance.getString("id_solution"), exec, exp);
            elegance.setAtmr(getResultParseDouble(resultElegance, "atmr"));
            elegance.setEc(getResultParseDouble(resultElegance, "ec"));
            elegance.setNac(getResultParseDouble(resultElegance, "nac"));
            elegance.setIsAll(getResultParseInteger(resultElegance, "is_all"));
            elegances.add(elegance);
        }

        return elegances;
    }

    private static List<Conventional> buildConventionalMetrics(Connection connection, Execution execution,
                                                               Experiment experiement) throws SQLException {
        Statement conventionalStatement = connection.createStatement();

        StringBuilder query = new StringBuilder();
        query.append("select * from ConventionalMetrics where execution_id = ");
        query.append(execution.getId());
        query.append(" OR (execution_id='' AND experiement_id=");
        query.append(experiement.getId());
        query.append(")");

        ResultSet resultSetConventional = conventionalStatement.executeQuery(query.toString());
        List<Conventional> conventionals = new ArrayList<Conventional>();

        while (resultSetConventional.next()) {
            Conventional conventional = new Conventional(resultSetConventional.getString("id_solution"), execution,
                    experiement);
            conventional.setSumCohesion(getResultParseDouble(resultSetConventional, "sum_cohesion"));
            conventional.setCohesion(getResultParseDouble(resultSetConventional, "cohesion"));
            conventional.setMeanDepComps(getResultParseDouble(resultSetConventional, "meanDepComps"));
            conventional.setMeanNumOps(getResultParseDouble(resultSetConventional, "meanNumOps"));
            conventional.setSumClassesDepIn(getResultParseDouble(resultSetConventional, "sumClassesDepIn").intValue());
            conventional
                    .setSumClassesDepOut(getResultParseDouble(resultSetConventional, "sumClassesDepOut").intValue());
            conventional.setSumDepIn(getResultParseDouble(resultSetConventional, "sumDepIn"));
            conventional.setSumDepOut(getResultParseDouble(resultSetConventional, "sumDepOut"));
            conventional.setIsAll(getResultParseInteger(resultSetConventional, "is_all"));

            conventionals.add(conventional);
        }

        return conventionals;

    }

    private static List<InfoResult> buildInfos(Connection connection, Experiment exp, Execution exec)
            throws SQLException {
        Statement infosStatement = connection.createStatement();
        List<InfoResult> infos = new ArrayList<InfoResult>();

        StringBuilder query = new StringBuilder();
        query.append("select * from infos where execution_id=");
        query.append(exec.getId());
        query.append(" OR (execution_id='' AND experiement_id=");
        query.append(exp.getId());
        query.append(")");

        ResultSet resultSetInfos = infosStatement.executeQuery(query.toString());

        while (resultSetInfos.next()) {
            InfoResult info = new InfoResult();
            info.setExecution(exec);
            info.setExperiement(exp);
            info.setId(resultSetInfos.getString("id"));
            info.setName(resultSetInfos.getString("name"));
            info.setIsAll(getResultParseInteger(resultSetInfos, "is_all"));
            info.setListOfConcerns(resultSetInfos.getString("list_of_concerns"));
            info.setNumberOfPackages(getResultParseInteger(resultSetInfos, "number_of_packages"));
            info.setNumberOfVariabilities(getResultParseInteger(resultSetInfos, "number_of_variabilities"));
            info.setNumberOfInterfaces(getResultParseInteger(resultSetInfos, "number_of_interfaces"));
            info.setNumberOfClasses(getResultParseInteger(resultSetInfos, "number_of_classes"));
            info.setNumberOfDependencies(getResultParseInteger(resultSetInfos, "number_of_dependencies"));
            info.setNumberOfAbstraction(getResultParseInteger(resultSetInfos, "number_of_abstractions"));
            info.setNumberOfAssociations(getResultParseInteger(resultSetInfos, "number_of_associations"));
            info.setNumberOfassociationsClass(getResultParseInteger(resultSetInfos, "number_of_associations_class"));

            infos.add(info);
        }

        return infos;
    }

    private static Integer getResultParseInteger(ResultSet resultSetInfos, String nameColumn) {
        try {
            return Integer.parseInt(resultSetInfos.getString(nameColumn));
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Double getResultParseDouble(ResultSet resultSetConventional, String nameColumn) {
        try {
            return Double.parseDouble(resultSetConventional.getString(nameColumn));
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<FunResults> buildFuns(Connection connection, Experiment exp, Execution exec)
            throws SQLException {
        Statement funStatement = connection.createStatement();

        StringBuilder query = new StringBuilder();
        query.append("select * from objectives where execution_id =");
        query.append(exec.getId());
        query.append(" OR (execution_id='' AND experiement_id =");
        query.append(exp.getId());
        query.append(")");

        ResultSet resultSetFuns = funStatement.executeQuery(query.toString());
        List<FunResults> funs = new ArrayList<FunResults>();

        while (resultSetFuns.next()) {
            FunResults fun = new FunResults();
            fun.setExperiement(exp);
            fun.setExecution(exec);
            fun.setIsAll(Integer.parseInt(resultSetFuns.getString("is_all")));
            fun.setObjectives(resultSetFuns.getString("objectives"));

            funs.add(fun);
        }
        return funs;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAlgorithmAndDescription() {
        if ("null".equals(this.description))
            return this.getAlgorithm();

        StringBuilder sb = new StringBuilder();
        sb.append(this.getAlgorithm());
        sb.append(" (");
        sb.append(this.description);
        sb.append(")");

        return sb.toString();
    }

    private String makeQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into experiments (id, name, algorithm, description, created_at) ");
        sb.append("values (");
        sb.append(this.id);
        sb.append(",'");
        sb.append(this.name);
        sb.append("','");
        sb.append(this.algorithm);
        sb.append("','");
        sb.append(this.description);
        sb.append("','");
        sb.append(this.getCreatedAt());
        sb.append("')");
        return sb.toString();
    }

    private String givenId() {
        return Id.generateUniqueId();
    }

    public void save() throws Exception {
        Statement statement = Database.getConnection().createStatement();
        statement.executeUpdate(makeQuery());
        statement.close();
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    private String setCreatedAt() {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return dt.format(new Date()).toString();
    }

    public Collection<Execution> getExecutions() {
        return executions;
    }

    public void setExecutions(Collection<Execution> execs) {
        this.executions = execs;
    }

}
