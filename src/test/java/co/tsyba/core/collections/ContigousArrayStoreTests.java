package co.tsyba.core.collections;

import org.junit.Test;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jan 28, 2019.
 */
public class ContigousArrayStoreTests {
	@Test
	public void testStoreReturnsItem() {
		final var store = new ContigousArrayStore<String>(10);
		store.append("y", "d", "o", "z", "i");

		final var item = store.get(3);
		assert "z".equals(item);
	}

	@Test(expected = IndexNotInRangeException.class)
	public void testStoreFailsToReturnItemBeforeFirstIndex() {
		final var store = new ContigousArrayStore<String>(10);
		store.append("y", "d", "o", "z", "i");

		store.get(-1);
	}

	@Test(expected = IndexNotInRangeException.class)
	public void testStoreFailsToReturnItemAfterLastIndex() {
		final var store = new ContigousArrayStore<String>(10);
		store.append("y", "d", "o", "z", "i");

		store.get(5);
	}

	@Test
	public void testStoreReturnsItems() {
		final var store = new ContigousArrayStore<String>(10);
		store.append("t", "c", "l", "p", "x", "d");

		final var items = store.get(3, 6);
		assert items.itemsEqual("p", "x", "d");
	}

	@Test(expected = IndexRangeNotInRangeException.class)
	public void testStoreFailsToReturnItemsBeforeFirstIndex() {
		final var store = new ContigousArrayStore<String>(10);
		store.append("t", "c", "l", "p", "x", "d");

		store.get(-1, 5);
	}

	@Test(expected = IndexRangeNotInRangeException.class)
	public void testStoreFailsToReturnItemsAfterLastIndex() {
		final var store = new ContigousArrayStore<String>(10);
		store.append("t", "c", "l", "p", "x", "d");

		store.get(3, 7);
	}

	@Test(expected = InvalidIndexRangeException.class)
	public void testStoreFailsToReturnItemsInInvalidIndexRange() {
		final var store = new ContigousArrayStore<String>(10);
		store.append("t", "c", "l", "p", "x", "d");

		store.get(6, 3);
	}

	@Test
	public void testStoreAppendsItem() {
		final var store = new ContigousArrayStore<String>(10);
		store.append("r");
		store.append("o");
		store.append("v");

		assert store.itemsEqual("r", "o", "v");
	}

	@Test
	public void testStoreDoesNotAppendNull() {
		final var store = new ContigousArrayStore<String>(10);
		store.append((String) null);
		store.append("o");
		store.append((String) null);
		store.append("q");

		assert store.itemsEqual("o", "q");
	}

	@Test
	public void testStoreAppendsItemsFromStore() {
		final var store1 = new ContigousArrayStore<String>(10);
		store1.append("t", "h", "b");

		final var store2 = new ContigousArrayStore<String>(10);
		store2.append(store1);
		store2.append("g");
		store2.append(store2);

		assert store2.itemsEqual("t", "h", "b", "g", "t", "h", "b", "g");
	}

	@Test
	public void testStoreAppendsItems() {
		final var store = new ContigousArrayStore<String>(10);
		store.append("t", "h", "b");
		store.append("g", "f");

		assert store.itemsEqual("t", "h", "b", "g", "f");
	}

	@Test
	public void testStoreDoesNotAppendNulls() {
		final var store = new ContigousArrayStore<String>(10);
		store.append(null, null, "b");
		store.append("g", null);

		assert store.itemsEqual("b", "g");
	}

	@Test
	public void testStoreInsertsItem() {
		final var store = new ContigousArrayStore<String>(10);
		store.append("y", "f", "e", "q", "p");

		store.insert(2, "o");
		store.insert(0, "c");
		store.insert(6, "t");

		assert store.itemsEqual("c", "y", "f", "o", "e", "q", "t", "p");
	}

	@Test
	public void testStoreDoesNotInsertNull() {
		final var store = new ContigousArrayStore<String>(10);
		store.append("y", "f", "e", "q", "p");

		store.insert(2, (String) null);
		store.insert(0, (String) null);
		store.insert(4, "t");

		assert store.itemsEqual("y", "f", "e", "q", "t", "p");
	}

	@Test(expected = IndexNotInRangeException.class)
	public void testStoreFailsToInsertItemBeforeFirstIndex() {
		final var store = new ContigousArrayStore<String>(10);
		store.append("y", "f", "e", "q", "p");

		store.insert(-1, "o");
	}

	@Test(expected = IndexNotInRangeException.class)
	public void testStoreFailsToInsertItemAfterLastIndex() {
		final var store = new ContigousArrayStore<String>(10);
		store.append("y", "f", "e", "q", "p");

		store.insert(5, "o");
	}

	@Test
	public void testStoreInsertsItemsFromStore() {
		final var store1 = new ContigousArrayStore<String>(10);
		store1.append("y", "f");

		final var store2 = new ContigousArrayStore<String>(10);
		store2.append("e", "q", "p");
		store2.insert(1, store2);
		store2.insert(0, store1);

		assert store2.itemsEqual("y", "f", "e", "e", "q", "p", "q", "p");
	}

	@Test(expected = IndexNotInRangeException.class)
	public void testStoreFailsToInsertItemsFromStoreBeforeFirstIndex() {
		final var store1 = new ContigousArrayStore<String>(10);
		store1.append("y", "f");

		final var store2 = new ContigousArrayStore<String>(10);
		store2.append("e", "q", "p");
		store2.insert(-1, store1);
	}

	@Test(expected = IndexNotInRangeException.class)
	public void testStoreFailsToInsertItemsFromStoreAfterLastIndex() {
		final var store1 = new ContigousArrayStore<String>(10);
		store1.append("y", "f");

		final var store2 = new ContigousArrayStore<String>(10);
		store2.append("e", "q", "p");
		store2.insert(-1, store1);
	}

	@Test
	public void testStoreRemovesItem() {
		final var store = new ContigousArrayStore<String>(10);
		store.append("t", "d", "s", "k", "j");

		store.remove(3);
		assert store.itemsEqual("t", "d", "s", "j");
	}

	@Test(expected = IndexNotInRangeException.class)
	public void testStoreFailsToRemoveItemBeforeFirstIndex() {
		final var store = new ContigousArrayStore<String>(10);
		store.append("t", "d", "s", "k", "j");

		store.remove(-1);
	}

	@Test(expected = IndexNotInRangeException.class)
	public void testStoreFailsToRemoveItemAfterLastIndex() {
		final var store = new ContigousArrayStore<String>(10);
		store.append("t", "d", "s", "k", "j");

		store.remove(-1);
	}

	@Test
	public void testStoreRemovesItems() {
		final var store = new ContigousArrayStore<String>(10);
		store.append("y", "i", "e", "x", "p", "r");

		store.remove(3, 6);
		assert store.itemsEqual("y", "i", "e");
	}

	@Test(expected = IndexRangeNotInRangeException.class)
	public void testStoreFailsToRemoveItemsBeforeFirstIndex() {
		final var store = new ContigousArrayStore<String>(10);
		store.append("y", "i", "e", "x", "p", "r");

		store.remove(-1, 6);
	}

	@Test(expected = IndexRangeNotInRangeException.class)
	public void testStoreFailsToRemoveItemsAfterLastIndex() {
		final var store = new ContigousArrayStore<String>(10);
		store.append("y", "i", "e", "x", "p", "r");

		store.remove(3, 7);
	}

	@Test(expected = InvalidIndexRangeException.class)
	public void testStoreFailsToRemoveItemsInInvalidIndexRange() {
		final var store = new ContigousArrayStore<String>(10);
		store.append("y", "i", "e", "x", "p", "r");

		store.remove(6, 3);
	}
}
