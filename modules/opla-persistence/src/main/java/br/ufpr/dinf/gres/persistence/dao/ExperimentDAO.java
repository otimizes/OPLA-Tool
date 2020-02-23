package br.ufpr.dinf.gres.persistence.dao;

import br.ufpr.dinf.gres.opla.entity.Experiment;
import br.ufpr.dinf.gres.persistence.util.GenericDAOImpl;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Fernando
 */
public class ExperimentDAO extends GenericDAOImpl<Experiment> {

    private static final long serialVersionUID = 1L;

    public ExperimentDAO() {
        super(Experiment.class);
    }

    public List<Experiment> findAllOrdened() {
        TypedQuery<Experiment> query = getEntityManager().createQuery("SELECT o FROM Experiment o ORDER BY o.id asc", Experiment.class);
        List<Experiment> resultList = query.getResultList();
        return resultList;
    }

}
