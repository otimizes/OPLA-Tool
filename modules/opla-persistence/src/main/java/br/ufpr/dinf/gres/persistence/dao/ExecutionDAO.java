package br.ufpr.dinf.gres.persistence.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import br.ufpr.dinf.gres.opla.entity.Execution;
import br.ufpr.dinf.gres.opla.entity.Experiment;
import br.ufpr.dinf.gres.persistence.util.GenericDAOImpl;

public class ExecutionDAO extends GenericDAOImpl<Execution> {

	private static final long serialVersionUID = 1L;

	public ExecutionDAO() {
		super(Execution.class);
	}

	public List<Execution> findByExperiment(Experiment experiment) {

		TypedQuery<Execution> query = getEntityManager()
				.createQuery("SELECT o FROM Execution o where o.experiment = :experiment", Execution.class);
		query.setParameter("experiment", experiment);

		return query.getResultList();
	}

}
