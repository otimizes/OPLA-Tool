package br.ufpr.dinf.gres.oplaapi.dto;

import arquitetura.io.OPLAThreadScope;

public class OptimizationInfo {
    public Long threadId;
    public String logs;
    public OptimizationInfoStatus status;
    public String hash = OPLAThreadScope.hash.get();

    public OptimizationInfo() {
    }

    public OptimizationInfo(Long threadId) {
        this.threadId = threadId;
    }

    public OptimizationInfo(Long threadId, String logs) {
        this.threadId = threadId;
        this.logs = logs;
    }

    public OptimizationInfo(Long threadId, String logs, OptimizationInfoStatus status) {
        this.threadId = threadId;
        this.logs = logs;
        this.status = status;
    }
}
