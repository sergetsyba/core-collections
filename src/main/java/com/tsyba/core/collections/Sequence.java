package com.tsyba.core.collections;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
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
		if (isEmpty()) {
			return Optional.empty();
		}

		final var item = get(0);
		return Optional.of(item);
	}

	/**
	 * Returns the last item in this sequence.
	 * <p>
	 * When this sequence is empty, returns an empty {@link Optional}.
	 */
	default Optional<T> getLast() {
		final var count = getCount();
		if (count == 0) {
			return Optional.empty();
		}

		final var item = get(count - 1);
		return Optional.of(item);
	}

	/**
	 * Returns item at the specified index in this sequence.
	 *
	 * @throws IndexNotInRangeException when the specified index is out of valid index
	 * range of this sequence
	 */
	default T get(int index) {
		return iterator(index)
			.next();
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
	 * This method can be used to safely call index based methods, which may result in an
	 * {@link IndexNotInRangeException}.
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
	 * This method can be used to safely call index range based methods, which may result
	 * in an {@link IndexRangeNotInRangeException}.
	 */
	default Optional<IndexRange> guard(IndexRange range) {
		final var validRange = getIndexRange();

		return validRange.contains(range)
			? Optional.of(range)
			: Optional.empty();
	}

	/**
	 * Returns the first item in this sequence, which matches the specified
	 * {@link Predicate}.
	 * <p>
	 * When no item in this sequence matches the specified {@link Predicate} or this
	 * sequence is empty, returns and empty {@link Optional}.
	 */
	default Optional<T> matchFirst(Predicate<T> condition) {
		for (var item : this) {
			if (condition.test(item)) {
				return Optional.of(item);
			}
		}

		return Optional.empty();
	}

	@Override
	Sequence<T> matchAll(Predicate<T> condition);

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
	 * Returns index of the first item, which matches the specified {@link Predicate} in
	 * this sequence. When no item matches the specified {@link Predicate} in this
	 * sequence, returns an empty {@link Optional}.
	 * <p>
	 * When this sequence is empty, returns an empty {@link Optional}. When the specified
	 * sequence is empty, returns first valid index of this sequence.
	 */
	default Optional<Integer> findFirst(Predicate<T> condition) {
		var index = 0;
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
	 * Returns index of the first occurrence of the specified sequence in this sequence.
	 * When the specified sequence does not occur in this sequence, returns an empty
	 * {@link Optional}.
	 * <p>
	 * When the specified sequence is empty, returns the first valid index of this
	 * sequence, unless this sequence is empty as well.
	 */
	default Optional<Integer> findFirst(Sequence<T> items) {
		if (isEmpty()) {
			return Optional.empty();
		}

		final var range = getIndexRange();
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
	 * Returns indexes of all occurrences of the specified item in this sequence.
	 * <p>
	 * When the specified item does not occur in this sequence, or this sequence is empty,
	 * returns an empty sequence.
	 */
	Sequence<Integer> findAll(T item);

	/**
	 * Returns indexes of all occurrences of the specified sequence in this sequence.
	 * <p>
	 * When the specified sequence does not occur in this sequence, or this sequence is
	 * empty, returns an empty sequence. When the specified sequence is empty, returns all
	 * indexes of this sequence.
	 */
	Sequence<Integer> findAll(Sequence<T> items);

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

	@Override
	<R> Sequence<R> convert(Function<T, R> converter);

	/**
	 * Returns {@link Iterator}, which starts with item at the specified index.
	 *
	 * @throws IndexNotInRangeException when the specified index is out of valid range of
	 * this sequence
	 */
	default Iterator<T> iterator(int index) {
		final var range = getIndexRange();
		if (!range.contains(index)) {
			throw new IndexNotInRangeException(index, range);
		}

		final var iterator = iterator();
		var skip = 0;

		while (skip < index) {
			iterator.next();
			++skip;
		}

		return iterator;
	}
}
