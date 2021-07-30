package br.otimizes.oplatool.architecture.io;

import br.otimizes.oplatool.domain.OPLAThreadScope;
import br.otimizes.oplatool.domain.config.FileConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OPLALogs {
    public static Map<String, List<OptimizationInfo>> lastLogs = new HashMap<>();

    public static void add(OptimizationInfo info) {
        OPLALogs.lastLogs.computeIfAbsent(OPLAThreadScope.hash.get(), k -> new ArrayList<>()).add(info);
    }

    public static List<OptimizationInfo> get(String token, String hash) {
        return get(token + FileConstants.FILE_SEPARATOR + hash);
    }

    private static List<OptimizationInfo> get(String s) {
        Map.Entry<String, List<OptimizationInfo>> stringListEntry = OPLALogs.lastLogs.entrySet().stream()
                .filter(log -> log.getKey().startsWith(s)).findFirst().orElse(null);
        return stringListEntry != null ? stringListEntry.getValue() : new ArrayList<>();
    }

    public static List<OptimizationInfo> remove(String token, String hash) {
        return remove(token + FileConstants.FILE_SEPARATOR + hash);
    }

    public static List<OptimizationInfo> remove(String s) {
        List<OptimizationInfo> optimizationInfos = OPLALogs.get(s);
        if (optimizationInfos.size() <= 1) return optimizationInfos;
        return OPLALogs.lastLogs.remove(s);
    }

    public static OptimizationInfo getFirst(String token, String hash) {
        List<OptimizationInfo> optimizationInfos = OPLALogs.get(token, hash);
        return optimizationInfos.size() > 1 ? optimizationInfos.remove(0) : optimizationInfos.get(0);
    }
}
