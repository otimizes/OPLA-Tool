package br.otimizes.oplatool.core.jmetal4.main.indicators;

public class InvertedGenerationalDistance {

    /**
     * This class can be invoqued from the command line. Two params are required:
     * 1) the name of the file containing the front, and 2) the name of the file
     * containig the true Pareto front
     **/
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("InvertedGenerationalDistance::Main: Usage: java " +
                    "InvertedGenerationalDistance <FrontFile> " +
                    "<TrueFrontFile>  <numberOfObjectives>");
            System.exit(1);
        } // if

        // STEP 1. Create an instance of Generational Distance
        br.otimizes.oplatool.core.jmetal4.qualityIndicator.InvertedGenerationalDistance qualityIndicator = new br.otimizes.oplatool.core.jmetal4.qualityIndicator.InvertedGenerationalDistance();

        // STEP 2. Read the fronts from the files
        double[][] solutionFront = qualityIndicator.utils_.readFront(args[0]);
        double[][] trueFront = qualityIndicator.utils_.readFront(args[1]);

        // STEP 3. Obtain the metric value
        double value = qualityIndicator.invertedGenerationalDistance(
                solutionFront,
                trueFront,
                (new Integer(args[2])).intValue());

        System.out.println(value);
    } // main

}
