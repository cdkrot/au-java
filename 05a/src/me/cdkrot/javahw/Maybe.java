package me.cdkrot.javahw;

import java.util.function.Function;

/**
 * Represents Optional value.
 */
public class Maybe<T> {
    private T value;

    private Maybe(T val) {
        value = val;
    }

    /**
     * Constructs Maybe with value t.
     */
    public static <T> Maybe<T> just(T t) {
        return new Maybe(t);
    }

    /**
     * Constructs empty Maybe.
     */
    public static <T> Maybe<T> nothing() {
        return new Maybe(null);
    }

    /**
     * Returns the value in maybe, throws exception on Nothing.
     * @return the value
     */
    public T get() throws NothingException {
        if (value == null)
            throw new NothingException();
        else
            return value;
    }

    /**
     * Returns stored value or default one, if value is not present.
     * @return the value
     */
    public T getWithDefault(T def) {
        if (value == null)
            return def;
        else
            return value;
    }

    /**
     * Returns is the value present
     * @return is empty or not
     */
    public boolean isPresent() {
        return value != null;
    }

    /**
     * Maps the value in maybe by mapper.
     * Nothing maps to Nothing
     * @return new value.
     */
    public <U> Maybe<U> map(Function<T, U> mapper) {
        if (value == null)
            return new Maybe<U>(null);
        else
            return new Maybe<U>(mapper.apply(value));
    }
}
