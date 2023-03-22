package co.tsyba.core.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class ContiguousArrayStoreTests {
	@Nested
	@DisplayName(".prepend(Object)")
	class PrependTests {
		@Test
		@DisplayName("when store has enough capacity, prepends item")
		void prependsItemWhenStoreHasEnoughCapacity() {
			final var store = new ContiguousArrayStore<>(7,
				new String[]{
					"5", "3", "6", "7"
				});

			store.prepend("6");

			assertItemCount(store, 5);
			assertItems(store,
				new String[]{
					"6", "5", "3", "6", "7", null, null
				});
		}

		@Test
		@DisplayName("when store is full, expands capacity and prepends item")
		void prependsItemWhenStoreFull() {
			final var store = new ContiguousArrayStore<>(5,
				new String[]{
					"5", "3", "7", "5", "3"
				});

			store.prepend("6");

			assertItemCount(store, 6);
			assertItems(store,
				new String[]{
					"6", "5", "3", "7", "5", "3", null, null, null, null, null, null
				});
		}

		@Test
		@DisplayName("when store is empty with no capacity, expands capacity and prepends item")
		void prependsItemWhenStoreEmptyWithZeroCapacity() {
			final var store = new ContiguousArrayStore<>(0,
				new String[]{
				});

			store.prepend("8");

			assertItemCount(store, 1);
			assertItems(store, new String[]{
				"8", null
			});
		}
	}

	@Nested
	@DisplayName(".prepend(ContiguousArrayStore)")
	class PrependStoreTests {
		@Test
		@DisplayName("when store has enough capacity, prepends items")
		void prependsItemsWhenStoreHasEnoughCapacity() {
			final var store1 = new ContiguousArrayStore<>(5,
				new String[]{
					"f", "e"
				});
			final var store2 = new ContiguousArrayStore<>(
				new String[]{
					"g", "r", "W"
				});

			store1.prepend(store2);

			assertItemCount(store1, 5);
			assertItems(store1,
				new String[]{
					"g", "r", "W", "f", "e"
				});
		}

		@Test
		@DisplayName("when store does not have enough capacity, expands capacity and prepends items")
		void prependsItemsWhenStoreDoesNotHaveEnoughCapacity() {
			final var store1 = new ContiguousArrayStore<>(5,
				new String[]{
					"h", "e", "w"
				});
			final var store2 = new ContiguousArrayStore<>(new String[]{
				"g", "r", "W"
			});

			store1.prepend(store2);

			assertItemCount(store1, 6);
			assertItems(store1,
				new String[]{
					"g", "r", "W", "h", "e", "w", null, null, null, null, null, null
				});
		}

		@Test
		@DisplayName("when store is full, expands capacity and prepends items")
		void prependsItemsWhenStoreFull() {
			final var store1 = new ContiguousArrayStore<>(3,
				new String[]{
					"f", "e", "q"
				});
			final var store2 = new ContiguousArrayStore<>(
				new String[]{
					"f", "w"
				});

			store1.prepend(store2);

			assertItemCount(store1, 5);
			assertItems(store1,
				new String[]{
					"f", "w", "f", "e", "q", null, null, null, null, null
				});
		}

		@Test
		@DisplayName("when store is empty and has no capacity, expands capacity and prepends items")
		void prependsItemsWhenEmptyWithZeroCapacity() {
			final var store1 = new ContiguousArrayStore<>(0,
				new String[]{
				});
			final var store2 = new ContiguousArrayStore<>(
				new String[]{
					"v", "e"
				});

			store1.prepend(store2);

			assertItemCount(store1, 2);
			assertItems(store1,
				new String[]{
					"v", "e", null, null
				});
		}
	}

	@Nested
	@DisplayName(".append(Object)")
	class AppendTests {
		@Test
		@DisplayName("when store has enough capacity, appends item")
		void appendsItemWhenStoreHasEnoughCapacity() {
			final var store = new ContiguousArrayStore<>(7,
				new String[]{
					"5", "3", "6", "7"
				});

			store.append("6");

			assertItemCount(store, 5);
			assertItems(store,
				new String[]{
					"5", "3", "6", "7", "6", null, null
				});
		}

		@Test
		@DisplayName("when store is full, expands capacity and appends item")
		void appendsItemWhenStoreFull() {
			final var store = new ContiguousArrayStore<>(5,
				new String[]{
					"5", "3", "7", "5", "3"
				});

			store.append("6");

			assertItemCount(store, 6);
			assertItems(store,
				new String[]{
					"5", "3", "7", "5", "3", "6", null, null, null, null, null, null
				});
		}

		@Test
		@DisplayName("when store is empty with no capacity, expands capacity and appends item")
		void prependsItemWhenStoreEmptyWithZeroCapacity() {
			final var store = new ContiguousArrayStore<>(0,
				new String[]{
				});

			store.append("8");

			assertItemCount(store, 1);
			assertItems(store, new String[]{
				"8", null
			});
		}
	}

	@Nested
	@DisplayName(".append(ContiguousArrayStore)")
	class AppendStoreTests {
		@Test
		@DisplayName("when store has enough capacity, appends items")
		void appendsItemsWhenStoreHasEnoughCapacity() {
			final var store1 = new ContiguousArrayStore<>(5,
				new String[]{
					"f", "e"
				});
			final var store2 = new ContiguousArrayStore<>(
				new String[]{
					"g", "r", "W"
				});

			store1.append(store2);

			assertItemCount(store1, 5);
			assertItems(store1,
				new String[]{
					"f", "e", "g", "r", "W"
				});
		}

		@Test
		@DisplayName("when store does not have enough capacity, expands capacity and appends items")
		void appendsItemsWhenStoreDoesNotHaveEnoughCapacity() {
			final var store1 = new ContiguousArrayStore<>(5,
				new String[]{
					"h", "e", "w"
				});
			final var store2 = new ContiguousArrayStore<>(
				new String[]{
					"g", "r", "W"
				});

			store1.append(store2);

			assertItemCount(store1, 6);
			assertItems(store1,
				new String[]{
					"h", "e", "w", "g", "r", "W", null, null, null, null, null, null
				});
		}

		@Test
		@DisplayName("when store is full, expands capacity and appends items")
		void appendsItemsWhenStoreFull() {
			final var store1 = new ContiguousArrayStore<>(3,
				new String[]{
					"f", "e", "q"
				});
			final var store2 = new ContiguousArrayStore<>(
				new String[]{
					"f", "w"
				});

			store1.append(store2);

			assertItemCount(store1, 5);
			assertItems(store1,
				new String[]{
					"f", "e", "q", "f", "w", null, null, null, null, null
				});
		}

		@Test
		@DisplayName("when store is empty and has no capacity, expands capacity and appends items")
		void appendsItemsWhenEmptyWithZeroCapacity() {
			final var store1 = new ContiguousArrayStore<>(0,
				new String[]{
				});
			final var store2 = new ContiguousArrayStore<>(
				new String[]{
					"v", "e"
				});

			store1.append(store2);

			assertItemCount(store1, 2);
			assertItems(store1,
				new String[]{
					"v", "e", null, null
				});
		}
	}

	@Nested
	@DisplayName(".insert(int, Object)")
	class InsertTests {
		@Test
		@DisplayName("when store has enough capacity, inserts item")
		void insertsItemWhenStoreHasEnoughCapacity() {
			final var store = new ContiguousArrayStore<>(7,
				new String[]{
					"5", "3", "6", "7"
				});

			store.insert(2, "6");

			assertItemCount(store, 5);
			assertItems(store,
				new String[]{
					"5", "3", "6", "6", "7", null, null
				});
		}

		@Test
		@DisplayName("when store is full, expands capacity and inserts item")
		void appendsItemWhenStoreFull() {
			final var store = new ContiguousArrayStore<>(5,
				new String[]{
					"5", "3", "7", "5", "3"
				});

			store.insert(2, "6");

			assertItemCount(store, 6);
			assertItems(store,
				new String[]{
					"5", "3", "6", "7", "5", "3", null, null, null, null, null, null
				});
		}
	}

	@Nested
	@DisplayName(".insert(int, ContiguousArrayStore)")
	class InsertStoreTests {
		@Test
		@DisplayName("when store has enough capacity, inserts items")
		void insertsItemsWhenStoreHasEnoughCapacity() {
			final var store1 = new ContiguousArrayStore<>(9,
				new String[]{
					"g", "3", "d", "W"
				});
			final var store2 = new ContiguousArrayStore<>(
				new String[]{
					"b", "J", "L", "O"
				});

			store1.insert(3, store2);

			assertItemCount(store1, 8);
			assertItems(store1,
				new String[]{
					"g", "3", "d", "b", "J", "L", "O", "W", null
				});
		}

		@Test
		@DisplayName("when store does not have enough capacity, expands capacity and inserts items")
		void insertsItemsWhenStoreDoesNotHaveEnoughCapacity() {
			final var store1 = new ContiguousArrayStore<>(3,
				new String[]{
					"g", "3"
				});
			final var store2 = new ContiguousArrayStore<>(
				new String[]{
					"b", "J"
				});

			store1.insert(1, store2);

			assertItemCount(store1, 4);
			assertItems(store1,
				new String[]{
					"g", "b", "J", "3", null, null, null, null
				});
		}

		@Test
		@DisplayName("when store is full, expands capacity and inserts items")
		void insertsItemsWhenStoreFull() {
			final var store1 = new ContiguousArrayStore<>(3,
				new String[]{
					"g", "3", "F"
				});
			final var store2 = new ContiguousArrayStore<>(
				new String[]{
					"b", "J"
				});

			store1.insert(1, store2);

			assertItemCount(store1, 5);
			assertItems(store1,
				new String[]{
					"g", "b", "J", "3", "F", null, null, null, null, null
				});
		}
	}

	@Nested
	@DisplayName(".replace(IndexRange, Object[])")
	class ReplaceTests {
		@Nested
		@DisplayName("when store has enough capacity")
		class EnoughCapacityTests {
			@Test
			@DisplayName("when index range and argument array are equal in length, replaces items")
			void replacesItemsWhenIndexRangeAndArgArrayEqualInLength() {
				final var store = new ContiguousArrayStore<>(7,
					new String[]{
						"f", "r", "w", "q", "t"
					});

				final var range = new IndexRange(1, 3);
				store.replace(range,
					new String[]{
						"W", "u"
					});

				assertItemCount(store, 5);
				assertItems(store,
					new String[]{
						"f", "W", "u", "q", "t", null, null
					});
			}

			@Test
			@DisplayName("when index range is longer than argument array, replaces items")
			void replacesItemsWhenIndexRangeLongerThanArgArray() {
				final var store = new ContiguousArrayStore<>(7,
					new String[]{
						"f", "r", "w", "q", "t"
					});

				final var range = new IndexRange(1, 4);
				store.replace(range,
					new String[]{
						"W"
					});

				assertItemCount(store, 3);
				assertItems(store,
					new String[]{
						"f", "W", "t", null, null, null, null
					});
			}

			@Test
			@DisplayName("when index range is shorter than argument array, replaces items")
			void replacesItemsWhenIndexRangeShorterThanArgArray() {
				final var store = new ContiguousArrayStore<>(7,
					new String[]{
						"f", "e", "W", "h"
					});

				final var range = new IndexRange(0, 2);
				store.replace(range,
					new String[]{
						"g", "b", "e", "O"
					});

				assertItemCount(store, 6);
				assertItems(store,
					new String[]{
						"g", "b", "e", "O", "W", "h", null
					});
			}

			@Test
			@DisplayName("when index range is empty, inserts items")
			void insertsItemsWhenIndexRangeEmpty() {
				final var store = new ContiguousArrayStore<>(8,
					new String[]{
						"b", "g", "e"
					});

				final var range = new IndexRange(2, 2);
				store.replace(range,
					new String[]{
						"b", "u", "P"
					});

				assertItemCount(store, 6);
				assertItems(store,
					new String[]{
						"b", "g", "b", "u", "P", "e", null, null
					});
			}

			@Test
			@DisplayName("when argument array is empty, removes items")
			void removesItemsWhenArgArrayEmpty() {
				final var store = new ContiguousArrayStore<>(5,
					new String[]{
						"b", "M", "L", "b"
					});

				final var range = new IndexRange(1, 4);
				store.replace(range,
					new String[]{
					});

				assertItemCount(store, 1);
				assertItems(store,
					new String[]{
						"b", null, null, null, null
					});
			}

			@Test
			@DisplayName("when index range and argument array are empty, does nothing")
			void doesNothingWhenIndexRangeAndArgArrayEmpty() {
				final var store = new ContiguousArrayStore<>(5,
					new String[]{
						"b", "M", "L", "b"
					});

				final var range = new IndexRange(1, 1);
				store.replace(range,
					new String[]{
					});

				assertItemCount(store, 4);
				assertItems(store,
					new String[]{
						"b", "M", "L", "b", null
					});
			}
		}

		@Nested
		@DisplayName("when store does not have enough capacity")
		class NotEnoughCapacityTests {
			@Test
			@DisplayName("when index range is shorter than argument array, expands capacity and replaces items")
			void replacesItemsWhenIndexRangeShorterThanArgArray() {
				final var store = new ContiguousArrayStore<>(5,
					new String[]{
						"f", "e", "W", "h"
					});

				final var range = new IndexRange(0, 2);
				store.replace(range,
					new String[]{
						"g", "b", "e", "O"
					});

				assertItemCount(store, 6);
				assertItems(store,
					new String[]{
						"g", "b", "e", "O", "W", "h", null, null, null, null, null, null
					});
			}


			@Test
			@DisplayName("when index range is empty, expands capacity and inserts items")
			void insertsItemsWhenIndexRangeEmpty() {
				final var store = new ContiguousArrayStore<>(4,
					new String[]{
						"b", "g", "e"
					});

				final var range = new IndexRange(2, 2);
				store.replace(range,
					new String[]{
						"b", "u", "P"
					});

				assertItemCount(store, 6);
				assertItems(store,
					new String[]{
						"b", "g", "b", "u", "P", "e", null, null, null, null, null, null
					});
			}
		}
	}

	@Nested
	@DisplayName(".remove(int)")
	class RemoveTests {
		@Test
		@DisplayName("removes item")
		void removesItem() {
			final var store = new ContiguousArrayStore<>(5,
				new String[]{
					"g", "r", "E", "x"
				});

			store.remove(3);

			assertItemCount(store, 3);
			assertItems(store,
				new String[]{
					"g", "r", "E", null, null
				});
		}
	}

	@Nested
	@DisplayName(".remove(IndexRange)")
	class RemoveIndexRangeTests {
		@Test
		@DisplayName("removes items")
		void removesItems() {
			final var store = new ContiguousArrayStore<>(7,
				new String[]{
					"b", "E", "Q", "f", "L", "B"
				});

			final var range = new IndexRange(2, 5);
			store.remove(range);

			assertItemCount(store, 3);
			assertItems(store,
				new String[]{
					"b", "E", "B", null, null, null, null
				});
		}
	}

	static void assertItemCount(ContiguousArrayStore store, int expected) {
		assert store.itemCount == expected :
			String.format("Incorrect item count." +
					"\n\texpected: %d," +
					"\n\tactual:   %d",
				expected, store.itemCount);
	}

	static void assertItems(ContiguousArrayStore store, Object[] expected) {
		assert Arrays.equals(store.items, expected) :
			String.format("Incorrect items." +
					"\n\texpected: %s" +
					"\n\tactual:   %s",
				Arrays.toString(expected),
				Arrays.toString(store.items));
	}
}

// created by on Jan 28, 2019
