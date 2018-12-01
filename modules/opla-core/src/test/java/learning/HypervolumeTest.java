package learning;

import jmetal4.qualityIndicator.Hypervolume;
import org.junit.Test;

public class HypervolumeTest {

    @Test
    public void calculate() {
        double[][] fitness = {{787.0, 33.0, 25.0},
        {676.0, 15.0, 30.0},
        {711.0, 17.0, 29.0},
        {729.0, 27.0, 26.0},
        {735.0, 21.0, 27.0},
        {705.0, 23.0, 27.0},
        {726.0, 18.0, 28.0},
        {713.0, 22.0, 28.0}};


        Hypervolume hypervolume = new Hypervolume();

//        hypervolume.hypervolume()
    }
}
