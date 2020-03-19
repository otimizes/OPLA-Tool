package br.ufpr.dinf.gres.persistence.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.ELEGObjectiveFunction;
import br.ufpr.dinf.gres.persistence.util.GenericDAOImpl;
import br.ufpr.dinf.gres.persistence.util.GenericMetricDAO;

/**
 * 
 * @author Fernando
 *
 */
public class EleganceMetricDAO extends GenericDAOImpl<ELEGObjectiveFunction> implements GenericMetricDAO<ELEGObjectiveFunction> {

	private static final Logger LOGGER = Logger.getLogger(EleganceMetricDAO.class);

	private static final long serialVersionUID = 1L;

	public EleganceMetricDAO() {
		super(ELEGObjectiveFunction.class);
	}

	@Override
	public List<ELEGObjectiveFunction> findBySolution(String solution) {
		LOGGER.debug("Finding metric date for: " + solution);

		String idSolution = solution.split("-")[1];

		TypedQuery<ELEGObjectiveFunction> query = getEntityManager()
				.createQuery("SELECT o FROM EleganceMetric o WHERE o.idSolution = :idSolution", ELEGObjectiveFunction.class);
		query.setParameter("idSolution", idSolution);

		List<ELEGObjectiveFunction> resultList = query.getResultList();
		LOGGER.debug("Number of br.ufpr.dinf.gres.core.jmetal4.results: " + resultList.size());
		return resultList;
	}

}
