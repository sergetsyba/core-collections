package co.tsyba.core.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jul 10, 2019.
 */
public class ListTests {
	@Nested
	@DisplayName("List(Collection<T>)")
	class NewWithCollectionTests {
		@Test
		@DisplayName("creates list")
		void createsList() {
			final var items = new List<>(
				new Set<>("v", "4", "G", "5"));

			Arrays.sort(items.store2);
			assert Arrays.equals(items.store2,
				new String[]{
					"4", "5", "G", "v"
				});
		}

		@Test
		@DisplayName("creates empty list")
		void createsEmptyList() {
			final var items = new List<>(
				new Set<String>());

			assert Arrays.equals(items.store2,
				new String[]{});
		}
	}

	@Nested
	@DisplayName("List(T...)")
	class NewWithVarargsTests {
		@Test
		@DisplayName("creates list")
		void createsList() {
			final var items = new List<>("v", "4", "G", "5");

			assert Arrays.equals(items.store2,
				new String[]{
					"v", "4", "G", "5"
				});
		}

		@Test
		@DisplayName("ignores null values")
		void ignoresNulls() {
			final var items = new List<>("h", "5", null, "R", null, null);

			assert Arrays.equals(items.store2,
				new Object[]{
					"h", "5", "R"
				});
		}

		@Test
		@DisplayName("creates empty list")
		void createsEmptyList() {
			final var items = new List<>();

			assert Arrays.equals(items.store2,
				new String[]{});
		}
	}

	@Nested
	@DisplayName("List(Iterable<T>)")
	class NewWithIterableTests {
		@Test
		@DisplayName("creates list")
		void createsList() {
			final var items = new List<>(
				Arrays.asList("b", "Y", "u", "3"));

			assert Arrays.equals(items.store2,
				new String[]{
					"b", "Y", "u", "3"
				});
		}

		@Test
		@DisplayName("ignores null values")
		void ignoresNulls() {
			final var items = new List<>(
				Arrays.asList(null, "y", "5", null, "4", "5"));

			assert Arrays.equals(items.store2,
				new Object[]{
					"y", "5", "4", "5"
				});
		}

		@Test
		@DisplayName("creates empty list")
		void createsEmptyList() {
			final var items = new List<>(
				new LinkedList<String>());

			assert Arrays.equals(items.store2,
				new String[]{});
		}
	}

	@Nested
	@DisplayName(".getCount()")
	class GetCountTests {
		@Test
		@DisplayName("when list is not empty, returns item count")
		void returnsItemCountWhenNotEmpty() {
			final var items = new List<>("g", "T", "e", "e");
			assert items.getCount() == 4;
		}

		@Test
		@DisplayName("when list is empty, return 0")
		void returnsZeroWhenEmpty() {
			final var items = new List<>();
			assert items.getCount() == 0;
		}
	}

	@Nested
	@DisplayName(".get(int)")
	class GetAtIndexTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			private final List<String> items = new List<>("e", "D", "d", "E", "X");
			private final IndexRange validRange = new IndexRange(0, 5);

			@Test
			@DisplayName("when index is within valid range, returns item")
			void returnsItemWhenWithinValidRange() {
				assert items.get(2)
					.equals("d");
			}

			@Test
			@DisplayName("when index at valid range start, returns first item")
			void returnsItemAtFirstIndex() {
				assert items.get(0)
					.equals("e");
			}

			@Test
			@DisplayName("when index at valid range end, returns last item")
			void returnsItemAtLastIndex() {
				assert items.get(4)
					.equals("X");
			}

			@Test
			@DisplayName("when index before valid range, fails")
			void failsWhenIndexBeforeValidRange() {
				try {
					items.get(-1);
				} catch (IndexNotInRangeException exception) {
					assert -1 == exception.index;
					assert validRange.equals(exception.validRange);
					return;
				}
				assert false;
			}

			@Test
			@DisplayName("fails when index after valid range")
			void failsWhenIndexAfterValidRange() {
				try {
					items.get(5);
				} catch (IndexNotInRangeException exception) {
					assert 5 == exception.index;
					assert validRange.equals(exception.validRange);
					return;
				}
				assert false;
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			private final List<String> items = new List<>();
			private final IndexRange validRange = new IndexRange();

			@Test
			@DisplayName("fails")
			void failsWhenEmpty() {
				try {
					items.get(0);
				} catch (IndexNotInRangeException exception) {
					assert 0 == exception.index;
					assert validRange.equals(exception.validRange);
					return;
				}
				assert false;
			}
		}
	}

	@Nested
	@DisplayName(".get(IndexRange)")
	class GetAtIndexRangeTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			private final List<String> items1 = new List<>("r", "4", "v", "E", "P", "e", "Q");
			private final IndexRange validRange = new IndexRange(0, 7);

			@Test
			@DisplayName("when index range is within valid index range, returns items")
			void returnsItemsWhenWithinValidRange() {
				final var range = new IndexRange(2, 5);
				final var items2 = items1.get(range);

				assert Arrays.equals(items2.store2,
					new String[]{
						"v", "E", "P"
					});
			}

			@Test
			@DisplayName("when index range ends after valid range, fails")
			void failsWhenIndexRangeEndsAfterValidRange() {
				final var range = new IndexRange(5, 8);
				try {
					items1.get(range);
				} catch (IndexRangeNotInRangeException exception) {
					assert range.equals(exception.indexRange);
					assert validRange.equals(exception.validRange);
					return;
				}
				assert false;
			}

			@Test
			@DisplayName("when index range starts after valid range, fails")
			void failsWhenIndexRangeStartsAfterValidRange() {
				final var range = new IndexRange(7, 12);
				try {
					items1.get(range);
				} catch (IndexRangeNotInRangeException exception) {
					assert range.equals(exception.indexRange);
					assert validRange.equals(exception.validRange);
					return;
				}
				assert false;
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			private final List<String> items = new List<>();
			private final IndexRange validRange = new IndexRange();

			@Test
			@DisplayName("fails")
			void failsWhenEmpty() {
				final var range = new IndexRange(0, 1);
				try {
					items.get(range);
				} catch (IndexRangeNotInRangeException exception) {
					assert range.equals(exception.indexRange);
					assert validRange.equals(exception.validRange);
					return;
				}
				assert false;
			}
		}
	}

	@Nested
	@DisplayName(".getDistinct()")
	class GetDistinctTests {
		@Test
		@DisplayName("when list is not empty, returns distinct items")
		void returnsDistinctItemsWhenNotEmpty() {
			final var items = new List<>("a", "b", "5", "a", "4", "4");
			final var distinct = items.getDistinct();

			assert Arrays.equals(distinct.store2,
				new String[]{
					"a", "b", "5", "4"
				});
		}

		@Test
		@DisplayName("when list is empty, returns empty list")
		void returnsEmptyListWhenEmpty() {
			final var items = new List<String>();
			final var distinct = items.getDistinct();

			assert Arrays.equals(distinct.store2,
				new String[]{});
		}
	}

	@Nested
	@DisplayName(".reverse()")
	class ReverseTests {
		@Test
		@DisplayName("when list is not empty, returns reversed items")
		void returnsReversedItemsWhenNotEmpty() {
			final var items = new List<>("a", "b", "5", "a", "4", "4");
			final var reversed = items.reverse();

			assert Arrays.equals(reversed.store2,
				new String[]{
					"4", "4", "a", "5", "b", "a"
				});
		}

		@Test
		@DisplayName("when list is empty, returns empty list")
		void returnsEmptyListWhenEmpty() {
			final var items = new List<String>();
			final var distinct = items.reverse();

			assert Arrays.equals(distinct.store2,
				new String[]{});
		}
	}

	@Nested
	@DisplayName(".sort(Comparator<T>)")
	class SortTests {
		@Test
		@DisplayName("when list is not empty, returns sorted items")
		void returnsSortedItemsWhenNotEmpty() {
			final var items = new List<>(5, 6, 2, 1, 9, 0, 3, 3, 5);
			final var sorted = items.sort(Comparator.naturalOrder());

			assert Arrays.equals(sorted.store2,
				new Integer[]{
					0, 1, 2, 3, 3, 5, 5, 6, 9
				});
		}

		@Test
		@DisplayName("when list is empty, returns empty list")
		void returnsEmptyListWhenEmpty() {
			final var items = new List<Integer>();
			final var sorted = items.sort(Comparator.naturalOrder());

			assert Arrays.equals(sorted.store2,
				new Integer[]{});
		}
	}

	@Nested
	@DisplayName(".shuffle()")
	class ShuffleTests {
		@Test
		@DisplayName("when list is not empty, returns shuffled items")
		void returnsSortedItemsWhenNotEmpty() {
			final var items = new List<>(5, 6, 2, 1, 9, 0, 3, 3, 5);
			final var shuffled = items.shuffle();

			assert 9 == shuffled.store2.length;
			assert !Arrays.equals(items.store2, shuffled.store2);
		}

		@Test
		@DisplayName("when list is empty, returns empty list")
		void returnsEmptyListWhenEmpty() {
			final var items = new List<Integer>();
			final var shuffled = items.shuffle();

			assert Arrays.equals(shuffled.store2,
				new Integer[]{});
		}
	}

	@Nested
	@DisplayName(".filter(Predicate<T>)")
	class FilterTests {
		@Test
		@DisplayName("when list is not empty, returns filtered items")
		void returnsFilteredItemsWhenNotEmpty() {
			final var items = new List<>(2, 4, 3, 2, 1, 9);
			final var odds = items.filter((item) ->
				item % 2 == 1);

			assert Arrays.equals(odds.store2,
				new Integer[]{
					3, 1, 9
				});
		}

		@Test
		@DisplayName("when list is empty, returns empty list")
		void returnsEmptyListWhenEmpty() {
			final var items = new List<Integer>();
			final var evens = items.filter((item) ->
				item % 2 == 0);

			assert Arrays.equals(evens.store2,
				new Integer[]{});
		}
	}

	@Nested
	@DisplayName(".convert(Function<T, R>)")
	class ConvertTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			@Test
			@DisplayName("converts items")
			void returnsConvertedItems() {
				final var items = new List<>(3, 6, 1, 2);
				final var converted = items.convert((item) ->
					Integer.toString(item));

				assert Arrays.equals(converted.store2,
					new String[]{
						"3", "6", "1", "2"
					});
			}

			@Test
			@DisplayName("ignores items converted to null")
			void ignoresItemsConvertedToNull() {
				final var items = new List<>(4, 6, 2, 1, 7, 8, 5);
				final var converted = items.convert((item) ->
					item % 2 == 0
						? Integer.toString(item)
						: null);

				assert Arrays.equals(converted.store2,
					new String[]{
						"4", "6", "2", "8"
					});
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			@Test
			@DisplayName("returns empty list")
			void returnsEmptyList() {
				final var items = new List<Integer>();
				final var converted = items.convert((item) ->
					item * 2);

				assert Arrays.equals(converted.store2,
					new Integer[]{});
			}
		}
	}

	@Nested
	@DisplayName(".bridge()")
	class BridgeTests {
		@Test
		@DisplayName("when list is not empty, returns items in java.util.List")
		void returnsJavaListWhenNotEmpty() {
			final var items = new List<>("5", "y", "a", "v");
			final var bridged = items.bridge();

			assert java.util.List.of("5", "y", "a", "v")
				.equals(bridged);
		}

		@Test
		@DisplayName("when list is empty, returns empty java.util.List")
		void returnsEmptyJavaListWhenEmpty() {
			final var items = new List<>();
			final var bridged = items.bridge();

			assert java.util.List.of()
				.equals(bridged);
		}
	}

	@Nested
	@DisplayName(".toString()")
	class ToStringTests {
		@Test
		@DisplayName("when list is not empty, converts to string")
		void returnsStringWhenNotEmpty() {
			final var items = new List<>("g", "H", "6", "c", "E");
			final var string = items.toString();

			assert "[g, H, 6, c, E]"
				.equals(string);
		}

		@Test
		@DisplayName("when list is empty, returns []")
		void returnsStringWhenEmpty() {
			final var items = new List<>();
			final var string = items.toString();

			assert "[]"
				.equals(string);
		}
	}
}
