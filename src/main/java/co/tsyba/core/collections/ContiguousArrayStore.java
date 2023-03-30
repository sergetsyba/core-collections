package co.tsyba.core.collections;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import static java.lang.System.arraycopy;
import static java.util.Arrays.fill;

class ContiguousArrayStore {
	Object[] items;
	int itemCount;

	static ContiguousArrayStore compact(Object[] items) {
		final var store = new ContiguousArrayStore(items.length);
		var count = 0;

		for (var item : items) {
			if (item != null) {
				store.items[count] = item;
				++count;
			}
		}

		store.itemCount = count;
		return store;
	}

	ContiguousArrayStore(Object[] items) {
		var index = 0;
		for (; index < items.length; ++index) {
			if (items[index] == null) {
				break;
			}
		}

		this.items = items;
		this.itemCount = index;
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

	private ContiguousArrayStore(Object[] items, int itemCount) {
		this.items = items;
		this.itemCount = itemCount;
	}

	/**
	 * Returns index of the first appearance of specified item in this store after the
	 * specified index.
	 * <p>
	 * When the specified item does not appear in this store after the specified index,
	 * returns -1.
	 */
	int find(int index, Object item) {
		++index;
		for (; index < itemCount; ++index) {
			if (items[index] == item) {
				return index;
			}
		}

		return -1;
	}

	int find(int startIndex, ContiguousArrayStore store) {
		final var maxIndex = itemCount - store.itemCount + 1;

		outerLoop:
		for (var index1 = startIndex + 1; index1 < maxIndex; ++index1) {
			for (var index2 = 0; index2 < store.itemCount; ++index2) {
				final var item1 = items[index1 + index2];
				final var item2 = store.items[index2];
				if (!item1.equals(item2)) {
					continue outerLoop;
				}
			}

			return index1;
		}

		return -1;
	}

	/**
	 * Returns {@code true} when items of the specified store occur in this store at the
	 * specified index; returns {@code false} otherwise.
	 */
	boolean contains(int index, ContiguousArrayStore store) {
		for (var index2 = 0; index2 < store.itemCount; ++index2) {
			if (!items[index + index2].equals(store.items[index])) {
				return false;
			}
		}

		return true;
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
	 * Prepends items from the specified store to the beginning of this store.
	 */
	void prepend(ContiguousArrayStore store) {
		shiftItems(0, store.itemCount);

		arraycopy(store.items, 0, items, 0, store.itemCount);
		itemCount += store.itemCount;
	}

	/**
	 * Appends the specified item to the end of this store.
	 */
	void append(Object item) {
		ensureExcessCapacity(1);

		items[itemCount] = item;
		++itemCount;
	}

	/**
	 * Appends items from the specified store to the end of this store.
	 */
	void append(ContiguousArrayStore store) {
		ensureExcessCapacity(store.itemCount);

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
	 * Inserts items from the specified store into this store at the specified index.
	 * <p>
	 * Produces undefined result when the specified index is after the current item
	 * count.
	 */
	void insert(int index, ContiguousArrayStore store) {
		shiftItems(index, store.itemCount);

		arraycopy(store.items, 0, items, index, store.itemCount);
		itemCount += store.itemCount;
	}

	/**
	 * Replaces items at the specified index range in this store with the items from the
	 * specified store.
	 */
	void replace(IndexRange range, ContiguousArrayStore store) {
		shiftItems(range.end, store.itemCount - range.length);

		arraycopy(store.items, 0, items, range.start, store.itemCount);
		itemCount += store.itemCount - range.length;
	}

	/**
	 * Removes item at the specified index from this store.
	 */
	void remove(int index) {
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
	 */
	ContiguousArrayStore reverse() {
		final var reversed = new Object[items.length];
		for (var index = 0; index < itemCount; ++index) {
			reversed[index] = items[itemCount - 1 - index];
		}

		return new ContiguousArrayStore(reversed, itemCount);
	}

	/**
	 * Returns items of this store, ordered according to the specified
	 * {@link Comparator}.
	 */
	@SuppressWarnings("unchecked")
	<T> ContiguousArrayStore sort(Comparator<T> comparator) {
		final var sorted = new Object[items.length];
		arraycopy(items, 0, sorted, 0, itemCount);

		Arrays.sort((T[]) sorted, 0, itemCount, comparator);
		return new ContiguousArrayStore(sorted, itemCount);
	}

	/**
	 * Returns items of this store in random order, based on the specified
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
	 */
	ContiguousArrayStore shuffle(Random random) {
		final var shuffled = new Object[items.length];
		arraycopy(items, 0, shuffled, 0, itemCount);

		// it's more convenient to iterate items backwards for simpler
		// random index generation
		for (var index = itemCount - 1; index >= 0; --index) {
			final var randomIndex = random.nextInt(index + 1);

			// swap items at iterated and randomly generated indices
			final var item = shuffled[index];
			shuffled[index] = shuffled[randomIndex];
			shuffled[randomIndex] = item;
		}

		return new ContiguousArrayStore(shuffled, itemCount);
	}

	void ensureExcessCapacity(int extra) {
		if (itemCount + extra > items.length) {
			final var capacity = 2 * (itemCount + extra);
			final var expanded = new Object[capacity];
			arraycopy(items, 0, expanded, 0, itemCount);

			items = expanded;
		}
	}

	void removeExcessCapacity() {
		if (itemCount < items.length) {
			final var items = new Object[itemCount];
			arraycopy(this.items, 0, items, 0, itemCount);
			this.items = items;
		}
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
