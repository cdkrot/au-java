package sp;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.rules.TemporaryFolder;
import org.junit.Rule;

import java.util.*;
import java.io.*;

public class SecondPartTasksTest {
    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();
    
    private void writeFile(File f, String text) throws IOException {
        try (FileWriter wr = new FileWriter(f)) {
            wr.write(text);
            wr.close();
        }
    }
    
    @Test
    public void testFindQuotes() throws IOException {
        writeFile(new File(tmp.getRoot(), "1.txt"), "aba\ncaba\nd\ncccc");
        writeFile(new File(tmp.getRoot(), "2.txt"), "lol kek cheburek");
        writeFile(new File(tmp.getRoot(), "3.txt"), "keker caba\naba\nmda");
        writeFile(new File(tmp.getRoot(), "4.txt"), "trash");

        String base = tmp.getRoot().getPath();
        assertEquals(SecondPartTasks.findQuotes(Arrays.asList(base + "/1.txt",
                                                              base + "/2.txt",
                                                              base + "/3.txt",
                                                              base + "/4.txt"), "aba"),
                     Arrays.asList(base + "/1.txt", base + "/3.txt"));
        
        assertEquals(SecondPartTasks.findQuotes(Arrays.asList(base + "/1.txt",
                                                              base + "/2.txt",
                                                              base + "/3.txt",
                                                              base + "/4.txt"), "kek"),
                     Arrays.asList(base + "/2.txt", base + "/3.txt"));
    }

    @Test
    public void testPiDividedBy4() {
        assertTrue(Math.abs(SecondPartTasks.piDividedBy4() - Math.PI / 4.0) < 1e-3);
    }

    @Test
    public void testFindPrinter() {
        Map<String, List<String>> lst = new HashMap<String, List<String>>();

        lst.put("aba", Arrays.asList("1", "2", "3"));
        lst.put("cde", Arrays.asList("123", "4"));
        lst.put("fgh", Arrays.asList("1", "2"));

        assertEquals(SecondPartTasks.findPrinter(lst), "cde");
    }

    @Test
    public void testCalculateGlobalOrder() {
        Map<String, Integer> a = new HashMap<String, Integer>();
        Map<String, Integer> b = new HashMap<String, Integer>();
        Map<String, Integer> c = new HashMap<String, Integer>();
        
        Map<String, Integer> exp = new HashMap<String, Integer>();
        
        a.put("a", 10);
        a.put("b", 20);
        a.put("c", 20);

        b.put("b", 15);
        c.put("a", 30);
        c.put("b", 30);

        exp.put("a", 40);
        exp.put("b", 65);
        exp.put("c", 20);
        
        assertEquals(SecondPartTasks.calculateGlobalOrder(Arrays.asList(a, b, c)), exp);
    }
}
