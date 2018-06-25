package me.cdkrot.javahw;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Scanner;

public class Client {
    /**
     * lists the directory and returns the result as array of entries
     * @param path to list
     * @param input stream to read from
     * @param output stream to write to
     * @return list of entries in dir, or null, if it doesn't exist
     * @throws RuntimeException if the response is invalid.
     */
    public static FileEntry[] listDirectory(String path, DataInputStream input, DataOutputStream output) throws IOException {
        output.writeInt(1);
        output.writeUTF(path);

        int cnt = input.readInt();
        if (cnt == -1) {
            return null;
        }

        if (cnt < 0 || cnt > 1000)
            throw new RuntimeException("Illegal responce");

        FileEntry res[] = new FileEntry[cnt];
        for (int i = 0; i != cnt; ++i) {
            res[i] = new FileEntry();
            res[i].path  = input.readUTF();
            res[i].isDir = input.readBoolean();
        }
        return res;
    }

    /**
     * Lists the directory and prints it's content to stdout
     * @param path directory to list
     * @param input stream to read from
     * @param output stream to write to
     */
    public static void cliList(String path, DataInputStream input, DataOutputStream output) throws IOException {
        FileEntry[] list = listDirectory(path, input, output);
        if (list == null) {
            System.out.println("Directory " + path + " doesn't exist");
            return;
        }

        System.out.println("Directory listing for " + path);
        System.out.println("total " + list.length);
        for (FileEntry entry: list)
            System.out.println((entry.isDir ? "d " : "f ") + entry.path);
    }

    /**
     * gets the requested file
     * @param path to fetch
     * @param input stream to read from
     * @param output stream to write to
     * @return file as the byte array, null if it doesn't exist
     */    
    public static byte[] getFile(String path, DataInputStream input, DataOutputStream output) throws IOException {
        output.writeInt(2);
        output.writeUTF(path);

        int len = input.readInt();
        if (len == -1)
            return null; // file doesn't exist.
        
        if (len < 0 || len > 10240)
            throw new RuntimeException("Illegal responce");

        byte[] buffer = new byte[len];
        int off = 0;
        while (off != len)
            off += input.read(buffer, off, len - off);

        return buffer;
    }

    /**
     * gets the file and prints the result to stdout
     * @param path to fetch
     * @param input stream to read from
     * @param output stream to write to
     */
    public static void cliGet(String path, DataInputStream input, DataOutputStream output) throws IOException {
        byte[] res = getFile(path, input, output);

        if (res == null) {
            System.out.println("File " + path + " doesn't exist");
            return;
        }
        
        String s;
        try {
            s = new String(res, "utf-8");
        } catch (Exception ex) {
            System.out.println("Failed displaying file as text");
            return;
        }

        System.out.println("File " + path + "; " + res.length + " bytes total");
        System.out.print(s);
    }

    /**
     * Convenience function, same result as listDirectory with Data-streams
     * @param path to list
     * @param input stream to read from
     * @param output stream to write to
     * @return list of entries in directory, or null if it doesn't exist
     */
    public static FileEntry[] listDirectory(String path, InputStream input, OutputStream output) throws IOException {
        try (DataInputStream dataIN   = new DataInputStream(input);
             DataOutputStream dataOUT = new DataOutputStream(output)) {
            return listDirectory(path, dataIN, dataOUT);
        }
    }

    /**
     * Convenience function, same result as getFile with Data-streams
     * @param path to fetch
     * @param input stream to read from
     * @param output stream to write to
     * @return file data as byte array, null if it doesn't exist
     */
    public static byte[] getFile(String path, InputStream input, OutputStream output) throws IOException {
        try (DataInputStream dataIN   = new DataInputStream(input);
             DataOutputStream dataOUT = new DataOutputStream(output)) {
            return getFile(path, dataIN, dataOUT);
        }
    }

    /**
     * Entry function for client
     * @param args cli args
     */
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
                
                if (line.startsWith("list "))
                    cliList(line.substring("list ".length()), input, output);
                else if (line.startsWith("get "))
                    cliGet(line.substring("get ".length()), input, output);
                else
                    System.out.println("Invalid command");
            }
        }
    }
}
