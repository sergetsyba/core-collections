package co.tsyba.core.collections;

import java.util.Comparator;
import org.junit.Test;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jul 7, 2019.
 */
public class CollectionTests {
	@Test
	public void testCollectionVerifiesEmpty() {
		// non-empty items
		final var items1 = collect("g", "r", "b", "q", "E");
		assert !items1.isEmpty();

		// empty items
		final var items2 = collect();
		assert items2.isEmpty();
	}

	@Test
	public void testCollectionReturnsItemCount() {
		// non-empty items
		final var items1 = collect("g", "r", "b", "q", "E");
		assert items1.getCount() == 5;

		// empty items
		final var items2 = collect();
		assert items2.getCount() == 0;
	}

	@Test
	public void testCollectionVerifiesItemPresence() {
		final var items = collect("v", "e", "f", "m", "h");

		// contains item
		assert items.contains("m");
		// does not contain item
		assert !items.contains("t");
	}

	@Test
	public void testCollectionFindsItem() {
		final var items = collect("j", "q", "z", "k", "e");

		// finds item
		final var index1 = items.find("k");
		assert index1.get() == 3;

		// does not find item
		final var index2 = items.find("f");
		assert index2.isEmpty();
	}

	@Test
	public void testCollectionMatchesItem() {
		final var items = collect("f", "b", "e", "E", "e");

		// finds match
		final var index1 = items.match(item -> item.equals("e"));
		assert index1.get() == 2;

		// does not find match
		final var index2 = items.match(item -> item.equals("z"));
		assert index2.isEmpty();
	}

	@Test
	public void testCollectionMatchesEachItem() {
		final var items = collect("K", "D", "s", "k", "c");

		// matches each item
		assert items.eachMatches(item -> !item.isBlank());
		// does not match each item
		assert !items.eachMatches(item -> item.toUpperCase()
				.equals(item));
	}

	@Test
	public void testCollectionMatchesNoneItem() {
		final var items = collect("b", "e", "m", "n", "b");

		// matches no item
		assert items.noneMatches(item -> item.isBlank());
		// does not match no items
		assert items.noneMatches(item -> item.toUpperCase()
				.equals(item));
	}

	@Test
	public void testCollectionReturnsMinimumItem() {
		final var minimum1 = collect("g", "l", "u", "e", "r")
				.getMinimum(Comparator.naturalOrder());

		// returns minimum
		assert minimum1.get()
				.equals("e");

		final var minimum2 = new List<String>()
				.getMinimum(Comparator.naturalOrder());

		// does not return minimum
		assert minimum2.isEmpty();
	}

	@Test
	public void testCollectionReturnsMaximumItem() {
		final var maximum = collect("D", "r", "w", "n", "p")
				.getMaximum(Comparator.naturalOrder());

		// return maximum
		assert maximum.get()
				.equals("w");

		final var maximum2 = new List<String>()
				.getMaximum(Comparator.naturalOrder());

		// does not return maximum
		assert maximum2.isEmpty();
	}

	@Test
	public void testCollectionIteratesItems() {
		final var items = collect("t", "O", "X", "V", "c");

		final var iteratedItems = new MutableList<String>();
		items.iterate(iteratedItems::append);

		assert iteratedItems.equals(items);
	}

	@Test
	public void testCollectionCombinesItems() {
		final var items = collect("n", "M", "m", "a", "y");
		final var combination = items.combine("7",
				(combined, item) -> combined + item);

		assert combination.equals("7nMmay");
	}

	private static <T> Collection<T> collect(T... items) {
		return new List<>(items);
	}
}
