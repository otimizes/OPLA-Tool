package br.otimizes.oplatool.core.jmetal4.operators;

import br.otimizes.oplatool.core.jmetal4.operators.mutation.*;

/**
 * Feature mutation operators enum
 * <p>
 * {@link FeatureDrivenOperator Feature Mutation,}
 * {@link MoveMethodMutation Move Method Mutation,}
 * {@link MoveAttributeMutation Move Attribute Mutation,}
 * {@link MoveOperationMutation Move Operation Mutation,}
 * {@link AddClassMutation Add Class Mutation,}
 * {@link AddManagerClassMutation Add Manager Class Mutation}
 * {@link FeatureDrivenOperatorForClass Feature Mutation for class}
 */
public enum MutationOperators implements IOperators {

    FEATURE_DRIVEN_OPERATOR {
        @Override
        public IOperator getOperator() {
            return new FeatureDrivenOperator();
        }
    },
    MOVE_METHOD_MUTATION {
        @Override
        public IOperator getOperator() {
            return new MoveMethodMutation();
        }
    },
    MOVE_ATTRIBUTE_MUTATION {
        @Override
        public IOperator getOperator() {
            return new MoveAttributeMutation();
        }
    },
    MOVE_OPERATION_MUTATION {
        @Override
        public IOperator getOperator() {
            return new MoveOperationMutation();
        }
    },
    ADD_CLASS_MUTATION {
        @Override
        public IOperator getOperator() {
            return new AddClassMutation();
        }
    },
    ADD_MANAGER_CLASS_MUTATION {
        @Override
        public IOperator getOperator() {
            return new AddManagerClassMutation();
        }
    },
    FEATURE_DRIVEN_OPERATOR_FOR_CLASS {
        @Override
        public IOperator getOperator() {
            return new FeatureDrivenOperatorForClass();
        }
    },
    DESIGN_PATTERNS {
        @Override
        public IOperator getOperator() {
            return null;
        }
    }
}
