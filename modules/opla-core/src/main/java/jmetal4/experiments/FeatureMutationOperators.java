package jmetal4.experiments;

public enum FeatureMutationOperators {

    FEATURE_MUTATION("featureMutation"),
    MOVE_METHOD_MUTATION("moveMethodMutation"),
    MOVE_ATTRIBUTE_MUTATION("moveAttributeMutation"),
    MOVE_OPERATION_MUTATION("moveOperationMutation"),
    FEATURE_INTERACTION_MODULARIZATION_MUTATION("featureInteractionModularization"),
    ADD_CLASS_MUTATION("addClassMutation"),
    ADD_MANAGER_CLASS_MUTATION("addManagerClassMutation"),
    DESIGN_PATTERNS("DesignPatterns");

    private String name;

    private FeatureMutationOperators(String name) {
        this.name = name;
    }

    public String getOperatorName() {
        return name;
    }
}
