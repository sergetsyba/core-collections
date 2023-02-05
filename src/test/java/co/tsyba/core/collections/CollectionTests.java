package co.tsyba.core.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class CollectionTests {
	@Nested
	@DisplayName(".isEmpty()")
	class IsEmptyTests {
		@Test
		@DisplayName("returns true when collection is empty")
		void returnsTrueWhenEmpty() {
			final var items = new TestCollection<>();
			assert items.isEmpty();
		}

		@Test
		@DisplayName("returns false when collection is not empty")
		void returnsFalseWhenNotEmpty() {
			final var items = new TestCollection<>("f", "T", "q", "r");
			assert !items.isEmpty();
		}
	}

	@Nested
	@DisplayName(".getCount()")
	class GetCountTests {
		@Test
		@DisplayName("returns item count when collection is not empty")
		void returnsCountWhenNotEmpty() {
			final var items = new TestCollection<>("b", "5", "F", "e");
			final var count = items.getCount();

			assert 4 == count;
		}

		@Test
		@DisplayName("returns 0 when collection is empty")
		void returnsZeroWhenEmpty() {
			final var items = new TestCollection<>();
			final var count = items.getCount();

			assert 0 == count;
		}
	}

	@Nested
	@DisplayName(".getMinimum()")
	class GetMinimumTests {
		@Test
		@DisplayName("returns minimum item when collection is not empty")
		void returnsMinWhenNotEmpty() {
			final var items = new TestCollection<>(5, 12, 7, 0, 3, 6);
			final var minimum = items.getMinimum(Comparator.naturalOrder());

			assert Optional.of(0)
				.equals(minimum);
		}

		@Test
		@DisplayName("returns empty when collection is empty")
		void returnsEmptyWhenEmpty() {
			final var items = new TestCollection<Integer>();
			final var minimum = items.getMinimum(Comparator.naturalOrder());

			assert minimum.isEmpty();
		}
	}

	@Nested
	@DisplayName(".getMaximum()")
	class GetMaximumTests {
		@Test
		@DisplayName("returns maximum item when collection is not empty")
		void returnsMaxWhenNotEmpty() {
			final var items = new TestCollection<>(5, 12, 7, 0, 3, 6);
			final var maximum = items.getMaximum(Comparator.naturalOrder());

			assert Optional.of(12)
				.equals(maximum);
		}

		@Test
		@DisplayName("returns empty when collection is empty")
		void returnsEmptyWhenEmpty() {
			final var items = new TestCollection<Integer>();
			final var maximum = items.getMaximum(Comparator.naturalOrder());

			assert maximum.isEmpty();
		}
	}

	@Nested
	@DisplayName(".contains(T)")
	class ContainsTests {
		@Test
		@DisplayName("returns true when item is present")
		void returnsTrueWhenPresent() {
			final var items = new TestCollection<>("t", "d", "5", "V", "A");
			assert items.contains("5");
		}

		@Test
		@DisplayName("returns false when item is absent")
		void returnsFalseWhenAbsent() {
			final var items = new TestCollection<>("O", "P", "q");
			assert !items.contains("5");
		}

		@Test
		@DisplayName("returns false when collection is empty")
		void returnsFalseWhenEmpty() {
			final var items = new TestCollection<>();
			assert !items.contains("5");
		}
	}

	@Nested
	@DisplayName(".contains(Collection<T>)")
	class ContainsCollectionTests {
		@Test
		@DisplayName("returns true when all items are present")
		void returnsTrueWhenAllPresent() {
			final var items1 = new TestCollection<>("t", "d", "5", "V", "A");
			final var items2 = new TestCollection<>("A", "d", "t");

			assert items1.contains(items2);
		}

		@Test
		@DisplayName("returns false when some items are absent")
		void returnsFalseWhenSomeAbsent() {
			final var items1 = new TestCollection<>("O", "P", "q");
			final var items2 = new TestCollection<>("P", "0", "O");

			assert !items1.contains(items2);
		}

		@Test
		@DisplayName("returns false when all items are absent")
		void returnsFalseWhenAllAbsent() {
			final var items1 = new TestCollection<>("Y", "f", "E", "3");
			final var items2 = new TestCollection<>("N", "R", "P");

			assert !items1.contains(items2);
		}

		@Test
		@DisplayName("returns false when collection is empty")
		void returnsFalseWhenEmpty() {
			final var items1 = new TestCollection<String>();
			final var items2 = new TestCollection<>("G", "Q");

			assert !items1.contains(items2);
		}

		@Test
		@DisplayName("returns true when items is empty")
		void returnsTrueWhenItemsEmpty() {
			final var items1 = new TestCollection<>("A", "t", "I", "P");
			final var items2 = new TestCollection<String>();

			assert items1.contains(items2);
		}

		@Test
		@DisplayName("returns true when collection and items are empty")
		void returnsTrueWhenBothEmpty() {
			final var items1 = new TestCollection<String>();
			final var items2 = new TestCollection<String>();

			assert items1.contains(items2);
		}
	}

	@Nested
	@DisplayName(".match(Predicate<T>)")
	class MatchTests {
		@Test
		@DisplayName("returns matched item when item matches")
		void returnsItemWhenAnyMatches() {
			final var items = new TestCollection<>(9, 3, 7, 8, 5, 2);
			final var match = items.match((item) ->
				item % 2 == 0);

			assert Optional.of(8)
				.equals(match);
		}

		@Test
		@DisplayName("returns empty when no item matches")
		void returnsEmptyWhenNoneMatches() {
			final var items = new TestCollection<>(9, 3, 7, 5, 5, 1);
			final var match = items.match((item) ->
				item % 2 == 0);

			assert match.isEmpty();
		}

		@Test
		@DisplayName("returns empty when collection is empty")
		void returnsEmptyWhenEmpty() {
			final var items = new TestCollection<Integer>();
			final var match = items.match((item) ->
				item % 2 == 0);

			assert match.isEmpty();
		}
	}

	@Nested
	@DisplayName(".noneMatches(Predicate<T>)")
	class NoneMatchesTests {
		@Test
		@DisplayName("returns true when no item matches")
		void returnsTrueWhenNoneMatches() {
			final var items = new TestCollection<>(3, 7, 1, 9, 11, 5);
			assert items.noneMatches((item) ->
				item % 2 == 0);
		}

		@Test
		@DisplayName("returns false when any item matches")
		void returnsFalseWhenAnyMatches() {
			final var items = new TestCollection<>(5, 7, 1, 4, 9, 2);
			assert !items.noneMatches((item) ->
				item % 2 == 0);
		}

		@Test
		@DisplayName("returns false when each item matches")
		void returnsFalseWhenEachMatches() {
			final var items = new TestCollection<>(8, 6, 2, 4);
			assert !items.noneMatches((item) ->
				item % 2 == 0);
		}

		@Test
		@DisplayName("returns true when collection is empty")
		void returnsTrueWhenEmpty() {
			final var items = new TestCollection<Integer>();
			assert items.noneMatches((item) ->
				item % 2 == 0);
		}
	}

	@Nested
	@DisplayName(".anyMatches(Predicate<T>)")
	class AnyMatchesTests {
		@Test
		@DisplayName("returns true when any item matches")
		void returnsTrueWhenAnyMatches() {
			final var items = new TestCollection<>(5, 7, 1, 4, 9, 2);
			assert items.anyMatches((item) ->
				item % 2 == 0);
		}

		@Test
		@DisplayName("returns true when each item matches")
		void returnsTrueWhenEachMatches() {
			final var items = new TestCollection<>(8, 6, 2, 4);
			assert items.anyMatches((item) ->
				item % 2 == 0);
		}

		@Test
		@DisplayName("returns false when no item matches")
		void returnsFalseWhenNoneMatches() {
			final var items = new TestCollection<>(3, 7, 1, 9, 11, 5);
			assert !items.anyMatches((item) ->
				item % 2 == 0);
		}

		@Test
		@DisplayName("returns false when collection is empty")
		void returnsFalseWhenEmpty() {
			final var items = new TestCollection<Integer>();
			assert !items.anyMatches((item) ->
				item % 2 == 0);
		}
	}

	@Nested
	@DisplayName(".eachMatches(Predicate<T>)")
	class EachMatchesTests {
		@Test
		@DisplayName("returns true when each items matches")
		void returnsTrueWhenEachMatches() {
			final var items = new TestCollection<>(8, 6, 2, 4);
			assert items.eachMatches((item) ->
				item % 2 == 0);
		}


		@Test
		@DisplayName("returns false when any item matches")
		void returnsFalseWhenAnyMatches() {
			final var items = new TestCollection<>(5, 7, 1, 4, 9, 2);
			assert !items.eachMatches((item) ->
				item % 2 == 0);
		}

		@Test
		@DisplayName("returns false when no item matches")
		void returnsFalseWhenNoneMatches() {
			final var items = new TestCollection<>(3, 7, 1, 9, 11, 5);
			assert !items.eachMatches((item) ->
				item % 2 == 0);
		}

		@Test
		@DisplayName("returns true when collection is empty")
		void returnsTrueWhenEmpty() {
			final var items = new TestCollection<Integer>();
			assert items.eachMatches((item) ->
				item % 2 == 0);
		}
	}

	@Nested
	@DisplayName(".iterate(Consumer<T>)")
	class IterateTests {
		@Test
		@DisplayName("iterates when collection is not empty")
		void iteratesWhenNotEmpty() {
			final var items = new TestCollection<>(3, 7, 1, 2, 3, 0);
			final var iterated = new LinkedList<Integer>();
			items.iterate(iterated::add);

			assert java.util.List.of(3, 7, 1, 2, 3, 0)
				.equals(iterated);
		}

		@Test
		@DisplayName("does nothing when collection is empty")
		void doesNothingWhenEmpty() {
			final var items = new TestCollection<Integer>();
			final var iterated = new LinkedList<Integer>();
			items.iterate(iterated::add);

			assert java.util.List.of()
				.equals(iterated);
		}
	}

	@Nested
	@DisplayName(".combine(R, BiFunction<R, T, R>)")
	class CombineTests {
		@Test
		@DisplayName("combines when collection is not empty")
		void combinesWhenNotEmpty() {
			final var items = new TestCollection<>(5, 3, 2, 9, 4, 7);
			final var sum = items.combine(4, Integer::sum);

			assert 34 == sum;
		}

		@Test
		@DisplayName("does nothing when collection is empty")
		void doesNothingWhenEmpty() {
			final var items = new TestCollection<Integer>();
			final var sum = items.combine(4, Integer::sum);

			assert 4 == sum;
		}
	}

	@Nested
	@DisplayName(".join(String)")
	class JoinTests {
		@Test
		@DisplayName("joins items into a string when collection is not empty")
		void joinsWhenNotEmpty() {
			final var items = new TestCollection<>("B", "3", "A", "4", "n");
			final var joined = items.join(", ");

			assert "B, 3, A, 4, n"
				.equals(joined);
		}

		@Test
		@DisplayName("returns empty string when collection is empty")
		void doesNothingWhenEmpty() {
			final var items = new TestCollection<>();
			final var joined = items.join(", ");

			assert ""
				.equals(joined);
		}
	}

	static class TestCollection<T> implements Collection<T> {
		private final Object[] items;

		@SafeVarargs
		public TestCollection(T... items) {
			this.items = items;
		}

		@Override
		public Collection<T> getDistinct() {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<T> sort(Comparator<T> comparator) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Collection<T> filter(Predicate<T> condition) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <R> Collection<R> convert(Function<T, R> converter) {
			throw new UnsupportedOperationException();
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
}

// created on Jul 7, 2019
