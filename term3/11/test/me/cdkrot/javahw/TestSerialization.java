package me.cdkrot.javahw;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test; 

import java.io.*;

public class TestSerialization {
    public static class TestClass {
        public String a;
        public byte b;
        public int c;
        public short d;
        public long e;
        public float f;
        public double g;

        public boolean equals(Object other) {
            if (!(other instanceof TestClass))
                return false;

            TestClass tc = (TestClass)other;
            return a.equals(tc.a) && b == tc.b && c == tc.c && d == tc.d && e == tc.e
                && Math.abs(f - tc.f) < 1e-9 && Math.abs(g - tc.g) < 1e-9;
        }
    };

    public static class EmptyClass {
        public boolean equals(Object other) {
            return other.getClass() == this.getClass();
        }
    };

    private byte[] serialize(Object o) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Serialization.serialize(o, os);

        return os.toByteArray();
    }

    private Object deserialize(Class<?> cl, byte[] data) throws Exception {
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        Object o = Serialization.deserialize(cl, is);
        
        assertTrue(is.available() == 0);
        return o;
    }
    
    @Test
    public void testFull() throws Exception {
        TestClass tc = new TestClass();
        tc.a = "123";
        tc.b = 1;
        tc.c = 2;
        tc.d = 3;
        tc.e = 4;
        tc.f = 5.0f;
        tc.g = 6.0;

        assertTrue(tc.equals(deserialize(TestClass.class, serialize(tc))));
    }

    @Test
    public void testEmpty() throws Exception {
        EmptyClass em = new EmptyClass();
        
        assertTrue(em.equals(deserialize(EmptyClass.class, serialize(em))));
    }
}
