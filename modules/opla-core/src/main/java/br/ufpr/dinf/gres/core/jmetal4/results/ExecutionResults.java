package br.ufpr.dinf.gres.core.jmetal4.results;

import br.ufpr.dinf.gres.core.jmetal4.metrics.AllMetrics;
import br.ufpr.dinf.gres.core.jmetal4.util.Id;

import java.util.List;

/**
 * Essa classe representa cada execucao de um dado experiementos.
 */
public class ExecutionResults {

    private String id;
    private List<InfoResults> infos;
    private List<FunResults> funs;
    private AllMetrics allMetrics;
    private ExperimentResults experimentResults;
    private long time = 0l;
    private int runs;

    public ExecutionResults(ExperimentResults experimentResults) {
        setId(Id.generateUniqueId());
        this.experimentResults = experimentResults;
    }


    public ExecutionResults(String id, long t) {

        this.id = id;
        this.time = t;
    }

    public List<InfoResults> getInfos() {
        return infos;
    }

    public void setInfos(List<InfoResults> infos) {
        this.infos = infos;
    }

    public List<FunResults> getFuns() {
        return funs;
    }

    public void setFuns(List<FunResults> funResults) {
        this.funs = funResults;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ExperimentResults getExperiement() {
        return this.experimentResults;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long estimatedTime) {
        this.time = estimatedTime;
    }

    public AllMetrics getAllMetrics() {
        return this.allMetrics;
    }

    public void setAllMetrics(AllMetrics allMetrics) {
        this.allMetrics = allMetrics;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public ExperimentResults getExperimentResults() {
        return experimentResults;
    }

    public void setExperimentResults(ExperimentResults experimentResults) {
        this.experimentResults = experimentResults;
    }

    // public List<FunResults> getAttributes() throws ClassNotFoundException,
    // SQLException, MissingConfigurationException {
    // ResultSet r = null;
    // r =
    // Database.getConnection().executeQuery("select id, objectives, is_all from objectives where execution_id = "
    // + this.id);
    //
    // List<FunResults> funs = new ArrayList<FunResults>();
    //
    // while(r.next()){
    // FunResults fun = new FunResults();
    // fun.setExecution(this);
    // fun.setExperiement(this.experiment);
    // fun.setAttributes(r.getString("objectives"));
    // fun.setIsAll(Integer.parseInt(r.getString("is_all")));
    //
    // funs.add(fun);
    // }
    //
    // return funs;
    // }

}
