package br.ufpr.dinf.gres.core.jmetal4.metrics;


import br.ufpr.dinf.gres.core.jmetal4.results.ExecutionResults;
import br.ufpr.dinf.gres.core.jmetal4.results.ExperimentResults;

public abstract class Metrics {

    private String idSolution;
    private ExperimentResults experiement;
    private ExecutionResults executionResults;
    private Integer isAll;

    public String getIdSolution() {
        return idSolution;
    }

    public void setIdSolution(String idSolution) {
        this.idSolution = idSolution;
    }

    public ExperimentResults getExperiement() {
        return experiement;
    }

    public void setExperiement(ExperimentResults experiement) {
        this.experiement = experiement;
    }

    public ExecutionResults getExecutionResults() {
        return executionResults;
    }

    public void setExecutionResults(ExecutionResults executionResults) {
        this.executionResults = executionResults;
    }

    public Integer getIsAll() {
        return isAll;
    }

    public void setIsAll(Integer isAll) {
        this.isAll = isAll;
    }


}
