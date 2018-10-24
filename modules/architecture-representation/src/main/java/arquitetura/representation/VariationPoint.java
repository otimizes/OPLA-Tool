package arquitetura.representation;


import arquitetura.exceptions.VariationPointElementTypeErrorException;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class VariationPoint {

    private Element variationPointElement;
    private List<Variant> variants;
    private List<Variability> variabilities = new ArrayList<Variability>();
    private String bindingTime;

    /**
     * @param variationPointElement {@link Element}
     * @param variants              {@link List}<{@link Element}>
     * @param bindingTime
     * @param type
     * @throws VariationPointElementTypeErrorException
     */
    public VariationPoint(Element variationPointElement, List<Variant> variants, String bindingTime) throws VariationPointElementTypeErrorException {
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
        if ((variationPointElement instanceof Class) || (variationPointElement instanceof Interface))
            return true;
        return false;
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

    /**
     * @return the variabilities
     */
    public List<Variability> getVariabilities() {
        return variabilities;
    }

    /**
     * @return the bindingTime
     */
    public String getBindingTime() {
        return bindingTime;
    }

    public void replaceVariationPointElement(Element newVPElement) {
        this.variationPointElement = newVPElement;
    }

//	@Override
//	public String toString() {
//		StringBuilder builder = new StringBuilder();
//				
//		if (!getVariants().isEmpty()) {
//			builder.append("Variants: ");
//			Element[] variantsArray = getVariants().toArray(new Element[0]);
//			for (int i = 0; i < variantsArray.length; i++) {
//				if (i > 0)
//					builder.append(", ");
//				builder.append(variantsArray[i].getName());
//			}
//		}
//		return builder.toString();
//	}
}
