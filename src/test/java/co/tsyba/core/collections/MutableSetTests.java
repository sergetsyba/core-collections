package co.tsyba.core.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

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

		@Test
		@DisplayName("when items are empty, does nothing")
		void doesNotAddEmptyItems() {
			final var items = new MutableSet<>("m", "Q", "K");
			final var returned = items.add();

			assert returned == items;
			assert new Set<>("m", "Q", "K")
				.equals(items);
		}
	}

	@Nested
	@DisplayName(".add(Collection<T>)")
	class AddCollectionTests {
		@Test
		@DisplayName("adds items")
		void addsItems() {
			final var items = new MutableSet<>("g", "b", "K");
			final var returned = items.add(
				new List<>("m", "Q", "k"));

			assert returned == items;
			assert new Set<>("g", "b", "K", "m", "Q", "k")
				.equals(items);
		}

		@Test
		@DisplayName("does not add duplicates")
		void doesNotAddDuplicates() {
			final var items = new MutableSet<>("g", "b", "K");
			final var returned = items.add(
				new List<>("K", "Q", "b"));

			assert returned == items;
			assert new Set<>("g", "b", "K", "Q")
				.equals(items);
		}

		@Test
		@DisplayName("adds first items")
		void addsFirstItems() {
			final var items = new MutableSet<String>();
			final var returned = items.add(
				new List<>("m", "Q", "K"));

			assert returned == items;
			assert new Set<>("m", "Q", "K")
				.equals(items);
		}

		@Test
		@DisplayName("when items are empty, does nothing")
		void doesNotAddEmptyItems() {
			final var items = new MutableSet<>("m", "Q", "K");
			final var returned = items.add(
				new List<>());

			assert returned == items;
			assert new Set<>("m", "Q", "K")
				.equals(items);
		}
	}

	@Nested
	@DisplayName(".add(Iterable<T>)")
	class AddIterableTests {
		@Test
		@DisplayName("adds items")
		void addsItems() {
			final var items = new MutableSet<>("g", "b", "K");
			final var returned = items.add(
				Arrays.asList("m", "Q", "k"));

			assert returned == items;
			assert new Set<>("g", "b", "K", "m", "Q", "k")
				.equals(items);
		}

		@Test
		@DisplayName("does not add duplicates")
		void doesNotAddDuplicates() {
			final var items = new MutableSet<>("g", "b", "K");
			final var returned = items.add(
				Arrays.asList("K", "Q", "b"));

			assert returned == items;
			assert new Set<>("g", "b", "K", "Q")
				.equals(items);
		}

		@Test
		@DisplayName("does not add nulls")
		void doesNotAddNulls() {
			final var items = new MutableSet<>("g", "b", "K");
			final var returned = items.add(
				Arrays.asList(null, "a", null, null));

			assert returned == items;
			assert new Set<>("g", "b", "K", "a")
				.equals(items);
		}

		@Test
		@DisplayName("adds first items")
		void addsFirstItems() {
			final var items = new MutableSet<>();
			final var returned = items.add(
				Arrays.asList("m", "Q", "K"));

			assert returned == items;
			assert new Set<>("m", "Q", "K")
				.equals(items);
		}

		@Test
		@DisplayName("when items are empty, does nothing")
		void doesNotAddEmptyItems() {
			final var items = new MutableSet<>("g", "b", "K");
			final var returned = items.add(
				new List<>());

			assert returned == items;
			assert new Set<>("g", "b", "K")
				.equals(items);
		}
	}

	@Nested
	@DisplayName(".remove(T)")
	class RemoveTests {
		@Test
		@DisplayName("when item is present, removes item")
		void removesItemWhenPresent() {
			final var items = new MutableSet<>("B", "Q", "1", "2", "h");
			final var returned = items.remove("2");

			assert returned == items;
			assert new Set<>("B", "Q", "1", "h")
				.equals(items);
		}

		@Test
		@DisplayName("when item is absent, does nothing")
		void doesNothingWhenAbsent() {
			final var items = new MutableSet<>("B", "Q", "1", "2", "h");
			final var returned = items.remove("7");

			assert returned == items;
			assert new Set<>("B", "Q", "1", "2", "h")
				.equals(items);
		}

		@Test
		@DisplayName("when item is absent, does nothing")
		void doesNothingWhenItemNull() {
			final var items = new MutableSet<>("B", "Q", "1", "2", "h");
			final var returned = items.remove("7");

			assert returned == items;
			assert new Set<>("B", "Q", "1", "2", "h")
				.equals(items);
		}

		@Test
		@DisplayName("when item is present and is last item, removes item")
		void removesLastItemWhenPresent() {
			final var items = new MutableSet<>("7");
			final var returned = items.remove("7");

			assert returned == items;
			assert new Set<String>()
				.equals(items);
		}

		@Test
		@DisplayName("when set is empty, does nothing")
		void doesNothingWhenEmpty() {
			final var items = new MutableSet<String>();
			final var returned = items.remove("7");

			assert returned == items;
			assert new Set<String>()
				.equals(items);
		}
	}

	@Nested
	@DisplayName(".remove(Collection<T>)")
	class RemoveCollectionTests {
		@Test
		@DisplayName("when some items are present, removes present items")
		void removesItemsWhenSomePresent() {
			final var items = new MutableSet<>("B", "Q", "1", "2", "h");
			final var returned = items.remove(
				new List<>("h", "q", "1", "B"));

			assert returned == items;
			assert new Set<>("Q", "2")
				.equals(items);
		}

		@Test
		@DisplayName("when all items are present, removes all items")
		void removesItemsWhenAllPresent() {
			final var items = new MutableSet<>("B", "Q", "1", "2", "h");
			final var returned = items.remove(
				new List<>("B", "Q", "1", "2", "h"));

			assert returned == items;
			assert new Set<>()
				.equals(items);
		}

		@Test
		@DisplayName("when all items are absent, does nothing")
		void doesNothingWhenAllAbsent() {
			final var items = new MutableSet<>("B", "Q", "1", "2", "h");
			final var returned = items.remove(
				new List<>("7", "4", "G", "b"));

			assert returned == items;
			assert new Set<>("B", "Q", "1", "2", "h")
				.equals(items);
		}

		@Test
		@DisplayName("when set is empty, does nothing")
		void doesNothingWhenEmpty() {
			final var items = new MutableSet<String>();
			final var returned = items.remove(
				new List<>("B", "Q", "1", "2", "h"));

			assert returned == items;
			assert new Set<String>()
				.equals(items);
		}
	}

	@Nested
	@DisplayName(".remove(T...)")
	class RemoveVarargsTests {
		@Test
		@DisplayName("when some items are present, removes present items")
		void removesItemsWhenSomePresent() {
			final var items = new MutableSet<>("B", "Q", "1", "2", "h");
			final var returned = items.remove("h", "q", "1", "B");

			assert returned == items;
			assert new Set<>("Q", "2")
				.equals(items);
		}

		@Test
		@DisplayName("when all items are present, removes all items")
		void removesItemsWhenAllPresent() {
			final var items = new MutableSet<>("B", "Q", "1", "2", "h");
			final var returned = items.remove("B", "Q", "1", "2", "h");

			assert returned == items;
			assert new Set<>()
				.equals(items);
		}

		@Test
		@DisplayName("when all items are absent, does nothing")
		void doesNothingWhenAllAbsent() {
			final var items = new MutableSet<>("B", "Q", "1", "2", "h");
			final var returned = items.remove("7", "4", "G", "b");

			assert returned == items;
			assert new Set<>("B", "Q", "1", "2", "h")
				.equals(items);
		}

		@Test
		@DisplayName("when some items are null, removes non-null items")
		void ignoresNullItems() {
			final var items = new MutableSet<>("B", "Q", "1", "2", "h");
			final var returned = items.remove(null, "Q", null, "2");

			assert returned == items;
			assert new Set<>("B", "1", "h")
				.equals(items);
		}

		@Test
		@DisplayName("when set is empty, does nothing")
		void doesNothingWhenEmpty() {
			final var items = new MutableSet<String>();
			final var returned = items.remove("B", "Q", "1", "2", "h");

			assert returned == items;
			assert new Set<String>()
				.equals(items);
		}
	}
}
