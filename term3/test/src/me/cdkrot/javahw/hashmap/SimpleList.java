package me.cdkrot.javahw.hashmap;

/**
 * Simple doubly-linked circle list
 */
public class SimpleList<E> {
    private Node<E> root;
    private int curSize;

    public SimpleList() {
        clear();
    }
    
    /**
     * Get size
     * @return the size
     */
    public int size() {
        return curSize;
    }

    /**
     * Removes all elements from list.
     */
    public void clear() {
        root = new Node<E>();
        root.prev = root.next = root;
        curSize = 0;
    }
    
    /**
     * Return head
     */
    public Node<E> getHead() {
        return root;
    }

    private void linkNodes(Node<E> a, Node<E> b) {
        a.next = b;
        b.prev = a;
    }
    
    /**
     * Insert element after specified node.
     */
    public void insertAfter(Node<E> node, E elem) {
        Node<E> nw = new Node<E>(elem, null, null);
        linkNodes(nw, node.next);
        linkNodes(node, nw);
        curSize += 1;
    }
        
    /**
     * Removes node.
     */
    public void remove(Node<E> node) {
        if (node == root)
            throw new IllegalArgumentException("Can't remove root");

        linkNodes(node.prev, node.next);
        curSize -= 1;
    }
        
    /**
     * Represents node of a list.
     */
    static public class Node<E> {
        protected Node() {}
        protected Node(E elem, Node<E> prev, Node<E> next) {
            this.elem = elem;
            this.prev = prev;
            this.next = next;
        }
        
        public E elem;
        protected Node<E> prev;
        protected Node<E> next;

        public Node<E> next() {
            return next;
        }

        public Node<E> prev() {
            return prev;
        }
    }
}
