package me.cdkrot.javahw;

import java.io.*;

public class Main {
    public static Maybe<Integer> parseString(String s) {
        try {
            return Maybe.just(Integer.parseInt(s));            
        } catch (NumberFormatException ex) {
            return Maybe.nothing();
        }
    }
    
    /**
     * Processes file as described in task
     * @param in, input file
     * @param out, output file
     */
    public static void processFile(String in, String out) throws IOException {
        try (BufferedReader fin  = new BufferedReader(new FileReader(in));
             BufferedWriter fout = new BufferedWriter(new FileWriter(out))) {

            String s;
            while ((s = fin.readLine()) != null) {
                Maybe<Integer> integral    = parseString(s);
                Maybe<Integer> transformed = integral.map((x) -> x * x);
                Maybe<String>  result      = transformed.map(x -> Integer.toString(x));
                
                fout.write(result.getWithDefault("null") + "\n");
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Error: not enough arguments");
        }

        processFile(args[0], args[1]);
    }
};
