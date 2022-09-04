package co.tsyba.core.collections;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import org.junit.Test;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jan 28, 2019.
 */
public class ContigousArrayStoreTests {
	@Test
	public void resturnItem() {
		final var store = store("u", "z", "f", "s", "a");

		// first item
		assert store.get(0)
				.equals("u");
		// item in the middle
		assert store.get(3)
				.equals("s");
		// last item
		assert store.get(4)
				.equals("a");
	}

	@Test(expected = IndexNotInRangeException.class)
	public void failsToReturnItemBeforeFirstIndex() {
		store("u", "z", "f", "s", "a")
				.get(-1);
	}

	@Test(expected = IndexNotInRangeException.class)
	public void failsToReturnItemAfterLastIndex() {
		store("u", "z", "f", "s", "a")
				.get(5);
	}

	@Test
	public void returnsItems() {
		final var store = store("t", "c", "l", "p", "x");

		// index range at start
		final var range1 = new IndexRange(0, 3);
		assert store.get(range1)
				.equals(store("t", "c", "l"));

		// index range in the middle
		final var range2 = new IndexRange(2, 3);
		assert store.get(range2)
				.equals(store("l"));

		// range at end
		final var range3 = new IndexRange(2, 5);
		assert store.get(range3)
				.equals(store("l", "p", "x"));

		// full range
		final var range5 = new IndexRange(0, 5);
		assert store.get(range5)
				.equals(store);
	}

	@Test(expected = Exception.class)
	public void failsToReturnItemsBeforeFirstIndex() {
		final var indexRange = new IndexRange(-1, 2);
		store("t", "c", "l", "p", "x")
				.get(indexRange);
	}

	@Test(expected = Exception.class)
	public void failsToReturnItemsAfterLastIndex() {
		final var indexRange = new IndexRange(3, 7);
		store("t", "c", "l", "p", "x")
				.get(indexRange);
	}

	@Test
	public void setsItem() {
		final var store = store("f", "b", "r", "o", "w");

		// first item
		store.set(0, "g");
		assert store("g", "b", "r", "o", "w")
				.equals(store);

		// item in the middle
		store.set(3, "t");
		assert store("g", "b", "r", "t", "w")
				.equals(store);

		// last item
		store.set(4, "z");
		assert store("g", "b", "r", "t", "z")
				.equals(store);

		// null value
		store.set(3, null);
		assert store("g", "b", "r", "t", "z")
				.equals(store);
	}

	@Test(expected = IndexNotInRangeException.class)
	public void failsToSetItemBeforeFirstIndex() {
		store("f", "b", "r", "o", "w")
				.set(-1, "g");
	}

	@Test(expected = IndexNotInRangeException.class)
	public void failsToSetItemAfterLastIndex() {
		store("f", "b", "r", "o", "w")
				.set(5, "g");
	}

	@Test
	public void appendsItem() {
		final var store = store("t", "f", "h");

		// appends item
		store.append("r");
		store.append("o");

		assert store("t", "f", "h", "r", "o")
				.equals(store);

		// appends null
		store.append((String) null);
		store.append("v");
		store.append((String) null);

		assert store("t", "f", "h", "r", "o", "v")
				.equals(store);
	}

	@Test
	public void appendsItems() {
		final var store = store("t", "h", "b");

		// appends store
		final var store1 = store("r", "v");
		store.append(store1);

		assert store("t", "h", "b", "r", "v")
				.equals(store);

		// appends empty store
		final var store2 = store();
		store.append(store2);

		assert store("t", "h", "b", "r", "v")
				.equals(store);

		// appends itself
		store.append(store);
		assert store("t", "h", "b", "r", "v", "t", "h", "b", "r", "v")
				.equals(store);
	}

	@Test
	public void appendsVariardicItems() {
		final var store = store("t", "h", "b");

		// appends items
		store.append("g", "f");
		assert store("t", "h", "b", "g", "f")
				.equals(store);

		// does not append nulls
		store.append(null, null, "r");
		store.append("g", null);

		assert store("t", "h", "b", "g", "f", "r", "g")
				.equals(store);
	}

	@Test
	public void insertsItem() {
		final var store = store("y", "q", "p");

		// inserts item
		store.insert(0, "c");
		store.insert(2, "o");

		assert store("c", "y", "o", "q", "p")
				.equals(store);

		// does not insert null
		store.insert(1, (String) null);
		store.insert(3, "z");
		store.insert(3, (String) null);

		assert store("c", "y", "o", "z", "q", "p")
				.equals(store);
	}

	@Test(expected = Exception.class)
	public void failsToInsertItemBeforeFirstIndex() {
		store("y", "q", "p")
				.insert(-1, "o");
	}

	@Test(expected = Exception.class)
	public void failsToInsertItemAfterLastIndex() {
		store("y", "q", "p")
				.insert(5, "o");
	}

	@Test
	public void insertsItems() {
		final var store = store("y", "f");

		// appends store
		final var store1 = store("e", "q", "p");
		store.insert(1, store1);

		assert store("y", "e", "q", "p", "f")
				.equals(store);

		// inserts empty store
		final var store2 = store();
		store.insert(4, store2);

		assert store("y", "e", "q", "p", "f")
				.equals(store);

		// inserts itself
		store.insert(4, store);
		assert store("y", "e", "q", "p", "y", "e", "q", "p", "f", "f")
				.equals(store);
	}

	@Test(expected = Exception.class)
	public void failsToInsertItemsFromStoreBeforeFirstIndex() {
		store("y", "f")
				.insert(-1, store("e", "q", "p"));
	}

	@Test(expected = Exception.class)
	public void failsToInsertItemsFromStoreAfterLastIndex() {
		store("y", "f")
				.insert(2, store("e", "q", "p"));
	}

	@Test
	public void removesItem() {
		final var store = store("t", "d", "s", "k", "j");

		// removes first item
		store.remove(0);
		assert store("d", "s", "k", "j")
				.equals(store);

		// removes middle item
		store.remove(2);
		assert store("d", "s", "j")
				.equals(store);

		// removes last item
		store.remove(2);
		assert store("d", "s")
				.equals(store);
	}

	@Test(expected = Exception.class)
	public void failsToRemoveItemBeforeFirstIndex() {
		store("t", "d", "s", "k", "j")
				.remove(-1);
	}

	@Test(expected = Exception.class)
	public void failsToRemoveItemAfterLastIndex() {
		store("t", "d", "s", "k", "j")
				.remove(5);
	}

	@Test
	public void removesItems() {
		// index range at start
		final var store1 = store("y", "i", "x", "p", "r");
		final var range1 = new IndexRange(0, 2);
		store1.remove(range1);

		assert store("x", "p", "r")
				.equals(store1);

		// index range in the middle
		final var store2 = store("y", "i", "x", "p", "r");
		final var range2 = new IndexRange(2, 3);
		store2.remove(range2);

		assert store("y", "i", "p", "r")
				.equals(store2);

		// range at end
		final var store3 = store("y", "i", "x", "p", "r");
		final var range3 = new IndexRange(2, 5);
		store3.remove(range3);

		assert store("y", "i")
				.equals(store3);

		// full range
		final var store5 = store("y", "i", "x", "p", "r");
		final var range5 = new IndexRange(0, 5);
		store5.remove(range5);

		assert store()
				.equals(store5);
	}

	@Test(expected = Exception.class)
	public void failsToRemoveItemsBeforeFirstIndex() {
		final var indexRange = new IndexRange(-1, 2);
		store("y", "i", "x", "p", "r")
				.remove(indexRange);
	}

	@Test(expected = Exception.class)
	public void failsToRemoveItemsAfterLastIndex() {
		final var indexRange = new IndexRange(3, 7);
		store("y", "i", "x", "p", "r")
				.remove(indexRange);
	}

	@Test
	public void reverses() {
		final var store = store("A", "c", "V", "M", "B");
		final var reversedStore = store.reverse();

		assert store("B", "M", "V", "c", "A")
				.equals(reversedStore);
	}

	@Test
	public void sorts() {
		final var store = store("i", "O", "b", "Q", "v");
		final var sortedStore = store.sort(
				Comparator.naturalOrder());

		assert store("O", "Q", "b", "i", "v")
				.equals(sortedStore);
	}

	@Test
	public void shuffles() {
		final var store = store("I", "R", "Z", "t", "w");
		final var random = new Random();
		final var shuffledStore = store.shuffle(random);

		assert !shuffledStore.equals(store);
		for (var item : shuffledStore) {
			assert Arrays.binarySearch(store.storage, 0, 5, item) > -1;
		}
	}

	private static ContigousArrayStore<String> store(String... items) {
		final var store = new ContigousArrayStore<String>(items.length);
		store.append(items);

		return store;
	}
}
