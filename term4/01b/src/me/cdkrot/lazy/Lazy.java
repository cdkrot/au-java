package me.cdkrot.lazy;

/**
 * Represents an abstract defered calculation
 * Calculation should be performed exactly once, at the moment of first access.
 *
 * @param <T> type of the result.
 */
public interface Lazy<T> {
    /**
     * Get the value
     */
    T get();
}
