package br.ufpr.dinf.gres.architecture.flyweights;

import br.ufpr.dinf.gres.architecture.exceptions.VariationPointElementTypeErrorException;
import br.ufpr.dinf.gres.architecture.helpers.ModelHelper;
import br.ufpr.dinf.gres.architecture.helpers.ModelHelperFactory;
import br.ufpr.dinf.gres.architecture.helpers.StereotypeHelper;
import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.architecture.representation.Variant;
import br.ufpr.dinf.gres.architecture.representation.VariationPoint;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Stereotype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VariationPointFlyweight {


    private static final VariationPointFlyweight INSTANCE = new VariationPointFlyweight();
    private Architecture architecture;
    private HashMap<String, VariationPoint> variationPoints = new HashMap<String, VariationPoint>();
    private ModelHelper modelHelper;
    private Package model;

    private VariationPointFlyweight() {
        modelHelper = ModelHelperFactory.getModelHelper();
    }

    public static VariationPointFlyweight getInstance() {
        return INSTANCE;
    }

    public VariationPoint getOrCreateVariationPoint(Classifier klass) throws VariationPointElementTypeErrorException {
        Element variationPointElement = null;
        variationPointElement = architecture.findElementByName(klass.getName(), "class"); // Busca Classe ja na representacao
        if (variationPointElement == null)
            variationPointElement = architecture.findElementByName(klass.getName(), "interface"); // Busca Classe ja na representacao
        Stereotype variantTypeForVariationPointElement = StereotypeHelper.getVariantType(klass);
        VariationPoint variationPoint = variationPoints.get(variationPointElement.getId());

        if (variationPoint == null) {

            Stereotype variationPointStereotype = StereotypeHelper.getStereotypeByName(klass, "variationPoint");
            if (variationPointStereotype != null) {

                VariantFlyweight variantFlyweight = VariantFlyweight.getInstance();
                variantFlyweight.setArchitecture(architecture);

                String bindingTime = StereotypeHelper.getValueOfAttribute(klass, variationPointStereotype, "bindingTime");
                List<Variant> variants = new ArrayList<Variant>();
                String[] variantsElements = StereotypeHelper.getValueOfAttribute(klass, variationPointStereotype, "variants").split(",");

                for (String variantElement : variantsElements) {
                    if ((variantElement != null) && (!"".equals(variantElement.trim()))) {
                        Classifier klasss = modelHelper.getClassByName(variantElement.trim(), this.model);
                        if (klasss != null)
                            variants.add(variantFlyweight.getOrCreateVariant(klasss));
                    }
                }

                if (variantTypeForVariationPointElement != null)
                    variantFlyweight.getOrCreateVariant(klass);

                variationPoint = new VariationPoint(variationPointElement, variants, bindingTime);
                for (Variant variantTemp : variants)
                    variantTemp.getVariationPoints().add(variationPoint);

                variationPoints.put(variationPointElement.getId(), variationPoint);
            }
        }

        return variationPoint;
    }

    /**
     * @param architecture the architecture to set
     */
    public void setArchitecture(Architecture architecture) {
        this.architecture = architecture;
    }

    public VariationPoint getVariationPoint(String variationPointId) {
        return variationPoints.get(variationPointId);
    }

    /**
     * @return the variabilities
     */
    public List<VariationPoint> getVariationPoints() {
        return new ArrayList<VariationPoint>(variationPoints.values());
    }

    public void setVariationPoints(HashMap<String, VariationPoint> variationPoints) {
        this.variationPoints = variationPoints;
    }

    public void resertVariationPoints() {
        this.variationPoints.clear();
    }

    public void addModel(Package model) {
        this.model = model;
    }

}
