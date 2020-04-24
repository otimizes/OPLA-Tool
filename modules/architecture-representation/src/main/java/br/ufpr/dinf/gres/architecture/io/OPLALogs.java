package br.ufpr.dinf.gres.architecture.io;

import br.ufpr.dinf.gres.domain.OPLAThreadScope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OPLALogs {
    public static Map<Long, List<OptimizationInfo>> lastLogs = new HashMap<>();

    public static void add(OptimizationInfo info) {
        OPLALogs.lastLogs.computeIfAbsent(OPLAThreadScope.mainThreadId.get(), k -> new ArrayList<>()).add(info);
    }
}
