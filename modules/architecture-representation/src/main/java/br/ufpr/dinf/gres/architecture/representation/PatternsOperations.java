package br.ufpr.dinf.gres.architecture.representation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PatternsOperations {

    private Set<String> patternsStereotypes = new HashSet<String>();

    public PatternsOperations(Set<String> listPatterns) {
        this.patternsStereotypes = listPatterns;
    }

    public PatternsOperations() {
    }

    /**
     * Retorna todos os br.ufpr.dinf.gres.patterns aplicados para a classe.
     *
     * @return {@link Set<String>}
     */
    public Set<String> getAllPatterns() {
        return Collections.unmodifiableSet(patternsStereotypes);
    }

    /**
     * Setter para os esterótipos dos br.ufpr.dinf.gres.patterns. Normalmente usado somente pelos
     * builers.
     *
     * @param patternsStereotypes
     */
    public void setPatternsStereotypes(Set<String> patternsStereotypes) {
        this.patternsStereotypes = patternsStereotypes;
    }

    /**
     * Aplica um Pattern.<br/>
     * <br/>
     * <p>
     * Ex:<br/>
     * <p>
     * {@code klass.applyPattern(Patterns.FACADE}
     *
     * @param pattern - Algum ENUM definido em {@link PatternsOperations}
     */
    public void applyPattern(Patterns pattern) {
        // joao
        //System.out.println("\n Classe architecture PatternsOperations - padroes sendo aplicado");
        this.patternsStereotypes.add(pattern.toString());
    }

    /**
     * Retorna se a classe tem ou não estereótipo de algum padrão aplicado
     *
     * @return {@link boolean}
     */
    public boolean hasPatternApplied() {
        return !this.patternsStereotypes.isEmpty();
    }

    public boolean removePattern(Patterns pattern) {
        return this.patternsStereotypes.remove(pattern.toString());
    }

}
