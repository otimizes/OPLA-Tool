package learning;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instances;

import java.util.Arrays;

/**
 * Attribute-Relation File Format Object used in Machine Learnings presents in Weka
 */
public class ArffExecution {

    private FastVector atts;
    private FastVector attVals;
    private Instances data;
    private double[] vals;
    private int attrIndices;
    private double[][] objectives;

    /**
     * To use it, instantiate the class by passing a list of function values Objective
     * @param objectives Function Values Objective
     */
    public ArffExecution(double[][] objectives) {
        newInstance(objectives, null);
    }

    /**
     * To use it, instantiate the class by passing a list of function values Objective and descriptions for the same
     * @param objectives Function Values Objective
     * @param descOjectives Objectives Description
     */
    public ArffExecution(double[][] objectives, String... descOjectives) {
        newInstance(objectives, descOjectives);
    }

    private void newInstance(double[][] objectives, String[] descOjectives) {
        attrIndices = objectives[0].length;
        this.objectives = objectives;
        atts = new FastVector();
        attVals = new FastVector();
        // - numeric
        if (descOjectives != null) {
            for (String descOjective : descOjectives) {
                atts.add(new Attribute(descOjective));
            }
        } else {
            for (int j = 0; j < objectives[0].length; j++) {
                atts.addElement(new Attribute("obj" + (j + 1)));
            }
        }
        // - string
        atts.addElement(new Attribute("execution", (FastVector) null));
        data = new Instances("MyRelation", atts, 0);

        for (int i = 0; i < objectives.length; i++) {
            vals = new double[data.numAttributes()];
            for (int j = 0; j < objectives[0].length; j++) {
                vals[j] = objectives[i][j];
            }
            vals[objectives[0].length] = data.attribute(objectives[0].length).addStringValue(String.valueOf(i));
            data.add(new DenseInstance(1.0, vals));
        }
        System.out.println("foi");
    }

    public FastVector getAtts() {
        return atts;
    }

    public void setAtts(FastVector atts) {
        this.atts = atts;
    }

    public FastVector getAttVals() {
        return attVals;
    }

    public void setAttVals(FastVector attVals) {
        this.attVals = attVals;
    }

    public Instances getData() {
        return data;
    }

    public void setData(Instances data) {
        this.data = data;
    }

    public double[] getVals() {
        return vals;
    }

    public void setVals(double[] vals) {
        this.vals = vals;
    }


    @Override
    public String toString() {
        return "ArffExecution{" +
                "atts=" + atts +
                ", attVals=" + attVals +
                ", data=" + data +
                ", vals=" + Arrays.toString(vals) +
                '}';
    }

    public int getAttrIndices() {
        return attrIndices;
    }

    public void setAttrIndices(int attrIndices) {
        this.attrIndices = attrIndices;
    }

    public double[][] getObjectives() {
        return objectives;
    }

    public void setObjectives(double[][] objectives) {
        this.objectives = objectives;
    }

    /**
     * Used to get Instances withod last column that indentify the class of object
     * @return Instances without class
     */
    public Instances getDataWithoutClass() {
        Instances newIn = new Instances(this.getData());
        newIn.deleteAttributeAt(attrIndices);
        return newIn;
    }
}
