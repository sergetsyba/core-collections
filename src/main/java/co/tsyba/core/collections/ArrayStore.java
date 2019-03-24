package co.tsyba.core.collections;

import java.util.Arrays;
import static java.lang.String.format;
import java.util.Iterator;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jan 28, 2019.
 */
class ArrayStore<Item> implements Iterable<Item> {
	Item[] storage;
	int itemCount;

	/**
	 * Creates a new array store with the specified capacity.
	 */
	public ArrayStore(int capacity) {
		assert capacity >= 0 :
				format("Negative array store capacity: %d.", capacity);

		this.storage = (Item[]) new Object[capacity];
		this.itemCount = 0;
	}

	/**
	 * Appends the specified item to the end of this array store.
	 */
	public void append(Item item) {
		ensureEnoughCapacity(1);

		storage[itemCount] = item;
		itemCount += 1;
	}

	/**
	 * Appends the specified items to the end of this array store.
	 */
	public void append(Item[] items) {
		ensureEnoughCapacity(items.length);

		System.arraycopy(items, 0, storage, itemCount, items.length);
		itemCount += items.length;
	}

	private void ensureEnoughCapacity(int extraItemCount) {
		if (itemCount + extraItemCount > storage.length) {
			final var expandedCapacity = estimateCapacity(extraItemCount);
			final var expandedStorage = (Item[]) new Object[expandedCapacity];

			System.arraycopy(storage, 0, expandedStorage, 0, itemCount);
			storage = expandedStorage;
		}
	}

	private int estimateCapacity(int extraItemCount) {
		// find nearest possible power-of-2 capacity, which will fit current
		// items and the specified extra item count
		final var factor = Math.log(itemCount + extraItemCount) / Math.log(2.0);
		final double roundedFactor = Math.ceil(factor);

		// note: same as 2^roundedFactor
		return 2 << ((int) roundedFactor - 1);
	}

	private void shiftItems(int index, int positions) {
		System.arraycopy(storage, index, storage, index + positions, itemCount - index);
	}

	/**
	 * Inserts the specified item at the specified index into this array store.
	 */
	public void insert(int index, Item item) {
		assert index >= 0 && index < itemCount :
				format("Index %d is out of valid range [0, %d).", index, itemCount);

		// todo: when capacity is extended, this will copy items, starting at insertion
		// index, twice; conider an optimization
		ensureEnoughCapacity(1);
		shiftItems(index, 1);

		storage[index] = item;
		itemCount += 1;
	}

	/**
	 * Inserts the specified items at the specified index into this array store.
	 */
	public void insert(int index, Item[] items) {
		assert index >= 0 && index < itemCount :
				format("Index %d is out of valid range [0, %d).", index, itemCount);

		ensureEnoughCapacity(items.length);
		shiftItems(index, items.length);

		System.arraycopy(items, 0, storage, index, items.length);
		itemCount += items.length;
	}

	/**
	 * Returns index of the first appearance of the specified item in this list within the
	 * specified index range. Returns -1 when the specified item does not appear in this
	 * list within the specified index range.
	 */
	public int find(int startIndex, int endIndex, Item item) {
		assert startIndex <= endIndex :
				format("Invalid index range: [%d, %d),", startIndex, endIndex);
		assert startIndex >= 0 && endIndex <= itemCount :
				format("Index range [%d, %d) is out of valid range [0, %d)", startIndex, endIndex, itemCount);

		for (int index = startIndex; index < endIndex; index += 1) {
			if (storage[index].equals(item)) {
				return index;
			}
		}

		return -1;
	}

	/**
	 * Removes item at the specified index from this list. Returns the removed item.
	 */
	public Item remove(int index) {
		assert index >= 0 && index < itemCount :
				format("Index %d is out of valid range [0, %d).", index, itemCount);

		final var removedItem = storage[index];
		shiftItems(index + 1, -1);

		// set last item  to null to make it eligible for garbage collection
		storage[itemCount - 1] = null;

		itemCount -= 1;
		return removedItem;
	}

	/**
	 * Removes items within the specified index range from the list. Returns the removed
	 * items.
	 */
	public Object[] remove(int startIndex, int endIndex) {
		assert startIndex <= endIndex :
				format("Invalid index range: [%d, %d),", startIndex, endIndex);
		assert startIndex >= 0 && endIndex <= itemCount :
				format("Index range [%d, %d) is out of valid range [0, %d)", startIndex, endIndex, itemCount);

		final var removedItemCount = endIndex - startIndex;
		final var removedItems = new Object[removedItemCount];
		System.arraycopy(storage, startIndex, removedItems, 0, removedItemCount);

		shiftItems(endIndex, -removedItemCount);

		// set last items to null to make them eligible for garbage collection
		for (var index = itemCount - removedItemCount; index < itemCount; index += 1) {
			storage[index] = null;
		}

		itemCount -= removedItemCount;
		return removedItems;
	}

	@Override
	public Iterator<Item> iterator() {
		return new Iterator<Item>() {
			int index = 0;

			@Override
			public boolean hasNext() {
				return index < itemCount;
			}

			@Override
			public Item next() {
				final var item = storage[index];
				index += 1;

				return item;
			}
		};
	}

	boolean itemsEqual(Item[] items) {
		if (itemCount != items.length) {
			return false;
		}

		for (var index = 0; index < itemCount; index += 1) {
			if (!storage[index].equals(items[index])) {
				return false;
			}
		}

		for (var index = itemCount; index < storage.length; index += 1) {
			if (storage[index] != null) {
				return false;
			}
		}

		return true;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(storage);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		else if (object instanceof ArrayStore) {
			final var store = (ArrayStore) object;

			return itemCount == store.itemCount
					&& Arrays.equals(storage, 0, itemCount, store.storage, 0, itemCount);
		}
		else {
			return false;
		}
	}

	@Override
	public String toString() {
		final var builder = new StringBuilder()
				.append("{");

		final var iterator = iterator();
		if (iterator.hasNext()) {
			final var item = iterator.next();
			builder.append(item);
		}

		while (iterator.hasNext()) {
			final var item = iterator.next();
			builder.append(", ")
					.append(item);
		}

		return builder.append("}")
				.toString();
	}
}
