package me.cdkrot.javahw;

import java.util.*;

/**
 * Implements smart list with special handling of small cases
 */
public class SmartList<E> extends AbstractList<E> implements List<E> {
    private int curSize = 0;
    private Object pdata = null;

    private int sizeFactor = 5;
    
    /**
     * Creates new list of zero elements
     */
    public SmartList() {}

    /**
     * Copies all elements from the collection.
     */
    public SmartList(Collection<E> col) {
        for (E elem: col)
            add(elem);
    }

    /**
     * Get current number of elements in list
     * @return size
     */
    @Override
    public int size() {
        return curSize;
    }

    /**
     * Get element by index
     * @param index
     * @return element
     * @throw IndexOutOfBoundsException on bad accesses
     */
    @SuppressWarnings("unchecked")
    @Override
    public E get(int index) {
        if (index < 0 || index >= curSize)
            throw new IndexOutOfBoundsException();

        if (curSize == 1)
            return (E)pdata;
        else if (curSize <= sizeFactor) {
            Object[] arr = (Object[])pdata;
            return (E)(arr[index]);
        } else {
            ArrayList<E> lst = (ArrayList<E>)pdata;
            return lst.get(index);
        }
    }

    /**
     * Set element by index
     * @param index
     * @param new value
     * @return old value
     * @throw IndexOutOfBoundsException on bad accesses
     */
    @SuppressWarnings("unchecked")
    @Override
    public E set(int index, E element) {
        // get function also checks for
        // index errors.
        E old = get(index);
        
        if (curSize == 1) {
            pdata = element;
        } else if (curSize <= sizeFactor) {
            Object[] arr = (Object[])pdata;
            arr[index] = element;
        } else {
            ArrayList<E> lst = (ArrayList<E>)pdata;
            lst.set(index, element);
        }
        
        return old;
    }

    @SuppressWarnings("unchecked")
    private void extend() {
        if (curSize == 0) {
            // nothing to do
        } else if (curSize == 1) {
            Object[] newstore = new Object[sizeFactor];
            newstore[0] = pdata;
            pdata = newstore;
        } else if (curSize == sizeFactor) {
            Object[] cur = (Object[])pdata;
            ArrayList<E> lst = new ArrayList<E>();
            
            for (int i = 0; i != curSize; ++i)
                lst.add((E)cur[i]);

            lst.add(null);
            pdata = lst;
        } else if (curSize >= sizeFactor + 1) {
            ArrayList<E> lst = (ArrayList<E>)pdata;
            lst.add(null);
        }

        curSize += 1;
        modCount += 1;
    }

    private void shrink() {
        if (curSize == 0) {
            // don't do anything.
        } else if (curSize == 1) {
            pdata = null;
        } else if (curSize == 2) {
            Object[] lst = (Object[])pdata;
            pdata = lst[0];
        } else if (3 <= curSize && curSize <= sizeFactor) {
            Object[] lst = (Object[])pdata;
            lst[curSize - 1] = null;
        } else if (curSize == sizeFactor + 1) {
            Object[] newstore = new Object[sizeFactor];
            ArrayList<E> lst = (ArrayList<E>)pdata;
            
            for (int i = 0; i != sizeFactor; ++i)
                newstore[i] = lst.get(i);

            pdata = newstore;
        } else {
            ArrayList<E> lst = (ArrayList<E>)pdata;
            lst.remove(curSize - 1);
        }

        curSize -= 1;
        modCount += 1;
    }

    /**
     * Adds element before the index
     * @param index
     * @param value to add
     * @throw IndexOutOfBoundsException on bad accesses
     */
    @Override
    public void add(int index, E element) {
        if (index < 0 || index > curSize)
            throw new IndexOutOfBoundsException();
        
        extend();
        for (int j = curSize - 1; j > index; --j)
            set(j, get(j - 1));

        set(index, element);
    }

    /**
     * Removes the element by index
     * @param index
     * @throw IndexOutOfBoundsException on bad accesses
     */    
    @Override
    public E remove(int index) {
        if (index < 0 || index >= curSize)
            throw new IndexOutOfBoundsException();
        
        E result = get(index);
        for (int j = index; j != curSize - 1; ++j)
            set(j, get(j + 1));

        shrink();
        return result;
    }
}
