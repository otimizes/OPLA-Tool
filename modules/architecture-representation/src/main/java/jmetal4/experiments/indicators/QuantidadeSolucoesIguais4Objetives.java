package jmetal4.experiments.indicators;

import jmetal4.qualityIndicator.util.MetricsUtil;
import jmetal4.util.JMException;

import java.io.FileNotFoundException;
import java.io.IOException;

public class QuantidadeSolucoesIguais4Objetives {

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public static void main(String[] args) throws FileNotFoundException, IOException, JMException, ClassNotFoundException {
        String[] abordagens = {
                "MECBA",
//            "MECBA-Clu",
        };
        String[] algoritmos = {
                "nsgaii",
                "paes",
                "spea2",
                "todos"
        };
        String[] softwares = {
                "OA_AJHotDraw",
                "OA_AJHsqldb",
        };

        for (String abordagem : abordagens) {
            for (String software : softwares) {
                for (String algorithm : algoritmos) {

                    System.out.println("\n\n--------------------------------");
                    System.out.println("Approach..: " + abordagem);
                    System.out.println("Software..: " + software);
                    System.out.println("MOEA......: " + algorithm);
                    System.out.println("");

                    int quantidadeSolucoes;
                    MetricsUtil mu = new MetricsUtil();

                    double[][] PFComb = mu.readFront("resultado/" + abordagem + "/" + software + "/All_FUN_" + algorithm + "-" + software + "_com");
                    double[][] PFInc = mu.readFront("resultado/" + abordagem + "/" + software + "/All_FUN_" + algorithm + "-" + software + "_inc");

                    quantidadeSolucoes = 0;

                    for (int i = 0; i < PFComb.length; i++) {
                        for (int j = 0; j < PFInc.length; j++) {
                            if ((PFComb[i][0] == PFInc[j][0]) && (PFComb[i][1] == PFInc[j][1])
                                    && (PFComb[i][2] == PFInc[j][2]) && (PFComb[i][3] == PFInc[j][3])) {
                                System.out.println("-->" + PFInc[j][0] + " " + PFInc[j][1] + " " + PFInc[j][2] + " " + PFInc[j][3]);
                                quantidadeSolucoes++;
                            }

                        }
                    }

                    //System.out.println("");
                    System.out.println("Total de Soluções em PFComb: " + PFComb.length);
                    System.out.println("Total de Soluções em PFInc: " + PFInc.length);
                    System.out.println("Total de Soluções de PFComb em PFInc: " + quantidadeSolucoes);

                }
            }
        }
    }
    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
}
