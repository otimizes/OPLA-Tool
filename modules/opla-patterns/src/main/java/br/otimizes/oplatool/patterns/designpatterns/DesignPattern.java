package br.otimizes.oplatool.patterns.designpatterns;

import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.architecture.representation.Patterns;
import br.otimizes.oplatool.patterns.models.Scope;

import java.util.Collection;
import java.util.Objects;
import java.util.Random;

/**
 * The Class DesignPattern.
 */
public abstract class DesignPattern {

    /** The Constant FEASIBLE. */
    public static final DesignPattern[] FEASIBLE = new DesignPattern[]{
            Strategy.getInstance(),
            Bridge.getInstance(),
            Facade.getInstance(),
            Mediator.getInstance()
    };

    /** The Constant IMPLEMENTED. */
    public static final DesignPattern[] IMPLEMENTED = new DesignPattern[]{
            Strategy.getInstance(),
            Bridge.getInstance(),
            Mediator.getInstance()
    };

    /** The name. */
    private final String name;
    
    /** The category. */
    private final String category;
    
    /** The random. */
    private final Random random;

    /**
     * Instantiates a new design pattern.
     *
     * @param name the name
     * @param category the category
     */
    public DesignPattern(String name, String category) {
        this.random = new Random();
        this.name = name;
        this.category = category;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the category.
     *
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Randomly verify as PS or PSPLA.
     *
     * @param scope the scope
     * @return true, if successful
     */
    public boolean randomlyVerifyAsPSOrPSPLA(Scope scope) {
        double PLAProbability = random.nextDouble();
        if (random.nextDouble() < PLAProbability) {
            return verifyPSPLA(scope);
        } else {
            return verifyPS(scope);
        }
    }

    /**
     * Verify PS.
     *
     * @param scope the scope
     * @return true, if PS
     */
    public abstract boolean verifyPS(Scope scope);

    /**
     * Verify PSPLA.
     *
     * @param scope the scope
     * @return true, if PSPLA
     */
    public abstract boolean verifyPSPLA(Scope scope);

    /**
     * Apply.
     *
     * @param scope the scope
     * @return true, if successful
     */
    public abstract boolean apply(Scope scope);

    /**
     * Adds the stereotype.
     *
     * @param element the element
     * @return true, if successful
     */
    public boolean addStereotype(Element element) {
        Patterns pattern = Patterns.valueOf(this.name.toUpperCase());
        if (pattern != null) {
            if (element instanceof Class) {
                Class elementClass = (Class) element;
                elementClass.getPatternsOperations().applyPattern(pattern);
                return true;
            } else if (element instanceof Interface) {
                Interface elementInterface = (Interface) element;
                elementInterface.getPatternsOperations().applyPattern(pattern);
                return true;
            }
        }
        return false;
    }

    /**
     * Adds the stereotype.
     *
     * @param elements the elements
     */
    protected void addStereotype(Collection<? extends Element> elements) {
        for (Element element : elements) {
            addStereotype(element);
        }
    }

    /**
     * Hash code.
     *
     * @return the hashCode
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.name);
        return hash;
    }

    /**
     * Equals.
     *
     * @param obj the obj
     * @return true, if successful
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DesignPattern other = (DesignPattern) obj;
        return Objects.equals(this.name, other.name);
    }

}
