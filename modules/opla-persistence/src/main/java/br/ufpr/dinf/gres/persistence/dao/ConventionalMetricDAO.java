package br.ufpr.dinf.gres.persistence.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.ufpr.dinf.gres.opla.entity.metric.ConventionalMetric;
import br.ufpr.dinf.gres.persistence.util.GenericDAOImpl;
import br.ufpr.dinf.gres.persistence.util.GenericMetricDAO;

/**
 * 
 * @author Fernando
 *
 */
public class ConventionalMetricDAO extends GenericDAOImpl<ConventionalMetric>
		implements GenericMetricDAO<ConventionalMetric> {

	private static final Logger LOGGER = Logger.getLogger(ConventionalMetricDAO.class);

	private static final long serialVersionUID = 1L;

	public ConventionalMetricDAO() {
		super(ConventionalMetric.class);
	}

	@Override
	public List<ConventionalMetric> findBySolution(String solution) {
		LOGGER.debug("Finding metric date for: " + solution);

		String idSolution = solution.split("-")[1];

		TypedQuery<ConventionalMetric> query = getEntityManager().createQuery(
				"SELECT o FROM ConventionalMetric o WHERE o.idSolution = :idSolution", ConventionalMetric.class);
		query.setParameter("idSolution", idSolution);

		List<ConventionalMetric> resultList = query.getResultList();
		LOGGER.debug("Number of results: " + resultList.size());
		return resultList;
	}

}
