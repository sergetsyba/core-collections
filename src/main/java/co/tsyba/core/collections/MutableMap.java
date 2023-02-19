package co.tsyba.core.collections;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Sep 1, 2019.
 */
public class MutableMap<K, V> extends Map<K, V> {
	private static final int minimumCapacity = 64;

	MutableMap(RobinHoodHashStore<Entry<K, V>> store) {
		super(store);
	}

	/**
	 * Creates a copy of the specified entries.
	 *
	 * @param entries
	 */
	public MutableMap(Map<K, V> entries) {
		super(entries);
	}

	public MutableMap(List<K> keys, List<V> values) {
		super(keys, values);
	}

	/**
	 * Creates an empty map.
	 */
	public MutableMap() {
		super(new RobinHoodHashStore<>(minimumCapacity));
	}

	/**
	 * Creates a copy of the specified entries of {@link java.util.Map}. Ignores any
	 * entries with {@code null} key or value.
	 *
	 * @param entries
	 */
	public MutableMap(java.util.Map<K, V> entries) {
		super(entries);
	}

	/**
	 * Returns value of an entry with the specified key in this map, keeping the specified
	 * value as a backup.
	 *
	 * <p>
	 * When this map contains an entry with the specified key, returns its value, just
	 * like {@link #get(java.lang.Object)}.
	 * <p>
	 * When this map does not contain an entry with the specified key,
	 * <em>inserts a new entry</em> with the specified key and backup value into
	 * this map and returns the specified backup value.
	 * <p>
	 * Does nothing when the specified key {@code null}. Similarly, does nothing when this
	 * map does not contain an entry with the specified key and the specified backup value
	 * is {@code null}.
	 *
	 * @param key
	 * @param backupValue
	 * @return
	 */
	public V get(K key, V backupValue) {
		return get(key)
			.orElseGet(() -> {
				set(key, backupValue);
				return backupValue;
			});
	}

	/**
	 * Inserts an entry with the specified key and value into this map. When this map
	 * contains an entry with the specified key, replaces its value with the specified
	 * one.
	 *
	 * <p>
	 * Does nothing when either key or value is {@code null}.
	 * <p>
	 * Returns itself.
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public MutableMap<K, V> set(K key, V value) {
		if (key == null || value == null) {
			// do nothing when either key or value is null
			return this;
		}

		final var entry = new Entry<K, V>(key, value);
		store.insert(entry);

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

		set(key, setValue);
		return this;
	}

	/**
	 * Inserts the specified entries into this map. When this map contains entries with
	 * keys from the specified entries, replaces their values with corresponding values
	 * from the specified ones.
	 *
	 * <p>
	 * Does nothing when the specified {@link MutableMap} is {@code null}.
	 * <p>
	 * Returns itself.
	 *
	 * @param entries
	 * @return
	 */
	public MutableMap<K, V> set(Map<K, V> entries) {
		if (entries == null) {
			// do nothing when the entries is null
			return this;
		}

		entries.iterate(this::set);
		return this;
	}

	/**
	 * Removes entry with the specified key from this map. Does nothing when this map does
	 * not contain an entry with the specified key.
	 *
	 * <p>
	 * Does nothing when the specified key is {@code null}.
	 * <p>
	 * Returns itself.
	 *
	 * @param key
	 * @return
	 */
	public MutableMap<K, V> remove(K key) {
		if (key == null) {
			// do nothing when the key is null
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
