package co.tsyba.core.collections;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class MutableMap<K, V> extends Map<K, V> {
	private static final int minimumCapacity = 64;

	MutableMap(RobinHoodHashStore<Entry<K, V>> store) {
		super(store);
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
	 * Creates an empty map.
	 */
	public MutableMap() {
		super(new RobinHoodHashStore<>(minimumCapacity));
	}

	/**
	 * Returns value for the specified key in this map. When this map contains no entry
	 * with the specified key, <em>inserts the specified backup value for the key<em/> and
	 * returns the backup value.
	 * <p>
	 * When the specified key is {@code null}, returns the specified backup value, even
	 * when the backup value is {@code null} as well.
	 */
	public V get(K key, V backup) {
		return get(key)
			.orElseGet(() -> {
				set(key, backup);
				return backup;
			});
	}

	/**
	 * Inserts an entry with the specified key and value into this map.
	 * <p>
	 * When this map already contains an entry with the specified key, replaces its value
	 * with the specified one.
	 * <p>
	 * Does nothing when either the specified key or value is {@code null}.
	 *
	 * @return itself
	 */
	public MutableMap<K, V> set(K key, V value) {
		if (key != null || value != null) {
			final var entry = new Entry<>(key, value);
			store.insert(entry);
		}

		return this;
	}

	/**
	 * Inserts an entry with the specified key and value into this map, using the
	 * specified {@link BiFunction} to resolve value for an existing entry with the
	 * specified key.
	 *
	 * <p>
	 * When this map does not contain an entry with the specified key, inserts a new entry
	 * with the specified key and value, just like
	 * {@link #set(java.lang.Object, java.lang.Object) }.
	 * <p>
	 * When this map contains an entry with the specified key, calls the specified
	 * {@link BiFunction} with value of the existing entry and the specified value. Then,
	 * replaces value of the existing entry with the returned value of the
	 * {@link BiFunction}.
	 * <p>
	 * Does nothing when the specified key, the specified value, or the value returned by
	 * the specified {@link BiFunction} is {@code null}.
	 * <p>
	 * Returns itself.
	 *
	 * @param key
	 * @param value
	 * @param merger
	 * @return
	 */
	public MutableMap<K, V> set(K key, V value, BiFunction<V, V, V> merger) {
		final var setValue = get(key)
			.map(storedValue -> merger.apply(storedValue, value))
			.orElse(value);

		return set(key, setValue);
	}

	/**
	 * Inserts all entries from the specified map into this one.
	 * <p>
	 * When this map already contains entries with some keys from the specified map,
	 * replaces their values with corresponding values from the specified map.
	 *
	 * @return itself
	 */
	public MutableMap<K, V> set(Map<K, V> entries) {
		entries.iterate(this::set);
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
	 * Removes entries with the specified keys from the map.
	 *
	 * <p>
	 * Does nothing when the specified {@link Collection} is {@code null}.
	 * <p>
	 * Returns itself.
	 *
	 * @param keys
	 * @return
	 */
	public MutableMap<K, V> remove(Collection<K> keys) {
		if (keys == null) {
			// do nothing when the keys is null
			return this;
		}

		keys.iterate(this::remove);
		return this;
	}

	/**
	 * Removes entries with the specified keys from the map. Ignores any {@code null}s
	 * among the specified keys.
	 *
	 * <p>
	 * Does nothing when the specified variadic array is {@code null}.
	 * <p>
	 * Returns itself.
	 *
	 * @param keys
	 * @return
	 */
	public MutableMap<K, V> remove(K... keys) {
		if (keys == null) {
			// do nothing when the keys is null
			return this;
		}

		for (K key : keys) {
			remove(key);
		}

		return this;
	}

	/**
	 * Removes all entries from this map.
	 *
	 * <p>
	 * Returns itself.
	 *
	 * @return
	 */
	public MutableMap<K, V> clear() {
		this.store = new RobinHoodHashStore<>(minimumCapacity);
		return this;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public MutableMap<K, V> iterate(BiConsumer<K, V> operation) {
		super.iterate(operation);
		return this;
	}

	/**
	 * @inheritDoc
	 */
//	@Override
	public MutableMap<K, V> filter(BiPredicate<K, V> condition) {
//		super.filter(condition);
		return this;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public <L, W> MutableMap<L, W> convert(BiFunction<K, V, Entry<L, W>> converter) {
		final var convertEntries = new RobinHoodHashStore<Entry<L, W>>(store.entryCount);
		for (var entry : this) {
			final var convertedEntry = converter.apply(entry.key, entry.value);
			if (convertedEntry != null
				&& convertedEntry.key != null
				&& convertedEntry.value != null) {

				convertEntries.insert(convertedEntry);
			}
		}

		return new MutableMap<>(convertEntries);
	}

	public Map<K, V> toImmutable() {
		final var store2 = new RobinHoodHashStore<>(store);
		return new Map<>(store2);
	}
}


// created on Sep 1, 2019.