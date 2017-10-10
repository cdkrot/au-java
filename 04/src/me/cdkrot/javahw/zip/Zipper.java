package me.cdkrot.javahw.zip;

import java.io.*;
import java.util.zip.*;
import java.util.Enumeration;
import java.util.regex.*;

public class Zipper {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Wrong number of params");
            return;
        }

        Zipper.extract(args[0], args[1]);
    };

    public static void extract(String path, String regex) throws IOException {
        ZipFile zip = new ZipFile(path);
        Pattern pat = Pattern.compile(regex);
        
        Enumeration<? extends ZipEntry> entries = zip.entries();
        while (entries.hasMoreElements()) {
            ZipEntry en = entries.nextElement();
            if (pat.matcher(en.getName()).matches())
                extractEntry(zip, en, ".");
        }
    }

    public static void extractEntry(ZipFile zip, ZipEntry entry, String base) throws IOException {
        try (InputStream in = zip.getInputStream(entry);
             OutputStream out = new FileOutputStream(new File(base, entry.getName()))) {
            while (true) {
                byte[] buf = new byte[4096];
                int cnt = in.read(buf);
                
                if (cnt <= 0)
                    break;
                
                out.write(buf, 0, cnt);
            }
        }
    }
};
