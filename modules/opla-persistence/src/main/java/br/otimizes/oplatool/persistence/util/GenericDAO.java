package br.otimizes.oplatool.persistence.util;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

/**
 * 
 * @author Fernando
 *
 * @param <T>
 */
public interface GenericDAO<T> extends Serializable {

	T findById(Long id);

	List<T> findAll();

	void save(T clazz);

	void udpate(T clazz);

	void excluir(T clazz);

	void excluir(Long id);

	EntityManager getEntityManager();

}
