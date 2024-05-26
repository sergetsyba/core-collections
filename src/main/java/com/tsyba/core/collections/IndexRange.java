package com.tsyba.core.collections;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A semi-open, contiguous range of indexes in a {@link Sequence}.
 */
public class IndexRange implements Sequence<Integer> {
	/**
	 * Start index of this range.
	 */
	public final int start;

	/**
	 * End index of this range.
	 */
	public final int end;

	/**
	 * Creates a new index range with the specified start and end indexes.
	 * <p>
	 * When the specified start and end indexes are equal, creates an empty index range.
	 *
	 * @throws IllegalArgumentException when the specified start index is negative
	 * @throws IllegalArgumentException when the specified end index is less than the
	 * specified start index
	 */
	public IndexRange(int start, int end) {
		if (start < 0) {
			throw new IllegalArgumentException("Cannot create index range with a negative start index: " + start + ".");
		}
		if (start > end) {
			throw new IllegalArgumentException("Cannot create index range with end index before start index: [" + start + ", " + end + "].");
		}

		if (start == end) {
			this.start = 0;
			this.end = 0;
		} else {
			this.start = start;
			this.end = end;
		}
	}

	/**
	 * Creates an empty index range.
	 */
	public IndexRange() {
		this.start = 0;
		this.end = 0;
	}

	@Override
	public int getCount() {
		return end - start;
	}

	public boolean contains(Integer index) {
		return index != null
			&& index >= start
			&& index < end;
	}

	/**
	 * Returns {@code true} when the specified index range is within this index range;
	 * returns {@code false} otherwise.
	 * <p>
	 * When the specified index range is empty, returns {@code true}.
	 */
	public boolean contains(IndexRange range) {
		return range.start == range.end
			|| start <= range.start && range.end <= end;
	}

	@Override
	public Optional<Integer> getFirst() {
		return isEmpty()
			? Optional.empty()
			: Optional.of(start);
	}

	@Override
	public Optional<Integer> getLast() {
		return isEmpty()
			? Optional.empty()
			: Optional.of(end - 1);
	}

	@Override
	public Integer get(int index) {
		final var range = getIndexRange();
		if (!range.contains(index)) {
			throw new IndexNotInRangeException(index, range);
		}

		return start + index;
	}

	@Override
	public IndexRange getPrefix(int index) {
		final var range = getIndexRange();
		if (!range.contains(index)) {
			throw new IndexNotInRangeException(index, range);
		}

		return new IndexRange(start, start + index);
	}

	@Override
	public IndexRange getSuffix(int index) {
		final var range = getIndexRange();
		if (!range.contains(index)) {
			throw new IndexNotInRangeException(index, range);
		}

		return new IndexRange(start + index, end);
	}

	@Override
	public Sequence<Integer> get(IndexRange range) {
		final var validRange = getIndexRange();
		if (!validRange.contains(range)) {
			throw new IndexRangeNotInRangeException(range, validRange);
		}

		return new IndexRange(start + range.start, start + range.end);
	}

	@Override
	public Sequence<Integer> matchAll(Predicate<Integer> condition) {
		final var store = new ContiguousArrayStore(end - start);
		for (var index : this) {
			if (condition.test(index)) {
				store.append(index);
			}
		}

		store.removeExcessCapacity();
		return new List<>(store);
	}

	@Override
	public Optional<Integer> findFirst(Integer item) {
		return contains(item)
			? Optional.of(item - start)
			: Optional.empty();
	}

	@Override
	public Sequence<Integer> find(Integer item) {
		return contains(item)
			? new List<>(item - start)
			: new List<>();
	}

	@Override
	public Sequence<Integer> find(Sequence<Integer> items) {
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
	private boolean contains(Sequence<Integer> items, int index) {
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

	@Override
	public IndexRange getDistinct() {
		return this;
	}

	@Override
	public Sequence<Integer> reverse() {
		// todo: implement IndexRange.reverse()
		throw new UnsupportedOperationException();
	}

	@Override
	public <R> Sequence<R> convert(Function<Integer, R> converter) {
		final var store = new ContiguousArrayStore(end - start);
		for (var index : this) {
			final var item = converter.apply(index);
			store.append(item);
		}

		store.removeExcessCapacity();
		return new List<>(store);
	}

	@Override
	public Iterator<Integer> iterator(int index) {
		final var range = getIndexRange();
		if (!range.contains(index)) {
			throw new IndexNotInRangeException(index, range);
		}

		return new Iterator<>() {
			private int next = start + index;

			@Override
			public boolean hasNext() {
				return next < end;
			}

			@Override
			public Integer next() {
				return next++;
			}
		};
	}

	@Override
	public Iterator<Integer> iterator() {
		return new Iterator<>() {
			private int next = start;

			@Override
			public boolean hasNext() {
				return next < end;
			}

			@Override
			public Integer next() {
				return next++;
			}
		};
	}

	@Override
	public int hashCode() {
		return Objects.hash(start, end);
	}

	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof IndexRange)) {
			return false;
		}

		final var indexRange = (IndexRange) object;
		return start == indexRange.start
			&& end == indexRange.end;
	}

	@Override
	public String toString() {
		return "[" + start + ", " + end + ")";
	}
}

// created on Apr 9, 2019
