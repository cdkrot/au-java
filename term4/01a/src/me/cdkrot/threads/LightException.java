package me.cdkrot.threads;

/**
 * Represents caught exception, throwed by some Supplier.
 */
public class LightException extends Exception {
    /**
     * Constructs new light exception
     * @param string reason of exception
     * @param throwable from supplier
     */
    protected LightException(String message, Throwable cause) {
        super(message, cause);
    }
}
