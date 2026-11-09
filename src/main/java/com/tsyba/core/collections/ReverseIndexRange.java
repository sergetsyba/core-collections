package com.tsyba.core.collections;

import java.util.Iterator;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * A semi-open, contiguous range of indexes, iterated in reverse order.
 */
class ReverseIndexRange extends IndexRange {
	ReverseIndexRange(int start, int end) {
		super(start, end);
	}

	/**
	 * Parses {@link ReverseIndexRange} from the specified string.
	 *
	 * @throws IllegalArgumentException when the specified string does not contain a
	 * properly formatted string representation of an {@link ReverseIndexRange}; or when
	 * the specified string contains a {@link ReverseIndexRange} with its start index
	 * after its end index
	 */
	public static ReverseIndexRange parse(String string) throws IllegalArgumentException {
		final var pattern = Pattern.compile("\\(\\s*(\\d+),\\s*(\\d+)\\s*\\]");
		final var matcher = pattern.matcher(string);

		if (matcher.find()) {
			final var start = matcher.group(2);
			final var end = matcher.group(1);

			return new ReverseIndexRange(
				Integer.parseInt(start),
				Integer.parseInt(end));
		} else {
			throw new IllegalArgumentException("Unsupported reverse index range format: " + string + ".");
		}
	}

	@Override
	public Optional<Integer> getFirst() {
		return isEmpty()
			? Optional.empty()
			: Optional.of(end - 1);
	}

	@Override
	public Optional<Integer> getLast() {
		return isEmpty()
			? Optional.empty()
			: Optional.of(start);
	}

	@Override
	public Integer get(int index) {
		final var validRange = getIndexRange();
		if (!validRange.contains(index)) {
			throw new IndexNotInRangeException(index, validRange);
		}

		return end - 1 - index;
	}

	@Override
	public ReverseIndexRange getPrefix(int index) {
		final var validRange = getIndexRange();
		if (!validRange.contains(index)) {
			throw new IndexNotInRangeException(index, validRange);
		}

		return new ReverseIndexRange(end - index, end);
	}

	@Override
	public ReverseIndexRange getSuffix(int index) {
		final var validRange = getIndexRange();
		if (!validRange.contains(index)) {
			throw new IndexNotInRangeException(index, validRange);
		}

		return new ReverseIndexRange(start, end - index);
	}

	@Override
	public ReverseIndexRange get(IndexRange indexRange) {
		final var validRange = getIndexRange();
		if (!validRange.contains(indexRange)) {
			throw new IndexRangeNotInRangeException(indexRange, validRange);
		}

		return new ReverseIndexRange(
			end - indexRange.end, end - indexRange.start);
	}

	@Override
	public Optional<Integer> findFirst(Integer item) {
		return contains(item)
			? Optional.of(end - 1 - item)
			: Optional.empty();
	}

	@Override
	public Sequence<Integer> findAll(Integer item) {
		return contains(item)
			? new List<>(end - 1 - item)
			: new List<>();
	}

	@Override
	public Iterator<Integer> iterator() {
		return new Iterator<>() {
			private int next = end - 1;

			@Override
			public boolean hasNext() {
				return next >= start;
			}

			@Override
			public Integer next() {
				return next--;
			}
		};
	}

	@Override
	public Iterator<Integer> iterator(int index) {
		final var validRange = getIndexRange();
		if (!validRange.contains(index)) {
			throw new IndexNotInRangeException(index, validRange);
		}

		return new Iterator<>() {
			private int next = end - 1 - index;

			@Override
			public boolean hasNext() {
				return next >= start;
			}

			@Override
			public Integer next() {
				return next--;
			}
		};
	}

	@Override
	public IndexRange reverse() {
		return new IndexRange(start, end);
	}

	@Override
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}
		if (object == this) {
			return true;
		}

		final var klass = object.getClass();
		if (!getClass().equals(klass)) {
			return false;
		}

		final var indexRange = (ReverseIndexRange) object;
		return start == indexRange.start
			&& end == indexRange.end;
	}

	@Override
	public String toString() {
		return "(" + end + ", " + start + "]";
	}
}