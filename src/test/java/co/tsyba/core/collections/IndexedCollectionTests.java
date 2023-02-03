package co.tsyba.core.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class IndexedCollectionTests {
	@Nested
	@DisplayName(".getIndexRange()")
	class GetIndexRangeTests {
		@Test
		@DisplayName("when collection is not empty, returns index range")
		void returnsIndexRangeWhenNotEmpty() {
			final var items = new TestCollection<>(6, 3, 2, 1, 3, 0);
			final var range = items.getIndexRange();

			assert new IndexRange(0, 6)
				.equals(range);
		}

		@Test
		@DisplayName("when collection is empty, returns empty index range")
		void returnsEmptyIndexRangeWhenEmpty() {
			final var items = new TestCollection<>();
			final var range = items.getIndexRange();

			assert new IndexRange(0, 0)
				.equals(range);
		}
	}

	@Nested
	@DisplayName(".get(int)")
	class GetTests {
		@Nested
		@DisplayName("when collection is not empty")
		class NotEmptyCollectionTests {
			final IndexedCollection<Integer> items = new TestCollection<>(
				4, 5, 2, 1, 0, 5, 7);

			@Test
			@DisplayName("when index is within valid range, returns item")
			void returnsItemWhenIndexWithinValidRange() {
				final var item = items.get(4);
				assert 0 == item;
			}

			@Test
			@DisplayName("when index is before valid range, fails")
			void failsWhenIndexBeforeValidRange() {
				try {
					items.get(-4);
				} catch (IndexNotInRangeException exception) {
					assert -4 == exception.index;
					assert new IndexRange(0, 7)
						.equals(exception.indexRange);

					return;
				}
				assert false;
			}

			@Test
			@DisplayName("when index is after valid range, fails")
			void failsWhenIndexAfterValidRange() {
				try {
					items.get(9);
				} catch (IndexNotInRangeException exception) {
					assert 9 == exception.index;
					assert new IndexRange(0, 7)
						.equals(exception.indexRange);

					return;
				}
				assert false;
			}
		}

		@Nested
		@DisplayName("when collection is empty")
		class EmptyCollectionTests {
			private final IndexedCollection<Integer> items = new TestCollection<>();

			@Test
			@DisplayName("fails")
			void failsWhenEmpty() {
				try {
					items.get(0);
				} catch (IndexNotInRangeException exception) {
					assert 0 == exception.index;
					assert new IndexRange(0, 0)
						.equals(exception.indexRange);

					return;
				}
				assert false;
			}
		}
	}

	@Nested
	@DisplayName(".getFirst()")
	class GetFirstTests {
		@Test
		@DisplayName("when collection is not empty, returns first item")
		void returnsFirstWhenNotEmpty() {
			final var items = new TestCollection<>("B", "V", "4");
			final var first = items.getFirst();

			assert Optional.of("B")
				.equals(first);
		}

		@Test
		@DisplayName("when collection is empty, returns empty")
		void returnsEmptyWhenEmpty() {
			final var items = new TestCollection<String>();
			final var first = items.getFirst();

			assert Optional.empty()
				.equals(first);
		}
	}

	@Nested
	@DisplayName(".getLast()")
	class GetLastTests {
		@Test
		@DisplayName("when collection is not empty, returns last item")
		void returnsLastWhenNotEmpty() {
			final var items = new TestCollection<>("B", "V", "4");
			final var last = items.getLast();

			assert Optional.of("4")
				.equals(last);
		}

		@Test
		@DisplayName("when collection is empty, returns empty")
		void returnsEmptyWhenEmpty() {
			final var items = new TestCollection<String>();
			final var last = items.getFirst();

			assert Optional.empty()
				.equals(last);
		}
	}

	@Nested
	@DisplayName(".getPrefix(int)")
	class GetPrefixTests {
		@Nested
		@DisplayName("when collection is not empty")
		class NotEmptyCollectionTests {
			private final IndexedCollection<String> items = new TestCollection<>(
				"B", "d", "R", "f", "a", "Q");

			@Test
			@DisplayName("when index is in valid range, returns prefix")
			void returnsPrefixWhenIndexInRange() {
				final var prefix = (TestCollection<String>) items.getPrefix(4);
				assert Arrays.equals(prefix.items,
					new String[]{
						"B", "d", "R", "f"
					});
			}

			@Test
			@DisplayName("when index is 0, returns empty collection")
			void returnsEmptyWhenIndexZero() {
				final var prefix = (TestCollection<String>) items.getPrefix(0);
				assert 0 == prefix.items.length;
			}

			@Test
			@DisplayName("when index is before valid range, fails")
			void failsWhenIndexBeforeValidRange() {
				try {
					items.getPrefix(-1);
				} catch (IndexNotInRangeException exception) {
					assert -1 == exception.index;
					assert new IndexRange(0, 6)
						.equals(exception.indexRange);

					return;
				}
				assert false;
			}

			@Test
			@DisplayName("when index is after valid range, fails")
			void failsWhenIndexAfterValidRange() {
				try {
					items.getPrefix(9);
				} catch (IndexNotInRangeException exception) {
					assert 9 == exception.index;
					assert new IndexRange(0, 6)
						.equals(exception.indexRange);

					return;
				}
				assert false;
			}
		}

		@Nested
		@DisplayName("when collection is empty")
		class EmptyCollectionTests {
			private final IndexedCollection<String> items = new TestCollection<>();

			@Test
			@DisplayName("fails")
			void fails() {
				try {
					items.getPrefix(0);
				} catch (IndexNotInRangeException exception) {
					assert 0 == exception.index;
					assert new IndexRange(0, 0)
						.equals(exception.indexRange);

					return;
				}
				assert false;
			}
		}
	}

	@Nested
	@DisplayName(".getSuffix(int)")
	class GetSuffixTests {
		@Nested
		@DisplayName("when collection is not empty")
		class NotEmptyCollectionTests {
			private final IndexedCollection<String> items = new TestCollection<>(
				"B", "d", "R", "f", "a", "Q");

			@Test
			@DisplayName("when index is in valid range, returns suffix")
			void returnsPrefixWhenIndexInRange() {
				final var suffix = (TestCollection<String>) items.getSuffix(3);
				assert Arrays.equals(suffix.items,
					new String[]{
						"f", "a", "Q"
					});
			}

			@Test
			@DisplayName("when index is before valid range, fails")
			void failsWhenIndexBeforeValidRange() {
				try {
					items.getSuffix(-1);
				} catch (IndexNotInRangeException exception) {
					assert -1 == exception.index;
					assert new IndexRange(0, 6)
						.equals(exception.indexRange);

					return;
				}
				assert false;
			}

			@Test
			@DisplayName("when index is after valid range, fails")
			void failsWhenIndexAfterValidRange() {
				try {
					items.getSuffix(6);
				} catch (IndexNotInRangeException exception) {
					assert 6 == exception.index;
					assert new IndexRange(0, 6)
						.equals(exception.indexRange);

					return;
				}
				assert false;
			}
		}

		@Nested
		@DisplayName("when collection is empty")
		class EmptyCollectionTests {
			private final IndexedCollection<String> items = new TestCollection<>();

			@Test
			@DisplayName("fails")
			void fails() {
				try {
					items.getSuffix(0);
				} catch (IndexNotInRangeException exception) {
					assert 0 == exception.index;
					assert new IndexRange(0, 0)
						.equals(exception.indexRange);

					return;
				}
				assert false;
			}
		}
	}

	@Nested
	@DisplayName(".contains(IndexedCollection<T>)")
	class ContainsCollectionTests {
		private final IndexedCollection<String> items = new TestCollection<>(
			"f", "G", "D", "3", "a");

		@Nested
		@DisplayName("when collection is not empty")
		class NotEmptyCollectionTests {
			@Test
			@DisplayName("when all items are present in same order, returns true")
			void returnsTrueWhenAllPresentInSameOrder() {
				final var items2 = new TestCollection<>("D", "3");
				assert items.contains(items2);
			}

			@Test
			@DisplayName("when all items are present in different order, returns false")
			void returnsFalseWhenAllPresentInDifferentOrder() {
				final var items2 = new TestCollection<>("f", "D");
				assert !items.contains(items2);
			}

			@Test
			@DisplayName("when some items are absent, returns false")
			void returnsFalseWhenSomeAbsent() {
				final var items2 = new TestCollection<>("D", "5");
				assert !items.contains(items2);
			}

			@Test
			@DisplayName("when all items are absent, returns false")
			void returnsFalseWhenAllAbsent() {
				final var items2 = new TestCollection<>("H", "7", "e", "Q");
				assert !items.contains(items2);
			}

			@Test
			@DisplayName("when items are empty, returns true")
			void returnsTrueWhenItemsEmpty() {
				final var items2 = new TestCollection<String>();
				assert items.contains(items2);
			}
		}

		@Nested
		@DisplayName("when collection is empty")
		class EmptyCollectionTests {
			private final IndexedCollection<String> items = new TestCollection<>();

			@Test
			@DisplayName("when items are not empty, returns false")
			void returnsFalseWhenEmpty() {
				final var items2 = new TestCollection<>("G", "p", "Q");
				assert !items.contains(items2);
			}

			@Test
			@DisplayName("when items are empty, returns true")
			void returnsTrueWhenEmptyAndItemsEmpty() {
				final var items2 = new TestCollection<String>();
				assert items.contains(items2);
			}
		}
	}

	@Nested
	@DisplayName(".find(T)")
	class FindTests {
		private final IndexedCollection<Integer> items = new TestCollection<>(
			9, 3, 7, 8, 5, 2);

		@Nested
		@DisplayName("when collection is not empty")
		class NotEmptyCollectionTests {
			@Test
			@DisplayName("when item is present, returns item index")
			void returnsIndexWhenPresent() {
				final var index = items.find(8);
				assert Optional.of(3)
					.equals(index);
			}

			@Test
			@DisplayName("when item is absent, returns empty")
			void returnsEmptyWhenAbsent() {
				final var index = items.find(0);
				assert index.isEmpty();
			}
		}

		@Nested
		@DisplayName("when collection is empty")
		class EmptyCollectionTests {
			private final IndexedCollection<Integer> items = new TestCollection<>();

			@Test
			@DisplayName("returns empty")
			void returnsEmptyWhenEmpty() {
				final var index = items.find(6);
				assert index.isEmpty();
			}
		}
	}

	@Nested
	@DisplayName(".match(Predicate<T>)")
	class MatchTests {
		private final IndexedCollection<Integer> items = new TestCollection<>(
			9, 3, 7, 8, 5, 2);

		@Nested
		@DisplayName("when collection is not empty")
		class NotEmptyCollectionTests {
			@Test
			@DisplayName("when item matches, returns matched item")
			void returnsItemWhenAnyMatches() {
				final var match = items.match((item) ->
					item % 2 == 0);

				assert Optional.of(8)
					.equals(match);
			}

			@Test
			@DisplayName("when no item matches, returns empty")
			void returnsEmptyWhenNoneMatches() {
				final var match = items.match((item) ->
					item % 20 == 0);

				assert match.isEmpty();
			}
		}

		@Nested
		@DisplayName("when collection is empty")
		class EmptyCollectionTests {
			private final IndexedCollection<Integer> items = new TestCollection<>();

			@Test
			@DisplayName("returns empty")
			void returnsEmptyWhenEmpty() {
				final var match = items.match((item) ->
					item % 2 == 0);

				assert match.isEmpty();
			}
		}
	}

	@Nested
	@DisplayName(".enumerate(BiConsumer<T, Integer>)")
	class EnumerateTests {
		@Test
		@DisplayName("when collection is not empty, enumerates items and their indexes")
		void enumeratesItemsAndIndexes() {
			final var items = new TestCollection<>(6, 3, 2, 1, 8);
			final var enumerated = new int[5];
			items.enumerate((item, index) ->
				enumerated[index] = item);

			assert Arrays.equals(enumerated,
				new int[]{
					6, 3, 2, 1, 8
				});
		}

		@Test
		@DisplayName("when collection is empty, does nothing")
		void doesNothingWhenEmpty() {
			final var items = new TestCollection<Integer>();
			final var enumerated = new LinkedList<Integer>();
			items.enumerate((item, index) ->
				enumerated.add(item));

			assert enumerated.isEmpty();
		}
	}

	static class TestCollection<T> implements IndexedCollection<T> {
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
		public IndexRange getIndexRange() {
			return new IndexRange(0, items.length);
		}

		@Override
		public IndexedCollection<T> get(IndexRange indexRange) {
			final var items2 = new Object[indexRange.length];
			System.arraycopy(items, indexRange.start, items2, 0, indexRange.length);

			@SuppressWarnings("unchecked")
			final var sub = (IndexedCollection<T>) new TestCollection<>(items2);
			return sub;
		}

		@Override
		public IndexedCollection<T> reverse() {
			throw new UnsupportedOperationException();
		}

		@Override
		public IndexedCollection<T> shuffle() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Iterator<T> reverseIterator() {
			return new Iterator<>() {
				private int index = items.length - 1;

				@Override
				public boolean hasNext() {
					return index >= 0;
				}

				@Override
				public T next() {
					@SuppressWarnings("unchecked")
					final var item = (T) items[index];
					--index;
					return item;
				}
			};
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
					final var item = (T) items[index];
					++index;
					return item;
				}
			};
		}
	}
}

// created on Jul 15, 2019