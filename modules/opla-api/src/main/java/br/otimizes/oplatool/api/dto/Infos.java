package br.otimizes.oplatool.api.dto;

import br.otimizes.oplatool.architecture.io.OptimizationInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Infos {
    private List<Map.Entry<String, List<OptimizationInfo>>> infos;

    Infos() {
        this.infos = new ArrayList<>();
    }

    public Infos(List<Map.Entry<String, List<OptimizationInfo>>> infos) {
        this.infos = infos;
    }

    public List<Map.Entry<String, List<OptimizationInfo>>> getInfos() {
        return infos;
    }

    public void setInfos(List<Map.Entry<String, List<OptimizationInfo>>> infos) {
        this.infos = infos;
    }
}
