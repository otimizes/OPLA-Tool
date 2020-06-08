package br.otimizes.oplatool.architecture.builders;

public class Diff {

    public static void main(String[] args) {
        ArchitectureBuilderPapyrus builder = new ArchitectureBuilderPapyrus();

        try {
            builder.create("/Users/elf/Documents/workspaceModeling/exportacao/output/9316217788/VAR_All_agm-1386114713.uml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
