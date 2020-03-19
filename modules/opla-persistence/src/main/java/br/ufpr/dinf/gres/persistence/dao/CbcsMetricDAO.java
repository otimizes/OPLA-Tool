package br.ufpr.dinf.gres.persistence.dao;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.CBCSObjectiveFunction;
import br.ufpr.dinf.gres.persistence.util.GenericDAOImpl;
import br.ufpr.dinf.gres.persistence.util.GenericMetricDAO;
import org.apache.log4j.Logger;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Fernando
 */
public class CbcsMetricDAO extends GenericDAOImpl<CBCSObjectiveFunction>
        implements GenericMetricDAO<CBCSObjectiveFunction> {

    private static final Logger LOGGER = Logger.getLogger(CbcsMetricDAO.class);

    private static final long serialVersionUID = 1L;

    public CbcsMetricDAO() {
        super(CBCSObjectiveFunction.class);
    }

    @Override
    public List<CBCSObjectiveFunction> findBySolution(String solution) {
        LOGGER.debug("Finding metric date for: " + solution);

        String idSolution = solution.split("-")[1];

        TypedQuery<CBCSObjectiveFunction> query = getEntityManager().createQuery(
                "SELECT o FROM CbcsMetric o WHERE o.idSolution = :idSolution", CBCSObjectiveFunction.class);
        query.setParameter("idSolution", idSolution);

        List<CBCSObjectiveFunction> resultList = query.getResultList();
        LOGGER.debug("Number of br.ufpr.dinf.gres.core.jmetal4.results: " + resultList.size());
        return resultList;
    }

}
