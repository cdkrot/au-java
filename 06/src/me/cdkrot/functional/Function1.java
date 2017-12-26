package me.cdkrot.functional;

/**
 * Represents function from A to R.
 * @param <A> first argument type.
 * @param <R> result.
 */
public abstract class Function1<A, R> {
    /**
     * Returns result of application of this function to the argument.
     * @param a -- the argument
     * @return the result.
     */
    public abstract R apply(A a);

    /**
     * Returns the composition of function, g(f(..)), where f is *this.
     * @param g -- function to compose with
     * @param <R2> -- the g's return type.
     * @return new function.
     */
    public <R2> Function1<A, R2> compose(Function1<R, R2> g) {
        return new Function1<A, R2>() {
            public R2 apply(A a) {
                return g.apply(Function1.this.apply(a));
            }
        };
    }
}
