package com.tsyba.core.collections;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A container, which provides non-destructive iterations over its items.
 * <p>
 * Unlike a more general {@link Iterable}, {@link Collection} guarantees that an iteration
 * does not destructively consume it. This property enables unlimited iterations over
 * {@link Collection}'s items.
 * <p>
 * However, unlike {@link Sequence}, {@link Collection} does not guarantee that relative
 * item order stays the same in each iteration.
 */
public interface Collection<T> extends Iterable<T> {
	/**
	 * Returns {@code true} when this collection is empty; returns {@code false}
	 * otherwise.
	 */
	default boolean isEmpty() {
		final var itemCount = getCount();
		return itemCount == 0;
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
	 *
	 * @throws UnsupportedOperationException when items of this collection are not
	 * {@link Comparable}
	 */
	default Optional<T> getMin(Comparator<T> comparator) {
		final var iterator = iterator();
		if (!iterator.hasNext()) {
			return Optional.empty();
		}

		var min = iterator.next();
		if (!(min instanceof Comparable)) {
			throw new UnsupportedOperationException("Collection items are not comparable.");
		}

		while (iterator.hasNext()) {
			final var item = iterator.next();
			if (comparator.compare(min, item) > 0) {
				min = item;
			}
		}

		return Optional.of(min);
	}

	/**
	 * Returns the smallest item in this collection, according to the natural order of the
	 * items.
	 *
	 * @throws UnsupportedOperationException when items of this collection are not
	 * {@link Comparable}
	 */
	default Optional<T> getMin() {
		@SuppressWarnings("unchecked")
		final var comparator = (Comparator<T>) Comparator.naturalOrder();
		return getMin(comparator);
	}

	/**
	 * Returns the largest item in this collection, according to the specified
	 * {@link Comparator}.
	 * <p>
	 * When this collection is empty, returns an empty {@link Optional}.
	 *
	 * @throws UnsupportedOperationException when items of this collection are not
	 * {@link Comparable}
	 */
	default Optional<T> getMax(Comparator<T> comparator) {
		final var iterator = iterator();
		if (!iterator.hasNext()) {
			return Optional.empty();
		}

		var max = iterator.next();
		if (!(max instanceof Comparable)) {
			throw new UnsupportedOperationException("Collection items are not comparable.");
		}

		while (iterator.hasNext()) {
			final var item = iterator.next();
			if (comparator.compare(max, item) < 0) {
				max = item;
			}
		}

		return Optional.of(max);
	}

	/**
	 * Returns the largest item in this collection, according to the natural order of the
	 * items.
	 *
	 * @throws UnsupportedOperationException when items of this collection are not
	 * {@link Comparable}
	 */
	default Optional<T> getMax() {
		@SuppressWarnings("unchecked")
		final var comparator = (Comparator<T>) Comparator.naturalOrder();
		return getMax(comparator);
	}

	/**
	 * Returns {@code true} when this collection contains the specified item; returns
	 * {@code false} otherwise.
	 */
	default boolean contains(T item) {
		return matchAny((item2) -> item2.equals(item))
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
	 * Returns {@code true} when no item in this collection satisfies the specified
	 * {@link Predicate}; returns {@code false} otherwise.
	 * <p>
	 * When this collection is empty, returns {@code true}.
	 */
	default boolean noneMatches(Predicate<T> condition) {
		return matchAny(condition)
			.isEmpty();
	}

	/**
	 * Returns {@code true} when at least one item in this collection satisfies the
	 * specified {@link Predicate}; returns {@code false} otherwise.
	 * <p>
	 * When this collection is empty, returns {@code false}.
	 */
	default boolean anyMatches(Predicate<T> condition) {
		return matchAny(condition)
			.isPresent();
	}

	/**
	 * Returns {@code true} when each item of this collection satisfies the specified
	 * {@link Predicate}; returns {@code false} otherwise.
	 * <p>
	 * When this collection is empty, returns {@code true}.
	 */
	default boolean eachMatches(Predicate<T> condition) {
		return matchAny((item) -> !condition.test(item))
			.isEmpty();
	}

	/**
	 * Returns the number of items in this collection, which satisfies the specified
	 * {@link Predicate}.
	 */
	default int countMatches(Predicate<T> condition) {
		var count = 0;
		for (var item : this) {
			if (condition.test(item)) {
				count += 1;
			}
		}

		return count;
	}

	/**
	 * Returns any item in this collection, which satisfies the specified
	 * {@link Predicate}.
	 * <p>
	 * When no item in this collection satisfies the specified {@link Predicate}, or this
	 * collection is empty, returns an empty {@link Optional}.
	 */
	default Optional<T> matchAny(Predicate<T> condition) {
		for (var item : this) {
			if (condition.test(item)) {
				return Optional.of(item);
			}
		}

		return Optional.empty();
	}

	/**
	 * Returns all items of this collection, which satisfy the specified
	 * {@link Predicate}.
	 */
	Collection<T> matchAll(Predicate<T> condition);

	/**
	 * Returns distinct items in this collection.
	 */
	Collection<T> getDistinct();

	/**
	 * Returns items of this collection, ordered according to the specified
	 * {@link Comparator}.
	 */
	default Sequence<T> sort(Comparator<T> comparator) {
		@SuppressWarnings("unchecked")
		final var items = (T[]) toArray();
		Arrays.sort(items, comparator);

		final var store = new ContiguousArrayStore(items, items.length);
		return new List<>(store);
	}

	/**
	 * Returns items of this collection, ordered according to their natural order.
	 *
	 * @throws RuntimeException when items of this collection are not {@link Comparable}.
	 */
	default Sequence<T> sort() {
		@SuppressWarnings("unchecked")
		final var comparator = (Comparator<T>) Comparator.naturalOrder();
		return sort(comparator);
	}

	/**
	 * Returns items of this collection in random order, based on the specified
	 * {@link Random}.
	 */
	default Sequence<T> shuffle(Random random) {
		final var items = toArray();
		shuffle(items, random);

		final var store = new ContiguousArrayStore(items, items.length);
		return new List<>(store);
	}

	/**
	 * Returns items of this collection in random order, where randomization is seeded
	 * from the current system time.
	 */
	default Sequence<T> shuffle() {
		final var time = System.currentTimeMillis();
		final var random = new Random(time);

		return shuffle(random);
	}

	/**
	 * Shuffles the specified items randomly, based on the specified {@link Random}.
	 *
	 * <pre>
	 * Implements in-place version of Fisher-Yates shuffle algorithm.
	 *
	 * Sources:
	 * 1. R. Durstenfeld. "Algorithm 235: Random permutation".
	 *    Communications of the ACM, vol. 7, July 1964, p. 420.
	 * 2. D. Knuth. "The Art of Computer Programming" vol. 2.
	 *    Addison–Wesley, 1969, pp. 139–140, algorithm P.
	 * </pre>
	 */
	private static <T> void shuffle(T[] items, Random random) {
		// it's more convenient to iterate items backwards for simpler
		// random index generation
		for (var index = items.length - 1; index >= 0; --index) {
			final var randomIndex = random.nextInt(index + 1);

			// swap items at iterated and randomly generated indices
			final var item = items[index];
			items[index] = items[randomIndex];
			items[randomIndex] = item;
		}
	}

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
	 * Returns items of this collection, converted by the specified {@link Function}.
	 * <p>
	 * When the specified {@link Function} returns {@code null}, the converted value will
	 * be ignored. Therefore, this method can be used to both filter and convert this
	 * collection in a single operation.
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

	/**
	 * Returns items of this collection in an array.
	 */
	default Object[] toArray() {
		final var count = getCount();
		final var items = new Object[count];
		var index = 0;

		for (var item : this) {
			items[index] = item;
			++index;
		}

		return items;
	}

	/**
	 * Returns items of this collection in a typed array.
	 */
	default T[] toArray(Class<? extends T[]> klass) {
		final var count = getCount();
		final var items = Arrays.copyOf(new Object[0], count, klass);

		var index = 0;
		for (var item : this) {
			items[index] = item;
			++index;
		}

		return items;
	}
}

// created on May 12, 2019
