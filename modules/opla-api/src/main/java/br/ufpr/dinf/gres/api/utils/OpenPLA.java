package br.ufpr.dinf.gres.api.utils;

import java.io.IOException;

public class OpenPLA {

    public static Process executeCommand(String command) throws IOException {
        return Runtime.getRuntime().exec(command);
    }

    public static String execCmd(String cmd) throws java.io.IOException {
        Process proc = Runtime.getRuntime().exec(cmd);
        java.io.InputStream is = proc.getInputStream();
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        String val = "";
        if (s.hasNext()) {
            val = s.next();
        } else {
            val = "";
        }
        return val;
    }

    public static Process executePapyrus(String location, String plas) {
        try {
            return OpenPLA.executeCommand(location + " -nosplash -Xverify:none -XX:+AggressiveOpts -XX:PermSize=512m-XX:MaxPermSize=512m -Xms2048m-Xmx2048m -Xmn512m -Xss2m -XX:+UseParallelOldGC -XX:MaxGCPauseMillis=10-XX:+UseG1GC-XX:CompileThreshold=5-XX:MaxGCPauseMillis=10-XX:MaxHeapFreeRatio=70-XX:+CMSIncrementalPacing-XX:+UseFastAccessorMethods-server " +
                    "-vm /home/wmfsystem/App/jdk/bin -clean -clearPersistedState -refresh --launcher.openFile " + plas);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Process executeJar(String location, String plas) {
        try {
            return OpenPLA.executeCommand("java -jar " + location + " " + plas);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
