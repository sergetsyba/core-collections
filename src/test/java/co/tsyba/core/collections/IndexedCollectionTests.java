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
		@DisplayName("returns index range")
		void returnsIndexRange() {
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
		@Test
		@DisplayName("when index is within valid range, returns item")
		void returnsItem() {
			final var items = new TestCollection<>(4, 5, 2, 1, 0, 5, 7);
			final var item = items.get(4);

			assert 0 == item;
		}

		@Test
		@DisplayName("when index is before valid range, fails")
		void failsWhenIndexBeforeValidRange() {
			try {
				new TestCollection<>(4, 5, 2, 1, 0, 5, 7)
					.get(-4);
			} catch (IndexNotInRangeException exception) {
				final var range = new IndexRange(0, 7);
				assert range.equals(exception.indexRange);
				assert -4 == exception.index;
				return;
			}
			assert false;
		}

		@Test
		@DisplayName("when index is after valid range, fails")
		void failsWhenIndexAfterValidRange() {
			try {
				new TestCollection<>(4, 5, 2, 1, 0, 5, 7)
					.get(9);
			} catch (IndexNotInRangeException exception) {
				final var range = new IndexRange(0, 7);
				assert range.equals(exception.indexRange);
				assert 9 == exception.index;
				return;
			}
			assert false;
		}

		@Test
		@DisplayName("when collection is empty, fails")
		void failsWhenEmpty() {
			try {
				new TestCollection<>()
					.get(0);
			} catch (IndexNotInRangeException exception) {
				final var range = new IndexRange(0, 0);
				assert range.equals(exception.indexRange);
				assert 0 == exception.index;
				return;
			}
			assert false;
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
	@DisplayName(".contains(IndexedCollection<T>)")
	class ContainsCollectionTests {
		@Nested
		@DisplayName("when all items are present")
		class AllItemsPresentTests {
			@Test
			@DisplayName("when in same order, returns true")
			void returnsTrueWhenAllPresentInSameOrder() {
				final var items1 = new TestCollection<>("f", "G", "D", "3", "a");
				final var items2 = new TestCollection<>("D", "3");

				assert items1.contains(items2);
			}

			@Test
			@DisplayName("when in different order, returns false")
			void returnsFalseWhenAllPresentInDifferentOrder() {
				final var items1 = new TestCollection<>("f", "G", "D", "3", "a");
				final var items2 = new TestCollection<>("f", "D");

				assert !items1.contains(items2);
			}
		}

		@Test
		@DisplayName("when some items are absent, returns false")
		void returnsFalseWhenSomeAbsent() {
			final var items1 = new TestCollection<>("f", "G", "D", "3", "a");
			final var items2 = new TestCollection<>("D", "5");

			assert !items1.contains(items2);
		}

		@Test
		@DisplayName("when all items are absent, returns false")
		void returnsFalseWhenAllAbsent() {
			final var items1 = new TestCollection<>("f", "G", "D", "3", "a");
			final var items2 = new TestCollection<>("H", "7", "e", "Q");

			assert !items1.contains(items2);
		}

		@Test
		@DisplayName("when items are empty, returns true")
		void returnsTrueWhenItemsEmpty() {
			final var items1 = new TestCollection<>("f", "G", "D", "3", "a");
			final var items2 = new TestCollection<String>();

			assert items1.contains(items2);
		}

		@Nested
		@DisplayName("when collection is empty")
		class EmptyCollectionTests {
			@Test
			@DisplayName("when items is not empty, returns false")
			void returnsFalseWhenEmpty() {
				final var items1 = new TestCollection<String>();
				final var items2 = new TestCollection<>("G", "p", "Q");

				assert !items1.contains(items2);
			}

			@Test
			@DisplayName("when items are empty, returns true")
			void returnsTrueWhenEmptyAndItemsEmpty() {
				final var items1 = new TestCollection<String>();
				final var items2 = new TestCollection<String>();

				assert items1.contains(items2);
			}
		}
	}

	@Nested
	@DisplayName(".find(T)")
	class FindTests {
		@Test
		@DisplayName("when item is present, returns item index")
		void returnsIndexWhenPresent() {
			final var items = new TestCollection<>(9, 3, 7, 8, 5, 2);
			final var index = items.find(8);

			assert Optional.of(3)
				.equals(index);
		}

		@Test
		@DisplayName("when item is absent, returns empty")
		void returnsEmptyWhenAbsent() {
			final var items = new TestCollection<>(9, 3, 7, 5, 5, 1);
			final var index = items.find(0);

			assert index.isEmpty();
		}

		@Test
		@DisplayName("when collection is empty, returns empty")
		void returnsEmptyWhenEmpty() {
			final var items = new TestCollection<Integer>();
			final var index = items.find(6);

			assert index.isEmpty();
		}
	}

	@Nested
	@DisplayName(".find(int, T)")
	class FindAfterIndexTests {
		@Test
		@DisplayName("when item is present at index, returns item index")
		void returnsItemIndexWhenPresentAtIndex() {
			final var items = new TestCollection<>("B", "a", "B", "R", "q", "B");
			final var match = items.find(2, "B");

			assert Optional.of(2)
				.equals(match);
		}

		@Test
		@DisplayName("when item is present after index, returns item index")
		void returnsItemIndexWhenPresentAfterIndex() {
			final var items = new TestCollection<>("B", "a", "B", "R", "q", "B");
			final var match = items.find(3, "B");

			assert Optional.of(5)
				.equals(match);
		}

		@Test
		@DisplayName("when item is absent at or after index, return empty")
		void returnsEmptyWhenAbsentAtOrAfterIndex() {
			final var items = new TestCollection<>("B", "a", "B", "r", "q", "d");
			final var match = items.find(3, "B");

			assert match.isEmpty();
		}

		@Test
		@DisplayName("when item is absent, returns empty")
		void returnsEmptyWhenAbsent() {
			final var items = new TestCollection<>("g", "a", "b", "r", "q", "d");
			final var match = items.find(0, "B");

			assert match.isEmpty();
		}

		@Test
		@DisplayName("when index is before valid range, fails")
		void failsWhenIndexBeforeValidRange() {
			try {
				new TestCollection<>("7", "4", "3", "12", "9")
					.find(-7, "4");
			} catch (IndexNotInRangeException exception) {
				assert -7 == exception.index;
				assert new IndexRange(0, 5)
					.equals(exception.indexRange);
				return;
			}
			assert false;
		}

		@Test
		@DisplayName("when index is after valid range, fails")
		void failsWhenIndexAfterValidRange() {
			try {
				new TestCollection<>("f", "T", "e", "Q")
					.find(7, "T");
			} catch (IndexNotInRangeException exception) {
				assert 7 == exception.index;
				assert new IndexRange(0, 4)
					.equals(exception.indexRange);
				return;
			}
			assert false;
		}

		@Test
		@DisplayName("when collection is empty, fails")
		void failsWhenEmpty() {
			try {
				new TestCollection<String>()
					.find(0, "A");
			} catch (IndexNotInRangeException exception) {
				assert 0 == exception.index;
				assert new IndexRange(0, 0)
					.equals(exception.indexRange);
				return;
			}
			assert false;
		}
	}

	@Nested
	@DisplayName(".match(Predicate<T>)")
	class MatchTests {
		@Test
		@DisplayName("when item matches, returns matched item")
		void returnsItemWhenAnyMatches() {
			final var items = new TestCollection<>(9, 3, 7, 8, 5, 2);
			final var match = items.match((item) ->
				item % 2 == 0);

			assert Optional.of(8)
				.equals(match);
		}

		@Test
		@DisplayName("when no item matches, returns empty")
		void returnsEmptyWhenNoneMatches() {
			final var items = new TestCollection<>(9, 3, 7, 5, 5, 1);
			final var match = items.match((item) ->
				item % 2 == 0);

			assert match.isEmpty();
		}

		@Test
		@DisplayName("when collection is empty, returns empty")
		void returnsEmptyWhenEmpty() {
			final var items = new TestCollection<Integer>();
			final var match = items.match((item) ->
				item % 2 == 0);

			assert match.isEmpty();
		}
	}

	@Nested
	@DisplayName(".match(int, Predicate<T>)")
	class MatchAfterIndexTests {
		@Test
		@DisplayName("when item matches at index, returns matched item")
		void returnsItemWhenPresentAtIndex() {
			final var items = new TestCollection<>("G", "a", "B", "R", "q", "D");
			final var match = items.match(2, (item2) ->
				Character.isUpperCase(item2.charAt(0)));

			assert Optional.of("B")
				.equals(match);
		}

		@Test
		@DisplayName("when item matches after index, returns matched item")
		void returnsItemWhenPresentAfterIndex() {
			final var items = new TestCollection<>("G", "a", "b", "R", "q", "D");
			final var match = items.match(2, (item2) ->
				Character.isUpperCase(item2.charAt(0)));

			assert Optional.of("R")
				.equals(match);
		}

		@Test
		@DisplayName("when no item matches at or after index, returns empty")
		void returnsEmptyWhenAbsentAtOrAfterIndex() {
			final var items = new TestCollection<>("G", "a", "B", "r", "q", "d");
			final var match = items.match(3, (item2) ->
				Character.isUpperCase(item2.charAt(0)));

			assert match.isEmpty();
		}

		@Test
		@DisplayName("when no item matches, returns empty")
		void returnsEmptyWhenAbsent() {
			final var items = new TestCollection<>("g", "a", "b", "r", "q", "d");
			final var match = items.match(2, (item2) ->
				Character.isUpperCase(item2.charAt(0)));

			assert match.isEmpty();
		}


		@Test
		@DisplayName("when start index is before valid range, fails")
		void failsWithIndexBeforeValidRange() {
			try {
				new TestCollection<>("7", "4", "3", "12", "9")
					.match(-4, String::isEmpty);
			} catch (IndexNotInRangeException exception) {
				assert -4 == exception.index;
				assert new IndexRange(0, 5)
					.equals(exception.indexRange);
				return;
			}
			assert false;
		}

		@Test
		@DisplayName("when start index is after valid range, fails")
		void failsWithIndexAfterValidRange() {
			try {
				new TestCollection<>("f", "T", "e", "Q")
					.match(12, String::isEmpty);
			} catch (IndexNotInRangeException exception) {
				assert 12 == exception.index;
				assert new IndexRange(0, 4)
					.equals(exception.indexRange);
				return;
			}
			assert false;
		}

		@Test
		@DisplayName("when collection is empty, fails")
		void failsWhenEmpty() {
			try {
				new TestCollection<String>()
					.match(0, String::isEmpty);
			} catch (IndexNotInRangeException exception) {
				assert 0 == exception.index;
				assert new IndexRange(0, 0)
					.equals(exception.indexRange);
				return;
			}
			assert false;
		}
	}

	@Nested
	@DisplayName(".enumerate(BiConsumer<T, Integer>)")
	class EnumerateTests {
		@Test
		@DisplayName("enumerates items and their indexes")
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
			return null;
		}

		@Override
		public List<T> sort(Comparator<T> comparator) {
			return null;
		}

		@Override
		public Collection<T> filter(Predicate<T> condition) {
			return null;
		}

		@Override
		public <R> Collection<R> convert(Function<T, R> converter) {
			return null;
		}

		@Override
		public IndexRange getIndexRange() {
			return new IndexRange(0, items.length);
		}

		@Override
		public IndexedCollection<T> reverse() {
			return null;
		}

		@Override
		public IndexedCollection<T> shuffle() {
			return null;
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

//class IndexedCollectionLegacyTests {
//	@Test
//	public void returnsFirstItem() {
//		final var items = collect("U", "n", "B", "C", "V");
//		final var firstItem = items.getFirst()
//			.get();
//
//		assert firstItem.equals("U");
//	}
//
//	@Test
//	public void returnsLastItem() {
//		final var items = collect("U", "n", "B", "C", "V");
//		final var lastItem = items.getLast()
//			.get();
//
//		assert lastItem.equals("V");
//	}
//
//	@Test
//	public void containsItems() {
//		final var items = collect("t", "x", "O", "p", "s");
//
//		// contains items
//		final var items1 = collect("O", "p", "s");
//		assert items.contains(items1);
//
//		// does not contain items
//		final var items2 = collect("O", "p", "S");
//		assert items.contains(items2) == false;
//
//		// contains itslef
//		assert items.contains(items);
//
//		// contains empty items
//		final var items4 = IndexedCollectionTests.<String>collect();
//		assert items.contains(items4);
//	}
//
//	@Test
//	public void findsItem() {
//		final var items = collect("j", "q", "z", "k", "e");
//
//		// item is present
//		final var index1 = items.find("k");
//		assert index1.get() == 3;
//
//		// item is absent
//		final var index2 = items.find("K");
//		assert index2.isEmpty();
//	}
//
//	@Test
//	public void findsItems() {
//		final var items = collect("u", "a", "g", "Z", "R");
//
//		// items are present
//		final var items1 = collect("a", "g", "Z", "R");
//		final var index1 = items.find(items1);
//		assert index1.get() == 1;
//
//		// items are absent
//		final var items2 = collect("a", "g", "Z", "r");
//		final var index2 = items.find(items2);
//		assert index2.isEmpty();
//
//		// finds itself
//		final var index3 = items.find(items);
//		assert index3.get() == 0;
//
//		// finds empty items
//		final var emptyItems = IndexedCollectionTests.<String>collect();
//		final var index4 = items.find(emptyItems);
//		assert index4.get() == 0;
//	}
//
//	@Test
//	public void matchesItem() {
//		final var items = collect("f", "b", "e", "E", "e");
//
//		// matches E
//		final var index1 = items.match(item -> item.toUpperCase().equals(item));
//		assert index1.get() == 3;
//
//		// does not match Z
//		final var index2 = items.match(item -> item.equals("Z"));
//		assert index2.isEmpty();
//	}
//
//	@Test
//	public void enumeratesItems() {
//		final var items = collect("t", "Q", "x", "z", "U");
//		final var enumeratedItems = new ArrayList<String>();
//		final var enumeratedIndexes = new ArrayList<Integer>();
//
//		items.enumerate((item, index) -> {
//			enumeratedItems.add(item);
//			enumeratedIndexes.add(index);
//		});
//
//		assert enumeratedItems.equals(
//			List.of("t", "Q", "x", "z", "U"));
//		assert enumeratedIndexes.equals(
//			List.of(0, 1, 2, 3, 4));
//	}
//
//	private static <T> IndexedCollection<T> collect(T... items) {
//		return new ListCollection<>(items);
//	}
//}

// created on Jul 15, 2019