package br.ufpr.dinf.gres.core.jmetal4.database;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Concern;
import br.ufpr.dinf.gres.core.jmetal4.core.Solution;
import br.ufpr.dinf.gres.core.jmetal4.experiments.Metrics;
import br.ufpr.dinf.gres.core.jmetal4.metrics.ObjectiveFunctionEvaluation;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.RelationalCohesion;
import br.ufpr.dinf.gres.domain.entity.AllMetrics;
import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;
import br.ufpr.dinf.gres.domain.entity.Info;
import br.ufpr.dinf.gres.domain.entity.objectivefunctions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Result {

    private String plaName;

    public void setPlaName(String plaName) {
        this.plaName = plaName;
    }

    /**
     * Returns {@link Info} given a {@link List} of {@link Solution} and a
     * executionId.<br />
     * <p>
     * Pass null to execution when are ALL br.ufpr.dinf.gres.core.jmetal4.results. So br.ufpr.dinf.gres.core.jmetal4.results belongs to
     * experiment and not to execution (run).
     * <p>
     * See {@link } for more details
     *
     * @param list
     * @param experiement
     * @param Execution
     * @return
     */
    public List<Info> getObjectives(List<Solution> list, Execution Execution, Experiment experiement) {
        List<Info> funResults = new ArrayList<Info>();
        for (Solution solution : list) {
            String sb = (solution.toString().trim()).replace(" ", "|");

            Info funResult = new Info();
            funResult.setName(plaName);
            funResult.setExecution(Execution);
            funResult.setExperiment(experiement);
            if (Execution == null)
                funResult.setIsAll(1);
            funResult.setObjectives(sb.replaceAll("\\s+", ""));
            funResults.add(funResult);
        }

        return funResults;
    }

    public List<Info> getInformations(List<Solution> solutionsList, Execution execution,
                                      Experiment experiement) {

        List<Info> Info = new ArrayList<Info>();

        for (int i = 0; i < solutionsList.size(); i++) {
            int numberOfVariables = solutionsList.get(0).getDecisionVariables().length;

            for (int j = 0; j < numberOfVariables; j++) {
                Architecture arch = (Architecture) solutionsList.get(i).getDecisionVariables()[j];

                Info ir = new Info();
                ir.setExecution(execution);
                ir.setExperiment(experiement);
                if (execution == null)
                    ir.setIsAll(1);
                ir.setName(plaName);
                ir.setListOfConcerns(getListOfConcerns(arch.getAllConcerns()));
                ir.setNumberOfPackages(arch.getAllPackages().size());
                ir.setNumberOfVariabilities(arch.getAllVariabilities().size());
                ir.setNumberOfInterfaces(arch.getAllInterfaces().size());
                ir.setNumberOfClasses(arch.getAllClasses().size());
                ir.setNumberOfDependencies(arch.getRelationshipHolder().getAllDependencies().size());
                ir.setNumberOfAbstractions(arch.getRelationshipHolder().getAllAbstractions().size());
                ir.setNumberOfGeneralizations(arch.getRelationshipHolder().getAllGeneralizations().size());
                ir.setNumberOfAssociations(arch.getRelationshipHolder().getAllAssociationsRelationships().size());
                ir.setNumberOfAssociationsClass(arch.getRelationshipHolder().getAllAssociationsClass().size());
                ir.setUserEvaluation(solutionsList.get(i).getEvaluation());
                ir.setFreezedElements(arch.toStringFreezedElements());
                ir.setObjectives(Arrays.toString(solutionsList.get(i).getObjectives()));
                Info.add(ir);
            }
        }

        return Info;
    }

    /**
     * Returns all concern formated like: concer1|concern2|...
     *
     * @param allConcerns
     * @return String
     */
    private String getListOfConcerns(List<Concern> allConcerns) {
        StringBuilder concernsList = new StringBuilder();
        for (Concern concern : allConcerns)
            concernsList.append(concern.getName()).append("|");

        return concernsList.substring(0, concernsList.length() - 1);
    }

    public AllMetrics getMetrics(List<Info> funResults, List<Solution> list, Execution Execution, Experiment experiement, List<String> objectiveFuncs) {

        AllMetrics allMetrics = new AllMetrics();
        int numberOfVariables = list.get(0).getDecisionVariables().length;

        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < numberOfVariables; j++) {
                Architecture arch = (Architecture) list.get(i).getDecisionVariables()[j];
                String idSolution = funResults.get(i).getId();
                if (objectiveFuncs.contains(Metrics.ELEG.toString()))
                    allMetrics.getElegance().add(buildEleganceMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains(Metrics.EXT.toString()))
                    allMetrics.getPlaExtensibility().add(buildPLAExtensibilityMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains(Metrics.CM.toString()))
                    allMetrics.getConventional().add(buildConventionalMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains(Metrics.FM.toString()))
                    allMetrics.getFm().add(buildFeatureDrivenMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains(Metrics.ACOMP.toString()))
                    allMetrics.getAcomp().add(buildAcompMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains(Metrics.ACLASS.toString()))
                    allMetrics.getAclass().add(buildAclassMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains(Metrics.TAM.toString()))
                    allMetrics.getTam().add(buildTamMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains(Metrics.COE.toString()))
                    allMetrics.getCoe().add(buildCoeMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains(Metrics.DC.toString()))
                    allMetrics.getDc().add(buildDcMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains(Metrics.EC.toString()))
                    allMetrics.getEc().add(buildEcMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains(Metrics.LCC.toString()))
                    allMetrics.getLcc().add(buildLccMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains(Metrics.WOCSCLASS.toString()))
                    allMetrics.getWocsclass().add(buildWocsclassMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains(Metrics.WOCSINTERFACE.toString()))
                    allMetrics.getWocsinterface().add(buildWocsinterfaceMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains(Metrics.CBCS.toString()))
                    allMetrics.getCbcs().add(buildCbcsMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains(Metrics.SD.toString()))
                    allMetrics.getSsc().add(buildSscMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains(Metrics.SV.toString()))
                    allMetrics.getSvc().add(buildSvcMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains(Metrics.TV.toString()))
                    allMetrics.getAv().add(buildAvMetrics(idSolution, Execution, experiement, arch));
            }
        }

        return allMetrics;
    }

    private FMObjectiveFunction buildFeatureDrivenMetrics(String idSolution, Execution Execution, Experiment experiement,
                                                          Architecture arch) {

        FMObjectiveFunction fd = new FMObjectiveFunction(idSolution, Execution, experiement);

        fd.setCdac(ObjectiveFunctionEvaluation.evaluateCDAC(arch));
        fd.setCdai(ObjectiveFunctionEvaluation.evaluateCDAI(arch));
        fd.setCdao(ObjectiveFunctionEvaluation.evaluateCDAO(arch));
        fd.setCibc(ObjectiveFunctionEvaluation.evaluateCIBC(arch));
        fd.setIibc(ObjectiveFunctionEvaluation.evaluateIIBC(arch));
        fd.setOobc(ObjectiveFunctionEvaluation.evaluateOOBC(arch));
        fd.setLcc(ObjectiveFunctionEvaluation.evaluateLCC(arch));
        fd.setLccClass(ObjectiveFunctionEvaluation.evaluateLCCClass(arch));
        fd.setCdaClass(ObjectiveFunctionEvaluation.evaluateCDAClass(arch));
        fd.setCibClass(ObjectiveFunctionEvaluation.evaluateCIBClass(arch));

        return fd;
    }

    private CMObjectiveFunction buildConventionalMetrics(String idSolution, Execution Execution, Experiment experiement,
                                                         Architecture arch) {

        CMObjectiveFunction conventional = new CMObjectiveFunction(idSolution, Execution, experiement);

        RelationalCohesion relationalCohesion = new RelationalCohesion(arch);
        conventional.setSumCohesion(relationalCohesion.getResults());
        conventional.setCohesion(relationalCohesion.evaluateICohesion());
        conventional.setMeanDepComps(ObjectiveFunctionEvaluation.evaluateMeanDepComps(arch));
        conventional.setMeanNumOps(ObjectiveFunctionEvaluation.evaluateMeanNumOps(arch));
        conventional.setSumClassesDepIn((double) ObjectiveFunctionEvaluation.evaluateSumClassesDepIn(arch));
        conventional.setSumClassesDepOut((double) ObjectiveFunctionEvaluation.evaluateSumClassesDepOut(arch));
        conventional.setSumDepIn(ObjectiveFunctionEvaluation.evaluateSumDepIn(arch));
        conventional.setSumDepOut(ObjectiveFunctionEvaluation.evaluateSumDepOut(arch));

        return conventional;
    }

    private EXTObjectiveFunction buildPLAExtensibilityMetrics(String idSolution, Execution Execution,
                                                              Experiment experiement, Architecture arch) {

        EXTObjectiveFunction plaExtensibility = new EXTObjectiveFunction(idSolution, Execution, experiement);
        plaExtensibility.setPlaExtensibility(ObjectiveFunctionEvaluation.evaluateEXT(arch));

        return plaExtensibility;
    }

    private ELEGObjectiveFunction buildEleganceMetrics(String idSolution, Execution Execution, Experiment experiement,
                                                       Architecture arch) {

        ELEGObjectiveFunction elegance = new ELEGObjectiveFunction(idSolution, Execution, experiement);
        elegance.setNac(ObjectiveFunctionEvaluation.evaluateNACElegance(arch));
        elegance.setAtmr(ObjectiveFunctionEvaluation.evaluateATMRElegance(arch));
        elegance.setEc(ObjectiveFunctionEvaluation.evaluateECElegance(arch));

        return elegance;
    }

    private ACOMPObjectiveFunction buildAcompMetrics(String idSolution, Execution Execution, Experiment experiement,
                                                     Architecture arch) {

        ACOMPObjectiveFunction acomp = new ACOMPObjectiveFunction(idSolution, Execution, experiement);

        acomp.setSumDepIn(ObjectiveFunctionEvaluation.evaluateSumDepIn(arch));
        acomp.setSumDepOut(ObjectiveFunctionEvaluation.evaluateSumDepOut(arch));

        return acomp;
    }

    private ACLASSObjectiveFunction buildAclassMetrics(String idSolution, Execution Execution, Experiment experiement,
                                                       Architecture arch) {

        ACLASSObjectiveFunction aclass = new ACLASSObjectiveFunction(idSolution, Execution, experiement);

        aclass.setSumClassesDepIn((double) ObjectiveFunctionEvaluation.evaluateSumClassesDepIn(arch));
        aclass.setSumClassesDepOut((double) ObjectiveFunctionEvaluation.evaluateSumClassesDepOut(arch));

        return aclass;
    }

    private TAMObjectiveFunction buildTamMetrics(String idSolution, Execution Execution, Experiment experiement,
                                                 Architecture arch) {

        TAMObjectiveFunction tam = new TAMObjectiveFunction(idSolution, Execution, experiement);

        tam.setMeanNumOps(ObjectiveFunctionEvaluation.evaluateMeanNumOps(arch));
        tam.setTam(ObjectiveFunctionEvaluation.evaluateTAM(arch));

        return tam;
    }

    private COEObjectiveFunction buildCoeMetrics(String idSolution, Execution Execution, Experiment experiement,
                                                 Architecture arch) {

        COEObjectiveFunction coe = new COEObjectiveFunction(idSolution, Execution, experiement);
        coe.setH(ObjectiveFunctionEvaluation.evaluateCOE(arch));

        return coe;
    }

    private LCCObjectiveFunction buildLccMetrics(String idSolution, Execution Execution, Experiment experiement,
                                                 Architecture arch) {

        LCCObjectiveFunction lcc = new LCCObjectiveFunction(idSolution, Execution, experiement);
        lcc.setLcc(ObjectiveFunctionEvaluation.evaluateLCC(arch));
        return lcc;
    }

    private DCObjectiveFunction buildDcMetrics(String idSolution, Execution Execution, Experiment experiement,
                                               Architecture arch) {

        DCObjectiveFunction dc = new DCObjectiveFunction(idSolution, Execution, experiement);

        dc.setCdai(ObjectiveFunctionEvaluation.evaluateCDAI(arch));
        dc.setCdao(ObjectiveFunctionEvaluation.evaluateCDAO(arch));
        dc.setCdac(ObjectiveFunctionEvaluation.evaluateCDAC(arch));


        return dc;
    }

    private ECObjectiveFunction buildEcMetrics(String idSolution, Execution Execution, Experiment experiement,
                                               Architecture arch) {

        ECObjectiveFunction ec = new ECObjectiveFunction(idSolution, Execution, experiement);

        ec.setCibc(ObjectiveFunctionEvaluation.evaluateCIBC(arch));
        ec.setIibc(ObjectiveFunctionEvaluation.evaluateIIBC(arch));
        ec.setOobc(ObjectiveFunctionEvaluation.evaluateOOBC(arch));


        return ec;
    }

    //addYni


    private WOCSCLASSObjectiveFunction buildWocsclassMetrics(String idSolution, Execution Execution,
                                                             Experiment experiement, Architecture arch) {

        WOCSCLASSObjectiveFunction wocsClass = new WOCSCLASSObjectiveFunction(idSolution, Execution, experiement);
        wocsClass.setWocsclass(ObjectiveFunctionEvaluation.evaluateWOCSCLASS(arch));

        return wocsClass;
    }

    private WOCSINTERFACEObjectiveFunction buildWocsinterfaceMetrics(String idSolution, Execution Execution,
                                                                     Experiment experiement, Architecture arch) {

        WOCSINTERFACEObjectiveFunction wocsInterface = new WOCSINTERFACEObjectiveFunction(idSolution, Execution, experiement);
        wocsInterface.setWocsinterface(ObjectiveFunctionEvaluation.evaluateWOCSINTERFFACE(arch));

        return wocsInterface;
    }

    private RCCObjectiveFunction buildCbcsMetrics(String idSolution, Execution Execution,
                                                  Experiment experiement, Architecture arch) {

        RCCObjectiveFunction cBcs = new RCCObjectiveFunction(idSolution, Execution, experiement);
        cBcs.setCbcs(ObjectiveFunctionEvaluation.evaluateWOCSINTERFFACE(arch));

        return cBcs;
    }

    private SDObjectiveFunction buildSscMetrics(String idSolution, Execution Execution,
                                                Experiment experiement, Architecture arch) {

        SDObjectiveFunction sSc = new SDObjectiveFunction(idSolution, Execution, experiement);
        sSc.setSsc(ObjectiveFunctionEvaluation.evaluateWOCSINTERFFACE(arch));

        return sSc;
    }


    private SVObjectiveFunction buildSvcMetrics(String idSolution, Execution Execution,
                                                Experiment experiement, Architecture arch) {

        SVObjectiveFunction sVc = new SVObjectiveFunction(idSolution, Execution, experiement);
        sVc.setSvc(ObjectiveFunctionEvaluation.evaluateWOCSINTERFFACE(arch));

        return sVc;
    }


    private TVObjectiveFunction buildAvMetrics(String idSolution, Execution Execution,
                                               Experiment experiement, Architecture arch) {

        TVObjectiveFunction aV = new TVObjectiveFunction(idSolution, Execution, experiement);
        aV.setAv(ObjectiveFunctionEvaluation.evaluateWOCSINTERFFACE(arch));

        return aV;
    }

    //addYni

}
