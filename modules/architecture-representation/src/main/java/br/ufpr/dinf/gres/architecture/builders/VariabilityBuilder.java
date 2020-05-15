package br.ufpr.dinf.gres.architecture.builders;

import br.ufpr.dinf.gres.architecture.exceptions.ModelIncompleteException;
import br.ufpr.dinf.gres.architecture.exceptions.ModelNotFoundException;
import br.ufpr.dinf.gres.architecture.exceptions.SMartyProfileNotAppliedToModelExcepetion;
import br.ufpr.dinf.gres.architecture.exceptions.VariationPointElementTypeErrorException;
import br.ufpr.dinf.gres.architecture.flyweights.VariabilityFlyweight;
import br.ufpr.dinf.gres.architecture.flyweights.VariationPointFlyweight;
import br.ufpr.dinf.gres.architecture.helpers.StereotypeHelper;
import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Variability;
import br.ufpr.dinf.gres.architecture.representation.VariationPoint;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Stereotype;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Variability builder
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class VariabilityBuilder {

    private Architecture architecture;

    public VariabilityBuilder(Architecture architecture) throws ModelNotFoundException, ModelIncompleteException {
        this.architecture = architecture;
    }

    /**
     * Creates the variability from class
     *
     * @param klass class
     * @return variability
     * @throws ModelNotFoundException                   not found model
     * @throws ModelIncompleteException                 incomplete model
     * @throws SMartyProfileNotAppliedToModelExcepetion not applied profile
     * @throws VariationPointElementTypeErrorException  type error
     */
    public List<Variability> create(Classifier klass) throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelExcepetion, VariationPointElementTypeErrorException {

        VariabilityFlyweight variabilityFlyweight = VariabilityFlyweight.getInstance();
        variabilityFlyweight.setArchitecture(architecture);


        Variability variability = null;
        List<Comment> commentVariability = StereotypeHelper.getCommentVariability(klass);

        for (Comment comment : commentVariability) {
            Map<String, String> variabilityAttributes = StereotypeHelper.getVariabilityAttributes(klass, comment);

            if (variabilityAttributes != null) {
                variability = variabilityFlyweight.getOrCreateVariability(klass.getName(), variabilityAttributes);
            }

            VariationPointFlyweight variationPointFlyweight = VariationPointFlyweight.getInstance();
            variationPointFlyweight.setArchitecture(architecture);
            VariationPoint variationPoint = variationPointFlyweight.getOrCreateVariationPoint(klass);

            if ((variationPoint != null)) {
                Stereotype varitionPointSte = StereotypeHelper.getStereotypeByName(klass, "variationPoint");
                List<String> variabilitiesForVariationPoint = Arrays.asList(StereotypeHelper.getValueOfAttribute(klass, varitionPointSte, "variabilities").split(","));
                for (int i = 0; i < variabilitiesForVariationPoint.size(); i++) {
                    variabilitiesForVariationPoint.set(i, variabilitiesForVariationPoint.get(i).trim());
                }

                if (variabilitiesForVariationPoint.contains(variability.getName().trim())) {
                    variationPoint.getVariabilities().add(variability);
                    variability.setVariationPoint(variationPoint);
                }
            }
        }

        return variabilityFlyweight.getVariabilities();
    }


}