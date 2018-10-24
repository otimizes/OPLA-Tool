package results;

import metrics.AllMetrics;
import utils.Id;

import java.util.List;

/**
 * Essa classe representa cada execucao de um dado experiementos.
 */
public class Execution {

    private String id;
    private List<InfoResult> infos;
    private List<FunResults> funs;
    private AllMetrics allMetrics;
    private Experiment experiment;
    private long time = 0l;

    public Execution(Experiment experiment) {
        setId(Id.generateUniqueId());
        this.experiment = experiment;
    }

    public Execution(String id, long t) {
        this.id = id;
        this.time = t;
    }

    public List<InfoResult> getInfos() {
        return infos;
    }

    public void setInfos(List<InfoResult> infos) {
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

    public Experiment getExperiement() {
        return this.experiment;
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

    // public List<FunResults> getObjectives() throws ClassNotFoundException,
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
    // fun.setObjectives(r.getString("objectives"));
    // fun.setIsAll(Integer.parseInt(r.getString("is_all")));
    //
    // funs.add(fun);
    // }
    //
    // return funs;
    // }

}
