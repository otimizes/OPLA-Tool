package br.ufpr.dinf.gres.architecture.representation;

/**
 * Architecture holder
 */
public class ArchitectureHolder {
    private static String name;

    public static String getName() {
        return name;
    }

    public static void setName(String n) {
        name = n;
    }

}
