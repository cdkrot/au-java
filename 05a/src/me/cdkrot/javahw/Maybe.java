package me.cdkrot.javahw;

import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

/**
 * Represents Optional value.
 * @param <T> -- the typeof stored value.
 */
public class Maybe<T> {
    private T value;

    private Maybe(T val) {
        value = val;
    }

    /**
     * Constructs Maybe with value t.
     * @param t -- the value to store
     */
    public static <T> Maybe<T> just(@NotNull T t) {
        return new Maybe<T>(t);
    }

    /**
     * Constructs empty Maybe.
     */
    public static <T> Maybe<T> nothing() {
        return new Maybe<T>(null);
    }

    /**
     * Returns the value in maybe, throws exception on Nothing.
     * @throws NothingException, when this maybe is Nothing.
     * @return the stored value otherways
     */
    public T get() throws EmptyValueException {
        if (value == null)
            throw new EmptyValueException();
        else
            return value;
    }

    /**
     * Returns stored value or default one, if value is not present.
     * @param def -- default value to return.
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
     * @param mapper -- the function to apply
     * @param <U> -- the result type.
     * @return new value.
     */
    public <U> Maybe<U> map(Function<T, U> mapper) {
        if (value == null)
            return new Maybe<U>(null);
        else
            return new Maybe<U>(mapper.apply(value));
    }
}
