package utils;

public class QtdElements {
    public int classes;
    public int concerns;
    public int interfaces;
    public int packages;
    public int variabilities;
    public int variants;
    public int variationPoints;

    public QtdElements() {
    }

    public QtdElements(int classes, int concerns, int interfaces, int packages, int variabilities, int variants, int variationPoints) {
        this.classes = classes;
        this.concerns = concerns;
        this.interfaces = interfaces;
        this.packages = packages;
        this.variabilities = variabilities;
        this.variants = variants;
        this.variationPoints = variationPoints;
    }

    public QtdElements(String classes, String concerns, String interfaces, String packages, String variabilities, String variants, String variationPoints) {
        this.classes = Integer.parseInt(classes);
        this.concerns = Integer.parseInt(concerns);
        this.interfaces = Integer.parseInt(interfaces);
        this.packages = Integer.parseInt(packages);
        this.variabilities = Integer.parseInt(variabilities);
        this.variants = Integer.parseInt(variants);
        this.variationPoints = Integer.parseInt(variationPoints);
    }
}
