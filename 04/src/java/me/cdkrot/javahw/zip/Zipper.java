package me.cdkrot.javahw.zip;

import java.io.*;
import java.util.zip.*;
import java.util.Enumeration;
import java.util.regex.*;

/**
 * Zipper class, implements the task.
 */
public class Zipper {
    /**
     * JVM entry point
     * @param args -- cli args.
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Wrong number of params");
            return;
        }

        try {
            Zipper.extractAll(args[0], args[1], ".");
        } catch (IOException exc) {
            System.err.println("IO Exception");
            System.exit(1);
        }
    };

    /**
     * Extracts all archives within path
     * @param path, where to search
     * @param regex, to extract.
     * @param base, where to put the result.
     * @throws IOException, on fail with io.
     */
    public static void extractAll(String path, String regex, String base) throws IOException {
        extractAll(new File(path), Pattern.compile(regex), base);
    }
    
    /**
     * Extracts all archives within path
     * @param path, where to search
     * @param regex, to extract.
     * @param base, where to put the result.
     * @throws IOException, on fail with io.
     */
    public static void extractAll(File path, Pattern regex, String base) throws IOException {
        if (path.isFile()) {
            tryExtract(path, regex, base);
        } else if (path.isDirectory()) {
            for (File f: path.listFiles())
                extractAll(f, regex, base);
        }
    }
    
    /**
     * Function, which extracts the archive.
     * @param path, path of zip
     * @param regex, regex of files to extract
     * @param base, directory to unpack to.
     * @throws IOException, on fail with io.
     */
    public static void tryExtract(File path, Pattern regex, String base) throws IOException {
        try {
            ZipFile zip = new ZipFile(path);

            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry en = entries.nextElement();
                if (regex.matcher(en.getName()).matches())
                    extractEntry(zip, en, base);
            }
        } catch (ZipException ig) {
            // nothing to do.
        }
    }

    /**
     * Helper function, extracts entry from zip
     * @param zip, zip archive
     * @param entry, zip entry
     * @param base, directory to extract to.
     * @throws IOException, on fail with io.
     */
    public static void extractEntry(ZipFile zip, ZipEntry entry, String base) throws IOException {
        File result = new File(base, entry.getName());
        result.getParentFile().mkdirs();
        
        try (InputStream in = zip.getInputStream(entry);
             OutputStream out = new FileOutputStream(result)) {

            byte[] buf = new byte[4096];
            while (true) {
                int cnt = in.read(buf);
                
                if (cnt <= 0)
                    break;
                
                out.write(buf, 0, cnt);
            }
        }
    }
};
