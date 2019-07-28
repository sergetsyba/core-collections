package co.tsyba.core.collections;

import java.util.Iterator;
import static java.lang.System.arraycopy;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

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

	ContigousArrayStore(T[] storage, int itemCount) {
		this.storage = storage;
		this.itemCount = itemCount;
	}

	/**
	 * Returns {@code true} when this store contains an item at the specified
	 * index; returns {@code false} otherwise.
	 */
	public boolean hasIndex(int index) {
		return index >= 0
				&& index < itemCount;
	}

	/**
	 * Returns {@code true} when this store contains items at the specified
	 * index range; returns {@code false} otherwise.
	 */
	public boolean hasIndexRange(IndexRange indexRange) {
		return indexRange.start >= 0
				&& indexRange.end <= itemCount;
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
	 * Returns items at the specified index range in this store.
	 *
	 * @throws IndexRangeNotInRangeException when the specified index range is
	 * out of valid index range of this store
	 */
	public ContigousArrayStore<T> get(IndexRange indexRange) {
		if (!hasIndexRange(indexRange)) {
			throw new IndexRangeNotInRangeException(indexRange,
					new IndexRange(0, itemCount));
		}

		final var returnedItemCount = indexRange.getLength();
		final var items = (T[]) new Object[returnedItemCount];
		arraycopy(storage, indexRange.start, items, 0, returnedItemCount);

		return new ContigousArrayStore<>(items, returnedItemCount);
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
	 * Appends items of the specified store to the end of this store.
	 */
	public void append(ContigousArrayStore<T> store) {
		prepareCapacity(store.itemCount);

		arraycopy(store.storage, 0, storage, itemCount, store.itemCount);
		itemCount += store.itemCount;
	}

	/**
	 * Appends the specified items to the end of this store. Ignores any
	 * {@code null} values among the items.
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
		moveItems(index, 1);

		storage[index] = item;
		itemCount += 1;
	}

	/**
	 * Inserts items of the specified store into this store at the specified
	 * index.
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
		moveItems(index, store.itemCount);

		arraycopy(store.storage, 0, storage, index, store.itemCount);
		itemCount += store.itemCount;
	}

	/**
	 * Removes item at the specified index from this store.
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
		moveItems(index + 1, -1);
		itemCount -= 1;

		// clear the item after the shifted ones to make it eligible
		// for garbage collection
		storage[itemCount] = null;
	}

	/**
	 * Removes items at the specified index range from this store.
	 *
	 * @throws IndexRangeNotInRangeException when the specified index range is
	 * out of valid index range of this store
	 */
	public void remove(IndexRange indexRange) {
		if (!hasIndexRange(indexRange)) {
			throw new IndexRangeNotInRangeException(indexRange,
					new IndexRange(0, itemCount));
		}
		if (indexRange.isEmpty()) {
			return;
		}

		// shift items after the removed index range to the left
		final var removedItemCount = indexRange.getLength();
		moveItems(indexRange.end, -removedItemCount);
		itemCount -= removedItemCount;

		// clear items after the shifted ones to make them eligible for
		// garbage collection
		final var endIndex = itemCount + removedItemCount;
		for (var index = itemCount; index < endIndex; index += 1) {
			storage[index] = null;
		}
	}

	/**
	 * Returns items of this store in reverse order.
	 *
	 * @return
	 */
	public ContigousArrayStore<T> reverse() {
		final var reverseItems = (T[]) new Object[itemCount];
		for (var index = 0; index < itemCount; ++index) {
			final var reverseIndex = itemCount - index - 1;
			reverseItems[reverseIndex] = storage[index];
		}

		return new ContigousArrayStore<>(reverseItems, itemCount);
	}

	/**
	 * Returns items of this store, ordered according to the specified
	 * {@link Comparator}.
	 */
	public ContigousArrayStore<T> sort(Comparator<T> comparator) {
		final var sortedItems = (T[]) new Object[itemCount];
		arraycopy(storage, 0, sortedItems, 0, itemCount);

		Arrays.sort(sortedItems, 0, itemCount, comparator);
		return new ContigousArrayStore<>(sortedItems, itemCount);
	}

	/**
	 * Returns items of this store in random order, according to the specified
	 * {@link Random}.
	 *
	 * <pre>
	 * Implements in-place version of Fisher-Yates shuffle algorithm.
	 *
	 * Sources:
	 * 1. R. Durstenfeld. "Algorithm 235: Random permutation".
	 *    Communications of the ACM, vol. 7, July 1964, p. 420.
	 * 2. D. Knuth. "The Art of Computer Programming" vol. 2.
	 *    Addison–Wesley, 1969, pp. 139–140, algorithm P.
	 * </pre>
	 *
	 * @param random
	 * @return
	 */
	public ContigousArrayStore<T> shuffle(Random random) {
		final var shuffledItems = (T[]) new Object[itemCount];
		arraycopy(storage, 0, shuffledItems, 0, itemCount);

		// it's more convenient to iterate items backwards for simpler
		// random index generation
		for (var index = itemCount - 1; index >= 0; --index) {
			final var randomIndex = random.nextInt(index + 1);

			// swap items at iterated and randomly generated indices
			final var item = shuffledItems[index];
			shuffledItems[index] = shuffledItems[randomIndex];
			shuffledItems[randomIndex] = item;
		}

		return new ContigousArrayStore<>(shuffledItems, itemCount);
	}

	private void prepareCapacity(int extraItemCount) {
		final var newItemCount = itemCount + extraItemCount;
		if (newItemCount <= storage.length) {
			return;
		}

		final var newCapacity = 2 * newItemCount + 1;
		final var newStorage = (T[]) new Object[newCapacity];
		arraycopy(storage, 0, newStorage, 0, itemCount);

		storage = newStorage;
	}

	public void removeExcessCapacity() {
		if (itemCount == storage.length) {
			return;
		}

		final var newCapacity = storage.length;
		final var newStorage = (T[]) new Object[newCapacity];
		arraycopy(storage, 0, newStorage, 0, itemCount);

		storage = newStorage;
	}

	private void moveItems(int startIndex, int positions) {
		final var newItemCount = itemCount + positions;

		if (newItemCount > storage.length) {
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
		return iterator(0);
	}

	public Iterator<T> iterator(int startIndex) {
		return new Iterator<T>() {
			private int index = startIndex;

			@Override
			public boolean hasNext() {
				return index < itemCount;
			}

			@Override
			public T next() {
				final var nextItem = storage[index];
				index += 1;

				return nextItem;
			}
		};
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
