package br.ufpr.dinf.gres.core.jmetal4.main.indicators;

public class Spread {
    /**
     * This class can be invoqued from the command line. Three params are required:
     * 1) the name of the file containing the front,
     * 2) the name of the file containig the true Pareto front
     * 3) the number of objectives
     */
    public static void main(String args[]) {
        if (args.length < 2) {
            System.err.println("Spread::Main: Error using Spread. Usage: \n java " +
                    "Spread <FrontFile> <TrueFrontFile>  " +
                    "<numberOfObjectives>");
            System.exit(1);
        } // if

        // STEP 1. Create a new instance of the metric
        br.ufpr.dinf.gres.core.jmetal4.qualityIndicator.Spread qualityIndicator = new br.ufpr.dinf.gres.core.jmetal4.qualityIndicator.Spread();

        // STEP 2. Read the fronts from the files
        double[][] solutionFront = qualityIndicator.utils_.readFront(args[0]);
        double[][] trueFront = qualityIndicator.utils_.readFront(args[1]);

        // STEP 3. Obtain the metric value
        double value = qualityIndicator.spread(solutionFront, trueFront, 2);

        System.out.println(value);
    } // Main
}
