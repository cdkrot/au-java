package me.cdkrot.functional;

/**
 * Represents a predicate, that is function from A to Boolean.
 * @param <A> the type this predicate operates on.
 */
public abstract class Predicate<A> extends Function1<A, Boolean> {
    /**
     * Predicate, which always returns true.
     */
    public final static Predicate<Object> ALWAYS_TRUE = new Predicate<Object>() {
        public Boolean apply(Object o) {
            return true;
        }
    };

    /**
     * Predicate, which always returns false.
     */
    public final static Predicate<Object> ALWAYS_FALSE = new Predicate<Object>() {
        public Boolean apply(Object o) {
            return false;
        }
    };

    /**
     * Inverses the current predicate.
     * @return the new predicate.
     */
    public Predicate<A> not() {
        return new Predicate<A>() {
            public Boolean apply(A a) {
                return !Predicate.this.apply(a);
            }
        };
    }

    /**
     * Combines *this with new predicate, returning a "or" predicate of this two.
     * @param other predicate.
     * @return new predicate
     */
    public Predicate<A> or(Predicate<A> other) {
        return new Predicate<A>() {
            public Boolean apply(A a) {
                return Predicate.this.apply(a) || other.apply(a);
            }
        };
    }

    /**
     * Combines *this with new predicate, returning a "and" predicate of this two.
     * @param other predicate.
     * @return new predicate.
     */
    public Predicate<A> and(Predicate<A> other) {
        return new Predicate<A>() {
            public Boolean apply(A a) {
                return Predicate.this.apply(a) && other.apply(a);
            }
        };
    }
}
