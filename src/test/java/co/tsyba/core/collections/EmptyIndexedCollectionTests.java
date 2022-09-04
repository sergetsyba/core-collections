package co.tsyba.core.collections;

import co.tsyba.core.collections.data.ListCollection;
import java.util.ArrayList;
import org.junit.Test;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jul 17, 2019.
 */
public class EmptyIndexedCollectionTests {
	private static final IndexedCollection<String> emptyItems = new ListCollection<>();
	
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
		final var items = new ListCollection<>("u", "a", "g");
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
		final var enumeratedItems = new ArrayList<String>();
		final var enumeratedIndexes = new ArrayList<Integer>();
		
		emptyItems.enumerate((item, index) -> {
			enumeratedItems.add(item);
			enumeratedIndexes.add(index);
		});
		
		assert enumeratedItems.isEmpty();
		assert enumeratedIndexes.isEmpty();
	}
}
