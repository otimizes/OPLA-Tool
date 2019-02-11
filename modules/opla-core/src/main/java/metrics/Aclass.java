/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metrics;

import results.Execution;
import results.Experiment;

/**
 * @author elf
 */
public class Aclass extends Metrics {

    private int sumClassesDepIn;
    private int sumClassesDepOut;

    public Aclass(String idSolution, Execution execution, Experiment experiement) {
        super.setExecution(execution);
        super.setExperiement(experiement);
        super.setIdSolution(idSolution);
    }


    public int getSumClassesDepIn() {
        return sumClassesDepIn;
    }

    public void setSumClassesDepIn(int sumClassesDepIn) {
        this.sumClassesDepIn = sumClassesDepIn;
    }

    public int getSumClassesDepOut() {
        return sumClassesDepOut;
    }

    public void setSumClassesDepOut(int sumClassesDepOut) {
        this.sumClassesDepOut = sumClassesDepOut;
    }


    @Override
    public String toString() {
        return "Aclass [sumClassesDepIn=" + sumClassesDepIn + ", sumClassesDepOut="
                + sumClassesDepOut + "]";
    }

    public Double evaluateAclassFitness() {
        return Double.parseDouble(this.sumClassesDepIn + this.sumClassesDepOut + "");
    }

}
