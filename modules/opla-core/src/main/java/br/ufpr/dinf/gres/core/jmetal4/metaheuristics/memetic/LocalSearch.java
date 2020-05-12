package br.ufpr.dinf.gres.core.jmetal4.metaheuristics.memetic;

import br.ufpr.dinf.gres.core.jmetal4.core.Operator;
import br.ufpr.dinf.gres.core.jmetal4.core.Problem;
import br.ufpr.dinf.gres.core.jmetal4.core.Solution;

public class LocalSearch implements Runnable {

    Solution[] offSpringForLocal_local = null;
    Problem problem_local_ = null;
    Operator operadorLocal_local = null;

    Solution[] solutionsLocal = null;

    public LocalSearch(Solution[] offSpringForLocal, Problem problem_, Operator operadorLocal) {
        this.offSpringForLocal_local = offSpringForLocal;
        this.problem_local_ = problem_;
        this.operadorLocal_local = operadorLocal;
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
                this.operadorLocal_local.execute(this.offSpringForLocal_local[0]);
                this.operadorLocal_local.execute(this.offSpringForLocal_local[1]);
                this.problem_local_.evaluateConstraints(this.offSpringForLocal_local[0]);
                this.problem_local_.evaluateConstraints(this.offSpringForLocal_local[1]);
                this.problem_local_.evaluate(this.offSpringForLocal_local[0]);
                this.problem_local_.evaluate(this.offSpringForLocal_local[1]);
                a = false;
            }
        } catch (Exception e) {
            System.out.println("erro da thread \n");
            e.printStackTrace();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
