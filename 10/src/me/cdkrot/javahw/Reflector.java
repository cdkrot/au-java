package me.cdkrot.javahw;

class Reflector {
    public static void printStructure(Class<?> someClass, String name, OutputStream os) throws IOException {
        
    }
    
    public static void printStructure(Class<?> someClass) throws IOException {
        try (FileOutputStream out = new FileOutputStream("SomeClass.java")) {
            printStructure(someClass, "SomeClass", out);
        }
    }

    
}
