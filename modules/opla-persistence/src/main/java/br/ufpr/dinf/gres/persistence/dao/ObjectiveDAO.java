package br.ufpr.dinf.gres.persistence.dao;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.ufpr.dinf.gres.opla.entity.Execution;
import br.ufpr.dinf.gres.opla.entity.Experiment;
import br.ufpr.dinf.gres.opla.entity.Objective;
import br.ufpr.dinf.gres.persistence.util.GenericDAOImpl;

/**
 * 
 * @author Fernando
 *
 */
public class ObjectiveDAO extends GenericDAOImpl<Objective> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(ObjectiveDAO.class);

	public ObjectiveDAO() {
		super(Objective.class);
	}

	public Long countNonDominatedSolution(Experiment experiment) {
		LOGGER.debug("Counting Non Dominated Solution for experiment:  " + experiment.getId());

		TypedQuery<Long> query = getEntityManager().createQuery(
				"SELECT count(o.id) FROM Objective o WHERE o.experiment = :experiment AND o.execution is empty",
				Long.class);
		query.setParameter("experiment", experiment);

		Long result = Long.valueOf(0);

		try {
			result = query.getSingleResult();
		} catch (NoResultException e) {
			LOGGER.warn(e);
		}

		LOGGER.debug("Number of Solution " + result);
		return result;

	}

	public Long countAllSoluctions(Experiment experiment, Execution execution) {
		LOGGER.debug("Counting All Solution for experiment:  " + experiment.getId());

		TypedQuery<Long> query = getEntityManager().createQuery(
				"SELECT count(o.id) FROM Objective o WHERE o.experiment = :experiment AND (o.execution = :execution OR o.execution is empty)",
				Long.class);
		query.setParameter("experiment", experiment);
		query.setParameter("execution", execution);

		Long result = Long.valueOf(0);

		try {
			result = query.getSingleResult();
		} catch (NoResultException e) {
			LOGGER.warn(e);
		}

		LOGGER.debug("Number of Solution " + result);
		return result;
	}

	public List<String> findNameSolution(Experiment experiment, Execution execution) {
		LOGGER.debug("Listing name of soluctions by experiment = " + experiment.getId() + " and execution = "
				+ execution.getId());

		TypedQuery<String> query = getEntityManager().createQuery(
				"SELECT o.solutionName FROM Objective o WHERE o.experiment = :experiment AND (o.execution = :execution OR o.execution is empty)",
				String.class);
		query.setParameter("experiment", experiment);
		query.setParameter("execution", execution);

		List<String> resultList = query.getResultList();

		LOGGER.debug("Number of Soluction encoutered" + resultList.size());
		return resultList;
	}

	public List<BigDecimal> findObjectiveValues(Experiment experiment, Execution execution, String solutionName) {
		LOGGER.debug("Listing Objetive Values by experiment = " + experiment.getId() + " , execution = "
				+ execution.getId() + " and solution name = " + solutionName);

		TypedQuery<String> query = getEntityManager().createQuery(
				"SELECT o.objectives FROM Objective o WHERE (o.experiment = :experiment OR o.execution = :execution) AND o.solutionName like :solutionName",
				String.class);
		query.setParameter("experiment", experiment);
		query.setParameter("execution", execution);
		query.setParameter("solutionName", "%" + solutionName.split("-")[1]);

		List<BigDecimal> result = Collections.emptyList();

		try {
			String retorno = query.getSingleResult();
			result = Arrays.asList(retorno.split("\\|")).stream().map(BigDecimal::new).collect(Collectors.toList());
		} catch (NoResultException e) {
			LOGGER.warn(e);
		}
		LOGGER.debug("Number of Solution " + result.size());
		return result;
	}

}
