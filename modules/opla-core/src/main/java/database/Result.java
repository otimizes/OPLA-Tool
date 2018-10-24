package database;

import arquitetura.representation.Architecture;
import arquitetura.representation.Concern;
import jmetal4.core.Solution;
import jmetal4.metrics.MetricsEvaluation;
import metrics.*;
import results.Execution;
import results.Experiment;
import results.FunResults;
import results.InfoResult;

import java.util.ArrayList;
import java.util.List;

public class Result {

    private String plaName;

    public void setPlaName(String plaName) {
        this.plaName = plaName;
    }

    /**
     * Returns {@link FunResults} given a {@link List} of {@link Solution} and a
     * executionId.<br />
     * <p>
     * Pass null to execution when are ALL results. So results belongs to
     * experiment and not to execution (run).
     * <p>
     * See {@link ResultTest} for more details
     *
     * @param list
     * @param experiement
     * @param execution
     * @return
     */
    public List<FunResults> getObjectives(List<Solution> list, Execution execution, Experiment experiement) {

        List<FunResults> funResults = new ArrayList<FunResults>();

        for (int i = 0; i < list.size(); i++) {
            String sb = (list.get(i).toString().trim()).replace(" ", "|");

            FunResults funResult = new FunResults();
            funResult.setName(plaName + "_" + funResult.getId());
            funResult.setExecution(execution);
            funResult.setExperiement(experiement);
            if (execution == null)
                funResult.setIsAll(1);
            funResult.setObjectives(sb.replaceAll("\\s+", ""));
            funResults.add(funResult);
        }

        return funResults;
    }

    public List<InfoResult> getInformations(List<Solution> solutionsList, Execution execution, Experiment experiement) {

        List<InfoResult> infoResults = new ArrayList<InfoResult>();

        for (int i = 0; i < solutionsList.size(); i++) {
            int numberOfVariables = solutionsList.get(0).getDecisionVariables().length;

            for (int j = 0; j < numberOfVariables; j++) {
                Architecture arch = (Architecture) solutionsList.get(i).getDecisionVariables()[j];

                InfoResult ir = new InfoResult();
                ir.setExecution(execution);
                ir.setExperiement(experiement);
                if (execution == null)
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

    public AllMetrics getMetrics(List<FunResults> funResults, List<Solution> list, Execution execution, Experiment experiement, List<String> objectiveFuncs) {

        MetricsEvaluation metrics = new MetricsEvaluation();
        AllMetrics allMetrics = new AllMetrics();
        int numberOfVariables = list.get(0).getDecisionVariables().length;

        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < numberOfVariables; j++) {
                Architecture arch = (Architecture) list.get(i).getDecisionVariables()[j];
                String idSolution = funResults.get(i).getId();
                if (objectiveFuncs.contains("elegance"))
                    allMetrics.getElegance().add(buildEleganceMetrics(idSolution, execution, experiement, metrics, arch));
                if (objectiveFuncs.contains("PLAExtensibility"))
                    allMetrics.getPlaExtensibility().add(buildPLAExtensibilityMetrics(idSolution, execution, experiement, metrics, arch));
                if (objectiveFuncs.contains("conventional"))
                    allMetrics.getConventional().add(buildConventionalMetrics(idSolution, execution, experiement, metrics, arch));
                if (objectiveFuncs.contains("featureDriven"))
                    allMetrics.getFeatureDriven().add(buildFeatureDrivenMetrics(idSolution, execution, experiement, metrics, arch));
                if (objectiveFuncs.contains("acomp"))
                    allMetrics.getAcomp().add(buildAcompMetrics(idSolution, execution, experiement, metrics, arch));
                if (objectiveFuncs.contains("aclass"))
                    allMetrics.getAclass().add(buildAclassMetrics(idSolution, execution, experiement, metrics, arch));
                if (objectiveFuncs.contains("tam"))
                    allMetrics.getTam().add(buildTamMetrics(idSolution, execution, experiement, metrics, arch));
                if (objectiveFuncs.contains("coe"))
                    allMetrics.getCoe().add(buildCoeMetrics(idSolution, execution, experiement, metrics, arch));
                if (objectiveFuncs.contains("dc"))
                    allMetrics.getDc().add(buildDcMetrics(idSolution, execution, experiement, metrics, arch));
                if (objectiveFuncs.contains("ec"))
                    allMetrics.getEc().add(buildEcMetrics(idSolution, execution, experiement, metrics, arch));
            }
        }

        return allMetrics;
    }

    private FeatureDriven buildFeatureDrivenMetrics(String idSolution, Execution execution, Experiment experiement,
                                                    MetricsEvaluation metrics, Architecture arch) {

        FeatureDriven fd = new FeatureDriven(idSolution, execution, experiement);

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

    private Conventional buildConventionalMetrics(String idSolution, Execution execution, Experiment experiement,
                                                  MetricsEvaluation metrics, Architecture arch) {

        Conventional conventional = new Conventional(idSolution, execution, experiement);

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

    private PLAExtensibility buildPLAExtensibilityMetrics(String idSolution, Execution execution,
                                                          Experiment experiement, MetricsEvaluation metrics, Architecture arch) {

        PLAExtensibility plaExtensibility = new PLAExtensibility(idSolution, execution, experiement);
        plaExtensibility.setPlaExtensibility(metrics.evaluatePLAExtensibility(arch));

        return plaExtensibility;
    }

    private Elegance buildEleganceMetrics(String idSolution, Execution execution, Experiment experiement,
                                          MetricsEvaluation metrics, Architecture arch) {

        Elegance elegance = new Elegance(idSolution, execution, experiement);
        elegance.setNac(metrics.evaluateNACElegance(arch));
        elegance.setAtmr(metrics.evaluateATMRElegance(arch));
        elegance.setEc(metrics.evaluateECElegance(arch));

        return elegance;
    }

    private Acomp buildAcompMetrics(String idSolution, Execution execution, Experiment experiement,
                                    MetricsEvaluation metrics, Architecture arch) {

        Acomp acomp = new Acomp(idSolution, execution, experiement);

        acomp.setSumDepIn(metrics.evaluateSumDepIn(arch));
        acomp.setSumDepOut(metrics.evaluateSumDepOut(arch));

        return acomp;
    }

    private Aclass buildAclassMetrics(String idSolution, Execution execution, Experiment experiement,
                                      MetricsEvaluation metrics, Architecture arch) {

        Aclass aclass = new Aclass(idSolution, execution, experiement);

        aclass.setSumClassesDepIn(metrics.evaluateSumClassesDepIn(arch));
        aclass.setSumClassesDepOut(metrics.evaluateSumClassesDepOut(arch));

        return aclass;
    }

    private Tam buildTamMetrics(String idSolution, Execution execution, Experiment experiement,
                                MetricsEvaluation metrics, Architecture arch) {

        Tam tam = new Tam(idSolution, execution, experiement);

        tam.setMeanNumOps(metrics.evaluateMeanNumOps(arch));

        return tam;
    }

    private Coe buildCoeMetrics(String idSolution, Execution execution, Experiment experiement,
                                MetricsEvaluation metrics, Architecture arch) {

        Coe coe = new Coe(idSolution, execution, experiement);

        coe.setLcc(metrics.evaluateLCC(arch));
        coe.setCohesion(metrics.evaluateCohesion(arch));


        return coe;
    }

    private Dc buildDcMetrics(String idSolution, Execution execution, Experiment experiement,
                              MetricsEvaluation metrics, Architecture arch) {

        Dc dc = new Dc(idSolution, execution, experiement);

        dc.setCdai(metrics.evaluateCDAI(arch));
        dc.setCdao(metrics.evaluateCDAO(arch));
        dc.setCdac(metrics.evaluateCDAC(arch));


        return dc;
    }

    private Ec buildEcMetrics(String idSolution, Execution execution, Experiment experiement,
                              MetricsEvaluation metrics, Architecture arch) {

        Ec ec = new Ec(idSolution, execution, experiement);

        ec.setCibc(metrics.evaluateCIBC(arch));
        ec.setIibc(metrics.evaluateIIBC(arch));
        ec.setOobc(metrics.evaluateOOBC(arch));


        return ec;
    }

}
