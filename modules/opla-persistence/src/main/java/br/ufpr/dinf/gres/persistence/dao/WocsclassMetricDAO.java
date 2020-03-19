package br.ufpr.dinf.gres.persistence.dao;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.WOCSCLASSObjectiveFunction;
import br.ufpr.dinf.gres.persistence.util.GenericDAOImpl;
import br.ufpr.dinf.gres.persistence.util.GenericMetricDAO;
import org.apache.log4j.Logger;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Fernando
 */
public class WocsclassMetricDAO extends GenericDAOImpl<WOCSCLASSObjectiveFunction>
        implements GenericMetricDAO<WOCSCLASSObjectiveFunction> {

    private static final Logger LOGGER = Logger.getLogger(WocsclassMetricDAO.class);

    private static final long serialVersionUID = 1L;

    public WocsclassMetricDAO() {
        super(WOCSCLASSObjectiveFunction.class);
    }

    @Override
    public List<WOCSCLASSObjectiveFunction> findBySolution(String solution) {
        LOGGER.debug("Finding metric date for: " + solution);

        String idSolution = solution.split("-")[1];

        TypedQuery<WOCSCLASSObjectiveFunction> query = getEntityManager().createQuery(
                "SELECT o FROM WocsclassMetric o WHERE o.idSolution = :idSolution", WOCSCLASSObjectiveFunction.class);
        query.setParameter("idSolution", idSolution);

        List<WOCSCLASSObjectiveFunction> resultList = query.getResultList();
        LOGGER.debug("Number of br.ufpr.dinf.gres.core.jmetal4.results: " + resultList.size());
        return resultList;
    }

}
