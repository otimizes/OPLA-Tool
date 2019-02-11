package arquitetura.flyweights;

import arquitetura.helpers.ModelHelper;
import arquitetura.helpers.ModelHelperFactory;
import arquitetura.representation.Architecture;
import arquitetura.representation.Variability;
import arquitetura.representation.Variant;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Package;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VariabilityFlyweight {

    private static final VariabilityFlyweight INSTANCE = new VariabilityFlyweight();
    private Architecture architecture;
    private HashMap<String, Variability> variabilities = new HashMap<String, Variability>();
    private ModelHelper modelHelper;

    private Map<String, Map<String, String>> variabilityAttributes = new HashMap<String, Map<String, String>>();

    private Package model;

    private VariabilityFlyweight() {
        modelHelper = ModelHelperFactory.getModelHelper();
    }

    public static VariabilityFlyweight getInstance() {
        return INSTANCE;
    }

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

    public void createVariants() {

        VariantFlyweight variantFlyweight = VariantFlyweight.getInstance();

        for (Variability v : this.getVariabilities()) {
            List<Variant> variants = new ArrayList<Variant>();

            String[] variantsElements = this.variabilityAttributes.get(v.getName()).get("variants").split(",");

            for (String variantElement : variantsElements) {
                if ((variantElement != null) && (variantElement.length() != 0)) {
                    Classifier klasss = modelHelper.getClassByName(variantElement.trim(), this.model);
                    if (klasss != null) {
                        variants.add(variantFlyweight.getOrCreateVariant(klasss));
                    }
                }
            }
//			
            //Variability vari = this.getVariability(v.getName());

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


    public Variability getVariability(String name) {
        return this.variabilities.get(name);
    }

    /**
     * @return the variabilities
     */
    public List<Variability> getVariabilities() {
        return new ArrayList<Variability>(variabilities.values());
    }

    private boolean allowAddingVar(Map<String, String> a) {
        return "true".equalsIgnoreCase(a.get("allowAddingVar")) ? true : false;
    }

    /**
     * @param architecture the architecture to set
     */
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