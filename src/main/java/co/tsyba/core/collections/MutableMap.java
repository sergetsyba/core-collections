package co.tsyba.core.collections;

public class MutableMap<K, V> extends Map<K, V> {
	private static final int minimumCapacity = 64;

	MutableMap(Set<Entry<K, V>> store) {
		super(store.store);
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
	public MutableMap(Entry<K, V>... entries) {
		super(entries);
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
	public MutableMap(List<K> keys, List<V> values) {
		super(keys, values);
	}

	/**
	 * Creates a copy of the specified entries.
	 */
	public MutableMap(Map<K, V> entries) {
		super(entries);
	}

	/**
	 * Returns value for the specified key in this map.
	 * <p>
	 * When this map contains no entry with the specified key, <em>inserts an entry with
	 * the specified key and value into this map</em> and returns the specified value.
	 * <p>
	 * When the specified key is {@code null} does not insert any entry, but returns the
	 * specified value, even when it's {@code null}. When the specified value is
	 * {@code null} and this map contains no entry with the specified key, does not insert
	 * any entry and returns {@code null}.
	 */
	public V get(K key, V backup) {
		return get(key)
			.orElseGet(() -> {
				set(key, backup);
				return backup;
			});
	}

	/**
	 * Sets the specified value for the specified key in this map.
	 * <p>
	 * When this map contains an entry with the specified key, replaces its value with the
	 * specified one.
	 * <p>
	 * Does nothing when either the specified key or value is {@code null}.
	 *
	 * @return itself
	 */
	public MutableMap<K, V> set(K key, V value) {
		if (key != null && value != null) {
			final var entry = new Entry<>(key, value);
			store.insert(entry);
		}

		return this;
	}

	/**
	 * Adds all entries of the specified map into this one.
	 * <p>
	 * When this and the specified maps contain entries with the same key, replaces values
	 * of each such entry in this map with a corresponding value from the specified map.
	 *
	 * @return itself
	 */
	public MutableMap<K, V> add(Map<K, V> entries) {
		for (var entry : entries) {
			store.insert(entry);
		}

		return this;
	}

	/**
	 * Adds all entries of the specified map to this one.
	 * <p>
	 * When this and the specified maps contain entries with the same key, calls the
	 * specified {@link TriFunction} for each such entry, and replaces its value in this
	 * map with a value returned from the specified {@link TriFunction}. The function is
	 * called with the key, a value from this, and a value from the specified maps.
	 * <p>
	 * When the specified {@link TriFunction} returns {@code null}, ignores the returned
	 * value and preserves the original value of an entry in this map.
	 *
	 * @return itself
	 */
	public MutableMap<K, V> add(Map<K, V> entries, TriFunction<K, V, V, V> resolver) {
		for (var entry : entries) {
			final var index = store.find(entry.key);
			if (index > -1) {
				var value = store.storage[index].item.value;
				value = resolver.apply(entry.key, value, entry.value);

				if (value != null) {
					entry = new Entry<>(entry.key, value);
					store.insert(entry);
				}
			} else {
				store.insert(entry);
			}
		}

		return this;
	}

	/**
	 * Removes an entry with the specified key from this map.
	 * <p>
	 * Does nothing when this map contains no entry with the specified key.
	 *
	 * @return itself
	 */
	public MutableMap<K, V> remove(K key) {
		if (key == null) {
			return this;
		}

		store.delete(key);
		return this;
	}

	/**
	 * Removes entries with the specified keys from this map.
	 * <p>
	 * Ignores any {@code null}s among the specified keys.
	 *
	 * @return itself
	 */
	@SafeVarargs
	public final MutableMap<K, V> remove(K... keys) {
		for (K key : keys) {
			remove(key);
		}

		return this;
	}

	/**
	 * Removes entries with the specified keys from this map.
	 *
	 * @return itself
	 */
	public MutableMap<K, V> remove(Collection<K> keys) {
		for (var key : keys) {
			store.delete(key);
		}

		return this;
	}


	/**
	 * Removes all entries from this map.
	 *
	 * @return itself
	 */
	public MutableMap<K, V> clear() {
		this.store = new RobinHoodHashStore<>(minimumCapacity);
		return this;
	}

	/**
	 * Returns an immutable copy of this map.
	 */
	public Map<K, V> toImmutable() {
		final var store2 = new RobinHoodHashStore<>(store);
		return new Map<>(store2);
	}
}

// created on Sep 1, 2019.
