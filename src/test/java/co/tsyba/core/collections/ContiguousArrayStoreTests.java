package co.tsyba.core.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

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
	@DisplayName(".prepend(Object[])")
	class PrependArrayTests {
		@Test
		@DisplayName("when store has enough capacity, prepends items")
		void prependsItemsWhenStoreHasEnoughCapacity() {
			final var store = new ContiguousArrayStore<>(5,
				new String[]{
					"f", "e"
				});

			store.prepend(
				new String[]{
					"g", "r", "W"
				});

			assertItemCount(store, 5);
			assertItems(store,
				new String[]{
					"g", "r", "W", "f", "e"
				});
		}

		@Test
		@DisplayName("when store does not have enough capacity, expands capacity and prepends items")
		void prependsItemsWhenStoreDoesNotHaveEnoughCapacity() {
			final var store = new ContiguousArrayStore<>(5,
				new String[]{
					"h", "e", "w"
				});

			store.prepend(
				new String[]{
					"g", "r", "W"
				});

			assertItemCount(store, 6);
			assertItems(store,
				new String[]{
					"g", "r", "W", "h", "e", "w", null, null, null, null, null, null
				});
		}

		@Test
		@DisplayName("when store is full, expands capacity and prepends items")
		void prependsItemsWhenStoreFull() {
			final var store = new ContiguousArrayStore<>(3,
				new String[]{
					"f", "e", "q"
				});

			store.prepend(
				new String[]{
					"f", "w"
				});

			assertItemCount(store, 5);
			assertItems(store,
				new String[]{
					"f", "w", "f", "e", "q", null, null, null, null, null
				});
		}

		@Test
		@DisplayName("when store is empty and has no capacity, expands capacity and prepends items")
		void prependsItemsWhenEmptyWithZeroCapacity() {
			final var store = new ContiguousArrayStore<>(0,
				new String[]{
				});

			store.prepend(
				new String[]{
					"v", "e"
				});

			assertItemCount(store, 2);
			assertItems(store,
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
	@DisplayName(".append(Object[])")
	class AppendArrayTests {
		@Test
		@DisplayName("when store has enough capacity, appends items")
		void appendsItemsWhenStoreHasEnoughCapacity() {
			final var store = new ContiguousArrayStore<>(5,
				new String[]{
					"f", "e"
				});

			store.append(
				new String[]{
					"g", "r", "W"
				});

			assertItemCount(store, 5);
			assertItems(store,
				new String[]{
					"f", "e", "g", "r", "W"
				});
		}

		@Test
		@DisplayName("when store does not have enough capacity, expands capacity and appends items")
		void appendsItemsWhenStoreDoesNotHaveEnoughCapacity() {
			final var store = new ContiguousArrayStore<>(5,
				new String[]{
					"h", "e", "w"
				});

			store.append(
				new String[]{
					"g", "r", "W"
				});

			assertItemCount(store, 6);
			assertItems(store,
				new String[]{
					"h", "e", "w", "g", "r", "W", null, null, null, null, null, null
				});
		}

		@Test
		@DisplayName("when store is full, expands capacity and appends items")
		void appendsItemsWhenStoreFull() {
			final var store = new ContiguousArrayStore<>(3,
				new String[]{
					"f", "e", "q"
				});

			store.append(
				new String[]{
					"f", "w"
				});

			assertItemCount(store, 5);
			assertItems(store,
				new String[]{
					"f", "e", "q", "f", "w", null, null, null, null, null
				});
		}

		@Test
		@DisplayName("when store is empty and has no capacity, expands capacity and appends items")
		void appendsItemsWhenEmptyWithZeroCapacity() {
			final var store = new ContiguousArrayStore<>(0,
				new String[]{
				});

			store.append(
				new String[]{
					"v", "e"
				});

			assertItemCount(store, 2);
			assertItems(store,
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
	@DisplayName(".insert(int, Object[])")
	class InsertArrayTests {
		@Test
		@DisplayName("when store has enough capacity, inserts items")
		void insertsItemsWhenStoreHasEnoughCapacity() {
			final var store = new ContiguousArrayStore<>(9,
				new String[]{
					"g", "3", "d", "W"
				});

			store.insert(3,
				new String[]{
					"b", "J", "L", "O"
				});

			assertItemCount(store, 8);
			assertItems(store,
				new String[]{
					"g", "3", "d", "b", "J", "L", "O", "W", null
				});
		}

		@Test
		@DisplayName("when store does not have enough capacity, expands capacity and inserts items")
		void insertsItemsWhenStoreDoesNotHaveEnoughCapacity() {
			final var store = new ContiguousArrayStore<>(3,
				new String[]{
					"g", "3"
				});

			store.insert(1,
				new String[]{
					"b", "J"
				});

			assertItemCount(store, 4);
			assertItems(store,
				new String[]{
					"g", "b", "J", "3", null, null, null, null
				});
		}

		@Test
		@DisplayName("when store is full, expands capacity and inserts items")
		void insertsItemsWhenStoreFull() {
			final var store = new ContiguousArrayStore<>(3,
				new String[]{
					"g", "3", "F"
				});

			store.insert(1,
				new String[]{
					"b", "J"
				});

			assertItemCount(store, 5);
			assertItems(store,
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


/*
 * Created by Serge Tsyba <tsyba@me.com> on Jan 28, 2019.
 */
class ContiguousArrayStoreLegacyTests {
	@Test
	public void resturnItem() {
		final var store = store("u", "z", "f", "s", "a");

		// first item
		assert store.get(0)
			.equals("u");
		// item in the middle
		assert store.get(3)
			.equals("s");
		// last item
		assert store.get(4)
			.equals("a");
	}

	//	@Test(expected = IndexNotInRangeException.class)
	public void failsToReturnItemBeforeFirstIndex() {
		store("u", "z", "f", "s", "a")
			.get(-1);
	}

	//	@Test(expected = IndexNotInRangeException.class)
	public void failsToReturnItemAfterLastIndex() {
		store("u", "z", "f", "s", "a")
			.get(5);
	}

	@Test
	public void returnsItems() {
		final var store = store("t", "c", "l", "p", "x");

		// index range at start
		final var range1 = new IndexRange(0, 3);
		assert store.get(range1)
			.equals(store("t", "c", "l"));

		// index range in the middle
		final var range2 = new IndexRange(2, 3);
		assert store.get(range2)
			.equals(store("l"));

		// range at end
		final var range3 = new IndexRange(2, 5);
		assert store.get(range3)
			.equals(store("l", "p", "x"));

		// full range
		final var range5 = new IndexRange(0, 5);
		assert store.get(range5)
			.equals(store);
	}

	//	@Test(expected = Exception.class)
	public void failsToReturnItemsBeforeFirstIndex() {
		final var indexRange = new IndexRange(-1, 2);
		store("t", "c", "l", "p", "x")
			.get(indexRange);
	}

	//	@Test(expected = Exception.class)
	public void failsToReturnItemsAfterLastIndex() {
		final var indexRange = new IndexRange(3, 7);
		store("t", "c", "l", "p", "x")
			.get(indexRange);
	}

	@Test
	public void setsItem() {
		final var store = store("f", "b", "r", "o", "w");

		// first item
		store.set(0, "g");
		assert store("g", "b", "r", "o", "w")
			.equals(store);

		// item in the middle
		store.set(3, "t");
		assert store("g", "b", "r", "t", "w")
			.equals(store);

		// last item
		store.set(4, "z");
		assert store("g", "b", "r", "t", "z")
			.equals(store);

		// null value
		store.set(3, null);
		assert store("g", "b", "r", "t", "z")
			.equals(store);
	}

	//	@Test(expected = IndexNotInRangeException.class)
	public void failsToSetItemBeforeFirstIndex() {
		store("f", "b", "r", "o", "w")
			.set(-1, "g");
	}

	//	@Test(expected = IndexNotInRangeException.class)
	public void failsToSetItemAfterLastIndex() {
		store("f", "b", "r", "o", "w")
			.set(5, "g");
	}

	@Test
	public void appendsItem() {
		final var store = store("t", "f", "h");

		// appends item
		store.append("r");
		store.append("o");

		assert store("t", "f", "h", "r", "o")
			.equals(store);

		// appends null
		store.append((String) null);
		store.append("v");
		store.append((String) null);

		assert store("t", "f", "h", "r", "o", "v")
			.equals(store);
	}

	@Test
	public void appendsItems() {
		final var store = store("t", "h", "b");

		// appends store
		final var store1 = store("r", "v");
		store.append(store1);

		assert store("t", "h", "b", "r", "v")
			.equals(store);

		// appends empty store
		final var store2 = store();
		store.append(store2);

		assert store("t", "h", "b", "r", "v")
			.equals(store);

		// appends itself
		store.append(store);
		assert store("t", "h", "b", "r", "v", "t", "h", "b", "r", "v")
			.equals(store);
	}

//	@Test
//	public void appendsVariardicItems() {
//		final var store = store("t", "h", "b");
//
//		// appends items
//		store.append("g", "f");
//		assert store("t", "h", "b", "g", "f")
//			.equals(store);
//
//		// does not append nulls
//		store.append(null, null, "r");
//		store.append("g", null);
//
//		assert store("t", "h", "b", "g", "f", "r", "g")
//			.equals(store);
//	}

	@Test
	public void insertsItem() {
		final var store = store("y", "q", "p");

		// inserts item
		store.insert(0, "c");
		store.insert(2, "o");

		assert store("c", "y", "o", "q", "p")
			.equals(store);

		// does not insert null
		store.insert(1, (String) null);
		store.insert(3, "z");
		store.insert(3, (String) null);

		assert store("c", "y", "o", "z", "q", "p")
			.equals(store);
	}

	//	@Test(expected = Exception.class)
	public void failsToInsertItemBeforeFirstIndex() {
		store("y", "q", "p")
			.insert(-1, "o");
	}

	//	@Test(expected = Exception.class)
	public void failsToInsertItemAfterLastIndex() {
		store("y", "q", "p")
			.insert(5, "o");
	}

	@Test
	public void insertsItems() {
		final var store = store("y", "f");

		// appends store
		final var store1 = store("e", "q", "p");
		store.insert(1, store1);

		assert store("y", "e", "q", "p", "f")
			.equals(store);

		// inserts empty store
		final var store2 = store();
		store.insert(4, store2);

		assert store("y", "e", "q", "p", "f")
			.equals(store);

		// inserts itself
		store.insert(4, store);
		assert store("y", "e", "q", "p", "y", "e", "q", "p", "f", "f")
			.equals(store);
	}

	//	@Test(expected = Exception.class)
	public void failsToInsertItemsFromStoreBeforeFirstIndex() {
		store("y", "f")
			.insert(-1, store("e", "q", "p"));
	}

	//	@Test(expected = Exception.class)
	public void failsToInsertItemsFromStoreAfterLastIndex() {
		store("y", "f")
			.insert(2, store("e", "q", "p"));
	}

	@Test
	public void removesItem() {
		final var store = store("t", "d", "s", "k", "j");

		// removes first item
		store.remove(0);
		assert store("d", "s", "k", "j")
			.equals(store);

		// removes middle item
		store.remove(2);
		assert store("d", "s", "j")
			.equals(store);

		// removes last item
		store.remove(2);
		assert store("d", "s")
			.equals(store);
	}

	//	@Test(expected = Exception.class)
	public void failsToRemoveItemBeforeFirstIndex() {
		store("t", "d", "s", "k", "j")
			.remove(-1);
	}

	//	@Test(expected = Exception.class)
	public void failsToRemoveItemAfterLastIndex() {
		store("t", "d", "s", "k", "j")
			.remove(5);
	}

	@Test
	public void removesItems() {
		// index range at start
		final var store1 = store("y", "i", "x", "p", "r");
		final var range1 = new IndexRange(0, 2);
		store1.remove(range1);

		assert store("x", "p", "r")
			.equals(store1);

		// index range in the middle
		final var store2 = store("y", "i", "x", "p", "r");
		final var range2 = new IndexRange(2, 3);
		store2.remove(range2);

		assert store("y", "i", "p", "r")
			.equals(store2);

		// range at end
		final var store3 = store("y", "i", "x", "p", "r");
		final var range3 = new IndexRange(2, 5);
		store3.remove(range3);

		assert store("y", "i")
			.equals(store3);

		// full range
		final var store5 = store("y", "i", "x", "p", "r");
		final var range5 = new IndexRange(0, 5);
		store5.remove(range5);

		assert store()
			.equals(store5);
	}

	//	@Test(expected = Exception.class)
	public void failsToRemoveItemsBeforeFirstIndex() {
		final var indexRange = new IndexRange(-1, 2);
		store("y", "i", "x", "p", "r")
			.remove(indexRange);
	}

	//	@Test(expected = Exception.class)
	public void failsToRemoveItemsAfterLastIndex() {
		final var indexRange = new IndexRange(3, 7);
		store("y", "i", "x", "p", "r")
			.remove(indexRange);
	}

	@Test
	public void reverses() {
		final var store = store("A", "c", "V", "M", "B");
		final var reversedStore = store.reverse();

		assert store("B", "M", "V", "c", "A")
			.equals(reversedStore);
	}

	@Test
	public void sorts() {
		final var store = store("i", "O", "b", "Q", "v");
		final var sortedStore = store.sort(
			Comparator.naturalOrder());

		assert store("O", "Q", "b", "i", "v")
			.equals(sortedStore);
	}

	@Test
	public void shuffles() {
		final var store = store("I", "R", "Z", "t", "w");
		final var random = new Random();
		final var shuffledStore = store.shuffle(random);

		assert !shuffledStore.equals(store);
		for (var item : shuffledStore) {
			assert Arrays.binarySearch(store.items, 0, 5, item) > -1;
		}
	}

	private static ContiguousArrayStore<String> store(String... items) {
		final var store = new ContiguousArrayStore<String>(items.length);
		store.append(items);

		return store;
	}
}
