package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class RScript {

    private String script;
    private String result;

    public RScript() {
    }

    public RScript(String script) throws IOException, InterruptedException {
        this.script = script;
        exec();
    }

    public String exec() throws IOException, InterruptedException {
        File tempFile = File.createTempFile("opla-", "-tool");
        FileWriter writer = new FileWriter(tempFile.toString());
        writer.write(script);
        writer.close();

        Process exec = Runtime.getRuntime().exec("Rscript " + tempFile.toString());
        exec.waitFor();
        Scanner scanner = new Scanner(exec.getInputStream());
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine() + "\n");
        }
        result = stringBuilder.toString();
        return result;
    }

    public String shapiroWilkTest(List<BigDecimal> values) throws IOException, InterruptedException {
        return new RScript("shapiro.test(c(" + getReplace(values) + "))").getResult();
    }

    public String wilcox(List<BigDecimal> values, List<BigDecimal> compare) throws IOException, InterruptedException {
        return new RScript("wilcox.test(c(" + getReplace(values) + "), c(" + getReplace(compare) + "))").getResult();
    }

    private String getReplace(List<?> values) {
        return values.toString().replace("[", "").replace("]", "");
    }

    public String boxplot(List<List<BigDecimal>> values, String dirPng) throws IOException, InterruptedException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("png(file = \""+ dirPng+"boxplot.png\")\n");
        stringBuilder.append("boxplot(");
        values.forEach(v -> stringBuilder.append("c(" + getReplace(v) + "),"));
        stringBuilder.append("names=");
        stringBuilder.append("c(" + getReplace(values.stream().map(v -> v.hashCode()).collect(Collectors.toList())) + ")");
        stringBuilder.append(")\n");
        stringBuilder.append("dev.off");
        System.out.println(stringBuilder.toString());
        new RScript(stringBuilder.toString());
        return dirPng + "boxplot.png";
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
