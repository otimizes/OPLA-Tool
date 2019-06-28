package utils;

public class QtdElements {
    public int classes;
    public int concerns;
    public int interfaces;
    public int packages;
    public int variabilities;
    public int variants;
    public int variationPoints;
    public int abstractions;
    public int agregations;
    public int associations;
    public int compositions;
    public int dependencies;
    public int generalizations;
    public int realizations;
    public int usage;

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

    public QtdElements(String[] elements) {
        for (int i = 0; i < elements.length; i++) {
            try {
                if (i == 0) this.classes = Integer.parseInt(elements[i]);
                if (i == 1) this.concerns = Integer.parseInt(elements[i]);
                if (i == 2) this.interfaces = Integer.parseInt(elements[i]);
                if (i == 3) this.packages = Integer.parseInt(elements[i]);

                if (i == 4) this.variationPoints = Integer.parseInt(elements[i]);
                if (i == 5) this.variants= Integer.parseInt(elements[i]);
                if (i == 6) this.variabilities = Integer.parseInt(elements[i]);

                if (i == 7) this.abstractions = Integer.parseInt(elements[i]);
                if (i == 8) this.agregations = Integer.parseInt(elements[i]);
                if (i == 9) this.associations = Integer.parseInt(elements[i]);
                if (i == 10) this.compositions = Integer.parseInt(elements[i]);
                if (i == 11) this.dependencies = Integer.parseInt(elements[i]);
                if (i == 12) this.generalizations = Integer.parseInt(elements[i]);
                if (i == 13) this.realizations = Integer.parseInt(elements[i]);
                if (i == 14) this.usage = Integer.parseInt(elements[i]);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }
}
