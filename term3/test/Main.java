package me.cdkrot.javahw;

import me.cdkrot.javahw.hashmap.HashMap;

/**
 * Runs tests on HashMap class
 * @author Sayutin Dmitry
 */
public class Main {
    
    private static int testsPassed = 0;
    
    /**
     * Assertion helper
     * @param val    the value to check
     * @param reason the message to print if fails
     */
    public static void expect(boolean val, String reason) {
        if (!val) {
            System.out.println("FAIL: " + reason);
            System.exit(1);
        }
        
        testsPassed += 1;
    }
    
    /**
     * Hand-written tests
     */
    public static void testSimple() {
        HashMap map = new HashMap();
        
        expect(map.size() == 0, "[01] map.size()");
        expect(map.contains("123") == false, "[02] map.contains()");
        expect(map.contains("") == false, "[03] map.contains()");
        expect(map.get("123") == null, "[04] map.get()");

        
        map.put("123", "228");
        expect(map.size() == 1, "[05] map.size()");
        expect(map.contains("123") == true, "[06] map.contains()");
        expect(map.contains("aba") == false, "[07] map.contains()");
        expect(map.contains("456") == false, "[08] map.contains()");
        expect(map.get("123").equals("228"), "[08] map.get()");
        
        map.put("1100", "mechmath");

        expect(map.size() == 2, "[09] map.size()");
        expect(map.contains("123") == true, "[10] map.contains()");
        expect(map.contains("1100") == true, "[11] map.contains()");
        expect(map.contains("aba") == false, "[12] map.contains()");
        expect(map.contains("456") == false, "[13] map.contains()");
        expect(map.get("123").equals("228"), "[14] map.get()");
        expect(map.get("1100").equals("mechmath"), "[15] map.get()");
        
        map.put("123", null);
        map.put("1100", "new value");
        
        expect(map.size() == 2, "[16] map.size()");
        expect(map.contains("123") == true, "[17] map.contains()");
        expect(map.contains("1100") == true, "[18] map.contains()");
        expect(map.contains("aba") == false, "[19] map.contains()");
        expect(map.get("123") == null, "[20] map.get()");
        expect(map.get("1100").equals("new value"), "[21] map.get()");
        
        map.remove("1100");
        map.remove("123");

        expect(map.size() == 0, "[22] map.size()");
        expect(map.contains("1100") == false, "[23] map.contains()");
        expect(map.contains("123") == false, "[24] map.contains()");
        expect(map.get("1100") == null, "[25] map.get()");
        
        map.put("aba", "caba");
        map.put("1", null);
        map.put("2", "1111");
        
        expect(map.size() == 3, "[26] map.size()");
        
        map.clear();
        
        expect(map.size() == 0, "[27] map.size()");
        expect(map.get("2") == null, "[28] map.get()");
    }
    
    /**
     * Automated testing on big, but simple test
     */
    public static void testRehash() {
        HashMap map = new HashMap();
        
        for (int i = 1; i <= 10000; ++i) {
            map.put(Integer.toString(i), Integer.toString(i));
            
            expect(map.size() == i, "[A1] map.size())");
            expect(map.get("aba") == null, "[A2] map.get()");
            expect(map.contains("aba") == false, "[A3] map.contains()");
            
            for (int j = 1; j <= i; ++j) {
                String s = Integer.toString(j);
                expect(map.get(s).equals(s), "[A4] map.get()");
                expect(map.contains(s) == true, "[A5] map.contains()");
            }
        }
    }
    
    /**
     * Runs tests above
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Testing HashMap");
        
        testSimple();
        testRehash();
        
        System.out.println("PASSED");
        System.out.println("Total " + testsPassed + " assertions succeeded");
    }
}
