package me.cdkrot.threads;

/**
 * Represents caught exception, throwed by some Supplier.
 */
public class LightException extends RuntimeException {
    private Exception exception;

    /**
     * Constructs new light exception
     * @param exception from supplier
     */
    protected LightException(Exception exception) {
        this.exception = exception;
    }

    /**
     * Returns the exception
     * @return exception
     */
    public Exception get() {
        return exception;
    }
}
