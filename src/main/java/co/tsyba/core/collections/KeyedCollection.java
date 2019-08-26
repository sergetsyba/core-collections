package co.tsyba.core.collections;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Aug 4, 2019.
 */
public interface KeyedCollection<K, V> extends Iterable<Map.Entry<K, V>> {
	/**
	 * Returns {@link true} when this collection ha no entries; returns
	 * {@link false} otherwise.
	 *
	 * @return
	 */
	public default boolean isEmpty() {
		return iterator()
				.hasNext() == false;
	}

	/**
	 * Returns the number of entries in this collection.
	 *
	 * @return
	 */
	public default int getCount() {
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
	public default boolean contains(K key) {
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
	public default boolean contains(K key, V value) {
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
	public default boolean contains(KeyedCollection<K, V> entries) {
		return entries.eachMatches(this::contains)
				|| (isEmpty() && entries.isEmpty());
	}

	/**
	 * Returns {@code true} when key and value of every entry in this collection
	 * does not satisfy the specified {@link BiPredicate}; returns {@code false}
	 * otherwise.
	 *
	 * @param condition
	 * @return
	 */
	public default boolean noneMatches(BiPredicate<K, V> condition) {
		for (var entry : this) {
			if (condition.test(entry.key, entry.value)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns {@code true} when key and value of at least one entry in this
	 * collection satisfies the specified {@link BiPredicate}; returns
	 * {@code false} otherwise.
	 *
	 * @param condition
	 * @return
	 */
	public default boolean anyMatches(BiPredicate<K, V> condition) {
		for (var entry : this) {
			if (condition.test(entry.key, entry.value)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns {@code true} when key and value of every entry of this collection
	 * satisfy the specified {@link BiPredicate}; returns {@code false}
	 * otherwise.
	 *
	 * @param condition
	 * @return
	 */
	public default boolean eachMatches(BiPredicate<K, V> condition) {
		for (var entry : this) {
			if (!condition.test(entry.key, entry.value)) {
				return false;
			}
		}

		// empty collection matches no entry
		return !isEmpty();
	}

	/**
	 * Applies the specified {@link BiConsumer} to key and value of every entry
	 * in this collection. Returns itself.
	 *
	 * @param operation
	 * @return
	 */
	public default KeyedCollection<K, V> iterate(BiConsumer<K, V> operation) {
		for (var entry : this) {
			operation.accept(entry.key, entry.value);
		}

		return this;
	}

	/**
	 * Returns entries of this collection whose key and value satisfy the
	 * specified {@link BiPredicate}.
	 *
	 * @param condition
	 * @return
	 */
	public KeyedCollection<K, V> filter(BiPredicate<K, V> condition);

	/**
	 * Returns entries of this collection with their keys and values converted
	 * by the specified {@link BiFunction}.
	 *
	 * @param <L>
	 * @param <W>
	 * @param converter
	 * @return
	 */
	public <L, W> KeyedCollection<L, W> convert(BiFunction<K, V, Map.Entry<L, W>> converter);

	/**
	 * Returns entries of this collection with their key and value combined by
	 * the specified {@link BiFunction}.
	 *
	 * @param <R>
	 * @param converter
	 * @return
	 */
	public <R> Collection<R> collect(BiFunction<K, V, R> converter);

	/**
	 * Combines this collection into a single value by applying the specified
	 * {@link TriFunction} to key and value of every entry and an intermediate
	 * combination, starting from the specified initial value.
	 *
	 * @param <R>
	 * @param initial
	 * @param combiner
	 * @return
	 */
	public default <R> R combine(R initial, TriFunction<R, K, V, R> combiner) {
		var combination = initial;
		for (var entry : this) {
			combination = combiner.apply(combination, entry.key, entry.value);
		}

		return combination;
	}

	/**
	 * Combines this collection into a {@link String} by joining key and value
	 * of every entry with the specified value separator between them, and
	 * joining every entry with the specified entry separator between them.
	 *
	 * @param valueSeparator
	 * @param entrySeparator
	 * @return
	 */
	public default String join(String valueSeparator, String entrySeparator) {
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
