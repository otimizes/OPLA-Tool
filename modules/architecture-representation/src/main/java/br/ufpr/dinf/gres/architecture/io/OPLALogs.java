package br.ufpr.dinf.gres.architecture.io;

import br.ufpr.dinf.gres.domain.OPLAThreadScope;
import br.ufpr.dinf.gres.domain.config.FileConstants;

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
        return OPLALogs.lastLogs.get(token + FileConstants.FILE_SEPARATOR + hash);
    }

    public static List<OptimizationInfo> remove(String token, String hash) {
        return OPLALogs.lastLogs.remove(token + FileConstants.FILE_SEPARATOR + hash);
    }
}
