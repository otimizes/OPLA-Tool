package utils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public enum RScriptOption {
    SHAPIRO_WILK("Shapiro Wilk"), WILCOX("Wilcox"), BOXPLOT("BoxPlot");

    private String description;

    RScriptOption(String description) {
        this.description = description;
    }

    public String getResult(RScriptOptionElement element) throws IOException, InterruptedException {
        switch (this) {
            case SHAPIRO_WILK:
                return element.values.stream().map(elm -> {
                    try {
                        return new RScript().shapiroWilkTest(elm);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.joining());
            case WILCOX:
                return new RScript().wilcox(element.values.get(0), element.values.get(1));
            case BOXPLOT:
                return new RScript().boxplot(element.values, element.dirPng);
        }
        return null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
