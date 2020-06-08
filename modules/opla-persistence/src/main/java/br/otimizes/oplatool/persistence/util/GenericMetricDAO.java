package br.otimizes.oplatool.persistence.util;

import java.util.List;

/**
 * Generic Data Access Object for Metric Entities
 * 
 * @author Fernando
 *
 */
public interface GenericMetricDAO<T> extends GenericDAO<T> {

	List<T> findBySolution(String solution);

}
