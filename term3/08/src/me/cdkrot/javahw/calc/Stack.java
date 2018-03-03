package me.cdkrot.javahw.calc;

/**
 * Stack of elements of <T>
 */
interface Stack<T> {
    /**
     * Get size of the stack
     * @return the size
     */
    int size();

    /**
     * Returns whether the stack is empty.
     * @return size() == 0
     */
    boolean empty();

    /**
     * Get top element of stack
     * @return the top element, null if empty().
     */
    T top();

    /**
     * Pops top element out of the stack
     * Does nothing if stack is empty.
     */
    void pop();

    /**
     * Clears the stack
     */
    void clear();
    
    /**
     * Add element to stack
     * @param a, element to add.
     */
    void push(T a);
}
