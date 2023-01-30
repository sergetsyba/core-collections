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
	 * Returns the number of items in this collection.
	 *
	 * @return
	 */
	default int getCount() {
		var itemCount = 0;
		for (var item : this) {
			itemCount += 1;
		}

		return itemCount;
	}

	/**
	 * Returns {@code true} when this collection contains the specified item; returns
	 * {@code false} otherwise.
	 *
	 * @param item
	 * @return
	 */
	default boolean contains(T item) {
		return anyMatches(storedItem -> storedItem.equals(item));
	}

	/**
	 * Returns {@code true} when this collection contains every item of the specified
	 * collection; returns {@code false} otherwise.
	 *
	 * @param items
	 * @return
	 */
	default boolean contains(Sequence<T> items) {
		return items.eachMatches(this::contains);
	}

	/**
	 * Returns {@code true} when no item in this collection satisfies the specified
	 * {@link Predicate}; returns {@code false} otherwise.
	 *
	 * @param condition
	 * @return
	 */
	default boolean noneMatches(Predicate<T> condition) {
		for (var item : this) {
			if (condition.test(item)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns {@code true} when at least one item in this collection satisfies the
	 * specified {@link Predicate}; returns {@code false} otherwise.
	 *
	 * @param condition
	 * @return
	 */
	default boolean anyMatches(Predicate<T> condition) {
		for (var item : this) {
			if (condition.test(item)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns {@code true} when every item of this collection satisfies the specified
	 * {@link Predicate}; returns {@code false} otherwise.
	 * <p>
	 * When this collection is empty, returns {@code false}.
	 *
	 * @param condition
	 * @return
	 */
	default boolean eachMatches(Predicate<T> condition) {
		for (var item : this) {
			if (!condition.test(item)) {
				return false;
			}
		}

		// empty collection matches no item
		return !isEmpty();
	}

	/**
	 * Returns the smallest item of this collection, according to the specified
	 * {@link Comparator}.
	 * <p>
	 * Returns an empty {@link Optional} when this collection is empty.
	 *
	 * @param comparator
	 * @return
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
	 * Returns the largest item of this collection, according to the specified
	 * {@link Comparator}.
	 * <p>
	 * Returns an empty {@link Optional} when this collection is empty.
	 *
	 * @param comparator
	 * @return
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
