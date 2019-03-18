package utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class RScriptOptionElement {
    public List<List<BigDecimal>> values;
    public String dirPng;

    public RScriptOptionElement() {
    }

    public RScriptOptionElement(List<List<BigDecimal>> values) {
        this.values = values;
    }
    public RScriptOptionElement(List<List<Double>> values, String dirPng) {
        this.values = values.stream().map(v->v.stream().map(vv -> BigDecimal.valueOf(vv)).collect(Collectors.toList())).collect(Collectors.toList());
        this.dirPng = dirPng;
    }
}
