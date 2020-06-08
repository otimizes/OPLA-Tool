package br.otimizes.oplatool.core.jmetal4.operators.pattern;

import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.operators.mutation.Mutation;
import br.otimizes.oplatool.architecture.exceptions.ClassNotFound;
import br.otimizes.oplatool.architecture.exceptions.ConcernNotFoundException;
import br.otimizes.oplatool.architecture.exceptions.NotFoundException;
import br.otimizes.oplatool.architecture.exceptions.PackageNotFound;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.common.Configuration;
import br.otimizes.oplatool.common.exceptions.JMException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Abstract mutation operator class
 */
public abstract class AbstractMutationOperator extends Mutation {

    public static final Logger LOGGER = LogManager.getLogger(AbstractMutationOperator.class);
    private static final long serialVersionUID = 1L;

    public AbstractMutationOperator(Map<String, Object> parameters) {
        super(parameters);
    }

    @Override
    public Object execute(Object o) throws JMException, CloneNotSupportedException, ClassNotFound, PackageNotFound, NotFoundException, ConcernNotFoundException {
        Solution solution = (Solution) o;
        Double probability = (Double) getParameter("probability");

        if (probability == null) {
            Configuration.logger_.severe("FeatureMutation.execute: probability not specified");
            java.lang.Class<String> cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        }

        try {
            hookMutation(solution, probability);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(AbstractMutationOperator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return solution;
    }

    protected abstract boolean hookMutation(Solution solution, Double probability) throws Exception;

    protected boolean isValidSolution(Architecture solution) {
        boolean isValid = true;
        List<Interface> allInterfaces = new ArrayList<>(solution.getAllInterfaces());
        if (!allInterfaces.isEmpty()) {
            for (Interface itf : allInterfaces) {
                if ((itf.getImplementors().isEmpty()) && (itf.getDependents().isEmpty()) && (!itf.getMethods().isEmpty())) {
                    return false;
                }
            }
        }
        return isValid;
    }

}
