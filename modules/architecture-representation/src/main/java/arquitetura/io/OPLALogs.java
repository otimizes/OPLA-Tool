package arquitetura.io;

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
