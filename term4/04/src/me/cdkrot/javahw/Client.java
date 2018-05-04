package me.cdkrot.javahw;

import java.io.*;
import java.net.*;
import java.util.Scanner;

class Client {
    static class PairStringBool { // because java truly sucks;
        String path;
        boolean isdir;
    }
    
    public static void handleList(String path, DataInputStream input, DataOutputStream output) throws IOException {
        output.writeInt(1);
        output.writeUTF(path);

        int cnt = input.readInt();
        if (cnt == -1) {
            System.out.println("Directory " + path + " doesn't exist");
            return;
        }
        
        if (cnt < 0 || cnt > 1000)
            throw new RuntimeException("Illegal responce");

        PairStringBool res[] = new PairStringBool[cnt];
        for (int i = 0; i != cnt; ++i) {
            res[i] = new PairStringBool();
            res[i].path  = input.readUTF();
            res[i].isdir = input.readBoolean();
        }

        System.out.println("Directory listing for " + path);
        System.out.println("total " + cnt);
        for (int i = 0; i != cnt; ++i)
            System.out.println((res[i].isdir ? "d " : "f ") + res[i].path);
    }

    public static void handleGet(String path, DataInputStream input, DataOutputStream output) throws IOException {
        output.writeInt(2);
        output.writeUTF(path);

        int len = input.readInt();
        if (len == -1) {
            System.out.println("File " + path + " doesn't exist");
            return;
        }
        
        if (len < 0 || len > 1000)
            throw new RuntimeException("Illegal responce");

        byte[] buffer = new byte[len];
        int off = 0;
        while (off != len)
            off += input.read(buffer, off, len - off);

        String res;
        try {
            res = new String(buffer, "utf-8");
        } catch (Exception ex) {
            System.out.println("Failed displaying file as text");
            return;
        }

        System.out.println("File " + path + "; " + len + " bytes total");
        System.out.print(res);
    }
    
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Please provide a hostname");
            return;
        }
        
        int port = 2030;
        String hostname = args[0];

        try (Socket sock = new Socket(hostname, port);
             DataInputStream input = new DataInputStream(sock.getInputStream());
             DataOutputStream output = new DataOutputStream(sock.getOutputStream())) {
            System.out.println("Connected to " + hostname + ":" + port);
            System.out.println("Use \"list <path>\" for directory listings");
            System.out.println("And \"get <path>\" for file view");
            System.out.println("");

            Scanner sc = new Scanner(System.in);
            while (true) {
                System.out.print(">>> ");
                
                if (!sc.hasNext())
                    break;
                String line = sc.nextLine();
                System.out.println(line);
                
                if (line.startsWith("list "))
                    handleList(line.substring("list ".length()), input, output);
                else if (line.startsWith("get "))
                    handleGet(line.substring("get ".length()), input, output);
                else
                    System.out.println("Invalid command");
            }
        }
    }
}
