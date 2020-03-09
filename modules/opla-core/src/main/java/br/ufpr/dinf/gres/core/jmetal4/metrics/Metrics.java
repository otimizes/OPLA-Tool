package br.ufpr.dinf.gres.core.jmetal4.metrics;


import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;

public abstract class Metrics {

    private String idSolution;
    private Experiment experiement;
    private Execution Execution;
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
        return Execution;
    }

    public void setExecution(Execution Execution) {
        this.Execution = Execution;
    }

    public Integer getIsAll() {
        return isAll;
    }

    public void setIsAll(Integer isAll) {
        this.isAll = isAll;
    }


}
