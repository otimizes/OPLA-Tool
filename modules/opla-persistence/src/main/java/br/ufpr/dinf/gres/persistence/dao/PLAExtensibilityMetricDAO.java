package br.ufpr.dinf.gres.persistence.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.EXTObjectiveFunction;
import br.ufpr.dinf.gres.persistence.util.GenericDAOImpl;
import br.ufpr.dinf.gres.persistence.util.GenericMetricDAO;

/**
 * 
 * @author Fernando
 *
 */
public class PLAExtensibilityMetricDAO extends GenericDAOImpl<EXTObjectiveFunction> implements GenericMetricDAO<EXTObjectiveFunction> {
	
	private static final Logger LOGGER = Logger.getLogger(PLAExtensibilityMetricDAO.class);

	private static final long serialVersionUID = 1L;

	public PLAExtensibilityMetricDAO() {
		super(EXTObjectiveFunction.class);
	}

	@Override
	public List<EXTObjectiveFunction> findBySolution(String solution) {
		LOGGER.debug("Finding metric date for: " + solution);

		String idSolution = solution.split("-")[1];

		TypedQuery<EXTObjectiveFunction> query = getEntityManager().createQuery(
				"SELECT o FROM PLAExtensibilityMetric o WHERE o.idSolution = :idSolution", EXTObjectiveFunction.class);
		query.setParameter("idSolution", idSolution);

		List<EXTObjectiveFunction> resultList = query.getResultList();
		LOGGER.debug("Number of br.ufpr.dinf.gres.core.jmetal4.results: " + resultList.size());
		return resultList;
	}

	

}
