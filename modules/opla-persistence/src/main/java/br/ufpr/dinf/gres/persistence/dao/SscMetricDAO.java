package br.ufpr.dinf.gres.persistence.dao;

import br.ufpr.dinf.gres.opla.entity.metric.SscMetric;
import br.ufpr.dinf.gres.persistence.util.GenericDAOImpl;
import br.ufpr.dinf.gres.persistence.util.GenericMetricDAO;
import org.apache.log4j.Logger;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Fernando
 */
public class SscMetricDAO extends GenericDAOImpl<SscMetric>
        implements GenericMetricDAO<SscMetric> {

    private static final Logger LOGGER = Logger.getLogger(SscMetricDAO.class);

    private static final long serialVersionUID = 1L;

    public SscMetricDAO() {
        super(SscMetric.class);
    }

    @Override
    public List<SscMetric> findBySolution(String solution) {
        LOGGER.debug("Finding metric date for: " + solution);

        String idSolution = solution.split("-")[1];

        TypedQuery<SscMetric> query = getEntityManager().createQuery(
                "SELECT o FROM SscMetric o WHERE o.idSolution = :idSolution", SscMetric.class);
        query.setParameter("idSolution", idSolution);

        List<SscMetric> resultList = query.getResultList();
        LOGGER.debug("Number of results: " + resultList.size());
        return resultList;
    }

}
