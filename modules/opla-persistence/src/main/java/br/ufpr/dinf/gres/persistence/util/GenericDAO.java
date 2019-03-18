package br.ufpr.dinf.gres.persistence.util;

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

	public T findById(Long id);

	public List<T> findAll();

	public void save(T clazz);

	public void udpate(T clazz);

	public void excluir(T clazz);

	public void excluir(Long id);

	public EntityManager getEntityManager();

}
