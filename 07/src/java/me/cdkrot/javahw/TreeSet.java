package me.cdkrot.javahw;

import java.util.*;

/**
 * Represents set of <T>.
 */
public class TreeSet<T> implements MyTreeSet<T> {
    private Random rnd = new Random(228);
    private Node<T> root = null;

    private Comparator<? super T> cmp = null;

    public TreeSet() {
        cmp = new Comparator<T>() {
                public int compare(T a, T other) {
                    return ((Comparable)a).compareTo(other);
                }
            };
    }

    public TreeSet(Comparator<? super T> cmp) {
        this.cmp = cmp;
    }
    
    private static class Node<U> {
        U value;
        int prio;
        Node<U> left;
        Node<U> right;
        int size;

        Node<U> prev;
        Node<U> next;
        
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
        node.prev = null;
        node.next = null;
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

    private class ForwardIterator implements Iterator<T> {
        private ForwardIterator() {
            ptr = getLeft(root);
        }
        
        private Node<T> ptr;
        private Node<T> last;
        
        public boolean hasNext() {
            return ptr != null;
        }

        public T next() {
            last = ptr;
            T res = ptr.value;
            ptr = ptr.next;
            return res;
        }

        public void remove() {
            TreeSet.this.remove(last.value);
        }
    };

    private class BackwardIterator implements Iterator<T> {
        private BackwardIterator() {
            ptr = getRight(root);
        }
        
        private Node<T> ptr;
        private Node<T> last;
        
        public boolean hasNext() {
            return ptr != null;
        }

        public T next() {
            last = ptr;
            T res = ptr.value;
            ptr = ptr.prev;
            return res;
        }

        public void remove() {
            TreeSet.this.remove(last.value);
        }
    };

    private class DescendingSet implements MyTreeSet<T> {
        public Iterator<T> iterator() {
            return TreeSet.this.descendingIterator();
        }
        
        public Iterator<T> descendingIterator() {
            return TreeSet.this.iterator();
        }

        public MyTreeSet<T> descendingSet() {
            return TreeSet.this;
        }

        public T first() {
            return TreeSet.this.last();
        }

        public T last() {
            return TreeSet.this.first();
        }

        public void clear() {
            TreeSet.this.clear();
        }
        
        public T lower(T elem) {
            return TreeSet.this.higher(elem);
        }

        public T floor(T elem) {
            return TreeSet.this.ceiling(elem);
        }

        public T ceiling(T elem) {
            return TreeSet.this.floor(elem);
        }

        public T higher(T elem) {
            return TreeSet.this.lower(elem);
        }

        public int size() {
            return TreeSet.this.size();
        }

        public boolean isEmpty() {
            return TreeSet.this.isEmpty();
        }
        
        public boolean add(T o) {
            return TreeSet.this.add(o);
        }

        public boolean remove(Object o) {
            return TreeSet.this.remove(o);
        }
        
        public boolean contains(Object o) {
            return TreeSet.this.contains(o);
        }
        
        public boolean removeAll(Collection<?> c) {
            return TreeSet.this.removeAll(c);
        }
    
        public boolean retainAll(Collection<?> c) {
            return TreeSet.this.retainAll(c);
        }

        public boolean addAll(Collection<? extends T> c) {
            return TreeSet.this.addAll(c);
        }

        public boolean containsAll(Collection<?> c) {
            return TreeSet.this.containsAll(c);
        }

        public Object[] toArray() {
            return TreeSet.this.toArray();
        }
    
        public <U> U[] toArray(U[] a) {
            return TreeSet.this.toArray(a);
        }
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
    private static <U> NodePair<U> split(Node<U> root, U key, Comparator<? super U> cmp) {
        if (root == null)
            return new NodePair<U>(null, null);
        if (cmp.compare(root.value, key) < 0) {
            NodePair<U> spl = split(root.right, key, cmp);
            root.right = spl.first;
            root.recalc();
            
            return new NodePair<U>(root, spl.second);
        } else {
            NodePair<U> spl = split(root.left, key, cmp);
            root.left = spl.second;
            root.recalc();
            
            return new NodePair<U>(spl.first, root);
        }
    }

    private static <U> Node<U> getLeft(Node<U> root) {
        if (root == null)
            return null;
        
        while (root.left != null)
            root = root.left;
        return root;
    }

    private static <U> Node<U> getRight(Node<U> root) {
        if (root == null)
            return null;
        
        while (root.right != null)
            root = root.right;
        return root;
    }

    private static <U> void connect(Node<U> a, Node<U> b) {
        if (a == null || b == null)
            return;
        a.next = b;
        b.prev = a;
    }
    
    /**
     * Adds element to Set
     * @param value, value to add
     * @return boolean, if added
     */
    public boolean add(T value) {
        NodePair<T> spl = split(root, value, cmp);

        if (spl.second != null && cmp.compare(getLeft(spl.second).value, value) == 0) {
            root = merge(spl.first, spl.second);
            return false;
        }

        Node<T> nw = makeNode(value);
        connect(getRight(spl.first), nw);
        connect(nw, getLeft(spl.second));
        root = merge(spl.first, merge(nw, spl.second));
        return true;
    }

    private Node<T> deleteFirst(Node<T> a) {
        if (a.left == null)
            return a.right;

        a.left = deleteFirst(a.left);
        a.recalc();
        return a;
    }
    
    /**
     * Deletes element from Set
     * @param value, value to delete
     * @return boolean, if deleted
     */
    public boolean remove(Object v) {
        T value = (T) v;
        NodePair<T> spl = split(root, value, cmp);

        if (spl.second == null || cmp.compare(getLeft(spl.second).value, value) != 0) {
            root = merge(spl.first, spl.second); // nothing to do.
            return false;
        }

        Node<T> pdel = getLeft(spl.second);
        spl.second = deleteFirst(spl.second);

        if (pdel.next != null)
            pdel.next.prev = null;

        if (pdel.prev != null)
            pdel.prev.next = null;

        if (pdel.next != null && pdel.prev != null)
            connect(pdel.prev, pdel.next);

        root = merge(spl.first, spl.second);
        return true;
    }
    
    /**
     * Returns whether Set contains Value
     * @param value to check
     * @return boolean indicating if set contains element or not
     */
    public boolean contains(Object v) {
        T value = (T)v;
        Node<T> cur = root;
        while (cur != null) {
            int cr = cmp.compare(cur.value, value);

            if (cr == 0)
                return true;
            else if (cr < 0)
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

    public boolean isEmpty() {
        return size() == 0;
    }
    
    public void clear() {
        root = null;
    }
    
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("not implemented");
    }
    
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("not implemented");
    }

    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException("not implemented");
    }

    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("not implemented");
    }

    public Object[] toArray() {
        throw new UnsupportedOperationException("not implemented");
    }
    
    public <U> U[] toArray(U[] a) {
        throw new UnsupportedOperationException("not implemented");
    }
    
    public T first() {
        if (root == null)
            return null;
        return getLeft(root).value;
    }

    public T last() {
        if (root == null)
            return null;
        return getRight(root).value;
    }

    private Node<T> lowerBound(Node<T> nd, T elem) {
        if (nd == null)
            return null;

        if (cmp.compare(nd.value, elem) < 0)
            return lowerBound(nd.right, elem);
        else if (cmp.compare(nd.value, elem) == 0)
            return nd;
        else {
            Node<T> tmp;
            if ((tmp = lowerBound(nd.left, elem)) != null)
                return tmp;
            return nd;
        }
    }

    private Node<T> upperBound(Node<T> nd, T elem) {
        if (nd == null)
            return null;

        if (cmp.compare(nd.value, elem) <= 0)
            return upperBound(nd.right, elem);
        else {
            Node<T> tmp;
            if ((tmp = upperBound(nd.left, elem)) != null)
                return tmp;
            return nd;
        }
    }
    
    public T lower(T elem) {
        Node<T> nd = lowerBound(root, elem);
        if (nd == null)
            return getRight(root).value;
        else if (nd.prev == null)
            return null;
        else
            return nd.prev.value;
    }

    public T floor(T elem) {
        Node<T> nd = upperBound(root, elem);
        if (nd == null)
            return getRight(root).value;
        else if (nd.prev == null)
            return null;
        else
            return nd.prev.value;
    }

    public T ceiling(T elem) {
        Node<T> nd = lowerBound(root, elem);
        if (nd != null)
            return nd.value;
        return null;
    }

    public T higher(T elem) {
        Node<T> nd = upperBound(root, elem);
        if (nd != null)
            return nd.value;
        return null;
    }

    public Iterator<T> iterator() {
        return new ForwardIterator();
    }
    
    public Iterator<T> descendingIterator() {
        return new BackwardIterator();
    }
    
    public MyTreeSet<T> descendingSet() {
        return new DescendingSet();
    }
}
