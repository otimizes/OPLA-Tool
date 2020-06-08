package br.otimizes.oplatool.architecture.papyrus.touml;

import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Concern;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.Interface;

import java.util.Set;

/**
 * Stereotype operations
 */
public class StereotypeOperations {

    private static final String PATTERNS = "br.otimizes.oplatool.patterns";
    private static final String CONCERNS = "concerns";
    private ElementXmiGenerator elementXmiGenerator;

    public StereotypeOperations(DocumentManager documentManager) {
        this.elementXmiGenerator = new ElementXmiGenerator(documentManager, null);
    }

    public StereotypeOperations withStereotypes(Set<Concern> concerns, String id) {
        if (!concerns.isEmpty()) {
            for (final Concern concern : concerns)
                elementXmiGenerator.generateConcern(concern.getName(), id, CONCERNS);
        }
        return this;
    }

    public StereotypeOperations withStereotype(Concern concern, String id) {
        elementXmiGenerator.generateConcern(concern.getName(), id, CONCERNS);
        return this;
    }

    public void withPatternsStereotype(Element klass) {
        if (klass instanceof Class) {
            if (((Class) klass).getPatternsOperations() != null) {
                for (String pattern : ((Class) klass).getPatternsOperations().getAllPatterns())
                    elementXmiGenerator.generateConcern(pattern, klass.getId(), PATTERNS);
            }
        }
        if (klass instanceof Interface) {
            if (((Interface) klass).getPatternsOperations() != null) {
                for (String pattern : ((Interface) klass).getPatternsOperations().getAllPatterns())
                    elementXmiGenerator.generateConcern(pattern, klass.getId(), PATTERNS);
            }
        }
    }

}