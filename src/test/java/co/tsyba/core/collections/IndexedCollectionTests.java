package co.tsyba.core.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class IndexedCollectionTests {
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
		@DisplayName("returns empty when collection is empty")
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
		@DisplayName("returns last item when collection is not empty")
		void returnsLastWhenNotEmpty() {
			final var items = new TestCollection<>("B", "V", "4");
			final var last = items.getLast();

			assert Optional.of("4")
				.equals(last);
		}

		@Test
		@DisplayName("returns empty when collection is empty")
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
		@Test
		@DisplayName("returns true when all items are present in same order")
		void returnsTrueWhenAllPresent() {
			final var items1 = new TestCollection<>("f", "G", "D", "3", "a");
			final var items2 = new TestCollection<>("D", "3");

			assert items1.contains(items2);
		}

		@Test
		@DisplayName("returns false when all items are present in different order")
		void returnsFalseWhenAllPresentInDifferentOrder() {
			final var items1 = new TestCollection<>("f", "G", "D", "3", "a");
			final var items2 = new TestCollection<>("f", "D");

			assert !items1.contains(items2);
		}

		@Test
		@DisplayName("returns false when some items are absent")
		void returnsFalseWhenSomeAbsent() {
			final var items1 = new TestCollection<>("f", "G", "D", "3", "a");
			final var items2 = new TestCollection<>("D", "5");

			assert !items1.contains(items2);
		}

		@Test
		@DisplayName("returns false when all items are absent")
		void returnsFalseWhenAllAbsent() {
			final var items1 = new TestCollection<>("f", "G", "D", "3", "a");
			final var items2 = new TestCollection<>("H", "7", "e", "Q");

			assert !items1.contains(items2);
		}

		@Test
		@DisplayName("returns false when collection is empty")
		void returnsFalseWhenEmpty() {
			final var items1 = new TestCollection<String>();
			final var items2 = new TestCollection<>("G", "p", "Q");

			assert !items1.contains(items2);
		}

		@Test
		@DisplayName("returns true when items are empty")
		void returnsTrueWhenItemsEmpty() {
			final var items1 = new TestCollection<>("f", "G", "D", "3", "a");
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
	@DisplayName(".find(T)")
	class FindTests {
		@Test
		@DisplayName("returns item index when item is present")
		void returnsIndexWhenPresent() {
			final var items = new TestCollection<>(9, 3, 7, 8, 5, 2);
			final var index = items.find(8);

			assert Optional.of(3)
				.equals(index);
		}

		@Test
		@DisplayName("returns empty when item is absent")
		void returnsEmptyWhenAbsent() {
			final var items = new TestCollection<>(9, 3, 7, 5, 5, 1);
			final var index = items.find(0);

			assert index.isEmpty();
		}

		@Test
		@DisplayName("returns empty when collection is empty")
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
		void returnsEmptyWhenAbsentAtAfterIndex() {
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
	@DisplayName(".match(int, Predicate<T>)")
	class MatchAfterIndexTests {
		@Test
		@DisplayName("returns matched item when item matches at index")
		void returnsItemWhenPresentAtIndex() {
			final var items = new TestCollection<>("G", "a", "B", "R", "q", "D");
			final var match = items.match(2, (item2) ->
				Character.isUpperCase(item2.charAt(0)));

			assert Optional.of("B")
				.equals(match);
		}

		@Test
		@DisplayName("returns matched item when item matches after index")
		void returnsItemWhenPresentAfterIndex() {
			final var items = new TestCollection<>("G", "a", "b", "R", "q", "D");
			final var match = items.match(2, (item2) ->
				Character.isUpperCase(item2.charAt(0)));

			assert Optional.of("R")
				.equals(match);
		}

		@Test
		@DisplayName("returns empty when no item matches at or after index")
		void returnsEmptyWhenAbsentAtAfterIndex() {
			final var items = new TestCollection<>("G", "a", "B", "r", "q", "d");
			final var match = items.match(3, (item2) ->
				Character.isUpperCase(item2.charAt(0)));

			assert match.isEmpty();
		}

		@Test
		@DisplayName("returns empty when no item matches")
		void returnsEmptyWhenAbsent() {
			final var items = new TestCollection<>("g", "a", "b", "r", "q", "d");
			final var match = items.match(2, (item2) ->
				Character.isUpperCase(item2.charAt(0)));

			assert match.isEmpty();
		}


		@Test
		@DisplayName("fails when start index is before valid range")
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
		@DisplayName("fails when start index is after valid range")
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
		@DisplayName("fails when collection is empty")
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
		public Iterator<T> iterator(int startIndex) {
			return new Iterator<>() {
				private int index = startIndex;

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