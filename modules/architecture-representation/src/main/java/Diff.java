import arquitetura.builders.ArchitectureBuilder;

public class Diff {

    public static void main(String args[]) {
        ArchitectureBuilder builder = new ArchitectureBuilder();

        try {
            builder.create("/Users/elf/Documents/workspaceModeling/exportacao/output/9316217788/VAR_All_agm-1386114713.uml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
