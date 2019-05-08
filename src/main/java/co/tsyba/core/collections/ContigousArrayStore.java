package co.tsyba.core.collections;

import java.util.Iterator;
import static java.lang.String.format;
import static java.lang.System.arraycopy;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jan 28, 2019.
 */
class ContigousArrayStore<Item> implements Iterable<Item> {
	Item[] storage;
	int itemCount;

	/**
	 * Creates a new {@link ContigousArrayStore} with the specified capacity.
	 *
	 * @throws NegativeCapacityException when the specified store capacity is
	 * negative
	 */
	public ContigousArrayStore(int capacity) {
		if (capacity < 0) {
			throw new NegativeCapacityException(capacity);
		}

		this.storage = (Item[]) new Object[capacity];
		this.itemCount = 0;
	}

	/**
	 * Returns the number of items in this store.
	 */
	public int getCount() {
		return itemCount;
	}

	/**
	 * Returns item at the specified index in this store.
	 *
	 * @throws IndexNotInRangeException when the specified index is out of valid
	 * index range of this store
	 */
	public Item get(int index) {
		if (!hasIndex(index)) {
			final var indexRange = new IndexRange(0, itemCount);
			throw new IndexNotInRangeException(index, indexRange);
		}

		return storage[index];
	}

	/**
	 * Returns a {@link ContigousArrayStore} with items at the specified index
	 * range in this store.
	 *
	 * @throws InvalidIndexRangeException when the specified index range start
	 * index is after the specified end index
	 * @throws IndexRangeNotInRangeException when the specified index range is
	 * out of valid index range of this store
	 */
	public ContigousArrayStore<Item> get(int startIndex, int endIndex) {
		if (!hasIndexSubRange(startIndex, endIndex)) {
			final var indexSubRange = new IndexRange(startIndex, endIndex);
			final var indexRange = new IndexRange(0, itemCount);
			throw new IndexRangeNotInRangeException(indexSubRange, indexRange);
		}

		final var returnedItemCount = endIndex - startIndex;
		final var items = new ContigousArrayStore<Item>(returnedItemCount);
		arraycopy(storage, startIndex, items.storage, 0, returnedItemCount);

		return items;
	}

	/**
	 * Appends the specified item to the end of this store. Does nothing when
	 * the specified item is {@code null}.
	 */
	public void append(Item item) {
		if (item == null) {
			return;
		}

		prepareCapacity(1);

		storage[itemCount] = item;
		itemCount += 1;
	}

	/**
	 * Appends items from the specified {@link ContigousArrayStore} to the end
	 * of this store.
	 */
	public void append(ContigousArrayStore<Item> store) {
		prepareCapacity(store.itemCount);

		arraycopy(store.storage, 0, storage, itemCount, store.itemCount);
		itemCount += store.itemCount;
	}

	/**
	 * Appends the specified items to the end of this store. Ignores any of the
	 * specified items which are {@code null}.
	 */
	public void append(Item... items) {
		prepareCapacity(items.length);

		for (var item : items) {
			if (item != null) {
				storage[itemCount] = item;
				itemCount += 1;
			}
		}
	}

	/**
	 * Inserts the specified item into this store at the specified index. Does
	 * nothing when the specified item is {@code null}.
	 *
	 * @throws IndexNotInRangeException when the specified index is out of valid
	 * index range of this store
	 */
	public void insert(int index, Item item) {
		if (!hasIndex(index)) {
			final var indexRange = new IndexRange(0, itemCount);
			throw new IndexNotInRangeException(index, indexRange);
		}
		if (item == null) {
			return;
		}

		// shift items at the insertion index one position to the right
		// to make room for the inserted item
		moveStoredItems(index, 1);

		storage[index] = item;
		itemCount += 1;
	}

	/**
	 * Inserts items from the specified {@link ContigousArrayStore} into this
	 * store at the specified index.
	 *
	 * @throws IndexNotInRangeException when the specified index is out of valid
	 * index range of this store
	 */
	public void insert(int index, ContigousArrayStore<Item> store) {
		if (!hasIndex(index)) {
			final var indexRange = new IndexRange(0, itemCount);
			throw new IndexNotInRangeException(index, indexRange);
		}

		// shift items at the insertion index to the right to make room
		// for the inserted items
		moveStoredItems(index, store.itemCount);

		arraycopy(store.storage, 0, storage, index, store.itemCount);
		itemCount += store.itemCount;
	}

	/**
	 * Removes item at the specified index from this store. Returns the removed
	 * item.
	 *
	 * @throws IndexNotInRangeException when the specified index is out of valid
	 * index range of this store
	 */
	public void remove(int index) {
		if (!hasIndex(index)) {
			final var indexRange = new IndexRange(0, itemCount);
			throw new IndexNotInRangeException(index, indexRange);
		}

		// shift items after the removed index one position to the left
		moveStoredItems(index + 1, -1);
		itemCount -= 1;

		// clear the item after the shifted ones to make it eligible
		// for garbage collection
		storage[itemCount] = null;
	}

	/**
	 * Removes items at the specified index range from this store. Returns a
	 * {@link ContigousArrayStore} with the removed items.
	 *
	 * @throws InvalidIndexRangeException when the specified index range start
	 * index is after the specified end index
	 * @throws IndexRangeNotInRangeException when the specified index range is
	 * out of valid index range of this store
	 */
	public void remove(int startIndex, int endIndex) {
		if (!hasIndexSubRange(startIndex, endIndex)) {
			final var indexSubRange = new IndexRange(startIndex, endIndex);
			final var indexRange = new IndexRange(0, itemCount);
			throw new IndexRangeNotInRangeException(indexSubRange, indexRange);
		}

		// shift items after the removed index range to the left
		final var removedItemCount = endIndex - startIndex;
		moveStoredItems(endIndex, -removedItemCount);
		itemCount -= removedItemCount;

		// clear items after the shifted ones to make them eligible for
		// garbage collection
		var index = startIndex;
		while (storage[index] != null) {
			storage[index] = null;
			index += 1;
		}
	}

	/**
	 * Returns {@code true} when the specified index is a valid index of this
	 * store; returns {@code false} otherwise.
	 */
	private boolean hasIndex(int index) {
		return 0 <= index && index < itemCount;
	}

	/**
	 * Returns {@code true} when the specified index range is a valid index sub
	 * range of this store; returns {@code false} otherwise.
	 */
	private boolean hasIndexSubRange(int startIndex, int endIndex) {
		if (startIndex > endIndex) {
			final var message = format("Invalid index range: [%d, %d).", startIndex, endIndex);
			throw new InvalidIndexRangeException(message);
		}

		return 0 <= startIndex && endIndex <= itemCount;
	}

	private void prepareCapacity(int extraItemCount) {
		final var newItemCount = itemCount + extraItemCount;

		if (newItemCount >= storage.length) {
			final var newCapacity = 2 * newItemCount + 1;
			final var newStorage = (Item[]) new Object[newCapacity];

			arraycopy(storage, 0, newStorage, 0, itemCount);
			storage = newStorage;
		}
	}

	private void moveStoredItems(int startIndex, int positions) {
		final var newItemCount = itemCount + positions;

		if (newItemCount >= storage.length) {
			final var newCapacity = 2 * newItemCount + 1;
			final var newStorage = (Item[]) new Object[newCapacity];

			arraycopy(storage, 0, newStorage, 0, startIndex);
			arraycopy(storage, startIndex, newStorage,
					startIndex + positions, itemCount - startIndex);

			storage = newStorage;
		} else {
			arraycopy(storage, startIndex, storage,
					startIndex + positions, itemCount - startIndex);
		}
	}

	@Override
	public Iterator<Item> iterator() {
		return new Iterator<Item>() {
			private int index = 0;

			@Override
			public boolean hasNext() {
				return index < itemCount;
			}

			@Override
			public Item next() {
				final var nextItem = storage[index];
				index += 1;

				return nextItem;
			}
		};
	}

	boolean itemsEqual(Item... items) {
		if (storage.length < items.length) {
			return false;
		}

		var index = 0;
		for (; index < items.length; index += 1) {
			if (!storage[index].equals(items[index])) {
				return false;
			}
		}

		for (; index < storage.length; index += 1) {
			if (storage[index] != null) {
				return false;
			}
		}

		return true;
	}
}
