package br.ufpr.dinf.gres.architecture.io;

import br.ufpr.dinf.gres.domain.OPLAThreadScope;

public class OptimizationInfo {
    public Long threadId;
    public String logs;
    public OptimizationInfoStatus status;
    public String hash = OPLAThreadScope.hash.get();
    public Integer currentGeneration = OPLAThreadScope.currentGeneration.get();

    public OptimizationInfo() {
    }

    public OptimizationInfo(Long threadId) {
        this.threadId = threadId;
    }

    public OptimizationInfo(Long threadId, String logs, OptimizationInfoStatus status) {
        this.threadId = threadId;
        this.logs = logs;
        this.status = status;
    }
}
