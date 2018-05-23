package me.cdkrot.xunit;

import java.io.IOException;
import java.io.PrintStream;
import me.cdkrot.xunit.annotations.*;

public class TheTestClass {
    public static PrintStream log = System.err;

    public void TheTestClass() {}
    
    @BeforeClass
    public void beforeClass() {
        log.println("[[Before class called]]");
    }

    @AfterClass
    public void afterClass() {
        log.println("[[After class called]]");
    }
    
    @Before
    public void before() {
        log.println("[[Before called]]");
    }

    @After
    public void after() {
        log.println("[[After called]]");
    }

    @Test
    public void test() {
        // /bin/true, do nothing, successfully.
    }

    @Test(expected=NumberFormatException.class)
    public void testWithException() {
        int val = Integer.valueOf("rock-and-roll");

        log.println(val);
    }

    @Test(expected=IOException.class)
    public void testWithIncorrectException() {
        int val = Integer.valueOf("rock-and-roll");

        log.println(val);
    }

    @Test(ignore="crap")
    public void testWhichIsIgnored() {
        log.println("[[wtf]]");
    }
}
