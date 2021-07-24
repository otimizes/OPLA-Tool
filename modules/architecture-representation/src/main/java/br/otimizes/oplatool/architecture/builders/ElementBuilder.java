package br.otimizes.oplatool.architecture.builders;

import br.otimizes.oplatool.architecture.exceptions.ConcernNotFoundException;
import br.otimizes.oplatool.architecture.helpers.ModelElementHelper;
import br.otimizes.oplatool.architecture.helpers.StereotypeHelper;
import br.otimizes.oplatool.architecture.helpers.XmiHelper;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.Variant;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Element builder
 *
 * @param <T> element
 * @author edipofederle<edipofederle @ gmail.com>
 */
public abstract class ElementBuilder<T extends Element> {

    protected final Architecture architecture;
    private final HashMap<String, T> createdElements = new HashMap<>();
    protected String name;
    protected Boolean isVariationPoint;
    protected Variant variantType;
    protected List<String> concerns;

    public ElementBuilder(Architecture architecture) {
        this.architecture = architecture;
    }

    protected abstract T buildElement(NamedElement modelElement);

    /**
     * Creates a new architectural element
     *
     * @param modelElement element
     * @return created element
     */
    public T create(NamedElement modelElement) {
        initialize();
        inspectStereotypes(modelElement);
        name = modelElement.getName();
        T element = buildElement(modelElement);
        try {
            element.addConcerns(concerns);
        } catch (ConcernNotFoundException e) {
            e.printStackTrace();
        }
        createdElements.put(XmiHelper.getXmiId(modelElement), element);
        return element;
    }

    /**
     * Inspect stereotypes adding if does not have
     *
     * @param modelElement element
     */
    private void inspectStereotypes(NamedElement modelElement) {
        List<Stereotype> allStereotypes = ModelElementHelper.getAllStereotypes(modelElement);
        for (Stereotype stereotype : allStereotypes) {
            verifyConcern(stereotype);
        }
    }

    /**
     * Add stereotype if does not have
     *
     * @param stereotype stereotype
     */
    private void verifyConcern(Stereotype stereotype) {
        if (StereotypeHelper.hasConcern(stereotype))
            concerns.add(stereotype.getName());
    }

    /**
     * Initialize builder
     */
    private void initialize() {
        name = "";
        isVariationPoint = false;
        variantType = null;
        concerns = new ArrayList<>();
    }
}