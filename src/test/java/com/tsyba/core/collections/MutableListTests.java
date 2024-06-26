package com.tsyba.core.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.tsyba.core.collections.Assertions.*;
import static com.tsyba.core.collections.MutableList.minimumCapacity;

class MutableListTests {
	@Nested
	@DisplayName("MutableList(T...)")
	class ConstructorVarargsTests {
		@Test
		@DisplayName("creates list")
		void createsList() {
			final var items = new MutableList<>("b", "d", "Q", "P", "G");

			assertCapacity(items, minimumCapacity);
			assertEquals(items,
				new String[]{
					"b", "d", "Q", "P", "G"
				});
		}

		@Test
		@DisplayName("when some items are null, creates list without nulls")
		void createsListWithoutNullsWhenSomeItemsNull() {
			final var items = new MutableList<>(null, "B", null, null, "G", null);

			assertCapacity(items, minimumCapacity);
			assertEquals(items,
				new String[]{
					"B", "G"
				});
		}

		@Test
		@DisplayName("when all items are null, creates empty list")
		void createsEmptyListWhenAllItemsNull() {
			final var items = new MutableList<String>(null, null, null);

			assertCapacity(items, minimumCapacity);
			assertEquals(items,
				new String[]{
				});
		}

		@Test
		@DisplayName("when argument array is empty, creates empty list")
		void createsEmptyListWhenArgArrayEmpty() {
			final var items = new MutableList<String>();

			assertCapacity(items, minimumCapacity);
			assertEquals(items,
				new String[]{
				});
		}
	}

	@Nested
	@DisplayName("MutableList(List<T>)")
	class ConstructorCollectionTests {
		@Test
		@DisplayName("creates list")
		void createsList() {
			final var items = new MutableList<>(
				new List<>("p", "g", "F", "q", "F"));

			assertCapacity(items, minimumCapacity);
			assertEquals(items,
				new String[]{
					"p", "g", "F", "q", "F"
				});
		}

		@Test
		@DisplayName("when argument list is empty, creates empty list")
		void createsEmptyListWhenArgListEmpty() {
			final var items = new MutableList<>(
				new List<String>());

			assertCapacity(items, minimumCapacity);
			assertEquals(items,
				new String[]{
				});
		}
	}

	@Nested
	@DisplayName(".set(int, T)")
	class SetTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			@Test
			@DisplayName("sets item")
			void setsItem() {
				final var items = new MutableList<>("g", "E", "x", "s");
				final var returned = items.set(2, "O");

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"g", "E", "O", "s"
					});
			}

			@Test
			@DisplayName("when item is null, does not set item")
			void doesNotSetItemWhenItemNull() {
				final var items = new MutableList<>("v", "E", "q", "A");
				final var returned = items.set(1, "M");

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"v", "M", "q", "A"
					});
			}

			@Test
			@DisplayName("when index is before valid range, fails")
			void failsWhenIndexBeforeValidRange() {
				assertThrows(() -> {
					new MutableList<>("g", "R", "c")
						.set(-2, "V");
				}, IndexNotInRangeException.class);
			}

			@Test
			@DisplayName("when index is after valid range, fails")
			void failsWhenIndexAfterValidRange() {
				assertThrows(() -> {
					new MutableList<>("g", "R", "c")
						.set(4, "V");
				}, IndexNotInRangeException.class);
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			@Test
			@DisplayName("when index is before valid range, fails")
			void failsWhenIndexBeforeValidRange() {
				final var expected = new IndexNotInRangeException(4,
					new IndexRange());

				assertThrows(() -> {
					new MutableList<>()
						.set(4, "V");
				}, expected);
			}

			@Test
			@DisplayName("when index is after valid range, fails")
			void failsWhenIndexAfterValidRange() {
				final var expected = new IndexNotInRangeException(0,
					new IndexRange());

				assertThrows(() -> {
					new MutableList<>()
						.set(0, "p");
				}, expected);
			}
		}
	}

	@Nested
	@DisplayName(".prepend(T)")
	class PrependTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			@Test
			@DisplayName("prepends item")
			void prependsItem() {
				final var items = new MutableList<>("v", "b", "f");
				final var returned = items.prepend("g");

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"g", "v", "b", "f"
					});
			}

			@Test
			@DisplayName("when item is null, does not prepend item")
			void doesNotPrependItemWhenItemNull() {
				final var items = new MutableList<>("v", "b", "f");
				final var returned = items.prepend((String) null);

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"v", "b", "f"
					});
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			@Test
			@DisplayName("prepends item")
			void prependsItem() {
				final var items = new MutableList<String>();
				final var returned = items.prepend("U");

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"U"
					});
			}

			@Test
			@DisplayName("when item is null, does not prepend item")
			void doesNotPrependItemWhenItemNull() {
				final var items = new MutableList<String>();
				final var returned = items.prepend((String) null);

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
					});
			}
		}
	}

	@SuppressWarnings("ConstantConditions")
	@Nested
	@DisplayName(".prepend(T...)")
	class PrependVarargsTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			@Test
			@DisplayName("prepends items")
			void prependsItems() {
				final var items = new MutableList<>("h", "V", "d");
				final var returned = items.prepend("n", "M");

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"n", "M", "h", "V", "d"
					});
			}

			@Test
			@DisplayName("when some items are null, prepends items without nulls")
			void prependsItemsWithoutNullsWhenSomeItemsNull() {
				final var items = new MutableList<>("g", "H");
				final var returned = items.prepend(null, "n", null, null, "O", null);

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"n", "O", "g", "H"
					});
			}

			@Test
			@DisplayName("when all items are null, does not prepend items")
			void doesNotPrependItemsWhenAllItemsNull() {
				final var items = new MutableList<>("f", "E", "q");
				final var returned = items.prepend(null, null, null);

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"f", "E", "q"
					});
			}

			@Test
			@DisplayName("when argument array is empty, does nothing")
			void doesNothingWhenArgArrayEmpty() {
				final var items = new MutableList<>("b", "R", "O");
				final var returned = items.prepend();

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"b", "R", "O"
					});
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			@Test
			@DisplayName("prepends items")
			void prependItems() {
				final var items = new MutableList<String>();
				final var returned = items.prepend("V", "B", "M");

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"V", "B", "M"
					});
			}

			@Test
			@DisplayName("when some items are null, prepends items without nulls")
			void prependsItemsWithoutNullsWhenSomeItemsNull() {
				final var items = new MutableList<String>();
				final var returned = items.prepend(null, "R", null, "V", null, null);

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"R", "V"
					});
			}

			@Test
			@DisplayName("when all items are null, does not prepend items")
			void doesNothingWhenAllItemsNull() {
				final var items = new MutableList<String>();
				final var returned = items.prepend(null, null);

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
					});
			}

			@Test
			@DisplayName("when argument array is empty, does nothing")
			void doesNothingWhenArgArrayEmpty() {
				final var items = new MutableList<String>();
				final var returned = items.prepend();

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
					});
			}
		}
	}

	@Nested
	@DisplayName(".prepend(List<T>)")
	class PrependListTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			@Test
			@DisplayName("prepends items")
			void prependsItems() {
				final var items = new MutableList<>("G", "f");
				final var returned = items.prepend(
					new List<>("F", "F", "a"));

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"F", "F", "a", "G", "f"
					});
			}

			@Test
			@DisplayName("when argument list is empty, does nothing")
			void doesNothingWhenArgListEmpty() {
				final var items = new MutableList<>("G", "f");
				final var returned = items.prepend(
					new List<>());

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"G", "f"
					});
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			@Test
			@DisplayName("prepends items")
			void prependsItems() {
				final var items = new MutableList<String>();
				final var returned = items.prepend(
					new List<>("V", "Q", "P"));

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"V", "Q", "P"
					});
			}

			@Test
			@DisplayName("when argument list is empty, does nothing")
			void doesNothingWhenArgListEmpty() {
				final var items = new MutableList<String>();
				final var returned = items.prepend(
					new List<>());

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
					});
			}
		}
	}

	@Nested
	@DisplayName(".append(T)")
	class AppendTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			@Test
			@DisplayName("appends item")
			void appendsItem() {
				final var items = new MutableList<>("v", "b", "f");
				final var returned = items.append("g");

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"v", "b", "f", "g"
					});
			}

			@Test
			@DisplayName("when item is null, does not append item")
			void doesNotAppendItemWhenItemNull() {
				final var items = new MutableList<>("v", "b", "f");
				final var returned = items.append((String) null);

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"v", "b", "f"
					});
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			@Test
			@DisplayName("appends item")
			void appendsItem() {
				final var items = new MutableList<String>();
				final var returned = items.append("U");

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"U"
					});
			}

			@Test
			@DisplayName("when item is null, does not append item")
			void doesNotAppendItemWhenItemNull() {
				final var items = new MutableList<String>();
				final var returned = items.append((String) null);

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
					});
			}
		}
	}

	@SuppressWarnings("ConstantConditions")
	@Nested
	@DisplayName(".append(T...)")
	class AppendVarargsTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			@Test
			@DisplayName("appends items")
			void appendsItems() {
				final var items = new MutableList<>("h", "V", "d");
				final var returned = items.append("n", "M");

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"h", "V", "d", "n", "M"
					});
			}

			@Test
			@DisplayName("when some items are null, appends items without nulls")
			void appendsItemsWithoutNullsWhenSomeItemsNull() {
				final var items = new MutableList<>("g", "H");
				final var returned = items.append(null, "n", null, null, "O", null);

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"g", "H", "n", "O"
					});
			}

			@Test
			@DisplayName("when all items are null, does not append items")
			void doesNotAppendItemsWhenAllItemsNull() {
				final var items = new MutableList<>("f", "E", "q");
				final var returned = items.append(null, null, null);

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"f", "E", "q"
					});
			}

			@Test
			@DisplayName("when argument array is empty, does nothing")
			void doesNothingWhenArgArrayEmpty() {
				final var items = new MutableList<>("b", "R", "O");
				final var returned = items.append();

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"b", "R", "O"
					});
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			@Test
			@DisplayName("appends items")
			void appendsItems() {
				final var items = new MutableList<String>();
				final var returned = items.append("V", "B", "M");

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"V", "B", "M"
					});
			}

			@Test
			@DisplayName("when argument array is empty, does nothing")
			void doesNothingWhenArgArrayEmpty() {
				final var items = new MutableList<String>();
				final var returned = items.append();

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
					});
			}
		}
	}

	@Nested
	@DisplayName(".append(List<T>)")
	class AppendListTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			@Test
			@DisplayName("appends items")
			void appendsItems() {
				final var items = new MutableList<>("G", "f");
				final var returned = items.append(
					new List<>("F", "F", "a"));

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"G", "f", "F", "F", "a"
					});
			}

			@Test
			@DisplayName("when argument list is empty, does nothing")
			void doesNothingWhenArgListEmpty() {
				final var items = new MutableList<>("G", "f");
				final var returned = items.append(
					new List<>());

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"G", "f"
					});
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			@Test
			@DisplayName("appends items")
			void appendsItems() {
				final var items = new MutableList<String>();
				final var returned = items.append(
					new List<>("V", "Q", "P"));

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"V", "Q", "P"
					});
			}

			@Test
			@DisplayName("when argument list is empty, does nothing")
			void doesNothingWhenArgListEmpty() {
				final var items = new MutableList<String>();
				final var returned = items.append(
					new List<>());

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
					});
			}
		}
	}

	@Nested
	@DisplayName(".insert(int, T)")
	class InsertTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			@Test
			@DisplayName("inserts item")
			void insertsItem() {
				final var items = new MutableList<>("F", "e", "Q", "T");
				final var returned = items.insert(2, "R");

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"F", "e", "R", "Q", "T"
					});
			}

			@Test
			@DisplayName("when item is null, does not insert item")
			void doesNotInsertItemWhenItemNull() {
				final var items = new MutableList<>("F", "e", "Q", "T");
				final var returned = items.insert(2, (String) null);

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"F", "e", "Q", "T"
					});
			}

			@Test
			@DisplayName("when index is before valid range, fails")
			void failsWhenIndexBeforeValidRange() {
				final var expected = new IndexNotInRangeException(-4,
					new IndexRange(0, 4));

				assertThrows(() -> {
					new MutableList<>("g", "E", "d", "A")
						.insert(-4, "r");
				}, expected);
			}

			@Test
			@DisplayName("when index is after valid range, fails")
			void failsWhenIndexAfterValidRange() {
				final var expected = new IndexNotInRangeException(9,
					new IndexRange(0, 2));

				assertThrows(() -> {
					new MutableList<>("g", "A")
						.insert(9, "r");
				}, expected);
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			@Test
			@DisplayName("when index is before valid range, fails")
			void failsWhenIndexBeforeValidRange() {
				final var expected = new IndexNotInRangeException(-3,
					new IndexRange());

				assertThrows(() -> {
					new MutableList<>()
						.insert(-3, "r");
				}, expected);
			}

			@Test
			@DisplayName("when index is after valid range, fails")
			void failsWhenIndexAfterValidRange() {
				final var expected = new IndexNotInRangeException(0,
					new IndexRange());

				assertThrows(() -> {
					new MutableList<>()
						.insert(0, "s");
				}, expected);
			}
		}
	}

	@SuppressWarnings("ConstantConditions")
	@Nested
	@DisplayName(".insert(int, T...)")
	class InsertVarargsTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			@Test
			@DisplayName("inserts items")
			void insertsItems() {
				final var items = new MutableList<>("v", "F", "G", "h");
				final var returned = items.insert(2, "F", "F");

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"v", "F", "F", "F", "G", "h"
					});
			}

			@Test
			@DisplayName("when some items are null, inserts items without nulls")
			void insertsItemsWithoutNullsWhenSomeItemsNull() {
				final var items = new MutableList<>("b", "a", "s");
				final var returned = items.insert(0, "F", null, null, "D");

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"F", "D", "b", "a", "s"
					});
			}

			@Test
			@DisplayName("when all items are null, does not insert items")
			void doesNotInsertItemsWhenAllItemsNull() {
				final var items = new MutableList<>("b", "a", "s");
				final var returned = items.insert(0, null, null);

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"b", "a", "s"
					});
			}

			@Test
			@DisplayName("when argument array is empty, does nothing")
			void doesNothingWhenArgArrayEmpty() {
				final var items = new MutableList<>("b", "a", "s");
				final var returned = items.insert(0);

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"b", "a", "s"
					});
			}

			@Test
			@DisplayName("when index is before valid range, fails")
			void failsWhenIndexBeforeValidRange() {
				final var expected = new IndexNotInRangeException(-4,
					new IndexRange(0, 4));

				assertThrows(() -> {
					new MutableList<>("b", "a", "X", "s")
						.insert(-4, "V", "s", "W");
				}, expected);
			}

			@Test
			@DisplayName("when index is after valid range, fails")
			void failsWhenIndexAfterValidRange() {
				final var expected = new IndexNotInRangeException(9,
					new IndexRange(0, 3));

				assertThrows(() -> {
					new MutableList<>("b", "X", "s")
						.insert(9, "V", "W");
				}, expected);
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			@Test
			@DisplayName("when index is before valid range, fails")
			void failsWhenIndexBeforeValidRange() {
				final var expected = new IndexNotInRangeException(-1,
					new IndexRange());

				assertThrows(() -> {
					new MutableList<>()
						.insert(-1, "f", "d", "Q");
				}, expected);
			}

			@Test
			@DisplayName("when index is after valid range, fails")
			void failsWhenIndexAfterValidRange() {
				final var expected = new IndexNotInRangeException(0,
					new IndexRange());

				assertThrows(() -> {
					new MutableList<>()
						.insert(0, "f", "d", "Q");
				}, expected);
			}
		}
	}

	@Nested
	@DisplayName(".insert(int, List<T>)")
	class InsertListTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			@Test
			@DisplayName("inserts items")
			void insertsItems() {
				final var items = new MutableList<>("v", "F", "G", "h");
				final var returned = items.insert(2,
					new List<>("F", "F"));

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"v", "F", "F", "F", "G", "h"
					});
			}

			@Test
			@DisplayName("when argument list is empty, does nothing")
			void doesNothingWhenArgListEmpty() {
				final var items = new MutableList<>("b", "a", "s");
				final var returned = items.insert(0,
					new List<>());

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"b", "a", "s"
					});
			}

			@Test
			@DisplayName("when index is before valid range, fails")
			void failsWhenIndexBeforeValidRange() {
				final var expected = new IndexNotInRangeException(-4,
					new IndexRange(0, 2));

				assertThrows(() -> {
					new MutableList<>("f", "g")
						.insert(-4,
							new List<>("R", "E"));
				}, expected);
			}

			@Test
			@DisplayName("when index is after valid range, fails")
			void failsWhenIndexAfterValidRange() {
				final var expected = new IndexNotInRangeException(9,
					new IndexRange(0, 3));

				assertThrows(() -> {
					new MutableList<>("b", "X", "s")
						.insert(9,
							new List<>("V", "W"));
				}, expected);
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			@Test
			@DisplayName("when index is before valid range, fails")
			void failsWhenIndexBeforeValidRange() {
				final var expected = new IndexNotInRangeException(-1,
					new IndexRange());

				assertThrows(() -> {
					new MutableList<>()
						.insert(-1,
							new List<>("f", "d", "Q"));
				}, expected);
			}

			@Test
			@DisplayName("when index is after valid range, fails")
			void failsWhenIndexAfterValidRange() {
				final var expected = new IndexNotInRangeException(0,
					new IndexRange());

				assertThrows(() -> {
					new MutableList<>()
						.insert(0,
							new List<>("f", "d", "Q"));
				}, expected);
			}
		}
	}

	@SuppressWarnings("ConstantConditions")
	@Nested
	@DisplayName(".replace(IndexRange, T...)")
	class ReplaceVarargsTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			@Test
			@DisplayName("replaces items")
			void replacesItems() {
				final var items = new MutableList<>("f", "e", "q", "R", "w");
				final var range = new IndexRange(2, 4);
				final var returned = items.replace(range, "a", "T", "f", "s");

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"f", "e", "a", "T", "f", "s", "w"
					});
			}

			@Test
			@DisplayName("when some argument items are null, replaces items without nulls")
			void replacesItemsWithoutNullsWhenSomeArgItemsNull() {
				final var items = new MutableList<>("f", "e", "q", "R", "w");
				final var range = new IndexRange(2, 5);
				final var returned = items.replace(range, null, "b", null, null, "n");

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"f", "e", "b", "n"
					});
			}

			@Test
			@DisplayName("when all argument items are null, deletes items")
			void deletesItemsWhenAllArgItemsNull() {
				final var items = new MutableList<>("f", "e", "q", "R", "w");
				final var range = new IndexRange(1, 3);
				final var returned = items.replace(range, null, null);

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"f", "R", "w"
					});
			}

			@Test
			@DisplayName("when argument array is empty, deletes items")
			void deletesItemsWhenAllArgArrayEmpty() {
				final var items = new MutableList<>("f", "e", "q", "R", "w");
				final var range = new IndexRange(2, 3);
				final var returned = items.replace(range);

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"f", "e", "R", "w"
					});
			}

			@Test
			@DisplayName("when index range and argument array are empty, does nothing")
			void doesNothingWhenIndexRangeAndArgArrayEmpty() {
				final var items = new MutableList<>("f", "e", "q", "R", "w");
				final var range = new IndexRange(2, 2);
				final var returned = items.replace(range);

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"f", "e", "q", "R", "w"
					});
			}

			@Test
			@DisplayName("when index range is empty, prepends items")
			void insertsItemsWhenIndexRangeEmpty() {
				final var items = new MutableList<>("v", "a", "f", "j");
				final var range = new IndexRange();
				final var returned = items.replace(range, "v", "m");

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"v", "m", "v", "a", "f", "j"
					});
			}

			@Test
			@DisplayName("when index range starts after valid range, fails")
			void failsWhenIndexRangeStartsAfterValidRange() {
				final var range = new IndexRange(7, 17);
				final var expected = new IndexRangeNotInRangeException(range,
					new IndexRange(0, 4));

				assertThrows(() -> {
					new MutableList<>("b", "b", "g", "e")
						.replace(range, "G", "s", "e");
				}, expected);
			}

			@Test
			@DisplayName("when index range ends after valid range, fails")
			void failsWhenIndexRangeEndsAfterValidRange() {
				final var range = new IndexRange(2, 17);
				final var expected = new IndexRangeNotInRangeException(range,
					new IndexRange(0, 4));

				assertThrows(() -> {
					new MutableList<>("b", "b", "g", "e")
						.replace(range, "G", "s", "e");
				}, expected);
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			@Test
			@DisplayName("when index range is empty, prepends items")
			void failsWhenIndexRangeCoincidesValidRange() {
				final var items = new MutableList<String>();
				final var range = new IndexRange();
				final var returned = items.replace(range, "v", "m");

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"v", "m"
					});
			}

			@Test
			@DisplayName("when index range starts after valid range, fails")
			void failsWhenIndexRangeStartsAfterValidRange() {
				final var range = new IndexRange(3, 7);
				final var expected = new IndexRangeNotInRangeException(range,
					new IndexRange());

				assertThrows(() -> {
					new MutableList<String>()
						.replace(range, "G", "v", "Q");
				}, expected);
			}

			@Test
			@DisplayName("when index range ends after valid range, fails")
			void failsWhenIndexRangeEndsAfterValidRange() {
				final var range = new IndexRange(0, 1);
				final var expected = new IndexRangeNotInRangeException(range,
					new IndexRange());

				assertThrows(() -> {
					new MutableList<String>()
						.replace(range, "G", "v", "Q");
				}, expected);
			}
		}
	}

	@SuppressWarnings("ConstantConditions")
	@Nested
	@DisplayName(".replace(IndexRange, List<T>)")
	class ReplaceListTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			@Test
			@DisplayName("replaces items")
			void replacesItems() {
				final var items = new MutableList<>("f", "e", "q", "R", "w");
				final var range = new IndexRange(2, 4);
				final var returned = items.replace(range,
					new List<>("a", "T", "f", "s"));

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"f", "e", "a", "T", "f", "s", "w"
					});
			}

			@Test
			@DisplayName("when index range is empty, prepends items")
			void insertsItemsWhenIndexRangeEmpty() {
				final var items = new MutableList<>("v", "a", "f", "j");
				final var range = new IndexRange(2, 2);
				final var returned = items.replace(range,
					new List<>("v", "m"));

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"v", "m", "v", "a", "f", "j"
					});
			}

			@Test
			@DisplayName("when argument list is empty, deletes items")
			void deletesItemsWhenAllArgListEmpty() {
				final var items = new MutableList<>("f", "e", "q", "R", "w");
				final var range = new IndexRange(2, 4);
				final var returned = items.replace(range,
					new List<>());

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"f", "e", "w"
					});
			}

			@Test
			@DisplayName("when index range and argument list are empty, does nothing")
			void doesNothingWhenIndexRangeAndArgListEmpty() {
				final var items = new MutableList<>("f", "e", "q", "R", "w");
				final var range = new IndexRange(2, 2);
				final var returned = items.replace(range,
					new List<>());

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"f", "e", "q", "R", "w"
					});
			}

			@Test
			@DisplayName("when index range starts after valid range, fails")
			void failsWhenIndexRangeStartsAfterValidRange() {
				final var range = new IndexRange(5, 17);
				final var expected = new IndexRangeNotInRangeException(range,
					new IndexRange(0, 4));

				assertThrows(() -> {
					new MutableList<>("b", "b", "g", "e")
						.replace(range,
							new List<>("G", "s", "e"));
				}, expected);
			}

			@Test
			@DisplayName("when index range ends after valid range, fails")
			void failsWhenIndexRangeEndsAfterValidRange() {
				final var range = new IndexRange(2, 17);
				final var expected = new IndexRangeNotInRangeException(range,
					new IndexRange(0, 4));

				assertThrows(() -> {
					new MutableList<>("b", "b", "g", "e")
						.replace(range,
							new List<>("G", "s", "e"));
				}, expected);
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			@Test
			@DisplayName("when index range is empty, prepends items")
			void failsWhenIndexRangeCoincidesValidRange() {
				final var items = new MutableList<String>();
				final var range = new IndexRange();
				final var returned = items.replace(range,
					new List<>("G", "v", "Q"));

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"G", "v", "Q"
					});
			}

			@Test
			@DisplayName("when index range starts after valid range, fails")
			void failsWhenIndexRangeStartsAfterValidRange() {
				final var range = new IndexRange(1, 2);
				final var expected = new IndexRangeNotInRangeException(range,
					new IndexRange());

				assertThrows(() -> {
					new MutableList<String>()
						.replace(range,
							new List<>("G", "v", "Q"));
				}, expected);
			}

			@Test
			@DisplayName("when index range ends after valid range, fails")
			void failsWhenIndexRangeEndsAfterValidRange() {
				final var range = new IndexRange(0, 2);
				final var expected = new IndexRangeNotInRangeException(range,
					new IndexRange());

				assertThrows(() -> {
					new MutableList<String>()
						.replace(range,
							new List<>("G", "v", "Q"));
				}, expected);
			}
		}
	}

	@Nested
	@DisplayName(".removeFirst()")
	class RemoveFirstTests {
		@Test
		@DisplayName("when list is not empty, removes first item")
		void removesFirstItemWhenListNotEmpty() {
			final var items = new MutableList<>("g", "e", "v", "Q");
			final var returned = items.removeFirst();

			Assertions.assertIs(returned, items);
			assertEquals(items,
				new String[]{
					"e", "v", "Q"
				});
		}

		@Test
		@DisplayName("when list is empty, does nothing")
		void doesNothingWhenListEmpty() {
			final var items = new MutableList<String>();
			final var returned = items.removeFirst();

			Assertions.assertIs(returned, items);
			assertEquals(items,
				new String[]{
				});
		}
	}

	@Nested
	@DisplayName(".removeLast()")
	class RemoveLastTests {
		@Test
		@DisplayName("when list is not empty, removes last item")
		void removesLastItemWhenListNotEmpty() {
			final var items = new MutableList<>("g", "e", "v", "Q");
			final var returned = items.removeLast();

			Assertions.assertIs(returned, items);
			assertEquals(items,
				new String[]{
					"g", "e", "v"
				});
		}

		@Test
		@DisplayName("when list is empty, does nothing")
		void doesNothingWhenListEmpty() {
			final var items = new MutableList<String>();
			final var returned = items.removeLast();

			Assertions.assertIs(returned, items);
			assertEquals(items,
				new String[]{
				});
		}
	}

	@Nested
	@DisplayName(".remove(int)")
	class RemoveTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			@Test
			@DisplayName("removes item")
			void removesItem() {
				final var items = new MutableList<>("g", "k", "U", "P");
				final var returned = items.remove(2);

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"g", "k", "P"
					});
			}

			@Test
			@DisplayName("when index is before valid range, fails")
			void failsWhenIndexBeforeValidRange() {
				final var expected = new IndexNotInRangeException(-9,
					new IndexRange(0, 2));

				assertThrows(() -> {
					new MutableList<>("f", "g")
						.remove(-9);
				}, expected);
			}

			@Test
			@DisplayName("when index is after valid range, fails")
			void failsWhenIndexAfterValidRange() {
				final var expected = new IndexNotInRangeException(7,
					new IndexRange(0, 2));

				assertThrows(() -> {
					new MutableList<>("f", "g")
						.remove(7);
				}, expected);
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			@Test
			@DisplayName("when index is before valid range, fails")
			void failsWhenIndexBeforeValidRange() {
				final var expected = new IndexNotInRangeException(-9,
					new IndexRange());

				assertThrows(() -> {
					new MutableList<>()
						.remove(-9);
				}, expected);
			}

			@Test
			@DisplayName("when index is after valid range, fails")
			void failsWhenIndexAfterValidRange() {
				final var expected = new IndexNotInRangeException(0,
					new IndexRange());

				assertThrows(() -> {
					new MutableList<>()
						.remove(0);
				}, expected);
			}
		}
	}

	@Nested
	@DisplayName(".remove(IndexRange)")
	class RemoveIndexRangeTests {
		@Nested
		@DisplayName("when list is not empty")
		class NotEmptyListTests {
			@Test
			@DisplayName("removes items")
			void removesItems() {
				final var items = new MutableList<>("g", "w", "Q", "c", "j", "P");
				final var returned = items.remove(
					new IndexRange(1, 4));

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"g", "j", "P"
					});
			}

			@Test
			@DisplayName("when index range is empty, does nothing")
			void doesNothingWhenIndexRangeEmpty() {
				final var items = new MutableList<>("g", "w", "Q", "c", "j", "P");
				final var returned = items.remove(
					new IndexRange());

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
						"g", "w", "Q", "c", "j", "P"
					});
			}

			@Test
			@DisplayName("when index range starts after valid range, fails")
			void failsWhenIndexRangeStartsAfterValidRange() {
				final var range = new IndexRange(9, 16);
				final var expected = new IndexRangeNotInRangeException(range,
					new IndexRange(0, 6));

				assertThrows(() -> {
					new MutableList<>("g", "w", "Q", "c", "j", "P")
						.remove(range);
				}, expected);
			}

			@Test
			@DisplayName("when index range ends after valid range, fails")
			void failsWhenIndexRangeEndsAfterValidRange() {
				final var range = new IndexRange(1, 7);
				final var expected = new IndexRangeNotInRangeException(range,
					new IndexRange(0, 6));

				assertThrows(() -> {
					new MutableList<>("g", "w", "Q", "c", "j", "P")
						.remove(range);
				}, expected);
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			@Test
			@DisplayName("when index range is empty, does nothing")
			void failsWhenIndexRangeCoincidesWithValidRange() {
				final var items = new MutableList<String>();
				final var returned = items.remove(
					new IndexRange());

				Assertions.assertIs(returned, items);
				assertEquals(items,
					new String[]{
					});
			}

			@Test
			@DisplayName("when index range starts after after valid range, fails")
			void failsWhenIndexRangeStartsAfterZero() {
				final var range = new IndexRange(2, 6);
				final var expected = new IndexRangeNotInRangeException(range,
					new IndexRange());

				assertThrows(() -> {
					new MutableList<>()
						.remove(range);
				}, expected);
			}

			@Test
			@DisplayName("when index range ends after valid range, fails")
			void failsWhenIndexRangeNotEmpty() {
				final var range = new IndexRange(0, 2);
				final var expected = new IndexRangeNotInRangeException(range,
					new IndexRange());

				assertThrows(() -> {
					new MutableList<>()
						.remove(range);
				}, expected);
			}
		}
	}

	@Nested
	@DisplayName(".clear()")
	class ClearTests {
		@Test
		@DisplayName("when list is not empty, clears list")
		void clearsWhenListNotEmpty() {
			final var items = new MutableList<>("b", "D", "e", "P");
			final var returned = items.clear();

			Assertions.assertIs(returned, items);
			assertEquals(items,
				new String[]{
				});
		}

		@Test
		@DisplayName("when list is empty, does nothing")
		void doesNothingWhenListEmpty() {
			final var items = new MutableList<>();
			final var returned = items.clear();

			Assertions.assertIs(returned, items);
			assertEquals(items,
				new String[]{
				});
		}
	}

	@Nested
	@DisplayName(".toImmutable()")
	class ToImmutableTests {
		@Test
		@DisplayName("returns immutable list")
		void returnsImmutableList() {
			final var items = new MutableList<>("v", "m", "K", "l")
				.toImmutable();

			final var klass = items.getClass();
			Assertions.assertEquals(klass, List.class);

			assertEquals(items,
				new String[]{
					"v", "m", "K", "l"
				});
		}

		@Test
		@DisplayName("when list is empty, returns empty immutable list")
		void returnsEmptyImmutableListWhenListEmpty() {
			final var items = new MutableList<>()
				.toImmutable();

			final var klass = items.getClass();
			Assertions.assertEquals(klass, List.class);

			assertEquals(items,
				new String[]{
				});
		}
	}

	private static void assertCapacity(MutableList<?> actual, int expected) {
		Assertions.assertEquals(actual.store.items.length, expected,
			"List capacity differs from expectation.");
	}
}

// created on Jul 25, 2019
