package co.tsyba.core.collections;

import org.junit.Test;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jul 25, 2019.
 */
public class EmptyMutableListTests {
	private static final MutableList<String> emptyItems = new MutableList<>();

	@Test
	public void removesFirstItem() {
		assert emptyItems.removeFirst()
				.isEmpty();
	}

	@Test
	public void removesLastItem() {
		assert emptyItems.removeLast()
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
	public void convertsItems() {
		assert emptyItems.convert(String::toUpperCase)
				.equals(emptyItems);
	}
}
