package me.cdkrot.javahw;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

public class TestInjector {
    public interface Chain1 {
    };

    public interface Chain2 {
    };

    public interface Chain3 {
    };

    public static class Chain1Impl implements Chain1 {
        public Chain1Impl() {}
    };

    public static class Chain2Impl implements Chain2 {
        public Chain2Impl(Chain1 ch) {}
    }

    public static class Chain3Impl implements Chain3 {
        public Chain3Impl(Chain2 ch) {}
    }

    public static class Chain4 {
        public Chain4(Chain3 ch) {}
    }

    public static class Chain2Fake implements Chain2 {
        public Chain2Fake() {}
    }

    public interface SelfCycle {
    };
    
    public static class SelfCycleImpl implements SelfCycle {
        public SelfCycleImpl(SelfCycle c) {}
    }

    public interface A {};
    public interface B {};

    public static class AImpl implements A {
        public AImpl(B b) {}
    }

    public static class BImpl implements B {
        public BImpl(A a) {}
    }
    
    @Test
    public void testSimpleChain() throws Exception {
        Object res = Injector.initialize(Chain4.class.getCanonicalName(),
                                         Arrays.asList(Chain1Impl.class, Chain2Impl.class, Chain3Impl.class, Chain4.class));
        assertTrue(res != null);
        assertTrue(res instanceof Chain4);
    }

    @Test
    public void testImpossible() {
        // it is not possible to load Chain4 when Chain2Impl is not listed.
        
        Object res = null;
        Exception ex = null;
        try {
            res = Injector.initialize(Chain4.class.getCanonicalName(),
                                         Arrays.asList(Chain1Impl.class, Chain3Impl.class, Chain4.class));
        } catch (Exception exc) {
            ex = exc;
        }
        
        assertTrue(res == null);
        assertTrue(ex != null);
        assertTrue(ex instanceof ImplementationNotFoundException);
    }

    @Test
    public void testAmbigious() {
        Object res = null;
        Exception ex = null;
        try {
            res = Injector.initialize(Chain4.class.getCanonicalName(),
                                      Arrays.asList(Chain1Impl.class, Chain2Impl.class, Chain3Impl.class, Chain4.class, Chain2Fake.class));
        } catch (Exception exc) {
            ex = exc;
        }
        
        assertTrue(res == null);
        assertTrue(ex != null);
        assertTrue(ex instanceof AmbiguousImplementationException);
    }
    
    @Test
    public void testNotAmbigiousButOnlyWay() throws Exception {
        // actually there is only one way since Chain1Impl is not provided.
        
        Object res = Injector.initialize(Chain4.class.getCanonicalName(),
                                  Arrays.asList(Chain2Impl.class, Chain3Impl.class, Chain4.class, Chain2Fake.class));
        
        assertTrue(res != null);
        assertTrue(res instanceof Chain4);
    }

    @Test
    public void testLoop() {
        Object res = null;
        Exception ex = null;
        try {
            res = Injector.initialize(SelfCycleImpl.class.getCanonicalName(),
                                      Arrays.asList(SelfCycleImpl.class));
        } catch (Exception exc) {
            ex = exc;
        }
        
        assertTrue(res == null);
        assertTrue(ex != null);
        assertTrue(ex instanceof InjectionCycleException);        
    }

    @Test
    public void testCycle() {
        Object res = null;
        Exception ex = null;
        try {
            res = Injector.initialize(AImpl.class.getCanonicalName(),
                                      Arrays.asList(AImpl.class, BImpl.class));
        } catch (Exception exc) {
            ex = exc;
        }
        
        assertTrue(res == null);
        assertTrue(ex != null);
        assertTrue(ex instanceof InjectionCycleException);
    }

}
