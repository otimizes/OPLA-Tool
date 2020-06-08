package br.otimizes.oplatool.core.jmetal4.metrics;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.core.jmetal4.metrics.conventionalMetrics.RelationalCohesion;
import br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions.*;
import br.otimizes.oplatool.domain.entity.Execution;
import br.otimizes.oplatool.domain.entity.Experiment;
import br.otimizes.oplatool.domain.entity.objectivefunctions.*;

/**
 * Objective Functions Enum
 * <p>
 * {@link br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions.ACLASS ACLASS,}
 * {@link br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions.ACOMP ACOMP,}
 * {@link br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions.CM CM,}
 * {@link br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions.CIBF CIBF,}
 * {@link br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions.COE COE,}
 * {@link br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions.CS CS,}
 * {@link br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions.DC DC,}
 * {@link br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions.EC EC,}
 * {@link br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions.FDAC FDAC,}
 * {@link br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions.ELEG ELEG,}
 * {@link br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions.EXT EXT,}
 * {@link br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions.FM FM,}
 * {@link br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions.LCC LCC,}
 * {@link br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions.LFCC LFCC,}
 * {@link br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions.RCC RCC,}
 * {@link br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions.SD SD,}
 * {@link br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions.SV SV,}
 * {@link br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions.TAM TAM,}
 * {@link br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions.TV TV,}
 * {@link br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions.WOCSCLASS WOCSCLASS}
 */
public enum ObjectiveFunctions implements ObjectiveFunctionsLink {

    ACLASS {
        @Override
        public Double evaluate(Architecture architecture) {
            return new ACLASS(architecture).getResults();
        }

        @Override
        public ACLASSObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement,
                                             Architecture arch) {
            ACLASSObjectiveFunction aclass = new ACLASSObjectiveFunction(idSolution, Execution, experiement);
            aclass.setSumClassesDepIn(Metrics.SumClassDepIn.evaluate(arch));
            aclass.setSumClassesDepOut(Metrics.SumClassDepOut.evaluate(arch));
            return aclass;
        }
    },
    ACOMP {
        @Override
        public Double evaluate(Architecture architecture) {
            return new ACOMP(architecture).getResults();
        }

        @Override
        public ACOMPObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            ACOMPObjectiveFunction acomp = new ACOMPObjectiveFunction(idSolution, Execution, experiement);
            acomp.setSumDepIn(Metrics.SumDepIn.evaluate(arch));
            acomp.setSumDepOut(Metrics.SumDepOut.evaluate(arch));
            return acomp;
        }
    },
    TV {
        @Override
        public Double evaluate(Architecture architecture) {
            return new TV(architecture).getResults();
        }

        @Override
        public TVObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            TVObjectiveFunction aV = new TVObjectiveFunction(idSolution, Execution, experiement);
            aV.setAv(ObjectiveFunctions.TV.evaluate(arch));
            return aV;
        }
    },
    RCC {
        @Override
        public Double evaluate(Architecture architecture) {
            return new RCC(architecture).getResults();
        }

        @Override
        public RCCObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            RCCObjectiveFunction cBcs = new RCCObjectiveFunction(idSolution, Execution, experiement);
            cBcs.setCbcs(ObjectiveFunctions.RCC.evaluate(arch));
            return cBcs;
        }
    },
    COE {
        @Override
        public Double evaluate(Architecture architecture) {
            return new COE(architecture).getResults();
        }

        @Override
        public COEObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            COEObjectiveFunction coe = new COEObjectiveFunction(idSolution, Execution, experiement);
            coe.setH(ObjectiveFunctions.COE.evaluate(arch));
            return coe;
        }

    },
    CM {
        @Override
        public Double evaluate(Architecture architecture) {
            return new CM(architecture).getResults();
        }

        @Override
        public CMObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            CMObjectiveFunction conventional = new CMObjectiveFunction(idSolution, Execution, experiement);
            RelationalCohesion relationalCohesion = new RelationalCohesion(arch);
            conventional.setSumCohesion(relationalCohesion.getResults());
            conventional.setCohesion(relationalCohesion.evaluateICohesion());
            conventional.setMeanDepComps(Metrics.MeanDepComps.evaluate(arch));
            conventional.setMeanNumOps(Metrics.MeanNumOps.evaluate(arch));
            conventional.setSumClassesDepIn(Metrics.SumClassDepIn.evaluate(arch));
            conventional.setSumClassesDepOut(Metrics.SumClassDepOut.evaluate(arch));
            conventional.setSumDepIn(Metrics.SumDepIn.evaluate(arch));
            conventional.setSumDepOut(Metrics.SumDepOut.evaluate(arch));
            return conventional;
        }
    },
    DC {
        @Override
        public Double evaluate(Architecture architecture) {
            return new DC(architecture).getResults();
        }

        @Override
        public DCObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            DCObjectiveFunction dc = new DCObjectiveFunction(idSolution, Execution, experiement);
            dc.setCdai(Metrics.CDAI.evaluate(arch));
            dc.setCdao(Metrics.CDAO.evaluate(arch));
            dc.setCdac(Metrics.CDAC.evaluate(arch));
            return dc;
        }
    },
    EC {
        @Override
        public Double evaluate(Architecture architecture) {
            return new EC(architecture).getResults();
        }

        @Override
        public ECObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            ECObjectiveFunction ec = new ECObjectiveFunction(idSolution, Execution, experiement);
            ec.setCibc(Metrics.CIBC.evaluate(arch));
            ec.setIibc(Metrics.IIBC.evaluate(arch));
            ec.setOobc(Metrics.OOBC.evaluate(arch));
            return ec;
        }
    },
    ELEG {
        @Override
        public Double evaluate(Architecture architecture) {
            return new ELEG(architecture).getResults();
        }

        @Override
        public ELEGObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            ELEGObjectiveFunction elegance = new ELEGObjectiveFunction(idSolution, Execution, experiement);
            elegance.setNac(Metrics.NACElegance.evaluate(arch));
            elegance.setAtmr(Metrics.ATMRElegance.evaluate(arch));
            elegance.setEc(Metrics.ECElegance.evaluate(arch));
            return elegance;
        }
    },
    FM {
        @Override
        public Double evaluate(Architecture architecture) {
            return new FM(architecture).getResults();
        }

        @Override
        public FMObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            FMObjectiveFunction fd = new FMObjectiveFunction(idSolution, Execution, experiement);
            fd.setCdac(Metrics.CDAC.evaluate(arch));
            fd.setCdai(Metrics.CDAI.evaluate(arch));
            fd.setCdao(Metrics.CDAO.evaluate(arch));
            fd.setCibc(Metrics.CIBC.evaluate(arch));
            fd.setIibc(Metrics.IIBC.evaluate(arch));
            fd.setOobc(Metrics.OOBC.evaluate(arch));
            fd.setLcc(ObjectiveFunctions.LCC.evaluate(arch));
            fd.setLccClass(Metrics.LCCClass.evaluate(arch));
            fd.setCdaClass(Metrics.CDAClass.evaluate(arch));
            return fd;
        }
    },
    LCC {
        @Override
        public Double evaluate(Architecture architecture) {
            return new LCC(architecture).getResults();
        }

        @Override
        public LCCObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            LCCObjectiveFunction lcc = new LCCObjectiveFunction(idSolution, Execution, experiement);
            lcc.setLcc(ObjectiveFunctions.LCC.evaluate(arch));
            return lcc;
        }
    },
    EXT {
        @Override
        public Double evaluate(Architecture architecture) {
            return new EXT(architecture).getResults();
        }

        @Override
        public EXTObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            EXTObjectiveFunction plaExtensibility = new EXTObjectiveFunction(idSolution, Execution, experiement);
            plaExtensibility.setPlaExtensibility(ObjectiveFunctions.EXT.evaluate(arch));
            return plaExtensibility;
        }
    },
    SD {
        @Override
        public Double evaluate(Architecture architecture) {
            return new SD(architecture).getResults();
        }

        @Override
        public SDObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            SDObjectiveFunction sSc = new SDObjectiveFunction(idSolution, Execution, experiement);
            sSc.setSsc(ObjectiveFunctions.SD.evaluate(arch));
            return sSc;
        }
    },
    SV {
        @Override
        public Double evaluate(Architecture architecture) {
            return new SV(architecture).getResults();
        }

        @Override
        public SVObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            SVObjectiveFunction sVc = new SVObjectiveFunction(idSolution, Execution, experiement);
            sVc.setSvc(ObjectiveFunctions.SV.evaluate(arch));
            return sVc;
        }
    },
    TAM {
        @Override
        public Double evaluate(Architecture architecture) {
            return new TAM(architecture).getResults();
        }

        @Override
        public TAMObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            TAMObjectiveFunction tam = new TAMObjectiveFunction(idSolution, Execution, experiement);
            tam.setMeanNumOps(Metrics.MeanNumOps.evaluate(arch));
            tam.setTam(ObjectiveFunctions.TAM.evaluate(arch));
            return tam;
        }
    },
    WOCSCLASS {
        @Override
        public Double evaluate(Architecture architecture) {
            return new WOCSCLASS(architecture).getResults();
        }

        @Override
        public WOCSCLASSObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            WOCSCLASSObjectiveFunction wocsClass = new WOCSCLASSObjectiveFunction(idSolution, Execution, experiement);
            wocsClass.setWocsclass(ObjectiveFunctions.WOCSCLASS.evaluate(arch));
            return wocsClass;
        }
    },
    CS {
        @Override
        public Double evaluate(Architecture architecture) {
            return new CS(architecture).getResults();
        }

        @Override
        public CSObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            CSObjectiveFunction wocsInterface = new CSObjectiveFunction(idSolution, Execution, experiement);
            wocsInterface.setWocsinterface(ObjectiveFunctions.CS.evaluate(arch));
            return wocsInterface;
        }
    },
    LFCC {
        @Override
        public Double evaluate(Architecture architecture) {
            return new LFCC(architecture).getResults();
        }

        @Override
        public LFCCObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            LFCCObjectiveFunction value = new LFCCObjectiveFunction(idSolution, Execution, experiement);
            value.setLfcc(ObjectiveFunctions.LFCC.evaluate(arch));
            return value;
        }
    },
    FDAC {
        @Override
        public Double evaluate(Architecture architecture) {
            return new FDAC(architecture).getResults();
        }

        @Override
        public FDACObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            FDACObjectiveFunction value = new FDACObjectiveFunction(idSolution, Execution, experiement);
            value.setFdac(ObjectiveFunctions.FDAC.evaluate(arch));
            return value;
        }
    },
    CIBF {
        @Override
        public Double evaluate(Architecture architecture) {
            return new CIBF(architecture).getResults();
        }

        @Override
        public CIBFObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            CIBFObjectiveFunction value = new CIBFObjectiveFunction(idSolution, Execution, experiement);
            value.setCibf(ObjectiveFunctions.CIBF.evaluate(arch));
            return value;
        }
    }

}
