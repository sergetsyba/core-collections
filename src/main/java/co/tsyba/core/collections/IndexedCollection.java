package co.tsyba.core.collections;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A {@link Collection}, which provides access to its items, based on their positional
 * index.
 * <p>
 * Unlike a {@link Collection}, {@link IndexedCollection} guarantees the same relative
 * item order on each iteration. Therefore, each item has a non-negative positional index,
 * using which it can be retrieved from the collection.
 * <p>
 * Note, that {@link IndexedCollection} does not guarantee an efficient way of retrieving
 * items by their index. In general, index-based retrieval has O(n) complexity, needing to
 * iterate over all items before it.
 */
public interface IndexedCollection<T> extends Collection<T> {
	/**
	 * Returns valid index range of this collection.
	 */
	IndexRange getIndexRange();

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
	 * Returns item at the specified index in this collection.
	 */
	default T get(int index) {
		final var iterator = iterator(index);
		return iterator.next();
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
		return isEmpty()
			? Optional.empty()
			: findUnsafely(0, item);
	}

	/**
	 * Returns index of the first occurrence of the specified item at or after the
	 * specified index in this collection
	 * <p>
	 * When the specified item does not occur at or after the specified index in this
	 * collection, returns an empty {@link Optional}.
	 *
	 * @throws IndexNotInRangeException when the specified index is out of valid index
	 * range of this collection.
	 */
	default Optional<Integer> find(int startIndex, T item) {
		final var range = getIndexRange();
		if (!range.contains(startIndex)) {
			throw new IndexNotInRangeException(startIndex, range);
		}

		return findUnsafely(startIndex, item);
	}

	private Optional<Integer> findUnsafely(int startIndex, T item) {
		final var iterator = iterator(startIndex);
		var index = startIndex;

		while (iterator.hasNext()) {
			final var next = iterator.next();
			if (next.equals(item)) {
				return Optional.of(index);
			} else {
				++index;
			}
		}

		return Optional.empty();
	}

	/**
	 * Returns the first item in this collection, which satisfies the specified
	 * {@link Predicate}.
	 * <p>
	 * When this collection is empty, or no item in this collection satisfies the
	 * specified {@link Predicate}, returns an empty {@link Optional}.
	 */
	default Optional<T> match(Predicate<T> predicate) {
		return isEmpty()
			? Optional.empty()
			: matchUnsafely(0, predicate);
	}

	/**
	 * Returns the first item in this collection, which satisfies the specified
	 * {@link Predicate}, at or after the specified index.
	 * <p>
	 * When this collection is empty, or no item in this collection satisfies the
	 * specified {@link Predicate} at or after the specified index, returns an empty
	 * {@link Optional}.
	 *
	 * @throws IndexNotInRangeException when the specified start index is out of valid
	 * index range of this collection
	 */
	default Optional<T> match(int startIndex, Predicate<T> predicate) {
		final var range = getIndexRange();
		if (!range.contains(startIndex)) {
			throw new IndexNotInRangeException(startIndex, range);
		}

		return matchUnsafely(startIndex, predicate);
	}

	private Optional<T> matchUnsafely(int startIndex, Predicate<T> predicate) {
		final var iterator = iterator(startIndex);
		while (iterator.hasNext()) {
			final var item = iterator.next();
			if (predicate.test(item)) {
				return Optional.of(item);
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
	 *
	 * @return
	 */
	IndexedCollection<T> reverse();

	/**
	 * Returns items of this collection in random order.
	 *
	 * @return
	 */
	IndexedCollection<T> shuffle();

	/**
	 * Applies the specified {@link Consumer} to every item and its index in this
	 * collection.
	 * <p>
	 * Returns itself.
	 *
	 * @param operation
	 * @return
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
	 *
	 * @return
	 */
	Iterator<T> reverseIterator();
}

// created on Jul 3, 2019