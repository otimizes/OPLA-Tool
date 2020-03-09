package br.ufpr.dinf.gres.domain.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

import br.ufpr.dinf.gres.domain.util.IdUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "executions")
public class Execution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "experiement_id", nullable = false)
    private Experiment experiment;

    @Column(name = "time")
    private Long time;

    @Column(name = "runs")
    private int runs;

    @Transient
    private List<Info> infos;
    @Transient
    private AllMetrics allMetrics;

    public Execution() {
    }

    public Execution(Experiment experimentResults) {
        setId(IdUtil.generateUniqueId());
        this.experiment = experimentResults;
    }


    public Execution(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public Date getTime() {
        return new Date(time);
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!getClass().equals(other.getClass())) {
            return false;
        }
        Execution castOther = (Execution) other;
        return Objects.equals(id, castOther.id) && Objects.equals(description, castOther.description)
                && Objects.equals(experiment, castOther.experiment) && Objects.equals(time, castOther.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, experiment, time);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append("id", id).append("description", description)
                .append("experimentId", experiment).append("time", time).toString();
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public List<Info> getInfos() {
        return infos;
    }

    public void setInfos(List<Info> infos) {
        this.infos = infos;
    }

    public AllMetrics getAllMetrics() {
        return allMetrics;
    }

    public void setAllMetrics(AllMetrics allMetrics) {
        this.allMetrics = allMetrics;
    }
}
