package co.tsyba.core.collections;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jul 29, 2019.
 */
public class Map<K, V> implements LameKeyedCollection<K, V> {
	RobinHoodHashStore<Entry<K, V>> store;

	Map(RobinHoodHashStore<Entry<K, V>> store) {
		this.store = new RobinHoodHashStore<>(store);
	}

	/**
	 * Creates a copy of the specified entries.
	 *
	 * @param entries
	 */
	public Map(Map<K, V> entries) {
		this(entries.store);
	}

	/**
	 * Creates a map by matching keys with values at corresponding indexes in the
	 * specified lists.
	 * <p>
	 * When the specified lists differ in item count, the created map will contain at most
	 * the number of entries of the shorter of the specified lists.
	 * <p>
	 * When the specified keys contain repeated items, only the last occurrence of the key
	 * (as well as the value at the corresponding index) will end up in the map.
	 *
	 * @param keys
	 * @param values
	 */
	public Map(List<K> keys, List<V> values) {
		final var entryCount = Math.min(
			keys.getCount(),
			values.getCount());

		this.store = new RobinHoodHashStore<>(entryCount);
		Lists.iterate(keys, values, (key, value) -> {
			final var entry = new Entry<>(key, value);
			this.store.insert(entry);
		});
	}

	/**
	 * Creates an empty map.
	 */
	public Map() {
		this.store = new RobinHoodHashStore<>(0);
	}

	/**
	 * Creates a copy of the specified entries of {@link java.util.Map}. Ignores any
	 * entries with {@code null} key or value.
	 *
	 * @param entries
	 */
	public Map(java.util.Map<K, V> entries) {
		final var entryCount = entries.size();
		this.store = new RobinHoodHashStore<>(entryCount);

		entries.forEach((key, value) -> {
			if (key != null && value != null) {
				final var entry = new Entry<>(key, value);
				this.store.insert(entry);
			}
		});
	}

	/**
	 * Returns {@code true} when this map has no entries; returns {@code false}
	 * otherwise.
	 */
	@Override
	public boolean isEmpty() {
		return store.entryCount == 0;
	}

	/**
	 * Returns the number of entries in this map.
	 */
	@Override
	public int getCount() {
		return store.entryCount;
	}

	/**
	 * Returns {@code true} when this map contains and entry with the specified key and
	 * value; returns {@code false} otherwise.
	 */
	@Override
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
		return entries.eachMatches(this::contains);
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
	@Override
	public Collection<V> getValues() {
		final var values = new MutableList<V>();
		for (var entry : this) {
			values.append(entry.value);
		}

		return values.toImmutable();
	}

	/**
	 * Returns value for the specified key in this map.
	 * <p>
	 * When this map has no value for the specified key, returns an empty
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
	@Override
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
	@Override
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
	public boolean matches(BiPredicate<K, V> condition) {
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
	 * When no entry in this map satisfies the specified {@link BiPredicate}, or when this
	 * map is empty, returns an empty {@link Optional}.
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
	 * Returns entries of this map, which satisfy the specified {@link BiPredicate}.
	 * <p>
	 * When no entry in this map satisfies the specified {@link BiPredicate}, or when this
	 * map is empty, returns an empty {@link Map}.
	 */
	public Map<K, V> match(BiPredicate<K, V> condition) {
		final var entries = new MutableMap<K, V>();
		for (var entry : this) {
			if (condition.test(entry.key, entry.value)) {
				entries.set(entry.key, entry.value);
			}
		}

		return entries.toImmutable();
	}

	/**
	 * Applies the specified {@link BiConsumer} to each entry in this map.
	 *
	 * @return itself
	 */
	@Override
	public Map<K, V> iterate(BiConsumer<K, V> operation) {
		for (var entry : this) {
			operation.accept(entry.key, entry.value);
		}
		
		return this;
	}

	/**
	 * Returns entries of this map, converted by the specified {@link BiFunction}.
	 * <p>
	 * When the specified {@link BiFunction} returns a {@code null} or an {@link Entry}
	 * with a {@code null} key or value, the converted entry will be ignored. Therefore,
	 * this method can be used to both filter and convert this map in a single operation.
	 */
	@Override
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
	 * Returns entries of this map with their key and value combined by the specified
	 * {@link BiFunction}.
	 * <p>
	 * Any {@code null} value returned by the specified {@link BiFunction} will be
	 * ignored. This can be used to perform both item filtering and key-value combination
	 * in a single operation.
	 *
	 * @param <R>
	 * @param converter
	 * @return
	 */
	public <R> Collection<R> collect(BiFunction<K, V, R> converter) {
		final var items = new ContiguousArrayStore<R>(store.entryCount);
		for (var entry : this) {
			final var convertedItem = converter.apply(entry.key, entry.value);
			items.append(convertedItem);
		}

		items.removeExcessCapacity();
		return new List<>(items);
	}


	/**
	 * Converts this map into an instance of {@link java.util.Map}.
	 *
	 * @return
	 */
	public java.util.Map<K, V> bridge() {
		final var entries = new HashMap<K, V>();
		iterate(entries::put);

		return entries;
	}

	@Override
	public Iterator<Entry<K, V>> iterator() {
		return new Iterator<Entry<K, V>>() {
			final Iterator<Entry<K, V>> iterator = store.iterator();

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public Entry<K, V> next() {
				return iterator.next();
			}
		};
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

		final Map map = (Map) object;
		return store.equals(map.store);
	}

	@Override
	public String toString() {
		return "{" + join(": ", ", ") + "}";
	}

	public static class Entry<K, V> {
		public final K key;
		public final V value;

		/**
		 * Creates a new entry with the specified key and value.
		 *
		 * @param key
		 * @param value
		 */
		public Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public int hashCode() {
			return key.hashCode();
		}

		@Override
		public boolean equals(Object object) {
			if (object.equals(key)) {
				return true;
			}
			if (!(object instanceof Entry)) {
				return false;
			}

			final var entry = (Entry) object;
			return key.equals(entry.key);
		}
	}

	// todo: remove
	@Override
	public KeyedCollection<K, V> filter(BiPredicate<K, V> condition) {
		throw new UnsupportedOperationException();
	}
}
