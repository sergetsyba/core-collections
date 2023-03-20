package co.tsyba.core.collections;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * A {@link Collection}, which provides access to its items, based on their positional
 * index.
 * <p>
 * Unlike a {@link Collection}, {@link IndexedCollection} guarantees the same relative
 * item order on each iteration. Therefore, each item has a non-negative positional index,
 * which can be used to retrieve it from the collection.
 * <p>
 * Note, that {@link IndexedCollection} does not guarantee an efficient way of retrieving
 * items by their index. In general, index-based retrieval has O(n) complexity, needing to
 * iterate over all items before it.
 */
public interface IndexedCollection<T> extends Collection<T> {
	/**
	 * Returns valid index range of this collection.
	 */
	default IndexRange getIndexRange() {
		final var count = getCount();
		return new IndexRange(0, count);
	}

	/**
	 * Returns an {@link Optional} with the specified index when it is within the valid
	 * index range of this collection; returns an empty {@link Optional} otherwise.
	 * <p>
	 * This method safeguards any index-based operations on this collection. It may serve
	 * as an alternative to explicitly checking whether an index is within the valid index
	 * range.
	 * <p>
	 * For instance, to safely get an item at an index
	 * <pre>{@code
	 * 	final var list = new List<String>("a", "b", "c");
	 * 	final var c = list.guard(2)
	 * 		.map(list::get);
	 * }</pre>
	 */
	default Optional<Integer> guard(int index) {
		final var range = getIndexRange();

		return range.contains(index)
			? Optional.of(index)
			: Optional.empty();
	}

	/**
	 * Returns item at the specified index in this collection.
	 *
	 * @throws IndexNotInRangeException when the specified index is out valid index range
	 * of this collection
	 */
	default T get(int index) {
		final var range = getIndexRange();
		if (!range.contains(index)) {
			throw new IndexNotInRangeException(index, range);
		}

		final var iterator = iterator();
		var index2 = 0;

		while (index2 < index) {
			iterator.next();
			++index2;
		}

		return iterator.next();
	}

	/**
	 * Returns the first item in this collection.
	 * <p>
	 * When this collection is empty, returns an empty {@link Optional}.
	 */
	default Optional<T> getFirst() {
		return guard(0)
			.map(this::get);
	}

	/**
	 * Returns the last item in this collection.
	 * <p>
	 * When this collection is empty, returns an empty {@link Optional}.
	 */
	default Optional<T> getLast() {
		final var itemCount = getCount();

		return guard(itemCount - 1)
			.map(this::get);
	}

	/**
	 * Returns items at the specified index range in this collection.
	 *
	 * @throws IndexRangeNotInRangeException when the specified index range is out of the
	 * valid index range of this collection
	 */
	IndexedCollection<T> get(IndexRange indexRange);

	/**
	 * Returns items from the first up to the one at the specified index in this
	 * collection.
	 *
	 * @throws IndexNotInRangeException when the specified index is out of valid index
	 * range of this collection
	 */
	default IndexedCollection<T> getPrefix(int index) {
		final var validRange = getIndexRange();
		if (!validRange.contains(index)) {
			throw new IndexNotInRangeException(index, validRange);
		}

		final var prefixRange = new IndexRange(0, index);
		return get(prefixRange);
	}

	/**
	 * Returns items from the one at the specified index up to the last in this
	 * collection.
	 *
	 * @throws IndexNotInRangeException when the specified index is out of valid index
	 * range of this collection
	 */
	default IndexedCollection<T> getSuffix(int index) {
		final var validRange = getIndexRange();
		if (!validRange.contains(index)) {
			throw new IndexNotInRangeException(index, validRange);
		}

		final var prefixRange = new IndexRange(index, validRange.end);
		return get(prefixRange);
	}

	/**
	 * Returns {@code true} when this collection contains the specified items, accounting
	 * for their order; returns {@code false} otherwise.
	 */
	default boolean contains(IndexedCollection<T> items) {
		return find(items)
			.isPresent();
	}

	/**
	 * Returns index of the first occurrence of the specified item in this collection.
	 * <p>
	 * When the specified item does not occur in this collection, returns an empty
	 * {@link Optional}.
	 */
	default Optional<Integer> find(T item) {
		var index = 0;
		for (var storedItem : this) {
			if (storedItem.equals(item)) {
				return Optional.of(index);
			} else {
				++index;
			}
		}

		return Optional.empty();
	}

	/**
	 * Returns index of the first occurrence of the specified items in this collection.
	 * <p>
	 * Returns an empty {@link Optional} when the specified items do not occur in this
	 * collection in this order.
	 */
	Optional<Integer> find(IndexedCollection<T> items);

	/**
	 * Returns items of this collection in reverse order.
	 */
	IndexedCollection<T> reverse();

	/**
	 * Returns items of this collection in random order.
	 */
	IndexedCollection<T> shuffle();

	/**
	 * Applies the specified {@link BiConsumer} to every item and its index in this
	 * collection.
	 * <p>
	 * Returns itself.
	 */
	default IndexedCollection<T> enumerate(BiConsumer<T, Integer> operation) {
		var index = 0;
		for (var item : this) {
			operation.accept(item, index);
			index += 1;
		}

		return this;
	}

	/**
	 * Returns iterator over items of this collection in reverse order.
	 */
	Iterator<T> reverseIterator();
}

// created on Jul 3, 2019