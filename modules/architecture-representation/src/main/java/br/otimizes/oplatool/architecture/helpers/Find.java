package br.otimizes.oplatool.architecture.helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * Find predicates in element
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class Find {

    /**
     * Verify if the list contains the predicate
     *
     * @param list      list
     * @param predicate predicate
     * @param <T>       type of element
     * @return list
     */
    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        List<T> result = new ArrayList<T>();
        for (T element : list) {
            if (predicate.apply(element)) {
                result.add(element);
            }
        }
        return result;
    }
}
