package co.tsyba.core.collections;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An immutable {@link Sequence}, which provides efficient, random-access to its items.
 */
public class List<T> implements Sequence<T> {
	ContiguousArrayStore store;

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
	public List<T> getPrefix(int index) {
		final var validRange = getIndexRange();
		if (!validRange.contains(index)) {
			throw new IndexNotInRangeException(index, validRange);
		}

		final var prefix = store.get(index, 0, index);
		return new List<>(prefix);
	}

	@Override
	public List<T> getSuffix(int index) {
		final var validRange = getIndexRange();
		if (!validRange.contains(index)) {
			throw new IndexNotInRangeException(index, validRange);
		}

		final var capacity = store.itemCount - index;
		final var suffix = store.get(capacity, index, store.itemCount);
		return new List<>(suffix);
	}

	@Override
	public List<T> get(IndexRange indexRange) {
		final var validRange = getIndexRange();
		if (!validRange.contains(indexRange)) {
			throw new IndexRangeNotInRangeException(indexRange, validRange);
		}

		final var count = indexRange.getCount();
		final var sub = store.get(count, indexRange.start, indexRange.end);
		return new List<>(sub);
	}

	@Override
	public List<Integer> find(T item) {
		final var indexes = new MutableList<Integer>();
		enumerate((index, item2) -> {
			if (item2.equals(item)) {
				indexes.append(index);
			}
		});

		return indexes.toImmutable();
	}

	@Override
	public Sequence<Integer> find(Sequence<T> items) {
		final var indexes = new MutableList<Integer>();
		final var range = getIndexRange();

		for (var index : range) {
			if (contains(items, index)) {
				indexes.append(index);
			}
		}

		return indexes.toImmutable();
	}

	/**
	 * Returns {@code true} when the specified {@link Sequence} occurs at the specified
	 * index in this list; returns {@code false} otherwise.
	 */
	private boolean contains(Sequence<T> items, int index) {
		final var iterator1 = iterator(index);
		final var iterator2 = items.iterator();

		while (iterator1.hasNext() && iterator2.hasNext()) {
			final var item1 = iterator1.next();
			final var item2 = iterator2.next();

			if (!item1.equals(item2)) {
				return false;
			}
		}

		// verify all items in the argument sequence have been compared
		return !iterator2.hasNext();
	}

	/**
	 * Returns distinct items in this list.
	 */
	public List<T> getDistinct() {
		final var distinct = new MutableSet<>(this);
		return new List<>(distinct);
	}

	@Override
	public List<T> reverse() {
		final var reversed = store.reverse();
		return new List<>(reversed);
	}

	@Override
	public List<T> iterate(Consumer<T> operation) {
		return (List<T>) Sequence.super.iterate(operation);
	}

	@Override
	public List<T> enumerate(BiConsumer<Integer, T> operation) {
		return (List<T>) Sequence.super.enumerate(operation);
	}

	@Override
	public List<T> filter(Predicate<T> condition) {
		final var filtered = new MutableList<T>();
		for (var item : this) {
			if (condition.test(item)) {
				filtered.append(item);
			}
		}

		return filtered.toImmutable();
	}

	@Override
	public <R> List<R> convert(Function<T, R> converter) {
		final var converted = new MutableList<R>();
		for (var item : this) {
			converted.append(
				converter.apply(item));
		}

		return converted.toImmutable();
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
	public Object[] toArray() {
		return Arrays.copyOf(store.items, store.itemCount);
	}

	@Override
	public T[] toArray(Class<? extends T[]> klass) {
		return Arrays.copyOf(store.items, store.itemCount, klass);
	}

	@Override
	public Iterator<T> iterator(int start) {
		return new Iterator<>() {
			private int index = start;

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
	public Iterator<T> iterator() {
		return iterator(0);
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