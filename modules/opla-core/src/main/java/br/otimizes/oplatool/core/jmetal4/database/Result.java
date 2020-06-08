package br.otimizes.oplatool.core.jmetal4.database;

import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctions;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Concern;
import br.otimizes.oplatool.domain.entity.Execution;
import br.otimizes.oplatool.domain.entity.Experiment;
import br.otimizes.oplatool.domain.entity.Info;
import br.otimizes.oplatool.domain.entity.objectivefunctions.ObjectiveFunctionDomain;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class with methods that fill objectives by solutions
 */
public class Result {

    private String plaName;

    public void setPlaName(String plaName) {
        this.plaName = plaName;
    }

    /**
     * Get infos at experiment end
     * @param solutions list of solutions
     * @param experiment experiment
     * @return list of infos
     */
    public List<Info> getInfos(List<Solution> solutions, Experiment experiment) {
        List<Info> funResults = new ArrayList<Info>();
        for (Solution solution : solutions) {
            String sb = (solution.toString().trim()).replace(" ", "|");

            Info funResult = new Info();
            funResult.setName(plaName);
            funResult.setExperiment(experiment);
            funResult.setIsAll(1);
            funResult.setObjectives(sb.replaceAll("\\s+", ""));
            funResults.add(funResult);
        }

        return funResults;
    }

    /**
     * Get infos during the experiment
     * @param solutions list of solutions
     * @param execution execution
     * @param experiment experiment
     * @return list of infos
     */
    public List<Info> getInfos(List<Solution> solutions, Execution execution,
                               Experiment experiment) {
        List<Info> Info = new ArrayList<>();

        for (int i = 0; i < solutions.size(); i++) {
            int numberOfVariables = solutions.get(0).getDecisionVariables().length;

            for (int j = 0; j < numberOfVariables; j++) {
                Architecture arch = (Architecture) solutions.get(i).getDecisionVariables()[j];

                Info ir = new Info();
                ir.setExecution(execution);
                ir.setExperiment(experiment);
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
                ir.setUserEvaluation(solutions.get(i).getEvaluation());
                ir.setFreezedElements(arch.toStringFreezedElements());
                ir.setObjectives(Arrays.toString(solutions.get(i).getObjectives()));
                Info.add(ir);
            }
        }

        return Info;
    }

    /**
     * Get string by concerns list
     * @param allConcerns concerns
     * @return list of concerns
     */
    private String getListOfConcerns(List<Concern> allConcerns) {
        StringBuilder concernsList = new StringBuilder();
        for (Concern concern : allConcerns)
            concernsList.append(concern.getName()).append("|");

        return concernsList.substring(0, concernsList.length() - 1);
    }

    /**
     * Get map of metrics
     * @param infos infos
     * @param solutions solutions
     * @param execution execution
     * @param experiment experiment
     * @param objectiveFunctions list of objective functions
     * @return map of metrics
     */
    public Map<String, List<ObjectiveFunctionDomain>> getMetrics(List<Info> infos, List<Solution> solutions, Execution execution, Experiment experiment, List<String> objectiveFunctions) {
        Map<String, List<ObjectiveFunctionDomain>> allMetrics = new HashMap<>();
        int numberOfVariables = solutions.get(0).getDecisionVariables().length;
        List<ObjectiveFunctions> collect = objectiveFunctions.stream().map(ObjectiveFunctions::valueOf).collect(Collectors.toList());

        for (int i = 0; i < solutions.size(); i++) {
            for (int j = 0; j < numberOfVariables; j++) {
                Architecture arch = (Architecture) solutions.get(i).getDecisionVariables()[j];
                String idSolution = infos.get(i).getId();
                for (ObjectiveFunctions fn : collect) {
                    ObjectiveFunctionDomain build = fn.build(idSolution, execution, experiment, arch);
                    allMetrics.computeIfAbsent(fn.toString(), f -> new ArrayList<>()).add(build);

                }
            }
        }
        return allMetrics;
    }
}


