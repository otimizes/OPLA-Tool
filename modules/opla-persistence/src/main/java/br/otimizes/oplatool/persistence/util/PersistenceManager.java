package br.otimizes.oplatool.persistence.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author Fernando
 */
public class PersistenceManager {

    public static EntityManagerFactory entityManagerFactory;
	public static EntityManager entityManager;

    public static EntityManager getEntityManager() {
        if (entityManager == null) {
            entityManager = entityManagerFactory.createEntityManager();
        }
        return entityManager;
    }

}
