package me.cdkrot.xunit;


import java.io.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import me.cdkrot.xunit.annotations.*;

/**
 * Main class for testing.
 */
public class XUnit {
    /**
     * Prints usage to the stderr.
     */
    public static void usage() {
        System.err.println("usage: <classes-root> <classname>");
    }

    /**
     * CLI-Entry point.
     * @param args cli args.
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            usage();
            return;
        }

        ClassLoader loader = null;

        try {
            loader = new URLClassLoader(new URL[]{new File(args[1]).toURI().toURL()});
        } catch (Exception ex) {
            System.err.println("Failed to create classloader for the specified directory");
            return;
        }

        try {
            Class<?> cls = loader.loadClass(args[1]);
            testClass(cls, System.err);
        } catch (ClassNotFoundException ex) {
            System.err.println("Class " + args[1] + " was not found");
            return;
        } catch (XUnitException ex) {
            System.err.println(ex.getMessage());
            return;
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return;
        }
    }

    /**
     * Tests the specified class and writes result to specified log stream
     * @param cls class to test
     * @param log stream to write testing log
     * @throws Exception when something wents wrong (e.g. reflection error).
     */
    public static void testClass(Class<?> cls, PrintStream log) throws Exception {
        int numIgnored = 0;
        int numOK      = 0;
        int numFailed  = 0;
        
        Object obj = cls.newInstance();

        List<Method> before      = new ArrayList<Method>();
        List<Method> after       = new ArrayList<Method>();
        List<Method> beforeClass = new ArrayList<Method>();
        List<Method> afterClass  = new ArrayList<Method>();
        List<TestMethod> test    = new ArrayList<TestMethod>();

        analyseMethods(cls, before, beforeClass, after, afterClass, test);

        for (Method mtd: beforeClass)
            mtd.invoke(obj);

        for (TestMethod testMethod: test){
            if (!testMethod.ignore.equals("")) {
                log.println("Ignoring test " + testMethod.mtd.getName() + " due to: " + testMethod.ignore);
                numIgnored += 1;
                continue;   
            }
            
            Class<?> eclass = Test.noneClass.class;

            for (Method mtd: before)
                mtd.invoke(obj);
                
            try {
                testMethod.mtd.invoke(obj);
            } catch (InvocationTargetException e) {
                eclass = e.getTargetException().getClass();
            } catch (Exception e) {
                eclass = e.getClass();
            }

            if (eclass != testMethod.expected) {
                log.println("In test " + testMethod.mtd.getName() + ", unexpected exception, expected " + testMethod.expected.getName() + " got " + eclass.getName());

                numFailed += 1;
            } else {
                log.println("OK " + testMethod.mtd.getName());
                numOK += 1;
            }

                            
            for (Method mtd: after)
                mtd.invoke(obj);
        }
        
        for (Method mtd: afterClass)
            mtd.invoke(obj);

        log.println("Completed");
        log.println("Total " + (numOK + numFailed + numIgnored) + " tests, " + numIgnored + " ignored, " + numOK + " succeeded and " + numFailed + " failed");
    }

    /**
     * Analyses class methods and puts them to correspoding list
     * @param cls class to analyse
     * @param before      list for "before" annotated methods.
     * @param after       list for "after" annotated methods.
     * @param beforeClass list for "beforeClass" annotated methods.
     * @param afterClass  list for "afterClass" annotated methods.
     * @param test        list for test annotated methods.x
     */
    public static void analyseMethods(Class<?> cls, List<Method> before, List<Method> beforeClass, List<Method> after, List<Method> afterClass, List<TestMethod> test) throws Exception {
        Method[] methods = cls.getMethods();
        Arrays.sort(methods, Comparator.comparing(Method::getName));
        
        for (Method mtd: methods) {
            boolean allready = false;

            Annotation an[] = mtd.getDeclaredAnnotations();
            for (Annotation a: an) {
                boolean ths = false;
            
                if (a.annotationType() == After.class) {
                    after.add(mtd);
                    ths = true;
                }
                
                if (a.annotationType() == Before.class) {
                    before.add(mtd);
                    ths = true;
                }
                
                if (a.annotationType() == AfterClass.class) {
                    afterClass.add(mtd);
                    ths = true;
                }

                if (a.annotationType() == BeforeClass.class) {
                    beforeClass.add(mtd);
                    ths = true;
                }

                if (a.annotationType() == Test.class) {
                    test.add(new TestMethod(mtd, (Test)a));
                    ths = true;
                }

                if (ths && allready)
                    throw new InvalidAnnotations("Invalid annotations in method " + mtd.getName());

                if (ths)
                    allready = true;
            }
        }
    }

    private static class TestMethod {
        public TestMethod() {}
        public TestMethod(Method method, Test tst) {
            this.method = method;
            expected = tst.expected();
            ignore   = tst.ignore();
        }
        
        public const Method method;
        public const Class<?> expected;
        public const String ignore;
    }

    /**
     * Generic XUnit exception class
     */
    public static class XUnitException extends RuntimeException {
        public XUnitException() {
            super();
        }
        
        public XUnitException(String msg) {
            super(msg);
        }

    }

    /**
     * Exception, which represents that annotations are invalid.
     */
    public static class InvalidAnnotations extends XUnitException {
        public InvalidAnnotations() {
            super();
        }
        
        public InvalidAnnotations(String msg) {
            super(msg);
        }
        
    }
};
