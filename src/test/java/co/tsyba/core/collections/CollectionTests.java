package co.tsyba.core.collections;

import co.tsyba.core.collections.data.ListCollection;
import java.util.ArrayList;
import java.util.Comparator;
import org.junit.Test;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jul 7, 2019.
 */
public class CollectionTests {
	@Test
	public void verifiesNotEmpty() {
		final var items = collect("g", "r", "b", "q", "E");
		assert items.isEmpty() == false;
	}

	@Test
	public void returnsItemCount() {
		final var items = collect("g", "r", "b", "q", "E");
		assert items.getCount() == 5;
	}

	@Test
	public void checksItemContainment() {
		final var items = collect("v", "e", "f", "m", "h");

		// item is present
		assert items.contains("m");
		// item is absent
		assert items.contains("t") == false;
	}

	@Test
	public void checksItemsContainment() {
		final var items = collect("p", "i", "f", "z", "u");

		// all items are present
		final var items2 = collect("i", "u", "f", "p");
		assert items.contains(items2);

		// some items are absent
		final var items3 = collect("i", "u", "x", "p");
		assert items.contains(items3) == false;

		// itself is present
		assert items.contains(items);
	}

	@Test
	public void checksNoItemMatch() {
		final var items = collect("f", "e", "c", "l");

		// matches no item is in upper case
		assert items.noneMatches(item -> item.toUpperCase().equals(item));
		// does not match no item is c
		assert items.noneMatches(item -> item.equals("c")) == false;
	}

	@Test
	public void checksAnyItemMatch() {
		final var items = collect("p", "P", "z", "p", "P");

		// matches any item is in upper case
		assert items.anyMatches(item -> item.toUpperCase().equals(item));
		// does not match any item is Z
		assert items.anyMatches(item -> item.equals("Z")) == false;
	}

	@Test
	public void checksEachItemMatch() {
		final var items = collect("K", "D", "s", "k", "c");

		// matches each item is not blank
		assert items.eachMatches(item -> !item.isBlank());
		// does not match each item is in upper case
		assert items.eachMatches(item -> item.toUpperCase().equals(item)) == false;
	}

	@Test
	public void returnsMinimumItem() {
		final var items = collect("g", "l", "u", "e", "r");
		final var minimum = items.getMinimum(Comparator.naturalOrder())
				.get();

		assert minimum.equals("e");
	}

	@Test
	public void returnsMaximumItem() {
		final var items = collect("D", "r", "w", "n", "p");
		final var maximum = items.getMaximum(Comparator.naturalOrder())
				.get();

		assert maximum.equals("w");
	}

	@Test
	public void iteratesItems() {
		final var items = collect("t", "O", "X", "V", "c");
		final var iteratedItems = new ArrayList<String>();

		items.iterate(iteratedItems::add);

		assert iteratedItems.equals(
				java.util.List.of("t", "O", "X", "V", "c"));
	}

	@Test
	public void combinesItems() {
		final var items = collect("n", "M", "m", "a", "y");
		final var combination = items.combine("7",
				(combined, item) -> combined + item);

		assert combination.equals("7nMmay");
	}

	@Test
	public void joinsItems() {
		final var items = collect("P", "x", "Z", "j", "e");

		final var joined = items.join(", ");
		assert "P, x, Z, j, e".equals(joined);
	}

	private static <T> Collection<T> collect(T... items) {
		return new ListCollection<>(items);
	}
}
