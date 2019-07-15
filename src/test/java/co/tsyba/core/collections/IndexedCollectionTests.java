package co.tsyba.core.collections;

import org.junit.Test;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jul 15, 2019.
 */
public class IndexedCollectionTests {
	@Test
	public void testCollectionReturnsFirstItem() {
		final var item1 = collect("U", "n", "B", "C", "V")
				.getFirst();

		// returns first item
		assert item1.get()
				.equals("U");

		final var item2 = collect()
				.getFirst();

		// does not return first item
		assert item2.isEmpty();
	}

	@Test
	public void testCollectionReturnsLastItem() {
		final var item1 = collect("U", "n", "B", "C", "V")
				.getLast();

		// returns last item
		assert item1.get()
				.equals("V");

		final var item2 = collect()
				.getLast();

		// does not return last item
		assert item2.isEmpty();
	}

	private static <T> IndexedCollection<T> collect(T... items) {
		return new List<>(items);
	}
}
