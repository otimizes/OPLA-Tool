package jmetal4.experiments;

public class Fitness {

    private double value;

    public Fitness(double d) {
        this.value = d;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
