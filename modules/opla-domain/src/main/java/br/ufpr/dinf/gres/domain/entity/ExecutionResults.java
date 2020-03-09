package br.ufpr.dinf.gres.domain.entity;

import java.util.List;

/**
 * Essa classe representa cada execucao de um dado experiementos.
 */
public class ExecutionResults implements IPersistentDto<Execution> {

    private String id;
    private List<InfoResults> infos;
    private AllMetrics allMetrics;
    private ExperimentResults experimentResults;
    private long time = 0l;
    private int runs;
    private String description;

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
    @Override
    public Execution newPersistentInstance() {
        Execution execution = new Execution();
        execution.setId(Long.valueOf(this.getId()));
        execution.setTime(this.getTime());
        execution.setDescription(this.getDescription());
        execution.setExperiment(this.getExperiement().newPersistentInstance());
        return execution;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
