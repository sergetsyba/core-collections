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
	 * Returns the first item of this collection.
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
	 * Returns the last item of this collection.
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
	 * Returns {@code true} when this collection contains the specified items, accounting
	 * for their order; returns {@code false} otherwise.
	 */
	default boolean contains(IndexedCollection<T> items) {
		return find(items)
			.isPresent();
	}

	/**
	 * Returns index of the first occurrence of an item, which satisfies the specified
	 * {@link Predicate} in this collection.
	 * <p>
	 * When this collection is empty, or no item in this collection satisfies the
	 * specified {@link Predicate}, returns an empty {@link Optional}.
	 */
	default Optional<Integer> matchFirst(Predicate<T> predicate) {
		var index = 0;
		for (var item : this) {
			if (predicate.test(item)) {
				return Optional.of(index);
			}

			index += 1;
		}

		return Optional.empty();
	}

	/**
	 * Returns index of the first occurrence of the specified item in this collection.
	 * <p>
	 * Returns an empty {@link Optional} when the specified item is not in this
	 * collection.
	 *
	 * @param item
	 * @return
	 */
	default Optional<Integer> find(T item) {
		return matchFirst(storedItem -> storedItem.equals(item));
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
	 * Returns iterator over items of this collection, starting from the specified index.
	 *
	 * @param startIndex
	 * @return
	 */
	Iterator<T> iterator(int startIndex);

	/**
	 * Returns iterator over items of this collection in reverse order.
	 *
	 * @return
	 */
	Iterator<T> reverseIterator();
}

// created on Jul 3, 2019