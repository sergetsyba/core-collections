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
public interface Collection<T> extends Iterable<T> {
	/**
	 * Returns {@code true} when this collection is empty; returns {@code false}
	 * otherwise.
	 *
	 * @return
	 */
	public default boolean isEmpty() {
		return iterator()
				.hasNext() == false;
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
		return contains(storedItem -> storedItem.equals(item));
	}

	/**
	 * Returns {@code true} when this collection contains every item of the
	 * specified collection; returns {@code false} otherwise.
	 *
	 * @param items
	 * @return
	 */
	public default boolean contains(Collection<T> items) {
		return items.matches(this::contains);
	}

	/**
	 * Returns {@code true} when this collection contains an item which
	 * satisfies the specified {@link Predicate}; returns {@code false}
	 * otherwise.
	 *
	 * @param condition
	 * @return
	 */
	public default boolean contains(Predicate<T> condition) {
		for (var item : this) {
			if (condition.test(item)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns {@code true} when every item of this collection satisfies the
	 * specified {@link Predicate}; returns {@code false} otherwise.
	 *
	 * @param condition
	 * @return
	 */
	public default boolean matches(Predicate<T> condition) {
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
	 * Returns the largest item of this collection, according to the specified
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
	 * Returns distinct items of this collection.
	 *
	 * @return
	 */
	public Collection<T> getDistinct();

	/**
	 * Returns items of this collection, ordered according to the specified
	 * {@link Comparator}.
	 *
	 * @param comparator
	 * @return
	 */
	public IndexedCollection<T> sort(Comparator<T> comparator);

	/**
	 * Applies the specified {@link Consumer} to every item of this collection.
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
	 * Returns items of this collection which satisfy the specified
	 * {@link Predicate}.
	 *
	 * @param condition
	 * @return
	 */
	public Collection<T> filter(Predicate<T> condition);

	/**
	 * Returns items of this collection, converted by the specified
	 * {@link Function}.
	 *
	 * @param <R>
	 * @param converter
	 * @return
	 */
	public <R> Collection<R> convert(Function<T, R> converter);

	/**
	 * Combines this collection into a single value by applying the specified
	 * {@link BiFunction} to every item and an intermediate combination,
	 * starting from the specified initial value.
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

	/**
	 * Combines this collection into a {@link String} by joining its items with
	 * the specified separator between them.
	 *
	 * @param separator
	 * @return
	 */
	public default String join(String separator) {
		final var builder = new StringBuilder();
		final var iterator = iterator();

		if (iterator.hasNext()) {
			final var item = iterator.next();
			builder.append(item);
		}

		while (iterator.hasNext()) {
			final var item = iterator.next();
			builder.append(separator);
			builder.append(item);
		}

		return builder.toString();
	}
}
