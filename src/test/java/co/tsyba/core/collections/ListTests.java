package co.tsyba.core.collections;

import org.junit.Test;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jul 10, 2019.
 */
public class ListTests {
	@Test
	public void verifiesNotEmpty() {
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
		final var firstItem = items.getFirst()
				.get();

		assert firstItem.equals("O");
	}

	@Test
	public void returnsLastItem() {
		final var items = new List<>("O", "T", "q", "M", "s");
		final var lastItem = items.getLast()
				.get();

		assert lastItem.equals("s");
	}

	@Test
	public void returnsDistinctItems() {
		// has reapeated items
		final var items1 = new List<>("r", "D", "S", "r", "S");
		final var distinctItems1 = items1.getDistinct();

		assert new List<>("r", "D", "S")
				.equals(distinctItems1);

		// has no reapeated items
		final var items2 = new List<>("r", "D", "x", "o", "M");
		final var distinctItems2 = items2.getDistinct();

		assert distinctItems2.equals(items2);
	}

	@Test
	public void filtersItems() {
		// keeps items in uppercase
		final var items = new List<>("r", "x", "O", "P", "z")
				.filter(item -> item.toUpperCase().equals(item));

		assert new List<>("O", "P")
				.equals(items);
	}

	@Test
	public void convertsItems() {
		// converts to uppercase
		final var items1 = new List<>("c", "G", "d", "Y", "l")
				.convert(String::toUpperCase);

		assert new List<>("C", "G", "D", "Y", "L")
				.equals(items1);

		// converts to uppercase, only items in lowecase
		final var items2 = new List<>("c", "G", "d", "Y", "l")
				.convert(item -> item.toUpperCase().equals(item)
				? null
				: item.toUpperCase());

		assert new List<>("C", "D", "L")
				.equals(items2);
	}
}
