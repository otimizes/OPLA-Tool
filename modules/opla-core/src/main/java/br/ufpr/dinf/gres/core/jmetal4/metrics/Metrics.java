package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernCohesion.LCCClass;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion.*;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns.CIBC;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns.CIBClass;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns.IIBC;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns.OOBC;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.*;
import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;
import br.ufpr.dinf.gres.domain.entity.objectivefunctions.ObjectiveFunctionDomain;

public enum Metrics implements ObjectiveFunctionsLink {
    ATMRElegance {
        @Override
        public Double evaluate(Architecture architecture) {
            return new ATMRElegance(architecture).getResults();
        }

        @Override
        public ObjectiveFunctionDomain build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }
    },
    ECElegance {
        @Override
        public Double evaluate(Architecture architecture) {
            return new ECElegance(architecture).getResults();
        }

        @Override
        public ObjectiveFunctionDomain build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }
    },
    NACElegance {
        @Override
        public Double evaluate(Architecture architecture) {
            return new NACElegance(architecture).getResults();
        }

        @Override
        public ObjectiveFunctionDomain build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }
    },
    CIBC {
        @Override
        public Double evaluate(Architecture architecture) {
            return new CIBC(architecture).getResults();
        }

        @Override
        public ObjectiveFunctionDomain build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }
    },
    IIBC {
        @Override
        public Double evaluate(Architecture architecture) {
            return new IIBC(architecture).getResults();
        }

        @Override
        public ObjectiveFunctionDomain build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }
    },
    OOBC {
        @Override
        public Double evaluate(Architecture architecture) {
            return new OOBC(architecture).getResults();
        }

        @Override
        public ObjectiveFunctionDomain build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }
    },
    CDAC {
        @Override
        public Double evaluate(Architecture architecture) {
            return new CDAC(architecture).getResults();
        }

        @Override
        public ObjectiveFunctionDomain build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }
    },
    CDAI {
        @Override
        public Double evaluate(Architecture architecture) {
            return new CDAI(architecture).getResults();
        }

        @Override
        public ObjectiveFunctionDomain build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }
    },
    CDAO {
        @Override
        public Double evaluate(Architecture architecture) {
            return new CDAO(architecture).getResults();
        }

        @Override
        public ObjectiveFunctionDomain build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }
    },
    CDAClass {
        @Override
        public Double evaluate(Architecture architecture) {
            return new CDAClass(architecture).getResults();
        }

        @Override
        public ObjectiveFunctionDomain build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }
    },
    CIBClass {
        @Override
        public Double evaluate(Architecture architecture) {
            return new CIBClass(architecture).getResults();
        }

        @Override
        public ObjectiveFunctionDomain build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }
    },
    LCCClass {
        @Override
        public Double evaluate(Architecture architecture) {
            return new LCCClass(architecture).getResults();
        }

        @Override
        public ObjectiveFunctionDomain build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }
    },
    MeanNumOps {
        @Override
        public Double evaluate(Architecture architecture) {
            return new MeanNumOpsByInterface(architecture).getResults();
        }

        @Override
        public ObjectiveFunctionDomain build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }
    },
    MeanDepComps {
        @Override
        public Double evaluate(Architecture architecture) {
            return new MeanDepComponents(architecture).getResults();
        }

        @Override
        public ObjectiveFunctionDomain build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }
    },
    SumClassDepIn {
        @Override
        public Double evaluate(Architecture architecture) {
            return new ClassDependencyIn(architecture).getResults();
        }

        @Override
        public ObjectiveFunctionDomain build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }
    },
    SumClassDepOut {
        @Override
        public Double evaluate(Architecture architecture) {
            return new ClassDependencyOut(architecture).getResults();
        }

        @Override
        public ObjectiveFunctionDomain build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }
    },
    SumDepIn {
        @Override
        public Double evaluate(Architecture architecture) {
            return new DependencyIn(architecture).getResults();
        }

        @Override
        public ObjectiveFunctionDomain build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }
    },
    SumDepOut {
        @Override
        public Double evaluate(Architecture architecture) {
            return new DependencyOut(architecture).getResults();
        }

        @Override
        public ObjectiveFunctionDomain build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            return null;
        }
    }
}
