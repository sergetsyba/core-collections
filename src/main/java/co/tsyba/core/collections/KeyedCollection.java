package co.tsyba.core.collections;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Aug 4, 2019.
 */
public interface KeyedCollection<K, V> extends Iterable<Map.Entry<K, V>> {
    /**
     * Returns {@code true} when this collection is empty; returns {@code false}
     * otherwise.
     *
     * @return
     */
    default boolean isEmpty() {
        return iterator()
                .hasNext() == false;
    }

    /**
     * Returns the number of entries in this collection.
     *
     * @return
     */
    default int getCount() {
        var entryCount = 0;
        for (var entry : this) {
            entryCount += 1;
        }

        return entryCount;
    }

    /**
     * Returns {@code true} when this collection contains the specified key;
     * returns {@code false} otherwise.
     *
     * @param key
     * @return
     */
    default boolean contains(K key) {
        return anyMatches((storedKey, value) -> storedKey.equals(key));
    }

    /**
     * Returns {@code true} when this collection contains an entry with the
     * specified key and value; returns {@code false} otherwise.
     *
     * @param key
     * @param value
     * @return
     */
    default boolean contains(K key, V value) {
        return anyMatches((storedKey, storedValue) -> storedKey.equals(key)
                && storedValue.equals(value));
    }

    /**
     * Returns {@code true} when this collection contains every entry of the
     * specified collection; returns {@code false} otherwise.
     *
     * @param entries
     * @return
     */
    default boolean contains(KeyedCollection<K, V> entries) {
        return entries.eachMatches(this::contains)
                || (isEmpty() && entries.isEmpty());
    }

    /**
     * Returns {@code true} when no entry in this collection satisfies the
     * specified {@link BiPredicate}; returns {@code false} otherwise.
     *
     * @param condition
     * @return
     */
    default boolean noneMatches(BiPredicate<K, V> condition) {
        for (var entry : this) {
            if (condition.test(entry.key, entry.value)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns {@code true} when at least one entry in this collection satisfies
     * the specified {@link BiPredicate}; returns {@code false} otherwise.
     *
     * @param condition
     * @return
     */
    default boolean anyMatches(BiPredicate<K, V> condition) {
        for (var entry : this) {
            if (condition.test(entry.key, entry.value)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns {@code true} when every entry in this collection satisfies the
     * specified {@link BiPredicate}; returns {@code false} otherwise.
     *
     * When this collection is empty, returns {@code false}.
     *
     * @param condition
     * @return
     */
    default boolean eachMatches(BiPredicate<K, V> condition) {
        for (var entry : this) {
            if (!condition.test(entry.key, entry.value)) {
                return false;
            }
        }

        // empty collection matches no entry
        return !isEmpty();
    }

    /**
     * Applies the specified {@link BiConsumer} to every entry in this
     * collection.
     *
     * Returns itself.
     *
     * @param operation
     * @return
     */
    default KeyedCollection<K, V> iterate(BiConsumer<K, V> operation) {
        for (var entry : this) {
            operation.accept(entry.key, entry.value);
        }

        return this;
    }

    /**
     * Returns entries of this collection, which satisfy the specified
     * {@link BiPredicate}.
     *
     * @param condition
     * @return
     */
    KeyedCollection<K, V> filter(BiPredicate<K, V> condition);

    /**
     * Returns entries of this collection, converted by the specified
     * {@link BiFunction}.
     *
     * @param <L>
     * @param <W>
     * @param converter
     * @return
     */
    <L, W> KeyedCollection<L, W> convert(BiFunction<K, V, Map.Entry<L, W>> converter);

    /**
     * Combines this collection into a single value by applying the specified
     * {@link TriFunction} to every entry and an intermediate combination, using
     * the specified value as a starting point.
     *
     * @param <R>
     * @param initial
     * @param combiner
     * @return
     */
    default <R> R combine(R initial, TriFunction<R, K, V, R> combiner) {
        var combination = initial;
        for (var entry : this) {
            combination = combiner.apply(combination, entry.key, entry.value);
        }

        return combination;
    }

    /**
     * Returns entries of this collection, combined by the specified
     * {@link BiFunction}.
     *
     * @param <R>
     * @param converter
     * @return
     */
    <R> Collection<R> collect(BiFunction<K, V, R> converter);

    /**
     * Combines this collection into a {@link String} by joining key and value
     * of every entry with the specified value separator, then joining them with
     * the specified entry separator.
     *
     * @param valueSeparator
     * @param entrySeparator
     * @return
     */
    default String join(String valueSeparator, String entrySeparator) {
        final var builder = new StringBuilder();

        final var iterator = iterator();
        if (iterator.hasNext()) {
            final var entry = iterator.next();
            builder.append(entry.key)
                    .append(valueSeparator)
                    .append(entry.value);
        }

        while (iterator.hasNext()) {
            final var entry = iterator.next();
            builder.append(entrySeparator)
                    .append(entry.key)
                    .append(valueSeparator)
                    .append(entry.value);
        }

        return builder.toString();
    }
}
