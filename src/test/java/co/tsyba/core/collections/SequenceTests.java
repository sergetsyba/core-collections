package co.tsyba.core.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
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
		void returnsCount() {
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
	@DisplayName(".getMinimum()")
	class GetMinimumTests {
		@Test
		@DisplayName("returns minimum item when sequence is not empty")
		void returnsMaxWhenNotEmpty() {
			final var sequence = new TestSequence<>(5, 12, 7, 0, 3, 6);
			final var minimum = sequence.getMinimum(Comparator.naturalOrder());

			assert Optional.of(0)
				.equals(minimum);
		}

		@Test
		@DisplayName("returns empty when sequence is empty")
		void returnsEmptyWhenEmpty() {
			final var sequence = new TestSequence<Integer>();
			final var minimum = sequence.getMinimum(Comparator.naturalOrder());

			assert Optional.empty()
				.equals(minimum);
		}
	}

	@Nested
	@DisplayName(".getMaximum()")
	class GetMaximumTests {
		@Test
		@DisplayName("returns maximum item when sequence is not empty")
		void returnsMaxWhenNotEmpty() {
			final var sequence = new TestSequence<>(5, 12, 7, 0, 3, 6);
			final var maximum = sequence.getMaximum(Comparator.naturalOrder());

			assert Optional.of(12)
				.equals(maximum);
		}

		@Test
		@DisplayName("returns empty when sequence is empty")
		void returnsEmptyWhenEmpty() {
			final var sequence = new TestSequence<Integer>();
			final var maximum = sequence.getMaximum(Comparator.naturalOrder());

			assert Optional.empty()
				.equals(maximum);
		}
	}

	@Nested
	@DisplayName(".matchFirst(Predicate<T>)")
	class MatchFirstTests {
		@Test
		@DisplayName("returns first item when any item matches")
		void returnsItemWhenAnyMatches() {
			final var sequence = new TestSequence<>(9, 3, 7, 8, 5, 2);
			final var match = sequence.matchFirst(item -> item % 2 == 0);

			assert Optional.of(8)
				.equals(match);
		}

		@Test
		@DisplayName("returns empty when no item matches")
		void returnsEmptyWhenNoneMatches() {
			final var sequence = new TestSequence<>(9, 3, 7, 5, 5, 1);
			final var match = sequence.matchFirst(item -> item % 2 == 0);

			assert Optional.empty()
				.equals(match);
		}

		@Test
		@DisplayName("returns empty when no sequence is empty")
		void returnsEmptyWhenEmpty() {
			final var sequence = new TestSequence<>(9, 3, 7, 5, 5, 1);
			final var match = sequence.matchFirst(item -> item % 2 == 0);

			assert Optional.empty()
				.equals(match);
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
		@DisplayName("returns false when any item matches")
		void returnsFalseWhenAnyMatches() {
			final var sequence = new TestSequence<>(5, 7, 1, 4, 9, 2);
			assert !sequence.noneMatches(item -> item % 2 == 0);
		}

		@Test
		@DisplayName("returns false when each item matches")
		void returnsFalseWhenEachMatches() {
			final var sequence = new TestSequence<>(8, 6, 2, 4);
			assert !sequence.noneMatches(item -> item % 2 == 0);
		}

		@Test
		@DisplayName("returns when sequence is empty")
		void returnsTrueWhenEmpty() {
			final var sequence = new TestSequence<Integer>();
			assert sequence.noneMatches(item -> item % 2 == 0);
		}
	}

	@Nested
	@DisplayName(".anyMatches(Predicate<T>)")
	class AnyMatchesTests {
		@Test
		@DisplayName("returns true when any item matches")
		void returnsTrueWhenAnyMatches() {
			final var sequence = new TestSequence<>(5, 7, 1, 4, 9, 2);
			assert sequence.anyMatches(item -> item % 2 == 0);
		}

		@Test
		@DisplayName("returns true when each item matches")
		void returnsTrueWhenEachMatches() {
			final var sequence = new TestSequence<>(8, 6, 2, 4);
			assert sequence.anyMatches(item -> item % 2 == 0);
		}

		@Test
		@DisplayName("returns false when no item matches")
		void returnsFalseWhenNoneMatches() {
			final var sequence = new TestSequence<>(3, 7, 1, 9, 11, 5);
			assert !sequence.anyMatches(item -> item % 2 == 0);
		}

		@Test
		@DisplayName("returns false when sequence is empty")
		void returnsFalseWhenEmpty() {
			final var sequence = new TestSequence<Integer>();
			assert !sequence.anyMatches(item -> item % 2 == 0);
		}
	}

	@Nested
	@DisplayName(".eachMatches(Predicate<T>)")
	class EachMatchesTests {
		@Test
		@DisplayName("returns true when each items matches")
		void returnsTrueWhenEachMatches() {
			final var sequence = new TestSequence<>(8, 6, 2, 4);
			assert sequence.eachMatches(item -> item % 2 == 0);
		}


		@Test
		@DisplayName("returns false when any item matches")
		void returnsFalseWhenAnyMatches() {
			final var sequence = new TestSequence<>(5, 7, 1, 4, 9, 2);
			assert !sequence.eachMatches(item -> item % 2 == 0);
		}

		@Test
		@DisplayName("returns false when no item matches")
		void returnsFalseWhenNoneMatches() {
			final var sequence = new TestSequence<>(3, 7, 1, 9, 11, 5);
			assert !sequence.eachMatches(item -> item % 2 == 0);
		}

		@Test
		@DisplayName("returns true when sequence is empty")
		void returnsTrueWhenEmpty() {
			final var sequence = new TestSequence<Integer>();
			assert sequence.eachMatches(item -> item % 2 == 0);
		}
	}

	@Nested
	@DisplayName(".join(String)")
	class JoinTests {
		@Test
		@DisplayName("joins items into a string when sequence is not empty")
		void joinsWhenNotEmpty() {
			final var sequence = new TestSequence<>("B", "3", "A", "4", "n");
			final var joined = sequence.join(", ");

			assert "B, 3, A, 4, n"
				.equals(joined);
		}

		@Test
		@DisplayName("returns empty string when sequence is empty")
		void doesNothingWhenEmpty() {
			final var sequence = new TestSequence<>();
			final var joined = sequence.join(", ");

			assert ""
				.equals(joined);
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