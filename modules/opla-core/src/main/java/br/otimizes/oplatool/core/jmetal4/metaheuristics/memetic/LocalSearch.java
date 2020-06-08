package br.otimizes.oplatool.core.jmetal4.metaheuristics.memetic;

import br.otimizes.oplatool.core.jmetal4.core.Operator;
import br.otimizes.oplatool.core.jmetal4.core.Problem;
import br.otimizes.oplatool.core.jmetal4.core.Solution;

/**
 * Local Search implementation
 */
public class LocalSearch implements Runnable {

    Solution[] offSpringForLocal = null;
    Problem problemLocal = null;
    Operator operadorLocal = null;

    Solution[] solutionsLocal = null;

    public LocalSearch(Solution[] offSpringForLocal, Problem problem_, Operator operadorLocal) {
        this.offSpringForLocal = offSpringForLocal;
        this.problemLocal = problem_;
        this.operadorLocal = operadorLocal;
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
        boolean a = true;
        try {
            while (a) {
                this.operadorLocal.execute(this.offSpringForLocal[0]);
                this.operadorLocal.execute(this.offSpringForLocal[1]);
                this.problemLocal.evaluateConstraints(this.offSpringForLocal[0]);
                this.problemLocal.evaluateConstraints(this.offSpringForLocal[1]);
                this.problemLocal.evaluate(this.offSpringForLocal[0]);
                this.problemLocal.evaluate(this.offSpringForLocal[1]);
                a = false;
            }
        } catch (Exception e) {
            System.out.println("erro da thread \n");
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
}
