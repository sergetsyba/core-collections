package co.tsyba.core.collections;

import java.util.function.BiFunction;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Aug 12, 2019.
 */
public interface LameKeyedCollection<K, V> extends KeyedCollection<K, V> {
    /**
     * Returns keys of all entries of this collection.
     *
     * @return
     */
    public default Collection<K> getKeys() {
        return collect((key, value) -> key);
    }

    /**
     * Returns values of all entries of this collection.
     *
     * @return
     */
    public default Collection<V> getValues() {
        return collect((key, value) -> value);
    }

    /**
     * Returns entries of this collection with their keys converted by the
     * specified {@link BiFunction}.
     *
     * @param <R>
     * @param converter
     * @return
     */
    public default <R> KeyedCollection<R, V> convertKeys(BiFunction<K, V, R> converter) {
        return convert((key, value) -> {
            final var convertedKey = converter.apply(key, value);
            return new Map.Entry<>(convertedKey, value);
        });
    }

    /**
     * Returns entries of this collection with their values converted by the
     * specified {@link BiFunction}.
     *
     * @param <R>
     * @param converter
     * @return
     */
    public default <R> KeyedCollection<K, R> convertValues(BiFunction<K, V, R> converter) {
        return convert((key, value) -> {
            final var convertedValue = converter.apply(key, value);
            return new Map.Entry<>(key, convertedValue);
        });
    }
}
