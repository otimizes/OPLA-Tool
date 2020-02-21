package br.ufpr.dinf.gres.oplaapi;

public class OptimizationInfo {
    public Long threadId;
    public String logs;
    public Long status;

    public OptimizationInfo(Long threadId) {
        this.threadId = threadId;
    }

    public OptimizationInfo(Long threadId, String logs) {
        this.threadId = threadId;
        this.logs = logs;
    }
}
