package jmetal4.experiments.indicators;

import jmetal4.util.JMException;

import java.io.*;

public class CreateRCommandCoverage {

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --

    public static void main(String[] args) throws FileNotFoundException, IOException, JMException, ClassNotFoundException {

        //String[] softwares = {"OA_AJHotDraw", "OA_AJHsqldb", "OO_BCEL", "OO_JHotDraw", "OO_MyBatis"}; //4objetives
        //String[] softwares = {"OA_AJHotDraw", "OA_AJHsqldb"}; //2objetives
        String[] softwares = {"OO_BCEL", "OO_JHotDraw", "OO_MyBatis"};

        for (String software : softwares) {
            //4 objetives
//            FileWriter os1 = new FileWriter("resultado_4objetives/metricas/coverage/r_command_"+software+"_NSGAII-PAES_PAES-NSGAII.txt", true);
//            FileWriter os2 = new FileWriter("resultado_4objetives/metricas/coverage/r_command_"+software+"_NSGAII-SPEA2_SPEA2-NSGAII.txt", true);
//            FileWriter os3 = new FileWriter("resultado_4objetives/metricas/coverage/r_command_"+software+"_PAES-SPEA2_SPEA2-PAES.txt", true);
            //2 objetives
            FileWriter os1 = new FileWriter("resultado_WTF/metricas/coverage/r_command_" + software + "_NSGAII-PAES_PAES-NSGAII.txt", true);
            FileWriter os2 = new FileWriter("resultado_WTF/metricas/coverage/r_command_" + software + "_NSGAII-SPEA2_SPEA2-NSGAII.txt", true);
            FileWriter os3 = new FileWriter("resultado_WTF/metricas/coverage/r_command_" + software + "_PAES-SPEA2_SPEA2-PAES.txt", true);

            String NSGAII_PAES = "";
            String PAES_NSGAII = "";
            String NSGAII_SPEA2 = "";
            String SPEA2_NSGAII = "";
            String PAES_SPEA2 = "";
            String SPEA2_PAES = "";

            for (int run = 0; run < 30; run++) {
                //4 objetives
                //BufferedReader buff = new BufferedReader(new FileReader("resultado_4objetives/metricas/coverage/"+software+"/"+run+"/result.tex"));
                //2 objetives
                BufferedReader buff = new BufferedReader(new FileReader("resultado_WTF/metricas/coverage/" + software + "/" + run + "/result.tex"));

                int contador = 0;
                while (buff.ready()) {
                    contador++;
                    String line = buff.readLine();
                    line = line.replace("\\", "");

                    if (contador == 5) {
                        String[] valores = line.split("&");
                        System.out.print(valores[0] + " - ");
                        System.out.print(valores[2] + " - ");
                        System.out.print(valores[3] + " - ");
                        System.out.println();

                        NSGAII_PAES = NSGAII_PAES + valores[2];
                        NSGAII_SPEA2 = NSGAII_SPEA2 + valores[3];
                        if (run != 29) {
                            NSGAII_PAES = NSGAII_PAES + ", ";
                            NSGAII_SPEA2 = NSGAII_SPEA2 + ", ";
                        }
                    }
                    if (contador == 7) {
                        String[] valores = line.split("&");
                        System.out.print(valores[0] + " - ");
                        System.out.print(valores[1] + " - ");
                        System.out.print(valores[3] + " - ");
                        System.out.println();

                        PAES_NSGAII = PAES_NSGAII + valores[1];
                        PAES_SPEA2 = PAES_SPEA2 + valores[3];
                        if (run != 29) {
                            PAES_NSGAII = PAES_NSGAII + ", ";
                            PAES_SPEA2 = PAES_SPEA2 + ", ";
                        }
                    }
                    if (contador == 9) {
                        String[] valores = line.split("&");
                        System.out.print(valores[0] + " - ");
                        System.out.print(valores[1] + " - ");
                        System.out.print(valores[2] + " - ");
                        System.out.println("\n\n");

                        SPEA2_NSGAII = SPEA2_NSGAII + valores[1];
                        SPEA2_PAES = SPEA2_PAES + valores[1];
                        if (run != 29) {
                            SPEA2_NSGAII = SPEA2_NSGAII + ", ";
                            SPEA2_PAES = SPEA2_PAES + ", ";
                        }
                    }
                }
            }

            os1.write("NSGAII_PAES<- c(" + NSGAII_PAES + ")" + "\n");
            os1.write("PAES_NSGAII<- c(" + PAES_NSGAII + ")" + "\n");
            os1.write("" + "\n");
            os1.write("wilcox.test(NSGAII_PAES,PAES_NSGAII)" + "\n");
            os1.write("boxplot(NSGAII_PAES,PAES_NSGAII, names=c(\"NSGAII - PAES\",\"PAES - NSGAII\"))" + "\n");
            os1.close();

            os2.write("NSGAII_SPEA2<- c(" + NSGAII_SPEA2 + ")" + "\n");
            os2.write("SPEA2_NSGAII<- c(" + SPEA2_NSGAII + ")" + "\n");
            os2.write("" + "\n");
            os2.write("wilcox.test(NSGAII_SPEA2,SPEA2_NSGAII)" + "\n");
            os2.write("boxplot(NSGAII_SPEA2,SPEA2_NSGAII, names=c(\"NSGAII - SPEA2\",\"SPEA2 - NSGAII\"))" + "\n");
            os2.close();

            os3.write("PAES_SPEA2<- c(" + PAES_SPEA2 + ")" + "\n");
            os3.write("SPEA2_PAES<- c(" + SPEA2_PAES + ")" + "\n");
            os3.write("" + "\n");
            os3.write("wilcox.test(PAES_SPEA2,SPEA2_PAES)" + "\n");
            os3.write("boxplot(PAES_SPEA2,SPEA2_PAES, names=c(\"PAES - SPEA2\",\"SPEA2 - PAES\"))" + "\n");
            os3.close();

        }

    }

    //  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --

}