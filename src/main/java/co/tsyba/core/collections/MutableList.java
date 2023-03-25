package co.tsyba.core.collections;

import java.util.Comparator;
import java.util.Random;

import static co.tsyba.core.collections.ContiguousArrayStore.compact;

/**
 * A mutable, sequential {@link Collection}, which provides efficient, randomized access
 * to its items.
 */
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
	MutableList(int capacity) {
		super(capacity);
	}

	MutableList(ContiguousArrayStore store) {
		super(store);
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
	 * Creates a copy of the specified items.
	 */
	public MutableList(Collection<T> items) {
		this(Math.max(items.getCount(), minimumCapacity));
		items.forEach(this::append);
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
		store.items[index] = item;
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
		final var compacted = compact(items);
		store.prepend(compacted);

		return this;
	}

	/**
	 * Prepends the specified items to the beginning of this list.
	 *
	 * @return itself
	 */
	public MutableList<T> prepend(List<T> items) {
		store.prepend(items.store);
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
		final var compacted = compact(items);
		store.append(compacted);

		return this;
	}

	/**
	 * Appends the specified items to the end of this list.
	 *
	 * @return itself
	 */
	public MutableList<T> append(List<T> items) {
		store.append(items.store);
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
		final var validRange = getIndexRange();
		if (!validRange.contains(index)) {
			throw new IndexNotInRangeException(index, validRange);
		}

		if (item != null) {
			store.insert(index, item);
		}

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
		final var validRange = getIndexRange();
		if (!validRange.contains(index)) {
			throw new IndexNotInRangeException(index, validRange);
		}

		final var compacted = compact(items);
		store.insert(index, compacted);

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
		final var validRange = getIndexRange();
		if (!validRange.contains(index)) {
			throw new IndexNotInRangeException(index, validRange);
		}

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
		if (!validRange.contains(range)) {
			throw new IndexRangeNotInRangeException(range, validRange);
		}

		final var compacted = compact(items);
		store.replace(range, compacted);

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
		if (!validRange.contains(range)) {
			throw new IndexRangeNotInRangeException(range, validRange);
		}

		store.replace(range, items.store);
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
	public MutableList<T> remove(IndexRange range) {
		store.remove(range);
		return this;
	}

	/**
	 * Removes all items from this list.
	 *
	 * @return itself
	 */
	public MutableList<T> clear() {
		store = new ContiguousArrayStore(minimumCapacity);
		return this;
	}

	@Override
	public List<T> reverse() {
		final var reversed = store.reverse();
		return new MutableList<>(reversed);
	}

	@Override
	public List<T> sort(Comparator<T> comparator) {
		final var sorted = store.sort(comparator);
		return new MutableList<>(sorted);
	}

	@Override
	public List<T> shuffle() {
		final var seed = System.currentTimeMillis();
		final var random = new Random(seed);
		final var shuffled = store.shuffle(random);

		return new MutableList<>(shuffled);
	}

	/**
	 * Returns immutable copy of this list.
	 */
	public List<T> toImmutable() {
		return new List<>(this);
	}
}

// created on May 12, 2019