package br.ufpr.dinf.gres.core.jmetal4.database;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Concern;
import br.ufpr.dinf.gres.core.jmetal4.core.Solution;
import br.ufpr.dinf.gres.core.jmetal4.metrics.MetricsEvaluation;
import br.ufpr.dinf.gres.domain.entity.AllMetrics;
import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;
import br.ufpr.dinf.gres.domain.entity.Info;
import br.ufpr.dinf.gres.domain.entity.metric.*;

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
                if (objectiveFuncs.contains("elegance"))
                    allMetrics.getElegance().add(buildEleganceMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains("PLAExtensibility"))
                    allMetrics.getPlaExtensibility().add(buildPLAExtensibilityMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains("conventional"))
                    allMetrics.getConventional().add(buildConventionalMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains("featureDriven"))
                    allMetrics.getFeatureDriven().add(buildFeatureDrivenMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains("acomp"))
                    allMetrics.getAcomp().add(buildAcompMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains("aclass"))
                    allMetrics.getAclass().add(buildAclassMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains("tam"))
                    allMetrics.getTam().add(buildTamMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains("coe"))
                    allMetrics.getCoe().add(buildCoeMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains("dc"))
                    allMetrics.getDc().add(buildDcMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains("ec"))
                    allMetrics.getEc().add(buildEcMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains("lcc"))
                    allMetrics.getLcc().add(buildLccMetrics(idSolution, Execution, experiement, arch));
                //addYni
                if (objectiveFuncs.contains("wocsc"))
                    allMetrics.getWocsclass().add(buildWocsclassMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains("wocsi"))
                    allMetrics.getWocsinterface().add(buildWocsinterfaceMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains("cbcs"))
                    allMetrics.getCbcs().add(buildCbcsMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains("ssc"))
                    allMetrics.getSsc().add(buildSscMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains("svc"))
                    allMetrics.getSvc().add(buildSvcMetrics(idSolution, Execution, experiement, arch));
                if (objectiveFuncs.contains("av"))
                    allMetrics.getAv().add(buildAvMetrics(idSolution, Execution, experiement, arch));

                //addYni
            }
        }

        return allMetrics;
    }

    private FeatureDrivenMetric buildFeatureDrivenMetrics(String idSolution, Execution Execution, Experiment experiement,
                                                          Architecture arch) {

        FeatureDrivenMetric fd = new FeatureDrivenMetric(idSolution, Execution, experiement);

        fd.setCdac(MetricsEvaluation.evaluateCDAC(arch));
        fd.setCdai(MetricsEvaluation.evaluateCDAI(arch));
        fd.setCdao(MetricsEvaluation.evaluateCDAO(arch));
        fd.setCibc(MetricsEvaluation.evaluateCIBC(arch));
        fd.setIibc(MetricsEvaluation.evaluateIIBC(arch));
        fd.setOobc(MetricsEvaluation.evaluateOOBC(arch));
        fd.setLcc(MetricsEvaluation.evaluateLCC(arch));
        fd.setLccClass(MetricsEvaluation.evaluateLCCClass(arch));
        fd.setCdaClass(MetricsEvaluation.evaluateCDAClass(arch));
        fd.setCibClass(MetricsEvaluation.evaluateCIBClass(arch));

        return fd;
    }

    private ConventionalMetric buildConventionalMetrics(String idSolution, Execution Execution, Experiment experiement,
                                                        Architecture arch) {

        ConventionalMetric conventional = new ConventionalMetric(idSolution, Execution, experiement);

        conventional.setSumCohesion(MetricsEvaluation.evaluateCohesion(arch));
        conventional.setCohesion(MetricsEvaluation.evaluateICohesion(conventional.getSumCohesion()));
        conventional.setMeanDepComps(MetricsEvaluation.evaluateMeanDepComps(arch));
        conventional.setMeanNumOps(MetricsEvaluation.evaluateMeanNumOps(arch));
        conventional.setSumClassesDepIn((double) MetricsEvaluation.evaluateSumClassesDepIn(arch));
        conventional.setSumClassesDepOut((double) MetricsEvaluation.evaluateSumClassesDepOut(arch));
        conventional.setSumDepIn(MetricsEvaluation.evaluateSumDepIn(arch));
        conventional.setSumDepOut(MetricsEvaluation.evaluateSumDepOut(arch));

        return conventional;
    }

    private PLAExtensibilityMetric buildPLAExtensibilityMetrics(String idSolution, Execution Execution,
                                                                Experiment experiement, Architecture arch) {

        PLAExtensibilityMetric plaExtensibility = new PLAExtensibilityMetric(idSolution, Execution, experiement);
        plaExtensibility.setPlaExtensibility(MetricsEvaluation.evaluatePLAExtensibility(arch));

        return plaExtensibility;
    }

    private EleganceMetric buildEleganceMetrics(String idSolution, Execution Execution, Experiment experiement,
                                                Architecture arch) {

        EleganceMetric elegance = new EleganceMetric(idSolution, Execution, experiement);
        elegance.setNac(MetricsEvaluation.evaluateNACElegance(arch));
        elegance.setAtmr(MetricsEvaluation.evaluateATMRElegance(arch));
        elegance.setEc(MetricsEvaluation.evaluateECElegance(arch));

        return elegance;
    }

    private AcompMetric buildAcompMetrics(String idSolution, Execution Execution, Experiment experiement,
                                          Architecture arch) {

        AcompMetric acomp = new AcompMetric(idSolution, Execution, experiement);

        acomp.setSumDepIn(MetricsEvaluation.evaluateSumDepIn(arch));
        acomp.setSumDepOut(MetricsEvaluation.evaluateSumDepOut(arch));

        return acomp;
    }

    private AclassMetric buildAclassMetrics(String idSolution, Execution Execution, Experiment experiement,
                                            Architecture arch) {

        AclassMetric aclass = new AclassMetric(idSolution, Execution, experiement);

        aclass.setSumClassesDepIn((double) MetricsEvaluation.evaluateSumClassesDepIn(arch));
        aclass.setSumClassesDepOut((double) MetricsEvaluation.evaluateSumClassesDepOut(arch));

        return aclass;
    }

    private TamMetric buildTamMetrics(String idSolution, Execution Execution, Experiment experiement,
                                      Architecture arch) {

        TamMetric tam = new TamMetric(idSolution, Execution, experiement);

        tam.setMeanNumOps(MetricsEvaluation.evaluateMeanNumOps(arch));

        return tam;
    }

    private CoeMetric buildCoeMetrics(String idSolution, Execution Execution, Experiment experiement,
                                      Architecture arch) {

        CoeMetric coe = new CoeMetric(idSolution, Execution, experiement);

        coe.setCohesion(MetricsEvaluation.evaluateCohesion(arch));

        return coe;
    }

    private LCCMetric buildLccMetrics(String idSolution, Execution Execution, Experiment experiement,
                                      Architecture arch) {

        LCCMetric lcc = new LCCMetric(idSolution, Execution, experiement);
        lcc.setLcc(MetricsEvaluation.evaluateLCC(arch));
        return lcc;
    }

    private DcMetric buildDcMetrics(String idSolution, Execution Execution, Experiment experiement,
                                    Architecture arch) {

        DcMetric dc = new DcMetric(idSolution, Execution, experiement);

        dc.setCdai(MetricsEvaluation.evaluateCDAI(arch));
        dc.setCdao(MetricsEvaluation.evaluateCDAO(arch));
        dc.setCdac(MetricsEvaluation.evaluateCDAC(arch));


        return dc;
    }

    private EcMetric buildEcMetrics(String idSolution, Execution Execution, Experiment experiement,
                                    Architecture arch) {

        EcMetric ec = new EcMetric(idSolution, Execution, experiement);

        ec.setCibc(MetricsEvaluation.evaluateCIBC(arch));
        ec.setIibc(MetricsEvaluation.evaluateIIBC(arch));
        ec.setOobc(MetricsEvaluation.evaluateOOBC(arch));


        return ec;
    }

    //addYni


    private WocsclassMetric buildWocsclassMetrics(String idSolution, Execution Execution,
                                                  Experiment experiement, Architecture arch) {

        WocsclassMetric wocsClass = new WocsclassMetric(idSolution, Execution, experiement);
        wocsClass.setWocsclass(MetricsEvaluation.evaluateWocsC(arch));

        return wocsClass;
    }

    private WocsinterfaceMetric buildWocsinterfaceMetrics(String idSolution, Execution Execution,
                                                          Experiment experiement, Architecture arch) {

        WocsinterfaceMetric wocsInterface = new WocsinterfaceMetric(idSolution, Execution, experiement);
        wocsInterface.setWocsinterface(MetricsEvaluation.evaluateWocsI(arch));

        return wocsInterface;
    }

    private CbcsMetric buildCbcsMetrics(String idSolution, Execution Execution,
                                        Experiment experiement, Architecture arch) {

        CbcsMetric cBcs = new CbcsMetric(idSolution, Execution, experiement);
        cBcs.setCbcs(MetricsEvaluation.evaluateWocsI(arch));

        return cBcs;
    }

    private SscMetric buildSscMetrics(String idSolution, Execution Execution,
                                      Experiment experiement, Architecture arch) {

        SscMetric sSc = new SscMetric(idSolution, Execution, experiement);
        sSc.setSsc(MetricsEvaluation.evaluateWocsI(arch));

        return sSc;
    }


    private SvcMetric buildSvcMetrics(String idSolution, Execution Execution,
                                      Experiment experiement, Architecture arch) {

        SvcMetric sVc = new SvcMetric(idSolution, Execution, experiement);
        sVc.setSvc(MetricsEvaluation.evaluateWocsI(arch));

        return sVc;
    }


    private AvMetric buildAvMetrics(String idSolution, Execution Execution,
                                    Experiment experiement, Architecture arch) {

        AvMetric aV = new AvMetric(idSolution, Execution, experiement);
        aV.setAv(MetricsEvaluation.evaluateWocsI(arch));

        return aV;
    }

    //addYni

}
