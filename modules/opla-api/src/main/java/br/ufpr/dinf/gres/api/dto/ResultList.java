package br.ufpr.dinf.gres.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class ResultList<T> {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<T> values;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long count;

    public ResultList(Long count) {
        this.count = count;
    }

    public ResultList(List<T> values) {
        this.values = values;
    }

    public ResultList() {
    }

    public List<T> getValues() {
        return values;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
