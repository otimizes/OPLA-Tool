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

    public static final DesignPattern[] FEASIBLE = new DesignPattern[]{
            Strategy.getInstance(),
            Bridge.getInstance(),
            Facade.getInstance(),
            Mediator.getInstance()
    };

    public static final DesignPattern[] IMPLEMENTED = new DesignPattern[]{
            Strategy.getInstance(),
            Bridge.getInstance(),
            Mediator.getInstance()
    };

    private final String name;
    private final String category;
    private final Random random;

    public DesignPattern(String name, String category) {
        this.random = new Random();
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public boolean randomlyVerifyAsPSOrPSPLA(Scope scope) {
        double PLAProbability = random.nextDouble();
        if (random.nextDouble() < PLAProbability) {
            return verifyPSPLA(scope);
        } else {
            return verifyPS(scope);
        }
    }

    public abstract boolean verifyPS(Scope scope);

    public abstract boolean verifyPSPLA(Scope scope);

    public abstract boolean apply(Scope scope);

    public boolean addStereotype(Element element) {
        Patterns pattern = Patterns.valueOf(this.name.toUpperCase());
        if (element instanceof Class) {
            Class elementClass = (Class) element;
            elementClass.getPatternsOperations().applyPattern(pattern);
            return true;
        } else if (element instanceof Interface) {
            Interface elementInterface = (Interface) element;
            elementInterface.getPatternsOperations().applyPattern(pattern);
            return true;
        }
        return false;
    }

    protected void addStereotype(Collection<? extends Element> elements) {
        for (Element element : elements) {
            addStereotype(element);
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.name);
        return hash;
    }

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
