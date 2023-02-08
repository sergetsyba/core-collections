package co.tsyba.core.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class SetTests {
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
}
