package me.cdkrot.javahw;

import java.util.HashMap;
import java.io.*;

public class Trie {
    private static class Node implements Serializable {
        public boolean term = false;
        public int subtree = 0;
        
        public Node parent = null;
        public char charParent = 0;
        
        public HashMap<Character, Node> go = new HashMap<Character, Node>();

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
    
    public boolean add(String s) {
        Node cur = root;
        for (int i = 0; i != s.length(); ++i)
            cur = cur.get(s.charAt(i));
        
        if (!cur.term) {
            cur.term = true;
            
            for (; cur != null; cur = cur.parent)
                cur.subtree += 1;
            return true;
        }
        return false;
    }
    
    public boolean containsKey(String s) {
        Node cur = root;
        for (int i = 0; i != s.length(); ++i)
            if (cur.go.containsKey(s.charAt(i)))
                cur = cur.go.get(s.charAt(i));
            else
                return false;
        
        return cur.term;
    }
    
    public boolean remove(String s) {
        Node cur = root;
        for (int i = 0; i != s.length(); ++i)
            if (cur.go.containsKey(s.charAt(i)))
                cur = cur.go.get(s.charAt(i));
            else
                return false;

        if (cur.term) {
            cur.term = false;

            while (cur != null) {
                cur.subtree -= 1;
                Node par = cur.parent;
                
                if (cur.subtree == 0 && par != null)
                    par.go.remove(cur.charParent);

                cur = par;
            }

            return true;
        }

        return false;
    }

    public int size() {
        return root.subtree;
    }

    public int howManyStartsWithPrefix(String s) {
        Node cur = root;
        for (int i = 0; i != s.length(); ++i)
            if (cur.go.containsKey(s.charAt(i)))
                cur = cur.go.get(s.charAt(i));
            else
                return 0;

        return cur.subtree;
    }

    public void serialize(OutputStream stream) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(stream);
        oos.writeObject(root);
        oos.close(); 
    }


    public void deserialize(InputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(in);
        root = (Node) ois.readObject();
        ois.close();
    } 
}
