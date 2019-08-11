package co.tsyba.core.collections;

import co.tsyba.core.collections.data.ListCollection;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jul 15, 2019.
 */
public class IndexedCollectionTests {
	@Test
	public void returnsFirstItem() {
		final var items = collect("U", "n", "B", "C", "V");
		final var firstItem = items.getFirst()
				.get();

		assert firstItem.equals("U");
	}

	@Test
	public void returnsLastItem() {
		final var items = collect("U", "n", "B", "C", "V");
		final var lastItem = items.getLast()
				.get();

		assert lastItem.equals("V");
	}

	@Test
	public void containsItems() {
		final var items = collect("t", "x", "O", "p", "s");

		// contains items
		final var items1 = collect("O", "p", "s");
		assert items.contains(items1);

		// does not contain items
		final var items2 = collect("O", "p", "S");
		assert items.contains(items2) == false;

		// contains itslef
		assert items.contains(items);

		// contains empty items
		final var items4 = IndexedCollectionTests.<String>collect();
		assert items.contains(items4);
	}

	@Test
	public void findsItem() {
		final var items = collect("j", "q", "z", "k", "e");

		// item is present
		final var index1 = items.find("k");
		assert index1.get() == 3;

		// item is absent
		final var index2 = items.find("K");
		assert index2.isEmpty();
	}

	@Test
	public void findsItems() {
		final var items = collect("u", "a", "g", "Z", "R");

		// items are present
		final var items1 = collect("a", "g", "Z", "R");
		final var index1 = items.find(items1);
		assert index1.get() == 1;

		// items are absent
		final var items2 = collect("a", "g", "Z", "r");
		final var index2 = items.find(items2);
		assert index2.isEmpty();

		// finds itself
		final var index3 = items.find(items);
		assert index3.get() == 0;

		// finds empty items
		final var emptyItems = IndexedCollectionTests.<String>collect();
		final var index4 = items.find(emptyItems);
		assert index4.get() == 0;
	}

	@Test
	public void matchesItem() {
		final var items = collect("f", "b", "e", "E", "e");

		// matches E
		final var index1 = items.match(item -> item.toUpperCase().equals(item));
		assert index1.get() == 3;

		// does not match Z
		final var index2 = items.match(item -> item.equals("Z"));
		assert index2.isEmpty();
	}

	@Test
	public void enumeratesItems() {
		final var items = collect("t", "Q", "x", "z", "U");
		final var enumeratedItems = new ArrayList<String>();
		final var enumeratedIndexes = new ArrayList<Integer>();

		items.enumerate((item, index) -> {
			enumeratedItems.add(item);
			enumeratedIndexes.add(index);
		});

		assert enumeratedItems.equals(
				List.of("t", "Q", "x", "z", "U"));
		assert enumeratedIndexes.equals(
				List.of(0, 1, 2, 3, 4));
	}

	private static <T> IndexedCollection<T> collect(T... items) {
		return new ListCollection<>(items);
	}
}
