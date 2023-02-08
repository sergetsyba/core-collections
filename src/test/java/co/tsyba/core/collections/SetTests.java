package co.tsyba.core.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;

public class SetTests {
	@Nested
	@DisplayName("Set(T...)")
	class NewWithVarargsTests {
		@Test
		@DisplayName("creates set")
		void createsSet() {
			final var items = new Set<>(2, 7, 5, 4, 9);

			final var store = items.toArray();
			Arrays.sort(store);

			assert Arrays.equals(store,
				new Integer[]{
					2, 4, 5, 7, 9
				});
		}

		@Test
		@DisplayName("creates empty set")
		void createsEmptySet() {
			final var items = new Set<Integer>();

			final var store = items.toArray();
			assert 0 == store.length;
		}
	}

	@Nested
	@DisplayName(".getCount()")
	class GetCountTests {
		@Test
		@DisplayName("when set is not empty, returns item count")
		void returnsItemCountWhenNotEmpty() {
			final var items = new Set<>(5, 3, 2, 1, 0, 9, 7);
			final var count = items.getCount();
			assert 7 == count;
		}

		@Test
		@DisplayName("when set is empty, returns 0")
		void returnsZeroWhenEmpty() {
			final var items = new Set<>();
			final var count = items.getCount();
			assert 0 == count;
		}
	}

	@Nested
	@DisplayName(".contains(T)")
	class ContainsTests {
		@Nested
		@DisplayName("when set is not empty")
		class NotEmptySetTests {
			@Test
			@DisplayName("when item is present, returns true")
			void returnsTrueWhenPresent() {
				final var items = new Set<>(6, 4, 2, 0, 9, 7);
				assert items.contains(0);
			}

			@Test
			@DisplayName("when item is absent, returns false")
			void returnsFalseWhenAbsent() {
				final var items = new Set<>(6, 4, 2, 0, 9, 7);
				assert !items.contains(5);
			}
		}

		@Nested
		@DisplayName("when set is empty")
		class EmptySetTests {
			@Test
			@DisplayName("returns false")
			void returnsFalseWhenEmpty() {
				final var items = new Set<Integer>();
				assert !items.contains(0);
			}
		}
	}

	@Nested
	@DisplayName(".sort(Comparator<T>)")
	class SortTests {
		@Test
		@DisplayName("when set is not empty, returns sorted items")
		void returnsSortedItemsWhenNotEmpty() {
			final var items = new Set<>("g", "G", "k", "q", "0", "P");
			final var sorted = items.sort(Comparator.naturalOrder());

			assert new List<>("0", "G", "P", "g", "k", "q")
				.equals(sorted);
		}

		@Test
		@DisplayName("when set is empty, returns empty list")
		void returnsEmptyListWhenEmpty() {
			final var items = new Set<String>();
			final var sorted = items.sort(Comparator.naturalOrder());

			assert new List<String>()
				.equals(sorted);
		}
	}

	@Nested
	@DisplayName(".filter(Predicate<T>)")
	class FilterTests {
		@Test
		@DisplayName("when set is not empty, returns filtered items")
		void returnsFilteredItemsWhenNotEmpty() {
			final var items = new Set<>(2, 4, 3, 2, 1, 9);
			final var odds = items.filter((item) ->
				item % 2 == 1);

			assert new Set<>(3, 1, 9)
				.equals(odds);
		}

		@Test
		@DisplayName("when list is empty, returns empty list")
		void returnsEmptyListWhenEmpty() {
			final var items = new Set<Integer>();
			final var evens = items.filter((item) ->
				item % 2 == 0);

			assert new Set<Integer>()
				.equals(evens);
		}
	}

	@Nested
	@DisplayName(".convert(Function<T, R>)")
	class ConvertTests {
		@Nested
		@DisplayName("when set is not empty")
		class NotEmptySetTests {
			@Test
			@DisplayName("converts items")
			void returnsConvertedItems() {
				final var items = new Set<>(3, 6, 1, 2);
				final var converted = items.convert((item) ->
					Integer.toString(item));

				assert new Set<>("3", "6", "1", "2")
					.equals(converted);
			}

			@Test
			@DisplayName("ignores items converted to null")
			void ignoresItemsConvertedToNull() {
				final var items = new Set<>(4, 6, 2, 1, 7, 8, 5);
				final var converted = items.convert((item) ->
					item % 2 == 0
						? Integer.toString(item)
						: null);

				assert new Set<>("4", "6", "2", "8")
					.equals(converted);
			}
		}

		@Nested
		@DisplayName("when set is empty")
		class EmptyListTests {
			@Test
			@DisplayName("returns empty set")
			void returnsEmptyList() {
				final var items = new Set<Integer>();
				final var converted = items.convert((item) ->
					item * 2);

				assert new Set<Integer>()
					.equals(converted);
			}
		}
	}

	@Nested
	@DisplayName(".toString()")
	class ToStringTests {
		@Test
		@DisplayName("when set is not empty, converts to string")
		void returnsStringWhenNotEmpty() {
			final var items = new List<>("g", "H", "6", "c", "E");
			final var string = items.toString();

			assert "[g, H, 6, c, E]"
				.equals(string);
		}

		@Test
		@DisplayName("when set is empty, returns []")
		void returnsStringWhenEmpty() {
			final var items = new List<>();
			final var string = items.toString();

			assert "[]"
				.equals(string);
		}
	}
}
