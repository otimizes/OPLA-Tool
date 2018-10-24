package br.ufpr.dinf.gres.opla.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "experiment_configurations")
public class ExperimentConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "number_of_runs")
    private Long numberOfRuns;

    @Column(name = "max_evaluations")
    private Integer maxEvaluations;

    @Column(name = "crossover_prob")
    private Double crossoverProb;

    @Column(name = "mutation_prob")
    private Double mutationProb;

    @Column(name = "patterns")
    private String patterns;

    @Column(name = "pattern_strategy")
    private String patternStrategy;

    @Column(name = "algorithm")
    private String algorithm;

    @Column(name = "mutation_operators")
    private String mutationOperators;

    @Column(name = "archive_size")
    private Integer archiveSize;

    @Column(name = "population_size")
    private Integer populationSize;

    @Column(name = "profiles_used")
    private String profilesUsed;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getNumberOfRuns() {
        return numberOfRuns;
    }

    public void setNumberOfRuns(Long numberOfRuns) {
        this.numberOfRuns = numberOfRuns;
    }

    public Integer getMaxEvaluations() {
        return maxEvaluations;
    }

    public void setMaxEvaluations(Integer maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    public Double getCrossoverProb() {
        return crossoverProb;
    }

    public void setCrossoverProb(Double crossoverProb) {
        this.crossoverProb = crossoverProb;
    }

    public Double getMutationProb() {
        return mutationProb;
    }

    public void setMutationProb(Double mutationProb) {
        this.mutationProb = mutationProb;
    }

    public String getPatterns() {
        return patterns;
    }

    public void setPatterns(String patterns) {
        this.patterns = patterns;
    }

    public String getPatternStrategy() {
        return patternStrategy;
    }

    public void setPatternStrategy(String patternStrategy) {
        this.patternStrategy = patternStrategy;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getMutationOperators() {
        return mutationOperators;
    }

    public void setMutationOperators(String mutationOperators) {
        this.mutationOperators = mutationOperators;
    }

    public Integer getArchiveSize() {
        return archiveSize;
    }

    public void setArchiveSize(Integer archiveSize) {
        this.archiveSize = archiveSize;
    }

    public Integer getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(Integer populationSize) {
        this.populationSize = populationSize;
    }

    public String getProfilesUsed() {
        return profilesUsed;
    }

    public void setProfilesUsed(String profilesUsed) {
        this.profilesUsed = profilesUsed;
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
        ExperimentConfiguration castOther = (ExperimentConfiguration) other;
        return Objects.equals(id, castOther.id)
                && Objects.equals(numberOfRuns, castOther.numberOfRuns)
                && Objects.equals(maxEvaluations, castOther.maxEvaluations)
                && Objects.equals(crossoverProb, castOther.crossoverProb)
                && Objects.equals(mutationProb, castOther.mutationProb) && Objects.equals(patterns, castOther.patterns)
                && Objects.equals(patternStrategy, castOther.patternStrategy)
                && Objects.equals(algorithm, castOther.algorithm)
                && Objects.equals(mutationOperators, castOther.mutationOperators)
                && Objects.equals(archiveSize, castOther.archiveSize)
                && Objects.equals(populationSize, castOther.populationSize)
                && Objects.equals(profilesUsed, castOther.profilesUsed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numberOfRuns, maxEvaluations, crossoverProb, mutationProb, patterns,
                patternStrategy, algorithm, mutationOperators, archiveSize, populationSize, profilesUsed);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append("experimentId", id)
                .append("numberOfRuns", numberOfRuns).append("maxEvaluations", maxEvaluations)
                .append("crossoverProb", crossoverProb).append("mutationProb", mutationProb)
                .append("patterns", patterns).append("patternStrategy", patternStrategy).append("algorithm", algorithm)
                .append("mutationOperators", mutationOperators).append("archiveSize", archiveSize)
                .append("populationSize", populationSize).append("profilesUsed", profilesUsed).toString();
    }

}
