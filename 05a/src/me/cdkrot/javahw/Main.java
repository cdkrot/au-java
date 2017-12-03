package me.cdkrot.javahw;

import java.io.*;

/**
 * Main class of a program
 */
public class Main {
    /**
     * Attempts to parse string as Integer
     * @param String to parse
     * @return Just int, on success, Nothing on fail.
     */
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

    /**
     * Entry point
     * @param args, command line args
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Error: not enough arguments");
        }

        processFile(args[0], args[1]);
    }
};
