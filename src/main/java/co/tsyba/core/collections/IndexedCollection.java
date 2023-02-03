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
 * which can be used to retrieve items from the collection.
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
	 * Returns item at the specified index in this collection.
	 *
	 * @throws IndexNotInRangeException when the specified index is out valid index range
	 * of this collection
	 */
	default T get(int index) {
		final var iterator = iterator(index);
		return iterator.next();
	}

	/**
	 * Returns the first item in this collection.
	 * <p>
	 * When this collection is empty, returns an empty {@link Optional}.
	 */
	default Optional<T> getFirst() {
		final var iterator = iterator();
		if (!iterator.hasNext()) {
			return Optional.empty();
		}

		final var item = iterator.next();
		return Optional.of(item);
	}

	/**
	 * Returns the last item in this collection.
	 * <p>
	 * When this collection is empty, returns an empty {@link Optional}.
	 */
	default Optional<T> getLast() {
		final var iterator = reverseIterator();
		if (!iterator.hasNext()) {
			return Optional.empty();
		}

		final var item = iterator.next();
		return Optional.of(item);
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
	 * Returns an empty {@link Optional} when the specified items are not in this
	 * collection.
	 *
	 * @param items
	 * @return
	 */
	default Optional<Integer> find(IndexedCollection<T> items) {
		var iterator1 = iterator();
		if (!iterator1.hasNext()) {
			// this collection is empty
			return Optional.empty();
		}

		indexSearch:
		for (var index = 0; ; ) {
			final var iterator2 = items.iterator();

			while (iterator2.hasNext()) {
				final var item1 = iterator1.next();
				final var item2 = iterator2.next();

				if (!item1.equals(item2)) {
					// found different items
					if (iterator1.hasNext()) {
						// there are more items after the different one in this
						// collection, restart search from the next index
						iterator1 = iterator(++index);
						continue indexSearch;
					} else {
						// there are no more items after the different one
						// in this collection, but more in the specified
						// collection; i.e. the specified collection extends
						// beyond this one, thus it is not contained
						return Optional.empty();
					}
				}
			}

			// reached end of the specified collection mathcing all items
			// with items in this collection
			return Optional.of(index);
		}
	}

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
	 * Returns iterator over items of this collection, which starts from the specified
	 * index.
	 *
	 * @throws IndexNotInRangeException when the specified index is out of valid index
	 * range of this collection
	 */
	default Iterator<T> iterator(int startIndex) {
		final var range = getIndexRange();
		if (!range.contains(startIndex)) {
			throw new IndexNotInRangeException(startIndex, range);
		}

		final var iterator = iterator();
		var index = 0;

		while (index < startIndex) {
			iterator.next();
			++index;
		}

		return iterator;
	}

	/**
	 * Returns iterator over items of this collection in reverse order.
	 */
	Iterator<T> reverseIterator();
}

// created on Jul 3, 2019