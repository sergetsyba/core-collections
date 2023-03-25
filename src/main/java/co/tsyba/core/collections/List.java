package co.tsyba.core.collections;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An immutable, sequential {@link Collection}, which provides efficient, randomized
 * access to its items.
 */
public class List<T> implements IndexedCollection<T> {
	ContiguousArrayStore store;

	List(int capacity) {
		this.store = new ContiguousArrayStore(capacity);
	}

	List(ContiguousArrayStore store) {
		this.store = store;
	}

	/**
	 * Creates a list with the specified items.
	 * <p>
	 * Ignores any {@code null} values among the specified items.
	 */
	@SafeVarargs
	public List(T... items) {
		final var store = new ContiguousArrayStore(items.length);
		for (var item : items) {
			if (item != null) {
				store.append(item);
			}
		}

		store.removeExcessCapacity();
		this.store = store;
	}

	/**
	 * Creates a copy of the specified {@link Collection}.
	 */
	public List(Collection<T> items) {
		final var array = items.toArray();
		this.store = new ContiguousArrayStore(array);
	}

	@Override
	public int getCount() {
		return store.itemCount;
	}

	@Override
	public T get(int index) {
		final var range = getIndexRange();
		if (!range.contains(index)) {
			throw new IndexNotInRangeException(index, range);
		}

		@SuppressWarnings("unchecked")
		final var item = (T) store.items[index];
		return item;
	}

	@Override
	public List<T> get(IndexRange indexRange) {
		final var validRange = getIndexRange();
		if (!validRange.contains(indexRange) || isEmpty()) {
			throw new IndexRangeNotInRangeException(indexRange, validRange);
		}

		final var items = new List<T>(indexRange.length);
		System.arraycopy(store.items, indexRange.start, items.store.items, 0, indexRange.length);
		return items;
	}

	@Override
	public Optional<Integer> find(IndexedCollection<T> items) {
		return Optional.empty();
	}

	private static <T> boolean contains(T[] items, int count, T item) {
		for (var index = 0; index < count; ++index) {
			if (items[index].equals(item)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns distinct items in this list.
	 */
	public List<T> getDistinct() {
		@SuppressWarnings("unchecked")
		final var distinct = (T[]) new Object[store.itemCount];
		var count = 0;

		for (var item : this) {
			if (!contains(distinct, count, item)) {
				distinct[count] = item;
				++count;
			}
		}

		return new List<>(distinct);
	}

	@Override
	public List<T> reverse() {
		final var reversed = store.reverse();
		return new List<>(reversed);
	}

	@Override
	public List<T> sort(Comparator<T> comparator) {
		final var sorted = store.sort(comparator);
		return new List<>(sorted);
	}

	@Override
	public List<T> shuffle() {
		final var time = System.currentTimeMillis();
		final var random = new Random(time);
		final var shuffled = store.shuffle(random);

		return new List<>(shuffled);
	}

	@Override
	public List<T> iterate(Consumer<T> operation) {
		return (List<T>) IndexedCollection.super.iterate(operation);
	}

	@Override
	public List<T> enumerate(BiConsumer<T, Integer> operation) {
		return (List<T>) IndexedCollection.super.enumerate(operation);
	}

	@Override
	public List<T> filter(Predicate<T> condition) {
		@SuppressWarnings("unchecked")
		final var filtered = (T[]) new Object[store.itemCount];
		var index = 0;

		for (var item : this) {
			if (condition.test(item)) {
				filtered[index] = item;
				++index;
			}
		}

		return new List<>(filtered);
	}

	@Override
	public <R> List<R> convert(Function<T, R> converter) {
		@SuppressWarnings("unchecked")
		final var converted = (R[]) new Object[store.itemCount];
		var index = 0;

		for (var item : this) {
			final var item2 = converter.apply(item);
			if (item2 != null) {
				converted[index] = item2;
				++index;
			}
		}

		return new List<>(converted);
	}

	/**
	 * Returns items of this list as a {@link java.util.List}.
	 */
	public java.util.List<T> bridge() {
		@SuppressWarnings("unchecked")
		final var items = (T[]) Arrays.copyOf(store.items, store.itemCount);
		return Arrays.asList(items);
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<>() {
			private int index = 0;

			@Override
			public boolean hasNext() {
				return index < store.itemCount;
			}

			@Override
			public T next() {
				@SuppressWarnings("unchecked")
				final var item = (T) store.items[index];
				++index;

				return item;
			}
		};
	}

	@Override
	public Iterator<T> reverseIterator() {
		return new Iterator<>() {
			private int index = store.itemCount - 1;

			@Override
			public boolean hasNext() {
				return index > -1;
			}

			@Override
			public T next() {
				@SuppressWarnings("unchecked")
				final var item = (T) store.items[index];
				--index;

				return item;
			}
		};
	}

	@Override
	public Object[] toArray() {
		return Arrays.copyOf(store.items, store.itemCount);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(store.items);
	}

	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof List)) {
			return false;
		}

		@SuppressWarnings("unchecked")
		final var items = (List<T>) object;
		return store.equals(items.store);
	}

	@Override
	public String toString() {
		return "[" + join(", ") + "]";
	}
}

// created on May 26, 2019