package br.otimizes.oplatool.persistence.util;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

/**
 * Generic implementation for DAO Pattern
 *
 * @param <T>
 * @author Fernando
 */
public abstract class GenericDAOImpl<T extends Serializable> implements GenericDAO<T> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(GenericDAOImpl.class);

	@PersistenceContext
	private EntityManager em;

	private final Class<T> clazz;

	public GenericDAOImpl(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public T findById(Long id) {
		if (em == null) em = PersistenceManager.getEntityManager();
		LOGGER.debug("Finding by id: " + id);
		return em.find(clazz, id);
	}

	@Override
	public List<T> findAll() {
		if (em == null) em = PersistenceManager.getEntityManager();
		LOGGER.debug("List all" + clazz.getSimpleName());
		TypedQuery<T> query = em.createQuery(" FROM " + clazz.getSimpleName(), clazz);
		List<T> resultList = query.getResultList();
		LOGGER.debug("Listing " + resultList.size() + " br.otimizes.oplatool.core.jmetal4.results");
		return resultList;
	}

	@Override
	public void save(T clazz) {
		if (em == null) em = PersistenceManager.getEntityManager();
		LOGGER.debug("Saving: " + clazz.getClass().getSimpleName());
		em.getTransaction().begin();
		em.persist(clazz);
		em.getTransaction().commit();
		LOGGER.debug("Saved Success");
	}

	@Override
	public void udpate(T clazz) {
		LOGGER.debug("Updating: " + clazz.getClass().getSimpleName());
		em.getTransaction().begin();
		em.merge(clazz);
		em.getTransaction().commit();
		LOGGER.debug("Updated Success");
	}

	@Override
	public void excluir(T clazz) {
		if (em == null) em = PersistenceManager.getEntityManager();
		LOGGER.debug("Deleting: " + clazz.getClass().getSimpleName());
		em.getTransaction().begin();
		em.remove(clazz);
		em.getTransaction().commit();
		LOGGER.debug("Deleted Success");
	}

	@Override
	public void excluir(Long id) {
		excluir(findById(id));
	}

	@Override
	public EntityManager getEntityManager() {
		if (em == null) em = PersistenceManager.getEntityManager();
		return em;
	}
	
	public Class<T> getClazz() {
		return clazz;
	}
}
