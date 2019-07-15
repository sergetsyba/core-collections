package co.tsyba.core.collections;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

/*
 * Created by Serge Tsyba (tsyba@me.com) on May 12, 2019.
 */
public interface Collection<T> extends Iterable<T> {
	/**
	 * Returns {@code true} when this collection is empty; returns {@code false}
	 * otherwise.
	 *
	 * @return
	 */
	public default boolean isEmpty() {
		return !iterator()
				.hasNext();
	}

	/**
	 * Returns the number of items in this collection.
	 *
	 * @return
	 */
	public default int getCount() {
		var itemCount = 0;
		for (var item : this) {
			itemCount += 1;
		}

		return itemCount;
	}

	/**
	 * Returns {@code true} when this collection contains the specified item;
	 * returns {@code false} otherwise.
	 *
	 * @param item
	 * @return
	 */
	public default boolean contains(T item) {
		return find(item)
				.isPresent();
	}

	/**
	 * Returns index of the first occurrence of the specified item in this
	 * collection. Returns an empty {@link Optional} when the specified item
	 * does not occur in this collection.
	 *
	 * @param item
	 * @return
	 */
	public default Optional<Integer> find(T item) {
		return match(storedItem -> storedItem.equals(item));
	}

	/**
	 * Returns index of the first occurrence of the specified item which matches
	 * the specified {@link Predicate} in this collection. Returns an empty
	 * {@link Optional} when no item matches the specified {@link Predicate} in
	 * this collection.
	 *
	 * @param predicate
	 * @return
	 */
	public default Optional<Integer> match(Predicate<T> predicate) {
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
	 * Returns {@code true} when every item in this collection satisfies the
	 * specified {@link Predicate}; returns {@code false} otherwise.
	 *
	 * @param condition
	 * @return
	 */
	public default boolean eachMatches(Predicate<T> condition) {
		for (var item : this) {
			if (!condition.test(item)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns {@code true} when not a single item in this collection satisfies
	 * the specified {@link Predicate}; returns {@code false} otherwise.
	 *
	 * @param condition
	 * @return
	 */
	public default boolean noneMatches(Predicate<T> condition) {
		for (var item : this) {
			if (condition.test(item)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns the smallest item in this collection, according to the specified
	 * {@link Comparator}. Returns an empty {@link Optional} when this
	 * collection is empty.
	 *
	 * @param comparator
	 * @return
	 */
	public default Optional<T> getMinimum(Comparator<T> comparator) {
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
	 * {@link Comparator}. Returns an empty {@link Optional} when this
	 * collection is empty.
	 *
	 * @param comparator
	 * @return
	 */
	public default Optional<T> getMaximum(Comparator<T> comparator) {
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
	 * Applies the specified {@link Consumer} to every item in this collection.
	 *
	 * Returns itself.
	 *
	 * @param operation
	 * @return
	 */
	public default Collection<T> iterate(Consumer<T> operation) {
		for (var item : this) {
			operation.accept(item);
		}

		return this;
	}

	/**
	 * Combines this collection into a single value by applying the specified
	 * {@link BiFunction} to every item, starting from the specified initial
	 * value.
	 *
	 * @param <R>
	 * @param initial
	 * @param combiner
	 * @return
	 */
	public default <R> R combine(R initial, BiFunction<R, T, R> combiner) {
		var combination = initial;
		for (var item : this) {
			combination = combiner.apply(combination, item);
		}

		return combination;
	}
}
