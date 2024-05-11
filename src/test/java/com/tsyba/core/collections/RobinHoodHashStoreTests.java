package com.tsyba.core.collections;

import com.tsyba.core.collections.data.IntegerValueHash42;
import com.tsyba.core.collections.data.IntegerValue;
import org.junit.Test;

/*
 * Created by Serge Tsyba <serge.tsyba@tsyba.com> on Dec 21, 2018.
 */
public class RobinHoodHashStoreTests {
	@Test
	public void createsEmptyHashStore() {
		final var store = new RobinHoodHashStore<Integer>(0);
		assert store.storageEquals();
	}

	@Test
	public void createsHashStore() {
		final var store = new RobinHoodHashStore<Integer>(5, 5);
		assert store.storageEquals(null, null, null, null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void doesNotCreateHashStoreWithNegativeCapacity() {
		new RobinHoodHashStore<Integer>(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void doesNotCreateHashStoreWithNegativeLoadFactor() {
		new RobinHoodHashStore<Integer>(5, -0.1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void doesNotCreateHashStoreWithLoadFactorZero() {
		new RobinHoodHashStore<Integer>(5, 0.0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void doesNotCreateHashStoreWithLoadFactorOne() {
		new RobinHoodHashStore<Integer>(5, 1.0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void doesNotCreateHashStoreWithLoadFactorOverOne() {
		new RobinHoodHashStore<Integer>(5, 1.1);
	}

	@Test
	public void addsItemIntoEmptySlot() {
		final var store = new RobinHoodHashStore<Integer>(5, 5);
		store.insert(17);
		store.insert(0);

		assert store.storageEquals(0, null, 17, null, null);
	}

	@Test
	public void addsItemDisplacedBySameDegreeEntries() {
		final var store = new RobinHoodHashStore<Integer>(5, 5);
		store.insert(0);
		store.insert(10);
		store.insert(5);

		assert store.storageEquals(0, 10, 5, null, null);
	}

	@Test
	public void addsItemDisplacedByPoorerEntries() {
		final var store = new RobinHoodHashStore<Integer>(5, 5);
		store.insert(0);
		store.insert(5);
		store.insert(10);

		store.insert(1);

		assert store.storageEquals(0, 5, 10, 1, null);
	}

	@Test
	public void addsItemDisplacedByRicherEntries() {
		final var store = new RobinHoodHashStore<Integer>(5, 5);
		store.insert(0);
		store.insert(1);
		store.insert(6);

		store.insert(5);

		assert store.storageEquals(0, 5, 1, 6, null);
	}

	@Test
	public void addsItemDisplacedBySameHashEntry() {
		final var entry1 = new IntegerValueHash42(0);
		final var entry2 = new IntegerValueHash42(17);

		final var store = new RobinHoodHashStore<IntegerValueHash42>(5, 5);
		store.insert(entry1);
		store.insert(entry2);

		assert store.storageIs(null, null, entry1, entry2, null);
	}

	@Test
	public void addsItemWithNegativeHashCode() {
		final var store = new RobinHoodHashStore<Integer>(5, 5);
		store.insert(-3);

		assert store.storageIs(null, null, -3, null, null);
	}

	@Test
	public void addsItemDisplacedByItemWithNegativeHashCode() {
		final var store = new RobinHoodHashStore<Integer>(5, 5);
		store.insert(-3);
		store.insert(7);

		assert store.storageIs(null, null, -3, 7, null);
	}

	@Test
	public void storeReplacesEqualItem() {
		final var entry1 = new IntegerValue(12);
		final var entry2 = new IntegerValue(12);
		assert entry1 != entry2;

		final var store = new RobinHoodHashStore<IntegerValue>(5, 5);
		store.insert(entry1);
		assert store.storageIs(null, null, entry1, null, null);

		store.insert(entry2);
		assert store.storageIs(null, null, entry2, null, null);
	}

	@Test
	public void storeFindsItemInExactSlot() {
		final var store = new RobinHoodHashStore<Integer>(5, 5);
		store.insert(27);
		store.insert(13);
		store.insert(86);

		final var index = store.find(27);
		assert index == 2;
	}

	@Test
	public void findsItemDisplacedBySameDegreeEntries() {
		final var store = new RobinHoodHashStore<Integer>(5, 5);
		store.insert(2);
		store.insert(12);
		store.insert(7);

		final var index = store.find(12);
		assert index == 3;
	}

	@Test
	public void findsItemDisplacedByPoorerEntries() {
		final var store = new RobinHoodHashStore<Integer>(5, 5);
		store.insert(0);
		store.insert(5);
		store.insert(10);

		store.insert(1);

		final var index = store.find(1);
		assert index == 3;
	}

	@Test
	public void findsItemDisplacedByRicherEntries() {
		final var store = new RobinHoodHashStore<Integer>(5, 5);
		store.insert(0);
		store.insert(1);
		store.insert(6);

		store.insert(5);

		final var index = store.find(5);
		assert index == 1;
	}

	@Test
	public void findsItemWithNegativeHashCode() {
		final var store = new RobinHoodHashStore<Integer>(5, 5);
		store.insert(7);
		store.insert(2);
		store.insert(-3);

		final var index = store.find(-3);
		assert index == 4;
	}

	@Test
	public void findsItemDisplacedByItemWithNegativeHashCode() {
		final var store = new RobinHoodHashStore<Integer>(5, 5);
		store.insert(-8);
		store.insert(-13);
		store.insert(7);

		final var index = store.find(7);
		assert index == 4;
	}

	@Test
	public void findsNoAbsentItem() {
		final var store = new RobinHoodHashStore<Integer>(5, 5);
		store.insert(0);
		store.insert(7);
		store.insert(14);

		final var index = store.find(2);
		assert index < 0;
	}

	@Test
	public void removesItemInExactSlot() {
		final var store = new RobinHoodHashStore<Integer>(5, 5);
		store.insert(4);
		store.insert(12);
		store.insert(0);

		assert store.delete(4);
		assert store.storageEquals(0, null, 12, null, null);
	}

	@Test
	public void removesItemDisplacedBySameDegreeEntries() {
		final var store = new RobinHoodHashStore<Integer>(5, 5);
		store.insert(7);
		store.insert(32);
		store.insert(37);

		assert store.delete(37);
		assert store.storageEquals(null, null, 7, 32, null);
	}

	@Test
	public void removesItemDisplacedByPoorerEntries() {
		final var store = new RobinHoodHashStore<Integer>(5, 5);
		store.insert(1);
		store.insert(21);
		store.insert(16);

		store.insert(12);

		assert store.delete(12);
		assert store.storageEquals(null, 1, 21, 16, null);
	}

	@Test
	public void removesItemDisplacedByRicherEntries() {
		final var store = new RobinHoodHashStore<Integer>(5, 5);
		store.insert(0);
		store.insert(1);
		store.insert(6);

		store.insert(5);

		assert store.delete(5);
		assert store.storageEquals(0, 1, 6, null, null);
	}

	@Test
	public void removesItemWithNegativeHashCode() {
		final var store = new RobinHoodHashStore<Integer>(5, 5);
		store.insert(6);
		store.insert(-3);
		store.insert(2);

		assert store.delete(-3);
		assert store.storageEquals(null, 6, 2, null, null);
	}

	@Test
	public void removesItemDisplacedByItemWithNegativeHashCode() {
		final var store = new RobinHoodHashStore<Integer>(5, 5);
		store.insert(-13);
		store.insert(-3);
		store.insert(2);

		assert store.delete(2);
		assert store.storageEquals(null, null, -13, -3, null);
	}

	@Test
	public void removesNoAbsentItem() {
		final var store = new RobinHoodHashStore<Integer>(5, 5);
		store.insert(0);
		store.insert(7);
		store.insert(14);

		assert store.delete(12) == false;
		assert store.storageEquals(0, null, 7, null, 14);
	}

	@Test
	public void expandsStorageAfterBucketReachesProbeDistanceLimit() {
		final var store = new RobinHoodHashStore<Integer>(5, 0.75);
		store.insert(0);
		store.insert(5);
		assert store.storageEquals(0, 5, null, null, null);

		store.insert(10);
		assert store.storageEquals(0, 10, null, null, null,
				5, null, null, null, null);
	}
}
