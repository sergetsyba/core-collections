package co.tsyba.core.collections;

import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An immutable, unordered {@link Collection} of unique items.
 */
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
		if (item == null) {
			return false;
		}

		final var index = find(item);
		return index > -1;
	}

	/**
	 * Returns {@code true} when this set is disjoint from the specified one; returns
	 * {@code false} otherwise.
	 * <p>
	 * Two sets are disjoint when they don't contain any common items.
	 * <p>
	 * When this or the specified set is empty, returns {@code false}, since an empty set
	 * is disjoint from all other sets, including itself.
	 */
	public boolean isDisjoint(Set<T> set) {
		return noneMatches(set::contains);
	}

	/**
	 * Returns union (A∪B) of this set and the specified one.
	 * <p>
	 * A union of two sets contains all (distinct) items from both sets.
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
	 * Returns {@code true} when this set intersects the specified one; returns
	 * {@code false} otherwise.
	 * <p>
	 * A set intersects another set when they have at least one common item.
	 * <p>
	 * When this or the specified set is empty, returns {@code false}, since an empty set
	 * never intersects another set, including itself.
	 */
	public boolean intersects(Set<T> set) {
		return anyMatches(set::contains);
	}

	/**
	 * Returns intersection (A∩B) of this set and the specified one.
	 * <p>
	 * An intersection of two sets contains all of their common items.
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
	 * Returns difference (A\B) of this set from the specified one.
	 * <p>
	 * A difference of a set from another set contains all items from the first set,
	 * except those, which are common with the other set.
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
	 * Returns symmetric difference (AΔB = (A\B)∪(B\A)) between this set and the specified
	 * one.
	 * <p>
	 * A symmetric difference of a set with another set contains all (distinct) items from
	 * both sets, except those, which are common between them.
	 */
	public Set<T> symmetricSubtract(Set<T> set) {
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

	/**
	 * Returns cartesian product (A×B) of this set and the specified one.
	 * <p>
	 * A cartesian product contains all possible (distinct) pairs of items from both
	 * sets.
	 */
	public <R> Set<Pair<T, R>> multiply(Set<R> set) {
		final var capacity = getCount() * set.getCount();
		final var product = new Set<Pair<T, R>>(capacity);

		for (var item1 : this) {
			for (var item2 : set) {
				final var pair = new Pair<>(item1, item2);
				product.insert(pair);
			}
		}

		product.removeExcessCapacity();
		return product;
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

	/**
	 * Returns items of this set as a {@link java.util.Set}.
	 */
	public java.util.Set<T> bridge() {
		final var set = new HashSet<T>();
		for (var item : this) {
			set.add(item);
		}

		return set;
	}

	@Override
	public String toString() {
		return "[" + join(", ") + "]";
	}
}
