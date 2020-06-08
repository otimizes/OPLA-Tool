package br.otimizes.oplatool.core.jmetal4.main.indicators;

public class GeneralizedSpread {
    /**
     * This class can be invoked from the command line. Three params are required:
     * 1) the name of the file containing the front,
     * 2) the name of the file containig the true Pareto front
     * 3) the number of objectives
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Error using GeneralizedSpread. " +
                    "Usage: \n java GeneralizedSpread" +
                    " <SolutionFrontFile> " +
                    " <TrueFrontFile> + <numberOfObjectives>");
            System.exit(1);
        }

        //Create a new instance of the metric
        br.otimizes.oplatool.core.jmetal4.qualityIndicator.GeneralizedSpread qualityIndicator = new br.otimizes.oplatool.core.jmetal4.qualityIndicator.GeneralizedSpread();
        //Read the front from the files
        double[][] solutionFront = br.otimizes.oplatool.core.jmetal4.qualityIndicator.GeneralizedSpread.utils_.readFront(args[0]);
        double[][] trueFront = br.otimizes.oplatool.core.jmetal4.qualityIndicator.GeneralizedSpread.utils_.readFront(args[1]);

        //Obtain delta value
        double value = qualityIndicator.generalizedSpread(solutionFront,
                trueFront,
                (new Integer(args[2])).intValue());

        System.out.println(value);
    }  // main

}
