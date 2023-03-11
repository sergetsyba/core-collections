package co.tsyba.core.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
			try {
				new MutableList<>(-8);
			} catch (IllegalArgumentException ignored) {
				return;
			}
			assert false;
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
		@DisplayName("when some items are null, creates list ignoring nulls")
		void createsListIgnoringNullsWhenSomeItemsNull() {
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
			@DisplayName("when item is null, does not set item")
			void doesNotSetItemWhenItemNull() {
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
				try {
					new MutableList<>("g", "R", "c")
						.set(-2, "V");
				} catch (IndexNotInRangeException ignored) {
					return;
				}
				assert false;
			}

			@Test
			@DisplayName("when index is after valid range, fails")
			void failsWhenIndexAfterValidRange() {
				try {
					new MutableList<>("g", "R", "c")
						.set(4, "V");
				} catch (IndexNotInRangeException ignored) {
					return;
				}
				assert false;
			}
		}

		@Nested
		@DisplayName("when list is empty")
		class EmptyListTests {
			@Test
			@DisplayName("when index is before valid range, fails")
			void failsWhenIndexBeforeValidRange() {
				try {
					new MutableList<>()
						.set(-1, "x");
				} catch (IndexNotInRangeException ignored) {
					return;
				}
				assert false;
			}

			@Test
			@DisplayName("when index is after valid range, fails")
			void failsWhenIndexAfterValidRange() {
				try {
					new MutableList<>()
						.set(0, "p");
				} catch (IndexNotInRangeException ignored) {
					return;
				}
				assert false;
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
			@DisplayName("when item is null, does not prepend null")
			void doesNotPrependNullWhenItemNull() {
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
			@DisplayName("when item is null, does not prepend null")
			void doesNotPrependNullWhenItemNull() {
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
			@DisplayName("when some items are null, does not prepend nulls")
			void doesNotPrependNullsWhenSomeItemsNull() {
				final var items = new MutableList<>("g", "H");
				final var returned = items.prepend(null, "n", null, null, "O", null);

				assert returned == items;
				assertEquals(items,
					new String[]{
						"n", "O", "g", "H"
					});
			}

			@Test
			@DisplayName("when all items are null, does not prepend nulls")
			void doesNotPrependNullsWhenAllItemsNull() {
				final var items = new MutableList<>("f", "E", "q");
				final var returned = items.prepend(null, null, null);

				assert returned == items;
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
			@DisplayName("when argument array is empty, does nothing")
			void doesNothingWhenArgArrayEmpty() {
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
			@DisplayName("when argument list is empty, does nothing")
			void doesNothingWhenArgListEmpty() {
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
			@DisplayName("when argument list is empty, does nothing")
			void doesNothingWhenArgListEmpty() {
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
			@DisplayName("when item is null, does not append null")
			void doesNotAppendNullWhenItemNull() {
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
			@DisplayName("when item is null, does not append null")
			void doesNotAppendNullWhenItemNull() {
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
			@DisplayName("when some items are null, does not append nulls")
			void doesNotAppendNullsWhenSomeItemsNull() {
				final var items = new MutableList<>("g", "H");
				final var returned = items.append(null, "n", null, null, "O", null);

				assert returned == items;
				assertEquals(items,
					new String[]{
						"g", "H", "n", "O"
					});
			}

			@Test
			@DisplayName("when all items are null, does not append nulls")
			void doesNotAppendNullsWhenAllItemsNull() {
				final var items = new MutableList<>("f", "E", "q");
				final var returned = items.append(null, null, null);

				assert returned == items;
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
			@DisplayName("when argument array is empty, does nothing")
			void doesNothingWhenArgArrayEmpty() {
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
			@DisplayName("when argument list is empty, does nothing")
			void doesNothingWhenArgListEmpty() {
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
			@DisplayName("when argument list is empty, does nothing")
			void doesNothingWhenArgListEmpty() {
				final var items = new MutableList<String>();
				final var returned = items.append(
					new List<>());

				assert returned == items;
				assertEquals(items,
					new String[]{});
			}
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

	static <T> void assertEquals(List<T> actual, T[] expected) {
		var index = 0;
		for (; index < expected.length; ++index) {
			assert expected[index].equals(actual.store.items[index]);
		}
		for (; index < actual.store.items.length; ++index) {
			assert actual.store.items[index] == null;
		}
	}
}

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jul 25, 2019.
 */
class MutableListLegacyTests {
	@Test
	public void removesFirstItem() {
		final var items = new MutableList<>("g", "E", "x", "P", "d");

		assert items.removeFirst()
			.get()
			.equals("g");

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
