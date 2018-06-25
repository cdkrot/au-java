package me.cdkrot.javahw;



import java.util.Comparator;
import java.io.*;
import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;


public class TestProtocol {    
    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();
    public PipedInputStream  clientToServerIn;
    public PipedOutputStream clientToServerOut;
    public PipedInputStream  serverToClientIn;
    public PipedOutputStream serverToClientOut;
    public Thread serverThread;
    
    
    public TestProtocol() {}

    public void writeTo(File f, String data) throws IOException {
        try (FileWriter fin = new FileWriter(f)) {
            fin.write(data);
        }
    }
    
    @Before
    public void setupEnv() throws IOException {
        tmp.newFolder("dir1");
        tmp.newFolder("dir2");
        File f = tmp.newFile("a.txt");
        tmp.newFile("dir1/b.txt");

        writeTo(f, "example text");
    }

    @Before
    public void setup() throws IOException {
        clientToServerOut = new PipedOutputStream();
        clientToServerIn  = new PipedInputStream(clientToServerOut);

        serverToClientOut = new PipedOutputStream();
        serverToClientIn  = new PipedInputStream(serverToClientOut);
    }

    @After
    public void teardown() throws IOException {
        clientToServerOut.close();
        clientToServerIn.close();

        serverToClientOut.close();
        serverToClientIn.close();

        if (serverThread != null) {
            serverThread.interrupt();
            try {
                serverThread.join();
            } catch (InterruptedException ex) {}
        }
    }

    public void lunchServer() {
        serverThread = new Thread() {
                @Override
                public void run() {
                    try {
                        Server.handleClient(tmp.getRoot().getPath(), clientToServerIn, serverToClientOut);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            };
        serverThread.start();
    }
    
    @Test
    public void testList() throws IOException {
        lunchServer();
        
        FileEntry entry[] = Client.listDirectory("/", serverToClientIn, clientToServerOut);
        Arrays.sort(entry, Comparator.comparing(FileEntry::toString));
        
        FileEntry expected[] = new FileEntry[] {new FileEntry("a.txt", false),
                                                new FileEntry("dir1", true),
                                                new FileEntry("dir2", true)};
        
        assertArrayEquals(entry, expected);
    }

    @Test
    public void testGet() throws Exception {
        lunchServer();
        
        byte[] file = Client.getFile("/a.txt", serverToClientIn, clientToServerOut);
        
        assertEquals(new String(file, "utf-8"), "example text");
    }

    @Test
    public void testListNonExistent() throws Exception {
        lunchServer();
        
        FileEntry entry[] = Client.listDirectory("/404", serverToClientIn, clientToServerOut);
        
        assertArrayEquals(entry, null);
    }
    
    @Test
    public void testGetNonExistent() throws Exception {
        lunchServer();
        
        byte[] file = Client.getFile("/kek.txt", serverToClientIn, clientToServerOut);
        
        assertArrayEquals(file, null);
    }
};
