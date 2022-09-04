package co.tsyba.core.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jul 10, 2019.
 */
public class ListTests {
	@Nested
	@DisplayName(".isEmpty")
	class IsEmptyTests {
		@Test
		@DisplayName("returns true when empty")
		void returnsTrueWhenEmpty() {
			final var items = new List<>();
			assert items.isEmpty();
		}

		@Test
		@DisplayName("returns false when not empty")
		void returnsFalseWhenNotEmpty() {
			final var items = new List<>("h", "4", "G");
			assert !items.isEmpty();
		}
	}

	@Nested
	@DisplayName(".getCount")
	class GetCountTests {
		@Test
		@DisplayName("returns item count")
		void returnsItemCount() {
			final var items = new List<>("g", "T", "e", "e");
			assert items.getCount() == 4;
		}

		@Test
		@DisplayName("return 0 when empty")
		void returnsZeroWhenEmpty() {
			final var items = new List<>();
			assert items.getCount() == 0;
		}
	}

	@Nested
	@DisplayName(".getFirst")
	class GetFirstTests {
		@Test
		@DisplayName("returns first item")
		void returnsFirstItem() {
			final var items = new List<>("O", "T", "q", "M", "s");
			final var first = items.getFirst();

			assert first.isPresent();
			assert first.get()
					.equals("O");
		}

		@Test
		@DisplayName("returns nothing when empty")
		void returnsEmptyWhenEmpty() {
			final var items = new List<>();
			final var first = items.getFirst();

			assert first.isEmpty();
		}
	}

	@Nested
	@DisplayName(".getLast")
	class GetLastTests {
		@Test
		@DisplayName("returns last item")
		void returnsLastItem() {
			final var items = new List<>("O", "T", "q", "M", "s");
			final var last = items.getLast();

			assert last.isPresent();
			assert last.get()
					.equals("s");
		}

		@Test
		@DisplayName("returns nothing when empty")
		void returnsEmptyWhenEmpty() {
			final var items = new List<>();
			final var first = items.getFirst();

			assert first.isEmpty();
		}
	}

}

class LegacyListTests {
	@Test
	@DisplayName("returns last item")
	public void returnsLastItem() {

	}

	@Test
	@DisplayName("returns item at index")
	public void returnsItemAtIndex() {

	}

	@Test
	public void guardsIndex() {
		final var items = new List<>("e", "D", "d", "E", "X");

		// fisrt index
		assert items.guard(0)
				.get()
				.equals("e");

		// item in the middle
		assert items.guard(2)
				.get()
				.equals("d");

		// item at the end
		assert items.guard(4)
				.get()
				.equals("X");

		// index before valid range
		assert items.guard(-1)
				.isEmpty();

		// index after valid range
		assert items.guard(5)
				.isEmpty();
	}

	@Test
	public void returnsDistinctItems() {
		// has reapeated items
		final var items1 = new List<>("r", "D", "S", "r", "S");
		assert items1.getDistinct()
				.equals(new List<>("r", "D", "S"));

		// has no reapeated items
		final var items2 = new List<>("r", "D", "x", "o", "M");
		assert items2.getDistinct()
				.equals(items2);
	}

	@Test
	public void filtersItems() {
		final var items = new List<>("r", "x", "O", "P", "z");

		// keeps items, which are in upper case
		assert items.filter(this::isUpperCase)
				.equals(new List<>("O", "P"));
	}

	@Test
	public void convertsItems() {
		// converts to upper case
		// filters out no items
		final var items1 = new List<>("c", "G", "d", "Y", "l");
		assert items1.convert(String::toUpperCase)
				.equals(new List<>("C", "G", "D", "Y", "L"));

		// converts items to upper case, only if it is in lower case
		// filters out some items
		final var items2 = new List<>("c", "G", "d", "Y", "l");
		assert items2.convert(item -> isUpperCase(item) ? null : item.toUpperCase())
				.equals(new List<>("C", "D", "L"));

		// filters out all items
		final var items3 = new List<>("c", "G", "d", "Y", "l");
		assert items3.convert(item -> null)
				.equals(new List<>());
	}

	private boolean isUpperCase(String string) {
		return string.toUpperCase()
				.equals(string);
	}
}
