package br.otimizes.oplatool.core.learning;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Attribute-Relation File Format Object used in Machine Learnings presents in Weka
 */
public class ArffExecution {

    private ArrayList<Attribute> listOfAttributes = new ArrayList<>();
    private ArrayList<Attribute> listOfValues = new ArrayList<>();
    private Instances data;
    private double[] values;
    private int attrIndices;
    private double[][] attributes;
    private boolean binary = false;

    /**
     * To use it, instantiate the class by passing a list of function values Objective
     *
     * @param attributes Function Values Objective
     */
    public ArffExecution(double[][] attributes) {
        newInstance(attributes, null, null);
    }

    /**
     * To use it, instantiate the class by passing a list of function values Objective and descriptions for the same
     *
     * @param attributes    Function Values Objective
     * @param objectivesDescriptions Objectives Description
     */
    public ArffExecution(double[][] attributes, double[] classes, String[] objectivesDescriptions) {
        newInstance(attributes, classes, objectivesDescriptions);
    }

    /**
     * To use it, instantiate the class by passing a list of function values Objective and descriptions for the same
     *
     * @param attributes    Function Values Objective
     * @param objectivesDescriptions Objectives Description
     */
    public ArffExecution(double[][] attributes, double[] classes, String[] objectivesDescriptions, boolean binary) {
        this.binary = binary;
        newInstance(attributes, classes, objectivesDescriptions);
    }

    private void newInstance(double[][] attributes, double[] classes, String[] objectivesDescriptions) {
        if (attributes.length <= 0) return;
        attrIndices = attributes[0].length;
        this.attributes = attributes;
        setNumericValues(attributes, objectivesDescriptions);
        setStringValues();
        data = new Instances("MyRelation", listOfAttributes, 0);

        for (int i = 0; i < attributes.length; i++) {
            values = new double[data.numAttributes()];
            System.arraycopy(attributes[i], 0, values, 0, attributes[0].length);
            if (classes != null)
                values[attributes[0].length] = classes[i];
            else
                values[attributes[0].length] = 0;
            data.add(new DenseInstance(1.0, values));
        }
    }

    private void setNumericValues(double[][] attributes, String[] objectivesDescriptions) {
        if (objectivesDescriptions != null) {
            for (String description : objectivesDescriptions) {
                listOfAttributes.add(new Attribute(description));
            }
        } else {
            for (int j = 0; j < attributes[0].length; j++) {
                listOfAttributes.add(new Attribute("obj" + (j + 1)));
            }
        }
    }

    private void setStringValues() {
        if (binary) {
            listOfAttributes.add(new Attribute("class", Arrays.asList("0", "1")));
        } else {
            listOfAttributes.add(new Attribute("class", Arrays.asList("0", "1", "2", "3", "4", "5")));
        }
    }

    public ArrayList<Attribute> getListOfAttributes() {
        return listOfAttributes;
    }

    public void setListOfAttributes(ArrayList<Attribute> listOfAttributes) {
        this.listOfAttributes = listOfAttributes;
    }

    public ArrayList<Attribute> getListOfValues() {
        return listOfValues;
    }

    public void setListOfValues(ArrayList<Attribute> listOfValues) {
        this.listOfValues = listOfValues;
    }

    public Instances getData() {
        return data;
    }

    public void setData(Instances data) {
        this.data = data;
    }

    public double[] getValues() {
        return values;
    }

    public void setValues(double[] values) {
        this.values = values;
    }


    @Override
    public String toString() {
        return "ArffExecution{" +
                "atts=" + listOfAttributes +
                ", attVals=" + listOfValues +
                ", data=" + data +
                ", vals=" + Arrays.toString(values) +
                '}';
    }

    public int getAttrIndices() {
        return attrIndices;
    }

    public void setAttrIndices(int attrIndices) {
        this.attrIndices = attrIndices;
    }

    public double[][] getAttributes() {
        return attributes;
    }

    public void setAttributes(double[][] attributes) {
        this.attributes = attributes;
    }

    /**
     * Used to get Instances withod last column that indentify the class of object
     *
     * @return Instances without class
     */
    public Instances getDataWithoutClass() {
        Instances newIn = new Instances(this.getData());
        newIn.setClassIndex(-1);
        newIn.deleteAttributeAt(attrIndices);
        return newIn;
    }
}
