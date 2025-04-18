package br.otimizes.oplatool.architecture.representation.relationship;

import java.io.Serializable;

/**
 * Multiplicity class
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class Multiplicity implements Serializable {

    private static final long serialVersionUID = 2679645297119128578L;
    private String lowerValue;
    private String upperValue;

    public Multiplicity(String lowerValue, String upperValue) {
        this.lowerValue = lowerValue;
        this.upperValue = upperValue;
    }

    public String getLowerValue() {
        return lowerValue == null ? "1" : lowerValue;
    }

    public void setLowerValue(String lowerValue) {
        this.lowerValue = lowerValue;
    }

    public String getUpperValue() {
        return upperValue == null ? "1" : upperValue;
    }

    public void setUpperValue(String upperValue) {
        this.upperValue = upperValue;
    }

    @Override
    public String toString() {
        if (bothEqualsOne()) {
            return "1";
        } else if (isCompleteMultiplicity()) {
            return this.lowerValue + ".." + this.upperValue;
        }
        return "";
    }

    private boolean bothEqualsOne() {
        return ("1".equals(this.upperValue)) && ("1".equals(this.lowerValue));
    }

    private boolean isCompleteMultiplicity() {
        return (this.lowerValue != null) && ((!"".equalsIgnoreCase(this.lowerValue)) || ((this.upperValue != null))) && (!"".equalsIgnoreCase(this.upperValue));
    }
}