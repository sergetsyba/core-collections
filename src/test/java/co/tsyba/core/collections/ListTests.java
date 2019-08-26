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
	public void guardsIndex() {
		final var items = new List<>("e", "D", "d", "E", "X");

		// fisrt index
		final var item1 = items.guard(0);
		assert item1.get()
				.equals("e");

		// item in the middle
		final var item2 = items.guard(2);
		assert item2.get()
				.equals("d");

		// item at the end
		final var item3 = items.guard(4);
		assert item3.get()
				.equals("X");

		// index before valid range
		final var item4 = items.guard(-1);
		assert item4.isEmpty();

		// index after valid range
		final var item5 = items.guard(5);
		assert item5.isEmpty();
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
		// keeps items, which are in upper case
		final var items = new List<>("r", "x", "O", "P", "z")
				.filter(item -> item.toUpperCase().equals(item));

		assert new List<>("O", "P")
				.equals(items);
	}

	@Test
	public void convertsItems() {
		// converts to upper case
		final var items1 = new List<>("c", "G", "d", "Y", "l")
				.convert(String::toUpperCase);

		assert new List<>("C", "G", "D", "Y", "L")
				.equals(items1);

		// converts items to upper case, which are in lower case
		final var items2 = new List<>("c", "G", "d", "Y", "l")
				.convert(item -> item.toUpperCase().equals(item)
				? null
				: item.toUpperCase());

		assert new List<>("C", "D", "L")
				.equals(items2);
	}
}
