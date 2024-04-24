package co.tsyba.core.collections;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * A container, which provides repeatable, non-destructive iterations over its items and
 * preserves relative item order in each iteration.
 * <p>
 * Unlike a more generic {@link Collection}, a {@link Sequence} guarantees its items
 * appear in the same relative order in each iteration, unless the {@link Sequence} is
 * modified. This property enables access to each item individually via a non-negative
 * positional index.
 */
public interface Sequence<T> extends Collection<T> {
	/**
	 * Returns valid index range of this sequence.
	 */
	default IndexRange getIndexRange() {
		final var count = getCount();
		return new IndexRange(0, count);
	}

	/**
	 * Returns the first item in this sequence.
	 * <p>
	 * When this sequence is empty, returns an empty {@link Optional}.
	 */
	default Optional<T> getFirst() {
		for (var item : this) {
			return Optional.of(item);
		}

		return Optional.empty();
	}

	/**
	 * Returns the last item in this sequence.
	 * <p>
	 * When this sequence is empty, returns an empty {@link Optional}.
	 */
	default Optional<T> getLast() {
		final var iterator = iterator();
		T item = null;

		while (iterator.hasNext()) {
			item = iterator.next();
		}

		return Optional.ofNullable(item);
	}

	/**
	 * Returns item at the specified index in this sequence.
	 *
	 * @throws IndexNotInRangeException when the specified index is out of valid index
	 * range of this sequence
	 */
	default T get(int index) {
		final var range = getIndexRange();
		if (!range.contains(index)) {
			throw new IndexNotInRangeException(index, range);
		}

		final var iterator = iterator();
		var index2 = range.start;

		while (index2 < index) {
			iterator.next();
			++index2;
		}

		return iterator.next();
	}

	/**
	 * Returns items from the first up to, but excluding, the one at the specified index
	 * in this sequence.
	 *
	 * @throws IndexNotInRangeException when the specified index is out of the valid index
	 * range of this sequence
	 */
	Sequence<T> getPrefix(int index);

	/**
	 * Returns items from the one at the specified index up to the last one in this
	 * sequence.
	 *
	 * @throws IndexNotInRangeException when the specified index is out of the valid range
	 * of this index
	 */
	Sequence<T> getSuffix(int index);

	/**
	 * Returns items within the specified index range in this sequence.
	 *
	 * @throws IndexRangeNotInRangeException when the specified index range is out of the
	 * valid index range of this sequence
	 */
	Sequence<T> get(IndexRange range);

	/**
	 * Returns an {@link Optional} with the specified index when it is within the valid
	 * index range of this sequence; returns an empty {@link Optional} otherwise.
	 * <p>
	 * This method can be used to safely call index based methods, which would otherwise
	 * result in an {@link IndexNotInRangeException}.
	 */
	default Optional<Integer> guard(int index) {
		final var range = getIndexRange();

		return range.contains(index)
			? Optional.of(index)
			: Optional.empty();
	}

	/**
	 * Returns an {@link Optional} with the specified {@link IndexRange} when it is within
	 * the valid index range of this sequence; returns an empty {@link Optional}
	 * otherwise.
	 * <p>
	 * This method can be used to safely call index range based methods, which would
	 * otherwise result in an {@link IndexRangeNotInRangeException}.
	 */
	default Optional<IndexRange> guard(IndexRange range) {
		final var validRange = getIndexRange();

		return validRange.contains(range)
			? Optional.of(range)
			: Optional.empty();
	}

	/**
	 * Returns index of the first occurrence of the specified item in this sequence. When
	 * this item does not occur in this sequence, returns an empty {@link Optional}.
	 */
	default Optional<Integer> findFirst(T item) {
		return findFirst((item2) -> {
			return item2.equals(item);
		});
	}

	/**
	 * Returns index of the first occurrence of the specified sequence in this sequence.
	 * When the specified sequence does not occur in this sequence, returns an empty
	 * {@link Optional}.
	 * <p>
	 * When the specified sequence is empty, returns the first valid index of this
	 * sequence, unless this sequence is empty as well.
	 */
	default Optional<Integer> findFirst(Sequence<T> items) {
		final var range = getIndexRange();
		if (range.isEmpty()) {
			return Optional.empty();
		}
		if (items.isEmpty()) {
			return Optional.of(range.start);
		}

		search:
		for (var index : range) {
			final var iterator1 = items.iterator();
			final var iterator2 = iterator(index);

			while (iterator1.hasNext() && iterator2.hasNext()) {
				final var item1 = iterator1.next();
				final var item2 = iterator2.next();

				if (!item1.equals(item2)) {
					continue search;
				}
			}

			return Optional.of(index);
		}

		return Optional.empty();
	}

	/**
	 * Returns index of the first item, which matches the specified {@link Predicate} in
	 * this sequence. When no item matches the specified {@link Predicate} in this
	 * sequence, returns an empty {@link Optional}.
	 * <p>
	 * When this sequence is empty, returns an empty {@link Optional}. When the specified
	 * sequence is empty, returns first valid index of this sequence.
	 */
	default Optional<Integer> findFirst(Predicate<T> condition) {
		final var range = getIndexRange();
		var index = range.start;

		for (var item : this) {
			if (condition.test(item)) {
				return Optional.of(index);
			} else {
				++index;
			}
		}

		return Optional.empty();
	}

	/**
	 * Returns items of this sequence in reverse order.
	 */
	Sequence<T> reverse();

	/**
	 * Applies the specified {@link BiConsumer} to each item and its index. Returns
	 * itself.
	 */
	default Sequence<T> enumerate(BiConsumer<Integer, T> consumer) {
		var index = 0;
		for (var item : this) {
			consumer.accept(index, item);
			++index;
		}

		return this;
	}

	default Iterator<T> iterator(int index) {
		final var range = getIndexRange();
		if (!range.contains(index)) {
			throw new IndexNotInRangeException(index, range);
		}

		final var iterator = iterator();
		var index2 = range.start;

		while (index2 < index) {
			iterator.next();
			++index2;
		}

		return iterator;
	}
}
