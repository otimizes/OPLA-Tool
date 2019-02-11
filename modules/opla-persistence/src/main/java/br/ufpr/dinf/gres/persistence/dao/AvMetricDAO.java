package br.ufpr.dinf.gres.persistence.dao;

import br.ufpr.dinf.gres.opla.entity.metric.AvMetric;
import br.ufpr.dinf.gres.persistence.util.GenericDAOImpl;
import br.ufpr.dinf.gres.persistence.util.GenericMetricDAO;
import org.apache.log4j.Logger;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Fernando
 */
public class AvMetricDAO extends GenericDAOImpl<AvMetric>
        implements GenericMetricDAO<AvMetric> {

    private static final Logger LOGGER = Logger.getLogger(AvMetricDAO.class);

    private static final long serialVersionUID = 1L;

    public AvMetricDAO() {
        super(AvMetric.class);
    }

    @Override
    public List<AvMetric> findBySolution(String solution) {
        LOGGER.debug("Finding metric date for: " + solution);

        String idSolution = solution.split("-")[1];

        TypedQuery<AvMetric> query = getEntityManager().createQuery(
                "SELECT o FROM AvMetric o WHERE o.idSolution = :idSolution", AvMetric.class);
        query.setParameter("idSolution", idSolution);

        List<AvMetric> resultList = query.getResultList();
        LOGGER.debug("Number of results: " + resultList.size());
        return resultList;
    }

}
