package co.tsyba.core.collections;

import org.junit.Test;

/*
 * Created by Serge Tsyba <serge.tsyba@tsyba.com> on Dec 21, 2018.
 */
public class RobinHoodHashStoreTests {
	@Test
	public void testHashStoreCreatesNewInstance() {
		final var store = new RobinHoodHashStore<Integer>(5, 1.0);
		assert store.storageEquals(null, null, null, null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHashStoreDoesNotCreateNewInstanceWithNegativeCapacity() {
		new RobinHoodHashStore<Integer>(-1, 1.0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHashStoreDoesNotCreateNewInstanceWithNegativeLoadFactor() {
		new RobinHoodHashStore<Integer>(5, -0.1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHashStoreDoesNotCreateNewInstanceWithLoadFactorOver1() {
		new RobinHoodHashStore<Integer>(5, 1.1);
	}

	@Test
	public void testHashStoreAddsItemIntoEmptySlot() {
		final var store = new RobinHoodHashStore<Integer>(5, 1.0);
		store.add(17);
		store.add(0);

		assert store.storageEquals(0, null, 17, null, null);
	}

	@Test
	public void testHashStoreAddsItemWithHashCollision() {
		final var store = new RobinHoodHashStore<Integer>(5, 1.0);
		store.add(0);
		store.add(10);
		store.add(5);

		assert store.storageEquals(0, 10, 5, null, null);
	}

	@Test
	public void testHashStoreAddsItemOccupiedByPoorerEntries() {
		final var store = new RobinHoodHashStore<Integer>(5, 1.0);
		store.add(0);
		store.add(5);
		store.add(10);

		store.add(1);

		assert store.storageEquals(0, 5, 10, 1, null);
	}

	@Test
	public void testHashStoreAddsItemOccupiedByRicherEntries() {
		final var store = new RobinHoodHashStore<Integer>(5, 1.0);
		store.add(0);
		store.add(1);
		store.add(6);

		store.add(5);

		assert store.storageEquals(0, 5, 1, 6, null);
	}

	@Test
	public void testHashStoreReplacesEqualItem() {
		final var entry1 = new IntegerValue(12);
		final var entry2 = new IntegerValue(12);
		assert entry1 != entry2;

		final var store = new RobinHoodHashStore<IntegerValue>(5, 1.0);
		store.add(entry1);
		assert store.storageEquals(null, null, entry1, null, null);

		store.add(entry2);
		// todo: this performs comparison by value
		assert store.storageEquals(null, null, entry2, null, null);
	}

	@Test
	public void testHashStoreFindsItemInExactSlot() {
		final var store = new RobinHoodHashStore<Integer>(5, 1.0);
		store.add(27);
		store.add(13);
		store.add(86);

		final var index = store.find(27);
		assert index == 2;
	}

	@Test
	public void testHashStoreFindsItemWithHashCollision() {
		final var store = new RobinHoodHashStore<Integer>(5, 1.0);
		store.add(2);
		store.add(12);
		store.add(7);

		final var index = store.find(12);
		assert index == 3;
	}

	@Test
	public void testHashStoreFindsItemOccupiedByPoorerEntries() {
		final var store = new RobinHoodHashStore<Integer>(5, 1.0);
		store.add(0);
		store.add(5);
		store.add(10);

		store.add(1);

		final var index = store.find(1);
		assert index == 3;
	}

	@Test
	public void testHashStoreFindsItemOccupiedByRicherEntries() {
		final var store = new RobinHoodHashStore<Integer>(5, 1.0);
		store.add(0);
		store.add(1);
		store.add(6);

		store.add(5);

		final var index = store.find(5);
		assert index == 1;
	}

	@Test
	public void testHashStoreFindsNoAbsentItem() {
		final var store = new RobinHoodHashStore<Integer>(5, 1.0);
		store.add(0);
		store.add(7);
		store.add(14);

		final var index = store.find(2);
		assert index < 0;
	}

	@Test
	public void testHashStoreRemovesItemInExactSlot() {
		final var store = new RobinHoodHashStore<Integer>(5, 1.0);
		store.add(4);
		store.add(12);
		store.add(0);

		assert store.remove(4);
		assert store.storageEquals(0, null, 12, null, null);
	}

	@Test
	public void testHashStoreRemovesItemWithHashCollision() {
		final var store = new RobinHoodHashStore<Integer>(5, 1.0);
		store.add(7);
		store.add(32);
		store.add(37);

		assert store.remove(37);
		assert store.storageEquals(null, null, 7, 32, null);
	}

	@Test
	public void testHashStoreRemovesItemOccupiedByPoorerEntries() {
		final var store = new RobinHoodHashStore<Integer>(5, 1.0);
		store.add(1);
		store.add(21);
		store.add(16);

		store.add(12);

		assert store.remove(12);
		assert store.storageEquals(null, 1, 21, 16, null);
	}

	@Test
	public void testHashStoreRemovesItemOccupiedByRicherEntries() {
		final var store = new RobinHoodHashStore<Integer>(5, 1.0);
		store.add(0);
		store.add(1);
		store.add(6);

		store.add(5);

		assert store.remove(5);
		assert store.storageEquals(0, 1, 6, null, null);
	}

	@Test
	public void testHashStoreRemovesNoAbsentItem() {
		final var store = new RobinHoodHashStore<Integer>(5, 1.0);
		store.add(0);
		store.add(7);
		store.add(14);

		assert store.remove(12) == false;
		assert store.storageEquals(0, null, 7, null, 14);
	}

	@Test
	public void testHashStoreExpandsStorageAfterLastSlotOccupied() {
		final var store = new RobinHoodHashStore<Integer>(5, 1.0);
		store.add(8);
		store.add(9);
		assert store.storageEquals(null, null, null, 8, 9);

		store.add(4);
		assert store.storageEquals(null, null, null, null, 4,
				null, null, null, 8, 9,
				null, null, null, null, null,
				null, null, null, null, null);
	}

	@Test
	public void testHashStoreExpandsStorageAfterReachingLoadFactor() {
		final var store = new RobinHoodHashStore<Integer>(5, 0.5);
		store.add(0);
		store.add(1);
		assert store.storageEquals(0, 1, null, null, null);

		store.add(2);
		assert store.storageEquals(0, 1, 2, null, null,
				null, null, null, null, null);
	}
}
