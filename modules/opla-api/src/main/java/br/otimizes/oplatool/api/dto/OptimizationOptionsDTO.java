package br.otimizes.oplatool.api.dto;

import br.otimizes.oplatool.api.gateway.OptimizationAlgorithms;
import br.otimizes.oplatool.architecture.builders.ArchitectureBuilders;
import br.otimizes.oplatool.architecture.representation.Patterns;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctions;
import br.otimizes.oplatool.core.jmetal4.operators.CrossoverOperators;
import br.otimizes.oplatool.core.jmetal4.operators.MutationOperators;

import java.util.Arrays;
import java.util.List;

public class OptimizationOptionsDTO {
    public List<OptimizationAlgorithms> algorithms = Arrays.asList(OptimizationAlgorithms.values());
    public List<ObjectiveFunctions> objectiveFunctions = Arrays.asList(ObjectiveFunctions.values());
    public List<MutationOperators> mutationOperators = Arrays.asList(MutationOperators.values());
    public List<CrossoverOperators> crossoverOperators = Arrays.asList(CrossoverOperators.values());
    public List<Patterns> designPatterns = Arrays.asList(Patterns.values());
    public List<ArchitectureBuilders> architectureBuilders = Arrays.asList(ArchitectureBuilders.values());
}
