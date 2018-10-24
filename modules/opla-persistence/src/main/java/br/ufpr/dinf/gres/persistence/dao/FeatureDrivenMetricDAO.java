package br.ufpr.dinf.gres.persistence.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.ufpr.dinf.gres.opla.entity.metric.FeatureDrivenMetric;
import br.ufpr.dinf.gres.persistence.util.GenericDAOImpl;
import br.ufpr.dinf.gres.persistence.util.GenericMetricDAO;

/**
 * 
 * @author Fernando
 *
 */
public class FeatureDrivenMetricDAO extends GenericDAOImpl<FeatureDrivenMetric>
		implements GenericMetricDAO<FeatureDrivenMetric> {
	
	private static final Logger LOGGER = Logger.getLogger(FeatureDrivenMetricDAO.class);

	private static final long serialVersionUID = 1L;

	public FeatureDrivenMetricDAO() {
		super(FeatureDrivenMetric.class);
	}

	@Override
	public List<FeatureDrivenMetric> findBySolution(String solution) {
		LOGGER.debug("Finding metric date for: " + solution);

		String idSolution = solution.split("-")[1];

		TypedQuery<FeatureDrivenMetric> query = getEntityManager().createQuery(
				"SELECT o FROM FeatureDrivenMetric o WHERE o.idSolution = :idSolution", FeatureDrivenMetric.class);
		query.setParameter("idSolution", idSolution);

		List<FeatureDrivenMetric> resultList = query.getResultList();
		LOGGER.debug("Number of results: " + resultList.size());
		return resultList;
	}


}
