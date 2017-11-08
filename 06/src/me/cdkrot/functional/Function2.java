package me.cdkrot.functional;

/**
 * Represents function with two arguments
 * @param <A> type of first argument
 * @param <B> type of second argument
 * @param <R> type of result
 */
public abstract class Function2<A, B, R> {
    /**
     * Returns the result of application of this function to it's arguments.
     * @param a -- first argument
     * @param b -- second argument
     * @return function result.
     */
    public abstract R apply(A a, B b);

    /**
     * Partially applies the function, substituting first arg
     * @param a -- first argument
     * @return new function.
     */
    public Function1<B, R> bind1(A a) {
        return new Function1<B, R>() {
            public R apply(B b) {
                return Function2.this.apply(a, b);
            }
        };
    }

    /**
     * Partially applies the function, substituing second argument.
     * @param b -- the second argument.
     * @return new function.
     */
    public Function1<A, R> bind2(B b) {
        return new Function1<A, R>() {
            public R apply(A a) {
                return Function2.this.apply(a, b);
            }
        };
    }

    /**
     * Partially applies the function, substituting first arg
     * @param a -- first argument
     * @return new function.
     */
    public Function1<B, R> curry(A a) {
        return bind1(a); // wtf this function.
    }

    /**
     * Returns the composition of function, g(f(..)), where f is *this.
     * @param g -- function to compose with
     * @return new function.
     */
    public <R2> Function2<A, B, R2> compose(Function1<R, R2> g) {
        return new Function2<A, B, R2>() {
            public R2 apply(A a, B b) {
                return g.apply(Function2.this.apply(a, b));
            }
        };
    }
}
