package me.cdkrot.javahw;

import java.util.HashMap;
import java.io.*;

/**
 * Implements the Trie data structure
 */
public class Trie {
    private static class Node implements Serializable {
        boolean isTerm = false;
        int subtreeSize = 0;
        
        Node parent = null;
        char charParent = 0;
        
        HashMap<Character, Node> go = new HashMap<Character, Node>();

        public Node get(char ch) {
            if (!go.containsKey(ch)) {
                Node node = new Node();
                node.parent = this;
                node.charParent = ch;
                go.put(ch, node);
            }
            
            return go.get(ch);
        }
    };

    private Node root = new Node();

    /**
     * Adds string to trie, if it it wasn't in it.
     *
     * @param s, string to add
     * @return true, if it didn't existed
     */
    public boolean add(String s) {
        Node cur = root;
        for (int i = 0; i != s.length(); ++i)
            cur = cur.get(s.charAt(i));
        
        if (!cur.isTerm) {
            cur.isTerm = true;
            
            for (; cur != null; cur = cur.parent)
                cur.subtreeSize += 1;
            return true;
        }
        return false;
    }

    /**
     * Checks if the string is in Trie
     * @param s, string to check
     * @return true, if s is contained in Trie.
     */
    public boolean containsKey(String s) {
        Node cur = root;
        for (int i = 0; i != s.length(); ++i)
            if (cur.go.containsKey(s.charAt(i)))
                cur = cur.go.get(s.charAt(i));
            else
                return false;
        
        return cur.isTerm;
    }

    /**
     * Removes string from trie, if it exists.
     * @param s, string to remove
     * @return true, if it existed.
     */
    public boolean remove(String s) {
        Node cur = root;
        for (int i = 0; i != s.length(); ++i)
            if (cur.go.containsKey(s.charAt(i)))
                cur = cur.go.get(s.charAt(i));
            else
                return false;

        if (cur.isTerm) {
            cur.isTerm = false;

            while (cur != null) {
                cur.subtreeSize -= 1;
                Node par = cur.parent;
                
                if (cur.subtreeSize == 0 && par != null)
                    par.go.remove(cur.charParent);

                cur = par;
            }

            return true;
        }

        return false;
    }

    /**
     * Returns number of stored strings
     * @return the number
     */
    public int size() {
        return root.subtreeSize;
    }

    /**
     * Counts number of string with given prefix
     * @param s, the prefix
     * @return the number
     */
    public int howManyStartsWithPrefix(String s) {
        Node cur = root;
        for (int i = 0; i != s.length(); ++i)
            if (cur.go.containsKey(s.charAt(i)))
                cur = cur.go.get(s.charAt(i));
            else
                return 0;

        return cur.subtreeSize;
    }

    /**
     * Writes Trie to stream
     * @param stream, to write to.
     * @throws IOException, when failed to write.
     */
    public void serialize(OutputStream stream) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(stream);
        oos.writeObject(root);
        oos.close(); 
    }

    /**
     * Reads Trie from stream
     * @param stream, to read from.
     * @throws IOException, when fails to read.
     * @throws ClassNotFoundException, when data is not valid.
     */
    public void deserialize(InputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(in);
        root = (Node) ois.readObject();
        ois.close();
    } 
}
