package br.ufpr.dinf.gres.domain.entity.metric;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;

@Entity
@Table(name = "conventional_metrics")
public class ConventionalMetric implements GenericMetric {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	@Column(name = "sum_cohesion")
	private Double sumCohesion;

	@Column(name = "macAggregation")
	private Double macAggregation;

	@Column(name = "cohesion")
	private Double cohesion;

	@Column(name = "meanDepComps")
	private Double meanDepComps;

	@Column(name = "meanNumOps")
	private Double meanNumOps;

	@Column(name = "sumClassesDepIn")
	private Double sumClassesDepIn;

	@Column(name = "sumClassesDepOut")
	private Double sumClassesDepOut;

	@Column(name = "sumDepIn")
	private Double sumDepIn;

	@Column(name = "sumDepOut")
	private Double sumDepOut;

	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "execution_id")
	private Execution execution;

	@Column(name = "is_all")
	private Integer isAll;

	@ManyToOne
	@JoinColumn(name = "experiement_id")
	private Experiment experiment;

	@Column(name = "id_solution")
	private String idSolution;

	public ConventionalMetric(String idSolution, Execution execution, Experiment experiement) {
		this.idSolution = idSolution;
		this.execution = execution;
		this.experiment = experiement;
	}

    @Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Double getSumCohesion() {
		return sumCohesion;
	}

	public void setSumCohesion(Double sumCohesion) {
		this.sumCohesion = sumCohesion;
	}

	public Double getMacAggregation() {
		return macAggregation;
	}

	public void setMacAggregation(Double macAggregation) {
		this.macAggregation = macAggregation;
	}

	public Double getCohesion() {
		return cohesion;
	}

	public void setCohesion(Double cohesion) {
		this.cohesion = cohesion;
	}

	public Double getMeanDepComps() {
		return meanDepComps;
	}

	public void setMeanDepComps(Double meanDepComps) {
		this.meanDepComps = meanDepComps;
	}

	public Double getMeanNumOps() {
		return meanNumOps;
	}

	public void setMeanNumOps(Double meanNumOps) {
		this.meanNumOps = meanNumOps;
	}

	public Double getSumClassesDepIn() {
		return sumClassesDepIn;
	}

	public void setSumClassesDepIn(Double sumClassesDepIn) {
		this.sumClassesDepIn = sumClassesDepIn;
	}

	public Double getSumClassesDepOut() {
		return sumClassesDepOut;
	}

	public void setSumClassesDepOut(Double sumClassesDepOut) {
		this.sumClassesDepOut = sumClassesDepOut;
	}

	public Double getSumDepIn() {
		return sumDepIn;
	}

	public void setSumDepIn(Double sumDepIn) {
		this.sumDepIn = sumDepIn;
	}

	public Double getSumDepOut() {
		return sumDepOut;
	}

	public void setSumDepOut(Double sumDepOut) {
		this.sumDepOut = sumDepOut;
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

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

	public String getIdSolution() {
		return idSolution;
	}

	public void setIdSolution(String idSolution) {
		this.idSolution = idSolution;
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
		ConventionalMetric castOther = (ConventionalMetric) other;
		return Objects.equals(id, castOther.id) && Objects.equals(sumCohesion, castOther.sumCohesion)
				&& Objects.equals(macAggregation, castOther.macAggregation)
				&& Objects.equals(cohesion, castOther.cohesion) && Objects.equals(meanDepComps, castOther.meanDepComps)
				&& Objects.equals(meanNumOps, castOther.meanNumOps)
				&& Objects.equals(sumClassesDepIn, castOther.sumClassesDepIn)
				&& Objects.equals(sumClassesDepOut, castOther.sumClassesDepOut)
				&& Objects.equals(sumDepIn, castOther.sumDepIn) && Objects.equals(sumDepOut, castOther.sumDepOut)
				&& Objects.equals(execution, castOther.execution) && Objects.equals(isAll, castOther.isAll)
				&& Objects.equals(experiment, castOther.experiment) && Objects.equals(idSolution, castOther.idSolution);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, sumCohesion, macAggregation, cohesion, meanDepComps, meanNumOps, sumClassesDepIn,
				sumClassesDepOut, sumDepIn, sumDepOut, execution, isAll, experiment, idSolution);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append("id", id).append("sumCohesion", sumCohesion)
				.append("macAggregation", macAggregation).append("cohesion", cohesion)
				.append("meanDepComps", meanDepComps).append("meanNumOps", meanNumOps)
				.append("sumClassesDepIn", sumClassesDepIn).append("sumClassesDepOut", sumClassesDepOut)
				.append("sumDepIn", sumDepIn).append("sumDepOut", sumDepOut).append("executionId", execution)
				.append("isAll", isAll).append("experimentId", experiment).append("idSolution", idSolution).toString();
	}

}
