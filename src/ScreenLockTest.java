import org.junit.Test;

import static org.junit.Assert.*;

public class ScreenLockTest {
    @Test
    public void bascis (){
        ScreenLock sl = new ScreenLock();
        assertEquals(0, sl.calculateCombinations('A',10));
        assertEquals(0, sl.calculateCombinations('A',0));
        assertEquals(0, sl.calculateCombinations('E',14));
        assertEquals(1, sl.calculateCombinations('B',1));
        assertEquals(5, sl.calculateCombinations('C',2));
        assertEquals(8, sl.calculateCombinations('E',2));
        assertEquals(31, sl.calculateCombinations('A', 3));
        assertEquals(37, sl.calculateCombinations('D', 3));
        assertEquals(256, sl.calculateCombinations('E',4));
        assertEquals(23280, sl.calculateCombinations('E', 8));
    }
}