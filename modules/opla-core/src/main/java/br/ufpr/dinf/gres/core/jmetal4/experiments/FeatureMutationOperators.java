package br.ufpr.dinf.gres.core.jmetal4.experiments;

import br.ufpr.dinf.gres.core.jmetal4.operators.mutation.*;

public enum FeatureMutationOperators implements IFeatureMutationOperators {

    FEATURE_MUTATION {
        @Override
        public IMutationOperator getOperator() {
            return new FeatureMutation();
        }
    },
    MOVE_METHOD_MUTATION {
        @Override
        public IMutationOperator getOperator() {
            return new MoveMethodMutation();
        }
    },
    MOVE_ATTRIBUTE_MUTATION {
        @Override
        public IMutationOperator getOperator() {
            return new MoveAttributeMutation();
        }
    },
    MOVE_OPERATION_MUTATION {
        @Override
        public IMutationOperator getOperator() {
            return new MoveOperationMutation();
        }
    },
    ADD_CLASS_MUTATION {
        @Override
        public IMutationOperator getOperator() {
            return new AddClassMutation();
        }
    },
    ADD_MANAGER_CLASS_MUTATION {
        @Override
        public IMutationOperator getOperator() {
            return new AddManagerClassMutation();
        }
    },
    DESIGN_PATTERNS {
        @Override
        public IMutationOperator getOperator() {
            return null;
        }
    };
}
