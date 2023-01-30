package co.tsyba.core.collections;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/*
 * Created by Serge Tsyba (tsyba@me.com) on May 12, 2019.
 */
public interface Sequence<T> extends Iterable<T> {
	/**
	 * Returns {@code true} when this sequence is empty; returns {@code false} otherwise.
	 */
	default boolean isEmpty() {
		final var iterator = iterator();
		return !iterator.hasNext();
	}

	/**
	 * Returns the number of items in this sequence.
	 */
	default int getCount() {
		var count = 0;
		for (var ignored : this) {
			count += 1;
		}

		return count;
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
		var last = (T) null;
		for (var item : this) {
			last = item;
		}

		return Optional.ofNullable(last);
	}

	/**
	 * Returns the smallest item in this sequence, according to the specified
	 * {@link Comparator}.
	 * <p>
	 * When this sequence is empty, returns an empty {@link Optional}.
	 */
	default Optional<T> getMinimum(Comparator<T> comparator) {
		final var iterator = iterator();
		if (!iterator.hasNext()) {
			return Optional.empty();
		}

		var minimum = iterator.next();
		while (iterator.hasNext()) {
			final var item = iterator.next();
			if (comparator.compare(minimum, item) > 0) {
				minimum = item;
			}
		}

		return Optional.of(minimum);
	}

	/**
	 * Returns the largest item in this sequence, according to the specified
	 * {@link Comparator}.
	 * <p>
	 * When this sequence is empty, returns an empty {@link Optional}.
	 */
	default Optional<T> getMaximum(Comparator<T> comparator) {
		final var iterator = iterator();
		if (!iterator.hasNext()) {
			return Optional.empty();
		}

		var maximum = iterator.next();
		while (iterator.hasNext()) {
			final var item = iterator.next();
			if (comparator.compare(maximum, item) < 0) {
				maximum = item;
			}
		}

		return Optional.of(maximum);
	}

	/**
	 * Returns {@code true} when this sequence contains the specified item; returns
	 * {@code false} otherwise.
	 */
	default boolean contains(T item) {
		return matchFirst(sequenceItem -> sequenceItem.equals(item))
			.isPresent();
	}

	/**
	 * Returns {@code true} when this sequence contains every item of the specified
	 * sequence; returns {@code false} otherwise.
	 */
	default boolean contains(Sequence<T> items) {
		return items.eachMatches(this::contains);
	}

	/**
	 * Returns the first item in this sequence, which matches the specified
	 * {@link Predicate}.
	 * <p>
	 * When this sequence is empty, returns an empty {@link Optional}.
	 */
	default Optional<T> matchFirst(Predicate<T> condition) {
		for (var item : this) {
			if (condition.test(item)) {
				return Optional.of(item);
			}
		}

		return Optional.empty();
	}

	/**
	 * Returns {@code true} when no item in this sequence satisfies the specified
	 * {@link Predicate}; returns {@code false} when at least one does.
	 * <p>
	 * When this sequence is empty, returns {@code true}.
	 */
	default boolean noneMatches(Predicate<T> condition) {
		return matchFirst(condition)
			.isEmpty();
	}

	/**
	 * Returns {@code true} when at least one item in this sequence satisfies the
	 * specified {@link Predicate}; returns {@code false} otherwise.
	 * <p>
	 * When this sequence is empty, returns {@code true}.
	 */
	default boolean anyMatches(Predicate<T> condition) {
		return matchFirst(condition)
			.isPresent();
	}

	/**
	 * Returns {@code true} when each item of this sequence satisfies the specified
	 * {@link Predicate}; returns {@code false} when at least one does not.
	 * <p>
	 * When this collection is empty, returns {@code true}.
	 */
	default boolean eachMatches(Predicate<T> condition) {
		return matchFirst(item -> !condition.test(item))
			.isEmpty();
	}

	/**
	 * Returns distinct items of this collection.
	 *
	 * @return
	 */
	Sequence<T> getDistinct();

	/**
	 * Returns items of this collection, ordered according to the specified
	 * {@link Comparator}.
	 *
	 * @param comparator
	 * @return
	 */
	IndexedCollection<T> sort(Comparator<T> comparator);

	/**
	 * Applies the specified {@link Consumer} to every item of this collection. Returns
	 * itself.
	 *
	 * @param operation
	 * @return
	 */
	default Sequence<T> iterate(Consumer<T> operation) {
		for (var item : this) {
			operation.accept(item);
		}

		return this;
	}

	/**
	 * Returns items of this collection, which satisfy the specified {@link Predicate}.
	 *
	 * @param condition
	 * @return
	 */
	Sequence<T> filter(Predicate<T> condition);

	/**
	 * Returns items of this collection, converted by the specified {@link Function}.
	 *
	 * @param <R>
	 * @param converter
	 * @return
	 */
	<R> Sequence<R> convert(Function<T, R> converter);

	/**
	 * Combines this collection into a single value by applying the specified
	 * {@link BiFunction} to every item and an intermediate combination, using the
	 * specified value as a starting point.
	 *
	 * @param <R>
	 * @param initial
	 * @param combiner
	 * @return
	 */
	default <R> R combine(R initial, BiFunction<R, T, R> combiner) {
		var combination = initial;
		for (var item : this) {
			combination = combiner.apply(combination, item);
		}

		return combination;
	}

	/**
	 * Combines this collection into a {@link String} by joining its items with the
	 * specified separator between them.
	 *
	 * @param separator
	 * @return
	 */
	default String join(String separator) {
		final var builder = new StringBuilder();
		final var iterator = iterator();

		if (iterator.hasNext()) {
			final var item = iterator.next();
			builder.append(item);
		}

		while (iterator.hasNext()) {
			final var item = iterator.next();
			builder.append(separator)
				.append(item);
		}

		return builder.toString();
	}
}