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

	@Nested
	@DisplayName(".contains(T)")
	class ContainsItemTests {
		@Test
		@DisplayName("returns true when item is present")
		void returnsTrueWhenPresent() {
			final var sequence = new TestSequence<>("t", "d", "5", "V", "A");
			assert sequence.contains("5");
		}

		@Test
		@DisplayName("returns false when item is absent")
		void returnsFalseWhenAbsent() {
			final var sequence = new TestSequence<>("O", "P", "q");
			assert !sequence.contains("5");
		}

		@Test
		@DisplayName("returns false when sequence is empty")
		void returnsFalseWhenEmpty() {
			final var sequence = new TestSequence<>();
			assert !sequence.contains("5");
		}
	}

	@Nested
	@DisplayName(".contains(Sequence<T>)")
	class ContainsItemsTests {
		@Test
		@DisplayName("returns true when all items are present")
		void returnsTrueWhenAllPresent() {
			final var sequence1 = new TestSequence<>("t", "d", "5", "V", "A");
			final var sequence2 = new TestSequence<>("A", "d", "t");

			assert sequence1.contains(sequence2);
		}

		@Test
		@DisplayName("returns false when some items are absent")
		void returnsFalseWhenSomeAbsent() {
			final var sequence1 = new TestSequence<>("O", "P", "q");
			final var sequence2 = new TestSequence<>("P", "0", "O");

			assert !sequence1.contains(sequence2);
		}

		@Test
		@DisplayName("returns false when all items are absent")
		void returnsFalseWhenAllAbsent() {
			final var sequence1 = new TestSequence<>("Y", "f", "E", "3");
			final var sequence2 = new TestSequence<>("N", "R", "P");

			assert !sequence1.contains(sequence2);
		}

		@Test
		@DisplayName("returns false when sequence is empty")
		void returnsFalseWhenEmpty() {
			final var sequence1 = new TestSequence<String>();
			final var sequence2 = new TestSequence<>("G", "Q");

			assert !sequence1.contains(sequence2);
		}

		@Test
		@DisplayName("returns true when items is empty")
		void returnsTrueWhenItemsEmpty() {
			final var sequence1 = new TestSequence<>("A", "t", "I", "P");
			final var sequence2 = new TestSequence<String>();

			assert sequence1.contains(sequence2);
		}

		@Test
		@DisplayName("returns true when sequence and items are empty")
		void returnsTrueWhenBothEmpty() {
			final var sequence1 = new TestSequence<String>();
			final var sequence2 = new TestSequence<String>();

			assert sequence1.contains(sequence2);
		}
	}

	@Nested
	@DisplayName(".noneMatches(Predicate<T>)")
	class NoneMatchesTests {
		@Test
		@DisplayName("returns true when no item matches")
		void returnsTrueWhenNoneMatches() {
			final var sequence = new TestSequence<>(3, 7, 1, 9, 11, 5);
			assert sequence.noneMatches(item -> item % 2 == 0);
		}

		@Test
		@DisplayName("returns false when some item matches")
		void returnsFalseWhenSomeMatches() {
			final var sequence = new TestSequence<>(5, 7, 1, 4, 9, 2);
			assert !sequence.noneMatches(item -> item % 2 == 0);
		}

		@Test
		@DisplayName("returns false when all items match")
		void returnsFalseWhenAllMatch() {
			final var sequence = new TestSequence<>(8, 6, 2, 4);
			assert !sequence.noneMatches(item -> item % 2 == 0);
		}

		@Test
		@DisplayName("returns false when some item matches")
		void returnsTrueWhenEmpty() {
			final var sequence = new TestSequence<Integer>();
			assert sequence.noneMatches(item -> item % 2 == 0);
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