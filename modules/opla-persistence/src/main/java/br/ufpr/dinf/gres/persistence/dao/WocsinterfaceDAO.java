package br.ufpr.dinf.gres.persistence.dao;

import br.ufpr.dinf.gres.opla.entity.metric.WocsinterfaceMetric;
import br.ufpr.dinf.gres.persistence.util.GenericDAOImpl;
import br.ufpr.dinf.gres.persistence.util.GenericMetricDAO;
import org.apache.log4j.Logger;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Fernando
 */
public class WocsinterfaceDAO extends GenericDAOImpl<WocsinterfaceMetric>
        implements GenericMetricDAO<WocsinterfaceMetric> {

    private static final Logger LOGGER = Logger.getLogger(WocsinterfaceDAO.class);

    private static final long serialVersionUID = 1L;

    public WocsinterfaceDAO() {
        super(WocsinterfaceMetric.class);
    }

    @Override
    public List<WocsinterfaceMetric> findBySolution(String solution) {
        LOGGER.debug("Finding metric date for: " + solution);

        String idSolution = solution.split("-")[1];

        TypedQuery<WocsinterfaceMetric> query = getEntityManager().createQuery(
                "SELECT o FROM WocsinterfaceMetric o WHERE o.idSolution = :idSolution", WocsinterfaceMetric.class);
        query.setParameter("idSolution", idSolution);

        List<WocsinterfaceMetric> resultList = query.getResultList();
        LOGGER.debug("Number of results: " + resultList.size());
        return resultList;
    }

}
