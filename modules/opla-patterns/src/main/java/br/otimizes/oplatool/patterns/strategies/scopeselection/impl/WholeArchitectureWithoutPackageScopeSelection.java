/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.otimizes.oplatool.patterns.strategies.scopeselection.impl;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.Patterns;
import br.otimizes.oplatool.patterns.models.Scope;
import br.otimizes.oplatool.patterns.strategies.scopeselection.ScopeSelectionStrategy;

/**
 * The Class WholeArchitectureWithoutPackageScopeSelection.
 *
 * @author giovaniguizzo
 */
public class WholeArchitectureWithoutPackageScopeSelection implements ScopeSelectionStrategy {

    @Override
    public Scope selectScope(Architecture architecture, Patterns pattern) {
        Scope scope = new Scope();
        scope.getElements().addAll(architecture.getElements());
        scope.getElements().removeIf(element -> element instanceof Package);
        return scope;
    }
}
