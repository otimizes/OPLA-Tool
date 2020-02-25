package jmetal4.experiments.indicators;

import jmetal4.util.JMException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CalculaMediaSolucoesRuns {

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
    public static void main(String[] args) throws FileNotFoundException, IOException, JMException, ClassNotFoundException {

        String[] abordagens = {
//            "MECBA",
                "MECBA-Clu"
        };
        String[] softwares = {
                "OA_AJHotDraw",
                "OA_AJHsqldb",
                "OA_TollSystems",
                "OA_HealthWatcher",
                "OO_BCEL",
                "OO_JBoss",
                "OO_JHotDraw",
                "OO_MyBatis"
        };
        String[] algorithms = {
                "nsgaii",
                "paes",
                "spea2"
        };
//        String contexto = "_Comb_2obj";
        String contexto = "_Comb_4obj";

        System.out.println(contexto);

        for (String abordagem : abordagens) {
            System.out.println("\n\n" + abordagem);
            for (String software : softwares) {
                System.out.println("\n\n" + software);
                for (String algorithm : algorithms) {
                    float total = 0;
                    for (int run = 0; run < 30; run++) {
                        BufferedReader buff = new BufferedReader(new FileReader("resultado/" + abordagem + "/" + algorithm + "/" + software + contexto + "/FUN_" + algorithm + "-" + software + "-" + run + ".NaoDominadas"));

                        int contador = 0;
                        while (buff.ready()) {
                            contador++;
                            String line = buff.readLine();
                        }
                        total = total + contador;
                        System.out.println(" " + algorithm + " / " + run + ": " + contador);
                    }
                    float media = total / 30;
                    System.out.println(" " + software + " / " + algorithm + "  --> Media: " + media + " - Total: " + total + "\n");
                }
            }
        }
    }
    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
}
