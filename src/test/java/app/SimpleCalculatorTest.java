package app;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimpleCalculatorTest {
    @Test
    public void sumTry(){
        int x  = 1;
        int y = 1;
        assertEquals(2, SimpleCalculator.sum(x,y));
    }

    @Test
    public void subtractTry(){
        int x  = 2;
        int y = 1;
        assertEquals(2, SimpleCalculator.subtract(x,y));
    }
}
