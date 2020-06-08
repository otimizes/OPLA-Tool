package br.otimizes.oplatool.architecture.builders;

import br.otimizes.oplatool.architecture.generate.GenerateArchitecture;
import br.otimizes.oplatool.architecture.representation.Architecture;


public class TimeReadWritePLAs {

    public static void main(String[] args) throws Exception {

        String pla = "/Users/elf/Desktop/serverUFPR/experiments/PLAS/agm/agm.uml";

        ArchitectureBuilderPapyrus b = new ArchitectureBuilderPapyrus();
        GenerateArchitecture g = new GenerateArchitecture();

        long startTimea = System.nanoTime();

        Architecture arch = b.create(pla);
        long endTimea = System.nanoTime();
        double durationa = (double) (endTimea - startTimea) / (Math.pow(10, 9));
        System.out.println("Read time (s) = " + durationa);

        long startTimeb = System.nanoTime();

        g.generate(arch, "/Users/elf/Desktop/");

        long endTimeb = System.nanoTime();
        double durationb = (double) (endTimeb - startTimeb) / (Math.pow(10, 9));
        System.out.println("Write time (s) = " + durationb);


    }

}
