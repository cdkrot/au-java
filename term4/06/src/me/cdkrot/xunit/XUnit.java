package me.cdkrot.xunit;


import java.io.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import me.cdkrot.xunit.annotations.*;

class XUnit {
    public static void usage() {
        System.err.println("usage: <classes-root> <classname>");
    }
    
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
            Class cls = loader.loadClass(args[1]);
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

    public static void testClass(Class cls, PrintStream log) throws Exception {
        int numIgnored = 0;
        int numOK      = 0;
        int numFailed  = 0;
        
        Object obj = cls.newInstance();

        List<Method> before      = new ArrayList<Method>();
        List<Method> after       = new ArrayList<Method>();
        List<Method> beforeClass = new ArrayList<Method>();
        List<Method> afterClass  = new ArrayList<Method>();
        List<TestMethod> test    = new ArrayList<TestMethod>();

        analyzeMethods(cls, before, beforeClass, after, afterClass, test);

        for (Method mtd: beforeClass)
            mtd.invoke(obj);

        for (TestMethod testMethod: test){
            if (!testMethod.ignore.equals("")) {
                log.println("Ignoring test " + testMethod.mtd.getName() + " due to: " + testMethod.ignore);
                numIgnored += 1;
                continue;   
            }
            
            Class eclass = Test.noneClass.class;

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

    public static void analyzeMethods(Class cls, List<Method> before, List<Method> beforeClass, List<Method> after, List<Method> afterClass, List<TestMethod> test) throws Exception {
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

    public static class TestMethod {
        public TestMethod() {}
        public TestMethod(Method mtd, Test tst) {
            this.mtd = mtd;
            expected = tst.expected();
            ignore   = tst.ignore();
        }
        
        public Method mtd;
        public Class expected;
        public String ignore;
    }
        
    public static class XUnitException extends RuntimeException {
        public XUnitException() {
            super();
        }
        
        public XUnitException(String msg) {
            super(msg);
        }

    }

    public static class InvalidAnnotations extends XUnitException {
        public InvalidAnnotations() {
            super();
        }
        
        public InvalidAnnotations(String msg) {
            super(msg);
        }
        
    }
};
