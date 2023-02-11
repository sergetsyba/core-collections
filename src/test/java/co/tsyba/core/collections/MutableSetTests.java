package co.tsyba.core.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class MutableSetTests {
	@Nested
	@DisplayName(".add(T)")
	class AddTests {
		@Test
		@DisplayName("adds item")
		void addsItem() {
			final var items = new MutableSet<>("g", "Q", "h");
			final var returned = items.add("B");

			assert returned == items;
			assert new Set<>("g", "Q", "h", "B")
				.equals(items);
		}

		@Test
		@DisplayName("does not add duplicate item")
		void doesNotAddDuplicate() {
			final var items = new MutableSet<>("g", "Q", "h");
			final var returned = items.add("Q");

			assert returned == items;
			assert new Set<>("g", "Q", "h")
				.equals(items);
		}

		@Test
		@DisplayName("does not add null item")
		void doesNotAddNull() {
			final var items = new MutableSet<>("g", "Q", "h");
			final var returned = items.add((String) null);

			assert returned == items;
			assert new Set<>("g", "Q", "h")
				.equals(items);
		}

		@Test
		@DisplayName("adds first item")
		void addsFirstItem() {
			final var items = new MutableSet<>();
			final var returned = items.add("Q");

			assert returned == items;
			assert new Set<>("Q")
				.equals(items);
		}
	}

	@Nested
	@DisplayName(".add(T...)")
	class AddVarargsTests {
		@Test
		@DisplayName("adds items")
		void addsItems() {
			final var items = new MutableSet<>("g", "b", "K");
			final var returned = items.add("m", "Q", "k");

			assert returned == items;
			assert new Set<>("g", "b", "K", "m", "Q", "k")
				.equals(items);
		}

		@Test
		@DisplayName("does not add duplicates")
		void doesNotAddDuplicates() {
			final var items = new MutableSet<>("g", "b", "K");
			final var returned = items.add("K", "Q", "b");

			assert returned == items;
			assert new Set<>("g", "b", "K", "Q")
				.equals(items);
		}

		@Test
		@DisplayName("does not add nulls")
		void doesNotAddNulls() {
			final var items = new MutableSet<>("g", "b", "K");
			final var returned = items.add(null, "a", null, null);

			assert returned == items;
			assert new Set<>("g", "b", "K", "a")
				.equals(items);
		}

		@Test
		@DisplayName("adds first items")
		void addsFirstItems() {
			final var items = new MutableSet<>();
			final var returned = items.add("m", "Q", "K");

			assert returned == items;
			assert new Set<>("m", "Q", "K")
				.equals(items);
		}
	}
}
