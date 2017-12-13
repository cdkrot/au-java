package me.cdkrot.javahw;

import java.util.*;
import java.lang.reflect.*;

/**
 * Helper class to perform dependency injection
 */
public class Injector {
    private static class ClassInfo {
        private ClassInfo() {}
        private ClassInfo(Class<?> cl) {
            cls = cl;
        }
        
        private Class<?> cls;

        // zero means, that class is not yet created.
        // one means, that class has started to construct.
        // two means, that class is created (then result has an instance).
        private int status = 0;
        private Object result = null;
    };

    /*
     * info: classname => it's info
     * impl: interface name => list of it's possible implementations.
     * return null if fail
     * return object on success
     * throw one of InjectorException on big fails.
     */
    private static Object dfs(String cur, Map<String, ClassInfo> info,
                              Map<String, List<String>> impl) throws InjectorException {        
        if (!info.containsKey(cur))
            return null;
        
        ClassInfo data = info.get(cur);
        
        if (data.status == 2)
            return data.result;
        if (data.status == 1)
            throw new InjectionCycleException();

        data.status = 1;
        
        if (data.cls.getConstructors().length != 1)
            throw new InvalidConstructorsCountException("Class " + cur + " has " + data.cls.getConstructors().length + " constructors");

        Constructor<?> cons = data.cls.getConstructors()[0];

        Object args[] = new Object[cons.getParameterCount()];
        Class<?> types[] = cons.getParameterTypes();

        int i;
        for (i = 0; i != cons.getParameterCount(); ++i) {
            if (info.containsKey(types[i].getCanonicalName())) {
                // the i'th argument is a class instance, not interface
                // simply construct the class.
                
                args[i] = dfs(types[i].getCanonicalName(), info, impl);
                if (args[i] == null)
                    break;
                else
                    continue;
            }

            int count = 0;
            if (impl.containsKey(types[i].getCanonicalName())) {
                for (String theimpl: impl.get(types[i].getCanonicalName())) {
                    Object sub = dfs(theimpl, info, impl);
                    
                    if (sub != null) {
                        args[i] = sub;
                        count += 1;
                    }
                }
            }
            
            if (count == 0)
                break; // failed to create the parameter.
            if (count >= 2)
                throw new AmbiguousImplementationException(); // too many ways.
        }
        
        data.status = 2;
        if (i == cons.getParameterCount()) {
            try {
                data.result = cons.newInstance(args);
            } catch (ReflectiveOperationException ex) {
                throw new InjectorException("reflection exception");
            }
        }
        
        return data.result;
    }
    
    /**
     * Performs depedency injection.
     * @param target class to create
     * @param classes list of implementations available.
     * @throw InjectorException subclass on big errors.
     */
    public static Object initialize(String target, List<Class<?>> classes) throws InjectorException {
        Map<String, ClassInfo> info = new HashMap<String, ClassInfo>();
        Map<String, List<String>> impl = new HashMap<String, List<String>>();
        
        for (Class<?> cls: classes)
            info.put(cls.getCanonicalName(), new ClassInfo(cls));
            
        for (Class<?> cls: classes)
            for (Class<?> interf: cls.getInterfaces()) {
                if (!impl.containsKey(interf.getCanonicalName()))
                    impl.put(interf.getCanonicalName(), new ArrayList<String>());
                
                impl.get(interf.getCanonicalName()).add(cls.getCanonicalName());
            }
        
        Object res = dfs(target, info, impl);
        
        if (res == null)
            throw new ImplementationNotFoundException();
        else
            return res;
    }

    /**
     * Performs depedency injection.
     *
     * Additionaly loads all the listed class by their names
     * Also makes sure, that list of classes contains target to
     * conform to Yurii Litvinov's tests. 
     *
     * @param target class to create
     * @param classes list of implementations available.
     * @throw InjectorException subclass on big errors.
     */
    public static Object initializeByNames(String target, List<String> classes) throws Exception {
        List<Class<?>> result = new ArrayList<Class<?>>();
        ClassLoader loader = Injector.class.getClassLoader();
        for (String name: classes)
            result.add(loader.loadClass(name));

        // conforming to Yurii Litvinov's tests.
        if (!classes.contains(target))
            result.add(loader.loadClass(target));
        
        return initialize(target, result);
    }
}
