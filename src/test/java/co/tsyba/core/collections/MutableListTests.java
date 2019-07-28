package co.tsyba.core.collections;

import org.junit.Test;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jul 25, 2019.
 */
public class MutableListTests {
	@Test
	public void removesFirstItem() {
		final var items = new MutableList<>("g", "E", "x", "P", "d");
		final var firstItem = items.removeFirst()
				.get();

		assert new List<>("E", "x", "P", "d")
				.equals(items);

		assert firstItem.equals("g");
	}

	@Test
	public void removesLastItem() {
		final var items = new MutableList<>("g", "E", "x", "P", "d");
		final var firstItem = items.removeLast()
				.get();

		assert new List<>("g", "E", "x", "P")
				.equals(items);

		assert firstItem.equals("d");
	}

	@Test
	public void guardsIndex() {
		final var items = new MutableList<>("W", "W", "x", "x", "X");

		// fisrt index
		final var items1 = items.guard(0, (item, index)
				-> items.set(index, item.toLowerCase()));

		assert items1 == items;
		assert new MutableList<>("w", "W", "x", "x", "X")
				.equals(items);

		// item in the middle
		final var items2 = items.guard(1, (item, index)
				-> items.set(index, item.toLowerCase()));

		assert items2 == items;
		assert new MutableList<>("w", "w", "x", "x", "X")
				.equals(items);

		// item at the end
		final var items3 = items.guard(4, (item, index)
				-> items.set(index, item.toLowerCase()));

		assert items3 == items;
		assert new MutableList<>("w", "w", "x", "x", "x")
				.equals(items);

		// index before valid range
		final var items4 = items.guard(-1, (item, index)
				-> items.set(index, item.toLowerCase()));

		assert items4 == items;
		assert new MutableList<>("w", "w", "x", "x", "x")
				.equals(items);

		// index after valid range
		final var items5 = items.guard(5, (item, index)
				-> items.set(index, item.toLowerCase()));

		assert items5 == items;
		assert new MutableList<>("w", "w", "x", "x", "x")
				.equals(items);
	}

	@Test
	public void returnsDistinctItems() {
		// has reapeated items
		final var items1 = new List<>("f", "S", "S", "f", "f");
		final var distinctItems1 = items1.getDistinct();

		assert new List<>("f", "S")
				.equals(distinctItems1);

		// has no reapeated items
		final var items2 = new MutableList<>("e", "E", "q", "c", "P");
		final var distinctItems2 = items2.getDistinct();

		assert distinctItems2.equals(items2);
	}

	@Test
	public void filtersItems() {
		// keeps items in uppercase
		final var items = new MutableList<>("r", "x", "O", "P", "z")
				.filter(item -> item.toUpperCase().equals(item));

		assert new List<>("O", "P")
				.equals(items);
	}

	@Test
	public void convertsItems() {
		// converts to uppercase
		final var items1 = new MutableList<>("c", "G", "d", "Y", "l")
				.convert(String::toUpperCase);

		assert new List<>("C", "G", "D", "Y", "L")
				.equals(items1);

		// converts to uppercase, only items in lowecase
		final var items2 = new MutableList<>("c", "G", "d", "Y", "l")
				.convert(item -> item.toUpperCase().equals(item)
				? null
				: item.toUpperCase());

		assert new List<>("C", "D", "L")
				.equals(items2);
	}
}
