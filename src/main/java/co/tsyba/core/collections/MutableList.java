package co.tsyba.core.collections;

import java.util.Optional;
import java.util.function.BiConsumer;

public class MutableList<T> extends List<T> {
	static final int minimumCapacity = 64;

	/**
	 * Creates an empty list with the specified item capacity.
	 * <p>
	 * When approximate item count is known in advance, use this constructor to create a
	 * list with enough space in its backing store. This improves performance of adding
	 * items to this list and saves memory when default capacity exceeds item count.
	 *
	 * @throws IllegalArgumentException when the specified item capacity is negative
	 */
	public MutableList(int capacity) {
		super(capacity);
	}

	/**
	 * Creates a list with the specified items.
	 */
	public MutableList(Collection<T> items) {
		this(Math.max(items.getCount(), minimumCapacity));
		append(items);
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


//	/**
//	 * Returns items at the specified index range in this list.
//	 *
//	 * @param indexRange
//	 * @return
//	 * @throws IndexRangeNotInRangeException when the specified index range is out of
//	 * valid index range of this list.
//	 */
//	@Override
//	public MutableList<T> get(IndexRange indexRange) {
//		final var items = store.get(indexRange);
//		return new MutableList<>(items);
//	}

	/**
	 * Replaces item at the specified index in this list with the specified item.
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
	 * Appends the specified item to the end of this list. Does nothing when the specified
	 * item is {@code null}. Returns itself.
	 *
	 * @param item
	 * @return
	 */
	public MutableList<T> append(T item) {
		store.append(item);
		return this;
	}

	/**
	 * Appends the specified items to the end of this list. Returns itself.
	 *
	 * @param items
	 * @return
	 */
	public MutableList<T> append(Collection<T> items) {
		items.forEach(this::append);
		return this;
	}

	/**
	 * Appends the specified items to the end of this list. Ignores any {@code null}
	 * values among the items. Returns itself.
	 *
	 * @param items
	 * @return
	 */
	public MutableList<T> append(T... items) {
		for (var item : items) {
			if (item != null) {

			}
		}

		store.append(items);
		return this;
	}

	/**
	 * Inserts the specified item into this list at the specified index. Does nothing when
	 * the specified item is {@code null}.
	 *
	 * @param index
	 * @param item
	 * @return
	 * @throws IndexNotInRangeException when the specified index is out of valid index
	 * range of this list
	 */
	public MutableList<T> insert(int index, T item) {
		store.insert(index, item);
		return this;
	}

	/**
	 * Inserts the specified items into this list at the specified index.
	 *
	 * @param index
	 * @param items
	 * @return
	 * @throws IndexNotInRangeException when the specified index is out of valid index
	 * range of this list
	 */
	public MutableList<T> insert(int index, List<T> items) {
		store.insert(index, items.store);
		return this;
	}

	/**
	 * Removes the first item from this list. Returns the removed item. Returns an empty
	 * {@link Optional} when this list is empty.
	 *
	 * @return
	 */
	public Optional<T> removeFirst() {
		final var startIndex = 0;
		return guard(0)
			.map(index -> {
				final var item = store.get(index);
				store.remove(index);
				return item;
			});
	}

	/**
	 * Removes the last item from this list. Returns the removed item. Returns an empty
	 * {@link Optional} when this list is empty.
	 *
	 * @return
	 */
	public Optional<T> removeLast() {
		return guard(store.itemCount - 1)
			.map(index -> {
				final var item = store.get(index);
				store.remove(index);
				return item;
			});
	}

	/**
	 * Removes item at the specified index from this list. Returns itself.
	 *
	 * @param index
	 * @return
	 * @throws IndexNotInRangeException when the specified index is out of valid index
	 * range of this list
	 */
	public MutableList<T> remove(int index) {
		store.remove(index);
		return this;
	}

	/**
	 * Removes items at the specified index range from this list. Returns itself.
	 *
	 * @param indexRange
	 * @return
	 * @throws IndexRangeNotInRangeException when the specified index range is out of
	 * valid index range of this list
	 */
	public MutableList<T> remove(IndexRange indexRange) {
		store.remove(indexRange);
		return this;
	}

	/**
	 * Removes all items from this list. Returns itself.
	 *
	 * @return
	 */
	public MutableList<T> clear() {
		store = new ContiguousArrayStore<>(minimumCapacity);
		return this;
	}

	/**
	 * Applies the specified {@link BiConsumer} to item at the specified index when the
	 * index is within the valid index range of this list. Does nothing otherwise. Returns
	 * itself.
	 *
	 * @param index
	 * @param operation
	 * @return
	 */
	public MutableList<T> guard(int index, BiConsumer<T, Integer> operation) {
		if (store.hasIndex(index)) {
			final var item = (T) store.items[index];
			operation.accept(item, index);
		}

		return this;
	}

	/**
	 * @return
	 */
	public List<T> toImmutable() {
		return new List<>(this);
	}
}

// created on May 12, 2019