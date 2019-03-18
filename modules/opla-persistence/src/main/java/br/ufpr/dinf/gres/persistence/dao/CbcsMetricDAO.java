package br.ufpr.dinf.gres.persistence.dao;

import br.ufpr.dinf.gres.opla.entity.metric.AvMetric;
import br.ufpr.dinf.gres.opla.entity.metric.CbcsMetric;
import br.ufpr.dinf.gres.persistence.util.GenericDAOImpl;
import br.ufpr.dinf.gres.persistence.util.GenericMetricDAO;
import org.apache.log4j.Logger;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Fernando
 */
public class CbcsMetricDAO extends GenericDAOImpl<CbcsMetric>
        implements GenericMetricDAO<CbcsMetric> {

    private static final Logger LOGGER = Logger.getLogger(CbcsMetricDAO.class);

    private static final long serialVersionUID = 1L;

    public CbcsMetricDAO() {
        super(CbcsMetric.class);
    }

    @Override
    public List<CbcsMetric> findBySolution(String solution) {
        LOGGER.debug("Finding metric date for: " + solution);

        String idSolution = solution.split("-")[1];

        TypedQuery<CbcsMetric> query = getEntityManager().createQuery(
                "SELECT o FROM CbcsMetric o WHERE o.idSolution = :idSolution", CbcsMetric.class);
        query.setParameter("idSolution", idSolution);

        List<CbcsMetric> resultList = query.getResultList();
        LOGGER.debug("Number of results: " + resultList.size());
        return resultList;
    }

}
