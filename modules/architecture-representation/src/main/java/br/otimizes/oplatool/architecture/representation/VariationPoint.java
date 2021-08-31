package br.otimizes.oplatool.architecture.representation;


import br.otimizes.oplatool.architecture.exceptions.VariationPointElementTypeErrorException;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Variation point representation
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class VariationPoint {

    private Element variationPointElement;
    private final List<Variant> variants;
    private final List<Variability> variabilities = new ArrayList<>();
    private final String bindingTime;

    public VariationPoint(Element variationPointElement, List<Variant> variants, String bindingTime)
            throws VariationPointElementTypeErrorException {
        if (variationPointElement == null)
            throw new InvalidParameterException("A variation point must have an element");
        if (!variationPointElementIsAInterfaceOrClass(variationPointElement)) {
            throw new VariationPointElementTypeErrorException();
        }
        this.variationPointElement = variationPointElement;
        this.variationPointElement.setVariationPoint(this);
        this.variants = variants;
        this.bindingTime = bindingTime;
    }

    private boolean variationPointElementIsAInterfaceOrClass(Element variationPointElement) {
        return (variationPointElement instanceof Class) || (variationPointElement instanceof Interface);
    }

    public Element getVariationPointElement() {
        return variationPointElement;
    }

    public List<Variant> getVariants() {
        return variants;
    }

    public int getNumberOfVariants() {
        return this.variants.size();
    }

    public List<Variability> getVariabilities() {
        return variabilities;
    }

    public String getBindingTime() {
        return bindingTime;
    }

    public void replaceVariationPointElement(Element newVPElement) {
        this.variationPointElement = newVPElement;
    }
}
