package co.tsyba.core.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import static co.tsyba.core.collections.Assert.assertEquals;

class ContiguousArrayStoreTests {
	@Nested
	@DisplayName(".get(int, int)")
	class GetTests {
		@Nested
		@DisplayName("when store is not empty")
		class NotEmptyStoreTests {
			private final ContiguousArrayStore store1 = new ContiguousArrayStore(
				new String[]{
					"g", "e", "W", "h", "F", "s", "M", "V", null, null
				});

			@Test
			@DisplayName("when indexes in range, returns items in range")
			void returnsItemsInRangeWhenIndexesInRange() {
				final var store2 = store1.get(3, 7);

				assertItemCount(store2, 4);
				assertItems(store2,
					new String[]{
						"h", "F", "s", "M"
					});
			}

			@Test
			@DisplayName("when indexes equal, returns empty store")
			void returnsEmptyStoreWhenIndexesEqual() {
				final var store2 = store1.get(4, 4);

				assertItemCount(store2, 0);
				assertItems(store2,
					new String[]{
					});
			}

			@Test
			@DisplayName("when indexes are at start and end, returns copy of store")
			void returnsStoreCopyWhenIndexesAtStartAndEnd() {
				final var store2 = store1.get(0, 8);

				assertItemCount(store2, 8);
				assertItems(store2,
					new String[]{
						"g", "e", "W", "h", "F", "s", "M", "V"
					});
			}
		}

		@Nested
		@DisplayName("when store is empty")
		class EmptyStoreTests {
			private final ContiguousArrayStore store1 = new ContiguousArrayStore(
				new String[]{
					null, null
				});

			@Test
			@DisplayName("when indexes are at start and end, returns empty store")
			void returnsEmptyStoreWhenIndexesAtStartAndEnd() {
				final var store2 = store1.get(0, 0);

				assertItemCount(store2, 0);
				assertItems(store2,
					new String[]{
					});
			}
		}
	}

	@Nested
	@DisplayName(".findBefore(int, ContiguousArrayStore)")
	class FindBeforeStoreTests {
		@Nested
		@DisplayName("when store is not empty")
		class NotEmptyStoreTests {
			private final ContiguousArrayStore store1 = new ContiguousArrayStore(
				new String[]{
					"f", "h", "R", "f", "h", "e", "R", "f", "o"
				});

			@Nested
			@DisplayName("when argument store is not empty")
			class NotEmptyArgStoreTests {
				@Test
				@DisplayName("when items are present at argument index, returns their index")
				void returnsItemsIndexWhenPresentAtIndex() {
					final var store2 = new ContiguousArrayStore(
						new String[]{
							"R", "f"
						});

					final var index = store1.findBefore(6, store2);
					assertEquals(index, 6);
				}

				@Test
				@DisplayName("when items are present before argument index, returns their index")
				void returnsItemsIndexWhenPresentBeforeIndex() {
					final var store2 = new ContiguousArrayStore(
						new String[]{
							"R", "f"
						});

					final var index = store1.findBefore(5, store2);
					assertEquals(index, 2);
				}

				@Test
				@DisplayName("when items are absent before argument index, returns -1")
				void returnsNotFoundWhenItemsAbsentBeforeIndex() {
					final var store2 = new ContiguousArrayStore(
						new String[]{
							"h", "r", "R"
						});

					final var index = store1.findBefore(3, store2);
					assertEquals(index, -1);
				}

				@Test
				@DisplayName("when items are present at store start, returns their index")
				void returnsItemsIndexWhenItemsPresentAtStart() {
					final var store2 = new ContiguousArrayStore(
						new String[]{
							"f", "h", "R"
						});

					final var index = store1.findBefore(6, store2);
					assertEquals(index, 0);
				}

				@Test
				@DisplayName("when items are present at store end, returns their index")
				void returnsItemsIndexWhenItemsPresentAtEnd() {
					final var store2 = new ContiguousArrayStore(
						new String[]{
							"e", "R", "f", "o"
						});

					final var index = store1.findBefore(8, store2);
					assertEquals(index, 5);
				}

				@Test
				@DisplayName("when argument store is larger, returns -1")
				void returnsNotFoundWhenArgStoreLarger() {
					final var store2 = new ContiguousArrayStore(
						new String[]{
							"f", "h", "R", "f", "h", "e", "R", "f", "o", "R", "R", "f"
						});

					final var index = store1.findBefore(8, store2);
					assertEquals(index, -1);
				}
			}

			@Nested
			@DisplayName("when argument store is empty")
			class EmptyArgStoreTests {
				private final ContiguousArrayStore store2 = new ContiguousArrayStore(
					new String[]{
					});

				@Test
				@DisplayName("returns argument index")
				void returnsIndexArgIndex() {
					final var index = store1.findBefore(5, store2);
					assertEquals(index, 5);
				}

				@Test
				@DisplayName("when argument index equals first index, returns first index")
				void returnsFirstIndexArgIndexEqualsFirstIndex() {
					final var index = store1.findBefore(0, store2);
					assertEquals(index, 0);
				}

				@Test
				@DisplayName("when argument index equals last index, returns last index")
				void returnsLastIndexArgIndexEqualsLastIndex() {
					final var index = store1.findBefore(8, store2);
					assertEquals(index, 8);
				}

				@Test
				@DisplayName("when argument index is after last index, returns last index")
				void returnsLastIndexArgIndexAfterLastIndex() {
					final var index = store1.findBefore(17, store2);
					assertEquals(index, 8);
				}
			}
		}

		@Nested
		@DisplayName("when store is empty")
		class EmptyStoreTests {
			private final ContiguousArrayStore store1 = new ContiguousArrayStore(
				new String[]{
				});

			@Test
			@DisplayName("when argument store is not empty, returns -1")
			void returnsNotFoundWhenArgStoreNotEmpty() {
				final var store2 = new ContiguousArrayStore(
					new String[]{
						"g", "E", "f", "a"
					});

				final var index = store1.findBefore(0, store2);
				assertEquals(index, -1);
			}

			@Test
			@DisplayName("when argument store is empty, returns -1")
			void returnsNotFoundWhenArgStoreEmpty() {
				final var store2 = new ContiguousArrayStore(
					new String[]{
					});

				final var index = store1.findBefore(0, store2);
				assertEquals(index, -1);
			}
		}
	}

	@Nested
	@DisplayName(".findAfter(int, ContiguousArrayStore)")
	class FindAfterStoreTests {
		@Nested
		@DisplayName("when store is not empty")
		class NotEmptyStoreTests {
			private final ContiguousArrayStore store1 = new ContiguousArrayStore(
				new String[]{
					"g", "E", "q", "e", "e", "f", "E", "q", "e", "q", null, null
				});

			@Nested
			@DisplayName("when argument store is not empty")
			class NotEmptyArgStoreTests {
				@Test
				@DisplayName("when items are present at argument index, returns their index")
				void returnsItemsIndexWhenItemsPresentAtArgIndex() {
					final var store2 = new ContiguousArrayStore(
						new String[]{
							"E", "q", "e", null
						});

					final var index = store1.findAfter(6, store2);
					assertEquals(index, 6);
				}

				@Test
				@DisplayName("when items are present after argument index, returns their index")
				void returnsItemsIndexWhenItemsPresentAfterArgIndex() {
					final var store2 = new ContiguousArrayStore(
						new String[]{
							"E", "q", "e", null
						});

					final var index = store1.findAfter(2, store2);
					assertEquals(index, 6);
				}

				@Test
				@DisplayName("when items are absent after argument index, returns -1")
				void returnsNotFoundWhenItemsAbsentAfterArgIndex() {
					final var store2 = new ContiguousArrayStore(
						new String[]{
							"q", "e", "e", null, null
						});

					final var index = store1.findAfter(3, store2);
					assertEquals(index, -1);
				}

				@Test
				@DisplayName("when items are present at store start, returns their index")
				void returnsItemsIndexWhenItemsPresentAtStart() {
					final var store2 = new ContiguousArrayStore(
						new String[]{
							"g", "E", "q", null
						});

					final var index = store1.findAfter(0, store2);
					assertEquals(index, 0);
				}

				@Test
				@DisplayName("when items are present at store end, returns their index")
				void returnsItemsIndexWhenItemsPresentAtEnd() {
					final var store2 = new ContiguousArrayStore(
						new String[]{
							"q", "e", "q", null, null
						});

					final var index = store1.findAfter(0, store2);
					assertEquals(index, 7);
				}

				@Test
				@DisplayName("when argument store is larger, returns -1")
				void returnsNotFoundWhenArgStoreLarger() {
					final var store2 = new ContiguousArrayStore(
						new String[]{
							"g", "E", "q", "e", "e", "f", "E", "q", "e", "q", "R", "c", null
						});

					final var index = store1.findAfter(0, store2);
					assertEquals(index, -1);
				}

				@Test
				@DisplayName("when argument index is after last index, returns -1")
				void returnsNotFoundWhenArgIndexLargerThanItemCount() {
					final var store2 = new ContiguousArrayStore(
						new String[]{
							"g", "E"
						});

					final var index = store1.findAfter(27, store2);
					assertEquals(index, -1);
				}
			}

			@Nested
			@DisplayName("when argument store is empty")
			class EmptyArgStoreTests {
				private final ContiguousArrayStore store2 = new ContiguousArrayStore(
					new String[]{
						null, null
					});

				@Test
				@DisplayName("returns argument index")
				void returnsArgIndex() {
					final var index = store1.findAfter(4, store2);
					assertEquals(index, 4);
				}

				@Test
				@DisplayName("when argument index equals first index, returns first index")
				void returnsFirstIndexWhenArgIndexEqualsFirstIndex() {
					final var index = store1.findAfter(0, store2);
					assertEquals(index, 0);
				}

				@Test
				@DisplayName("when argument index equals last index, returns last index")
				void returnsLastIndexWhenArgIndexEqualsLastIndex() {
					final var index = store1.findAfter(9, store2);
					assertEquals(index, 9);
				}

				@Test
				@DisplayName("when argument index is after last index, returns -1")
				void returnsNotFoundWhenArgIndexAfterLastIndex() {
					final var index = store1.findAfter(10, store2);
					assertEquals(index, -1);
				}
			}
		}

		@Nested
		@DisplayName("when store is empty")
		class EmptyStoreTests {
			private final ContiguousArrayStore store1 = new ContiguousArrayStore(
				new String[]{
				});

			@Test
			@DisplayName("when argument store is not empty, returns -1")
			void returnsNotFoundWhenArgStoreIsNotEmpty() {
				final var store2 = new ContiguousArrayStore(
					new String[]{
						"g", "E", "A"
					});

				final var index = store1.findAfter(0, store2);
				assertEquals(index, -1);
			}

			@Test
			@DisplayName("when argument store is empty, returns -1")
			void returnsNotFoundWhenArgStoreIsEmpty() {
				final var store2 = new ContiguousArrayStore(
					new String[]{
					});

				final var index = store1.findAfter(0, store2);
				assertEquals(index, -1);
			}
		}
	}

	@Nested
	@DisplayName(".prepend(Object)")
	class PrependTests {
		@Test
		@DisplayName("when store has enough capacity, prepends item")
		void prependsItemWhenStoreHasEnoughCapacity() {
			final var store = new ContiguousArrayStore(
				new String[]{
					"5", "3", "6", "7", null, null, null
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
			final var store = new ContiguousArrayStore(
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
			final var store = new ContiguousArrayStore(
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
			final var store1 = new ContiguousArrayStore(
				new String[]{
					"f", "e", null, null, null
				});
			final var store2 = new ContiguousArrayStore(
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
			final var store1 = new ContiguousArrayStore(
				new String[]{
					"h", "e", "w", null, null
				});
			final var store2 = new ContiguousArrayStore(new String[]{
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
			final var store1 = new ContiguousArrayStore(
				new String[]{
					"f", "e", "q"
				});
			final var store2 = new ContiguousArrayStore(
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
			final var store1 = new ContiguousArrayStore(
				new String[]{
				});
			final var store2 = new ContiguousArrayStore(
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
			final var store = new ContiguousArrayStore(
				new String[]{
					"5", "3", "6", "7", null, null, null
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
			final var store = new ContiguousArrayStore(
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
			final var store = new ContiguousArrayStore(
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
			final var store1 = new ContiguousArrayStore(
				new String[]{
					"f", "e", null, null, null
				});
			final var store2 = new ContiguousArrayStore(
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
			final var store1 = new ContiguousArrayStore(
				new String[]{
					"h", "e", "w", null, null
				});
			final var store2 = new ContiguousArrayStore(
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
			final var store1 = new ContiguousArrayStore(
				new String[]{
					"f", "e", "q"
				});
			final var store2 = new ContiguousArrayStore(
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
			final var store1 = new ContiguousArrayStore(
				new String[]{
				});
			final var store2 = new ContiguousArrayStore(
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
			final var store = new ContiguousArrayStore(
				new String[]{
					"5", "3", "6", "7", null, null, null
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
			final var store = new ContiguousArrayStore(
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
			final var store1 = new ContiguousArrayStore(
				new String[]{
					"g", "3", "d", "W", null, null, null, null, null
				});
			final var store2 = new ContiguousArrayStore(
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
			final var store1 = new ContiguousArrayStore(
				new String[]{
					"g", "3", null
				});
			final var store2 = new ContiguousArrayStore(
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
			final var store1 = new ContiguousArrayStore(
				new String[]{
					"g", "3", "F"
				});
			final var store2 = new ContiguousArrayStore(
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
	@DisplayName(".replace(IndexRange, ContiguousArrayStore)")
	class ReplaceStoreTests {
		@Nested
		@DisplayName("when store has enough capacity")
		class EnoughCapacityTests {
			@Test
			@DisplayName("when index range and argument array are equal in length, replaces items")
			void replacesItemsWhenIndexRangeAndArgArrayEqualInLength() {
				final var store1 = new ContiguousArrayStore(
					new String[]{
						"f", "r", "w", "q", "t", null, null
					});

				final var range = new IndexRange(1, 3);
				final var store2 = new ContiguousArrayStore(
					new String[]{
						"W", "u"
					});

				store1.replace(range, store2);

				assertItemCount(store1, 5);
				assertItems(store1,
					new String[]{
						"f", "W", "u", "q", "t", null, null
					});
			}

			@Test
			@DisplayName("when index range is longer than argument array, replaces items")
			void replacesItemsWhenIndexRangeLongerThanArgArray() {
				final var store1 = new ContiguousArrayStore(
					new String[]{
						"f", "r", "w", "q", "t", null, null
					});

				final var range = new IndexRange(1, 4);
				final var store2 = new ContiguousArrayStore(
					new String[]{
						"W"
					});

				store1.replace(range, store2);

				assertItemCount(store1, 3);
				assertItems(store1,
					new String[]{
						"f", "W", "t", null, null, null, null
					});
			}

			@Test
			@DisplayName("when index range is shorter than argument array, replaces items")
			void replacesItemsWhenIndexRangeShorterThanArgArray() {
				final var store1 = new ContiguousArrayStore(
					new String[]{
						"f", "e", "W", "h", null, null, null
					});
				final var store2 = new ContiguousArrayStore(
					new String[]{
						"g", "b", "e", "O"
					});

				final var range = new IndexRange(0, 2);
				store1.replace(range, store2);

				assertItemCount(store1, 6);
				assertItems(store1,
					new String[]{
						"g", "b", "e", "O", "W", "h", null
					});
			}

			@Test
			@DisplayName("when index range is empty, inserts items")
			void insertsItemsWhenIndexRangeEmpty() {
				final var store1 = new ContiguousArrayStore(
					new String[]{
						"b", "g", "e", null, null, null, null, null
					});
				final var store2 = new ContiguousArrayStore(
					new String[]{
						"b", "u", "P"
					});

				final var range = new IndexRange(2, 2);
				store1.replace(range, store2);

				assertItemCount(store1, 6);
				assertItems(store1,
					new String[]{
						"b", "g", "b", "u", "P", "e", null, null
					});
			}

			@Test
			@DisplayName("when argument array is empty, removes items")
			void removesItemsWhenArgArrayEmpty() {
				final var store1 = new ContiguousArrayStore(
					new String[]{
						"b", "M", "L", "b", null
					});
				final var store2 = new ContiguousArrayStore(
					new String[]{
					});

				final var range = new IndexRange(1, 4);
				store1.replace(range, store2);

				assertItemCount(store1, 1);
				assertItems(store1,
					new String[]{
						"b", null, null, null, null
					});
			}

			@Test
			@DisplayName("when index range and argument array are empty, does nothing")
			void doesNothingWhenIndexRangeAndArgArrayEmpty() {
				final var store1 = new ContiguousArrayStore(
					new String[]{
						"b", "M", "L", "b", null
					});

				final var range = new IndexRange(1, 1);
				final var store2 = new ContiguousArrayStore(
					new String[]{
					});

				store1.replace(range, store2);

				assertItemCount(store1, 4);
				assertItems(store1,
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
				final var store1 = new ContiguousArrayStore(
					new String[]{
						"f", "e", "W", "h", null
					});

				final var range = new IndexRange(0, 2);
				final var store2 = new ContiguousArrayStore(
					new String[]{
						"g", "b", "e", "O"
					});

				store1.replace(range, store2);

				assertItemCount(store1, 6);
				assertItems(store1,
					new String[]{
						"g", "b", "e", "O", "W", "h", null, null, null, null, null, null
					});
			}

			@Test
			@DisplayName("when index range is empty, expands capacity and inserts items")
			void insertsItemsWhenIndexRangeEmpty() {
				final var store1 = new ContiguousArrayStore(
					new String[]{
						"b", "g", "e", null
					});
				final var store2 = new ContiguousArrayStore(
					new String[]{
						"b", "u", "P"
					});

				final var range = new IndexRange(2, 2);
				store1.replace(range, store2);

				assertItemCount(store1, 6);
				assertItems(store1,
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
			final var store = new ContiguousArrayStore(
				new String[]{
					"g", "r", "E", "x", null
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
			final var store = new ContiguousArrayStore(
				new String[]{
					"b", "E", "Q", "f", "L", "B", null
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

	@Nested
	@DisplayName(".reverse()")
	class ReverseTests {
		@Test
		@DisplayName("when store is not empty, reverses items")
		void reversesItemsWhenStoreNotEmpty() {
			final var store = new ContiguousArrayStore(
				new String[]{
					"v", "R", "e", "Q", null, null, null
				});

			final var reversed = store.reverse();

			assertItemCount(reversed, 4);
			assertItems(reversed,
				new String[]{
					"Q", "e", "R", "v", null, null, null
				});
		}

		@Test
		@DisplayName("when store is empty, does nothing")
		void doesNothingWhenStoreEmpty() {
			final var store = new ContiguousArrayStore(
				new String[]{
					null, null, null, null
				});

			final var reversed = store.reverse();

			assertItemCount(reversed, 0);
			assertItems(reversed,
				new String[]{
					null, null, null, null
				});
		}
	}

	@Nested
	@DisplayName(".sort(Comparator<T>)")
	class SortTests {
		@Test
		@DisplayName("when store is not empty, sorts items")
		void sortsItemsWhenStoreNotEmpty() {
			final var store = new ContiguousArrayStore(
				new String[]{
					"v", "R", "e", "C", "q", null, null
				});

			final var sorted = store.<String>sort(Comparator.naturalOrder());

			assertItemCount(sorted, 5);
			assertItems(sorted,
				new String[]{
					"C", "R", "e", "q", "v", null, null
				});

		}

		@Test
		@DisplayName("when store is empty, does nothing")
		void doesNothingWhenStoreEmpty() {
			final var store = new ContiguousArrayStore(
				new String[]{
					null, null, null, null
				});

			final var sorted = store.<String>sort(Comparator.naturalOrder());

			assertItemCount(sorted, 0);
			assertItems(sorted,
				new String[]{
					null, null, null, null
				});

		}
	}

	@Nested
	@DisplayName(".shuffle(Random)")
	class ShuffleTests {
		@Test
		@DisplayName("when store is not empty, shuffles items")
		void shufflesItemsWhenStoreNotEmpty() {
			final var store = new ContiguousArrayStore(
				new String[]{
					"v", "M", "l", "P", null, null, null
				});

			final var random = new Random();
			final var shuffled = store.shuffle(random);

			assertItemCount(shuffled, 4);
			assertItemsShuffled(shuffled, store, 4);
		}

		@Test
		@DisplayName("when store is empty, does nothing")
		void doesNothingWhenStoreEmpty() {
			final var store = new ContiguousArrayStore(
				new String[]{
					null, null, null, null
				});

			final var random = new Random();
			final var shuffled = store.shuffle(random);

			assertItemCount(shuffled, 0);
			assertItems(shuffled,
				new String[]{
					null, null, null, null
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

	static void assertItemsShuffled(ContiguousArrayStore shuffled, ContiguousArrayStore original, int itemCount) {
		// verify shuffled store preserves item capacity
		assert shuffled.items.length == original.items.length;

		// verify items part of shuffled store differs from the original
		assert !Arrays.equals(shuffled.items, 0, itemCount,
			original.items, 0, itemCount);

		// verify capacity part of shuffled store is the same as the original
		assert Arrays.equals(shuffled.items, itemCount, shuffled.items.length,
			original.items, itemCount, original.items.length);

		// verify shuffled array contains all items of the original
		Arrays.sort(shuffled.items, 0, itemCount);
		Arrays.sort(original.items, 0, itemCount);
		assert Arrays.equals(shuffled.items, 0, itemCount,
			original.items, 0, itemCount);
	}
}

// created by on Jan 28, 2019
