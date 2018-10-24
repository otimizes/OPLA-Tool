package metrics;


import results.Execution;
import results.Experiment;

public abstract class Metrics {

    private String idSolution;
    private Experiment experiement;
    private Execution execution;
    private Integer isAll;

    public String getIdSolution() {
        return idSolution;
    }

    public void setIdSolution(String idSolution) {
        this.idSolution = idSolution;
    }

    public Experiment getExperiement() {
        return experiement;
    }

    public void setExperiement(Experiment experiement) {
        this.experiement = experiement;
    }

    public Execution getExecution() {
        return execution;
    }

    public void setExecution(Execution execution) {
        this.execution = execution;
    }

    public Integer getIsAll() {
        return isAll;
    }

    public void setIsAll(Integer isAll) {
        this.isAll = isAll;
    }


}
