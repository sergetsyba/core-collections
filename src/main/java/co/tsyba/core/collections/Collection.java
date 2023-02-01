package co.tsyba.core.collections;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A container, which allows iteration over its items.
 * <p>
 * Unlike more general {@link Iterable}, {@link Collection} guarantees iteration will not
 * destructively consume it. Therefore, it can be iterated any number of times. This
 * requirement enables useful operations, which can be based solely on iterability.
 * <p>
 * Note, that {@link Collection} does not guarantee that relative item order stays the
 * same in each iteration.
 */
public interface Collection<T> extends Iterable<T> {
	/**
	 * Returns {@code true} when this collection is empty; returns {@code false}
	 * otherwise.
	 */
	default boolean isEmpty() {
		final var iterator = iterator();
		return !iterator.hasNext();
	}

	/**
	 * Returns the number of items in this collection.
	 */
	default int getCount() {
		var count = 0;
		for (var ignored : this) {
			count += 1;
		}

		return count;
	}

	/**
	 * Returns the smallest item in this collection, according to the specified
	 * {@link Comparator}.
	 * <p>
	 * When this collection is empty, returns an empty {@link Optional}.
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
	 * Returns the largest item in this collection, according to the specified
	 * {@link Comparator}.
	 * <p>
	 * When this collection is empty, returns an empty {@link Optional}.
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
	 * Returns {@code true} when this collection contains the specified item; returns
	 * {@code false} otherwise.
	 */
	default boolean contains(T item) {
		return match((storedItem) -> storedItem.equals(item))
			.isPresent();
	}

	/**
	 * Returns {@code true} when this collection contains every item of the specified
	 * collection; returns {@code false} otherwise.
	 */
	default boolean contains(Collection<T> items) {
		return items.eachMatches(this::contains);
	}

	/**
	 * Returns any item in this collection, which satisfies the specified
	 * {@link Predicate}.
	 * <p>
	 * When this collection is empty or no item in satisfies the specified
	 * {@link Predicate}, returns an empty {@link Optional}.
	 */
	default Optional<T> match(Predicate<T> condition) {
		for (var item : this) {
			if (condition.test(item)) {
				return Optional.of(item);
			}
		}

		return Optional.empty();
	}

	/**
	 * Returns {@code true} when no item in this collection satisfies the specified
	 * {@link Predicate}; returns {@code false} otherwise.
	 * <p>
	 * When this collection is empty, returns {@code true}.
	 */
	default boolean noneMatches(Predicate<T> condition) {
		return match(condition)
			.isEmpty();
	}

	/**
	 * Returns {@code true} when at least one item in this collection satisfies the
	 * specified {@link Predicate}; returns {@code false} otherwise.
	 * <p>
	 * When this collection is empty, returns {@code true}.
	 */
	default boolean anyMatches(Predicate<T> condition) {
		return match(condition)
			.isPresent();
	}

	/**
	 * Returns {@code true} when each item of this collection satisfies the specified
	 * {@link Predicate}; returns {@code false} otherwise.
	 * <p>
	 * When this collection is empty, returns {@code true}.
	 */
	default boolean eachMatches(Predicate<T> condition) {
		return match((item) -> !condition.test(item))
			.isEmpty();
	}

	/**
	 * Returns distinct items in this collection.
	 */
	Collection<T> getDistinct();

	/**
	 * Returns items of this collection, ordered according to the specified
	 * {@link Comparator}.
	 */
	List<T> sort(Comparator<T> comparator);

	/**
	 * Applies the specified {@link Consumer} to every item of this collection.
	 *
	 * @return itself
	 */
	default Collection<T> iterate(Consumer<T> operation) {
		for (var item : this) {
			operation.accept(item);
		}

		return this;
	}

	/**
	 * Returns items of this collection, which satisfy the specified {@link Predicate}.
	 */
	Collection<T> filter(Predicate<T> condition);

	/**
	 * Returns items of this collection, converted by the specified {@link Function}.
	 */
	<R> Collection<R> convert(Function<T, R> converter);

	/**
	 * Combines this collection into a single value by applying the specified
	 * {@link BiFunction} to every item and an intermediate combination, using the
	 * specified value as a starting point.
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

// created on May 12, 2019
