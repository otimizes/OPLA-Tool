package br.ufpr.dinf.gres.core.jmetal4.main.indicators;

public class Hypervolume {

    /**
     * This class can be invoqued from the command line. Three params are required:
     * 1) the name of the file containing the front,
     * 2) the name of the file containig the true Pareto front
     * 3) the number of objectives
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Error using delta. Type: \n java hypervolume " +
                    "<SolutionFrontFile>" +
                    "<TrueFrontFile> + <numberOfObjectives>");
            System.exit(1);
        }

        //Create a new instance of the metric
        br.ufpr.dinf.gres.core.jmetal4.qualityIndicator.Hypervolume qualityIndicator = new br.ufpr.dinf.gres.core.jmetal4.qualityIndicator.Hypervolume();
        //Read the front from the files
        double[][] solutionFront = qualityIndicator.utils_.readFront(args[0]);
        double[][] trueFront = qualityIndicator.utils_.readFront(args[1]);

        //Obtain delta value
        double value = qualityIndicator.hypervolume(solutionFront,
                trueFront,
                (new Integer(args[2])).intValue());

        System.out.println(value);
    } // main
}
