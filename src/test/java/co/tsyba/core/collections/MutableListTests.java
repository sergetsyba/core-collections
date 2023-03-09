package co.tsyba.core.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static co.tsyba.core.collections.MutableList.minimumCapacity;

class MutableListTests {
	@Nested
	@DisplayName("MutableList(int)")
	class ConstructorIntTests {
		@Test
		@DisplayName("when capacity is positive, creates empty list")
		void createsEmptyListWhenCapacityPositive() {
			final var items = new MutableList<>(7);

			assert items.storeEquals(7,
				new String[]{});
		}

		@Test
		@DisplayName("when capacity is 0, creates empty list")
		void createsEmptyListWhenCapacityZero() {
			final var items = new MutableList<>(0);

			assert items.storeEquals(0,
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
	@DisplayName("MutableList(Collection<T>)")
	class ConstructorCollectionTests {
		@Test
		@DisplayName("creates list")
		void createsList() {
			final var items = new MutableList<>(
				new List<>("p", "g", "F", "q", "F"));

			assert items.storeEquals(minimumCapacity,
				new String[]{
					"p", "g", "F", "q", "F"
				});
		}

		@Test
		@DisplayName("when argument collection is empty, creates empty list")
		void createsEmptyListWhenArgCollectionEmpty() {
			final var items = new MutableList<>(
				new List<String>());

			assert items.storeEquals(minimumCapacity,
				new String[]{});
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

			assert items.storeEquals(minimumCapacity,
				new String[]{
					"b", "d", "Q", "P", "G"
				});
		}

		@Test
		@DisplayName("when some items are null, creates list ignoring nulls")
		void createsListWhenSomeItemsNull() {
			final var items = new MutableList<>(
				null, "B", null, null, "G", null);

			assert items.storeEquals(minimumCapacity,
				new String[]{
					"B", "G"
				});
		}

		@Test
		@DisplayName("when all items are null, creates empty list")
		void createsEmptyListWhenAllItemsNull() {
			final var items = new MutableList<>(
				null, null, null);

			assert items.storeEquals(minimumCapacity,
				new String[]{});
		}

		@Test
		@DisplayName("when argument array is empty, creates empty list")
		void createsEmptyListWhenArgArrayEmpty() {
			final var items = new MutableList<String>();

			assert items.storeEquals(minimumCapacity,
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
				assert items.storeEquals(
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
				assert items.storeEquals(
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
					assert items.storeEquals(
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
					assert items.storeEquals(
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
					assert items.storeEquals(
						new String[]{
							"U"
						});
				}

			}
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
