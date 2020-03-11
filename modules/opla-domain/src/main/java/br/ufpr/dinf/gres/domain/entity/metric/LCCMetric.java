package br.ufpr.dinf.gres.domain.entity.metric;

import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Entity
@Table(name = "lcc_metrics")
public class LCCMetric {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "execution_id")
    private Execution execution;

    @ManyToOne
    @JoinColumn(name = "experiement_id", nullable = false)
    private Experiment experiment;

    @Column(name = "is_all")
    private Integer isAll;

    @Column(name = "id_solution")
    private String idSolution;


    @Column(name = "lcc")
    private Double lcc;

    public LCCMetric(Double lcc) {
        this.lcc = lcc;
    }

    public LCCMetric(String idSolution, Execution execution, Experiment experiement) {
        this.idSolution = idSolution;
        this.execution = execution;
        this.experiment = experiement;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Execution getExecution() {
        return execution;
    }

    public void setExecution(Execution execution) {
        this.execution = execution;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public Integer getIsAll() {
        return isAll;
    }

    public void setIsAll(Integer isAll) {
        this.isAll = isAll;
    }

    public String getIdSolution() {
        return idSolution;
    }

    public void setIdSolution(String idSolution) {
        this.idSolution = idSolution;
    }

    public Double getLcc() {
        return lcc;
    }

    public void setLcc(Double lcc) {
        this.lcc = lcc;
    }
}
