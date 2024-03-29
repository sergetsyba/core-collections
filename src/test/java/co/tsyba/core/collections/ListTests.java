package co.tsyba.core.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static co.tsyba.core.collections.Assertions.*;

public class ListTests {
	@Nested
	@DisplayName("List(T...)")
	class NewWithVarargsTests {
		@Test
		@DisplayName("creates list")
		void createsList() {
			final var items = new List<>("v", "4", "G", "5");

			assertEquals(items,
				new String[]{
					"v", "4", "G", "5"
				});
		}

		@Test
		@DisplayName("when some items are null, creates list without nulls")
		void createsListWithoutNullsWhenSomeItemsNull() {
			final var items = new List<>("h", "5", null, "R", null, null);

			assertEquals(items,
				new String[]{
					"h", "5", "R"
				});
		}

		@Test
		@DisplayName("when all items are null, creates empty list")
		void createsEmptyListWhenAllItemsNull() {
			final var items = new List<>(null, null, null);

			assertEquals(items,
				new String[]{
				});
		}

		@Test
		@DisplayName("when argument array is empty, creates empty list")
		void createsEmptyListWhenArgArrayEmpty() {
			final var items = new List<>();

			assertEquals(items,
				new String[]{
				});
		}
	}

	@Nested
	@DisplayName("List(Collection<T>)")
	class NewWithCollectionTests {
		@Test
		@DisplayName("creates list")
		void createsList() {
			final var items = new List<>(
				new List<>("v", "4", "G", "5"));

			assertEquals(items,
				new String[]{
					"v", "4", "G", "5"
				});
		}

		@Test
		@DisplayName("when argument collection is empty, creates empty list")
		void createsEmptyListWhenArgCollectionEmpty() {
			final var items = new List<>(
				new List<>());

			assertEquals(items,
				new String[]{
				});
		}
	}

	@Nested
	@DisplayName(".getIndexRange()")
	class GetIndexRangeTests {
		@Test
		@DisplayName("when list is not empty, returns valid index range")
		void returnsValidRangeWhenListNotEmpty() {
			final var items = new List<>(6, 3, 2, 1, 3, 0);
			final var range = items.getIndexRange();

			assertEquals(range,
				new IndexRange(0, 6));
		}

		@Test
		@DisplayName("when list is empty, returns empty index range")
		void returnsEmptyRangeWhenListEmpty() {
			final var items = new List<>();
			final var range = items.getIndexRange();

			assertEquals(range,
				new IndexRange());
		}
	}

	@Nested
	@DisplayName(".guard(int)")
	class GuardTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			private final List<String> items = new List<>("e", "4", "6", "7");

			@Test
			@DisplayName("when index is within valid range, returns index")
			void returnsIndexWhenIndexWithinValidRange() {
				final var index = items.guard(2);
				assertEquals(index, 2);
			}

			@Test
			@DisplayName("when index is before valid range, returns empty optional")
			void returnsEmptyOptionalWhenIndexBeforeValidRange() {
				final var index = items.guard(-3);
				assertEmpty(index);
			}

			@Test
			@DisplayName("when index is after valid range, returns empty optional")
			void returnsEmptyOptionalWhenIndexAfterValidRange() {
				final var index = items.guard(7);
				assertEmpty(index);
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			private final List<String> items = new List<>();

			@Test
			@DisplayName("returns empty optional")
			void returnsEmptyOptional() {
				final var index = items.guard(0);
				assertEmpty(index);
			}
		}
	}

	@Nested
	@DisplayName(".getCount()")
	class GetCountTests {
		@Test
		@DisplayName("when list is not empty, returns item count")
		void returnsItemCountWhenNotEmpty() {
			final var items = new List<>("g", "T", "e", "e");
			final var count = items.getCount();

			assertEquals(count, 4);
		}

		@Test
		@DisplayName("when list is empty, return 0")
		void returnsZeroWhenEmpty() {
			final var items = new List<>();
			final var count = items.getCount();

			assertEquals(count, 0);
		}
	}

	@Nested
	@DisplayName(".getFirst()")
	class GetFirstTests {
		@Test
		@DisplayName("when list is not empty, returns first item")
		void returnsFirstItemWhenListNotEmpty() {
			final var items = new List<>("B", "V", "4");
			final var first = items.getFirst();

			assertEquals(first, "B");
		}

		@Test
		@DisplayName("when list is empty, returns empty optional")
		void returnsEmptyOptionalWhenListEmpty() {
			final var items = new List<String>();
			final var first = items.getFirst();

			assertEmpty(first);
		}
	}

	@Nested
	@DisplayName(".getLast()")
	class GetLastTests {
		@Test
		@DisplayName("when list is not empty, returns last item")
		void returnsLastItemWhenListNotEmpty() {
			final var items = new List<>("B", "V", "4");
			final var last = items.getLast();

			assertEquals(last, "4");
		}

		@Test
		@DisplayName("when list is empty, returns empty optional")
		void returnsEmptyOptionalWhenEmpty() {
			final var items = new List<String>();
			final var last = items.getFirst();

			assertEmpty(last);
		}
	}

	@Nested
	@DisplayName(".getPrefix(int)")
	class GetPrefixTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			private final List<String> items = new List<>("B", "d", "R", "f", "a", "Q");

			@Test
			@DisplayName("when index is within valid range, returns prefix")
			void returnsPrefixWhenIndexWithinValidRange() {
				final var prefix = items.getPrefix(4);

				assertEquals(prefix,
					new String[]{
						"B", "d", "R", "f"
					});
			}

			@Test
			@DisplayName("when index is 0, returns empty list")
			void returnsEmptyListWhenIndexZero() {
				final var prefix = items.getPrefix(0);

				assertEquals(prefix,
					new String[]{
					});
			}

			@Test
			@DisplayName("when index equals item count, fails")
			void failsWhenIndexEqualsItemCount() {
				final var expected = new IndexNotInRangeException(6,
					new IndexRange(0, 6));

				assertThrows(() -> {
					items.getPrefix(6);
				}, expected);
			}

			@Test
			@DisplayName("when index is before valid range, fails")
			void failsWhenIndexBeforeValidRange() {
				final var expected = new IndexNotInRangeException(-1,
					new IndexRange(0, 6));

				assertThrows(() -> {
					items.getPrefix(-1);
				}, expected);
			}

			@Test
			@DisplayName("when index is after valid range, fails")
			void failsWhenIndexAfterValidRange() {
				final var expected = new IndexNotInRangeException(9,
					new IndexRange(0, 6));

				assertThrows(() -> {
					items.getPrefix(9);
				}, expected);
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			private final List<String> items = new List<>();

			@Test
			@DisplayName("fails")
			void fails() {
				final var expected = new IndexNotInRangeException(0,
					new IndexRange());

				assertThrows(() -> {
					items.getPrefix(0);
				}, expected);
			}
		}
	}

	@Nested
	@DisplayName(".getSuffix(int)")
	class GetSuffixTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			private final List<String> items = new List<>("B", "d", "R", "f", "a", "Q");

			@Test
			@DisplayName("when index is within valid range, returns suffix")
			void returnsSuffixWhenIndexWithinValidRange() {
				final var suffix = items.getSuffix(3);

				assertEquals(suffix,
					new String[]{
						"f", "a", "Q"
					});
			}

			@Test
			@DisplayName("when index is 0, returns list copy")
			void returnsListCopyWhenIndexZero() {
				final var suffix = items.getSuffix(0);

				assertIsNot(suffix, items);
				assertEquals(suffix,
					new String[]{
						"B", "d", "R", "f", "a", "Q"
					});
			}

			@Test
			@DisplayName("when index equals item count, fails")
			void failsWhenIndexEqualsItemCount() {
				final var expected = new IndexNotInRangeException(6,
					new IndexRange(0, 6));

				assertThrows(() -> {
					items.getSuffix(6);
				}, expected);
			}

			@Test
			@DisplayName("when index is before valid range, fails")
			void failsWhenIndexBeforeValidRange() {
				final var expected = new IndexNotInRangeException(-1,
					new IndexRange(0, 6));

				assertThrows(() -> {
					items.getSuffix(-1);
				}, expected);
			}

			@Test
			@DisplayName("when index is after valid range, fails")
			void failsWhenIndexAfterValidRange() {
				final var expected = new IndexNotInRangeException(9,
					new IndexRange(0, 6));

				assertThrows(() -> {
					items.getSuffix(9);
				}, expected);
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			private final List<String> items = new List<>();

			@Test
			@DisplayName("fails")
			void fails() {
				final var expected = new IndexNotInRangeException(0,
					new IndexRange());

				assertThrows(() -> {
					items.getSuffix(0);
				}, expected);
			}
		}
	}

	@Nested
	@DisplayName(".get(int)")
	class GetAtIndexTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			private final List<String> items = new List<>("e", "D", "d", "E", "X");

			@Test
			@DisplayName("returns item")
			void returnsItem() {
				final var item = items.get(2);
				assertEquals(item, "d");
			}

			@Test
			@DisplayName("when index is before valid range, fails")
			void failsWhenIndexBeforeValidRange() {
				final var expected = new IndexNotInRangeException(-1,
					new IndexRange(0, 5));

				assertThrows(() -> {
					items.get(-1);
				}, expected);
			}

			@Test
			@DisplayName("when index is after valid range, fails")
			void failsWhenIndexAfterValidRange() {
				final var expected = new IndexNotInRangeException(5,
					new IndexRange(0, 5));

				assertThrows(() -> {
					items.get(5);
				}, expected);
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			private final List<String> items = new List<>();

			@Test
			@DisplayName("fails")
			void fails() {
				final var expected = new IndexNotInRangeException(0,
					new IndexRange());

				assertThrows(() -> {
					items.get(0);
				}, expected);
			}
		}
	}

	@Nested
	@DisplayName(".get(IndexRange)")
	class GetAtIndexRangeTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			private final List<String> items1 = new List<>(
				"r", "4", "v", "E", "P", "e", "Q");

			@Test
			@DisplayName("when index range is within valid range, returns items")
			void returnsItemsWhenIndexRangeWithinValidRange() {
				final var items2 = items1.get(
					new IndexRange(2, 5));

				assertEquals(items2,
					new String[]{
						"v", "E", "P"
					});
			}

			@Test
			@DisplayName("when index range is empty, returns empty list")
			void returnsEmptyListWhenIndexRangeEmpty() {
				final var items2 = items1.get(
					new IndexRange(2, 2));

				assertEquals(items2,
					new String[]{
					});
			}

			@Test
			@DisplayName("when index range starts after valid range, fails")
			void failsWhenIndexRangeStartsAfterValidRange() {
				final var range = new IndexRange(7, 12);
				final var expected = new IndexRangeNotInRangeException(range,
					new IndexRange(0, 7));

				assertThrows(() -> {
					items1.get(range);
				}, expected);
			}

			@Test
			@DisplayName("when index range ends after valid range, fails")
			void failsWhenIndexRangeEndsAfterValidRange() {
				final var range = new IndexRange(5, 8);
				final var expected = new IndexRangeNotInRangeException(range,
					new IndexRange(0, 7));

				assertThrows(() -> {
					items1.get(range);
				}, expected);
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			private final List<String> items = new List<>();

			@Test
			@DisplayName("when index range coincides with valid range, fails")
			void failsWhenIndexRangeCoincidesWithValidRange() {
				final var range = new IndexRange();
				final var expected = new IndexRangeNotInRangeException(range,
					new IndexRange());

				assertThrows(() -> {
					items.get(range);
				}, expected);
			}

			@Test
			@DisplayName("when index range starts after valid range, fails")
			void failsWhenIndexRangeStartsAfterValidRange() {
				final var range = new IndexRange(4, 7);
				final var expected = new IndexRangeNotInRangeException(range,
					new IndexRange());

				assertThrows(() -> {
					items.get(range);
				}, expected);
			}

			@Test
			@DisplayName("when index range ends after valid range, fails")
			void failsWhenIndexRangeEndsAfterValidRange() {
				final var range = new IndexRange(0, 1);
				final var expected = new IndexRangeNotInRangeException(range,
					new IndexRange());

				assertThrows(() -> {
					items.get(range);
				}, expected);
			}
		}
	}

	@Nested
	@DisplayName(".matchFirst(Predicate<T>)")
	class MatchFirstTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			private final List<Integer> items = new List<>(5, 6, 4, 3, 4, 5);

			@Test
			@DisplayName("when item matches, returns item")
			void returnsItemWhenItemMatches() {
				final var matched = items.matchFirst((item) ->
					item == 4);

				assertEquals(matched, 4);
			}

			@Test
			@DisplayName("when no item matches, returns empty optional")
			void returnsEmptyOptionalWhenNoItemMatches() {
				final var matched = items.matchFirst((item) ->
					item == 2);

				assertEmpty(matched);
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			private final List<Integer> items = new List<>();

			@Test
			@DisplayName("returns empty optional")
			void returnsEmptyOptional() {
				final var matched = items.matchFirst((item) ->
					item == 4);

				assertEmpty(matched);
			}
		}
	}

	@Nested
	@DisplayName(".matchLast(Predicate<T>)")
	class MatchLastTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			private final List<Integer> items = new List<>(5, 6, 4, 3, 4, 5);

			@Test
			@DisplayName("when an item matches, returns item")
			void returnsItemWhenItemMatches() {
				final var matched = items.matchLast((item) ->
					item == 5);

				assertEquals(matched, 5);
			}

			@Test
			@DisplayName("when no item matches, returns empty optional")
			void returnsEmptyOptionalWhenNoItemMatches() {
				final var matched = items.matchLast((item) ->
					item == 9);

				assertEmpty(matched);
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			private final List<Integer> items = new List<>();

			@Test
			@DisplayName("returns empty optional")
			void returnsEmptyOptional() {
				final var matched = items.matchLast((item) ->
					item == 5);

				assertEmpty(matched);
			}
		}
	}

	@Nested
	@DisplayName(".findFirst(T)")
	class FindFirstTests {
		private final List<Integer> items = new List<>(9, 3, 7, 8, 5, 8, 2, 8);

		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			@Test
			@DisplayName("when item is present, returns item index")
			void returnsIndexWhenItemPresent() {
				final var index = items.findFirst(8);
				assertEquals(index, 3);
			}

			@Test
			@DisplayName("when item is absent, returns empty optional")
			void returnsEmptyOptionalWhenItemAbsent() {
				final var index = items.findFirst(0);
				assertEmpty(index);
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			private final List<Integer> items = new List<>();

			@Test
			@DisplayName("returns empty optional")
			void returnsEmptyOptional() {
				final var index = items.findFirst(6);
				assertEmpty(index);
			}
		}
	}

	@Nested
	@DisplayName(".findLast(T)")
	class FindLastTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			private final List<String> items = new List<>("b", "e", "Q", "e", "f", "2");

			@Test
			@DisplayName("when item is present, returns its index")
			void returnsItemIndexWhenItemPresent() {
				final var index = items.findLast("e");
				assertEquals(index, 3);
			}

			@Test
			@DisplayName("when item is absent, returns empty optional")
			void returnsEmptyOptionalWhenItemAbsent() {
				final var index = items.findLast("F");
				assertEmpty(index);
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			private final List<String> items = new List<>();

			@Test
			@DisplayName("returns empty optional")
			void returnsEmptyOptional() {
				final var index = items.findLast("e");
				assertEmpty(index);
			}
		}
	}

	@Nested
	@DisplayName(".find(T)")
	class FindTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			private final List<String> items = new List<>("g", "E", "g", "R", "r", "g", "v");

			@Test
			@DisplayName("when item is present, returns its indexes")
			void returnsItemIndexesWhenItemPresent() {
				final var indexes = items.find("g");

				assertEquals(indexes,
					new Integer[]{
						0, 2, 5
					});
			}

			@Test
			@DisplayName("when item is absent, returns empty list")
			void returnsEmptyListWhenItemAbsent() {
				final var indexes = items.find("G");

				assertEquals(indexes,
					new Integer[]{
					});
			}

			@Test
			@DisplayName("when item is null, returns empty list")
			void returnsEmptyListWhenItemNull() {
				final var indexes = items.find((String) null);

				assertEquals(indexes,
					new Integer[]{
					});
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			private final List<String> items = new List<>();

			@Test
			@DisplayName("returns empty list")
			void returnsEmptyList() {
				final var indexes = items.find("g");

				assertEquals(indexes,
					new Integer[]{
					});
			}

			@Test
			@DisplayName("when item is null, returns empty list")
			void returnsEmptyListWhenItemNull() {
				final var indexes = items.find((String) null);

				assertEquals(indexes,
					new Integer[]{
					});
			}
		}
	}

	@Nested
	@DisplayName(".findFirst(Predicate<T>)")
	class FindFirstPredicateTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			private final List<String> items = new List<>("f", "e", "b", "e", "Q", "d");

			@Test
			@DisplayName("when an item matches, returns its index")
			void returnsItemIndexWhenItemMatches() {
				final var index = items.findFirst((item) ->
					item.equals("e"));

				assertEquals(index, 1);
			}

			@Test
			@DisplayName("when no item matches, returns empty optional")
			void returnsEmptyOptionalWhenNoItemMatches() {
				final var index = items.findFirst((item) ->
					item.equals("E"));

				assertEmpty(index);
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			private final List<String> items = new List<>();

			@Test
			@DisplayName("returns empty optional")
			void returnsEmptyOptional() {
				final var index = items.findFirst((item) ->
					item.equals("e"));

				assertEmpty(index);
			}
		}
	}

	@Nested
	@DisplayName(".findLast(Predicate<T>)")
	class FindLastPredicateTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			private final List<String> items = new List<>("f", "e", "b", "e", "Q", "d");

			@Test
			@DisplayName("when an item matches, returns its index")
			void returnsItemIndexWhenItemMatches() {
				final var index = items.findLast((item) ->
					item.equals("e"));

				assertEquals(index, 3);
			}

			@Test
			@DisplayName("when no item matches, returns empty optional")
			void returnsEmptyOptionalWhenNoItemMatches() {
				final var index = items.findLast((item) ->
					item.equals("E"));

				assertEmpty(index);
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			private final List<String> items = new List<>();

			@Test
			@DisplayName("returns empty optional")
			void returnsEmptyOptional() {
				final var index = items.findLast((item) ->
					item.equals("e"));

				assertEmpty(index);
			}
		}
	}

	@Nested
	@DisplayName(".findFirst(List<T>)")
	class FindFirstListTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			private final List<String> items1 = new List<>("v", "r", "e", "c", "r", "e");

			@Test
			@DisplayName("when all items are present, returns their index")
			void returnsItemsIndexWhenAllItemsPresent() {
				final var items2 = new List<>("r", "e");
				final var index = items1.findFirst(items2);

				assertEquals(index, 1);
			}

			@Test
			@DisplayName("when some items are absent, returns empty optional")
			void returnsEmptyOptionalWhenSomeItemsAbsent() {
				final var items2 = new List<>("c", "r", "E");
				final var index = items1.findFirst(items2);

				assertEmpty(index);
			}

			@Test
			@DisplayName("when argument list is empty, returns 0")
			void returnsZeroWhenArgListEmpty() {
				final var items2 = new List<String>();
				final var index = items1.findFirst(items2);

				assertEquals(index, 0);
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			private final List<String> items1 = new List<>();

			@Test
			@DisplayName("when argument list is not empty, returns empty optional")
			void returnsEmptyOptionalWhenArgListNotEmpty() {
				final var items2 = new List<>("g", "e");
				final var index = items1.findFirst(items2);

				assertEmpty(index);
			}

			@Test
			@DisplayName("when argument list is empty, returns empty optional")
			void returnsEmptyOptionalWhenArgListEmpty() {
				final var items2 = new List<String>();
				final var index = items1.findFirst(items2);

				assertEmpty(index);
			}
		}
	}

	@Nested
	@DisplayName(".findLast(List<T>)")
	class FindLastListTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			private final List<String> items1 = new List<>("v", "r", "e", "c", "r", "e", "b");

			@Test
			@DisplayName("when all items are present, returns their index")
			void returnsItemsIndexWhenAllItemsPresent() {
				final var items2 = new List<>("r", "e");
				final var index = items1.findLast(items2);

				assertEquals(index, 4);
			}

			@Test
			@DisplayName("when items present at list start, returns their index")
			void returnsItemsIndexWhenItemsAtListStart() {
				final var items2 = new List<>("v", "r");
				final var index = items1.findLast(items2);

				assertEquals(index, 0);
			}

			@Test
			@DisplayName("when items present at list end, returns their index")
			void returnsItemsIndexWhenItemsAtListEnd() {
				final var items2 = new List<>("e", "b");
				final var index = items1.findLast(items2);

				assertEquals(index, 5);
			}

			@Test
			@DisplayName("when items prefix present at list end, returns empty optional")
			void returnsEmptyOptionalWhenListEndsInArgListPrefix() {
				final var items2 = new List<>("e", "b", "T", "q");
				final var index = items1.findLast(items2);

				assertEmpty(index);
			}

			@Test
			@DisplayName("when some items are absent, returns empty optional")
			void returnsEmptyOptionalWhenSomeItemsAbsent() {
				final var items2 = new List<>("c", "r", "B");
				final var index = items1.findLast(items2);

				assertEmpty(index);
			}

			@Test
			@DisplayName("when argument list is larger, returns empty optional")
			void returnsEmptyOptionalWhenArgListLarger() {
				final var items2 = new List<>("v", "e", "q", "W", "f", "c", "q", "V", "e");
				final var index = items1.findLast(items2);

				assertEmpty(index);
			}

			@Test
			@DisplayName("when argument list is empty, returns last index")
			void returnsLastIndexWhenArgListEmpty() {
				final var items2 = new List<String>();
				final var index = items1.findLast(items2);

				assertEquals(index, 6);
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			private final List<String> items1 = new List<>();

			@Test
			@DisplayName("when argument list is not empty, returns empty optional")
			void returnsEmptyOptionalWhenArgListNotEmpty() {
				final var items2 = new List<>("g", "e");
				final var index = items1.findLast(items2);

				assertEmpty(index);
			}

			@Test
			@DisplayName("when argument list is empty, returns empty optional")
			void returnsEmptyOptionalWhenArgListEmpty() {
				final var items2 = new List<String>();
				final var index = items1.findLast(items2);

				assertEmpty(index);
			}
		}
	}

	@Nested
	@DisplayName(".find(List<T>)")
	class FindListTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			private final List<String> items1 = new List<>(
				"v", "v", "b", "r", "v", "b", "v", "b", "F");

			@Test
			@DisplayName("when items are present, returns their indexes")
			void returnsIndexesWhenItemsPresent() {
				final var items2 = new List<>("v", "b");
				final var indexes = items1.find(items2);

				assertEquals(indexes,
					new Integer[]{
						1, 4, 6
					});
			}

			@Test
			@DisplayName("when items are absent, returns empty list")
			void returnsEmptyListWhenItemsAbsent() {
				final var items2 = new List<>("V", "b");
				final var indexes = items1.find(items2);

				assertEquals(indexes,
					new Integer[]{
					});
			}

			@Test
			@DisplayName("when argument list is empty, returns all indexes")
			void returnsAllIndexesWhenArgListEmpty() {
				final var items2 = new List<String>();
				final var indexes = items1.find(items2);

				assertEquals(indexes,
					new Integer[]{
						0, 1, 2, 3, 4, 5, 6, 7, 8
					});
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			private final List<String> items1 = new List<>();

			@Test
			@DisplayName("when argument list is not empty, returns empty list")
			void returnsEmptyListWhenArgListNotEmpty() {
				final var items2 = new List<>("g", "R");
				final var indexes = items1.findFirst(items2);

				assertEmpty(indexes);
			}

			@Test
			@DisplayName("when argument list is empty, returns empty list")
			void returnsEmptyListWhenArgListEmpty() {
				final var items2 = new List<String>();
				final var indexes = items1.findFirst(items2);

				assertEmpty(indexes);
			}
		}
	}

	@Nested
	@DisplayName(".getDistinct()")
	class GetDistinctTests {
		@Test
		@DisplayName("when list is not empty, returns distinct items")
		void returnsDistinctItemsWhenListNotEmpty() {
			final var items = new List<>("a", "b", "5", "a", "4", "4");
			final var distinct = items.getDistinct();

			assertEquals(distinct,
				new String[]{
					"a", "b", "5", "4"
				});
		}

		@Test
		@DisplayName("when list is empty, returns empty list")
		void returnsEmptyListWhenListEmpty() {
			final var items = new List<String>();
			final var distinct = items.getDistinct();

			assertEquals(distinct,
				new String[]{
				});
		}
	}

	@Nested
	@DisplayName(".enumerate(BiConsumer<T, Integer>)")
	class EnumerateTests {
		@Test
		@DisplayName("when list is not empty, enumerates items and their indexes")
		void enumeratesItemsAndIndexesWhenListNotEmpty() {
			final var items = new List<>(6, 3, 2, 1, 8);
			final var enumerated = new Integer[5];
			final var returned = items.enumerate((item, index) ->
				enumerated[index] = item);

			assertIs(returned, items);
			assertEquals(enumerated,
				new Integer[]{
					6, 3, 2, 1, 8
				});
		}

		@Test
		@DisplayName("when list is empty, does nothing")
		void doesNothingWhenListEmpty() {
			final var items = new List<String>();
			final var enumerated = new String[3];
			final var returned = items.enumerate((item, index) ->
				enumerated[index] = item);

			assertIs(returned, items);
			assertEquals(enumerated,
				new String[]{
					null, null, null
				});
		}
	}

	@Nested
	@DisplayName(".filter(Predicate<T>)")
	class FilterTests {
		@Test
		@DisplayName("when list is not empty, returns filtered items")
		void returnsFilteredItemsWhenListNotEmpty() {
			final var items = new List<>(2, 4, 3, 2, 1, 9);
			final var odds = items.filter((item) ->
				item % 2 == 1);

			assertEquals(odds,
				new Integer[]{
					3, 1, 9
				});
		}

		@Test
		@DisplayName("when list is empty, returns empty list")
		void returnsEmptyListWhenListEmpty() {
			final var items = new List<Integer>();
			final var evens = items.filter((item) ->
				item % 2 == 0);

			assertEquals(evens,
				new Integer[]{
				});
		}
	}

	@Nested
	@DisplayName(".convert(Function<T, R>)")
	class ConvertTests {
		private final List<Integer> items = new List<>(4, 6, 2, 1, 7, 8, 5);

		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			@Test
			@DisplayName("converts items")
			void returnsConvertedItems() {
				final var converted = items.convert((item) ->
					Integer.toString(item));

				assertEquals(converted,
					new String[]{
						"4", "6", "2", "1", "7", "8", "5"
					});
			}

			@Test
			@DisplayName("when some items are converted to null, returns converted items without nulls")
			void returnsConvertedItemsWithoutNullsWhenSomeItemsConvertedToNull() {
				final var converted = items.convert((item) ->
					item % 2 == 0
						? Integer.toString(item)
						: null);

				assertEquals(converted,
					new String[]{
						"4", "6", "2", "8"
					});
			}

			@Test
			@DisplayName("when all items are converted to null, returns empty list")
			void returnsEmptyListWhenAllItemsConvertedToNull() {
				final var converted = items.convert((item) ->
					null);

				assertEquals(converted,
					new String[]{
					});
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			private final List<Integer> items = new List<>();

			@Test
			@DisplayName("returns empty list")
			void returnsEmptyList() {
				final var converted = items.convert((item) ->
					item * 2);

				assertEquals(converted,
					new Integer[]{
					});
			}
		}
	}

	@Nested
	@DisplayName(".toArray()")
	class ToArrayTests {
		@Test
		@DisplayName("when list is not empty, returns items in array")
		void returnsItemsInArrayWhenListNotEmpty() {
			final var items = new List<>(5, 3, 2, 0, 0, 3, 4);
			final var array = items.toArray();

			assertEquals(array,
				new Integer[]{
					5, 3, 2, 0, 0, 3, 4
				});
		}

		@Test
		@DisplayName("when list is empty, returns empty array")
		void returnsEmptyArrayWhenListEmpty() {
			final var items = new List<>();
			final var array = items.toArray();

			assertEquals(array,
				new Integer[]{
				});
		}
	}

	@Nested
	@DisplayName(".bridge()")
	class BridgeTests {
		@Test
		@DisplayName("when list is not empty, returns items in java.util.List")
		void returnsItemsInJavaListWhenNotEmpty() {
			final var items = new List<>("5", "y", "a", "v");
			final var bridged = items.bridge();

			assertEquals(bridged,
				new String[]{
					"5", "y", "a", "v"
				});
		}

		@Test
		@DisplayName("when list is empty, returns empty java.util.List")
		void returnsEmptyJavaListWhenEmpty() {
			final var items = new List<>();
			final var bridged = items.bridge();

			assertEquals(bridged,
				new String[]{
				});
		}
	}

	@Nested
	@DisplayName(".toString()")
	class ToStringTests {
		@Test
		@DisplayName("when list is not empty, returns items in string")
		void convertsToStringWhenListNotEmpty() {
			final var items = new List<>("g", "H", "6", "c", "E");
			final var string = items.toString();

			assertEquals(string, "[g, H, 6, c, E]");
		}

		@Test
		@DisplayName("when list is empty, returns []")
		void convertsToStringWhenListEmpty() {
			final var items = new List<>();
			final var string = items.toString();

			assertEquals(string, "[]");
		}
	}
}

// created on Jul 10, 2019