package br.ufpr.dinf.gres.persistence.util;

import java.util.List;

/**
 * Generic Data Access Object for Metric Entities
 * 
 * @author Fernando
 *
 */
public interface GenericMetricDAO<T> extends GenericDAO<T> {

	public List<T> findBySolution(String solution);

}
