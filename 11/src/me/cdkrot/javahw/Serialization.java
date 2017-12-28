package me.cdkrot.javahw;

import java.util.*;
import java.io.*;
import java.lang.reflect.*;

/**
 * 
 */
public class Serialization {
    /**
     *
     */
    public static void serialize(Object obj, OutputStream os) throws IOException, ReflectiveOperationException {
        Class<?> cls = obj.getClass();
        Field[] fields = cls.getFields();
        Arrays.sort(fields, Comparator.comparing(Field::getName));

        DataOutputStream dos = new DataOutputStream(os);
        for (Field fld: fields) {
            if (fld.getClass() == (Class<?>)(byte.class))
                dos.writeByte(fld.getByte(obj));
            else if (fld.getClass() == (Class<?>)(short.class))
                dos.writeShort(fld.getShort(obj));
            else if (fld.getClass() == (Class<?>)(int.class))
                dos.writeInt(fld.getInt(obj));
            else if (fld.getClass() == (Class<?>)(float.class))
                dos.writeFloat(fld.getFloat(obj));
            else if (fld.getClass() == (Class<?>)(double.class))
                dos.writeDouble(fld.getDouble(obj));
            else if (fld.getClass() == (Class<?>)(String.class))
                dos.writeUTF((String)fld.get(obj));
            else
                throw new RuntimeException("invalid field");
        }
    }
        
    public static <T> T deserialize(InputStream is, Class<T> cls) {return null;}
}
