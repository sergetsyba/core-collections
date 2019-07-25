package co.tsyba.core.collections;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/*
 * Created by Serge Tsyba <tsyba@me.com> on May 26, 2019.
 */
public class List<T> implements IndexedCollection<T> {
	final ContigousArrayStore<T> store;

	List(ContigousArrayStore<T> store) {
		this.store = store;
	}

	/**
	 * Creates a copy of the specified items.
	 *
	 * @param items
	 */
	public List(List<T> items) {
		final var indexRange = new IndexRange(0, items.store.itemCount);
		this.store = new ContigousArrayStore<>(items.store, indexRange);
	}

	/**
	 * Creates a list with the specified items. Ignores any {@code null} values
	 * among the items.
	 *
	 * @param items
	 */
	public List(T... items) {
		this.store = new ContigousArrayStore<>(items.length);
		this.store.append(items);
		this.store.removeExcessCapacity();
	}

	/**
	 * Returns {@code true} when this list is empty; returns {@code false}
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
	 * Returns the first item of this list. Returns an empty {@link Optional}
	 * when this list is empty.
	 *
	 * @return
	 */
	@Override
	public Optional<T> getFirst() {
		// todo: return guard(0, this::get);

		if (isEmpty()) {
			return Optional.empty();
		}

		final var item = store.storage[0];
		return Optional.of(item);
	}

	/**
	 * Returns the last item of this list. Returns an empty {@link Optional}
	 * when this list is empty.
	 *
	 * @return
	 */
	@Override
	public Optional<T> getLast() {
		if (isEmpty()) {
			return Optional.empty();
		}

		final var endIndex = store.itemCount - 1;
		final var item = store.storage[endIndex];

		return Optional.of(item);
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
		return store.storage[index];
	}

	// todo: get(IndexRange)
	// todo: guard(int, Consumer<>);
	// todo: guard(int, Function<>);
	/**
	 * Returns distinct items of this list.
	 *
	 * @return
	 */
	@Override
	public List<T> getDistinct() {
		final var distinctStore = new ContigousArrayStore<T>(store.itemCount);
		final var distinctItems = new List<>(distinctStore);

		for (var item : this) {
			if (!distinctItems.contains(item)) {
				distinctItems.store.append(item);
			}
		}

		distinctStore.removeExcessCapacity();
		return distinctItems;
	}

	/**
	 * Returns items of this list in reverse order.
	 *
	 * @return
	 */
	@Override
	public List<T> reverse() {
		final var reversedStore = store.reverse();
		return new List<>(reversedStore);
	}

	/**
	 * Returns items of this list, ordered according to the specified
	 * {@link Comparator}.
	 *
	 * @param comparator
	 * @return
	 */
	@Override
	public List<T> sort(Comparator<T> comparator) {
		final var sortedStore = store.sort(comparator);
		return new List<>(sortedStore);
	}

	/**
	 * Returns items of this list in random order.
	 *
	 * @return
	 */
	@Override
	public List<T> shuffle() {
		final var random = new Random();
		final var shuffledStore = store.shuffle(random);

		return new List<>(shuffledStore);
	}

	/**
	 * Applies the specified {@link Consumer} to every item of this list.
	 *
	 * The specified {@link Consumer} is applied consecutively to every item
	 * from first to last. Returns itself.
	 *
	 * @param operation
	 * @return
	 */
	@Override
	public List<T> iterate(Consumer<T> operation) {
		return (List<T>) IndexedCollection.super.iterate(operation);
	}

	/**
	 * Applies the specified {@link Consumer} to every item and its index in
	 * this list.
	 *
	 * The specified {@link Consumer} is applied consecutively to every item
	 * from first to last and to every index from starting to ending. Returns
	 * itself.
	 *
	 * @param operation
	 * @return
	 */
	@Override
	public List<T> enumerate(BiConsumer<T, Integer> operation) {
		return (List<T>) IndexedCollection.super.enumerate(operation);
	}

	/**
	 * Returns items of this list, which match the specified {@link Predicate}.
	 *
	 * This operation preserves relative item order - the filtered items will
	 * appear in the same relative order in the returned list as they appear in
	 * this list (accounting for items removed by filtering).
	 *
	 * @param condition
	 * @return
	 */
	@Override
	public List<T> filter(Predicate<T> condition) {
		final var filteredStore = new ContigousArrayStore<T>(store.itemCount);
		for (var item : this) {
			if (condition.test(item)) {
				filteredStore.append(item);
			}
		}

		filteredStore.removeExcessCapacity();
		return new List<>(filteredStore);
	}

	/**
	 * Returns items of this list, converted using the specified
	 * {@link Function}.
	 *
	 * This operation preserves relative item order - the converted items will
	 * appear in the same relative order in the returned list as their originals
	 * in this list.
	 *
	 * Any {@code null} value returned by the specified {@link Function} will be
	 * ignored. This can be used to achieve item filtering and conversion as a
	 * single operation.
	 *
	 * @param <R>
	 * @param converter
	 * @return
	 */
	@Override
	public <R> List<R> convert(Function<T, R> converter) {
		final var convertedStore = new ContigousArrayStore<R>(store.itemCount);
		for (var item : this) {
			final var convertedItem = converter.apply(item);
			convertedStore.append(convertedItem);
		}

		convertedStore.removeExcessCapacity();
		return new List<>(convertedStore);
	}

	@Override
	public Iterator<T> iterator() {
		return store.iterator();
	}

	@Override
	public Iterator<T> iterator(int startIndex) {
		return store.iterator(startIndex);
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
