/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.otimizes.oplatool.patterns.strategies.scopeselection.impl;

import java.util.Iterator;

import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.patterns.models.Scope;
import br.otimizes.oplatool.patterns.strategies.scopeselection.ScopeSelectionStrategy;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.Patterns;

/**
 * The Class WholeArchitectureWithoutPackageScopeSelection.
 *
 * @author giovaniguizzo
 */
public class WholeArchitectureWithoutPackageScopeSelection implements ScopeSelectionStrategy {

    /**
     * Select scope.
     *
     * @param architecture the architecture
     * @param pattern the pattern
     * @return the scope
     */
    @Override
    public Scope selectScope(Architecture architecture, Patterns pattern) {
        Scope scope = new Scope();
        scope.getElements().addAll(architecture.getElements());
        for (Iterator<Element> it = scope.getElements().iterator(); it.hasNext(); ) {
            Element element = it.next();
            if (element instanceof Package) {
                it.remove();
            }
        }
        return scope;
    }

}
