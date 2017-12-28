package me.cdkrot.javahw.calc;

/**
 * Implementation of stack of <T>.
 */
public class SimpleStack<T> implements Stack<T> {
    /**
     * Get size of the stack
     * @return the size
     */
    public int size() {
        return curSize;
    }

    /**
     * Returns whether the stack is empty.
     * @return size() == 0
     */
    public boolean empty() {
        return size() == 0;
    }

    /**
     * Get top element of stack
     * @return the top element, null if empty().
     */
    public T top() {
        if (head == null)
            throw new RuntimeException("empty stack");
        return head.elem;
    }

    /**
     * Pops top element out of the stack
     * Does nothing if stack is empty.
     */
    public void pop() {
        if (head == null)
            throw new RuntimeException("empty stack");
        
        head = head.next;
        curSize -= 1;
    }

    /**
     * Clears the stack
     */
    public void clear() {
        while (!empty())
            pop();
    }
    
    /**
     * Add element to stack
     * @param a, element to add.
     */
    public void push(T a) {
        head = new Node<T>(a, head);
        curSize += 1;
    }

    private Node<T> head = null;
    private int curSize = 0;
    
    private static class Node<U> {
        private Node(U elm, Node nxt) {
            elem = elm;
            next = nxt;
        }
        
        private U elem;
        private Node next;
    };
}
