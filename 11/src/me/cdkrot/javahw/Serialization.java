package me.cdkrot.javahw;

import java.util.*;
import java.io.*;
import java.lang.reflect.*;

/**
 * Helper class for serialization
 */
public class Serialization {
    
    /**
     * Writes object to stream
     * @param obj object to write
     * @param os output stream to use
     * @throws IOException on io fail
     * @throws ReflectiveOperationException when failed to work with class
     */
    public static void serialize(Object obj, OutputStream os) throws IOException, ReflectiveOperationException {
        Class<?> cls = obj.getClass();
        Field[] fields = cls.getFields();
        Arrays.sort(fields, Comparator.comparing(Field::getName));

        DataOutputStream dos = new DataOutputStream(os);
        for (Field fld: fields) {
            if (fld.getType() == byte.class)
                dos.writeByte(fld.getByte(obj));
            else if (fld.getType() == short.class)
                dos.writeShort(fld.getShort(obj));
            else if (fld.getType() == int.class)
                dos.writeInt(fld.getInt(obj));
            else if (fld.getType() == long.class)
                dos.writeLong(fld.getLong(obj));
            else if (fld.getType() == float.class)
                dos.writeFloat(fld.getFloat(obj));
            else if (fld.getType() == double.class)
                dos.writeDouble(fld.getDouble(obj));
            else if (fld.getType() == String.class)
                dos.writeUTF((String)fld.get(obj));
            else
                throw new RuntimeException("invalid field");
        }
    }
    
    /**
     * Reads object from stream
     * @param cls class to read
     * @param is input stream to use
     * @throw IOException on io fail
     * @throw ReflectiveOperationException when failed to work with class
     */
    public static <T> T deserialize(Class<T> cls, InputStream is) throws IOException, ReflectiveOperationException {
        T obj = cls.newInstance();

        Field[] fields = cls.getFields();
        Arrays.sort(fields, Comparator.comparing(Field::getName));

        DataInputStream dis = new DataInputStream(is);
        for (Field fld: fields) {
            if (fld.getType() == byte.class)
                fld.setByte(obj, dis.readByte());
            else if (fld.getType() == short.class)
                fld.setShort(obj, dis.readShort());
            else if (fld.getType() == int.class)
                fld.setInt(obj, dis.readInt());
            else if (fld.getType() == long.class)
                fld.setLong(obj, dis.readLong());
            else if (fld.getType() == float.class)
                fld.setFloat(obj, dis.readFloat());
            else if (fld.getType() == double.class)
                fld.setDouble(obj, dis.readDouble());
            else if (fld.getType() == String.class)
                fld.set(obj, dis.readUTF());
            else
                throw new RuntimeException("invalid field");
        }

        return obj;
    }
}
