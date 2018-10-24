package br.ufpr.dinf.gres.persistence.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.ufpr.dinf.gres.opla.entity.metric.EleganceMetric;
import br.ufpr.dinf.gres.persistence.util.GenericDAOImpl;
import br.ufpr.dinf.gres.persistence.util.GenericMetricDAO;

/**
 * 
 * @author Fernando
 *
 */
public class EleganceMetricDAO extends GenericDAOImpl<EleganceMetric> implements GenericMetricDAO<EleganceMetric> {

	private static final Logger LOGGER = Logger.getLogger(EleganceMetricDAO.class);

	private static final long serialVersionUID = 1L;

	public EleganceMetricDAO() {
		super(EleganceMetric.class);
	}

	@Override
	public List<EleganceMetric> findBySolution(String solution) {
		LOGGER.debug("Finding metric date for: " + solution);

		String idSolution = solution.split("-")[1];

		TypedQuery<EleganceMetric> query = getEntityManager()
				.createQuery("SELECT o FROM EleganceMetric o WHERE o.idSolution = :idSolution", EleganceMetric.class);
		query.setParameter("idSolution", idSolution);

		List<EleganceMetric> resultList = query.getResultList();
		LOGGER.debug("Number of results: " + resultList.size());
		return resultList;
	}

}
