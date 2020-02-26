package br.ufpr.dinf.gres.architecture.helpers;


/**
 * @param <T>
 * @author edipofederle<edipofederle@gmail.com>
 */
public interface Predicate<T> {
    boolean apply(T element);
}