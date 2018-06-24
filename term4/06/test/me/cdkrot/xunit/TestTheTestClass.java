package me.cdkrot.xunit;

import java.io.*;
import org.junit.Test; 
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class TestTheTestClass {
    @Test
    public void runTesting() throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        PrintStream  log = new PrintStream(out);
        TheTestClass.log = log;
        
        XUnit.testClass(TheTestClass.class, log);

        String result = out.toString();
        result = result.replace(System.lineSeparator(), "\n");
        
        assertTrue(out.toString().equals
                   ("[[Before class called]]\n" +
                    "[[Before called]]\n" +
                    "OK test\n" +
                    "[[After called]]\n" + 
                    "Ignoring test testWhichIsIgnored due to: crap\n" +
                    "[[Before called]]\n" +
                    "OK testWithException\n" +
                    "[[After called]]\n" +
                    "[[Before called]]\n" +
                    "In test testWithIncorrectException, unexpected exception, expected java.io.IOException got java.lang.NumberFormatException\n" + 
                    "[[After called]]\n" +
                    "[[After class called]]\n" +
                    "Completed\n" +
                    "Total 4 tests, 1 ignored, 2 succeeded and 1 failed\n"));
    }
    
}
