package br.ufpr.dinf.gres.persistence.dao;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.TVObjectiveFunction;
import br.ufpr.dinf.gres.persistence.util.GenericDAOImpl;
import br.ufpr.dinf.gres.persistence.util.GenericMetricDAO;
import org.apache.log4j.Logger;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Fernando
 */
public class AvMetricDAO extends GenericDAOImpl<TVObjectiveFunction>
        implements GenericMetricDAO<TVObjectiveFunction> {

    private static final Logger LOGGER = Logger.getLogger(AvMetricDAO.class);

    private static final long serialVersionUID = 1L;

    public AvMetricDAO() {
        super(TVObjectiveFunction.class);
    }

    @Override
    public List<TVObjectiveFunction> findBySolution(String solution) {
        LOGGER.debug("Finding metric date for: " + solution);

        String idSolution = solution.split("-")[1];

        TypedQuery<TVObjectiveFunction> query = getEntityManager().createQuery(
                "SELECT o FROM AvMetric o WHERE o.idSolution = :idSolution", TVObjectiveFunction.class);
        query.setParameter("idSolution", idSolution);

        List<TVObjectiveFunction> resultList = query.getResultList();
        LOGGER.debug("Number of br.ufpr.dinf.gres.core.jmetal4.results: " + resultList.size());
        return resultList;
    }

}
