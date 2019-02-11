package br.ufpr.dinf.gres.persistence.dao;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import br.ufpr.dinf.gres.opla.entity.Experiment;
import br.ufpr.dinf.gres.opla.entity.MapObjectiveName;
import br.ufpr.dinf.gres.persistence.util.GenericDAOImpl;

public class MapObjectivesDAO extends GenericDAOImpl<MapObjectiveName> {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = Logger.getLogger(MapObjectivesDAO.class);

	public MapObjectivesDAO() {
		super(MapObjectiveName.class);
	}

	public List<String> findNamesByExperiment(Experiment experiment) {

		LOGGER.debug("Finding Solutions by experiment:  " + experiment.getId());
		
		TypedQuery<String> query = getEntityManager()
				.createQuery("SELECT o.names FROM MapObjectiveName o WHERE o.experiment = :experiment", String.class);
		query.setParameter("experiment", experiment);

		List<String> result = Collections.emptyList();

		try {
			String names = query.getSingleResult();
			result = Arrays.asList(names.split(StringUtils.SPACE)).stream()
									.map(StringUtils::capitalize)
									.collect(Collectors.toList());
		} catch (NoResultException e) {
			LOGGER.warn(e);
		}

		LOGGER.debug("Number of Solution " + result);
		return result;
	}

}
