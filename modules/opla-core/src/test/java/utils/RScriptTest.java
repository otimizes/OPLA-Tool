package utils;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class RScriptTest {

    @Test
    public void main() {
        try {
            RScript rScript = new RScript("shapiro.test(c(1.02323900, 1.02324700, 1.02327500, 1.02834500, 1.02209000, 1.02187600, 1.02050100, 1.02173300, 1.02324600, 1.02727700, 1.02181600, 1.02174300, 1.02039400, 1.02214400, 1.02324700, 1.02324700, 1.02183100, 1.02857600, 1.02043900, 1.02183200, 1.02973100, 1.02706300, 1.02711600, 1.02188500, 1.02048400, 1.02315600, 1.02045600, 1.02474000, 1.02695500, 1.02177100, 0.00091900, 0.00056900, 0.00056900, 0.00056900, 0.00057300))");
            String result = rScript.getResult();
            String expected = "\tShapiro-Wilk normality testdata:  c(1.023239, 1.023247, 1.023275, 1.028345, 1.02209, 1.021876,     1.020501, 1.021733, 1.023246, 1.027277, 1.021816, 1.021743,     1.020394, 1.022144, 1.023247, 1.023247, 1.021831, 1.028576,     1.020439, 1.021832, 1.029731, 1.027063, 1.027116, 1.021885,     1.020484, 1.023156, 1.020456, 1.02474, 1.026955, 1.021771,     0.000919, 0.000569, 0.000569, 0.000569, 0.000573)W = 0.42448, p-value = 1.402e-10";
            assertEquals(expected, result.replaceAll("\n", ""));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("R NÃO INSTALADO");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
