package br.ufpr.dinf.gres.core.jmetal4.main.indicators;

public class Epsilon {

    /**
     * Returns the additive-epsilon value of the paretoFront. This method call to the
     * calculate epsilon-indicator one
     *
     */
    public static void main(String[] args) {
        double ind_value;

        if (args.length < 2) {
            System.err.println("Error using delta. Type: \n java AdditiveEpsilon " +
                    "<FrontFile>" +
                    "<TrueFrontFile> + <numberOfObjectives>");
            System.exit(1);
        }

        br.ufpr.dinf.gres.core.jmetal4.qualityIndicator.Epsilon qualityIndicator = new br.ufpr.dinf.gres.core.jmetal4.qualityIndicator.Epsilon();
        double[][] solutionFront = qualityIndicator.utils_.readFront(args[0]);
        double[][] trueFront = qualityIndicator.utils_.readFront(args[1]);
        //qualityIndicator.dim_ = trueParetoFront[0].length;
        //qualityIndicator.set_params();

        ind_value = qualityIndicator.epsilon(trueFront,
                solutionFront,
                new Integer(args[2]).intValue());

        System.out.println(ind_value);
    } // main

}
