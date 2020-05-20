package br.ufpr.dinf.gres.core.jmetal4.main.indicators;

public class Hypervolume {

    /**
     * This class can be invoqued from the command line. Three params are required:
     * 1) the name of the file containing the front,
     * 2) the name of the file containig the true Pareto front
     * 3) the number of objectives
     */
    public static void main(String[] args) {

        br.ufpr.dinf.gres.core.jmetal4.qualityIndicator.
                Hypervolume qualityIndicator = new br.ufpr.dinf.gres.core.jmetal4.qualityIndicator.Hypervolume();
        double v = qualityIndicator.calculateHypervolume(new double[][]{
                new double[]{-1.0, -1.0, 0.2765957446808511},
                new double[]{0.0, 0.0, 0.0}
        }, 2, 3);
        double v2 = qualityIndicator.calculateHypervolume(new double[][]{
                new double[]{-1.8, -1.0, 0.9893617021276596},
                new double[]{1.0, 1.0, 1.0}
        }, 2, 3);
        double v3 = qualityIndicator.calculateHypervolume(new double[][]{
                new double[]{-0.4, 0.0, 0.8085106382978723}
        }, 1, 3);
        System.out.println(v);
        System.out.println(v2);
        System.out.println(v3);
//        if (args.length < 2) {
//            System.err.println("Error using delta. Type: \n java hypervolume " +
//                    "<SolutionFrontFile>" +
//                    "<TrueFrontFile> + <numberOfObjectives>");
//            System.exit(1);
//        }
//
//        //Create a new instance of the metric
//        br.ufpr.dinf.gres.core.jmetal4.qualityIndicator.Hypervolume qualityIndicator = new br.ufpr.dinf.gres.core.jmetal4.qualityIndicator.Hypervolume();
//        //Read the front from the files
//        double[][] trueFront = qualityIndicator.utils_.readFront("/home/wmfsystem/oplatool/output/8320592751e65ac081c83838287d4036/6920-05-2020-8924/19/Hypervolume/hypervolume.normalize");
//        double[][] solutionFront = qualityIndicator.utils_.readFront("/home/wmfsystem/oplatool/output/8320592751e65ac081c83838287d4036/6920-05-2020-8924/19/Hypervolume/todo.normalize");
////
////        //Obtain delta value
//        double value = qualityIndicator.hypervolume(solutionFront,
//                trueFront,
//                3);
////
//        System.out.println(value);
    } // main
}
