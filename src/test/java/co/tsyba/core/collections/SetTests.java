package co.tsyba.core.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

public class SetTests {
	@Nested
	@DisplayName("Set(Collection<T>)")
	class NewWithCollectionTests {
		@Test
		@DisplayName("creates set")
		void createsSet() {
			final var items = new Set<>(
				new List<>("v", "4", "G", "v", "5"));

			final var store = items.toArray();
			Arrays.sort(store);

			assert Arrays.equals(store,
				new String[]{
					"4", "5", "G", "v"
				});
		}

		@Test
		@DisplayName("creates empty set")
		void createsEmptySet() {
			final var items = new Set<>(
				new List<String>());

			final var store = items.toArray();
			assert 0 == store.length;
		}
	}

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
		@DisplayName("ignores null values")
		void ignoresNulls() {
			final var items = new Set<>("h", "5", null, "R", null, null);

			final var store = items.toArray();
			Arrays.sort(store);

			assert Arrays.equals(store,
				new Object[]{
					"5", "R", "h"
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
	@DisplayName("Set(Iterable<T>)")
	class NewWithIterableTests {
		@Test
		@DisplayName("creates set")
		void createsSet() {
			final var items = new Set<>(
				Arrays.asList("b", "Y", "u", "3"));

			final var store = items.toArray();
			Arrays.sort(store);

			assert Arrays.equals(store,
				new String[]{
					"3", "Y", "b", "u"
				});
		}

		@Test
		@DisplayName("ignores null values")
		void ignoresNulls() {
			final var items = new Set<>(
				Arrays.asList(null, "y", "5", null, "4", "5"));

			final var store = items.toArray();
			Arrays.sort(store);

			assert Arrays.equals(store,
				new Object[]{
					"4", "5", "y"
				});
		}

		@Test
		@DisplayName("creates empty set")
		void createsEmptySet() {
			final var items = new Set<Integer>(
				new LinkedList<>());

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
			private final Set<Integer> items = new Set<>(6, 4, 2, 0, 9, 7);

			@Test
			@DisplayName("when item is present, returns true")
			void returnsTrueWhenItemPresent() {
				assert items.contains(0);
			}

			@Test
			@DisplayName("when item is absent, returns false")
			void returnsFalseWhenItemAbsent() {
				assert !items.contains(5);
			}
		}

		@Nested
		@DisplayName("when set is empty")
		class EmptySetTests {
			private final Set<Integer> items = new Set<>();

			@Test
			@DisplayName("returns false")
			void returnsFalseWhenEmpty() {
				assert !items.contains(0);
			}
		}
	}

	@Nested
	@DisplayName(".unite(Set<T>)")
	class UniteTests {
		@Nested
		@DisplayName("when set is not empty")
		class NotEmptySetTests {
			private final Set<Integer> set1 = new Set<>(5, 4, 7, 8, 0);

			@Test
			@DisplayName("when other set is not empty, returns union")
			void returnsUnionWhenOtherSetNotEmpty() {
				final var set2 = new Set<>(5, 0, 2, 3);
				final var union = set1.unite(set2);

				assert new Set<>(0, 2, 3, 5, 4, 7, 8)
					.equals(union);
			}

			@Test
			@DisplayName("when other set is empty, returns items from set")
			void returnsUnionWhenOtherSetEmpty() {
				final var set2 = new Set<Integer>();
				final var union = set1.unite(set2);

				assert new Set<>(5, 4, 7, 8, 0)
					.equals(union);
			}

			@Test
			@DisplayName("when sets are equal, returns union")
			void returnsUnionWhenSetsEqual() {
				final var set2 = new Set<>(set1);
				final var intersection = set1.intersect(set2);

				assert new Set<>(5, 4, 7, 8, 0)
					.equals(intersection);
			}
		}

		@Nested
		@DisplayName("when set is empty")
		class EmptySetTests {
			private final Set<Integer> set1 = new Set<>();

			@Test
			@DisplayName("when other set is not empty, returns items from other set")
			void returnsUnionWhenOtherSetNotEmpty() {
				final var set2 = new Set<>(5, 0, 2, 3);
				final var union = set1.unite(set2);

				assert new Set<>(5, 0, 2, 3)
					.equals(union);
			}

			@Test
			@DisplayName("when other set is empty, returns empty set")
			void returnsUnionWhenOtherSetEmpty() {
				final var set2 = new Set<Integer>();
				final var union = set1.unite(set2);

				assert new Set<>()
					.equals(union);
			}
		}
	}

	@Nested
	@DisplayName(".intersect(Set<T>)")
	class IntersectTests {
		@Nested
		@DisplayName("when set is not empty")
		class NotEmptySetTests {
			private final Set<Integer> set1 = new Set<>(5, 4, 7, 8, 0);

			@Test
			@DisplayName("when other set is not empty, returns intersection")
			void returnsUnionWhenOtherSetNotEmpty() {
				final var set2 = new Set<>(5, 0, 2, 3);
				final var intersection = set1.intersect(set2);

				assert new Set<>(0, 5)
					.equals(intersection);
			}

			@Test
			@DisplayName("when other set is empty, returns empty set")
			void returnsUnionWhenOtherSetEmpty() {
				final var set2 = new Set<Integer>();
				final var intersection = set1.intersect(set2);

				assert new Set<>()
					.equals(intersection);
			}

			@Test
			@DisplayName("when sets are equal, returns intersection")
			void returnsUnionWhenSetsEqual() {
				final var set2 = new Set<>(set1);
				final var intersection = set1.intersect(set2);

				assert new Set<>(5, 4, 7, 8, 0)
					.equals(intersection);
			}
		}

		@Nested
		@DisplayName("when set is empty")
		class EmptySetTests {
			private final Set<Integer> set1 = new Set<>();

			@Test
			@DisplayName("when other set is not empty, returns empty set")
			void returnsUnionWhenOtherSetNotEmpty() {
				final var set2 = new Set<>(5, 0, 2, 3);
				final var intersection = set1.intersect(set2);

				assert new Set<>()
					.equals(intersection);
			}

			@Test
			@DisplayName("when other set is empty, returns empty set")
			void returnsUnionWhenOtherSetEmpty() {
				final var set2 = new Set<Integer>();
				final var intersection = set1.intersect(set2);

				assert new Set<>()
					.equals(intersection);
			}
		}
	}

	@Nested
	@DisplayName(".subtract(Set<T>)")
	class SubtractTests {
		@Nested
		@DisplayName("when set is not empty")
		class NotEmptySetTests {
			private final Set<Integer> set1 = new Set<>(5, 4, 7, 8, 0);

			@Test
			@DisplayName("when other set is not empty, returns difference")
			void returnsUnionWhenOtherSetNotEmpty() {
				final var set2 = new Set<>(5, 0, 2, 3);
				final var difference = set1.subtract(set2);

				assert new Set<>(4, 7, 8)
					.equals(difference);
			}

			@Test
			@DisplayName("when other set is empty, returns set")
			void returnsUnionWhenOtherSetEmpty() {
				final var set2 = new Set<Integer>();
				final var difference = set1.subtract(set2);

				assert new Set<>(5, 4, 7, 8, 0)
					.equals(difference);
			}

			@Test
			@DisplayName("when sets are equal, returns empty set")
			void returnsEmptySetWhenSetsEqual() {
				final var set2 = new Set<>(set1);
				final var difference = set1.subtract(set2);

				assert new Set<>()
					.equals(difference);
			}
		}

		@Nested
		@DisplayName("when set is empty")
		class EmptySetTests {
			private final Set<Integer> set1 = new Set<>();

			@Test
			@DisplayName("when other set is not empty, returns empty set")
			void returnsUnionWhenOtherSetNotEmpty() {
				final var set2 = new Set<>(5, 0, 2, 3);
				final var difference = set1.subtract(set2);

				assert new Set<>()
					.equals(difference);
			}

			@Test
			@DisplayName("when other set is empty, returns empty set")
			void returnsUnionWhenOtherSetEmpty() {
				final var set2 = new Set<Integer>();
				final var difference = set1.subtract(set2);

				assert new Set<>()
					.equals(difference);
			}
		}
	}

	@Nested
	@DisplayName(".disjoint(Set<T>)")
	class DisjointTests {
		@Nested
		@DisplayName("when set is not empty")
		class NotEmptySetTests {
			private final Set<Integer> set1 = new Set<>(5, 4, 7, 8, 0);

			@Test
			@DisplayName("when other set is not empty, returns symmetric difference")
			void returnsDifferenceWhenOtherSetNotEmpty() {
				final var set2 = new Set<>(5, 0, 2, 3);
				final var difference = set1.disjoint(set2);

				assert new Set<>(4, 7, 8, 2, 3)
					.equals(difference);
			}

			@Test
			@DisplayName("when other set is empty, returns set")
			void returnsDifferenceWhenOtherSetEmpty() {
				final var set2 = new Set<Integer>();
				final var difference = set1.disjoint(set2);

				assert new Set<>(5, 4, 7, 8, 0)
					.equals(difference);
			}

			@Test
			@DisplayName("when sets are equal, returns empty set")
			void returnsEmptySetWhenSetsEqual() {
				final var set2 = new Set<>(set1);
				final var difference = set1.disjoint(set2);

				assert new Set<>()
					.equals(difference);
			}
		}

		@Nested
		@DisplayName("when set is empty")
		class EmptySetTests {
			private final Set<Integer> set1 = new Set<>();

			@Test
			@DisplayName("when other set is not empty, returns other set")
			void returnsOtherSetWhenOtherSetNotEmpty() {
				final var set2 = new Set<>(5, 0, 2, 3);
				final var difference = set1.disjoint(set2);

				assert new Set<>(5, 0, 2, 3)
					.equals(difference);
			}

			@Test
			@DisplayName("when other set is empty, returns empty set")
			void returnsEmptySetWhenOtherSetEmpty() {
				final var set2 = new Set<Integer>();
				final var difference = set1.disjoint(set2);

				assert new Set<>()
					.equals(difference);
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
