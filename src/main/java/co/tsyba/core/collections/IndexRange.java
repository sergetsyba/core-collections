// Created by Serge Tsyba <tsyba@me.com> on Apr 9, 2019.
package co.tsyba.core.collections;

import java.util.Iterator;
import java.util.Objects;

/**
 * A semi-open, contiguous range of indexes in an {@link IndexedCollection}.
 */
public class IndexRange implements Iterable<Integer> {
	/**
	 * Start index of this range.
	 */
	public final int start;

	/**
	 * End index of this range.
	 */
	public final int end;

	/**
	 * Number of indexes in this range.
	 */
	public final int length;

	/**
	 * Creates a new index range with the specified start and end indexes.
	 *
	 * @throws IllegalArgumentException when the specified start index is negative
	 * @throws IllegalArgumentException when the specified end index is less than the
	 * specified start index
	 */
	public IndexRange(int start, int end) {
		if (start < 0) {
			throw new IllegalArgumentException("Cannot create index range with a negative start index: " + start + ".");
		}
		if (end < start) {
			throw new IllegalArgumentException("Cannot create index range with end index before start index: [" + start + ", " + end + "].");
		}

		this.start = start;
		this.end = end;
		this.length = end - start;
	}

	/**
	 * Create an empty index range.
	 */
	public IndexRange() {
		this(0, 0);
	}

	/**
	 * Returns {@code true} when this index range is empty; returns {@code false}
	 * otherwise.
	 */
	public boolean isEmpty() {
		return length == 0;
	}

	/**
	 * Returns {@code true} when the specified index is within this index range; returns
	 * {@code false} otherwise.
	 */
	public boolean contains(int index) {
		return index >= start
			&& index < end;
	}

	/**
	 * Returns {@code true} when the specified {@link IndexRange} is within this index
	 * range; returns {@code false} otherwise.
	 */
	public boolean contains(IndexRange range) {
		return range.start >= start
			&& range.end <= end;
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
}
