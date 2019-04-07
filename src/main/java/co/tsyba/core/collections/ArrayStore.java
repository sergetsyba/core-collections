package co.tsyba.core.collections;

import java.util.Arrays;
import static java.lang.String.format;
import java.util.Iterator;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jan 28, 2019.
 */
class ArrayStore<Item> implements Iterable<Item> {
	Object[] storage;
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
		if (itemCount + 1 > storage.length) {
			// when appended item does not fit into storage, expand storage capacity
			final var expandedCapacity = (itemCount + 1) * 2;
			final var expandedStorage = (Item[]) new Object[expandedCapacity];

			// replace storage with expanded one
			System.arraycopy(storage, 0, expandedStorage, 0, itemCount);
			storage = expandedStorage;
		}

		storage[itemCount] = item;
		itemCount += 1;
	}

	/**
	 * Appends the specified items to the end of this array store.
	 */
	public void append(Item[] items) {
		if (itemCount + items.length > storage.length) {
			// when appended items do not fit into storage, expand storage capacity
			final var expandedCapacity = (itemCount + items.length) * 2;
			final var expandedStorage = (Item[]) new Object[expandedCapacity];

			// replace storage with expanded one
			System.arraycopy(storage, 0, expandedStorage, 0, itemCount);
			storage = expandedStorage;
		}

		System.arraycopy(items, 0, storage, itemCount, items.length);
		itemCount += items.length;
	}

	/**
	 * Inserts the specified item at the specified index into this array store.
	 */
	public void insert(int index, Item item) {
		assert index >= 0 && index < itemCount :
				format("Index %d is out of valid range [0, %d).", index, itemCount);

		if (itemCount + 1 > storage.length) {
			// when inserted item does not fit into storage, expand storage capacity
			final var expandedCapacity = (itemCount + 1) * 2;
			final var expandedStorage = (Item[]) new Object[expandedCapacity];

			// replace storage with expanded storage, creating slot for inserted item
			System.arraycopy(storage, 0, expandedStorage, 0, index);
			System.arraycopy(storage, index, expandedStorage, index + 1, itemCount - index);
			storage = expandedStorage;
		}
		else {
			// shift items after the insertion index one position to the right,
			// creating slot for insreted item
			System.arraycopy(storage, index, storage, index + 1, itemCount - index);
		}

		storage[index] = item;
		itemCount += 1;
	}

	/**
	 * Inserts the specified items at the specified index into this array store.
	 */
	public void insert(int index, Item[] items) {
		assert index >= 0 && index < itemCount :
				format("Index %d is out of valid range [0, %d).", index, itemCount);

		if (itemCount + items.length > storage.length) {
			// when inserted items do not fit into storage, expand storage capacity
			final var expandedCapacity = (itemCount + items.length) * 2;
			final var expandedStorage = (Item[]) new Object[expandedCapacity];

			// replace storage with expanded storage, creating slots for inserted items
			System.arraycopy(storage, 0, expandedStorage, 0, index);
			System.arraycopy(storage, index, expandedStorage, index + items.length, itemCount - index);
			storage = expandedStorage;
		}
		else {
			// shift items after the insertion index to the right, creating slots
			// for inserted items
			System.arraycopy(storage, index, storage, index + items.length, itemCount - index);
		}

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

		for (var index = startIndex; index < endIndex; index += 1) {
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

		if (itemCount - 1 < storage.length / 4 + 1) {
			// when the item count drops below 1/4 of storage capacity, reduce storage
			// capacity
			final var reducedCapacity = (itemCount - 1) * 2;
			final var reducedStorage = (Item[]) new Object[reducedCapacity];

			// replace storage with reduced storage, eliminating the removed item
			System.arraycopy(storage, 0, reducedStorage, 0, index);
			System.arraycopy(storage, index + 1, reducedStorage, index, itemCount - (index + 1));
			storage = reducedStorage;
		}
		else {
			// shift items after the removal index one position to the left,
			// eliminating the removed item
			System.arraycopy(storage, index + 1, storage, index, itemCount - (index + 1));
			// set last item to null to make it eligible for garbage collection
			storage[itemCount - 1] = null;
		}

		itemCount -= 1;
		return (Item) removedItem;
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

		if (itemCount - removedItemCount < storage.length / 4 + 1) {
			// when the item count drops below 1/4 of storage capacity, reduce storage
			// capacity
			final var reducedCapacity = (itemCount - removedItemCount) * 2;
			final var reducedStorage = (Item[]) new Object[reducedCapacity];

			// replace storage with reduced storage, eliminating the removed items
			System.arraycopy(storage, 0, reducedStorage, 0, startIndex);
			System.arraycopy(storage, endIndex, reducedStorage, startIndex, itemCount - endIndex);
			storage = reducedStorage;
		}
		else {
			// shift items after the removal index to the left, eliminating
			// the removed items
			System.arraycopy(storage, endIndex, storage, startIndex, itemCount - endIndex);

			// set last items to null to make them eligible for garbage collection
			for (var index = itemCount - removedItemCount; index < itemCount; index += 1) {
				storage[index] = null;
			}
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

				return (Item) item;
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
