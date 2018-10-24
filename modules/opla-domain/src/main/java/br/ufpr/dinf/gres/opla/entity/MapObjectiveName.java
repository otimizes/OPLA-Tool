package br.ufpr.dinf.gres.opla.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "map_objectives_names")
public class MapObjectiveName implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "names")
	private String names;

	@ManyToOne
	@JoinColumn(name = "experiment_id", nullable = false)
	private Experiment experiment;

	@Transient
	private BigDecimal value;

	protected MapObjectiveName() {
		// Empty
	}

	public MapObjectiveName(BigDecimal value, String name) {
		this.value = value;
		this.names = name.toUpperCase();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
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
		MapObjectiveName castOther = (MapObjectiveName) other;
		return Objects.equals(id, castOther.id) && Objects.equals(names, castOther.names)
				&& Objects.equals(experiment, castOther.experiment);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, names, experiment);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append("id", id).append("names", names)
				.append("experiment", experiment).toString();
	}

}
