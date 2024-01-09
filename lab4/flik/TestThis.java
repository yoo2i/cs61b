package flik;

import static org.junit.Assert.*;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

public class TestThis {

    @Test
    public void justTestFlik() {
        for (int i = 128; i < 500; i++) {
            assertTrue("i is " + i + " and j is " + i, Flik.isSameNumber(i, i));
        }
    }

    @Test
    public void ramdomTestFlik() {
        for (int i = 0; i < 100000; i++) {
            int j = StdRandom.uniform(0, 100000);
            assertTrue("i is " + j, Flik.isSameNumber(j, j));
        }
    }
}
