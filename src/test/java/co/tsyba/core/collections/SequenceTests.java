package co.tsyba.core.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;

public class SequenceTests {
	@Nested
	@DisplayName(".isEmpty()")
	class IsEmptyTests {
		@Test
		@DisplayName("returns true when empty")
		void returnsTrueWhenEmpty() {
			final var sequence = new TestSequence<>();
			assert sequence.isEmpty();
		}

		@Test
		@DisplayName("returns false when not empty")
		void returnsFalseWhenNotEmpty() {
			final var sequence = new TestSequence<>("f", "T", "q", "r");
			assert !sequence.isEmpty();
		}
	}

	@Nested
	@DisplayName(".getCount()")
	class GetCountTests {
		@Test
		@DisplayName("returns item count")
		void returnsItemCount() {
			final var sequence = new TestSequence<>("b", "5", "F", "e");
			final var count = sequence.getCount();
			assert 4 == count;
		}

		@Test
		@DisplayName("returns 0 when empty")
		void returnsZeroWhenEmpty() {
			final var sequence = new TestSequence<>();
			final var count = sequence.getCount();
			assert 0 == count;
		}
	}
}

class TestSequence<T> implements Sequence<T> {
	private final Object[] items;

	@SafeVarargs
	public TestSequence(T... items) {
		this.items = items;
	}

	@Override
	public Sequence<T> getDistinct() {
		return null;
	}

	@Override
	public IndexedCollection<T> sort(Comparator<T> comparator) {
		return null;
	}

	@Override
	public Sequence<T> filter(Predicate<T> condition) {
		return null;
	}

	@Override
	public <R> Sequence<R> convert(Function<T, R> converter) {
		return null;
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<>() {
			private int index = 0;

			@Override
			public boolean hasNext() {
				return index < items.length;
			}

			@Override
			public T next() {
				@SuppressWarnings("unchecked")
				var item = (T) items[index];
				++index;
				return item;
			}
		};
	}
}