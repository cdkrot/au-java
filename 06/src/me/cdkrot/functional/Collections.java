package me.cdkrot.functional;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * Provides utilities for applying functions to collections.
 */
public class Collections {
    /**
     * Maps function to each element of the collection.
     * @param f the transforming function
     * @param iter the collection.
     * @param <A> the collection type
     * @param <B> the result type.
     * @return list.
     */
    public static <A, B> List<B> map(Function1<? super A, B> f, Iterable<A> iter) {
        List<B> result = new ArrayList<B>();

        for (A a: iter)
            result.add(f.apply(a));
        return result;
    }

    /**
     * Filters elements from collection based on f
     * @param f filtering function
     * @param iter the collection
     * @param <A> the type of collection.
     * @return the list with elements with f(x) = true.
     */
    public static <A> List<A> filter(Predicate<? super A> f, Iterable<A> iter) {
        List<A> result = new ArrayList<A>();

        for (A a: iter)
            if (f.apply(a))
                result.add(a);
        return result;
    }

    /**
     * Takes largest prefix of collection, on which f returns true.
     * @param f the function
     * @param iter the collection
     * @param <A> the type of collection.
     * @return the prefix.
     */
    public static <A> List<A> takeWhile(Predicate<? super A> f, Iterable<A> iter) {
        List<A> result = new ArrayList<A>();

        for (A a: iter)
            if (!f.apply(a))
                break;
            else
                result.add(a);
        return result;
    }

    /**
     * Takes largest prefix of collection, on which f returns false.
     * @param f the function
     * @param iter the collection
     * @param <A> the type of collection.
     * @return the prefix.
     */
    public static <A> List<A> takeUnless(Predicate<? super A> f, Iterable<A> iter) {
        return takeWhile(f.not(), iter);
    }

    /**
     * Folds the list into one value.
     * @param f the foldering function
     * @param value initial value
     * @param iter the collection
     * @param <A> the collection type
     * @param <B> the result type
     * @return the folding result
     */
    public static <A, B> B foldl(Function2<? super B, ? super A, B> f, B value, Iterable<A> iter) {
        for (A a: iter)
            value = f.apply(value, a);
        return value;
    }

    private static <A, B> B foldrImpl(Function2<? super A, ? super B, B> f, B value, Iterator<A> iter) {
        if (!iter.hasNext())
            return value;

        A a = iter.next();
        return f.apply(a, foldrImpl(f, value, iter));
    }

    /**
     * Folds the list into one value.
     * @param f the foldering function
     * @param value initial value
     * @param iter the collection
     * @param <A> the collection type
     * @param <B> the result type
     * @return the folding result
     */
    public static <A, B> B foldr(Function2<? super A, ? super B, B> f, B value, Iterable<A> iter) {
        return foldrImpl(f, value, iter.iterator());
    }
}

