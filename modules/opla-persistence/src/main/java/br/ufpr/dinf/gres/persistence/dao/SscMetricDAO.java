package br.ufpr.dinf.gres.persistence.dao;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.SDObjectiveFunction;
import br.ufpr.dinf.gres.persistence.util.GenericDAOImpl;
import br.ufpr.dinf.gres.persistence.util.GenericMetricDAO;
import org.apache.log4j.Logger;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Fernando
 */
public class SscMetricDAO extends GenericDAOImpl<SDObjectiveFunction>
        implements GenericMetricDAO<SDObjectiveFunction> {

    private static final Logger LOGGER = Logger.getLogger(SscMetricDAO.class);

    private static final long serialVersionUID = 1L;

    public SscMetricDAO() {
        super(SDObjectiveFunction.class);
    }

    @Override
    public List<SDObjectiveFunction> findBySolution(String solution) {
        LOGGER.debug("Finding metric date for: " + solution);

        String idSolution = solution.split("-")[1];

        TypedQuery<SDObjectiveFunction> query = getEntityManager().createQuery(
                "SELECT o FROM SscMetric o WHERE o.idSolution = :idSolution", SDObjectiveFunction.class);
        query.setParameter("idSolution", idSolution);

        List<SDObjectiveFunction> resultList = query.getResultList();
        LOGGER.debug("Number of br.ufpr.dinf.gres.core.jmetal4.results: " + resultList.size());
        return resultList;
    }

}
