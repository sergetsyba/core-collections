package co.tsyba.core.collections;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/*
 * Created by Serge Tsyba (tsyba@me.com) on May 12, 2019.
 */
public class MutableList<T> extends List<T> {
	private static final int minimumCapacity = 64;

	MutableList(ContigousArrayStore<T> store) {
		super(store);
	}

	/**
	 * Creates a copy of the specified items.
	 *
	 * @param items
	 */
	public MutableList(List<T> items) {
		this(Math.min(items.getCount(), minimumCapacity));
		this.store.append(items.store);
	}

	/**
	 * Creates a list with the specified items. Ignores any {@code null} values
	 * among the items.
	 *
	 * @param items
	 */
	public MutableList(T... items) {
		this(Math.min(items.length, minimumCapacity));
		this.store.append(items);
	}

	/**
	 * Creates an empty list with the specified amount of reserved capacity.
	 *
	 * When item count is known in advance, reserving item capacity during list
	 * creation improves performance of item insertion and item appending.
	 *
	 * @param capacity
	 */
	public MutableList(int capacity) {
		this(new ContigousArrayStore<T>(capacity));
	}

	/**
	 * Replaces item at the specified index in this list with the specified one.
	 *
	 * @throws IndexNotInRangeException when the specified index is out of valid
	 * index range of this list
	 *
	 * @param index
	 * @param item
	 * @return
	 */
	public MutableList<T> set(int index, T item) {
		store.set(index, item);
		return this;
	}

	/**
	 * Appends the specified item to the end of this list. Does nothing when the
	 * specified item is {@code null}. Returns itself.
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
	public MutableList<T> append(List<T> items) {
		store.append(items.store);
		return this;
	}

	/**
	 * Appends the specified items to the end of this list. Ignores any
	 * {@code null} values among the items. Returns itself.
	 *
	 * @param items
	 * @return
	 */
	public MutableList<T> append(T... items) {
		store.append(items);
		return this;
	}

	/**
	 * Inserts the specified item into this list at the specified index. Does
	 * nothing when the specified item is {@code null}.
	 *
	 * @throws IndexNotInRangeException when the specified index is out of valid
	 * index range of this list
	 *
	 * @param index
	 * @param item
	 * @return
	 */
	public MutableList<T> insert(int index, T item) {
		store.insert(index, item);
		return this;
	}

	/**
	 * Inserts the specified items into this list at the specified index.
	 *
	 * @throws IndexNotInRangeException when the specified index is out of valid
	 * index range of this list
	 *
	 * @param index
	 * @param items
	 * @return
	 */
	public MutableList<T> insert(int index, List<T> items) {
		store.insert(index, items.store);
		return this;
	}

	/**
	 * Removes the first item from this list. Returns the removed item. Returns
	 * an empty {@link Optional} when this list is empty.
	 *
	 * @return
	 */
	public Optional<T> removeFirst() {
		return getFirst()
				.map(item -> {
					store.remove(0);
					return item;
				});
	}

	/**
	 * Removes the last item from this list. Returns the removed item. Returns
	 * an empty {@link Optional} when this list is empty.
	 *
	 * @return
	 */
	public Optional<T> removeLast() {
		return getLast()
				.map(item -> {
					final var endIndex = store.itemCount - 1;
					store.remove(endIndex);

					return item;
				});
	}

	/**
	 * Removes item at the specified index from this list. Returns the removed
	 * item.
	 *
	 * @throws IndexNotInRangeException when the specified index is out of valid
	 * index range of this list
	 *
	 * @param index
	 * @return
	 */
	public T remove(int index) {
		final var item = get(index);
		store.remove(index);

		return item;
	}

	/**
	 * Removes items at the specified index range from this list. Returns the
	 * removed items.
	 *
	 * @throws IndexRangeNotInRangeException when the specified index range is
	 * out of valid index range of this list
	 *
	 * @param indexRange
	 * @return
	 */
	public MutableList<T> remove(IndexRange indexRange) {
		final var items = store.get(indexRange);
		store.remove(indexRange);

		return new MutableList<>(items);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MutableList<T> getDistinct() {
		final var distinctStore = new ContigousArrayStore<T>(store.itemCount);
		final var distinctItems = new MutableList<>(distinctStore);

		for (var item : this) {
			if (!distinctItems.contains(item)) {
				distinctItems.store.append(item);
			}
		}

		return distinctItems;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MutableList<T> filter(Predicate<T> condition) {
		final var filteredStore = new ContigousArrayStore<T>(store.itemCount);
		for (var item : this) {
			if (condition.test(item)) {
				filteredStore.append(item);
			}
		}

		return new MutableList<>(filteredStore);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <R> MutableList<R> convert(Function<T, R> converter) {
		final var convertedStore = new ContigousArrayStore<R>(store.itemCount);
		for (var item : this) {
			final var convertedItem = converter.apply(item);
			convertedStore.append(convertedItem);
		}

		return new MutableList<>(convertedStore);
	}
}
