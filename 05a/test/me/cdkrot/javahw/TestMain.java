package me.cdkrot.javahw;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test; 
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.security.*;
import java.io.*;

public class TestMain {
    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();
    
    @Test
    public void testSimple() throws IOException {
        String path = "res/text";
        String out = (new File(tmp.getRoot(), "out")).getPath();
        Main.processFile(path, out);

        assertEquals(getFileChecksum(new File(out)), "247388D0D70B79D3743645529E5340F6554528D1");
    }

    @Test
    public void testEmpty() throws IOException {
        String path = "res/empty";
        String out = (new File(tmp.getRoot(), "out")).getPath();
        Main.processFile(path, out);

        assertEquals(getFileChecksum(new File(out)), "DA39A3EE5E6B4B0D3255BFEF95601890AFD80709");
    }

    
    private String getFileChecksum(File f) throws IOException {
        FileInputStream stream = new FileInputStream(f);
        MessageDigest md;

        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Bad library");
        }
        
        byte[] buf = new byte[4096];
        while (true) {
            int cnt = stream.read(buf);
            if (cnt <= 0)
                break;

            md.update(buf, 0, cnt);
        }
        
        String res = "";
        for (byte b: md.digest())
            res += String.format("%02X", b);
        return res;
    }
}
