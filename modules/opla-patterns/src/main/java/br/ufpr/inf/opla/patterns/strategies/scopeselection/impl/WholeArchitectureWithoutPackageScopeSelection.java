/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.strategies.scopeselection.impl;

import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import arquitetura.representation.Patterns;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.strategies.scopeselection.ScopeSelectionStrategy;

import java.util.Iterator;

/**
 * @author giovaniguizzo
 */
public class WholeArchitectureWithoutPackageScopeSelection implements ScopeSelectionStrategy {

    @Override
    public Scope selectScope(Architecture architecture, Patterns pattern) {
        Scope scope = new Scope();
        scope.getElements().addAll(architecture.getElements());
        for (Iterator<Element> it = scope.getElements().iterator(); it.hasNext(); ) {
            Element element = it.next();
            if (element instanceof arquitetura.representation.Package) {
                it.remove();
            }
        }
        return scope;
    }

}
