package co.tsyba.core.collections;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/*
 * Created by Serge Tsyba (tsyba@me.com) on May 12, 2019.
 */
public class MutableList<T> extends List<T> {
	MutableList(ContigousArrayStore<T> store) {
		super(store);
	}

	public MutableList(int capacity) {
	}

	public MutableList() {
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
	 * Appends items from the specified items to the end of this list. Returns
	 * itself.
	 *
	 * @param items
	 * @return
	 */
	public MutableList<T> append(List<T> items) {
		store.append(items.store);
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
	 * Removes and returns the first item from this list. Returns an empty
	 * {@link Optional} when this list is empty.
	 *
	 * @return
	 */
	public Optional<T> removeFirst() {
		return store.removeFirst();
	}

	/**
	 * Removes and returns the last item from this list. Returns an empty
	 * {@link Optional} when this list is empty.
	 *
	 * @return
	 */
	public Optional<T> removeLast() {
		return store.removeLast();
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

	@Override
	public List<T> filter(Predicate<T> condition) {
		return super.filter(condition); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public <R> List<R> convert(Function<T, R> converter) {
		return super.convert(converter); //To change body of generated methods, choose Tools | Templates.
	}
}
