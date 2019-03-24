package co.tsyba.core.collections;

import java.util.Arrays;
import org.junit.Test;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jan 28, 2019.
 */
public class ArrayStoreTests {
	@Test
	public void testStoreCreatesWithCapacity() {
		final var store = new ArrayStore<String>(5);
		assert store.itemsEqual(new String[] {});
	}

	@Test(expected = AssertionError.class)
	public void testStoreFailsToCreateWithNegativeCapacity() {
		new ArrayStore<String>(-1);
	}

	@Test
	public void testStoreAppendsItem() {
		final var store = new ArrayStore<String>(5);
		store.append("a");
		store.append("u");
		store.append("b");

		assert store.itemsEqual(new String[] {
			"a", "u", "b"
		});
	}

	@Test
	public void testStoreAppendsItemOverCapacity() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"a", "b", "c", "d", "e"
		});

		store.append("f");

		assert store.itemsEqual(new String[] {
			"a", "b", "c", "d", "e", "f"
		});
	}

	@Test
	public void testStoreAppendsItems() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"k", "p", "f"
		});

		assert store.itemsEqual(new String[] {
			"k", "p", "f"
		});
	}

	@Test
	public void testStoreAppendsEmptyItems() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"h", "d"
		});

		store.append(new String[] {});

		assert store.itemsEqual(new String[] {
			"h", "d"
		});
	}

	@Test
	public void testStoreAppendsItemsOverCapacity() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"a", "b", "c", "d", "e"
		});

		store.append(new String[] {
			"f", "g"
		});

		assert store.itemsEqual(new String[] {
			"a", "b", "c", "d", "e", "f", "g"
		});
	}

	@Test
	public void testStoreInsertsItem() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"b", "d", "k"
		});

		store.insert(1, "x");

		assert store.itemsEqual(new String[] {
			"b", "x", "d", "k"
		});
	}

	@Test(expected = AssertionError.class)
	public void testStoreFailsToInsertItemBeforeFirstIndex() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"b", "d", "k"
		});

		store.insert(-1, "x");
	}

	@Test(expected = AssertionError.class)
	public void testStoreFailsToInsertItemAfterLastIndex() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"b", "d", "k"
		});

		store.insert(3, "x");
	}

	@Test
	public void testStoreInsertsItemOverCapacity() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"a", "b", "c", "d", "e"
		});

		store.insert(3, "f");

		assert store.itemsEqual(new String[] {
			"a", "b", "c", "f", "d", "e"
		});
	}

	@Test
	public void testStoreInsertsItems() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"g", "e", "a"
		});

		store.insert(1, new String[] {
			"i", "a"
		});

		assert store.itemsEqual(new String[] {
			"g", "i", "a", "e", "a"
		});
	}

	@Test
	public void testStoreInsertsEmptyItems() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"g", "e", "a"
		});

		store.insert(1, new String[0]);

		assert store.itemsEqual(new String[] {
			"g", "e", "a"
		});
	}

	@Test(expected = AssertionError.class)
	public void testStoreFailsToInsertItemsBeforeFirstIndex() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"g", "e", "a"
		});

		store.insert(-1, new String[] {
			"i", "a"
		});
	}

	@Test(expected = AssertionError.class)
	public void testStoreFailsToInsertItemsAfterLastIndex() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"g", "e", "a"
		});

		store.insert(3, new String[] {
			"i", "a"
		});
	}

	@Test
	public void testInsertsItemsOverCapacity() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"a", "b", "c", "d"
		});

		store.insert(3, new String[] {
			"e", "f", "g"
		});

		assert store.itemsEqual(new String[] {
			"a", "b", "c", "e", "f", "g", "d"
		});
	}

	@Test
	public void testStoreFindsItem() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"b", "e", "f", "d", "e"
		});

		final var index1 = store.find(0, 5, "e");
		assert index1 == 1;

		final var index2 = store.find(2, 5, "e");
		assert index2 == 4;
	}

	@Test
	public void testStoreDoesNotFindAbsentItem() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"b", "e", "f", "d", "e"
		});

		final var index = store.find(1, 5, "b");
		assert index == -1;
	}

	@Test
	public void testStoreDoesNotFindItemInEmptyIndexRange() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"b", "e", "f", "d", "e"
		});

		final var index = store.find(1, 1, "e");
		assert index == -1;
	}

	@Test(expected = AssertionError.class)
	public void testStoreFailsToFindItemBeforeFirstIndex() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"b", "e", "f", "d", "e"
		});

		store.find(-1, 5, "e");
	}

	@Test(expected = AssertionError.class)
	public void testStoreFailsToFindItemAfterLastIndex() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"b", "e", "f", "d", "e"
		});

		store.find(0, 6, "e");
	}

	public void testStoreFailsToFindItemInInvalidIndexRange() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"b", "e", "f", "d", "e"
		});

		store.find(4, 2, "e");
	}

	@Test
	public void testStoreRemovesItem() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"f", "j", "c"
		});

		final var removedItem = store.remove(1);

		assert removedItem.equals("j");
		assert store.itemsEqual(new String[] {
			"f", "c"
		});
	}

	@Test
	public void testStoreRemovesLastItem() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"f", "j", "c", "d", "r"
		});

		final var removedItem = store.remove(4);

		assert removedItem.equals("r");
		assert store.itemsEqual(new String[] {
			"f", "j", "c", "d"
		});
	}

	@Test(expected = AssertionError.class)
	public void testStoreFailsToRemoveItemBeforeFirstIndex() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"f", "j", "c"
		});

		store.remove(-1);
	}

	@Test(expected = AssertionError.class)
	public void testStoreFailsToRemoveItemAfterLastIndex() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"f", "j", "c"
		});

		store.remove(3);
	}

	@Test
	public void testStoreRemovesItems() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"a", "b", "c", "d", "e"
		});

		final var removedItems = store.remove(1, 4);
		assert Arrays.equals(removedItems, new Object[] {
			"b", "c", "d"
		});

		assert store.itemsEqual(new String[] {
			"a", "e"
		});
	}

	@Test
	public void testStoreRemovesItemsInEmptyIndexRange() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"a", "b", "c", "d", "e"
		});

		final var removedItems = store.remove(1, 1);
		assert Arrays.equals(removedItems, new Object[] {});

		assert store.itemsEqual(new String[] {
			"a", "b", "c", "d", "e"
		});
	}

	@Test(expected = AssertionError.class)
	public void testStoreFailsToRemoveItemsBeforeFirstIndex() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"a", "b", "c", "d", "e"
		});

		store.remove(-1, 4);
	}

	@Test(expected = AssertionError.class)
	public void testStoreFailsToRemoveItemsAfterLastIndex() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"a", "b", "c", "d", "e"
		});

		store.remove(0, 6);
	}

	@Test(expected = AssertionError.class)
	public void testStoreFailsToRemoveItemsInInvalidIndexRange() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"a", "b", "c", "d", "e"
		});

		store.remove(4, 1);
	}

	@Test
	public void testStoreIteratesItems() {
		final var store = new ArrayStore<String>(5);
		store.append(new String[] {
			"a", "b", "c", "d", "e"
		});

		final var iteratedItems = new String[5];
		var index = 0;

		for (var item : store) {
			iteratedItems[index] = item;
			index += 1;
		}

		assert store.itemsEqual(iteratedItems);
	}

	@Test
	public void testStoreIteratesEmptyItems() {
		final var store = new ArrayStore<String>(5);

		final var iteratedItems = new String[0];
		var index = 0;

		for (var item : store) {
			iteratedItems[index] = item;
			index += 1;
		}

		assert store.itemsEqual(iteratedItems);
	}

	@Test
	public void testStoreConfirmsEqualToEqualStore() {
		final var store1 = new ArrayStore<String>(5);
		store1.append(new String[] {
			"a", "b", "c", "d", "e"
		});

		final var store2 = new ArrayStore<String>(10);
		store2.append(new String[] {
			"a", "b", "c", "d", "e"
		});

		assert store1.equals(store2);
	}

	@Test
	public void testStoreConfirmsEqualToEmptyStore() {
		final var store1 = new ArrayStore<String>(5);
		final var store2 = new ArrayStore<String>(0);

		assert store1.equals(store2);
	}

	@Test
	public void testStoreDoesNotConfirmEqualToDifferentStore() {
		final var store1 = new ArrayStore<String>(5);
		store1.append(new String[] {
			"a", "b", "c", "d", "e"
		});

		final var store2 = new ArrayStore<String>(10);
		store2.append(new String[] {
			"a", "b", "c", "d", "f"
		});

		assert !store1.equals(store2);
	}
}
