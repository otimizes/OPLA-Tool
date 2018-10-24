package arquitetura.builders;

import arquitetura.exceptions.ModelIncompleteException;
import arquitetura.exceptions.ModelNotFoundException;
import arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;
import arquitetura.exceptions.VariationPointElementTypeErrorException;
import arquitetura.flyweights.VariabilityFlyweight;
import arquitetura.flyweights.VariationPointFlyweight;
import arquitetura.helpers.StereotypeHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.Variability;
import arquitetura.representation.VariationPoint;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Stereotype;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class VariabilityBuilder {

    private Architecture architecture;


    public VariabilityBuilder(Architecture architecture) throws ModelNotFoundException, ModelIncompleteException {
        this.architecture = architecture;
    }

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