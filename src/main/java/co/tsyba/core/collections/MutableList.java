package co.tsyba.core.collections;

public class MutableList<T> extends List<T> {
	static final int minimumCapacity = 64;

	/**
	 * Creates an empty list with the specified item capacity.
	 * <p>
	 * When approximate item count is known in advance, use this constructor to create a
	 * list with enough space in its backing store. This improves performance when adding
	 * items to this list and saves memory when default capacity exceeds item count.
	 *
	 * @throws IllegalArgumentException when the specified item capacity is negative
	 */
	public MutableList(int capacity) {
		super(capacity);
	}

	/**
	 * Creates a list with the specified items.
	 * <p>
	 * Ignores any {@code null} values among the specified items.
	 */
	@SafeVarargs
	public MutableList(T... items) {
		this(Math.max(items.length, minimumCapacity));
		append(items);
	}

	/**
	 * Creates a list with the specified items.
	 */
	public MutableList(List<T> items) {
		this(Math.max(items.getCount(), minimumCapacity));
		append(items);
	}

	/**
	 * Replaces item at the specified index in this list with the specified item.
	 * <p>
	 * When the specified item is {@code null}, does nothing.
	 *
	 * @return itself
	 * @throws IndexNotInRangeException when the specified index is out of valid index
	 * range of this list
	 */
	public MutableList<T> set(int index, T item) {
		store.set(index, item);
		return this;
	}

	/**
	 * Prepends the specified item to the beginning of this list.
	 * <p>
	 * When the specified item is {@code null}, does nothing.
	 *
	 * @return itself
	 */
	public MutableList<T> prepend(T item) {
		if (item != null) {
			store.prepend(item);
		}

		return this;
	}

	/**
	 * Prepends the specified items to the beginning of this list.
	 * <p>
	 * Ignores any {@code null} values among the specified items.
	 *
	 * @return itself
	 */
	@SafeVarargs
	public final MutableList<T> prepend(T... items) {
		var nullCount = 0;
		for (var item : items) {
			if (item == null) {
				nullCount += 1;
			}
		}

		store.moveItems(0, items.length - nullCount);

		for (int index1 = 0, index2 = 0; index1 < items.length; index1 += 1) {
			if (items[index1] != null) {
				store.items[index2] = items[index1];
				index2 += 1;
			}
		}

		return this;
	}

	/**
	 * Prepends the specified items to the beginning of this list.
	 *
	 * @return itself
	 */
	public MutableList<T> prepend(List<T> items) {
		final var count = items.getCount();
		store.moveItems(0, count);

		var index = 0;
		for (var item : items) {
			store.items[index] = item;
			index += 1;
		}

		return this;
	}

	/**
	 * Appends the specified item to the end of this list.
	 * <p>
	 * When the specified item is {@code null}, does nothing.
	 *
	 * @return itself
	 */
	public MutableList<T> append(T item) {
		if (item != null) {
			store.append(item);
		}

		return this;
	}

	/**
	 * Appends the specified items to the end of this list.
	 * <p>
	 * Ignores any {@code null} values among the specified items.
	 *
	 * @return itself
	 */
	@SafeVarargs
	public final MutableList<T> append(T... items) {
		for (var item : items) {
			if (item != null) {
				store.append(item);
			}
		}

		return this;
	}

	/**
	 * Appends the specified items to the end of this list.
	 *
	 * @return itself
	 */
	public MutableList<T> append(List<T> items) {
		// todo: copy array
		items.forEach(this::append);
		return this;
	}

	/**
	 * Inserts the specified item into this list at the specified index.
	 * <p>
	 * When the specified item is {@code null}, does nothing.
	 *
	 * @return itself
	 * @throws IndexNotInRangeException when the specified index is out of valid index
	 * range of this list
	 */
	public MutableList<T> insert(int index, T item) {
		store.insert(index, item);
		return this;
	}

	/**
	 * Inserts the specified items into this list at the specified index.
	 * <p>
	 * Ignores any {@code null} values among the specified items.
	 *
	 * @return itself
	 * @throws IndexNotInRangeException when the specified index is out of valid index
	 * range of this list
	 */
	@SafeVarargs
	public final MutableList<T> insert(int index, T... items) {
		final var store2 = new ContiguousArrayStore<T>(items.length);
		for (var item : items) {
			if (item != null) {
				store2.append(item);
			}
		}

		store.insert(index, store2);
		return this;
	}

	/**
	 * Inserts the specified items into this list at the specified index.
	 *
	 * @return itself
	 * @throws IndexNotInRangeException when the specified index is out of valid index
	 * range of this list
	 */
	public MutableList<T> insert(int index, List<T> items) {
		store.insert(index, items.store);
		return this;
	}

	/**
	 * Replaces items at the specified index range in this list with the specified items.
	 * <p>
	 * Ignores any {@code null} values among the specified items.
	 *
	 * @return itself
	 * @throws IndexRangeNotInRangeException when the specified index range is out of
	 * valid index range of this list
	 */
	@SafeVarargs
	public final MutableList<T> replace(IndexRange range, T... items) {
		final var validRange = getIndexRange();
		if (!validRange.contains(range) || isEmpty()) {
			throw new IndexRangeNotInRangeException(range, validRange);
		}

		final var store2 = new ContiguousArrayStore<T>(items.length);
		for (var item : items) {
			if (item != null) {
				store2.append(item);
			}
		}

		store.remove(range);
		if (range.start == store.itemCount) {
			store.append(store2);
		} else {
			store.insert(range.start, store2);
		}
		return this;
	}

	/**
	 * Replaces items at the specified index range in this list with the specified items.
	 *
	 * @return itself
	 * @throws IndexRangeNotInRangeException when the specified index range is out of
	 * valid index range of this list
	 */
	public final MutableList<T> replace(IndexRange range, List<T> items) {
		final var validRange = getIndexRange();
		if (!validRange.contains(range) || isEmpty()) {
			throw new IndexRangeNotInRangeException(range, validRange);
		}

		store.remove(range);
		if (range.start == store.itemCount) {
			store.append(items.store);
		} else {
			store.insert(range.start, items.store);
		}

		return this;
	}

	/**
	 * Removes the first item from this list.
	 * <p>
	 * When this list is empty, does nothing.
	 *
	 * @return itself
	 */
	public MutableList<T> removeFirst() {
		guard(0)
			.ifPresent(store::remove);

		return this;
	}

	/**
	 * Removes the last item from this list.
	 * <p>
	 * When this list is empty, does nothing.
	 *
	 * @return itself
	 */
	public MutableList<T> removeLast() {
		guard(store.itemCount - 1)
			.ifPresent(store::remove);

		return this;
	}

	/**
	 * Removes item at the specified index in this list.
	 *
	 * @return itself
	 * @throws IndexNotInRangeException when the specified index is out of valid index
	 * range of this list
	 */
	public MutableList<T> remove(int index) {
		store.remove(index);
		return this;
	}

	/**
	 * Removes items at the specified index range in this list.
	 *
	 * @return itself
	 * @throws IndexRangeNotInRangeException when the specified index range is out of
	 * valid index range of this list
	 */
	public MutableList<T> remove(IndexRange indexRange) {
		store.remove(indexRange);
		return this;
	}

	/**
	 * Removes all items from this list.
	 *
	 * @return itself
	 */
	public MutableList<T> clear() {
		store = new ContiguousArrayStore<>(minimumCapacity);
		return this;
	}

	/**
	 * Returns immutable copy of this list.
	 */
	public List<T> toImmutable() {
		return new List<>(this);
	}
}

// created on May 12, 2019