package br.ufpr.dinf.gres.persistence.dao;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.CSObjectiveFunction;
import br.ufpr.dinf.gres.persistence.util.GenericDAOImpl;
import br.ufpr.dinf.gres.persistence.util.GenericMetricDAO;
import org.apache.log4j.Logger;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Fernando
 */
public class WocsinterfaceDAO extends GenericDAOImpl<CSObjectiveFunction>
        implements GenericMetricDAO<CSObjectiveFunction> {

    private static final Logger LOGGER = Logger.getLogger(WocsinterfaceDAO.class);

    private static final long serialVersionUID = 1L;

    public WocsinterfaceDAO() {
        super(CSObjectiveFunction.class);
    }

    @Override
    public List<CSObjectiveFunction> findBySolution(String solution) {
        LOGGER.debug("Finding metric date for: " + solution);

        String idSolution = solution.split("-")[1];

        TypedQuery<CSObjectiveFunction> query = getEntityManager().createQuery(
                "SELECT o FROM WocsinterfaceMetric o WHERE o.idSolution = :idSolution", CSObjectiveFunction.class);
        query.setParameter("idSolution", idSolution);

        List<CSObjectiveFunction> resultList = query.getResultList();
        LOGGER.debug("Number of br.ufpr.dinf.gres.core.jmetal4.results: " + resultList.size());
        return resultList;
    }

}
