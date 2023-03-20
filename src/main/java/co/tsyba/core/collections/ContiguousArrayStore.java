package co.tsyba.core.collections;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

import static java.lang.System.arraycopy;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jan 28, 2019.
 */
class ContiguousArrayStore<T> implements Iterable<T> {
	Object[] items;
	int itemCount;

	ContiguousArrayStore(Object[] items) {
		this.items = items;
		this.itemCount = items.length;
	}

	/**
	 * Creates a new array store with the specified capacity.
	 *
	 * @throws IllegalArgumentException when the specified store capacity is negative
	 */
	ContiguousArrayStore(int capacity) {
		if (capacity < 0) {
			throw new IllegalArgumentException("Cannot create array store with negative capacity: " + capacity + ".");
		}

		this.items = new Object[capacity];
		this.itemCount = 0;
	}

	ContiguousArrayStore(int capacity, Object[] items) {
		this.items = new Object[capacity];
		this.itemCount = items.length;
		arraycopy(items, 0, this.items, 0, items.length);
	}

	ContiguousArrayStore(ContiguousArrayStore<T> store) {
		this.items = new Object[store.items.length];
		this.itemCount = store.itemCount;
		arraycopy(store.items, 0, this.items, 0, this.itemCount);
	}

	/**
	 * Creates a copy of the specified index range in the specified array store.
	 */
	public ContiguousArrayStore(ContiguousArrayStore<T> items, IndexRange indexRange) {
		this.itemCount = indexRange.end - indexRange.start + 1;
		this.items = (T[]) new Object[itemCount];
		arraycopy(items.items, indexRange.start, this.items, 0, itemCount);
	}

	private ContiguousArrayStore(T[] storage, int itemCount) {
		this.items = storage;
		this.itemCount = itemCount;
	}

	/**
	 * Returns {@code true} when this store contains an item at the specified index;
	 * returns {@code false} otherwise.
	 */
	public boolean hasIndex(int index) {
		return index >= 0
			&& index < itemCount;
	}

	/**
	 * Returns {@code true} when this store contains items at the specified index range;
	 * returns {@code false} otherwise.
	 */
	public boolean hasIndexRange(IndexRange indexRange) {
		return indexRange.start >= 0
			&& indexRange.end <= itemCount;
	}

	/**
	 * Returns item at the specified index in this store.
	 *
	 * @throws IndexNotInRangeException when the specified index is out of valid index
	 * range of this store
	 */
	public T get(int index) {
		if (itemCount == 0) {
			throw new IndexNotInRangeException(
				index, new IndexRange());
		}
		if (!hasIndex(index)) {
			throw new IndexNotInRangeException(index,
				new IndexRange(0, itemCount));
		}

		return (T) items[index];
	}

	/**
	 * Returns items at the specified index range in this store.
	 *
	 * @throws IndexRangeNotInRangeException when the specified index range is out of
	 * valid index range of this store
	 */
	public ContiguousArrayStore<T> get(IndexRange indexRange) {
		if (itemCount == 0) {
			throw new IndexRangeNotInRangeException(indexRange,
				new IndexRange());
		}
		if (!hasIndexRange(indexRange)) {
			throw new IndexRangeNotInRangeException(indexRange,
				new IndexRange(0, itemCount));
		}

		final var items = (T[]) new Object[indexRange.length];
		arraycopy(this.items, indexRange.start, items, 0, indexRange.length);

		return new ContiguousArrayStore<>(items, indexRange.length);
	}

	/**
	 * Replaces item at the specified index with the specified one. Does nothing when the
	 * specified item is {@code null}.
	 *
	 * @throws IndexNotInRangeException when the specified index is out of valid index
	 * range of this store
	 */
	public void set(int index, T item) {
		if (!hasIndex(index)) {
			throw new IndexNotInRangeException(index,
				new IndexRange(0, itemCount));
		}
		if (item == null) {
			return;
		}

		items[index] = item;
	}

	/**
	 * Prepends the specified item to the beginning of this store.
	 */
	public void prepend(Object item) {
		shiftItems(0, 1);

		items[0] = item;
		++itemCount;
	}

	/**
	 * Prepends the specified items to the beginning of this store.
	 */
	public void prepend(Object[] items) {
		shiftItems(0, items.length);

		arraycopy(items, 0, this.items, 0, items.length);
		itemCount += items.length;
	}

	/**
	 * Appends the specified item to the end of this store.
	 */
	public void append(T item) {
		if (item == null) {
			return;
		}

		prepareCapacity(1);

		items[itemCount] = item;
		itemCount += 1;
	}

	/**
	 * Appends items of the specified store to the end of this store.
	 */
	public void append(ContiguousArrayStore<T> store) {
		prepareCapacity(store.itemCount);

		arraycopy(store.items, 0, items, itemCount, store.itemCount);
		itemCount += store.itemCount;
	}

	/**
	 * Appends the specified items to the end of this store. Ignores any {@code null}
	 * values among the items.
	 */
	public void append(T... items) {
		prepareCapacity(items.length);

		for (var item : items) {
			if (item != null) {
				this.items[itemCount] = item;
				itemCount += 1;
			}
		}
	}

	/**
	 * Inserts the specified item into this store at the specified index. Does nothing
	 * when the specified item is {@code null}.
	 *
	 * @throws IndexNotInRangeException when the specified index is out of valid index
	 * range of this store
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

		items[index] = item;
		itemCount += 1;
	}

	/**
	 * Inserts items of the specified store into this store at the specified index.
	 *
	 * @throws IndexNotInRangeException when the specified index is out of valid index
	 * range of this store
	 */
	public void insert(int index, ContiguousArrayStore<T> store) {
		if (!hasIndex(index)) {
			throw new IndexNotInRangeException(index,
				new IndexRange(0, itemCount));
		}

		// shift items at the insertion index to the right to make room
		// for the inserted items
		moveItems(index, store.itemCount);

		arraycopy(store.items, 0, items, index, store.itemCount);
		itemCount += store.itemCount;
	}

	/**
	 * Removes item at the specified index from this store.
	 *
	 * @throws IndexNotInRangeException when the specified index is out of valid index
	 * range of this store
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
		items[itemCount] = null;
	}

	/**
	 * Removes items at the specified index range from this store.
	 *
	 * @throws IndexRangeNotInRangeException when the specified index range is out of
	 * valid index range of this store
	 */
	public void remove(IndexRange indexRange) {
		if (!hasIndexRange(indexRange)) {
			throw new IndexRangeNotInRangeException(indexRange,
				new IndexRange(0, itemCount));
		}

		// shift items after the removed index range to the left
		moveItems(indexRange.end, -indexRange.length);
		itemCount -= indexRange.length;

		// clear items after the shifted ones to make them eligible for
		// garbage collection
		final var endIndex = itemCount + indexRange.length;
		for (var index = itemCount; index < endIndex; index += 1) {
			items[index] = null;
		}
	}

	/**
	 * Returns items of this store in reverse order.
	 *
	 * @return
	 */
	public ContiguousArrayStore<T> reverse() {
		final var reverseItems = (T[]) new Object[itemCount];
		for (var index = 0; index < itemCount; ++index) {
			final var reverseIndex = itemCount - index - 1;
			reverseItems[reverseIndex] = (T) items[index];
		}

		return new ContiguousArrayStore<>(reverseItems, itemCount);
	}

	/**
	 * Returns items of this store, ordered according to the specified
	 * {@link Comparator}.
	 */
	public ContiguousArrayStore<T> sort(Comparator<T> comparator) {
		final var sortedItems = (T[]) new Object[itemCount];
		arraycopy(items, 0, sortedItems, 0, itemCount);

		Arrays.sort(sortedItems, 0, itemCount, comparator);
		return new ContiguousArrayStore<>(sortedItems, itemCount);
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
	public ContiguousArrayStore<T> shuffle(Random random) {
		final var shuffledItems = (T[]) new Object[itemCount];
		arraycopy(items, 0, shuffledItems, 0, itemCount);

		// it's more convenient to iterate items backwards for simpler
		// random index generation
		for (var index = itemCount - 1; index >= 0; --index) {
			final var randomIndex = random.nextInt(index + 1);

			// swap items at iterated and randomly generated indices
			final var item = shuffledItems[index];
			shuffledItems[index] = shuffledItems[randomIndex];
			shuffledItems[randomIndex] = item;
		}

		return new ContiguousArrayStore<>(shuffledItems, itemCount);
	}

	private void prepareCapacity(int extraItemCount) {
		final var newItemCount = itemCount + extraItemCount;
		if (newItemCount <= items.length) {
			return;
		}

		final var newCapacity = 2 * newItemCount + 1;
		final var newStorage = (T[]) new Object[newCapacity];
		arraycopy(items, 0, newStorage, 0, itemCount);

		items = newStorage;
	}

	public void removeExcessCapacity() {
		if (itemCount == items.length) {
			return;
		}

		final var newCapacity = itemCount;
		final var newStorage = (T[]) new Object[newCapacity];
		arraycopy(items, 0, newStorage, 0, itemCount);

		items = newStorage;
	}

	private void shiftItems(int index, int positions) {
		if (itemCount + positions > items.length) {
			final var capacity = 2 * (itemCount + positions);
			final var expanded = new Object[capacity];

			if (index > 0) {
				arraycopy(items, 0, expanded, 0, index);
			}
			arraycopy(items, index, expanded, index + positions,
				itemCount - index);

			items = expanded;
		} else {
			arraycopy(items, index, items, index + positions,
				itemCount - index);
		}
	}

	void moveItems(int startIndex, int positions) {
		final var newItemCount = itemCount + positions;

		if (newItemCount > items.length) {
			final var newCapacity = 2 * newItemCount + 1;
			final var newStorage = (T[]) new Object[newCapacity];

			arraycopy(items, 0, newStorage, 0, startIndex);
			arraycopy(items, startIndex, newStorage,
				startIndex + positions, itemCount - startIndex);

			items = newStorage;
		} else {
			arraycopy(items, startIndex, items,
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
				final var nextItem = items[index];
				index += 1;

				return (T) nextItem;
			}
		};
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(items);
	}

	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof ContiguousArrayStore)) {
			return false;
		}

		final var store = (ContiguousArrayStore) object;
		return Arrays.equals(items, 0, itemCount,
			store.items, 0, store.itemCount);
	}
}
