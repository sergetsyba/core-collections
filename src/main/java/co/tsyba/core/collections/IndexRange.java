// Created by Serge Tsyba <tsyba@me.com> on Apr 9, 2019.
package co.tsyba.core.collections;

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

	@Override
	public IndexRange getIndexRange() {
		return this;
	}

	public boolean contains(Integer index) {
		return index >= start
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
		if (!contains(index)) {
			throw new IndexNotInRangeException(index, this);
		}

		return index;
	}

	@Override
	public IndexRange getPrefix(int index) {
		if (!contains(index)) {
			throw new IndexNotInRangeException(index, this);
		}

		return new IndexRange(start, index);
	}

	@Override
	public IndexRange getSuffix(int index) {
		if (!contains(index)) {
			throw new IndexNotInRangeException(index, this);
		}

		return new IndexRange(index, end);
	}

	@Override
	public Sequence<Integer> get(IndexRange range) {
		if (!contains(range)) {
			throw new IndexRangeNotInRangeException(range, this);
		}

		return range;
	}

	@Override
	public Optional<Integer> findFirst(Integer item) {
		return contains(item)
			? Optional.of(item)
			: Optional.empty();
	}



	@Override
	public Sequence<Integer> reverse() {
		// todo: implement IndexRange.reverse()
		throw new UnsupportedOperationException();
	}

	@Override
	public Sequence<Integer> filter(Predicate<Integer> condition) {
		// todo: implement IndexRange.filter(Predicate<Integer>)
		throw new UnsupportedOperationException();
	}

	@Override
	public <R> Sequence<R> convert(Function<Integer, R> converter) {
		// todo: implement IndexRange.convert(Function<Integer, R>)
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Integer> iterator() {
		return new Iterator<>() {
			private int index = start;

			@Override
			public boolean hasNext() {
				return index < end;
			}

			@Override
			public Integer next() {
				final var next = index;
				index += 1;

				return next;
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
