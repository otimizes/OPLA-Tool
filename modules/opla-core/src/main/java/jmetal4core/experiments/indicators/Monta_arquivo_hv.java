package jmetal4.experiments.indicators;

import jmetal4.util.JMException;

import java.io.*;

public class Monta_arquivo_hv {

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public static void main(String[] args) throws FileNotFoundException, IOException, JMException, ClassNotFoundException {
        String[] algoritmos = {
                "nsgaii",
                "paes",
                "spea2"
        };
        String[] softwares = {
                "OA_AJHotDraw",
                "OA_AJHsqldb",
                "OA_HealthWatcher",
                "OA_TollSystems",
//            "OO_BCEL",
//            "OO_JBoss",
//            "OO_JHotDraw",
//            "OO_MyBatis"
        };

        String[] contextos = {
                "_Comb_4obj",
                "_Inc_4obj"
        };
        for (String software : softwares) {

            File directory = new File("resultado/hypervolume/" + software);
            if (!directory.exists()) {
                if (!directory.mkdir()) {
                    System.exit(0);
                }
            }
            String allalgorithms_allruns = "resultado/hypervolume/" + software + "_allalgorithms_allruns.txt";

            for (String algorithm : algoritmos) {
                for (String contexto : contextos) {
                    String outFile = "resultado/hypervolume/" + software + "/" + software + "_" + algorithm + contexto + "_allruns.txt";
                    for (int run = 0; run < 30; run++) {
                        String srcFile = "resultado/" + algorithm + "/" + software + contexto + "/FUN_" + algorithm + "-" + software + "-" + run + ".NaoDominadas";
                        mergeFiles(srcFile, outFile, "\n");
                        mergeFiles(srcFile, allalgorithms_allruns, "");
                    }

                }
            }
        }
    }
    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --

    /**
     * Takes all files in a specified directory and merge them together...
     */
    public static void mergeAllFiles(String fileDir) throws FileNotFoundException, IOException {
        File dirSrc = new File(fileDir);
        File[] list = dirSrc.listFiles();
        for (int j = 0; j < list.length; j++) {
            String lines;
            String srcFile = list[j].getPath();
            String outFile = "JavaMerged.txt";
            BufferedReader inFile = new BufferedReader(new FileReader(new File(srcFile)));
            BufferedWriter outPut = new BufferedWriter(new FileWriter(outFile, true));
            while ((lines = inFile.readLine()) != null) {
                outPut.write(lines);
                outPut.newLine();
            }
            outPut.flush();
            outPut.close();
            inFile.close();
        }
    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --

    /**
     * Takes a files and merge it together outFile...
     */
    public static void mergeFiles(String srcFile, String outFile, String separator) throws FileNotFoundException, IOException {
        String lines;
        BufferedReader inFile = new BufferedReader(new FileReader(new File(srcFile)));
        BufferedWriter outPut = new BufferedWriter(new FileWriter(outFile, true));
        while ((lines = inFile.readLine()) != null) {
            outPut.write(lines);
            outPut.newLine();
        }
        outPut.write(separator);
        outPut.flush();
        outPut.close();
        inFile.close();
    }

}
