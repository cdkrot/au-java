package me.cdkrot.javahw.hashmap;

/**
 * Linked list with interface of a map from string to string.
 */
public class HashMapList {
    private HashMapListNode head = null;
    private int curSize = 0;
    
    /**
     * Get hashmap size
     * @return the size
     */
    public int size() {
        return curSize;
    }
    
    /**
     * Returns if the list has such element
     * @param key
     * @return boolean, the answer
     */
    public boolean contains(String key) {
        for (HashMapListNode node = head; node != null; node = node.next)
            if (node.key.equals(key))
                return true;
        return false;
    }
    
    /**
     * Get the value by key
     * @param key
     * @return value, or null if no such key.
     */
    public String get(String key) {
        for (HashMapListNode node = head; node != null; node = node.next)
            if (node.key.equals(key))
                return node.value;
        return null;
    }
    
    /**
     * Set the key with value.
     * @param key
     * @param value
     * @return previous value
     */
    public String put(String key, String value) {
        HashMapListNode prev = null;
        for (HashMapListNode node = head; node != null; prev = node, node = node.next)
            if (node.key.equals(key)) {
                String res = node.value;
                node.value = value;
                return res;
            }

        curSize += 1;
        head = new HashMapListNode(key, value, head);

        return null;
    }
    
    /**
     * Removes the key.
     * @param key
     * @return stored value, if any.
     */
    public String remove(String key) {
        HashMapListNode prev = null;
        for (HashMapListNode node = head; node != null; prev = node, node = node.next)
            if (node.key.equals(key)) {
                // well, in reasonable languages like C(++)
                // there would be only one case
                // but who said, that java is reasonable?

                String res = node.value;
                
                if (prev == null)
                    head = node.next;
                else
                    prev.next = node.next;
                
                curSize -= 1;
                return res;
            }
        
        return null;
    }
    
    /**
     * Removes all elements from list.
     */
    public void clear() {
        head = null;
        curSize = 0;
    }

    /**
     * Returns key of first element in this list
     * @return the key
     */
    public String getHeadKey() {
        return head.key;
    }

    /**
     * Returns value of first element in this list
     * @return the value
     */
    public String getHeadValue() {
        return head.value;
    }


    /**
     * Deletes the first element from list, if any.
     */
    public void deleteHead() {
        if (head != null) {
            head = head.next;
            curSize -= 1;
        }
    }    
    
    /**
     * Represents node of a list.
     */
    static private class HashMapListNode {
        public HashMapListNode() {}
        public HashMapListNode(String key, String value, HashMapListNode next) {
            this.key   = key;
            this.value = value;
            this.next  = next;
        }
        
        public String key;
        public String value;
        public HashMapListNode next;
    }
}
