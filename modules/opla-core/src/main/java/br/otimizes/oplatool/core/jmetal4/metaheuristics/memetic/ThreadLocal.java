package br.otimizes.oplatool.core.jmetal4.metaheuristics.memetic;

import br.otimizes.oplatool.core.jmetal4.core.Operator;
import br.otimizes.oplatool.core.jmetal4.core.Problem;
import br.otimizes.oplatool.core.jmetal4.core.Solution;

/**
 * Thread local implementation
 */
public class ThreadLocal extends Thread {

    Solution[] offSpringForLocal = null;
    Problem problemLocal = null;
    Operator localOperator = null;

    Solution[] solutionsLocal = null;

    public ThreadLocal(Solution[] offSpringForLocal, Problem problem_, Operator localOperator) {
        this.offSpringForLocal = offSpringForLocal;
        this.problemLocal = problem_;
        this.localOperator = localOperator;
    }

    public synchronized Solution[] getSolutionsLocal() {
        return solutionsLocal;
    }

    public synchronized void setSolutionsLocal(Solution[] solutionsLocalOk) {
        this.solutionsLocal = solutionsLocalOk;
    }

    @Override
    public synchronized void start() {
        this.run();
    }

    @Override
    public synchronized void run() {
        //System.out.println("entrou");
        try {
            this.localOperator.execute(this.offSpringForLocal[0]);
            this.localOperator.execute(this.offSpringForLocal[1]);
            this.problemLocal.evaluateConstraints(this.offSpringForLocal[0]);
            this.problemLocal.evaluateConstraints(this.offSpringForLocal[1]);
            this.problemLocal.evaluate(this.offSpringForLocal[0]);
            this.problemLocal.evaluate(this.offSpringForLocal[1]);

            this.setSolutionsLocal(this.offSpringForLocal);
        } catch (Exception e) {
            this.setSolutionsLocal(this.offSpringForLocal);

            //System.out.println("erro da thread \n"+e);
        }


    }

}
