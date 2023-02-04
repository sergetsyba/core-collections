package co.tsyba.core.collections;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/*
 * Created by Serge Tsyba <tsyba@me.com> on May 26, 2019.
 */
public class List<T> implements IndexedCollection<T> {
	ContiguousArrayStore<T> store;
	Object[] store2;

	/**
	 * Creates a list using the specified {@link ContiguousArrayStore} as its backing
	 * store.
	 */
	List(ContiguousArrayStore<T> store) {
		this.store = store;
	}

	/**
	 * Creates a copy of the specified items.
	 */
	public List(List<T> items) {
		this.store = new ContiguousArrayStore<>(items.store);
	}

	/**
	 * Creates a list with the specified items.
	 * <p>
	 * Ignores any {@code null} values among the specified items.
	 */
	@SafeVarargs
	public List(T... items) {
		final var store = new ContiguousArrayStore<T>(items.length);
		for (var item : items) {
			store.append(item);
		}

		store.removeExcessCapacity();
		this.store = new ContiguousArrayStore<>(store.items);

		final var store2 = new Object[items.length];
		var index = 0;
		for (var item : items) {
			if (item != null) {
				store2[index] = item;
				++index;
			}
		}

		this.store2 = (index == items.length)
			? store2
			: Arrays.copyOf(store2, index);
	}

	/**
	 * Creates a list with the specified items.
	 * <p>
	 * Ignores any {@code null} values among the specified items.
	 */
	public List(Iterable<T> items) {
		final var store = new ContiguousArrayStore<T>(64);
		for (var item : items) {
			store.append(item);
		}

		store.removeExcessCapacity();
		this.store = new ContiguousArrayStore<>(store.items);
	}

	/**
	 * Returns the number of items in this list.
	 */
	@Override
	public int getCount() {
		return store2.length;
	}

	/**
	 * Returns item at the specified index in this list.
	 *
	 * @throws IndexNotInRangeException when the specified index is out of valid index
	 * range of this list
	 */
	public T get(int index) {
		@SuppressWarnings("unchecked")
		final var item = (T) store2[index];
		return item;
	}

	/**
	 * Returns items at the specified {@link IndexRange} in this list.
	 *
	 * @throws IndexRangeNotInRangeException when the specified {@link IndexRange} is out
	 * of valid index range of this list
	 */
	public List<T> get(IndexRange indexRange) {
		final var items = store.get(indexRange);
		return new List<>(items);
	}

	@Override
	public Optional<Integer> find(IndexedCollection<T> items) {
		return Optional.empty();
	}

	/**
	 * Returns distinct items of this list.
	 */
	@Override
	public List<T> getDistinct() {
		final var distinctItems = new List<T>(
			new ContiguousArrayStore<>(store.itemCount));

		for (var item : this) {
			if (!distinctItems.contains(item)) {
				distinctItems.store.append(item);
			}
		}

		distinctItems.store.removeExcessCapacity();
		return distinctItems;
	}

	/**
	 * Returns items of this list in reverse order.
	 */
	@Override
	public List<T> reverse() {
		final var reversedItems = store.reverse();
		return new List<>(reversedItems);
	}

	/**
	 * Returns items of this list, ordered according to the specified {@link Comparator}.
	 */
	@Override
	public List<T> sort(Comparator<T> comparator) {
		final var sortedItems = store.sort(comparator);
		return new List<>(sortedItems);
	}

	/**
	 * Returns items of this list in random order.
	 */
	@Override
	public List<T> shuffle() {
		final var random = new Random();
		final var shuffledItems = store.shuffle(random);

		return new List<>(shuffledItems);
	}

	/**
	 * Applies the specified {@link Consumer} to every item of this list.
	 * <p>
	 * The specified {@link Consumer} is applied consecutively to every item from first to
	 * last.
	 *
	 * @return itself
	 */
	@Override
	public List<T> iterate(Consumer<T> operation) {
		return (List<T>) IndexedCollection.super.iterate(operation);
	}

	/**
	 * Applies the specified {@link BiConsumer} to every item and its index in this list.
	 * <p>
	 * The specified {@link BiConsumer} is applied consecutively to every item and its
	 * index from first to last.
	 *
	 * @return itself
	 */
	@Override
	public List<T> enumerate(BiConsumer<T, Integer> operation) {
		return (List<T>) IndexedCollection.super.enumerate(operation);
	}

	/**
	 * Returns items of this list, which match the specified {@link Predicate}.
	 * <p>
	 * This operation preserves relative item order - the filtered items will appear in
	 * the same relative order in the returned list as they appear in this list
	 * (accounting for items removed by filtering).
	 */
	@Override
	public List<T> filter(Predicate<T> condition) {
		final var filtered = new ContiguousArrayStore<T>(store.itemCount);
		for (var item : this) {
			if (condition.test(item)) {
				filtered.append(item);
			}
		}

		filtered.removeExcessCapacity();
		return new List<>(filtered);
	}

	/**
	 * Returns items of this list, converted using the specified {@link Function}.
	 * <p>
	 * This operation preserves relative item order - the converted items will appear in
	 * the same relative order in the returned list as their originals in this list.
	 * <p>
	 * Any {@code null} value returned by the specified {@link Function} will be ignored.
	 * Therefore, this method can be used to perform both item filtering and conversion in
	 * a single operation.
	 */
	@Override
	public <R> List<R> convert(Function<T, R> converter) {
		final var converted = new ContiguousArrayStore<R>(store.itemCount);
		for (var item : this) {
			final var convertedItem = converter.apply(item);
			converted.append(convertedItem);
		}

		converted.removeExcessCapacity();
		return new List<>(converted);
	}

	/**
	 * Returns items of this list as a {@link java.util.List}.
	 */
	public java.util.List<T> bridge() {
		final var items = Arrays.copyOf(store.items, store.itemCount);
		@SuppressWarnings("unchecked")
		final var list = (java.util.List<T>) Arrays.asList(items);
		return list;
	}

	@Override
	public Iterator<T> iterator() {
		return store.iterator();
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

		@SuppressWarnings("unchecked")
		final var items = (List<T>) object;
		return store.equals(items.store);
	}

	/**
	 * Returns a {@link String} representation of this list.
	 * <p>
	 * A {@link String} representation of the {@link List} is a {@link String}
	 * representation of its items, joined by comma (,) and enclosed into square brackets
	 * ([...]).
	 * <p>For instance,
	 * <pre>{@code
	 * final var items = new List<>("a", "b", "c");
	 * final var string = items.toString();
	 * // [a, b, c]
	 * }</pre>
	 */
	@Override
	public String toString() {
		return "[" + join(", ") + "]";
	}

	@Override
	public Iterator<T> reverseIterator() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
