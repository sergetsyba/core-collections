package co.tsyba.core.collections;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Aug 11, 2019.
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> {
    /**
     * Applies this function to the specified arguments.
     *
     * @param t
     * @param u
     * @param v
     * @return
     */
    R apply(T t, U u, V v);
}
