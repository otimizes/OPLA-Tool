package br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernCohesion;

import br.ufpr.dinf.gres.architecture.representation.Attribute;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Concern;
import br.ufpr.dinf.gres.architecture.representation.Method;

import java.util.Collection;
import java.util.HashSet;

public class LCCClassComponentResult {

    private final HashSet<Concern> allConcerns = new HashSet<>();
    private final Class cls;

    public LCCClassComponentResult(Class classC) {
        this.cls = classC;
        addConcerns(cls.getOwnConcerns());
        for (Attribute at : cls.getAllAttributes()) {
            addConcerns(at.getOwnConcerns());
        }
        for (Method method : cls.getAllMethods()) {
            addConcerns(method.getOwnConcerns());
        }
    }

    private void addConcerns(Collection<Concern> concerns) {
        allConcerns.addAll(concerns);
    }

    public int numberOfConcerns() {
        return allConcerns.size();
    }

    @Override
    public String toString() {
        return cls.getName() + ": " + numberOfConcerns();
    }
}
