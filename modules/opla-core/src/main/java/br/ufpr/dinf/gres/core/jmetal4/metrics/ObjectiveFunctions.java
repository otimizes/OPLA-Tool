package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.RelationalCohesion;
import br.ufpr.dinf.gres.core.jmetal4.metrics.objectivefunctions.*;
import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;
import br.ufpr.dinf.gres.domain.entity.objectivefunctions.*;
import br.ufpr.dinf.gres.persistence.base.BaseService;
import br.ufpr.dinf.gres.persistence.service.objectivefunctions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public enum ObjectiveFunctions implements ObjectiveFunctionsLink {
    ACLASS {
        @Autowired
        private ACLASSObjectiveFunctionService aclassMetricService;

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

        @Override
        public ACLASSObjectiveFunctionService getService() {
            return aclassMetricService;
        }
    },
    ACOMP {
        @Autowired
        private ACOMPObjectiveFunctionService acompMetricService;

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

        @Override
        public ACOMPObjectiveFunctionService getService() {
            return acompMetricService;
        }
    },
    TV {
        @Autowired
        private TVObjectiveFunctionService avMetricService;

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

        @Override
        public TVObjectiveFunctionService getService() {
            return avMetricService;
        }
    },
    CBCS {
        @Autowired
        private RCCObjectiveFunctionService cbcsMetricService;

        @Override
        public Double evaluate(Architecture architecture) {
            return new CBCS(architecture).getResults();
        }

        @Override
        public RCCObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            RCCObjectiveFunction cBcs = new RCCObjectiveFunction(idSolution, Execution, experiement);
            cBcs.setCbcs(ObjectiveFunctions.CBCS.evaluate(arch));
            return cBcs;
        }

        @Override
        public BaseService getService() {
            return cbcsMetricService;
        }
    },
    COE {

        @Autowired
        private COEObjectiveFunctionService coeMetricService;

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

        @Override
        public BaseService getService() {
            return coeMetricService;
        }
    },
    CM {
        @Autowired
        private CMObjectiveFunctionService conventionalMetricService;

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

        @Override
        public BaseService getService() {
            return conventionalMetricService;
        }
    },
    DC {

        @Autowired
        private DCObjectiveFunctionService dcMetricService;

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

        @Override
        public BaseService getService() {
            return dcMetricService;
        }
    },
    EC {
        @Autowired
        private ECObjectiveFunctionService ecMetricService;

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

        @Override
        public BaseService getService() {
            return ecMetricService;
        }
    },
    ELEG {
        @Autowired
        private ELEGObjectiveFunctionService eleganceMetricService;

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

        @Override
        public BaseService getService() {
            return eleganceMetricService;
        }
    },
    FM {
        @Autowired
        private FMObjectiveFunctionService featureDrivenMetricService;

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
            fd.setCibClass(Metrics.CIBClass.evaluate(arch));
            return fd;
        }

        @Override
        public BaseService getService() {
            return featureDrivenMetricService;
        }
    },
    LCC {
        @Autowired
        private LCCObjectiveFunctionService lccMetricService;

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

        @Override
        public BaseService getService() {
            return lccMetricService;
        }
    },
    EXT {

        @Autowired
        private EXTObjectiveFunctionService plaExtensibilityMetricService;

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

        @Override
        public BaseService getService() {
            return plaExtensibilityMetricService;
        }
    },
    SD {

        @Autowired
        private SDObjectiveFunctionService sscMetricService;

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

        @Override
        public BaseService getService() {
            return sscMetricService;
        }
    },
    SV {
        @Autowired
        private SVObjectiveFunctionService svcMetricService;

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

        @Override
        public BaseService getService() {
            return svcMetricService;
        }
    },
    TAM {

        @Autowired
        private TAMObjectiveFunctionService tamMetricService;

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

        @Override
        public BaseService getService() {
            return tamMetricService;
        }
    },
    WOCSCLASS {
        @Autowired
        private WOCSCLASSObjectiveFunctionService wocsclassMetricService;

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

        @Override
        public BaseService getService() {
            return wocsclassMetricService;
        }
    },
    WOCSINTERFACE {
        @Autowired
        private WOCSINTERFACEObjectiveFunctionService wocsinterfaceMetricService;

        @Override
        public Double evaluate(Architecture architecture) {
            return new WOCSINTERFACE(architecture).getResults();
        }

        @Override
        public WOCSINTERFACEObjectiveFunction build(String idSolution, Execution Execution, Experiment experiement, Architecture arch) {
            WOCSINTERFACEObjectiveFunction wocsInterface = new WOCSINTERFACEObjectiveFunction(idSolution, Execution, experiement);
            wocsInterface.setWocsinterface(ObjectiveFunctions.WOCSINTERFACE.evaluate(arch));
            return wocsInterface;
        }

        @Override
        public BaseService getService() {
            return wocsinterfaceMetricService;
        }
    };

}
