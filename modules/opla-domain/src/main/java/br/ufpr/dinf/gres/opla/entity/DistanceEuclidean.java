package br.ufpr.dinf.gres.opla.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "distance_euclidean")
public class DistanceEuclidean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "solution_name")
    private String solutionName;

    @ManyToOne
    @JoinColumn(name = "experiment_id", nullable = false)
    private Experiment expediment;

    @Column(name = "ed")
    private Double ed;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSolutionName() {
        return solutionName;
    }

    public void setSolutionName(String solutionName) {
        this.solutionName = solutionName;
    }

    public Experiment getExpediment() {
        return expediment;
    }

    public void setExpediment(Experiment expediment) {
        this.expediment = expediment;
    }

    public Double getEd() {
        return ed;
    }

    public void setEd(Double ed) {
        this.ed = ed;
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
        DistanceEuclidean castOther = (DistanceEuclidean) other;
        return Objects.equals(id, castOther.id) && Objects.equals(solutionName, castOther.solutionName)
                && Objects.equals(expediment, castOther.expediment) && Objects.equals(ed, castOther.ed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, solutionName, expediment, ed);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append("id", id)
                .append("solutionName", solutionName).append("expedimentId", expediment).append("ed", ed).toString();
    }

}
