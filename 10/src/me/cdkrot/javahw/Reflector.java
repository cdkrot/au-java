package me.cdkrot.javahw;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.lang.annotation.*;

class Reflector {
    private static String stringJoin(ArrayList<String> list, String sep) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i != list.size(); ++i) {
            if (i != 0)
                res.append(sep);
            res.append(list.get(i));
        }
        
        return res.toString();
    }

    private static String shortTypeName(Type type) {
        return shortTypeName(type.getTypeName());
    }
    
    private static String shortTypeName(String type) {
        String res = type;
        
        int i = res.lastIndexOf("$");
        if (i != -1)
            res = res.substring(i + 1);

        if (res.lastIndexOf(".") != -1 && res.substring(0, res.lastIndexOf(".")).equals("java.lang"))
            res = res.substring(res.lastIndexOf(".") + 1);

        System.err.println(type + " -> " + res);
        
        return res;
    }
    
    private static String getModifiers(int mod) {
        ArrayList<String> list = new ArrayList<String>();

        if (Modifier.isPublic(mod))
            list.add("public");
        if (Modifier.isPrivate(mod))
            list.add("private");
        if (Modifier.isProtected(mod))
            list.add("protected");

        if (Modifier.isStatic(mod))
            list.add("static");

        if (Modifier.isFinal(mod))
            list.add("final");
        
        if (Modifier.isAbstract(mod))
            list.add("abstract");

        return stringJoin(list, " ");
    }

    private static String getGenericType(TypeVariable<?> type) {
        String res = type.getName();
        ArrayList<String> bounds = new ArrayList<String>();
        
        for (Type bnd: type.getBounds())
            if (bnd.getTypeName() != "java.lang.Object")
                bounds.add(shortTypeName(bnd.getTypeName()));

        if (bounds.size() > 0) {
            res += " extends ";
            res += stringJoin(bounds, " & ");
        }
        
        return res;
    }
    
    private static String getGenericTypes(TypeVariable<?>[] types) {
        ArrayList<String> lst = new ArrayList<String>();
        for (int i = 0; i != types.length; ++i)
            lst.add(getGenericType(types[i]));

        if (lst.size() > 0)
            return "<" + stringJoin(lst, ", ") + ">";
        return "";
    }
    
    private static String getHeader(Class<?> cls) throws IOException {
        String res = getModifiers(cls.getModifiers());
        if (!res.isEmpty())
            res += " ";

        res += "class ";
        res += cls.getSimpleName();

        res += getGenericTypes(cls.getTypeParameters());
        
        res += " ";

        if (cls.getSuperclass() != Object.class) {
            res += "extends " + shortTypeName(cls.getGenericSuperclass().getTypeName()) + " ";
        }

        if (cls.getGenericInterfaces().length != 0) {
            res += "implements ";

            ArrayList<String> lst = new ArrayList<String>();
            for (Type interf: cls.getGenericInterfaces())
                lst.add(shortTypeName(interf.getTypeName()));
            
            res += stringJoin(lst, ", ") + " ";
        }

        res += "{";
        
        return res;
    }

    private static String genParameterName(int id) {
        id += 1;

        String res = "";
        while (id != 0) {
            res += (char)('a' + ((id + 25) % 26));
            id /= 26;
        }
        return res;
    }
    
    private static String createReturn(Class<?> cl) {
        String v = getSensibleValue(cl);
        if (v == "")
            return v;
        else
            return "return " + v + ";";
    }

    private static String getSensibleValue(Class<?> cl) {
        if (cl == void.class)
            return "";
        else if (!cl.isPrimitive())
            return "null";
        else if (cl == boolean.class)
            return "false";
        else
            return "0";
    }
    
    private static String getSignature(Method mt) {
        String res = getModifiers(mt.getModifiers());
        if (!res.isEmpty())
            res += " ";

        String tps = getGenericTypes(mt.getTypeParameters());
        if (!tps.isEmpty())
            res += tps + " ";
        
        res += shortTypeName(mt.getGenericReturnType().getTypeName()) + " ";
        res += mt.getName();
        res += "(";

        ArrayList<String> lst = new ArrayList<String>();
        int id = 0;
        for (Type type: mt.getGenericParameterTypes())
            lst.add(shortTypeName(type.getTypeName()) + ' ' + genParameterName(id++));
        
        res += stringJoin(lst, ", ");
        
        res += ") {" + createReturn(mt.getReturnType()) + "}";

        return res;
    }

    private static String getIndent(int indent) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i != 4 * indent; ++i)
            s.append(' ');
        return s.toString();
    }

    private static String getFieldSignature(Field fld) {
        String res = getModifiers(fld.getModifiers());
        if (!res.isEmpty())
            res += " ";
        res += shortTypeName(fld.getType().getTypeName()) + " " + fld.getName();

        return res;
    }
    
    private static void writeField(int indent, Field fld, Writer os) throws IOException {
        os.write(getIndent(indent) + getFieldSignature(fld) + " = " + getSensibleValue(fld.getType()) + ";\n");
    }
    
    public static void writeClass(int indent, Class<?> cls, Writer os) throws IOException {
        if (!cls.isSynthetic()) {
            os.write(getIndent(indent) + getHeader(cls) + "\n");

            for (Class<?> cl: cls.getDeclaredClasses())
                writeClass(indent + 1, cl, os);

            for (Field fld: cls.getDeclaredFields())
                writeField(indent + 1, fld, os);
            
            for (Method mt: cls.getDeclaredMethods()) {
                for (Annotation an: mt.getDeclaredAnnotations())
                    os.write(getIndent(indent + 1) + an.toString() + "\n");
                os.write(getIndent(indent + 1) + getSignature(mt) + "\n");
            }
            
            os.write(getIndent(indent) + "}\n");
        }
    }
    
    public static void printStructure(Class<?> cls) throws IOException {
        try (Writer out = new FileWriter(cls.getSimpleName() + ".java")) {
            out.write("package " + cls.getPackage().getName() + ";\n");
            
            writeClass(0, cls, out);
        }
    }

    private static void printDiff(ArrayList<String> ma, ArrayList<String> mb, PrintStream stream) {
        Collections.sort(ma);
        Collections.sort(mb);

        int i = 0, j = 0;

        while (i != ma.size() && j != mb.size())
            if (ma.get(i).compareTo(mb.get(j)) < 0) {
                stream.println("- " + ma.get(i));
                ++i;
            } else if (ma.get(i).compareTo(mb.get(j)) == 0) {
                ++i;
                ++j;
            } else {
                stream.println("+ " + mb.get(j));
                ++j;
            }

        while (i != ma.size()) {
            stream.println("- " + ma.get(i));
            ++i;
        }

        while (j != mb.size()) {
            stream.println("+ " + mb.get(j));
            ++j;
        }
    }

    /**
     * Prints the difference of two classes
     * In terms of mismatched methods and fields
     * @param a first class
     * @param b second class
     * @param stream to print difference
     */
    public static void diffClasses(Class<?> a, Class<?> b, PrintStream stream) {
        stream.println("# Different methods");

        ArrayList<String> ma = new ArrayList<String>();
        ArrayList<String> mb = new ArrayList<String>();

        for (Method mt: a.getDeclaredMethods())
            ma.add(getSignature(mt));
        for (Method mt: b.getDeclaredMethods())
            mb.add(getSignature(mt));

        printDiff(ma, mb, stream);
        
        ma.clear();
        mb.clear();

        stream.println("# Different fields");
        
        for (Field fld: a.getDeclaredFields())
            ma.add(getFieldSignature(fld) + ";");
        for (Field fld: b.getDeclaredFields())
            mb.add(getFieldSignature(fld) + ";");
        
        printDiff(ma, mb, stream);
    }

    /**
     * Prints the difference of two classes
     * In terms of mismatched methods and fields
     * Here difference printed to System.out
     * @param a first class
     * @param b second class
     */
    public static void diffClasses(Class<?> a, Class<?> b) {
        diffClasses(a, b, System.out);
    }

    public static void main(String[] args) throws Exception {
        //        printStructure(Object.class);
                printStructure(Collections.class);
        //printStructure(ArrayList.class);
    }
}
