package br.otimizes.oplatool.architecture.helpers;

import br.otimizes.oplatool.architecture.representation.Variability;
import br.otimizes.oplatool.architecture.representation.Variant;
import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

public class Strings {

    public static String splitVariants(List<Variant> list) {
        List<String> names = new ArrayList<>();
        for (Variant variant : list)
            names.add(variant.getName());
        return Joiner.on(", ").skipNulls().join(names);
    }

    public static String splitVariabilities(List<Variability> list) {
        List<String> names = new ArrayList<>();
        for (Variability variability : list)
            names.add(variability.getName());
        return Joiner.on(", ").skipNulls().join(names);
    }
}
