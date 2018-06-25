package me.cdkrot.javahw;

import java.io.*;
import java.net.*;

public class Server {
    /**
     * Should be called when client has requested "list" operation.
     * @param root file-serving root
     * @param arg request parameter
     * @param client stream to write the answer to
     * @throws IOException when stream fails.
     */
    public static void handleList(String root, String arg, DataOutputStream client) throws IOException {
        if (arg.equals("/"))
            arg = ".";
       else if (arg.startsWith("/"))
            arg = arg.substring(1);
        
        File dr = new File(root, arg);
        if (!dr.isDirectory()) {
            client.writeInt(-1);
            return;
        }

        File[] listing  = dr.listFiles();
        client.writeInt(listing.length);
        for (File fl: listing) {
            client.writeUTF(fl.getName());
            client.writeBoolean(fl.isDirectory());
        }
    }

    /**
     * Should be called when client has requested "get" operation.
     * @param root file-serving root
     * @param arg request parameter
     * @param client stream to write the answer to
     * @throws IOException when stream fails.
     */
    public static void handleGet(String root, String arg, DataOutputStream client) throws IOException {
        if (arg.startsWith("/"))
            arg = arg.substring(1);
        
        byte[] buffer = new byte[10];
        int size = 0;

        if (!(new File(root, arg)).isFile()) {
            client.writeInt(-1);
            return;
        }
            
        try (InputStream is = new FileInputStream(new File(root, arg))) {
            while (is.available() > 0) {
                byte[] chunk = new byte[1024];
                int len = is.read(chunk);
                
                if (len == -1)
                    break;

                if (size + len > buffer.length) {
                    int new_length = buffer.length;
                    while (new_length < size + len)
                        new_length *= 2;

                    byte[] newbuf = new byte[new_length];
                    for (int i = 0; i != size; ++i)
                        newbuf[i] = buffer[i];

                    buffer = newbuf;
                }

                for (int i = 0; i != len; ++i)
                    buffer[size++] = chunk[i];
            }
        }
        
        client.writeInt(size);
        client.write(buffer, 0, size);
    }
    
    /**
     * Handles the client (expectedly runned in new thread)
     * @param root file-serving root
     * @param inputStream stream to read from
     * @param inputStream stream to write to
     */
    public static void handleClient(String root, InputStream inputStream, OutputStream outputStream) throws IOException {
        try (DataInputStream input = new DataInputStream(inputStream);
             DataOutputStream output = new DataOutputStream(outputStream)) {

            while (true) {
                int type = input.readInt();
                String arg = input.readUTF();

                if (type == 1)
                    handleList(root, arg, output);
                if (type == 2)
                    handleGet(root, arg, output);
            }
        }
    }

    /**
     * Handles the client (expectedly runned in new thread)
     * @param root file-serving root
     * @param inputStream stream to read from
     * @param inputStream stream to write to
     */
    public static void handleClient(String root, Socket socket) throws IOException {
        try (InputStream input = socket.getInputStream();
             OutputStream output = socket.getOutputStream()) {
            handleClient(root, input, output);
        }
    }

    
    /**
     * Entry function for the server
     * @param args cli args
     */
    public static void main(String[] args) throws IOException {
        int port = 2030;

        try (ServerSocket ssock = new ServerSocket(port)) {
            System.out.println("Listening on :" + port);
            
            while (!Thread.interrupted()) {
                try {
                    Socket client = ssock.accept();
                    System.out.println("New incoming connection");
                    
                    new Thread() {
                        @Override
                        public void run() {
                            
                            try {
                                handleClient(".", client);
                                client.close();
                            } catch (IOException ex) {
                                System.err.println("Input/Output error");
                            }
                        }
                    }.start();
                } catch (Exception ex) {
                    ; // pass;
                }
            }
        }
    }
}
