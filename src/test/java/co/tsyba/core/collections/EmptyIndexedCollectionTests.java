package co.tsyba.core.collections;

import org.junit.Test;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jul 17, 2019.
 */
public class EmptyIndexedCollectionTests {
	private static final IndexedCollection<String> emptyItems = new List<>();

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
	public void findsNoItem() {
		assert emptyItems.find("F")
				.isEmpty();
	}

	@Test
	public void findsNoItems() {
		final var items = collect("u", "a", "g");
		assert emptyItems.find(items)
				.isEmpty();

		// empty collection does not contains itself
		assert emptyItems.find(emptyItems)
				.isEmpty();
	}

	@Test
	public void matchesNoItem() {
		assert emptyItems.match(String::isBlank)
				.isEmpty();
	}

	@Test
	public void enumeratesNoItems() {
		final var items = new MutableList<String>();
		final var indexes = new MutableList<Integer>();

		emptyItems.enumerate((item, index) -> {
			items.append(item);
			indexes.append(index);
		});

		assert items.isEmpty();
		assert indexes.isEmpty();
	}

	private static <T> IndexedCollection<T> collect(T... items) {
		return new List<>(items);
	}
}
