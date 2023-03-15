package co.tsyba.core.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class MutableListTests {
	@Nested
	@DisplayName("MutableList(int)")
	class ConstructorIntTests {
		@Test
		@DisplayName("when capacity is positive, creates empty list")
		void createsEmptyListWhenCapacityPositive() {
			final var items = new MutableList<String>(7);

			assertCapacity(items, 7);
			assertEquals(items,
				new String[]{});
		}

		@Test
		@DisplayName("when capacity is 0, creates empty list")
		void createsEmptyListWhenCapacityZero() {
			final var items = new MutableList<String>(0);

			assertCapacity(items, 0);
			assertEquals(items,
				new String[]{});
		}

		@Test
		@DisplayName("when capacity is negative, fails")
		void failsWhenCapacityNegative() {
			assertThrows(() -> {
				new MutableList<String>(-8);
			}, IllegalArgumentException.class);
		}
	}

	@Nested
	@DisplayName("MutableList(T...)")
	class ConstructorVarargsTests {
		@Test
		@DisplayName("creates list")
		void createsList() {
			final var items = new MutableList<>(
				"b", "d", "Q", "P", "G");

			assertEquals(items,
				new String[]{
					"b", "d", "Q", "P", "G"
				});
		}

		@Test
		@DisplayName("when some items are null, creates list without nulls")
		void createsListWithoutNullsWhenSomeItemsNull() {
			final var items = new MutableList<>(
				null, "B", null, null, "G", null);

			assertEquals(items,
				new String[]{
					"B", "G"
				});
		}

		@Test
		@DisplayName("when all items are null, creates empty list")
		void createsEmptyListWhenAllItemsNull() {
			final var items = new MutableList<String>(
				null, null, null);

			assertEquals(items,
				new String[]{});
		}

		@Test
		@DisplayName("when argument array is empty, creates empty list")
		void createsEmptyListWhenArgArrayEmpty() {
			final var items = new MutableList<String>();

			assertEquals(items,
				new String[]{});
		}
	}


	@Nested
	@DisplayName("MutableList(Collection<T>)")
	class ConstructorCollectionTests {
		@Test
		@DisplayName("creates list")
		void createsList() {
			final var items = new MutableList<>(
				new List<>("p", "g", "F", "q", "F"));

			assertEquals(items,
				new String[]{
					"p", "g", "F", "q", "F"
				});
		}

		@Test
		@DisplayName("when argument collection is empty, creates empty list")
		void createsEmptyListWhenArgCollectionEmpty() {
			final var items = new MutableList<>(
				new List<String>());

			assertEquals(items,
				new String[]{});
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

				assert returned == items;
				assertEquals(items,
					new String[]{
						"g", "E", "O", "s"
					});
			}

			@Test
			@DisplayName("when item is null, does not append item")
			void doesNotAppendItemWhenItemNull() {
				final var items = new MutableList<>("v", "E", "q", "A");
				final var returned = items.set(1, "M");

				assert returned == items;
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
					new IndexRange(0, 0));

				assertThrows(() -> {
					new MutableList<>()
						.set(4, "V");
				}, expected);
			}

			@Test
			@DisplayName("when index is after valid range, fails")
			void failsWhenIndexAfterValidRange() {
				final var expected = new IndexNotInRangeException(0,
					new IndexRange(0, 0));

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

				assert returned == items;
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

				assert returned == items;
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

				assert returned == items;
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

				assert returned == items;
				assertEquals(items,
					new String[]{});
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

				assert returned == items;
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

				assert returned == items;
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

				assert returned == items;
				assertEquals(items,
					new String[]{
						"f", "E", "q"
					});
			}

			@Test
			@DisplayName("when argument array is empty, does not prepend items")
			void doesNotPrependItemsWhenArgArrayEmpty() {
				final var items = new MutableList<>("b", "R", "O");
				final var returned = items.prepend();

				assert returned == items;
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

				assert returned == items;
				assertEquals(items,
					new String[]{
						"V", "B", "M"
					});
			}

			@Test
			@DisplayName("when argument array is empty, does not prepend items")
			void doesNotPrependItemsWhenArgArrayEmpty() {
				final var items = new MutableList<String>();
				final var returned = items.prepend();

				assert returned == items;
				assertEquals(items,
					new String[]{});
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

				assert returned == items;
				assertEquals(items,
					new String[]{
						"F", "F", "a", "G", "f"
					});
			}

			@Test
			@DisplayName("when argument list is empty, does not prepend items")
			void doesNotPrependItemsWhenArgListEmpty() {
				final var items = new MutableList<>("G", "f");
				final var returned = items.prepend(
					new List<>());

				assert returned == items;
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

				assert returned == items;
				assertEquals(items,
					new String[]{
						"V", "Q", "P"
					});
			}

			@Test
			@DisplayName("when argument list is empty, does not prepend items")
			void doesNotPrependItemsWhenArgListEmpty() {
				final var items = new MutableList<String>();
				final var returned = items.prepend(
					new List<>());

				assert returned == items;
				assertEquals(items,
					new String[]{});
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

				assert returned == items;
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

				assert returned == items;
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

				assert returned == items;
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

				assert returned == items;
				assertEquals(items,
					new String[]{});
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

				assert returned == items;
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

				assert returned == items;
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

				assert returned == items;
				assertEquals(items,
					new String[]{
						"f", "E", "q"
					});
			}

			@Test
			@DisplayName("when argument array is empty, does not append items")
			void doesNotAppendItemsWhenArgArrayEmpty() {
				final var items = new MutableList<>("b", "R", "O");
				final var returned = items.append();

				assert returned == items;
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

				assert returned == items;
				assertEquals(items,
					new String[]{
						"V", "B", "M"
					});
			}

			@Test
			@DisplayName("when argument array is empty, does not append items")
			void doesNotAppendItemsWhenArgArrayEmpty() {
				final var items = new MutableList<String>();
				final var returned = items.append();

				assert returned == items;
				assertEquals(items,
					new String[]{});
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

				assert returned == items;
				assertEquals(items,
					new String[]{
						"G", "f", "F", "F", "a"
					});
			}

			@Test
			@DisplayName("when argument list is empty, does not append items")
			void doesNotAppendItemsWhenArgListEmpty() {
				final var items = new MutableList<>("G", "f");
				final var returned = items.append(
					new List<>());

				assert returned == items;
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

				assert returned == items;
				assertEquals(items,
					new String[]{
						"V", "Q", "P"
					});
			}

			@Test
			@DisplayName("when argument list is empty, does not append items")
			void doesNotAppendItemsWhenArgListEmpty() {
				final var items = new MutableList<String>();
				final var returned = items.append(
					new List<>());

				assert returned == items;
				assertEquals(items,
					new String[]{});
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

				assert returned == items;
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

				assert returned == items;
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
						.set(-4, "r");
				}, expected);
			}

			@Test
			@DisplayName("when index is after valid range, fails")
			void failsWhenIndexAfterValidRange() {
				final var expected = new IndexNotInRangeException(9,
					new IndexRange(0, 2));

				assertThrows(() -> {
					new MutableList<>("g", "A")
						.set(9, "r");
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
					new IndexRange(0, 0));

				assertThrows(() -> {
					new MutableList<>()
						.set(-3, "r");
				}, expected);
			}

			@Test
			@DisplayName("when index is after valid range, fails")
			void failsWhenIndexAfterValidRange() {
				final var expected = new IndexNotInRangeException(0,
					new IndexRange(0, 0));

				assertThrows(() -> {
					new MutableList<>()
						.set(0, "s");
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

				assert returned == items;
				assertEquals(items, new String[]{
					"v", "F", "F", "F", "G", "h"
				});
			}

			@Test
			@DisplayName("when some items are null, inserts items without nulls")
			void insertsItemsWithoutNullsWhenSomeItemsNull() {
				final var items = new MutableList<>("b", "a", "s");
				final var returned = items.insert(0, "F", null, null, "D");

				assert returned == items;
				assertEquals(items, new String[]{
					"F", "D", "b", "a", "s"
				});
			}

			@Test
			@DisplayName("when all items are null, does not insert items")
			void doesNotInsertItemsWhenAllItemsNull() {
				final var items = new MutableList<>("b", "a", "s");
				final var returned = items.insert(0, null, null);

				assert returned == items;
				assertEquals(items, new String[]{
					"b", "a", "s"
				});
			}

			@Test
			@DisplayName("when argument array is empty, does not insert items")
			void doesNotInsertItemsWhenArgArrayEmpty() {
				final var items = new MutableList<>("b", "a", "s");
				final var returned = items.insert(0);

				assert returned == items;
				assertEquals(items, new String[]{
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
					new IndexRange(0, 0));

				assertThrows(() -> {
					new MutableList<>()
						.insert(-1, "f", "d", "Q");
				}, expected);
			}

			@Test
			@DisplayName("when index is after valid range, fails")
			void failsWhenIndexAfterValidRange() {
				final var expected = new IndexNotInRangeException(0,
					new IndexRange(0, 0));

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

				assert returned == items;
				assertEquals(items, new String[]{
					"v", "F", "F", "F", "G", "h"
				});
			}

			@Test
			@DisplayName("when argument list is empty, does not insert items")
			void doesNotInsertItemsWhenArgListEmpty() {
				final var items = new MutableList<>("b", "a", "s");
				final var returned = items.insert(0,
					new List<>());

				assert returned == items;
				assertEquals(items, new String[]{
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
					new IndexRange(0, 0));

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
					new IndexRange(0, 0));

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

				assert returned == items;
				assertEquals(items, new String[]{
					"f", "e", "a", "T", "f", "s", "w"
				});
			}

			@Test
			@DisplayName("when some argument items are null, replaces items without nulls")
			void replacesItemsWithoutNullsWhenSomeArgItemsNull() {
				final var items = new MutableList<>("f", "e", "q", "R", "w");
				final var range = new IndexRange(2, 5);
				final var returned = items.replace(range, null, "b", null, null, "n");

				assert returned == items;
				assertEquals(items, new String[]{
					"f", "e", "b", "n"
				});
			}

			@Test
			@DisplayName("when all argument items are null, deletes items")
			void deletesItemsWhenAllArgItemsNull() {
				final var items = new MutableList<>("f", "e", "q", "R", "w");
				final var range = new IndexRange(1, 3);
				final var returned = items.replace(range, null, null);

				assert returned == items;
				assertEquals(items, new String[]{
					"f", "R", "w"
				});
			}

			@Test
			@DisplayName("when argument array is empty, deletes items")
			void deletesItemsWhenAllArgArrayEmpty() {
				final var items = new MutableList<>("f", "e", "q", "R", "w");
				final var range = new IndexRange(2, 3);
				final var returned = items.replace(range);

				assert returned == items;
				assertEquals(items, new String[]{
					"f", "e", "R", "w"
				});
			}

			@Test
			@DisplayName("when index range is empty, inserts items")
			void insertsItemsWhenIndexRangeEmpty() {
				final var items = new MutableList<>("v", "a", "f", "j");
				final var range = new IndexRange(2, 2);
				final var returned = items.replace(range, "v", "m");

				assert returned == items;
				assertEquals(items, new String[]{
					"v", "a", "v", "m", "f", "j"
				});
			}

			@Test
			@DisplayName("when index range is out of valid range, fails")
			void failsWhenIndexRangeOutOfValidRange() {
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
			@DisplayName("fails")
			void fails() {
				final var range = new IndexRange(0, 0);
				final var expected = new IndexRangeNotInRangeException(range,
					new IndexRange(0, 0));

				assertThrows(() -> {
					new MutableList<String>()
						.replace(range, "G", "v", "Q");
				}, expected);
			}
		}
	}

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

				assert returned == items;
				assertEquals(items, new String[]{
					"f", "e", "a", "T", "f", "s", "w"
				});
			}

			@Test
			@DisplayName("when argument list is empty, deletes items")
			void deletesItemsWhenAllArgListEmpty() {
				final var items = new MutableList<>("f", "e", "q", "R", "w");
				final var range = new IndexRange(2, 4);
				final var returned = items.replace(range,
					new List<>());

				assert returned == items;
				assertEquals(items, new String[]{
					"f", "e", "w"
				});
			}

			@Test
			@DisplayName("when index range is empty, inserts items")
			void insertsItemsWhenIndexRangeEmpty() {
				final var items = new MutableList<>("v", "a", "f", "j");
				final var range = new IndexRange(2, 2);
				final var returned = items.replace(range,
					new List<>("v", "m"));

				assert returned == items;
				assertEquals(items, new String[]{
					"v", "a", "v", "m", "f", "j"
				});
			}

			@Test
			@DisplayName("when index range is out of valid range, fails")
			void failsWhenIndexRangeOutOfValidRange() {
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
			@DisplayName("fails")
			void fails() {
				final var range = new IndexRange(0, 0);
				final var expected = new IndexRangeNotInRangeException(range,
					new IndexRange(0, 0));

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

			assert returned == items;
			assertEquals(items, new String[]{
				"e", "v", "Q"
			});
		}

		@Test
		@DisplayName("when list is empty, does nothing")
		void doesNothingWhenListEmpty() {
			final var items = new MutableList<String>();
			final var returned = items.removeFirst();

			assert returned == items;
			assertEquals(items, new String[]{
			});
		}
	}

	static <T> void assertCapacity(List<T> actual, int expected) {
		assert expected == actual.store.items.length :
			String.format("Incorrect list capacity." +
					"\n\texpected: %d" +
					"\n\tactual: %d",
				expected,
				actual.store.items.length);
	}

	static void assertThrows(ThrowingRunnable runnable, Throwable expected) {
		try {
			runnable.run();
		} catch (Throwable actual) {
			assert expected.equals(actual) :
				String.format("Incorrect throwable." +
						"\n\texpected: %s" +
						"\n\tactual: %s",
					expected, actual);

			return;
		}

		assert false : String.format("Finished without throwing." +
				"\n\texpected: %s",
			expected);
	}

	static void assertThrows(ThrowingRunnable runnable, Class<? extends Throwable> expected) {
		try {
			runnable.run();
		} catch (Throwable throwable) {
			final var actual = throwable.getClass();
			assert expected.equals(actual) :
				String.format("Incorrect throwable." +
						"\n\texpected: %s" +
						"\n\tactual: %s",
					expected, actual);

			return;
		}

		assert false : String.format("Finished without throwing." +
				"\n\texpected: %s",
			expected.getName());
	}

	static <T> void assertEquals(List<T> actual, T[] expected) {
		var index = 0;
		for (; index < expected.length; ++index) {
			assert expected[index].equals(actual.store.items[index]) :
				String.format("Lists are not equal." +
						"\n\texpected: %s" +
						"\n\tactual: %s",
					Arrays.toString(expected), actual);
		}
		for (; index < actual.store.items.length; ++index) {
			assert actual.store.items[index] == null :
				String.format("Lists are not equal." +
						"\n\texpected: %s" +
						"\n\tactual: %s",
					Arrays.toString(expected), actual);
			;
		}
	}

	interface ThrowingRunnable {
		void run() throws Exception;
	}
}

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jul 25, 2019.
 */
class MutableListLegacyTests {
	@Test
	public void removesFirstItem() {
		final var items = new MutableList<>("g", "E", "x", "P", "d");

//		assert items.removeFirst()
//			.get()
//			.equals("g");

		assert items.equals(
			new List<>("E", "x", "P", "d"));
	}

	@Test
	public void removesLastItem() {
		final var items = new MutableList<>("g", "E", "x", "P", "d");

		assert items.removeLast()
			.get()
			.equals("d");

		assert items.equals(
			new List<>("g", "E", "x", "P"));
	}

	@Test
	public void clearsItems() {
		final var items = new MutableList<>("a", "R", "o", "c");

		assert items.clear() == items;
		assert items.equals(
			new List<>());
	}

	@Test
	public void guardsIndex() {
		// note: this tests guarding also works with mutation
		final var items = new MutableList<>("W", "W", "x", "x", "X");

		// fisrt index
		final var items1 = items.guard(0, (item, index)
			-> items.set(index, item.toLowerCase()));

		assert items1 == items;
		assert items.equals(
			new List<>("w", "W", "x", "x", "X"));

		// item in the middle
		final var items2 = items.guard(1, (item, index)
			-> items.set(index, item.toLowerCase()));

		assert items2 == items;
		assert items.equals(
			new List<>("w", "w", "x", "x", "X"));

		// item at the end
		final var items3 = items.guard(4, (item, index)
			-> items.set(index, item.toLowerCase()));

		assert items3 == items;
		assert items.equals(
			new List<>("w", "w", "x", "x", "x"));

		// index before valid range
		final var items4 = items.guard(-1, (item, index)
			-> items.set(index, item.toLowerCase()));

		assert items4 == items;
		assert items.equals(
			new List<>("w", "w", "x", "x", "x"));

		// index after valid range
		final var items5 = items.guard(5, (item, index)
			-> items.set(index, item.toLowerCase()));

		assert items5 == items;
		assert items.equals(
			new List<>("w", "w", "x", "x", "x"));
	}

	@Test
	public void returnsDistinctItems() {
		// has reapeated items
		final var items1 = new List<>("f", "S", "S", "f", "f");
		assert items1.getDistinct()
			.equals(new List<>("f", "S"));

		// has no reapeated items
		final var items2 = new MutableList<>("e", "E", "q", "c", "P");
		assert items2.getDistinct()
			.equals(items2);
	}

	@Test
	public void filtersItems() {
		final var items = new MutableList<>("r", "x", "O", "P", "z");

		// keeps items in uppercase
		assert items.filter(this::isUpperCase)
			.equals(new List<>("O", "P"));
	}

	@Test
	public void convertsItems() {
		// converts to upper case
		final var items1 = new List<>("c", "G", "d", "Y", "l");
		assert items1.convert(String::toUpperCase)
			.equals(new List<>("C", "G", "D", "Y", "L"));

		// converts items to upper case, only if it is in lower case
		// filters out some items
		final var items2 = new List<>("c", "G", "d", "Y", "l");
		assert items2.convert(item -> isUpperCase(item) ? null : item.toUpperCase())
			.equals(new List<>("C", "D", "L"));

		// filters out all values
		final var items3 = new List<>("c", "G", "d", "Y", "l");
		assert items3.convert(item -> null)
			.equals(new List<>());
	}

	private boolean isUpperCase(String string) {
		return string.toUpperCase()
			.equals(string);
	}
}
