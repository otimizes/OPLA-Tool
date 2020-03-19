package br.ufpr.dinf.gres.persistence.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.FMObjectiveFunction;
import br.ufpr.dinf.gres.persistence.util.GenericDAOImpl;
import br.ufpr.dinf.gres.persistence.util.GenericMetricDAO;

/**
 * 
 * @author Fernando
 *
 */
public class FeatureDrivenMetricDAO extends GenericDAOImpl<FMObjectiveFunction>
		implements GenericMetricDAO<FMObjectiveFunction> {
	
	private static final Logger LOGGER = Logger.getLogger(FeatureDrivenMetricDAO.class);

	private static final long serialVersionUID = 1L;

	public FeatureDrivenMetricDAO() {
		super(FMObjectiveFunction.class);
	}

	@Override
	public List<FMObjectiveFunction> findBySolution(String solution) {
		LOGGER.debug("Finding metric date for: " + solution);

		String idSolution = solution.split("-")[1];

		TypedQuery<FMObjectiveFunction> query = getEntityManager().createQuery(
				"SELECT o FROM FeatureDrivenMetric o WHERE o.idSolution = :idSolution", FMObjectiveFunction.class);
		query.setParameter("idSolution", idSolution);

		List<FMObjectiveFunction> resultList = query.getResultList();
		LOGGER.debug("Number of br.ufpr.dinf.gres.core.jmetal4.results: " + resultList.size());
		return resultList;
	}


}
