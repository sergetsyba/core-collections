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
	// todo: remove
	ContiguousArrayStore<T> store = null;
	Object[] store2;

	/**
	 * Creates a copy of the specified {@link Collection}.
	 */
	public List(Collection<T> items) {
		this.store = null;
		this.store2 = items.toArray();
	}

	/**
	 * Creates a list with the specified items.
	 * <p>
	 * Ignores any {@code null} values among the specified items.
	 */
	@SafeVarargs
	public List(T... items) {
		final var store = new Object[items.length];
		var count = 0;

		for (var item : items) {
			if (item != null) {
				store[count] = item;
				++count;
			}
		}

		// trim excess capacity, if necessary
		this.store2 = (count == store.length)
			? store
			: Arrays.copyOf(store, count);
	}

	/**
	 * Creates a list with the specified items.
	 * <p>
	 * Ignores any {@code null} values among the specified items.
	 */
	public List(Iterable<T> items) {
		var store = new Object[64];
		var count = 0;

		for (var item : items) {
			// double capacity when it runs out
			if (count == store.length) {
				store = Arrays.copyOf(store, 2 * store.length);
			}

			if (item != null) {
				store[count] = item;
				++count;
			}
		}

		// trim excess capacity, if necessary
		this.store2 = (count == store.length)
			? store
			: Arrays.copyOf(store, count);
	}

	@Override
	public int getCount() {
		return store2.length;
	}

	public T get(int index) {
		final var range = getIndexRange();
		if (!range.contains(index)) {
			throw new IndexNotInRangeException(index, range);
		}

		@SuppressWarnings("unchecked")
		final var item = (T) store2[index];
		return item;
	}

	public List<T> get(IndexRange indexRange) {
		final var validRange = getIndexRange();
		if (!validRange.contains(indexRange)) {
			throw new IndexRangeNotInRangeException(indexRange, validRange);
		}

		// todo: replace copy with List with preset index range
		final var subStore = Arrays.copyOfRange(store2, indexRange.start, indexRange.end);
		@SuppressWarnings("unchecked")
		final var items = (List<T>) new List<>(subStore);
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
		final var distinct = (T[]) new Object[store2.length];
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
		@SuppressWarnings("unchecked")
		final var reversed = (T[]) new Object[store2.length];
		var index = store2.length - 1;

		for (var item : this) {
			reversed[index] = item;
			--index;
		}

		return new List<>(reversed);
	}

	@Override
	public List<T> sort(Comparator<T> comparator) {
		@SuppressWarnings("unchecked")
		final var sorted = (T[]) Arrays.copyOf(store2, store2.length);
		Arrays.sort(sorted, 0, sorted.length, comparator);

		return new List<>(sorted);
	}

	// Implements in-place version of Fisher-Yates shuffle algorithm.
	//
	// Sources:
	//	1. R. Durstenfeld. "Algorithm 235: Random permutation".
	//		Communications of the ACM, vol. 7, July 1964, p. 420.
	//	2. D. Knuth. "The Art of Computer Programming" vol. 2.
	//		Addison–Wesley, 1969, pp. 139–140, algorithm P.
	@Override
	public List<T> shuffle() {
		@SuppressWarnings("unchecked")
		final var shuffled = (T[]) Arrays.copyOf(store2, store2.length);

		final var time = System.currentTimeMillis();
		final var random = new Random(time);

		// it's more convenient to iterate items backwards for simpler random index
		// generation
		for (var index = store2.length - 1; index >= 0; --index) {
			final var randomIndex = random.nextInt(index + 1);

			// swap items at iterated and randomly generated indices
			final var item = shuffled[index];
			shuffled[index] = shuffled[randomIndex];
			shuffled[randomIndex] = item;
		}

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
		final var filtered = (T[]) new Object[store2.length];
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
		final var converted = (R[]) new Object[store2.length];
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
		final var items = (T[]) Arrays.copyOf(store2, store2.length);
		return Arrays.asList(items);
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<>() {
			private int index = 0;

			@Override
			public boolean hasNext() {
				return index < store2.length;
			}

			@Override
			public T next() {
				@SuppressWarnings("unchecked")
				final var item = (T) store2[index];
				++index;

				return item;
			}
		};
	}

	@Override
	public Iterator<T> reverseIterator() {
		return new Iterator<>() {
			private int index = store2.length - 1;

			@Override
			public boolean hasNext() {
				return index > -1;
			}

			@Override
			public T next() {
				@SuppressWarnings("unchecked")
				final var item = (T) store2[index];
				--index;

				return item;
			}
		};
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(store2);
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
		return Arrays.equals(store2, items.store2);
	}

	@Override
	public String toString() {
		return "[" + join(", ") + "]";
	}
}

// created on May 26, 2019