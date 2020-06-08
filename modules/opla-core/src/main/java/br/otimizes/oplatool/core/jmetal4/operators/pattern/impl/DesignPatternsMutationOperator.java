/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.otimizes.oplatool.core.jmetal4.operators.pattern.impl;

import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.operators.pattern.AbstractMutationOperator;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Patterns;
import br.otimizes.oplatool.core.jmetal4.util.PseudoRandom;
import br.otimizes.oplatool.patterns.designpatterns.DesignPattern;
import br.otimizes.oplatool.patterns.models.Scope;
import br.otimizes.oplatool.patterns.repositories.ArchitectureRepository;
import br.otimizes.oplatool.patterns.strategies.designpatternselection.DesignPatternSelectionStrategy;
import br.otimizes.oplatool.patterns.strategies.designpatternselection.defaultstrategy.RandomDesignPatternSelection;
import br.otimizes.oplatool.patterns.strategies.scopeselection.ScopeSelectionStrategy;
import br.otimizes.oplatool.patterns.strategies.scopeselection.defaultstrategy.RandomScopeSelection;
import org.apache.log4j.Priority;

import java.util.Map;

/**
 * Class that call the design patterns operators
 */
public class DesignPatternsMutationOperator extends AbstractMutationOperator {

    private static final long serialVersionUID = 1L;

    protected final ScopeSelectionStrategy scopeSelectionStrategy;
    protected final DesignPatternSelectionStrategy designPatternSelectionStrategy;

    public DesignPatternsMutationOperator(Map<String, Object> parameters, ScopeSelectionStrategy scopeSelectionStrategy, DesignPatternSelectionStrategy designPatternSelectionStrategy) {
        super(parameters);
        this.scopeSelectionStrategy = scopeSelectionStrategy;
        this.designPatternSelectionStrategy = designPatternSelectionStrategy;
    }

    @Override
    public boolean hookMutation(Solution solution, Double probability) throws Exception {
        boolean applied = false;
        if (solution.getDecisionVariables()[0].getVariableType() == java.lang.Class.forName(Architecture.ARCHITECTURE_TYPE)) {
            if (PseudoRandom.randDouble() < probability) {
                Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
                if (scopeSelectionStrategy == null && designPatternSelectionStrategy == null) {
                    this.mutateArchitecture(arch);
                } else if (scopeSelectionStrategy == null) {
                    this.mutateArchitecture(arch, designPatternSelectionStrategy);
                } else if (designPatternSelectionStrategy == null) {
                    this.mutateArchitecture(arch, scopeSelectionStrategy);
                } else {
                    this.mutateArchitecture(arch, scopeSelectionStrategy, designPatternSelectionStrategy);
                }
                applied = true;
            }
        }
        if (!this.isValidSolution(((Architecture) solution.getDecisionVariables()[0]))) {
            Architecture clone = ((Architecture) solution.getDecisionVariables()[0]).deepClone();
            solution.getDecisionVariables()[0] = clone;
        }
        return applied;
    }

    public Architecture mutateArchitecture(Architecture architecture) {
        RandomScopeSelection rss = new RandomScopeSelection();
        RandomDesignPatternSelection rdps = new RandomDesignPatternSelection();
        return mutateArchitecture(architecture, rss, rdps);
    }

    public Architecture mutateArchitecture(Architecture architecture, ScopeSelectionStrategy scopeSelectionStartegy) {
        RandomDesignPatternSelection rdps = new RandomDesignPatternSelection();
        return mutateArchitecture(architecture, scopeSelectionStartegy, rdps);
    }

    public Architecture mutateArchitecture(Architecture architecture, DesignPatternSelectionStrategy designPatternSelectionStrategy) {
        RandomScopeSelection rss = new RandomScopeSelection();
        return mutateArchitecture(architecture, rss, designPatternSelectionStrategy);
    }

    public Architecture mutateArchitecture(Architecture architecture, ScopeSelectionStrategy scopeSelectionStartegy, DesignPatternSelectionStrategy designPatternSelectionStrategy) {
        ArchitectureRepository.setCurrentArchitecture(architecture);
        DesignPattern designPattern = designPatternSelectionStrategy.selectDesignPattern();
        if (designPattern != null) {
            Scope scope = scopeSelectionStartegy.selectScope(architecture, Patterns.valueOf(designPattern.getName().toUpperCase()));
            if (designPattern.randomlyVerifyAsPSOrPSPLA(scope)) {
                if (designPattern.apply(scope)) {
                    LOGGER.log(Priority.INFO,
                            "Design Pattern " + designPattern.getName() + " applied to scope " + scope.getElements().toString() + " successfully!");
                }
            }
        }
        return architecture;
    }

}
