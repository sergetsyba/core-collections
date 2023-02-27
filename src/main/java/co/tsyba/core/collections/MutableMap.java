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
	 * Returns value for the specified key in this map. When this map contains no entry
	 * with the specified key, <em>inserts an entry with the specified key and backup
	 * value into this map</em> and returns the backup value.
	 * <p>
	 * When the specified key is {@code null} does not insert an entry, but returns the
	 * specified backup value, even when the backup value is {@code null} as well.
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
		if (key != null && value != null) {
			final var entry = new Entry<>(key, value);
			store.insert(entry);
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