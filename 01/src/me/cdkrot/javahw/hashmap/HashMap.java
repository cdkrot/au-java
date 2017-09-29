package me.cdkrot.javahw.hashmap;

/**
 * HashMap class, keys and values are Strings
 */
public class HashMap {
    private int curSize = 0;
    private final int initialBuckets = 1024;
    private final int growFactor = 2;
    private final int maxRate = 2;
    
    private HashMapList[] buckets;

    public HashMap() {
        clear();
    }
    
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
        return buckets[getBucketID(key)].contains(key);
    }
    
    /**
     * Get the value by key
     * @param key
     * @return value, or null if no such key.
     */
    public String get(String key) {
        return buckets[getBucketID(key)].get(key);
    }
    
    /**
     * Set the key in hashmap
     * @param key
     * @param value
     * @return previous value
     */
    public String put(String key, String value) {
        int id = getBucketID(key);
        
        curSize -= buckets[id].size();
        String res = buckets[getBucketID(key)].put(key, value);
        curSize += buckets[id].size();

        rehash();
        return res;
    }
    
    
    /**
     * Removes the key from hashmap
     * @param key
     * @return the previous element
     */
    public String remove(String key) {
        int id = getBucketID(key);
        
        curSize -= buckets[id].size();
        String res = buckets[getBucketID(key)].remove(key);
        curSize += buckets[id].size();

        rehash();
        return res;
    }
    
    /**
     * Removes all elements from hashmap.
     */
    public void clear() {
        curSize = 0;
        buckets = new HashMapList[initialBuckets];
        for (int i = 0; i != buckets.length; ++i)
            buckets[i] = new HashMapList();
    }
    
    private void rehash() {
        if (curSize <= maxRate * buckets.length)
            return;
        
        HashMapList[] old = buckets;
        buckets = new HashMapList[growFactor * old.length];
        for (int i = 0; i != buckets.length; ++i)
            buckets[i] = new HashMapList();
        
        for (int id = 0; id != old.length; ++id)
            while (old[id].size() != 0) {
                String key = old[id].getHeadKey();
                String val = old[id].getHeadValue();
                old[id].deleteHead();

                int nid = getBucketID(key);
                buckets[nid].put(key, val);
            }
    }
    
    private int getBucketID(String s) {
        return s.hashCode() & (buckets.length - 1);
    }
}
