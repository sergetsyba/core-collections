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
	ContigousArrayStore<T> store;

	/**
	 * Creates a new list using the specified {@link ContigousArrayStore} as its backing store.
	 *
	 * @param store
	 */
	List(ContigousArrayStore<T> store) {
		this.store = store;
	}

	/**
	 * Creates a copy of the specified items.
	 *
	 * @param items
	 */
	public List(List<T> items) {
		if (items.isEmpty()) {
			this.store = new ContigousArrayStore<>(0);
		} else {
			final var indexRange = new IndexRange(0, items.store.itemCount - 1);
			this.store = new ContigousArrayStore<>(items.store, indexRange);
		}
	}

	/**
	 * Creates a list with the specified items. Ignores any {@code null} values among the items.
	 *
	 * @param items
	 */
	public List(T... items) {
		this.store = new ContigousArrayStore<>(items.length);
		this.store.append(items);
		this.store.removeExcessCapacity();
	}

	/**
	 * Creates a list with the specified items. Ignores any {@code null} values among the items.
	 *
	 * @param items
	 */
	public List(Iterable<T> items) {
		this.store = new ContigousArrayStore<>(64);
		for (var item : items) {
			this.store.append(item);
		}

		this.store.removeExcessCapacity();
	}

	/**
	 * Returns {@code true} when this list is empty; returns {@code false} otherwise.
	 */
	@Override
	public boolean isEmpty() {
		return store.itemCount == 0;
	}

	/**
	 * Returns the number of items in this list.
	 */
	@Override
	public int getCount() {
		return store.itemCount;
	}

	/**
	 * Returns the first item of this list.
	 * <p>
	 * Returns an empty {@link Optional} when this list is empty.
	 */
	@Override
	public Optional<T> getFirst() {
		return guard(0)
			.map(this::get);
	}

	/**
	 * Returns the last item of this list.
	 * <p>
	 * Returns an empty {@link Optional} when this list is empty.
	 */
	@Override
	public Optional<T> getLast() {
		return guard(store.itemCount - 1)
			.map(this::get);
	}

	/**
	 * Returns item at the specified index in this list.
	 *
	 * @throws IndexNotInRangeException when the specified index is out of valid index range of
	 * this list
	 */
	public T get(int index) {
		return store.get(index);
	}

	/**
	 * Returns items at the specified {@link IndexRange} in this list.
	 *
	 * @throws IndexRangeNotInRangeException when the specified {@link IndexRange} is out of valid
	 * index range of this list
	 */
	public List<T> get(IndexRange indexRange) {
		final var items = store.get(indexRange);
		return new List<>(items);
	}

	/**
	 * Returns an {@link Optional} with the specified index when it is within the valid index
	 * range of this list; returns an empty {@link Optional} otherwise.
	 * <p>
	 * This method safeguards any index-based operations on the list. It may serve as an
	 * alternative to explicitly checking whether an index is within valid index range of this
	 * list.
	 * <p>
	 * For instance, to safely get an item at an index
	 * <pre>{@code
	 * 	final var list = new List<String>("a", "b", "c");
	 * 	final var c = list.guard(2)
	 * 		.map(list::get);
	 * }</pre>
	 */
	public Optional<Integer> guard(int index) {
		if (!store.hasIndex(index)) {
			return Optional.empty();
		}

		return Optional.of(index);
	}

	/**
	 * Returns distinct items of this list.
	 */
	@Override
	public List<T> getDistinct() {
		final var distinctItems = new List<T>(
			new ContigousArrayStore<>(store.itemCount));

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
	 *
	 * @return
	 */
	@Override
	public List<T> reverse() {
		final var reversedItems = store.reverse();
		return new List<>(reversedItems);
	}

	/**
	 * Returns items of this list, ordered according to the specified {@link Comparator}.
	 *
	 * @param comparator
	 * @return
	 */
	@Override
	public List<T> sort(Comparator<T> comparator) {
		final var sortedItems = store.sort(comparator);
		return new List<>(sortedItems);
	}

	/**
	 * Returns items of this list in random order.
	 *
	 * @return
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
	 * The specified {@link Consumer} is applied consecutively to every item from first to last.
	 * Returns itself.
	 *
	 * @param operation
	 * @return
	 */
	@Override
	public List<T> iterate(Consumer<T> operation) {
		return (List<T>) IndexedCollection.super.iterate(operation);
	}

	/**
	 * Applies the specified {@link Consumer} to every item and its index in this list.
	 * <p>
	 * The specified {@link Consumer} is applied consecutively to every item from first to last and
	 * to every index from starting to ending. Returns itself.
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
	 * <p>
	 * This operation preserves relative item order - the filtered items will appear in the same
	 * relative order in the returned list as they appear in this list (accounting for items removed
	 * by filtering).
	 *
	 * @param condition
	 * @return
	 */
	@Override
	public List<T> filter(Predicate<T> condition) {
		final var filteredItems = new ContigousArrayStore<T>(store.itemCount);
		for (var item : this) {
			if (condition.test(item)) {
				filteredItems.append(item);
			}
		}

		filteredItems.removeExcessCapacity();
		return new List<>(filteredItems);
	}

	/**
	 * Returns items of this list, converted using the specified {@link Function}.
	 * <p>
	 * This operation preserves relative item order - the converted items will appear in the same
	 * relative order in the returned list as their originals in this list.
	 * <p>
	 * Any {@code null} value returned by the specified {@link Function} will be ignored. This can
	 * be used to perform both item filtering and conversion in a single operation.
	 *
	 * @param <R>
	 * @param converter
	 * @return
	 */
	@Override
	public <R> List<R> convert(Function<T, R> converter) {
		final var convertedItems = new ContigousArrayStore<R>(store.itemCount);
		for (var item : this) {
			final var convertedItem = converter.apply(item);
			convertedItems.append(convertedItem);
		}

		convertedItems.removeExcessCapacity();
		return new List<>(convertedItems);
	}

	/**
	 * Converts this list into an instance of {@link java.util.List}.
	 *
	 * @return
	 */
	public java.util.List<T> bridge() {
		final var items = Arrays.copyOf(store.storage, store.itemCount);
		return Arrays.asList(items);
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

	@Override
	public String toString() {
		return "[" + join(", ") + "]";
	}

	@Override
	public Iterator<T> reverseIterator() {
		throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	}
}
