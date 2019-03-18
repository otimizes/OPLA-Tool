package arquitetura.representation;

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
     * Retorna todos os patterns aplicados para a classe.
     *
     * @return {@link Set<String>}
     */
    public Set<String> getAllPatterns() {
        return Collections.unmodifiableSet(patternsStereotypes);
    }

    /**
     * Setter para os esterótipos dos patterns. Normalmente usado somente pelos
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
        if (!this.patternsStereotypes.contains(pattern.getName())) {
            this.patternsStereotypes.add(pattern.getName());

            // joao
            //System.out.println("\n Classe architecture PatternsOperations - padroes sendo aplicado");
        }
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
        return this.patternsStereotypes.remove(pattern.getName());
    }

}
