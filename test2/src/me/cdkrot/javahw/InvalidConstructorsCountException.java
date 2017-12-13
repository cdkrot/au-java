package me.cdkrot.javahw;

/**
 * Represents an excpetion for Injector when class has either zero or two+ constructors.
 */
public class InvalidConstructorsCountException extends InjectorException {
    public InvalidConstructorsCountException() {
        super();
    }

    public InvalidConstructorsCountException(String msg) {
        super(msg);
    }
}
