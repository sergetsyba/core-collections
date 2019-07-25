package co.tsyba.core.collections;

import java.util.Comparator;
import org.junit.Test;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jul 17, 2019.
 */
public class EmptyCollectionTests {
	private static final Collection<String> emptyItems = new List<>();

	@Test
	public void verifiesEmpty() {
		assert emptyItems.isEmpty();
	}

	@Test
	public void returnsItemCount() {
		assert emptyItems.getCount() == 0;
	}

	@Test
	public void verifiesNoItemContainment() {
		assert emptyItems.contains("m") == false;
	}

	@Test
	public void verifiesNoItemsContainment() {
		final var items = collect("i", "u", "f", "p");
		assert emptyItems.contains(items) == false;

		// empty collection does not contain itself;
		// note: if empty collection contains itself, what index does 
		// empty collection appear at
		assert emptyItems.contains(emptyItems) == false;
	}

	@Test
	public void verifiesNoItemMatch() {
		assert emptyItems.contains(item -> item.equals("P")) == false;
	}

	@Test
	public void verifiesNoEachItemMatch() {
		assert emptyItems.matches(String::isBlank) == false;
	}

	@Test
	public void returnsNoMinimumItem() {
		final var minimum = emptyItems.getMinimum(Comparator.naturalOrder());
		assert minimum.isEmpty();
	}

	@Test
	public void returnsNoMaximumItem() {
		final var maximum = emptyItems.getMaximum(Comparator.naturalOrder());
		assert maximum.isEmpty();
	}

	@Test
	public void iteratesNoItems() {
		final var iteratedItems = new MutableList<String>();
		emptyItems.iterate(iteratedItems::append);

		assert iteratedItems.isEmpty();
	}

	@Test
	public void combinesNoItems() {
		final var combination = emptyItems.combine("7",
				(combined, item) -> combined + item);

		assert "7".equals(combination);
	}

	@Test
	public void joinsNoItems() {
		final var joined = emptyItems.join(", ");
		assert "".equals(joined);
	}

	private static <T> Collection<T> collect(T... items) {
		return new List<>(items);
	}
}
