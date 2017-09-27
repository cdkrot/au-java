package me.cdkrot.javahw;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import me.cdkrot.javahw.Spiral;

public class SpiralTest {
    private ByteArrayOutputStream captured;
    private PrintStream orig;
    
    @Before
    public void setUp() {
        orig = System.out;
        captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured));
    }

    @After
    public void tearDown() {
        System.setOut(orig);
        captured = null;
    }

    @Test
    public void testPrintSpiralTiny() {
        int[][] arr = {{5}};
        Spiral.printSpiral(arr);
        
        assertEquals(captured.toString(), "5\n");
    }
    
    @Test
    public void testPrintSpiral() {
        int[][] arr = {
            { 1,  2,  3,  4, 5},
            { 6,  7,  8,  9, 10},
            {11, 12, 13, 14, 15},
            {16, 17, 18, 19, 20},
            {21, 22, 23, 24, 25}
        };

        Spiral.printSpiral(arr);

        String exp = "13\n14\n19\n18\n17\n12\n7\n8\n9\n10\n15\n20\n25\n24\n23\n22\n21\n";
        exp += "16\n11\n6\n1\n2\n3\n4\n5\n";
        
        assertEquals(captured.toString(), exp);
    }

    @Test
    public void testSortByColumnsOneColumn() {
        int[][] arr = {{5}, {1}, {3}, {4}, {2}};
        int[][] exp = {{5}, {1}, {3}, {4}, {2}};
        arr = Spiral.sortByColumns(arr);
        
        assertEquals(arr, exp);
    }

    @Test
    public void testSortByColumnsTiny() {
        int[][] arr = {{5}};
        int[][] exp = {{5}};
        arr = Spiral.sortByColumns(arr);

        assertEquals(arr, exp);
    }

    @Test
    public void testSortByColumnsMatrix() {
        int[][] arr = {{5, 4, 3}, {0, 2, 1}};
        int[][] exp = {{3, 4, 5}, {1, 2, 0}};
        arr = Spiral.sortByColumns(arr);

        assertEquals(arr, exp);
    }
}
