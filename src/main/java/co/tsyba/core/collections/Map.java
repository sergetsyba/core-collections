package co.tsyba.core.collections;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;
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
		} else {
			final var entry = store.storage[index];
			return entry.item.value.equals(value);
		}
	}

	/**
	 * Returns {@code true} when this map contains all entries of the specified map;
	 * returns {@code false} otherwise.
	 */
	public boolean contains(Map<K, V> entries) {
		return entries.eachMatches(this::contains);
	}

	/**
	 * Returns value of an entry with the specified key in this map. Returns an empty
	 * {@link Optional} when this map does not contain an entry with the specified key.
	 *
	 * @param key
	 * @return
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
	 *
	 * @param keys
	 * @return
	 */
	public Map<K, V> get(Collection<K> keys) {
		if (keys == null) {
			// return nothing when the keys is null
			return new Map<>();
		}

		final var returnedStore = new RobinHoodHashStore<Entry<K, V>>(store.entryCount);
		for (var key : keys) {
			get(key).ifPresent(value -> {
				final var entry = new Entry<>(key, value);
				returnedStore.insert(entry);
			});
		}

		return new Map<>(returnedStore);
	}

	/**
	 * Returns entries with the specified in from the map. Ignores any {@code null}s among
	 * the specified keys.
	 *
	 * <p>
	 * Does nothing when the specified variadic array is {@code null}.
	 * <p>
	 * Returns itself.
	 *
	 * @param keys
	 * @return
	 */
	public Map<K, V> get(K... keys) {
		if (keys == null) {
			// return nothing when the keys is null
			return new Map<>();
		}

		final var returnedStore = new RobinHoodHashStore<Entry<K, V>>(store.entryCount);
		for (var key : keys) {
			get(key).ifPresent(value -> {
				final var entry = new Entry<>(key, value);
				returnedStore.insert(entry);
			});
		}

		return new Map<>(returnedStore);
	}

	/**
	 * Returns entries of this map, whose key and value satisfy the specified
	 * {@link BiPredicate}.
	 *
	 * @param condition
	 * @return
	 */
	@Override
	public Map<K, V> filter(BiPredicate<K, V> condition) {
		final var filteredEntries = new RobinHoodHashStore<Entry<K, V>>(store.entryCount);
		for (var entry : this) {
			if (condition.test(entry.key, entry.value)) {
				filteredEntries.insert(entry);
			}
		}

		return new Map<>(filteredEntries);
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
	@Override
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
	 * Returns entries of this map with their key and value converted by the specified
	 * {@link BiFunction}.
	 * <p>
	 * Any {@code null} key or value returned by the specified {@link BiFunction} will be
	 * ignored. This can be used to perform both entry filtering and conversion in a
	 * single operation.
	 */
	@Override
	public <L, W> Map<L, W> convert(BiFunction<K, V, Entry<L, W>> converter) {
		final var convertEntries = new RobinHoodHashStore<Entry<L, W>>(store.entryCount);
		for (var entry : this) {
			final var convertedEntry = converter.apply(entry.key, entry.value);
			if (convertedEntry != null
				&& convertedEntry.key != null
				&& convertedEntry.value != null) {

				convertEntries.insert(convertedEntry);
			}
		}

		return new Map<>(convertEntries);
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
}
