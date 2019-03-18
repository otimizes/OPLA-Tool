package br.ufpr.dinf.gres.persistence.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author Fernando
 */
public class PersistenceManager {

	private static EntityManagerFactory entityManagerFactory;
	private static EntityManager entityManager;

	static {
		entityManagerFactory = Persistence.createEntityManagerFactory("oplaPU");
	}

	public static EntityManager getEntityManager() {
		if (entityManager == null) {
			entityManager = entityManagerFactory.createEntityManager();
		}
		return entityManager;
	}

}
