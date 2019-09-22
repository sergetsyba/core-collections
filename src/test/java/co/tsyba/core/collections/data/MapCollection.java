package co.tsyba.core.collections.data;

import co.tsyba.core.collections.Collection;
import co.tsyba.core.collections.Map.Entry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import co.tsyba.core.collections.KeyedCollection;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Aug 11, 2019.
 */
public class MapCollection<K, V> implements KeyedCollection<K, V> {
	private final Map<K, V> entries = new HashMap<>();

	public MapCollection(Object... items) {
		for (var index = 0; index < items.length; index += 2) {
			entries.put((K) items[index], (V) items[index + 1]);
		}
	}

	@Override
	public KeyedCollection<K, V> filter(BiPredicate<K, V> condition) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <L, W> KeyedCollection<L, W> convert(BiFunction<K, V, Entry<L, W>> converter) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <R> Collection<R> collect(BiFunction<K, V, R> converter) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Entry<K, V>> iterator() {
		return new Iterator<Entry<K, V>>() {
			final Iterator<java.util.Map.Entry<K, V>> iterator = entries.entrySet()
					.iterator();

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public Entry<K, V> next() {
				final var entry = iterator.next();
				final var key = entry.getKey();
				final var value = entry.getValue();

				return new Entry<>(key, value) {
				};
			}
		};
	}
}
