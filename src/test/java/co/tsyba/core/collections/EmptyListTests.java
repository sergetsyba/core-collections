package co.tsyba.core.collections;

import org.junit.Test;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jul 25, 2019.
 */
public class EmptyListTests {
	private static final List<String> emptyItems = new List<>();

	@Test
	public void verifiesEmpty() {
		assert emptyItems.isEmpty();
	}

	@Test
	public void returnsItemCount() {
		assert emptyItems.getCount() == 0;
	}

	@Test
	public void returnsNoFirstItem() {
		assert emptyItems.getFirst()
				.isEmpty();
	}

	@Test
	public void returnsNoLastItem() {
		assert emptyItems.getLast()
				.isEmpty();
	}

	@Test
	public void returnsNoDistinctItems() {
		assert emptyItems.getDistinct()
				.equals(emptyItems);
	}

	@Test
	public void filtersNoItems() {
		assert emptyItems.filter(String::isBlank)
				.equals(emptyItems);
	}

	@Test
	public void convertsNoItems() {
		assert emptyItems.convert(String::toUpperCase)
				.equals(emptyItems);
	}
}
