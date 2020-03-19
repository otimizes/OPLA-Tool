package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernCohesion.LCCClass;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion.*;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns.CIBC;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns.IIBC;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns.OOBC;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.*;
import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;
import br.ufpr.dinf.gres.domain.entity.objectivefunctions.BaseObjectiveFunction;
import br.ufpr.dinf.gres.persistence.base.BaseService;

public enum Metrics implements ObjectiveFunctionsLink {
    ATMRElegance {
        @Override
        public Double evaluate(Architecture architecture) {
            return new ATMRElegance(architecture).getResults();
        }

        @Override
        public BaseObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }

        @Override
        public BaseService getService() {
            return null;
        }
    },
    ECElegance {
        @Override
        public Double evaluate(Architecture architecture) {
            return new ECElegance(architecture).getResults();
        }

        @Override
        public BaseObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }

        @Override
        public BaseService getService() {
            return null;
        }
    },
    NACElegance {
        @Override
        public Double evaluate(Architecture architecture) {
            return new NACElegance(architecture).getResults();
        }

        @Override
        public BaseObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }

        @Override
        public BaseService getService() {
            return null;
        }
    },
    CIBC {
        @Override
        public Double evaluate(Architecture architecture) {
            return new CIBC(architecture).getResults();
        }

        @Override
        public BaseObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }

        @Override
        public BaseService getService() {
            return null;
        }
    },
    IIBC {
        @Override
        public Double evaluate(Architecture architecture) {
            return new IIBC(architecture).getResults();
        }

        @Override
        public BaseObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }

        @Override
        public BaseService getService() {
            return null;
        }
    },
    OOBC {
        @Override
        public Double evaluate(Architecture architecture) {
            return new OOBC(architecture).getResults();
        }

        @Override
        public BaseObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }

        @Override
        public BaseService getService() {
            return null;
        }
    },
    CDAC {
        @Override
        public Double evaluate(Architecture architecture) {
            return new CDAC(architecture).getResults();
        }

        @Override
        public BaseObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }

        @Override
        public BaseService getService() {
            return null;
        }
    },
    CDAI {
        @Override
        public Double evaluate(Architecture architecture) {
            return new CDAI(architecture).getResults();
        }

        @Override
        public BaseObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }

        @Override
        public BaseService getService() {
            return null;
        }
    },
    CDAO {
        @Override
        public Double evaluate(Architecture architecture) {
            return new CDAO(architecture).getResults();
        }

        @Override
        public BaseObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }

        @Override
        public BaseService getService() {
            return null;
        }
    },
    CDAClass {
        @Override
        public Double evaluate(Architecture architecture) {
            return new CDAClass(architecture).getResults();
        }

        @Override
        public BaseObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }

        @Override
        public BaseService getService() {
            return null;
        }
    },
    CIBClass {
        @Override
        public Double evaluate(Architecture architecture) {
            return new CIBClass(architecture).getResults();
        }

        @Override
        public BaseObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }

        @Override
        public BaseService getService() {
            return null;
        }
    },
    LCCClass {
        @Override
        public Double evaluate(Architecture architecture) {
            return new LCCClass(architecture).getResults();
        }

        @Override
        public BaseObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }

        @Override
        public BaseService getService() {
            return null;
        }
    },
    MeanNumOps {
        @Override
        public Double evaluate(Architecture architecture) {
            return new MeanNumOpsByInterface(architecture).getResults();
        }

        @Override
        public BaseObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }

        @Override
        public BaseService getService() {
            return null;
        }
    },
    MeanDepComps {
        @Override
        public Double evaluate(Architecture architecture) {
            return new MeanDepComponents(architecture).getResults();
        }

        @Override
        public BaseObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }

        @Override
        public BaseService getService() {
            return null;
        }
    },
    SumClassDepIn {
        @Override
        public Double evaluate(Architecture architecture) {
            return new ClassDependencyIn(architecture).getResults();
        }

        @Override
        public BaseObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }

        @Override
        public BaseService getService() {
            return null;
        }
    },
    SumClassDepOut {
        @Override
        public Double evaluate(Architecture architecture) {
            return new ClassDependencyOut(architecture).getResults();
        }

        @Override
        public BaseObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }

        @Override
        public BaseService getService() {
            return null;
        }
    },
    SumDepIn {
        @Override
        public Double evaluate(Architecture architecture) {
            return new DependencyIn(architecture).getResults();
        }

        @Override
        public BaseObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }

        @Override
        public BaseService getService() {
            return null;
        }
    },
    SumDepOut {
        @Override
        public Double evaluate(Architecture architecture) {
            return new DependencyOut(architecture).getResults();
        }

        @Override
        public BaseObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }

        @Override
        public BaseService getService() {
            return null;
        }
    }
}
