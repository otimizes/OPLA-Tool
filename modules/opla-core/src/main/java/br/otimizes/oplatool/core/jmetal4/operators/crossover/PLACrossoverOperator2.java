package br.otimizes.oplatool.core.jmetal4.operators.crossover;

import br.otimizes.oplatool.common.Configuration;
import br.otimizes.oplatool.core.jmetal4.core.OPLASolution2;
import br.otimizes.oplatool.core.jmetal4.encodings.solutionType.ArchitectureSolutionType;
import br.otimizes.oplatool.core.jmetal4.util.PseudoRandom;
import org.uma.jmetal.operator.crossover.CrossoverOperator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * PLA Crossover operator that call another crossover operators
 */
public class PLACrossoverOperator2 implements CrossoverOperator<OPLASolution2> {
    protected Map<String, Object> parameters_;

    private static final long serialVersionUID = -51015356906090226L;
    private static List VALID_TYPES = Arrays.asList(ArchitectureSolutionType.class);

    public List<String> getOperators() {
        return operators;
    }

    public void setOperators(List<String> operators) {
        this.operators = operators;
    }

    private List<String> operators;
    private Double probability;

    public PLACrossoverOperator2(List<String> operators, Double probability) {
        this.operators = operators;
        this.probability = probability;
    }

    public PLACrossoverOperator2(Double probability) {
        this.probability = probability;
    }


    @Override
    public double getCrossoverProbability() {
        return this.probability;
    }

    @Override
    public int getNumberOfRequiredParents() {
        return 0;
    }

    @Override
    public int getNumberOfGeneratedChildren() {
        return 1;
    }

    @Override
    public List<OPLASolution2> execute(List<OPLASolution2> parents) {
        if (parents.size() < 2) {
            Configuration.logger_.severe("PLACrossover.execute: operator needs two parents");
        }

        int r = PseudoRandom.randInt(0, this.operators.size() - 1);
        if (r != 0 && this.operators.size() == 1) r = 0;
        else if (this.operators.size() <= 0) return parents;
//        CrossoverOperators selectedOperator = CrossoverOperators.valueOf(this.operators.get(r));
//        return (List<OPLASolution2>) selectedOperator.getOperator().execute(parameters_, parents, "allLevels");
        return parents;
    }
}