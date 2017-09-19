package me.cdkrot.javahw.hashmap;

/**
 * HashMap class, keys and values are Strings
 * @author Dmitry Sayutin
 */
public class HashMap {
    private int curSize = 0;
    private final int initialBuckets = 1024;
    private final int growFactor = 2;
    private final int maxRate = 2;
    
    private HashMapNode[] buckets = new HashMapNode[initialBuckets];
    
    /**
     * Get hashmap size
     * @return the size
     */
    public int size() {
        return curSize;
    }
    
    /**
     * Returns if the hashmap has such element
     * @param key
     * @return boolean, the answer
     */
    public boolean contains(String key) {
        int id = getBucketID(key);
        
        for (HashMapNode node = buckets[id]; node != null; node = node.next)
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
        int id = getBucketID(key);
        
        for (HashMapNode node = buckets[id]; node != null; node = node.next)
            if (node.key.equals(key))
                return node.value;
        return null;
    }
    
    /**
     * Set the key in hashmap
     * @param key
     * @param value
     * @return previous value
     */
    public String put(String key, String value) {
        String res = putImpl(key, value);
        rehash();
        return res;
    }
    
    private String putImpl(String key, String value) {
        int id = getBucketID(key);
        
        HashMapNode prev = null;
        for (HashMapNode node = buckets[id]; node != null; prev = node, node = node.next)
            if (node.key.equals(key)) {
                String res = node.value;
                node.value = value;                
                return res;
            }
        
        curSize += 1;
        
        buckets[id] = new HashMapNode(key, value, buckets[id]);
        
        return null;
    }
    
    /**
     * Removes the key from hashmap
     * @param key
     * @return the previous element
     */
    public String remove(String key) {
        int id = getBucketID(key);
        
        HashMapNode prev = null;
        for (HashMapNode node = buckets[id]; node != null; prev = node, node = node.next)
            if (node.key.equals(key)) {
                // well, in reasonable languages like C(++)
                // there would be only one case
                // but who said, that java is reasonable?

                String res = node.value;
                
                if (prev == null)
                    buckets[id] = node.next;
                else
                    prev.next = node.next;
                
                curSize -= 1;
                return res;
            }
        
        return null;
    }
    
    /**
     * Removes all elements from hashmap.
     */
    public void clear() {
        curSize = 0;
        buckets = new HashMapNode[initialBuckets];
    }
    
    private void rehash() {
        if (curSize <= maxRate * buckets.length)
            return;
        
        HashMapNode[] old = buckets;
        buckets = new HashMapNode[growFactor * old.length];
        
        for (int id = 0; id != old.length; ++id)
            for (HashMapNode node = old[id]; node != null; node = node.next) {
                int nid = getBucketID(node.key);
                buckets[nid] = new HashMapNode(node.key, node.value, buckets[nid]);
            }
    }
    
    private int getBucketID(String s) {
        return s.hashCode() & (buckets.length - 1);
    }

    /**
     * Supporting class for HashMap, represents node of list in HashMap's bucket.
     * @author Sayutin Dmitry
     */
    static private class HashMapNode {
        public HashMapNode() {}
        public HashMapNode(String key, String value, HashMapNode next) {
            this.key   = key;
            this.value = value;
            this.next  = next;
        }
        
        public String key;
        public String value;
        public HashMapNode next;
    }
}
