/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.dinf.gres.patterns.strategies.scopeselection.impl;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.architecture.representation.Patterns;
import br.ufpr.dinf.gres.patterns.models.Scope;
import br.ufpr.dinf.gres.patterns.strategies.scopeselection.ScopeSelectionStrategy;

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
            if (element instanceof br.ufpr.dinf.gres.architecture.representation.Package) {
                it.remove();
            }
        }
        return scope;
    }

}
