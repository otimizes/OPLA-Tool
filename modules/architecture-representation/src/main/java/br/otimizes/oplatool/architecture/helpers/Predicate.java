package br.otimizes.oplatool.architecture.helpers;


/**
 * Predicate class
 *
 * @param <T> type
 * @author edipofederle<edipofederle @ gmail.com>
 */
public interface Predicate<T> {
    boolean apply(T element);
}