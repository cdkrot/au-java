package me.cdkrot.javahw;

import java.util.Random;

/**
 * Represents set of <T>.
 */
public class Set <T extends Comparable<? super T>> {
    private Random rnd = new Random(228);
    private Node<T> root = null;
    
    private static class Node<U> {
        U value;
        int prio;
        Node<U> left;
        Node<U> right;
        int size;

        void recalc() {
            size = 1;
            if (left != null)
                size += left.size;
            if (right != null)
                size += right.size;
        }
    }

    private Node<T> makeNode(T value) {
        Node<T> node = new Node<T>();
        node.value = value;
        node.prio = rnd.nextInt();
        node.left = null;
        node.right = null;
        node.size = 1;
        return node;
    }
    
    private static class NodePair<U> {
        NodePair(Node<U> a, Node<U> b) {
            first = a;
            second = b;
        }
        
        Node<U> first;
        Node<U> second;
    }
    
    private static <U> Node<U> merge(Node<U> l, Node<U> r) {
        if (l == null)
            return r;
        if (r == null)
            return l;

        if (l.prio >= r.prio) {
            l.right = merge(l.right, r);
            l.recalc();
            return l;
        } else {
            r.left = merge(l, r.left);
            r.recalc();
            return r;
        }
    }

    /**
     * Split root to (<, >=)
     * @param root, the node to split
     * @param key, by which key.
     * @return Pair of split result
     */
    private static <U extends Comparable<? super U>> NodePair<U> split(Node<U> root, U key) {
        if (root == null)
            return new NodePair<U>(null, null);
        if (root.value.compareTo(key) < 0) {
            NodePair<U> spl = split(root.right, key);
            root.right = spl.first;
            root.recalc();
            
            return new NodePair<U>(root, spl.second);
        } else {
            NodePair<U> spl = split(root.left, key);
            root.left = spl.second;
            root.recalc();
            
            return new NodePair<U>(spl.first, root);
        }
    }

    private static <U> Node<U> getLeft(Node<U> root) {
        while (root.left != null)
            root = root.left;
        return root;
    }

    private static <U> Node<U> getRight(Node<U> root) {
        while (root.right != null)
            root = root.right;
        return root;
    }

    /**
     * Adds element to Set
     * @param value, value to add
     * @return boolean, if added
     */
    public boolean add(T value) {
        NodePair<T> spl = split(root, value);

        if (spl.second != null && getLeft(spl.second).value.compareTo(value) == 0) {
            root = merge(spl.first, spl.second);
            return false;
        }

        root = merge(spl.first, merge(makeNode(value), spl.second));
        return true;
    }

    /**
     * Returns whether Set contains Value
     * @param value to check
     * @return boolean indicating if set contains element or not
     */
    public boolean contains(T value) {
        Node<T> cur = root;
        while (cur != null) {
            int cmp = cur.value.compareTo(value);

            if (cmp == 0)
                return true;
            else if (cmp < 0)
                cur = cur.right;
            else
                cur = cur.left;
        }
        return false;
    }

    /**
     * Returns size
     * @return size, the size
     */
    public int size() {
        if (root == null)
            return 0;
        return root.size;
    }
}
