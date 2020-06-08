package br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions;

import br.otimizes.oplatool.core.jmetal4.metrics.conventionalMetrics.*;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;
import br.otimizes.oplatool.core.jmetal4.metrics.conventionalMetrics.*;

/**
 * Conventional Function
 * <p>
 * It aims to provide indicators on basic design principles including cohesion, coupling and size.
 * CM (pla) is formed by an aggregation of various metrics extracted from (W¨ust, 2016)
 * (DepIn, DepOut, CDepIn, CDepOut, DepPack, NumOps, H)
 */
public class CM extends ObjectiveFunctionImplementation {

    public CM(Architecture architecture) {
        super(architecture);
        double MACFitness = 0.0;
        double meanDepComps = 0.0;
        double sumCohesion = 0.0;
        double sumClassesDepIn = 0;
        double sumClassesDepOut = 0;
        double sumDepIn = 0;
        double sumDepOut = 0;
        double iCohesion = 0.0;

        MeanNumOpsByInterface numOps = new MeanNumOpsByInterface(architecture);
        Double meanNumOps = numOps.getResults();

        MeanDepComponents depComps = new MeanDepComponents(architecture);
        meanDepComps = depComps.getResults();

        ClassDependencyOut classesDepOut = new ClassDependencyOut(architecture);
        sumClassesDepOut = classesDepOut.getResults();

        ClassDependencyIn classesDepIn = new ClassDependencyIn(architecture);
        sumClassesDepIn = classesDepIn.getResults();

        DependencyOut DepOut = new DependencyOut(architecture);
        sumDepOut = DepOut.getResults();

        DependencyIn DepIn = new DependencyIn(architecture);
        sumDepIn = DepIn.getResults();

        RelationalCohesion cohesion = new RelationalCohesion(architecture);
        sumCohesion = cohesion.getResults();
        if (sumCohesion == 0) {
            iCohesion = 1.0;
        } else
            iCohesion = 1 / sumCohesion;

        MACFitness = meanNumOps + meanDepComps + sumClassesDepIn + sumClassesDepOut + sumDepIn + sumDepOut
                + (1 / sumCohesion);

        this.setResults(MACFitness);
    }

}
