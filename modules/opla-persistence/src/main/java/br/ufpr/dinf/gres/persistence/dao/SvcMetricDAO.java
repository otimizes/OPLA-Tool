package br.ufpr.dinf.gres.persistence.dao;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.SVObjectiveFunction;
import br.ufpr.dinf.gres.persistence.util.GenericDAOImpl;
import br.ufpr.dinf.gres.persistence.util.GenericMetricDAO;
import org.apache.log4j.Logger;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Fernando
 */
public class SvcMetricDAO extends GenericDAOImpl<SVObjectiveFunction>
        implements GenericMetricDAO<SVObjectiveFunction> {

    private static final Logger LOGGER = Logger.getLogger(SvcMetricDAO.class);

    private static final long serialVersionUID = 1L;

    public SvcMetricDAO() {
        super(SVObjectiveFunction.class);
    }

    @Override
    public List<SVObjectiveFunction> findBySolution(String solution) {
        LOGGER.debug("Finding metric date for: " + solution);

        String idSolution = solution.split("-")[1];

        TypedQuery<SVObjectiveFunction> query = getEntityManager().createQuery(
                "SELECT o FROM SvcMetric o WHERE o.idSolution = :idSolution", SVObjectiveFunction.class);
        query.setParameter("idSolution", idSolution);

        List<SVObjectiveFunction> resultList = query.getResultList();
        LOGGER.debug("Number of br.ufpr.dinf.gres.core.jmetal4.results: " + resultList.size());
        return resultList;
    }

}
