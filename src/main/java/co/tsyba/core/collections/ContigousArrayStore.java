package co.tsyba.core.collections;

import java.util.Iterator;
import static java.lang.System.arraycopy;
import java.util.Arrays;
import java.util.Optional;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jan 28, 2019.
 */
class ContigousArrayStore<T> implements Iterable<T> {
	T[] storage;
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

		this.storage = (T[]) new Object[capacity];
		this.itemCount = 0;
	}

	public ContigousArrayStore(ContigousArrayStore<T> items, IndexRange indexRange) {
		this.storage = (T[]) new Object[items.itemCount];
	}

	/**
	 * Returns {@code true} when this store has no items; returns {@code false}
	 * otherwise.
	 */
	public boolean isEmpty() {
		return itemCount == 0;
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
	public T get(int index) {
		if (!hasIndex(index)) {
			throw new IndexNotInRangeException(index,
					new IndexRange(0, itemCount));
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
	public ContigousArrayStore<T> get(IndexRange indexRange) {
		if (!hasIndexRange(indexRange)) {
			throw new IndexRangeNotInRangeException(indexRange,
					new IndexRange(0, itemCount));
		}

		final var returnedItemCount = indexRange.getLength();
		final var items = new ContigousArrayStore<T>(returnedItemCount);
		arraycopy(storage, indexRange.start, items.storage, 0,
				returnedItemCount);

		return items;
	}

	/**
	 * Replaces item at the specified index with the specified one. Does nothing
	 * when the specified item is {@code null}.
	 *
	 * @throws IndexNotInRangeException when the specified index is out of valid
	 * index range of this store
	 */
	public void set(int index, T item) {
		if (!hasIndex(index)) {
			throw new IndexNotInRangeException(index,
					new IndexRange(0, itemCount));
		}
		if (item == null) {
			return;
		}

		storage[index] = item;
	}

	/**
	 * Appends the specified item to the end of this store. Does nothing when
	 * the specified item is {@code null}.
	 */
	public void append(T item) {
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
	public void append(ContigousArrayStore<T> store) {
		prepareCapacity(store.itemCount);

		arraycopy(store.storage, 0, storage, itemCount, store.itemCount);
		itemCount += store.itemCount;
	}

	/**
	 * Appends the specified items to the end of this store. Ignores any of the
	 * specified items which are {@code null}.
	 */
	public void append(T... items) {
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
	public void insert(int index, T item) {
		if (!hasIndex(index)) {
			throw new IndexNotInRangeException(index,
					new IndexRange(0, itemCount));
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
	public void insert(int index, ContigousArrayStore<T> store) {
		if (!hasIndex(index)) {
			throw new IndexNotInRangeException(index,
					new IndexRange(0, itemCount));
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
			throw new IndexNotInRangeException(index,
					new IndexRange(0, itemCount));
		}

		// shift items after the removed index one position to the left
		moveStoredItems(index + 1, -1);
		itemCount -= 1;

		// clear the item after the shifted ones to make it eligible
		// for garbage collection
		storage[itemCount] = null;
	}

	/**
	 * Removes the first item from this store. Returns the removed item. Does
	 * nothing when this store is empty;
	 */
	public Optional<T> removeFirst() {
		if (isEmpty()) {
			return Optional.empty();
		}
		else {
			final var removedItem = storage[0];
			remove(0);

			return Optional.of(removedItem);
		}
	}

	/**
	 * Removes the first item from this store. Returns the removed item. Does
	 * nothing when this store is empty;
	 */
	public Optional<T> removeLast() {
		if (isEmpty()) {
			return Optional.empty();
		}
		else {
			final var removedItem = storage[itemCount - 1];
			remove(itemCount - 1);

			return Optional.of(removedItem);
		}
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
	public void remove(IndexRange indexRange) {
		if (!hasIndexRange(indexRange)) {
			throw new IndexRangeNotInRangeException(indexRange,
					new IndexRange(0, itemCount));
		}

		// shift items after the removed index range to the left
		final var removedItemCount = indexRange.getLength();
		moveStoredItems(indexRange.end, -removedItemCount);
		itemCount -= removedItemCount;

		// clear items after the shifted ones to make them eligible for
		// garbage collection
		var index = indexRange.start;
		while (storage[index] != null) {
			storage[index] = null;
			index += 1;
		}
	}

	private boolean hasIndex(int index) {
		return index >= 0
				&& index < itemCount;
	}

	private boolean hasIndexRange(IndexRange indexRange) {
		return indexRange.start >= 0
				&& indexRange.end <= itemCount;
	}

	private void prepareCapacity(int extraItemCount) {
		final var newItemCount = itemCount + extraItemCount;

		if (newItemCount >= storage.length) {
			final var newCapacity = 2 * newItemCount + 1;
			final var newStorage = (T[]) new Object[newCapacity];

			arraycopy(storage, 0, newStorage, 0, itemCount);
			storage = newStorage;
		}
	}

	private void moveStoredItems(int startIndex, int positions) {
		final var newItemCount = itemCount + positions;

		if (newItemCount >= storage.length) {
			final var newCapacity = 2 * newItemCount + 1;
			final var newStorage = (T[]) new Object[newCapacity];

			arraycopy(storage, 0, newStorage, 0, startIndex);
			arraycopy(storage, startIndex, newStorage,
					startIndex + positions, itemCount - startIndex);

			storage = newStorage;
		}
		else {
			arraycopy(storage, startIndex, storage,
					startIndex + positions, itemCount - startIndex);
		}
	}

	@Override
	public Iterator<T> iterator() {
		final var indexRange = new IndexRange(0, itemCount);
		return iterator(indexRange, 1);
	}

	public Iterator<T> reverseIterator() {
		final var indexRange = new IndexRange(0, itemCount);
		return iterator(indexRange, -1);
	}

	Iterator<T> iterator(IndexRange indexRange, int stride) {
		return new Iterator<T>() {
			private int index = stride > 0
					? indexRange.start
					: indexRange.end;

			@Override
			public boolean hasNext() {
				return index < itemCount;
			}

			@Override
			public T next() {
				final var nextItem = storage[index];
				index += stride;

				return nextItem;
			}
		};
	}

	boolean itemsEqual(T[] items) {
		if (storage.length < items.length) {
			return false;
		}

		return Arrays.equals(items, 0, items.length,
				items, 0, items.length);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(storage);
	}

	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof ContigousArrayStore)) {
			return false;
		}

		final var store = (ContigousArrayStore<T>) object;
		return Arrays.equals(storage, 0, itemCount,
				store.storage, 0, store.itemCount);
	}
}
