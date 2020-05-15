package br.ufpr.dinf.gres.architecture.helpers;


/**
 * Predicate class
 *
 * @param <T> type
 * @author edipofederle<edipofederle @ gmail.com>
 */
public interface Predicate<T> {
    boolean apply(T element);
}