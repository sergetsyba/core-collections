package co.tsyba.core.collections;

import org.junit.Test;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jul 10, 2019.
 */
public class ListTests {
	@Test
	public void checksEmpty() {
		final var items = new List<>("t", "g", "e", "b", "T");
		assert items.isEmpty() == false;
	}

	@Test
	public void returnsItemCount() {
		final var items = new List<>("t", "F", "g", "e", "b");
		assert items.getCount() == 5;
	}

	@Test
	public void returnsFirstItem() {
		final var items = new List<>("O", "T", "q", "M", "s");

		assert items.getFirst()
				.get()
				.equals("O");
	}

	@Test
	public void returnsLastItem() {
		final var items = new List<>("O", "T", "q", "M", "s");

		assert items.getLast()
				.get()
				.equals("s");
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
