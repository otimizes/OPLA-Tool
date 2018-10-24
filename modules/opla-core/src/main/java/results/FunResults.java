/*
Fun files is where objective values are stored.
 */
package results;

import utils.Id;

/**
 * @author elf`
 */
public class FunResults {

    private String id;
    private String name;
    private String solution_name; // mesmo nome do arquivo da arquitetura gerado
    private Execution execution;
    private int isAll;
    private Experiment experiement;
    /**
     * Objectives are stored on a single attribute, the values are separated by
     * pipe ("|").
     */
    private String objectives;

    public FunResults() {
        this.id = Id.generateUniqueId();
    }

    public String getObjectives() {
        return objectives;
    }

    /**
     * String objs should be a string of values separated with pipes |.
     * <p>
     * Ex: 0.19191919|0.199193393|39393993
     *
     * @param objs
     */
    public void setObjectives(String objs) {
        this.objectives = objs;
    }

    public String getId() {
        return this.id;
    }

    public Execution getExecution() {
        return this.execution;
    }

    public void setExecution(Execution execution) {
        this.execution = execution;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = "FUN_" + name;

    }

    public int getIsAll() {
        return isAll;
    }

    /*
     * 0 - false 1 - true
     * 
     * Using int type because SQLITE don't have a boolean type
     */
    public void setIsAll(int isAll) {
        this.isAll = isAll;
    }

    public Experiment getExperiement() {
        return this.experiement;
    }

    public void setExperiement(Experiment experiement) {
        this.experiement = experiement;
    }

    public String getSolution_name() {
        return solution_name;
    }

    public void setSolution_name(String solution_name) {
        this.solution_name = solution_name;
    }


}
