package br.otimizes.oplatool.core.jmetal4.experiments;

import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.jmetal4.qualityIndicator.util.MetricsUtil;
import br.otimizes.oplatool.core.persistence.Persistence;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.HashMap;

@Service
public class EdCalculation {

    private MetricsUtil metricsUtil;
    private NumberFormat numberFormat = NumberFormat.getInstance();
    private final Persistence persistence;

    public EdCalculation(Persistence persistence) {
        metricsUtil = new MetricsUtil();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumIntegerDigits(2);
        numberFormat.setRoundingMode(RoundingMode.HALF_UP);
        this.persistence = persistence;
    }

    private static double round(double value, int cases, int ceilOrFloor) {
        double rounded = value;
        rounded *= (Math.pow(10, cases));
        if (ceilOrFloor == 0) {
            rounded = Math.ceil(rounded);
        } else {
            rounded = Math.floor(rounded);
        }
        rounded /= (Math.pow(10, cases));
        return rounded;
    }

    public HashMap<String, Double> calculate(String experimentId, int numberObjectives) throws Exception {
        SolutionSet ss = persistence.queryNonDominatedSolutionsByExperiment(experimentId);
        HashMap<String, Double> results = new HashMap<>();

        String[] names = new String[ss.size()];
        for (int i = 0; i < ss.size(); i++)
            names[i] = ss.get(i).getSolutionName();

        double[][] front = ss.writeObjectivesToMatrix();
        double[] min = metricsUtil.getMinimumValues(front, numberObjectives);
        for (double v : min) {
            System.out.println("->" + v + ", ");
        }
        for (int i = 0; i < front.length; i++)
            results.put(names[i], round(metricsUtil.distance(min, front[i]), 4, 0));

        return results;
    }
}
