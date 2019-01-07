package co.tsyba.core.collections;

import org.junit.Test;

/*
 * Created by Serge Tsyba <serge.tsyba@tsyba.com> on Dec 21, 2018.
 */
public class RobinHoodHashStoreTests {
	@Test
	public void testHashStoreCreatesHashStore() {
		final var store = new RobinHoodHashStore<Integer>(5, 0.99);
		assert store.storageEquals(null, null, null, null, null);
	}

	@Test
	public void testHashStoreCreatesEmptyHashStore() {
		new RobinHoodHashStore<Integer>(0, 0.99);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHashStoreDoesNotCreateHashStoreWithNegativeCapacity() {
		new RobinHoodHashStore<Integer>(-1, 0.99);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHashStoreDoesNotCreateHashStoreWithNegativeLoadFactor() {
		new RobinHoodHashStore<Integer>(5, -0.1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHashStoreDoesNotCreateHashStoreWithLoadFactorZero() {
		new RobinHoodHashStore<Integer>(5, 0.0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHashStoreDoesNotCreateHashStoreWithLoadFactorOverOne() {
		new RobinHoodHashStore<Integer>(5, 1.1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHashStoreDoesNotCreateHashStoreWithLoadFactorOne() {
		new RobinHoodHashStore<Integer>(5, 1.1);
	}

	@Test
	public void testHashStoreAddsItemIntoEmptySlot() {
		final var store = new RobinHoodHashStore<Integer>(5, 0.99, 5);
		store.insert(17);
		store.insert(0);

		assert store.storageEquals(0, null, 17, null, null);
	}

	@Test
	public void testHashStoreAddsItemWithHashCollision() {
		final var store = new RobinHoodHashStore<Integer>(5, 0.99, 5);
		store.insert(0);
		store.insert(10);
		store.insert(5);

		assert store.storageEquals(0, 10, 5, null, null);
	}

	@Test
	public void testHashStoreAddsItemOccupiedByPoorerEntries() {
		final var store = new RobinHoodHashStore<Integer>(5, 0.99, 5);
		store.insert(0);
		store.insert(5);
		store.insert(10);

		store.insert(1);

		assert store.storageEquals(0, 5, 10, 1, null);
	}

	@Test
	public void testHashStoreAddsItemOccupiedByRicherEntries() {
		final var store = new RobinHoodHashStore<Integer>(5, 0.99, 5);
		store.insert(0);
		store.insert(1);
		store.insert(6);

		store.insert(5);

		assert store.storageEquals(0, 5, 1, 6, null);
	}

	@Test
	public void testHashStoreReplacesEqualItem() {
		final var entry1 = new IntegerValue(12);
		final var entry2 = new IntegerValue(12);
		assert entry1 != entry2;

		final var store = new RobinHoodHashStore<IntegerValue>(5, 0.99, 5);
		store.insert(entry1);
		assert store.storageEquals(null, null, entry1, null, null);

		store.insert(entry2);
		// todo: this performs comparison by value
		assert store.storageEquals(null, null, entry2, null, null);
	}

	@Test
	public void testHashStoreFindsItemInExactSlot() {
		final var store = new RobinHoodHashStore<Integer>(5, 0.99, 5);
		store.insert(27);
		store.insert(13);
		store.insert(86);

		final var index = store.find(27);
		assert index == 2;
	}

	@Test
	public void testHashStoreFindsItemWithHashCollision() {
		final var store = new RobinHoodHashStore<Integer>(5, 0.99, 5);
		store.insert(2);
		store.insert(12);
		store.insert(7);

		final var index = store.find(12);
		assert index == 3;
	}

	@Test
	public void testHashStoreFindsItemOccupiedByPoorerEntries() {
		final var store = new RobinHoodHashStore<Integer>(5, 0.99, 5);
		store.insert(0);
		store.insert(5);
		store.insert(10);

		store.insert(1);

		final var index = store.find(1);
		assert index == 3;
	}

	@Test
	public void testHashStoreFindsItemOccupiedByRicherEntries() {
		final var store = new RobinHoodHashStore<Integer>(5, 0.99, 5);
		store.insert(0);
		store.insert(1);
		store.insert(6);

		store.insert(5);

		final var index = store.find(5);
		assert index == 1;
	}

	@Test
	public void testHashStoreFindsNoAbsentItem() {
		final var store = new RobinHoodHashStore<Integer>(5, 0.99, 5);
		store.insert(0);
		store.insert(7);
		store.insert(14);

		final var index = store.find(2);
		assert index < 0;
	}

	@Test
	public void testHashStoreRemovesItemInExactSlot() {
		final var store = new RobinHoodHashStore<Integer>(5, 0.99, 5);
		store.insert(4);
		store.insert(12);
		store.insert(0);

		assert store.remove(4);
		assert store.storageEquals(0, null, 12, null, null);
	}

	@Test
	public void testHashStoreRemovesItemWithHashCollision() {
		final var store = new RobinHoodHashStore<Integer>(5, 0.99, 5);
		store.insert(7);
		store.insert(32);
		store.insert(37);

		assert store.remove(37);
		assert store.storageEquals(null, null, 7, 32, null);
	}

	@Test
	public void testHashStoreRemovesItemOccupiedByPoorerEntries() {
		final var store = new RobinHoodHashStore<Integer>(5, 0.99, 5);
		store.insert(1);
		store.insert(21);
		store.insert(16);

		store.insert(12);

		assert store.remove(12);
		assert store.storageEquals(null, 1, 21, 16, null);
	}

	@Test
	public void testHashStoreRemovesItemOccupiedByRicherEntries() {
		final var store = new RobinHoodHashStore<Integer>(5, 0.99, 5);
		store.insert(0);
		store.insert(1);
		store.insert(6);

		store.insert(5);

		assert store.remove(5);
		assert store.storageEquals(0, 1, 6, null, null);
	}

	@Test
	public void testHashStoreRemovesNoAbsentItem() {
		final var store = new RobinHoodHashStore<Integer>(5, 0.99, 5);
		store.insert(0);
		store.insert(7);
		store.insert(14);

		assert store.remove(12) == false;
		assert store.storageEquals(0, null, 7, null, 14);
	}

	@Test
	public void testHashStoreExpandsStorageAfterReachingItemCountLimit() {
		final var store = new RobinHoodHashStore<Integer>(5, 0.5, 5);
		store.insert(0);
		store.insert(1);
		store.insert(2);
		assert store.storageEquals(0, 1, 2, null, null);

		store.insert(3);
		assert store.storageEquals(0, 1, 2, 3, null,
				null, null, null, null, null);
	}

	@Test
	public void testHashStoreExpandsStorageAfterBucketReachesProbeDistanceLimit() {
		final var store = new RobinHoodHashStore<Integer>(5, 0.75);
		store.insert(0);
		store.insert(5);
		assert store.storageEquals(0, 5, null, null, null);

		store.insert(10);
		assert store.storageEquals(0, 10, null, null, null,
				5, null, null, null, null);
	}
}
