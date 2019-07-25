package co.tsyba.core.collections;

import static java.lang.String.format;
import java.util.Iterator;
import java.util.Objects;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Apr 9, 2019.
 */
public class IndexRange implements Iterable<Integer> {
	public static final IndexRange EMPTY = new IndexRange(0, 0);

	public final int start;
	public final int end;

	public IndexRange(int start, int end) {
		if (end < start) {
			final String messsage = format("Invalid index range [%d, %d).", start, end);
			throw new IllegalArgumentException(messsage);
		}

		this.start = start;
		this.end = end;
	}

	public boolean isEmpty() {
		return start > end;
	}

	public int getLength() {
		return end - start;
	}

	public boolean contains(int index) {
		return start <= index
				&& index < end;
	}

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
		return new Iterator<Integer>() {
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
