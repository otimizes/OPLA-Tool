package br.ufpr.dinf.gres.persistence.dao;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.RCCObjectiveFunction;
import br.ufpr.dinf.gres.persistence.util.GenericDAOImpl;
import br.ufpr.dinf.gres.persistence.util.GenericMetricDAO;
import org.apache.log4j.Logger;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Fernando
 */
public class CbcsMetricDAO extends GenericDAOImpl<RCCObjectiveFunction>
        implements GenericMetricDAO<RCCObjectiveFunction> {

    private static final Logger LOGGER = Logger.getLogger(CbcsMetricDAO.class);

    private static final long serialVersionUID = 1L;

    public CbcsMetricDAO() {
        super(RCCObjectiveFunction.class);
    }

    @Override
    public List<RCCObjectiveFunction> findBySolution(String solution) {
        LOGGER.debug("Finding metric date for: " + solution);

        String idSolution = solution.split("-")[1];

        TypedQuery<RCCObjectiveFunction> query = getEntityManager().createQuery(
                "SELECT o FROM CbcsMetric o WHERE o.idSolution = :idSolution", RCCObjectiveFunction.class);
        query.setParameter("idSolution", idSolution);

        List<RCCObjectiveFunction> resultList = query.getResultList();
        LOGGER.debug("Number of br.ufpr.dinf.gres.core.jmetal4.results: " + resultList.size());
        return resultList;
    }

}
