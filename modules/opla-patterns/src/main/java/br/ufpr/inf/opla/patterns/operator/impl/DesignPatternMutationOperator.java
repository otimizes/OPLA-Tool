/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.operator.impl;

import arquitetura.representation.Architecture;
import arquitetura.representation.Patterns;
import br.ufpr.inf.opla.patterns.designpatterns.DesignPattern;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.operator.AbstractMutationOperator;
import br.ufpr.inf.opla.patterns.repositories.ArchitectureRepository;
import br.ufpr.inf.opla.patterns.strategies.designpatternselection.DesignPatternSelectionStrategy;
import br.ufpr.inf.opla.patterns.strategies.designpatternselection.defaultstrategy.RandomDesignPatternSelection;
import br.ufpr.inf.opla.patterns.strategies.scopeselection.ScopeSelectionStrategy;
import br.ufpr.inf.opla.patterns.strategies.scopeselection.defaultstrategy.RandomScopeSelection;
import jmetal4.core.Solution;
import jmetal4.problems.OPLA;
import jmetal4.util.PseudoRandom;
import org.apache.log4j.Priority;

import java.util.Map;

/**
 * @author giovaniguizzo
 */
public class DesignPatternMutationOperator extends AbstractMutationOperator {

    private static final long serialVersionUID = 1L;

    protected final ScopeSelectionStrategy scopeSelectionStrategy;
    protected final DesignPatternSelectionStrategy designPatternSelectionStrategy;

    public DesignPatternMutationOperator(Map<String, Object> parameters, ScopeSelectionStrategy scopeSelectionStrategy, DesignPatternSelectionStrategy designPatternSelectionStrategy) {
        super(parameters);
        this.scopeSelectionStrategy = scopeSelectionStrategy;
        this.designPatternSelectionStrategy = designPatternSelectionStrategy;
    }

    @Override
    protected boolean hookMutation(Solution solution, Double probability) throws Exception {
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
            OPLA.contDiscardedSolutions_++;
            LOGGER.log(Priority.INFO, "Invalid Solution. Reverting Modifications.");
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
        Scope scope = scopeSelectionStartegy.selectScope(architecture, Patterns.valueOf(designPattern.getName().toUpperCase()));
        if (designPattern.randomlyVerifyAsPSOrPSPLA(scope)) {
            if (designPattern.apply(scope)) {
                LOGGER.log(Priority.INFO,
                        "Design Pattern " + designPattern.getName() + " applied to scope " + scope.getElements().toString() + " successfully!");
            }
        }
        return architecture;
    }

}
