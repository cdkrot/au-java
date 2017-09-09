package me.cdkrot.javahw.hashmap;

/**
 * HashMap class, keys and values are Strings
 * @author Dmitry Sayutin
 */
public class HashMap {
    private int curSize = 0;
    private HashMapNode[] buckets = new HashMapNode[1024];
    
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
        return get(key) != null;
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
     * @param value, null to delete the element
     * @return previous value, if any, null otherwise.
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
                if (value != null) {
                    node.value = value;
                } else {
                    // well, in reasonable languages like C(++)
                    // there would be only one case
                    // but who said, that java is reasonable?
                    
                    if (prev == null)
                        buckets[id] = node.next;
                    else
                        prev.next = node.next;
                    
                    curSize -= 1;
                }
                
                return res;
            }
        
        if (value != null) {
            curSize += 1;
            
            HashMapNode node = new HashMapNode();
            node.key = key;
            node.value = value;
            node.next = buckets[id];
            buckets[id] = node;
        }
        
        return null;
    }
    
    /**
     * Removes the key from hashmap
     * Efficiently same as put(key, null)
     * @param key
     * @return the previous element
     */
    public String remove(String key) {
        return put(key, null);
    }
    
    /**
     * Removes all elements from hashmap.
     */
    public void clear() {
        curSize = 0;
        buckets = new HashMapNode[1024];
    }
    
    private void rehash() {
        if (4 * curSize <= buckets.length)
            return;
        
        HashMapNode[] old = buckets;
        buckets = new HashMapNode[2 * old.length];
        
        for (int id = 0; id != old.length; ++id)
            for (HashMapNode node = old[id]; node != null; node = node.next) {
                int nid = getBucketID(node.key);
                HashMapNode newnode = new HashMapNode();
                newnode.key   = node.key;
                newnode.value = node.value;
                newnode.next = buckets[nid];
                buckets[nid] = newnode;
            }
    }
    
    private int getBucketID(String s) {
        return s.hashCode() & (buckets.length - 1);
    }
}
