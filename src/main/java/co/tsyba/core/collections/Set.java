package co.tsyba.core.collections;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;

public class Set<T> extends RobinHoodHashStore<T> implements Collection<T> {
	/**
	 * Creates a copy of the specified {@link Collection}.
	 */
	public Set(Collection<T> items) {
		super(items.getCount());
		for (var item : items) {
			insert(item);
		}
	}

	/**
	 * Creates a set with the specified items.
	 * <p>
	 * Ignores any {@code null} values among the specified items.
	 */
	@SafeVarargs
	public Set(T... items) {
		super(items.length);
		for (var item : items) {
			if (item != null) {
				insert(item);
			}
		}
	}

	/**
	 * Creates a set with the specified items.
	 * <p>
	 * Ignores any {@code null} values among the specified items.
	 */
	public Set(Iterable<T> items) {
		super(64);
		for (var item : items) {
			if (item != null) {
				insert(item);
			}
		}
	}

	/**
	 * Creates a set with the specified item capacity.
	 */
	private Set(int capacity) {
		super(capacity);
	}

	@Override
	public int getCount() {
		return entryCount;
	}

	@Override
	public boolean contains(T item) {
		final var index = find(item);
		return index > -1;
	}

	/**
	 * Returns union (A∪B) of this set and the specified one.
	 * <p>
	 * The returned set contains all items from this and the specified sets.
	 */
	public Set<T> unite(Set<T> set) {
		final var capacity = getCount() + set.getCount();
		final var union = new Set<T>(capacity);

		for (var item : this) {
			union.insert(item);
		}
		for (var item : set) {
			union.insert(item);
		}

		union.removeExcessCapacity();
		return union;
	}

	/**
	 * Returns intersection (A∩B) of this set and the specified one.
	 * <p>
	 * The returned set contains items, which are present only in both this set and the
	 * specified one.
	 */
	public Set<T> intersect(Set<T> set) {
		final var capacity = getCount() + set.getCount();
		final var intersection = new Set<T>(capacity);

		for (var item : this) {
			if (set.contains(item)) {
				intersection.insert(item);
			}
		}

		intersection.removeExcessCapacity();
		return intersection;
	}

	/**
	 * Returns difference (A\B) of this set and the specified one.
	 * <p>
	 * The returned set contains items, which are present in this set, except those, which
	 * are present in the specified one.
	 */
	public Set<T> subtract(Set<T> set) {
		final var capacity = getCount() + set.getCount();
		final var difference = new Set<T>(capacity);

		for (var item : this) {
			if (!set.contains(item)) {
				difference.insert(item);
			}
		}

		difference.removeExcessCapacity();
		return difference;
	}

	/**
	 * Returns symmetric difference (AΔB = (A\B)∪(B\A)) of this set and the specified
	 * one.
	 * <p>
	 * The returned set contains items from this set, which are not present in the
	 * specified one, plus items which are present in the specified set, but not this
	 * one.
	 */
	public Set<T> disjoint(Set<T> set) {
		final var capacity = getCount() + set.getCount();
		final var difference = new Set<T>(capacity);

		for (var item : this) {
			if (!set.contains(item)) {
				difference.insert(item);
			}
		}
		for (var item : set) {
			if (!contains(item)) {
				difference.insert(item);
			}
		}

		difference.removeExcessCapacity();
		return difference;
	}

	@Override
	public List<T> sort(Comparator<T> comparator) {
		@SuppressWarnings("unchecked")
		final var items = (T[]) new Object[entryCount];
		var index = 0;

		for (var item : this) {
			items[index] = item;
			++index;
		}

		Arrays.sort(items, comparator);
		return new List<>(items);
	}

	@Override
	public Set<T> filter(Predicate<T> condition) {
		final var itemCount = getCount();
		final var filtered = new Set<T>(itemCount);

		for (var item : this) {
			if (condition.test(item)) {
				filtered.insert(item);
			}
		}

		removeExcessCapacity();
		return filtered;
	}

	@Override
	public <R> Set<R> convert(Function<T, R> converter) {
		final var itemCount = getCount();
		final var converted = new Set<R>(itemCount);

		for (var item : this) {
			final var item2 = converter.apply(item);
			if (item2 != null) {
				converted.insert(item2);
			}
		}

		removeExcessCapacity();
		return converted;
	}

	@Override
	public String toString() {
		return "[" + join(", ") + "]";
	}
}
