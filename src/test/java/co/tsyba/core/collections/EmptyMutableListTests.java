package co.tsyba.core.collections;

import org.junit.Test;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jul 25, 2019.
 */
public class EmptyMutableListTests {
	private static final MutableList<String> emptyItems = new MutableList<>();

	@Test
	public void removesNoFirstItem() {
		assert emptyItems.removeFirst()
				.isEmpty();
	}

	@Test
	public void removesNoLastItem() {
		assert emptyItems.removeLast()
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
