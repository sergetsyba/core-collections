package co.tsyba.core.collections;

import java.util.Comparator;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static co.tsyba.core.collections.ContiguousArrayStore.compact;

/**
 * A mutable, sequential {@link Collection}, which provides efficient, randomized access
 * to its items.
 */
public class MutableList<T> extends List<T> {
	static final int minimumCapacity = 64;

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
		super(new ContiguousArrayStore(
			Math.max(items.length, minimumCapacity)));

		append(items);
	}

	/**
	 * Creates a copy of the specified items.
	 */
	public MutableList(Collection<T> items) {
		this(new ContiguousArrayStore(
			Math.max(items.getCount(), minimumCapacity)));

		items.forEach(this::append);
	}

	@Override
	public MutableList<T> getPrefix(int index) {
		final var prefix = super.getPrefix(index);
		return new MutableList<>(prefix.store);
	}

	@Override
	public MutableList<T> getSuffix(int index) {
		final var suffix = super.getSuffix(index);
		return new MutableList<>(suffix.store);
	}

	@Override
	public MutableList<T> get(IndexRange indexRange) {
		final var sub = super.get(indexRange);
		return new MutableList<>(sub);
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
		final var range = getIndexRange();
		if (!range.contains(index)) {
			throw new IndexNotInRangeException(index, range);
		}

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
		final var range = getIndexRange();
		if (!range.contains(index)) {
			throw new IndexNotInRangeException(index, range);
		}

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
		final var validRange = getIndexRange();
		if (!validRange.contains(range)) {
			throw new IndexRangeNotInRangeException(range, validRange);
		}

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
	public MutableList<T> getDistinct() {
		final var distinct = super.getDistinct();
		return new MutableList<>(distinct);
	}

	@Override
	public MutableList<T> reverse() {
		final var reversed = store.reverse();
		return new MutableList<>(reversed);
	}

	@Override
	public MutableList<T> sort(Comparator<T> comparator) {
		final var sorted = super.sort(comparator);
		return new MutableList<>(sorted.store);
	}

	@Override
	public List<T> sort() {
		final var sorted = super.sort();
		return new MutableList<>(sorted.store);
	}

	@Override
	public MutableList<T> shuffle(Random random) {
		final var shuffled = super.shuffle(random);
		return new MutableList<>(shuffled.store);
	}

	@Override
	public List<T> shuffle() {
		final var shuffled = super.shuffle();
		return new MutableList<>(shuffled.store);
	}

	@Override
	public MutableList<T> iterate(Consumer<T> operation) {
		super.iterate(operation);
		return this;
	}

	@Override
	public MutableList<T> enumerate(BiConsumer<T, Integer> operation) {
		super.enumerate(operation);
		return this;
	}

	@Override
	public MutableList<T> filter(Predicate<T> condition) {
		final var filtered = super.filter(condition);
		return new MutableList<>(filtered.store);
	}

	@Override
	public <R> MutableList<R> convert(Function<T, R> converter) {
		final var converted = super.convert(converter);
		return new MutableList<>(converted);
	}

	/**
	 * Returns immutable copy of this list.
	 */
	public List<T> toImmutable() {
		return new List<>(this);
	}
}

// created on May 12, 2019