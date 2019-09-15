package co.tsyba.core.collections;

import org.junit.Test;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jul 25, 2019.
 */
public class EmptyListTests {
	private static final List<String> emptyItems = new List<>();

	@Test
	public void checksEmpty() {
		assert emptyItems.isEmpty();
	}

	@Test
	public void returnsItemCount() {
		assert emptyItems.getCount() == 0;
	}

	@Test
	public void returnsFirstItem() {
		assert emptyItems.getFirst()
				.isEmpty();
	}

	@Test
	public void returnsLastItem() {
		assert emptyItems.getLast()
				.isEmpty();
	}

	@Test
	public void guardsIndex() {
		// index before valid range
		assert emptyItems.guard(-1)
				.isEmpty();

		// index after valid range
		assert emptyItems.guard(0)
				.isEmpty();
	}

	@Test
	public void returnsDistinctItems() {
		assert emptyItems.getDistinct()
				.equals(emptyItems);
	}

	@Test
	public void filtersItems() {
		assert emptyItems.filter(String::isBlank)
				.equals(emptyItems);
	}

	@Test
	public void convertsNoItems() {
		assert emptyItems.convert(String::toUpperCase)
				.equals(emptyItems);
	}
}
