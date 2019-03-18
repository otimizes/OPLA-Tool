package br.ufpr.dinf.gres.persistence.dao;

import br.ufpr.dinf.gres.opla.entity.metric.WocsclassMetric;
import br.ufpr.dinf.gres.persistence.util.GenericDAOImpl;
import br.ufpr.dinf.gres.persistence.util.GenericMetricDAO;
import org.apache.log4j.Logger;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Fernando
 */
public class WocsclassMetricDAO extends GenericDAOImpl<WocsclassMetric>
        implements GenericMetricDAO<WocsclassMetric> {

    private static final Logger LOGGER = Logger.getLogger(WocsclassMetricDAO.class);

    private static final long serialVersionUID = 1L;

    public WocsclassMetricDAO() {
        super(WocsclassMetric.class);
    }

    @Override
    public List<WocsclassMetric> findBySolution(String solution) {
        LOGGER.debug("Finding metric date for: " + solution);

        String idSolution = solution.split("-")[1];

        TypedQuery<WocsclassMetric> query = getEntityManager().createQuery(
                "SELECT o FROM WocsclassMetric o WHERE o.idSolution = :idSolution", WocsclassMetric.class);
        query.setParameter("idSolution", idSolution);

        List<WocsclassMetric> resultList = query.getResultList();
        LOGGER.debug("Number of results: " + resultList.size());
        return resultList;
    }

}
