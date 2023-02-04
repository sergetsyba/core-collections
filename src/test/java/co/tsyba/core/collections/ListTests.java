package co.tsyba.core.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jul 10, 2019.
 */
public class ListTests {
	@Nested
	@DisplayName("List(List)")
	class NewWithListTests {
		@Test
		@DisplayName("creates list")
		void createsList() {
			final var items = new List<>("v", "4", "G", "5");
			final var copy = new List<>(items);

			assert Arrays.equals(copy.store.items,
				new String[]{
					"v", "4", "G", "5"
				});
		}

		@Test
		@DisplayName("creates empty list")
		void createsEmptyList() {
			final var items = new List<>();
			final var copy = new List<>(items);

			assert Arrays.equals(copy.store.items,
				new String[]{});
		}
	}

	@Nested
	@DisplayName("List(T...)")
	class NewWithArrayTests {
		@Test
		@DisplayName("creates list")
		void createsList() {
			final var items = new List<>("v", "4", "G", "5");

			assert Arrays.equals(items.store.items,
				new String[]{
					"v", "4", "G", "5"
				});
		}

		@Test
		@DisplayName("creates empty list")
		void createsEmptyList() {
			final var items = new List<>();

			assert Arrays.equals(items.store.items,
				new String[]{});
		}

		@Test
		@DisplayName("ignores null values")
		void ignoresNulls() {
			final var items = new List<>("h", "5", null, "R", null, null);

			assert Arrays.equals(items.store.items,
				new Object[]{
					"h", "5", "R"
				});
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

			assert Arrays.equals(items.store.items,
				new String[]{
					"b", "Y", "u", "3"
				});
		}

		@Test
		@DisplayName("creates empty list")
		void createsEmptyList() {
			final var items = new List<>(
				new LinkedList<String>());

			assert Arrays.equals(items.store.items,
				new String[]{});
		}

		@Test
		@DisplayName("ignores null values")
		void ignoresNulls() {
			final var items = new List<>(
				Arrays.asList(null, "y", "5", null, "4", "5"));

			assert Arrays.equals(items.store.items,
				new Object[]{
					"y", "5", "4", "5"
				});
		}
	}

	@Nested
	@DisplayName(".isEmpty()")
	class IsEmptyTests {
		@Test
		@DisplayName("returns true when list is empty")
		void returnsTrueWhenEmpty() {
			final var items = new List<>();
			assert items.isEmpty();
		}

		@Test
		@DisplayName("returns false when list is not empty")
		void returnsFalseWhenNotEmpty() {
			final var items = new List<>("h", "4", "G");
			assert !items.isEmpty();
		}
	}

	@Nested
	@DisplayName(".getCount()")
	class GetCountTests {
		@Test
		@DisplayName("returns item count")
		void returnsItemCount() {
			final var items = new List<>("g", "T", "e", "e");
			assert items.getCount() == 4;
		}

		@Test
		@DisplayName("return 0 when list is empty")
		void returnsZeroWhenEmpty() {
			final var items = new List<>();
			assert items.getCount() == 0;
		}
	}

	@Nested
	@DisplayName(".getFirst()")
	class GetFirstTests {
		@Test
		@DisplayName("returns first item")
		void returnsFirstItem() {
			final var items = new List<>("O", "T", "q", "M", "s");
			final var first = items.getFirst();

			assert first.isPresent();
			assert first.get()
				.equals("O");
		}

		@Test
		@DisplayName("returns empty when list is empty")
		void returnsEmptyWhenListIsEmpty() {
			final var items = new List<>();
			final var first = items.getFirst();

			assert first.isEmpty();
		}
	}

	@Nested
	@DisplayName(".getLast()")
	class GetLastTests {
		@Test
		@DisplayName("returns last item")
		void returnsLastItem() {
			final var items = new List<>("O", "T", "q", "M", "s");
			final var last = items.getLast();

			assert last.isPresent();
			assert last.get()
				.equals("s");
		}

		@Test
		@DisplayName("returns empty when list is empty")
		void returnsEmptyWhenListIsEmpty() {
			final var items = new List<>();
			final var first = items.getFirst();

			assert first.isEmpty();
		}
	}

	@Nested
	@DisplayName(".get(int)")
	class GetAtIndexTests {
		private final List<String> items = new List<>("e", "D", "d", "E", "X");
		// index range: [0, 4]

		@Test
		@DisplayName("returns item when index in valid range")
		void returnsItemWhenIndexInValidRange() {
			assert items.get(2)
				.equals("d");
		}

		@Test
		@DisplayName("returns item at first index")
		void returnsItemAtFirstIndex() {
			assert items.get(0)
				.equals("e");
		}

		@Test
		@DisplayName("returns item at last index")
		void returnsItemAtLastIndex() {
			assert items.get(4)
				.equals("X");
		}

		@Test
		@DisplayName("fails when index before valid range")
		void failsWhenIndexBeforeValidRange() {
			try {
				items.get(-1);
			} catch (IndexNotInRangeException ignored) {
				return;
			}
			assert false;
		}

		@Test
		@DisplayName("fails when index after valid range")
		void failsWhenIndexAfterValidRange() {
			try {
				items.get(5);
			} catch (IndexNotInRangeException ignored) {
				return;
			}
			assert false;
		}

		@Test
		@DisplayName("fails when list is empty")
		void failsWhenListIsEmpty() {
			try {
				new List<>()
					.get(0);
			} catch (IndexNotInRangeException ignored) {
				return;
			}
			assert false;
		}
	}

	@Nested
	@DisplayName(".get(IndexRange)")
	class GetAtIndexRangeTests {
		private final List<String> items = new List<>("r", "4", "v", "E", "P", "e", "Q");
		// index range: [0, 6]

		@Test
		@DisplayName("returns items at index range")
		void returnsItemsWhenIndexRangeInValidRange() {
			final var range = new IndexRange(2, 5);
			final var expected = new List<>("v", "E", "P");

			assert items.get(range)
				.equals(expected);
		}

		@Test
		@DisplayName("fails when index range ends after valid range")
		void failsWhenIndexRangeEndsAfterValidRange() {
			try {
				final var range = new IndexRange(5, 8);
				items.get(range);
			} catch (IndexRangeNotInRangeException ignored) {
				return;
			}
			assert false;
		}

		@Test
		@DisplayName("fails when index range after valid range")
		void failsWhenIndexRangeStartsAfterValidRange() {
			try {
				final var range = new IndexRange(7, 12);
				items.get(range);
			} catch (IndexRangeNotInRangeException ignored) {
				return;
			}
			assert false;
		}

		@Test
		@DisplayName("fails when list is empty")
		void failsWhenListIsEmpty() {
			try {
				final var range = new IndexRange(0, 1);
				new List<>()
					.get(range);
			} catch (IndexRangeNotInRangeException ignored) {
				return;
			}
			assert false;
		}
	}

	@Nested
	@DisplayName(".getDistinct()")
	class GetDistinctTests {
		@Test
		@DisplayName("returns distinct items")
		void returnsDistinctItems() {
			final var items = new List<>("a", "b", "5", "a", "4", "4");
			final var distinct = items.getDistinct();

			assert new List<>("a", "b", "5", "4")
				.equals(distinct);
		}

		@Test
		@DisplayName("returns empty list when list is empty")
		void returnsEmptyListWhenListIsEmpty() {
			final var items = new List<String>();
			final var distinct = items.getDistinct();

			assert new List<String>()
				.equals(distinct);
		}
	}

	@Nested
	@DisplayName(".filter(Predicate<T>)")
	class FilterTests {
		@Test
		@DisplayName("filters items")
		void filtersItems() {
			final var items = new List<>(2, 4, 3, 2, 1, 9);
			final var odds = items.filter((item) -> item % 2 == 1);

			assert new List<>(3, 1, 9)
				.equals(odds);
		}

		@Test
		@DisplayName("returns empty list when list is empty")
		void returnsEmptyListWhenListIsEmpty() {
			final var items = new List<Integer>();
			final var evens = items.filter((item) -> item % 2 == 0);

			assert new List<Integer>()
				.equals(evens);
		}
	}

	@Nested
	@DisplayName(".convert(Function<T, R>)")
	class ConvertTests {
		@Test
		@DisplayName("converts items")
		void convertsItems() {
			final var items = new List<>(3, 6, 1, 2);
			final var converted = items.convert((item) -> Integer.toString(item));

			assert new List<>("3", "6", "1", "2")
				.equals(converted);
		}

		@Test
		@DisplayName("ignores items converted to null")
		void ignoresItemsConvertedToNull() {
			final var items = new List<>(4, 6, 2, 1, 7, 8, 5);
			final var evens = items.convert((item) ->
				item % 2 == 0
					? Integer.toString(item)
					: null);

			assert new List<>("4", "6", "2", "8")
				.equals(evens);
		}

		@Test
		@DisplayName("returns empty list when list is empty")
		void returnsEmptyListWhenListIsEmpty() {
			final var items = new List<Integer>();
			final var evens = items.convert((item) -> item * 2);

			assert new List<>()
				.equals(evens);
		}
	}

	@Nested
	@DisplayName(".bridge()")
	class BridgeTests {
		@Test
		@DisplayName("bridges to java.util.List")
		void bridges() {
			final var items = new List<>("5", "y", "a", "v");
			final var bridged = items.bridge();

			assert java.util.List.of("5", "y", "a", "v")
				.equals(bridged);
		}

		@Test
		@DisplayName("bridges to empty java.util.List when empty")
		void bridgesToEmptyJavaListWhenEmpty() {
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
		@DisplayName("converts to string")
		void convertsToString() {
			final var items = new List<>("g", "H", "6", "c", "E");
			final var string = items.toString();

			assert "[g, H, 6, c, E]"
				.equals(string);
		}

		@Test
		@DisplayName("converts empty list to string")
		void convertsEmptyListToString() {
			final var items = new List<>();
			final var string = items.toString();

			assert "[]"
				.equals(string);
		}
	}
}
