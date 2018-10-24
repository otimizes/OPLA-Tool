package br.ufpr.dinf.gres.persistence.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.ufpr.dinf.gres.opla.entity.metric.PLAExtensibilityMetric;
import br.ufpr.dinf.gres.persistence.util.GenericDAOImpl;
import br.ufpr.dinf.gres.persistence.util.GenericMetricDAO;

/**
 * 
 * @author Fernando
 *
 */
public class PLAExtensibilityMetricDAO extends GenericDAOImpl<PLAExtensibilityMetric> implements GenericMetricDAO<PLAExtensibilityMetric> {
	
	private static final Logger LOGGER = Logger.getLogger(PLAExtensibilityMetricDAO.class);

	private static final long serialVersionUID = 1L;

	public PLAExtensibilityMetricDAO() {
		super(PLAExtensibilityMetric.class);
	}

	@Override
	public List<PLAExtensibilityMetric> findBySolution(String solution) {
		LOGGER.debug("Finding metric date for: " + solution);

		String idSolution = solution.split("-")[1];

		TypedQuery<PLAExtensibilityMetric> query = getEntityManager().createQuery(
				"SELECT o FROM PLAExtensibilityMetric o WHERE o.idSolution = :idSolution", PLAExtensibilityMetric.class);
		query.setParameter("idSolution", idSolution);

		List<PLAExtensibilityMetric> resultList = query.getResultList();
		LOGGER.debug("Number of results: " + resultList.size());
		return resultList;
	}

	

}
