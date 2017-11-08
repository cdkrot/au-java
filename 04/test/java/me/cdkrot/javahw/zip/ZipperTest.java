package me.cdkrot.javahw.zip;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import java.security.*;
import java.io.*;
import java.util.Map;
import java.util.HashMap;

public class ZipperTest {
    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();
    
    @Test
    public void testExtractSimple() throws IOException {
        String path = "test/resources/test_simple";
        
        Zipper.extractAll(path, "d.*\\.cpp", tmp.getRoot().getPath());

        Map<String, String> exp = new HashMap<String, String>();
        exp.put("d2.cpp", "8E0532940A8268586BD3D8AC86070383993871D9");
        exp.put("d.cpp",  "02732796FE6803D9C56825ED16C8002FA45F6CA4");
        
        Map<String, String> mp = new HashMap<String, String>();
        
        for (File f: tmp.getRoot().listFiles())
            mp.put(f.getName(), getFileChecksum(f));

        assertTrue(mp.equals(exp));
    }

    @Test
    public void testExtractEmpty() throws IOException {
        String path = "test/resources/test_emptytest";

        Zipper.extractAll(path, ".*", tmp.getRoot().getPath());
        assertEquals(tmp.getRoot().listFiles().length, 0);
    }
    
    @Test
    public void testExtractMultiFile() throws IOException {
        String path = "test/resources/test_multifile";
        
        Zipper.extractAll(path, ".*", tmp.getRoot().getPath());

        Map<String, String> exp = new HashMap<String, String>();
        exp.put("a.txt", "38505A984F71C07843A5F3E394ADA2BF4C7B6ABC");
        exp.put("b.txt", "FC92C5EA9CDAFDF0825E180C3677474A0CFD045F");
        
        Map<String, String> mp = new HashMap<String, String>();
        
        for (File f: tmp.getRoot().listFiles())
            mp.put(f.getName(), getFileChecksum(f));

        assertTrue(mp.equals(exp));
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
};
