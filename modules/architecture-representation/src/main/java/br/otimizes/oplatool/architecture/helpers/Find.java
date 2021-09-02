package br.otimizes.oplatool.architecture.helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * Find predicates in element
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class Find {

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
