package br.otimizes.oplatool.architecture.builders;

import br.otimizes.oplatool.architecture.exceptions.ModelIncompleteException;
import br.otimizes.oplatool.architecture.exceptions.ModelNotFoundException;
import br.otimizes.oplatool.architecture.exceptions.SMartyProfileNotAppliedToModelException;
import br.otimizes.oplatool.architecture.exceptions.VariationPointElementTypeErrorException;
import br.otimizes.oplatool.architecture.flyweights.VariabilityFlyweight;
import br.otimizes.oplatool.architecture.flyweights.VariationPointFlyweight;
import br.otimizes.oplatool.architecture.helpers.StereotypeHelper;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Variability;
import br.otimizes.oplatool.architecture.representation.VariationPoint;
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

    private final Architecture architecture;

    public VariabilityBuilder(Architecture architecture) {
        this.architecture = architecture;
    }

    /**
     * Creates the variability from class
     *
     * @param klass class
     * @return variability
     * @throws ModelNotFoundException                  not found model
     * @throws ModelIncompleteException                incomplete model
     * @throws SMartyProfileNotAppliedToModelException not applied profile
     * @throws VariationPointElementTypeErrorException type error
     */
    public List<Variability> create(Classifier klass) throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelException, VariationPointElementTypeErrorException {
        VariabilityFlyweight variabilityFlyweight = VariabilityFlyweight.getInstance();
        variabilityFlyweight.setArchitecture(architecture);
        Variability variability;
        List<Comment> commentVariability = StereotypeHelper.getCommentVariability(klass);

        for (Comment comment : commentVariability) {
            Map<String, String> variabilityAttributes = StereotypeHelper.getVariabilityAttributes(klass, comment);

            variability = variabilityFlyweight.getOrCreateVariability(klass.getName(), variabilityAttributes);

            VariationPoint variationPoint = getVariationPoint(klass);

            if ((variationPoint != null)) {
                Stereotype variationsPoints = StereotypeHelper.getStereotypeByName(klass, "variationPoint");
                List<String> variabilitiesForVariationPoint = Arrays.asList(StereotypeHelper.getValueOfAttribute(klass, variationsPoints, "variabilities").split(","));
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

    private VariationPoint getVariationPoint(Classifier klass) throws VariationPointElementTypeErrorException {
        VariationPointFlyweight variationPointFlyweight = VariationPointFlyweight.getInstance();
        variationPointFlyweight.setArchitecture(architecture);
        return variationPointFlyweight.getOrCreateVariationPoint(klass);
    }
}