package br.otimizes.oplatool.architecture.flyweights;

import br.otimizes.oplatool.architecture.helpers.ModelHelper;
import br.otimizes.oplatool.architecture.helpers.ModelHelperFactory;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Variability;
import br.otimizes.oplatool.architecture.representation.Variant;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Package;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Variability utils
 */
public class VariabilityFlyweight {

    private static final VariabilityFlyweight INSTANCE = new VariabilityFlyweight();
    private Architecture architecture;
    private HashMap<String, Variability> variabilities = new HashMap<>();
    private final ModelHelper modelHelper;

    private final Map<String, Map<String, String>> variabilityAttributes = new HashMap<>();

    private Package model;

    private VariabilityFlyweight() {
        modelHelper = ModelHelperFactory.getModelHelper();
    }

    public static VariabilityFlyweight getInstance() {
        return INSTANCE;
    }

    /**
     * Get or create variability
     *
     * @param owner                 owner
     * @param variabilityAttributes attributes
     * @return variability
     */
    public Variability getOrCreateVariability(String owner, Map<String, String> variabilityAttributes) {
        Variability variability = variabilities.get(variabilityAttributes.get("name"));
        this.variabilityAttributes.put(variabilityAttributes.get("name"), variabilityAttributes);

        if (variability == null) {
            VariantFlyweight variantFlyweight = VariantFlyweight.getInstance();
            variantFlyweight.setArchitecture(architecture);

            variability = new Variability(variabilityAttributes.get("name"),
                    variabilityAttributes.get("minSelection"),
                    variabilityAttributes.get("maxSelection"),
                    variabilityAttributes.get("bindingTime"),
                    allowAddingVar(variabilityAttributes),
                    owner,
                    variabilityAttributes.get("idOwnerPackage"));

            variabilities.put(variability.getName(), variability);
        }

        return variability;
    }

    /**
     * Create variants
     */
    public void createVariants() {
        VariantFlyweight variantFlyweight = VariantFlyweight.getInstance();
        for (Variability v : this.getVariabilities()) {
            List<Variant> variants = new ArrayList<>();

            String[] variantsElements = this.variabilityAttributes.get(v.getName()).get("variants").split(",");

            for (String variantElement : variantsElements) {
                if ((variantElement != null) && (variantElement.length() != 0)) {
                    Classifier klasss = modelHelper.getClassByName(variantElement.trim(), this.model);
                    if (klasss != null) {
                        variants.add(variantFlyweight.getOrCreateVariant(klasss));
                    }
                }
            }
            for (Variant variant : variants) {
                if (!v.getVariants().contains(variant)) {
                    v.getVariants().add(variant);
                }
            }
            for (Variant variantTemp : variants) {
                if (!variantTemp.getVariabilities().contains(v))
                    variantTemp.getVariabilities().add(v);
            }
        }
    }

    /**
     * Get variability
     *
     * @param name name of variability
     * @return variability
     */
    public Variability getVariability(String name) {
        return this.variabilities.get(name);
    }

    /**
     * @return the variabilities
     */
    public List<Variability> getVariabilities() {
        return new ArrayList<Variability>(variabilities.values());
    }

    public void setVariabilities(HashMap<String, Variability> variabilities) {
        this.variabilities = variabilities;
    }

    private boolean allowAddingVar(Map<String, String> a) {
        return "true".equalsIgnoreCase(a.get("allowAddingVar"));
    }

    public void setArchitecture(Architecture architecture) {
        this.architecture = architecture;
    }

    public void resetVariabilities() {
        this.variabilities.clear();
    }

    public void addModel(Package model2) {
        this.model = model2;
    }
}