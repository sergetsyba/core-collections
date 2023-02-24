package co.tsyba.core.collections;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class Map<K, V> implements Iterable<Map.Entry<K, V>> {
	RobinHoodHashStore<Entry<K, V>> store;

	Map(RobinHoodHashStore<Entry<K, V>> store) {
		this.store = new RobinHoodHashStore<>(store);
	}

	/**
	 * Creates a map with the specified entries.
	 * <p>
	 * Ignores any {@code null} values among the specified entries, as well as entries
	 * with {@code} null keys or values.
	 * <p>
	 * When the specified entries contain repeated keys, only the last entry with such key
	 * ends up in the map.
	 */
	@SafeVarargs
	public Map(Entry<K, V>... entries) {
		final var store = new RobinHoodHashStore<Entry<K, V>>(entries.length);
		for (var entry : entries) {
			if (entry != null && entry.key != null && entry.value != null) {
				store.insert(entry);
			}
		}

		this.store = store;
	}

	/**
	 * Creates a map with the specified keys and values, matching them by indexes in their
	 * lists.
	 * <p>
	 * When the specified lists differ in item count, extra items in the longer list are
	 * ignored.
	 * <p>
	 * When the specified keys contain repeated items, only the last occurrence of such
	 * key, as well as its matching value, ends up in the map.
	 */
	public Map(List<K> keys, List<V> values) {
		final var iterator1 = keys.iterator();
		final var iterator2 = values.iterator();

		final var store = new RobinHoodHashStore<Entry<K, V>>(64);
		while (iterator1.hasNext() && iterator2.hasNext()) {
			store.insert(
				new Entry<>(
					iterator1.next(),
					iterator2.next()));
		}

		this.store = store;
	}

	/**
	 * Creates a copy of the specified {@link Map}.
	 */
	public Map(Map<K, V> entries) {
		this(entries.store);
	}

	/**
	 * Returns {@code true} when this map has no entries; returns {@code false}
	 * otherwise.
	 */
	public boolean isEmpty() {
		return store.entryCount == 0;
	}

	/**
	 * Returns the number of entries in this map.
	 */
	public int getCount() {
		return store.entryCount;
	}

	/**
	 * Returns {@code true} when this map contains an entry with the specified key and
	 * value; returns {@code false} otherwise.
	 */
	public boolean contains(K key, V value) {
		if (key == null) {
			return false;
		}

		final var index = store.find(key);
		if (index < 0) {
			return false;
		}

		final var entry = store.storage[index].item;
		return entry.value.equals(value);
	}

	/**
	 * Returns {@code true} when this map contains all entries of the specified map;
	 * returns {@code false} otherwise.
	 * <p>
	 * When the specified {@link Map} is empty, returns {@code true}.
	 */
	public boolean contains(Map<K, V> entries) {
		return entries.allMatch(this::contains);
	}

	/**
	 * Returns keys of all entries in this map.
	 */
	public Set<K> getKeys() {
		final var keys = new MutableSet<K>();
		for (var entry : this) {
			keys.add(entry.key);
		}

		return keys.toImmutable();
	}

	/**
	 * Returns values of all entries in this map.
	 */
	public List<V> getValues() {
		final var values = new MutableList<V>();
		for (var entry : this) {
			values.append(entry.value);
		}

		return values.toImmutable();
	}

	/**
	 * Returns value for the specified key in this map.
	 * <p>
	 * When this map contains no entry with the specified key, returns an empty
	 * {@link Optional}.
	 */
	public Optional<V> get(K key) {
		if (key == null) {
			return Optional.empty();
		}

		final var index = store.find(key);
		if (index < 0) {
			return Optional.empty();
		}

		final var entry = store.storage[index].item;
		return Optional.of(entry.value);
	}

	/**
	 * Returns entries with the specified keys in this map.
	 * <p>
	 * Ignores any {@code null} values among the specified keys.
	 */
	@SafeVarargs
	public final Map<K, V> get(K... keys) {
		final var entries = new MutableMap<K, V>();
		for (var key : keys) {
			get(key)
				.ifPresent((value) ->
					entries.set(key, value));
		}

		return entries.toImmutable();
	}

	/**
	 * Returns entries with the specified keys in this map.
	 */
	public Map<K, V> get(Collection<K> keys) {
		final var entries = new MutableMap<K, V>();
		for (var key : keys) {
			get(key)
				.ifPresent((value) ->
					entries.set(key, value));
		}

		return entries.toImmutable();
	}

	/**
	 * Returns {@code true} when any entry in this map satisfies the specified
	 * {@link BiPredicate}; returns {@code false} otherwise.
	 * <p>
	 * When this map is empty, returns {@code false}.
	 */
	public boolean anyMatches(BiPredicate<K, V> condition) {
		for (var entry : this) {
			if (condition.test(entry.key, entry.value)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns {@code true} when no entries in this map satisfy the specified
	 * {@link BiPredicate}; returns {@code false} otherwise.
	 * <p>
	 * When this map is empty, returns {@code true};
	 */
	public boolean noneMatches(BiPredicate<K, V> condition) {
		for (var entry : this) {
			if (condition.test(entry.key, entry.value)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns {@code true} when all entries in this map satisfy the specified
	 * {@link BiPredicate}; returns {@code false} otherwise.
	 * <p>
	 * When this map is empty, returns {@code true}.
	 */
	public boolean allMatch(BiPredicate<K, V> condition) {
		for (var entry : this) {
			if (!condition.test(entry.key, entry.value)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns value of some entry in this map, which satisfies the specified
	 * {@link BiPredicate}.
	 * <p>
	 * When no entry in this map satisfies the specified {@link BiPredicate}, returns an
	 * empty {@link Optional}.
	 */
	public Optional<V> matchAny(BiPredicate<K, V> condition) {
		for (var entry : this) {
			if (condition.test(entry.key, entry.value)) {
				return Optional.of(entry.value);
			}
		}

		return Optional.empty();
	}

	/**
	 * Applies the specified {@link BiConsumer} to each entry in this map.
	 *
	 * @return itself
	 */
	public Map<K, V> iterate(BiConsumer<K, V> operation) {
		for (var entry : this) {
			operation.accept(entry.key, entry.value);
		}

		return this;
	}

	/**
	 * Returns entries of this map, which satisfy the specified {@link BiPredicate}.
	 * <p>
	 * When no entry in this map satisfies the specified {@link BiPredicate}, returns an
	 * empty {@link Map}.
	 */
	public Map<K, V> filter(BiPredicate<K, V> condition) {
		final var entries = new MutableMap<K, V>();
		for (var entry : this) {
			if (condition.test(entry.key, entry.value)) {
				entries.set(entry.key, entry.value);
			}
		}

		return entries.toImmutable();
	}

	/**
	 * Returns entries of this map, converted by the specified {@link BiFunction}.
	 * <p>
	 * When the specified {@link BiFunction} returns a {@code null} or an {@link Entry}
	 * with a {@code null} key or value, the converted entry is ignored. So, this method
	 * can be used to both filter and convert this map in a single operation.
	 */
	public <L, W> Map<L, W> convert(BiFunction<K, V, Entry<L, W>> converter) {
		final var entries = new MutableMap<L, W>();
		for (var entry : this) {
			final var converted = converter.apply(entry.key, entry.value);
			if (converted != null) {
				entries.set(converted.key, converted.value);
			}
		}

		return entries.toImmutable();
	}

	/**
	 * Combines this map into a single value by applying the specified {@link TriFunction}
	 * to each entry and an intermediate combination, using the specified initial value as
	 * a starting point.
	 */
	public <R> R combine(R initial, TriFunction<R, K, V, R> combiner) {
		var combined = initial;
		for (var entry : this) {
			combined = combiner.apply(combined, entry.key, entry.value);
		}

		return combined;
	}

	/**
	 * Combines this map into a {@link String} by joining {@link String} representations
	 * of key and value of each entry with the specified value separator, then joining
	 * them with the specified entry separator between them.
	 */
	public String join(String valueSeparator, String entrySeparator) {
		final var iterator = iterator();
		final var builder = new StringBuilder();

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

	@Override
	public Iterator<Entry<K, V>> iterator() {
		return store.iterator();
	}

	@Override
	public int hashCode() {
		return store.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof Map)) {
			return false;
		}

		@SuppressWarnings("unchecked")
		final var map = (Map<K, V>) object;
		return store.equals(map.store);
	}

	@Override
	public String toString() {
		return "{" + join(":", ", ") + "}";
	}

	public static class Entry<K, V> {
		public final K key;
		public final V value;

		/**
		 * Creates an entry with the specified key and value.
		 */
		public Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(key);
		}

		@Override
		public boolean equals(Object object) {
			if (object == this) {
				return true;
			}
			if (object == key) {
				return true;
			}
			if (!(object instanceof Entry)) {
				return false;
			}

			@SuppressWarnings("unchecked")
			final var entry = (Entry<K, V>) object;
			return key.equals(entry.key);
		}

		@Override
		public String toString() {
			return key + ":" + value;
		}
	}
}

// created on Jul 29, 2019.
