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
			final var set = new MutableSet<>("g", "Q", "h");
			final var returned = set.add("B");

			assert returned == set;
			assert new Set<>("g", "Q", "h", "B")
				.equals(set);
		}

		@Test
		@DisplayName("does not add null item")
		void doesNotAddNull() {
			final var set = new MutableSet<>("g", "Q", "h");
			final var returned = set.add(null);

			assert returned == set;
			assert new Set<>("g", "Q", "h")
				.equals(set);
		}

		@Test
		@DisplayName("adds first item")
		void addsFirstItem() {
			final var set = new MutableSet<>();
			final var returned = set.add("Q");

			assert returned == set;
			assert new Set<>("Q")
				.equals(set);
		}
	}
}
