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

    private MetricsUtil mu;
    private NumberFormat format = NumberFormat.getInstance();
    private final Persistence persistence;

    public EdCalculation(Persistence persistence) {
        mu = new MetricsUtil();
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
        format.setMaximumIntegerDigits(2);
        format.setRoundingMode(RoundingMode.HALF_UP);
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
        double[] min = mu.getMinimumValues(front, numberObjectives);
        for (int i = 0; i < min.length; i++) {
            System.out.println("->" + min[i] + ", ");
        }
        for (int i = 0; i < front.length; i++)
            results.put(names[i], round(mu.distance(min, front[i]), 4, 0));

        return results;
    }


}
