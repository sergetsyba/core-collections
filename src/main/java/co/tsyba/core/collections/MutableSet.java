package co.tsyba.core.collections;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A mutable, unordered {@link Collection} of unique items.
 */
public class MutableSet<T> extends Set<T> {
	@SafeVarargs
	public MutableSet(T... items) {
		super(items);
	}

	public MutableSet(Collection<T> items) {
		super(items);
	}

	public MutableSet(Iterable<T> items) {
		super(items);
	}

	@Override
	public MutableSet<T> unite(Set<T> set) {
		final var items = super.unite(set);
		return new MutableSet<>(items.store);
	}

	@Override
	public MutableSet<T> intersect(Set<T> set) {
		final var items = super.intersect(set);
		return new MutableSet<>(items.store);
	}

	@Override
	public MutableSet<T> subtract(Set<T> set) {
		final var items = super.subtract(set);
		return new MutableSet<>(items.store);
	}

	@Override
	public MutableSet<T> symmetricSubtract(Set<T> set) {
		final var items = super.symmetricSubtract(set);
		return new MutableSet<>(items.store);
	}

	@Override
	public <R> MutableSet<Pair<T, R>> multiply(Set<R> set) {
		final var items = super.multiply(set);
		return new MutableSet<>(items.store);
	}

	/**
	 * Adds the specified item to this set. Returns itself.
	 * <p>
	 * When the specified item is {@code null}, does nothing.
	 */
	public MutableSet<T> add(T item) {
		if (item != null) {
			store.insert(item);
		}

		return this;
	}

	/**
	 * Adds the specified items to this set. Returns itself.
	 * <p>
	 * Ignores any {@code null} values among the specified items.
	 */
	@SafeVarargs
	public final MutableSet<T> add(T... items) {
		for (var item : items) {
			if (item != null) {
				store.insert(item);
			}
		}

		return this;
	}

	/**
	 * Adds the specified items to this set. Returns itself.
	 */
	public MutableSet<T> add(Collection<T> items) {
		for (var item : items) {
			store.insert(item);
		}

		return this;
	}

	/**
	 * Adds the specified items to this set. Returns itself.
	 * <p>
	 * Ignores any {@code null} values among the specified items.
	 */
	public MutableSet<T> add(Iterable<T> items) {
		for (var item : items) {
			if (item != null) {
				store.insert(item);
			}
		}

		return this;
	}

	/**
	 * Removes the specified item from this set, if present. Returns itself.
	 * <p>
	 * When the specified item is {@code null}, does nothing.
	 */
	public MutableSet<T> remove(T item) {
		if (item != null) {
			store.delete(item);
		}

		return this;
	}

	/**
	 * Removes any of the specified items present in this set. Returns itself.
	 * <p>
	 * Ignores any {@code null} values among the specified items.
	 */
	@SafeVarargs
	public final MutableSet<T> remove(T... items) {
		for (var item : items) {
			if (item != null) {
				store.delete(item);
			}
		}

		return this;
	}

	/**
	 * Removes any of the specified items present in this set. Returns itself.
	 */
	public MutableSet<T> remove(Collection<T> items) {
		for (var item : items) {
			store.delete(item);
		}

		return this;
	}

	/**
	 * Removes the specified items from this set, if present. Returns itself.
	 * <p>
	 * Ignores any {@code null} values among the specified items.
	 */
	public MutableSet<T> remove(Iterable<T> items) {
		for (var item : items) {
			if (item != null) {
				store.delete(item);
			}
		}

		return this;
	}

	/**
	 * Removes all items from this set. Returns itself.
	 */
	public MutableSet<T> removeAll() {
		store.deleteAll();
		return this;
	}

	@Override
	public MutableSet<T> iterate(Consumer<T> operation) {
		return (MutableSet<T>) super.iterate(operation);
	}

	@Override
	public MutableSet<T> filter(Predicate<T> condition) {
		final var items = super.filter(condition);
		return new MutableSet<>(items.store);
	}

	@Override
	public <R> MutableSet<R> convert(Function<T, R> converter) {
		final var items = super.convert(converter);
		return new MutableSet<>(items.store);
	}

	/**
	 * Returns an immutable copy of this set.
	 */
	public Set<T> toImmutable() {
		return new Set<>(this);
	}
}
