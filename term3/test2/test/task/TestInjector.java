package task;

import org.junit.Test;
import task.testClasses.ClassWithOneClassDependency;
import task.testClasses.ClassWithOneInterfaceDependency;
import task.testClasses.ClassWithoutDependencies;
import task.testClasses.InterfaceImpl;

import java.util.Collections;

import static org.junit.Assert.assertTrue;

import me.cdkrot.javahw.*;

/**
 * Contains tests written by Yurii Litvinov, almost untouched.
 */
public class TestInjector {

    @Test
    public void injectorShouldInitializeClassWithoutDependencies()
            throws Exception {
        Object object = Injector.initializeByNames("task.testClasses.ClassWithoutDependencies", Collections.emptyList());
        assertTrue(object instanceof ClassWithoutDependencies);
    }

    @Test
    public void injectorShouldInitializeClassWithOneClassDependency()
            throws Exception {
        Object object = Injector.initializeByNames(
                "task.testClasses.ClassWithOneClassDependency",
                Collections.singletonList("task.testClasses.ClassWithoutDependencies")
        );
        assertTrue(object instanceof ClassWithOneClassDependency);
        ClassWithOneClassDependency instance = (ClassWithOneClassDependency) object;
        assertTrue(instance.dependency != null);
    }

    @Test
    public void injectorShouldInitializeClassWithOneInterfaceDependency()
            throws Exception {
        Object object = Injector.initializeByNames(
                "task.testClasses.ClassWithOneInterfaceDependency",
                Collections.singletonList("task.testClasses.InterfaceImpl")
        );
        assertTrue(object instanceof ClassWithOneInterfaceDependency);
        ClassWithOneInterfaceDependency instance = (ClassWithOneInterfaceDependency) object;
        assertTrue(instance.dependency instanceof InterfaceImpl);
    }
}
