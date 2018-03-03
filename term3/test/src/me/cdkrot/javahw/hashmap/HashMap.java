package me.cdkrot.javahw.hashmap;

import java.util.*;

/**
 * HashMap class.
 */
public class HashMap<K, V> implements Map<K, V> {
    private int curSize = 0;
    private final int initialBuckets = 1024;
    private final int growFactor = 2;
    private final int maxRate = 2;
    
    private SimpleList[] buckets;
    
    public HashMap() {
        clear();
    }
    
    /**
     * Removes all elements from hashmap.
     */
    public void clear() {
        curSize = 0;
        buckets = new SimpleList[initialBuckets];
        for (int i = 0; i != buckets.length; ++i)
            buckets[i] = new SimpleList();
    }
    
    /**
     * Get hashmap size
     * @return the size
     */
    public int size() {
        return curSize;
    }

    public boolean isEmpty() {
        return size() != 0;
    }
    
    /**
     * Returns if the hashmap has such element
     * @param key
     * @return boolean, the answer
     */
    public boolean containsKey(Object key) {
        SimpleList.Node<Map.Entry<K, V>> head = getBucket(key).getHead();
        SimpleList.Node<Map.Entry<K, V>> node;
        for (node = head.next(); node != head; node = node.next())
            if (node.elem.getKey().equals(key))
                return true;
        return false;
    }

    public boolean containsValue(Object value) {
        for (int i = 0; i != buckets.length; ++i) {
            SimpleList.Node<Map.Entry<K, V>> head = buckets[i].getHead();
            SimpleList.Node<Map.Entry<K, V>> node;
            for (node = head.next(); node != head; node = node.next())
                if (node.elem.getValue().equals(value))
                    return true;
        }
        
        return false;
    }

    public void putAll(Map<? extends K,? extends V> m) {
        // Set<Map.Entry> entrySet = m.entrySet();
        // Iterator<Map.Entry> iter = entrySet.iterator();
        
        // while (iter.hasNext()) {
        //     Map.Entry<K, V> entry = iter.next();
        //     put(entry.getKey(), entry.getValue());
        // }
    }

    public V remove(Object key) {
        SimpleList lst = getBucket(key);
        SimpleList.Node<Map.Entry<K, V>> head = lst.getHead();
        SimpleList.Node<Map.Entry<K, V>> node;
        for (node = head.next(); node != head; node = node.next())
            if (node.elem.getKey().equals(key)) {
                lst.remove(node);
                return node.elem.getValue();
            }
        return null;
    }

    public V put(K key, V value) {
        SimpleList lst = getBucket(key);
        SimpleList.Node<Map.Entry<K, V>> head = lst.getHead();
        SimpleList.Node<Map.Entry<K, V>> node;
        for (node = head.next(); node != head; node = node.next())
            if (node.elem.getKey().equals(key)) {
                V old = node.elem.getValue();
                node.elem.setValue(value);
                return old;
            }

        lst.insertAfter(head, new AbstractMap.SimpleEntry<K, V>(key, value));
        curSize += 1;
        return null;
    }

    public V get(Object key) {
        SimpleList lst = getBucket(key);
        SimpleList.Node<Map.Entry<K, V>> head = lst.getHead();
        SimpleList.Node<Map.Entry<K, V>> node;
        for (node = head.next(); node != head; node = node.next())
            if (node.elem.getKey().equals(key)) {        
                return node.elem.getValue();
            }

        return null;
    }
    
    public Set<K> keySet() {
        // TODO
    }
    
    public Set<Map.Entry<K,V>> entrySet() {
        return null; // TODO
    }

    public Collection<V> values() {
        return null; // TODO
    }
    
    private SimpleList<Map.Entry<K, V>> getBucket(Object key) {
        return buckets[key.hashCode() & (buckets.length - 1)];
    }
}
