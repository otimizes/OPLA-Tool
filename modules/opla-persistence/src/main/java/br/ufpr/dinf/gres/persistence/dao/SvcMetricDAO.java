package br.ufpr.dinf.gres.persistence.dao;

import br.ufpr.dinf.gres.opla.entity.metric.SvcMetric;
import br.ufpr.dinf.gres.persistence.util.GenericDAOImpl;
import br.ufpr.dinf.gres.persistence.util.GenericMetricDAO;
import org.apache.log4j.Logger;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Fernando
 */
public class SvcMetricDAO extends GenericDAOImpl<SvcMetric>
        implements GenericMetricDAO<SvcMetric> {

    private static final Logger LOGGER = Logger.getLogger(SvcMetricDAO.class);

    private static final long serialVersionUID = 1L;

    public SvcMetricDAO() {
        super(SvcMetric.class);
    }

    @Override
    public List<SvcMetric> findBySolution(String solution) {
        LOGGER.debug("Finding metric date for: " + solution);

        String idSolution = solution.split("-")[1];

        TypedQuery<SvcMetric> query = getEntityManager().createQuery(
                "SELECT o FROM SvcMetric o WHERE o.idSolution = :idSolution", SvcMetric.class);
        query.setParameter("idSolution", idSolution);

        List<SvcMetric> resultList = query.getResultList();
        LOGGER.debug("Number of results: " + resultList.size());
        return resultList;
    }

}
