// Created by Serge Tsyba <tsyba@me.com> on Apr 9, 2019.
package co.tsyba.core.collections;

import java.util.Iterator;
import java.util.Objects;

/**
 * A contiguous range of indexes in an {@link IndexedCollection}.
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
	 * @throws IllegalArgumentException when the specified end index is negative
	 * @throws IllegalArgumentException when the specified start index is greater than or equal to
	 * 	the specified end index
	 */
	public IndexRange(int start, int end) {
		if (start < 0) {
			throw new IllegalArgumentException("Cannot create an index range with a negative start index: " + start + ".");
		}
		if (end < 0) {
			throw new IllegalArgumentException("Cannot create an index range with a negative end index: " + end + ".");
		}
		if (start == end) {
			throw new IllegalArgumentException("Cannot create an empty index range: [" + start + ", " + end + "].");
		}
		if (start > end) {
			throw new IllegalArgumentException("Cannot create an index range of negative length: [" + start + ", " + end + "].");
		}

		this.start = start;
		this.end = end;
		this.length = end - start + 1;
	}

	/**
	 * Returns {@code true} when the specified index is within this index range; returns
	 * {@code false} otherwise.
	 */
	public boolean contains(int index) {
		return start <= index
			&& index < end;
	}

	/**
	 * Returns {@code true} when the specified {@link IndexRange} is within this index range;
	 * returns {@code false} otherwise.
	 */
	public boolean contains(IndexRange indexRange) {
		return start <= indexRange.start
			&& indexRange.end <= end;
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
	public Iterator<Integer> iterator() {
		return new Iterator<>() {
			private int index = start;

			@Override
			public boolean hasNext() {
				return index <= end;
			}

			@Override
			public Integer next() {
				final var nextIndex = index;
				index += 1;

				return nextIndex;
			}
		};
	}
}
