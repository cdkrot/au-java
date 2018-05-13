package me.cdkrot.javahw;

import java.io.*;
import java.net.*;

class Server {
    public static void handleList(String arg, DataOutputStream client) throws IOException {
        if (arg.equals("/"))
            arg = ".";
        else if (arg.startsWith("/"))
            arg = arg.substring(1);

        File dr = new File(arg);
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

    public static void handleGet(String arg, DataOutputStream client) throws IOException {
        if (arg.startsWith("/"))
            arg = arg.substring(1);
        
        byte[] buffer = new byte[10];
        int size = 0;
        
        try (InputStream is = new FileInputStream(arg)) {
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
    
    public static void handleClient(Socket client) {
        try (DataInputStream input = new DataInputStream(client.getInputStream());
             DataOutputStream output = new DataOutputStream(client.getOutputStream())) {

            while (true) {
                int type = input.readInt();
                String arg = input.readUTF();

                if (type == 1)
                    handleList(arg, output);
                if (type == 2)
                    handleGet(arg, output);
            }
        } catch (IOException ex) {
        }
    }
    
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
                            handleClient(client);
                            
                            try {
                                client.close();
                            } catch (IOException ex) {}
                        }
                    }.start();
                } catch (Exception ex) {
                    ; // pass;
                }
            }
        }
    }
}
