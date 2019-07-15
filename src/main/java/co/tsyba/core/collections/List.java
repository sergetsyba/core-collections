package co.tsyba.core.collections;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/*
 * Created by Serge Tsyba <tsyba@me.com> on May 26, 2019.
 */
public class List<T> implements IndexedCollection<T> {
	private static final int defaultMinimumCapacity = 64;
	final ContigousArrayStore<T> store;

	List(ContigousArrayStore<T> store) {
		this.store = store;
	}

	public List() {
		this.store = new ContigousArrayStore<>(defaultMinimumCapacity);
	}

	public List(List<T> items) {
		this.store = null;
	}

	public List(T... items) {
		this.store = new ContigousArrayStore<>(items.length);
		this.store.append(items);
	}

	/**
	 * Returns {@code true} when this list has no items; returns {@code false}
	 * otherwise.
	 *
	 * @return
	 */
	@Override
	public boolean isEmpty() {
		return store.itemCount == 0;
	}

	/**
	 * Returns the number of items in this list.
	 *
	 * @return
	 */
	@Override
	public int getCount() {
		return store.itemCount;
	}

	/**
	 * Returns item at the specified index in this list.
	 *
	 * @throws IndexNotInRangeException when the specified index is out of valid
	 * index range of this list
	 *
	 * @param index
	 * @return
	 */
	public T get(int index) {
		return store.get(index);
	}

	/**
	 * Returns the first item in this list. Returns an empty {@link Optional}
	 * when this list is empty.
	 *
	 * @return
	 */
	@Override
	public Optional<T> getFirst() {
		if (isEmpty()) {
			return Optional.empty();
		}

		final var item = store.storage[0];
		return Optional.of(item);
	}

	/**
	 * Returns the last item in this list. Returns an empty {@link Optional}
	 * when this list is empty.
	 *
	 * @return
	 */
	@Override
	public Optional<T> getLast() {
		if (isEmpty()) {
			return Optional.empty();
		}

		final var item = store.storage[store.itemCount - 1];
		return Optional.of(item);
	}

	/**
	 * Applies the specified {@link Consumer} to every item in this list.
	 *
	 * This operation
	 *
	 * @param operation
	 * @return
	 */
	@Override
	public List<T> iterate(Consumer<T> operation) {
		for (var item : this) {
			operation.accept(item);
		}

		return this;
	}

	@Override
	public Iterator<T> iterator() {
		return store.iterator();
	}

	@Override
	public Iterator<T> reverseIterator() {
		return store.reverseIterator();
	}

	/**
	 * Returns items, which match the specified {@link Predicate} in this list.
	 *
	 * This operation preserves relative item order: filtered items in the
	 * returned list will appear in the same relative order as they appear in
	 * this list (accounting for items removed by filtering).
	 *
	 * @param condition
	 * @return
	 */
	public List<T> filter(Predicate<T> condition) {
		final var itemCount = getCount();
		final var filteredItems = new MutableList<T>(itemCount);

		for (var item : this) {
			if (condition.test(item)) {
				filteredItems.append(item);
			}
		}

		return new List<>(filteredItems);
	}

	/**
	 * Returns items from this list, converted using the specified
	 * {@link Function}.
	 *
	 * This operation preserves relative item order: converted items in the
	 * returned list will appear in the same order as their originals in this
	 * list.
	 *
	 * Additionally, any {@code null} value returned by the specified
	 * {@link Function} will be ignored. This can be used to achieve item
	 * filtering and conversion as a single operation.
	 *
	 * @param <R>
	 * @param converter
	 * @return
	 */
	public <R> List<R> convert(Function<T, R> converter) {
		final var itemCount = getCount();
		final var convertedItems = new MutableList<R>(itemCount);

		for (var item : this) {
			final var convertedItem = converter.apply(item);
			convertedItems.append(convertedItem);
		}

		return new List<>(convertedItems);
	}

	@Override
	public int hashCode() {
		return store.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof List)) {
			return false;
		}

		final var items = (List) object;
		return store.equals(items.store);
	}
}
