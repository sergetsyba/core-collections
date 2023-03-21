package co.tsyba.core.collections;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

import static java.lang.System.arraycopy;
import static java.util.Arrays.fill;

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

	private ContiguousArrayStore(T[] storage, int itemCount) {
		this.items = storage;
		this.itemCount = itemCount;
	}

	/**
	 * Prepends the specified item to the beginning of this store.
	 */
	void prepend(Object item) {
		shiftItems(0, 1);

		items[0] = item;
		++itemCount;
	}

	/**
	 * Prepends the specified items to the beginning of this store.
	 */
	void prepend(Object[] items) {
		shiftItems(0, items.length);

		arraycopy(items, 0, this.items, 0, items.length);
		itemCount += items.length;
	}

	/**
	 * Appends the specified item to the end of this store.
	 */
	void append(Object item) {
		ensureCapacity(1);

		items[itemCount] = item;
		++itemCount;
	}

	/**
	 * Appends the specified items to the end of this store.
	 */
	void append(Object[] items) {
		ensureCapacity(items.length);

		arraycopy(items, 0, this.items, itemCount, items.length);
		itemCount += items.length;
	}

	/**
	 * Appends items of the specified store to the end of this store.
	 */
	void append(ContiguousArrayStore<T> store) {
		ensureCapacity(store.itemCount);

		arraycopy(store.items, 0, items, itemCount, store.itemCount);
		itemCount += store.itemCount;
	}

	/**
	 * Inserts the specified item into this store at the specified index.
	 * <p>
	 * Produces undefined result when the specified index is after the current item
	 * count.
	 */
	void insert(int index, Object item) {
		shiftItems(index, 1);

		items[index] = item;
		++itemCount;
	}

	/**
	 * Inserts the specified items into this store at the specified index.
	 * <p>
	 * Produces undefined result when the specified index is after the current item
	 * count.
	 */
	void insert(int index, Object[] items) {
		shiftItems(index, items.length);

		arraycopy(items, 0, this.items, index, items.length);
		itemCount += items.length;
	}

	/**
	 * Replaces items at the specified index range in this list with the specified items.
	 */
	void replace(IndexRange range, Object[] items) {
		shiftItems(range.end, items.length - range.length);

		arraycopy(items, 0, this.items, range.start, items.length);
		itemCount += items.length - range.length;
	}

	/**
	 * Removes item at the specified index from this store.
	 */
	public void remove(int index) {
		shiftItems(index + 1, -1);
		itemCount -= 1;
	}

	/**
	 * Removes items at the specified index range from this store.
	 */
	void remove(IndexRange indexRange) {
		shiftItems(indexRange.end, -indexRange.length);
		itemCount -= indexRange.length;
	}

	/**
	 * Returns items of this store in reverse order.
	 *
	 * @return
	 */
	ContiguousArrayStore<T> reverse() {
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
	ContiguousArrayStore<T> sort(Comparator<T> comparator) {
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
	ContiguousArrayStore<T> shuffle(Random random) {
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

	private void ensureCapacity(int extra) {
		if (itemCount + extra > items.length) {
			final var capacity = 2 * (itemCount + extra);
			final var expanded = new Object[capacity];
			arraycopy(items, 0, expanded, 0, itemCount);

			items = expanded;
		}
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

			if (positions < 0) {
				// clear items after the shifted range
				fill(items, itemCount + positions, itemCount, null);
			}
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

// created on Jan 28, 2019
