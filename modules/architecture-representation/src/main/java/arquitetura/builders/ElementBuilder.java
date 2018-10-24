package arquitetura.builders;

import arquitetura.exceptions.ConcernNotFoundException;
import arquitetura.helpers.ModelElementHelper;
import arquitetura.helpers.StereotypeHelper;
import arquitetura.helpers.XmiHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.Variant;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @param <T>
 * @author edipofederle<edipofederle@gmail.com>
 */
public abstract class ElementBuilder<T extends arquitetura.representation.Element> {

    protected final Architecture architecture;
    private final HashMap<String, T> createdElements = new HashMap<String, T>();
    protected String name;
    protected Boolean isVariationPoint;
    protected Variant variantType;
    protected List<String> concerns;

    public ElementBuilder(Architecture architecture) {
        this.architecture = architecture;
    }

    protected abstract T buildElement(NamedElement modelElement);

    /**
     * Cria um novo elemento arquitetural.
     *
     * @param modelElement
     * @return
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

    private void inspectStereotypes(NamedElement modelElement) {
        List<Stereotype> allStereotypes = ModelElementHelper.getAllStereotypes(modelElement);
        for (Stereotype stereotype : allStereotypes) {
            verifyConcern(stereotype);
        }
    }

    private void verifyConcern(Stereotype stereotype) {
        if (StereotypeHelper.hasConcern(stereotype))
            concerns.add(stereotype.getName());
    }

    private void initialize() {
        name = "";
        isVariationPoint = false;
        variantType = null;
        concerns = new ArrayList<String>();
    }

}