package br.ufpr.dinf.gres.core.jmetal4.database;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Concern;
import br.ufpr.dinf.gres.core.jmetal4.core.Solution;
import br.ufpr.dinf.gres.core.jmetal4.metrics.*;
import br.ufpr.dinf.gres.core.jmetal4.results.ExecutionResults;
import br.ufpr.dinf.gres.core.jmetal4.results.ExperimentResults;
import br.ufpr.dinf.gres.core.jmetal4.results.InfoResults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Result {

    private String plaName;

    public void setPlaName(String plaName) {
        this.plaName = plaName;
    }

    /**
     * Returns {@link InfoResults} given a {@link List} of {@link Solution} and a
     * executionId.<br />
     * <p>
     * Pass null to execution when are ALL br.ufpr.dinf.gres.core.jmetal4.results. So br.ufpr.dinf.gres.core.jmetal4.results belongs to
     * experiment and not to execution (run).
     * <p>
     * See {@link } for more details
     *
     * @param list
     * @param experiement
     * @param executionResults
     * @return
     */
    public List<InfoResults> getObjectives(List<Solution> list, ExecutionResults executionResults, ExperimentResults experiement) {
        List<InfoResults> funResults = new ArrayList<InfoResults>();
        for (Solution solution : list) {
            String sb = (solution.toString().trim()).replace(" ", "|");

            InfoResults funResult = new InfoResults();
            funResult.setName(plaName + "_" + funResult.getId());
            funResult.setExecutionResults(executionResults);
            funResult.setExperiement(experiement);
            if (executionResults == null)
                funResult.setIsAll(1);
            funResult.setObjectives(sb.replaceAll("\\s+", ""));
            funResults.add(funResult);
        }

        return funResults;
    }

    public List<InfoResults> getInformations(List<Solution> solutionsList, ExecutionResults executionResults,
                                             ExperimentResults experiement) {

        List<InfoResults> infoResults = new ArrayList<InfoResults>();

        for (int i = 0; i < solutionsList.size(); i++) {
            int numberOfVariables = solutionsList.get(0).getDecisionVariables().length;

            for (int j = 0; j < numberOfVariables; j++) {
                Architecture arch = (Architecture) solutionsList.get(i).getDecisionVariables()[j];

                InfoResults ir = new InfoResults();
                ir.setExecutionResults(executionResults);
                ir.setExperiement(experiement);
                if (executionResults == null)
                    ir.setIsAll(1);
                ir.setName(plaName + "_" + ir.getId());
                ir.setListOfConcerns(getListOfConcerns(arch.getAllConcerns()));
                ir.setNumberOfPackages(arch.getAllPackages().size());
                ir.setNumberOfVariabilities(arch.getAllVariabilities().size());
                ir.setNumberOfInterfaces(arch.getAllInterfaces().size());
                ir.setNumberOfClasses(arch.getAllClasses().size());
                ir.setNumberOfDependencies(arch.getRelationshipHolder().getAllDependencies().size());
                ir.setNumberOfAbstraction(arch.getRelationshipHolder().getAllAbstractions().size());
                ir.setNumberOfGeneralizations(arch.getRelationshipHolder().getAllGeneralizations().size());
                ir.setNumberOfAssociations(arch.getRelationshipHolder().getAllAssociationsRelationships().size());
                ir.setNumberOfassociationsClass(arch.getRelationshipHolder().getAllAssociationsClass().size());
                ir.setUserEvaluation(solutionsList.get(i).getEvaluation());
                ir.setFreezedElements(arch.toStringFreezedElements());
                ir.setObjectives(Arrays.toString(solutionsList.get(i).getObjectives()));
                infoResults.add(ir);
            }
        }

        return infoResults;
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

    public AllMetrics getMetrics(List<InfoResults> funResults, List<Solution> list, ExecutionResults executionResults, ExperimentResults experiement, List<String> objectiveFuncs) {

        MetricsEvaluation metrics = new MetricsEvaluation();
        AllMetrics allMetrics = new AllMetrics();
        int numberOfVariables = list.get(0).getDecisionVariables().length;

        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < numberOfVariables; j++) {
                Architecture arch = (Architecture) list.get(i).getDecisionVariables()[j];
                String idSolution = funResults.get(i).getId();
                if (objectiveFuncs.contains("elegance"))
                    allMetrics.getElegance().add(buildEleganceMetrics(idSolution, executionResults, experiement, metrics, arch));
                if (objectiveFuncs.contains("PLAExtensibility"))
                    allMetrics.getPlaExtensibility().add(buildPLAExtensibilityMetrics(idSolution, executionResults, experiement, metrics, arch));
                if (objectiveFuncs.contains("conventional"))
                    allMetrics.getConventional().add(buildConventionalMetrics(idSolution, executionResults, experiement, metrics, arch));
                if (objectiveFuncs.contains("featureDriven"))
                    allMetrics.getFeatureDriven().add(buildFeatureDrivenMetrics(idSolution, executionResults, experiement, metrics, arch));
                if (objectiveFuncs.contains("acomp"))
                    allMetrics.getAcomp().add(buildAcompMetrics(idSolution, executionResults, experiement, metrics, arch));
                if (objectiveFuncs.contains("aclass"))
                    allMetrics.getAclass().add(buildAclassMetrics(idSolution, executionResults, experiement, metrics, arch));
                if (objectiveFuncs.contains("tam"))
                    allMetrics.getTam().add(buildTamMetrics(idSolution, executionResults, experiement, metrics, arch));
                if (objectiveFuncs.contains("coe"))
                    allMetrics.getCoe().add(buildCoeMetrics(idSolution, executionResults, experiement, metrics, arch));
                if (objectiveFuncs.contains("dc"))
                    allMetrics.getDc().add(buildDcMetrics(idSolution, executionResults, experiement, metrics, arch));
                if (objectiveFuncs.contains("ec"))
                    allMetrics.getEc().add(buildEcMetrics(idSolution, executionResults, experiement, metrics, arch));
                //addYni
                if (objectiveFuncs.contains("wocsc"))
                    allMetrics.getWocsclass().add(buildWocsclassMetrics(idSolution, executionResults, experiement, metrics, arch));
                if (objectiveFuncs.contains("wocsi"))
                    allMetrics.getWocsinterface().add(buildWocsinterfaceMetrics(idSolution, executionResults, experiement, metrics, arch));
                if (objectiveFuncs.contains("cbcs"))
                    allMetrics.getCbcs().add(buildCbcsMetrics(idSolution, executionResults, experiement, metrics, arch));
                if (objectiveFuncs.contains("ssc"))
                    allMetrics.getSsc().add(buildSscMetrics(idSolution, executionResults, experiement, metrics, arch));
                if (objectiveFuncs.contains("svc"))
                    allMetrics.getSvc().add(buildSvcMetrics(idSolution, executionResults, experiement, metrics, arch));
                if (objectiveFuncs.contains("av"))
                    allMetrics.getAv().add(buildAvMetrics(idSolution, executionResults, experiement, metrics, arch));

                //addYni
            }
        }

        return allMetrics;
    }

    private FeatureDriven buildFeatureDrivenMetrics(String idSolution, ExecutionResults executionResults, ExperimentResults experiement,
                                                    MetricsEvaluation metrics, Architecture arch) {

        FeatureDriven fd = new FeatureDriven(idSolution, executionResults, experiement);

        fd.setCdac(metrics.evaluateCDAC(arch));
        fd.setCdai(metrics.evaluateCDAI(arch));
        fd.setCdao(metrics.evaluateCDAO(arch));
        fd.setCibc(metrics.evaluateCIBC(arch));
        fd.setIibc(metrics.evaluateIIBC(arch));
        fd.setOobc(metrics.evaluateOOBC(arch));
        fd.setLcc(metrics.evaluateLCC(arch));
        fd.setLccClass(metrics.evaluateLCCClass(arch));
        fd.setCdaClass(metrics.evaluateCDAClass(arch));
        fd.setCibClass(metrics.evaluateCIBClass(arch));

        return fd;
    }

    private Conventional buildConventionalMetrics(String idSolution, ExecutionResults executionResults, ExperimentResults experiement,
                                                  MetricsEvaluation metrics, Architecture arch) {

        Conventional conventional = new Conventional(idSolution, executionResults, experiement);

        conventional.setSumCohesion(metrics.evaluateCohesion(arch));
        conventional.setCohesion(metrics.evaluateICohesion(conventional.getSumChoesion()));
        conventional.setMeanDepComps(metrics.evaluateMeanDepComps(arch));
        conventional.setMeanNumOps(metrics.evaluateMeanNumOps(arch));
        conventional.setSumClassesDepIn(metrics.evaluateSumClassesDepIn(arch));
        conventional.setSumClassesDepOut(metrics.evaluateSumClassesDepOut(arch));
        conventional.setSumDepIn(metrics.evaluateSumDepIn(arch));
        conventional.setSumDepOut(metrics.evaluateSumDepOut(arch));

        return conventional;
    }

    private PLAExtensibility buildPLAExtensibilityMetrics(String idSolution, ExecutionResults executionResults,
                                                          ExperimentResults experiement, MetricsEvaluation metrics, Architecture arch) {

        PLAExtensibility plaExtensibility = new PLAExtensibility(idSolution, executionResults, experiement);
        plaExtensibility.setPlaExtensibility(metrics.evaluatePLAExtensibility(arch));

        return plaExtensibility;
    }

    private Elegance buildEleganceMetrics(String idSolution, ExecutionResults executionResults, ExperimentResults experiement,
                                          MetricsEvaluation metrics, Architecture arch) {

        Elegance elegance = new Elegance(idSolution, executionResults, experiement);
        elegance.setNac(metrics.evaluateNACElegance(arch));
        elegance.setAtmr(metrics.evaluateATMRElegance(arch));
        elegance.setEc(metrics.evaluateECElegance(arch));

        return elegance;
    }

    private Acomp buildAcompMetrics(String idSolution, ExecutionResults executionResults, ExperimentResults experiement,
                                    MetricsEvaluation metrics, Architecture arch) {

        Acomp acomp = new Acomp(idSolution, executionResults, experiement);

        acomp.setSumDepIn(metrics.evaluateSumDepIn(arch));
        acomp.setSumDepOut(metrics.evaluateSumDepOut(arch));

        return acomp;
    }

    private Aclass buildAclassMetrics(String idSolution, ExecutionResults executionResults, ExperimentResults experiement,
                                      MetricsEvaluation metrics, Architecture arch) {

        Aclass aclass = new Aclass(idSolution, executionResults, experiement);

        aclass.setSumClassesDepIn(metrics.evaluateSumClassesDepIn(arch));
        aclass.setSumClassesDepOut(metrics.evaluateSumClassesDepOut(arch));

        return aclass;
    }

    private Tam buildTamMetrics(String idSolution, ExecutionResults executionResults, ExperimentResults experiement,
                                MetricsEvaluation metrics, Architecture arch) {

        Tam tam = new Tam(idSolution, executionResults, experiement);

        tam.setMeanNumOps(metrics.evaluateMeanNumOps(arch));

        return tam;
    }

    private Coe buildCoeMetrics(String idSolution, ExecutionResults executionResults, ExperimentResults experiement,
                                MetricsEvaluation metrics, Architecture arch) {

        Coe coe = new Coe(idSolution, executionResults, experiement);

        coe.setLcc(metrics.evaluateLCC(arch));
        coe.setCohesion(metrics.evaluateCohesion(arch));


        return coe;
    }

    private Dc buildDcMetrics(String idSolution, ExecutionResults executionResults, ExperimentResults experiement,
                              MetricsEvaluation metrics, Architecture arch) {

        Dc dc = new Dc(idSolution, executionResults, experiement);

        dc.setCdai(metrics.evaluateCDAI(arch));
        dc.setCdao(metrics.evaluateCDAO(arch));
        dc.setCdac(metrics.evaluateCDAC(arch));


        return dc;
    }

    private Ec buildEcMetrics(String idSolution, ExecutionResults executionResults, ExperimentResults experiement,
                              MetricsEvaluation metrics, Architecture arch) {

        Ec ec = new Ec(idSolution, executionResults, experiement);

        ec.setCibc(metrics.evaluateCIBC(arch));
        ec.setIibc(metrics.evaluateIIBC(arch));
        ec.setOobc(metrics.evaluateOOBC(arch));


        return ec;
    }

    //addYni


    private Wocsclass buildWocsclassMetrics(String idSolution, ExecutionResults executionResults,
                                            ExperimentResults experiement, MetricsEvaluation metrics, Architecture arch) {

        Wocsclass wocsClass = new Wocsclass(idSolution, executionResults, experiement);
        wocsClass.setWocsClass(metrics.evaluateWocsClass(arch));

        return wocsClass;
    }

    private Wocsinterface buildWocsinterfaceMetrics(String idSolution, ExecutionResults executionResults,
                                                    ExperimentResults experiement, MetricsEvaluation metrics, Architecture arch) {

        Wocsinterface wocsInterface = new Wocsinterface(idSolution, executionResults, experiement);
        wocsInterface.setWocsInterface(metrics.evaluateWocsInterface(arch));

        return wocsInterface;
    }

    private Cbcs buildCbcsMetrics(String idSolution, ExecutionResults executionResults,
                                  ExperimentResults experiement, MetricsEvaluation metrics, Architecture arch) {

        Cbcs cBcs = new Cbcs(idSolution, executionResults, experiement);
        cBcs.setCbcs(metrics.evaluateWocsInterface(arch));

        return cBcs;
    }

    private Ssc buildSscMetrics(String idSolution, ExecutionResults executionResults,
                                ExperimentResults experiement, MetricsEvaluation metrics, Architecture arch) {

        Ssc sSc = new Ssc(idSolution, executionResults, experiement);
        sSc.setSsc(metrics.evaluateWocsInterface(arch));

        return sSc;
    }


    private Svc buildSvcMetrics(String idSolution, ExecutionResults executionResults,
                                ExperimentResults experiement, MetricsEvaluation metrics, Architecture arch) {

        Svc sVc = new Svc(idSolution, executionResults, experiement);
        sVc.setSvc(metrics.evaluateWocsInterface(arch));

        return sVc;
    }


    private Av buildAvMetrics(String idSolution, ExecutionResults executionResults,
                              ExperimentResults experiement, MetricsEvaluation metrics, Architecture arch) {

        Av aV = new Av(idSolution, executionResults, experiement);
        aV.setAv(metrics.evaluateWocsInterface(arch));

        return aV;
    }

    //addYni

}
