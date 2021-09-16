package br.otimizes.oplatool.core.jmetal4.metaheuristics.memetic;

import br.otimizes.oplatool.core.jmetal4.core.Operator;
import br.otimizes.oplatool.core.jmetal4.core.Problem;
import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;

/**
 * Local Search Until implementation
 */
public class LocalSearchUntil implements Runnable {

    public boolean solutionBest(SolutionSet result) {
        double vConventional = 0;
        double vPatterns = 0;
        try {
            for (int k = 0; k < result.get(0).numberOfObjectives(); k++) {
                vConventional += result.get(0).getObjective(k);
                vPatterns += result.get(1).getObjective(k);
            }
            if (vConventional < vPatterns) {
                return false;
            } else return vPatterns < vConventional;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    Solution offSpringForLocal_local;
    Solution offSpring_local;
    Problem problem_local_;
    Operator operadorLocal_local;
    Solution[] solutionsLocal = null;

    public LocalSearchUntil(Solution offSpring, Solution offSpringForLocal, Problem problem_,
                            Operator operadorLocal) {
        this.offSpringForLocal_local = offSpringForLocal;
        this.problem_local_ = problem_;
        this.operadorLocal_local = operadorLocal;
        this.offSpring_local = offSpring;
    }

    public synchronized Solution[] getSolutionsLocal() {
        return solutionsLocal;
    }

    public synchronized void setSolutionsLocal(Solution[] solutionsLocalOk) {
        this.solutionsLocal = solutionsLocalOk;
    }

    public void setLocal_Search(Solution[] offSpringForLocal, Problem problem_, Operator operadorLocal) {

    }

    @Override
    public synchronized void run() {
        try {
            int contador0 = 0;
            boolean resultComparation0 = false;
            while (resultComparation0 == false) {
                SolutionSet solutions0 = new SolutionSet(2);
                this.operadorLocal_local.execute(offSpringForLocal_local);
                this.problem_local_.evaluateConstraints(offSpringForLocal_local);
                this.problem_local_.evaluate(offSpringForLocal_local);

                solutions0.add(offSpring_local);
                solutions0.add(offSpringForLocal_local);
                resultComparation0 = this.solutionBest(solutions0);
                solutions0 = null;
                contador0++;
                if (contador0 > 14) {
                    offSpringForLocal_local = offSpring_local;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("erro da thread \n");
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
