package arquitetura.helpers;

import arquitetura.representation.Variability;
import arquitetura.representation.Variant;
import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

public class Strings {

    public static String spliterVariants(List<Variant> list) {
        List<String> names = new ArrayList<String>();

        for (Variant variant : list)
            names.add(variant.getName());

        return Joiner.on(", ").skipNulls().join(names);
    }

    public static String spliterVariabilities(List<Variability> list) {
        List<String> names = new ArrayList<String>();

        for (Variability variability : list)
            names.add(variability.getName());

        return Joiner.on(", ").skipNulls().join(names);
    }

}
