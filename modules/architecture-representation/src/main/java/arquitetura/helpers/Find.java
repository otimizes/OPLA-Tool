package arquitetura.helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class Find {

    public static <T> List<T> filter(List<T> col, Predicate<T> predicate) {
        List<T> result = new ArrayList<T>();
        for (T element : col) {
            if (predicate.apply(element)) {
                result.add(element);
            }
        }
        return result;
    }

}
