package br.ufpr.dinf.gres.opla.entity.metric;

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

import br.ufpr.dinf.gres.opla.entity.Execution;
import br.ufpr.dinf.gres.opla.entity.Experiment;

@Entity
@Table(name = "ConventionalMetrics")
public class ConventionalMetric implements GenericMetric {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "sum_cohesion")
	private String sumCohesion;

	@Column(name = "macAggregation")
	private String macAggregation;

	@Column(name = "cohesion")
	private String cohesion;

	@Column(name = "meanDepComps")
	private String meanDepComps;

	@Column(name = "meanNumOps")
	private String meanNumOps;

	@Column(name = "sumClassesDepIn")
	private String sumClassesDepIn;

	@Column(name = "sumClassesDepOut")
	private String sumClassesDepOut;

	@Column(name = "sumDepIn")
	private String sumDepIn;

	@Column(name = "sumDepOut")
	private String sumDepOut;

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

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSumCohesion() {
		return sumCohesion;
	}

	public void setSumCohesion(String sumCohesion) {
		this.sumCohesion = sumCohesion;
	}

	public String getMacAggregation() {
		return macAggregation;
	}

	public void setMacAggregation(String macAggregation) {
		this.macAggregation = macAggregation;
	}

	public String getCohesion() {
		return cohesion;
	}

	public void setCohesion(String cohesion) {
		this.cohesion = cohesion;
	}

	public String getMeanDepComps() {
		return meanDepComps;
	}

	public void setMeanDepComps(String meanDepComps) {
		this.meanDepComps = meanDepComps;
	}

	public String getMeanNumOps() {
		return meanNumOps;
	}

	public void setMeanNumOps(String meanNumOps) {
		this.meanNumOps = meanNumOps;
	}

	public String getSumClassesDepIn() {
		return sumClassesDepIn;
	}

	public void setSumClassesDepIn(String sumClassesDepIn) {
		this.sumClassesDepIn = sumClassesDepIn;
	}

	public String getSumClassesDepOut() {
		return sumClassesDepOut;
	}

	public void setSumClassesDepOut(String sumClassesDepOut) {
		this.sumClassesDepOut = sumClassesDepOut;
	}

	public String getSumDepIn() {
		return sumDepIn;
	}

	public void setSumDepIn(String sumDepIn) {
		this.sumDepIn = sumDepIn;
	}

	public String getSumDepOut() {
		return sumDepOut;
	}

	public void setSumDepOut(String sumDepOut) {
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
