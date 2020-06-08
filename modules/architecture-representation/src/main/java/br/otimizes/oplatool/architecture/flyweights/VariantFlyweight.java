package br.otimizes.oplatool.architecture.flyweights;

import br.otimizes.oplatool.architecture.helpers.StereotypeHelper;
import br.otimizes.oplatool.architecture.helpers.XmiHelper;
import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.representation.*;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Stereotype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Variant utils
 */
public class VariantFlyweight extends XmiHelper {

    private static final VariantFlyweight INSTANCE = new VariantFlyweight();
    private Architecture architecture;
    private HashMap<String, Variant> variants = new HashMap<String, Variant>();


    private VariantFlyweight() {
    }

    public static VariantFlyweight getInstance() {
        return INSTANCE;
    }

    /**
     * Get or create variant
     *
     * @param classifier classifier
     * @return variant
     */
    public Variant getOrCreateVariant(Classifier classifier) {

        Variant variant = variants.get(classifier.getName());
        if (variant == null) {
            Stereotype variantType = StereotypeHelper.getVariantType(classifier);
            VariantType type = VariantType.getByName(variantType.getName());

            if ((type != null)) {
                Element variantElement;
                try {
                    variantElement = architecture.findElementById(getXmiId(classifier));

                    String rootVp = StereotypeHelper.getValueOfAttribute(classifier, variantType, "rootVP");
                    VariabilityFlyweight.getInstance().getVariabilities();
                    variant = Variant.createVariant().withName(classifier.getName())
                            .withVariantType(variantType.getName())
                            .andRootVp(rootVp)
                            .build();
                    variant.setVariantElement(variantElement);
                    variantElement.setVariant(variant);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                String[] variabilitiesForVariant = StereotypeHelper.getValueOfAttribute(classifier, variantType, "variabilities").split(",");
                for (String variability : variabilitiesForVariant) {
                    Variability variabilityVariant = VariabilityFlyweight.getInstance().getVariability(variability.trim());
                    if (variabilityVariant != null) {
                        if (!variabilityVariant.getVariants().contains(variant)) {
                            variabilityVariant.getVariants().add(variant);
                            variant.getVariabilities().add(variabilityVariant);
                        }
                    }
                }
                variants.put(variant.getName(), variant);
            }

        }

        return variant;
    }

    public Variant getVariant(String name) {
        return variants.get(name);
    }


    public List<Variant> getVariants() {
        return new ArrayList<Variant>(variants.values());
    }

    public void setVariants(HashMap<String, Variant> variants) {
        this.variants = variants;
    }

    public void setArchitecture(Architecture architecture) {
        this.architecture = architecture;
    }

    public void resetVariants() {
        this.variants.clear();
    }

}
