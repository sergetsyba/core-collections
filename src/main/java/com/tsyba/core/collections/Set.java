package com.tsyba.core.collections;

import java.util.HashSet;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An immutable, unordered {@link Collection} of unique items.
 */
public class Set<T> implements Collection<T> {
	RobinHoodHashStore<T> store;

	/**
	 * Creates a set with the specified item store.
	 */
	Set(RobinHoodHashStore<T> store) {
		this.store = store;
	}

	/**
	 * Creates a copy of the specified {@link Collection}.
	 */
	public Set(Collection<T> items) {
		this.store = new RobinHoodHashStore<>(
			items.getCount());

		for (var item : items) {
			this.store.insert(item);
		}
	}

	/**
	 * Creates a set with the specified items.
	 * <p>
	 * Ignores any {@code null} values among the specified items.
	 */
	@SafeVarargs
	public Set(T... items) {
		this.store = new RobinHoodHashStore<>(items.length);

		for (var item : items) {
			if (item != null) {
				store.insert(item);
			}
		}
	}

	/**
	 * Creates a set with the specified items.
	 * <p>
	 * Ignores any {@code null} values among the specified items.
	 */
	public Set(Iterable<T> items) {
		this.store = new RobinHoodHashStore<>(64);

		for (var item : items) {
			if (item != null) {
				store.insert(item);
			}
		}
	}

	@Override
	public int getCount() {
		return store.entryCount;
	}

	@Override
	public Set<T> getDistinct() {
		return this;
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
	 * Returns union (A∪B) of this set and the specified one.
	 * <p>
	 * A union of two sets contains all (distinct) items from both sets.
	 */
	public Set<T> unite(Set<T> set) {
		final var capacity = getCount() + set.getCount();
		final var store = new RobinHoodHashStore<T>(capacity);

		for (var item : this) {
			store.insert(item);
		}
		for (var item : set) {
			store.insert(item);
		}

		store.removeExcessCapacity();
		return new Set<>(store);
	}

	/**
	 * Returns intersection (A∩B) of this set and the specified one.
	 * <p>
	 * An intersection of two sets contains all of their common items.
	 */
	public Set<T> intersect(Set<T> set) {
		final var capacity = getCount() + set.getCount();
		final var store = new RobinHoodHashStore<T>(capacity);

		for (var item : this) {
			if (set.contains(item)) {
				store.insert(item);
			}
		}

		store.removeExcessCapacity();
		return new Set<>(store);
	}

	/**
	 * Returns difference (A\B) of this set from the specified one.
	 * <p>
	 * A difference of a set from another set contains all items from the first set,
	 * except those, which are common with the other set.
	 */
	public Set<T> subtract(Set<T> set) {
		final var capacity = getCount() + set.getCount();
		final var store = new RobinHoodHashStore<T>(capacity);

		for (var item : this) {
			if (!set.contains(item)) {
				store.insert(item);
			}
		}

		store.removeExcessCapacity();
		return new Set<>(store);
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
		final var store = new RobinHoodHashStore<T>(capacity);

		for (var item : this) {
			if (!set.contains(item)) {
				store.insert(item);
			}
		}
		for (var item : set) {
			if (!contains(item)) {
				store.insert(item);
			}
		}

		store.removeExcessCapacity();
		return new Set<>(store);
	}

	/**
	 * Returns cartesian product (A×B) of this set and the specified one.
	 * <p>
	 * A cartesian product contains all possible (distinct) pairs of items from both
	 * sets.
	 */
	public <R> Set<Pair<T, R>> multiply(Set<R> set) {
		final var capacity = getCount() * set.getCount();
		final var store = new RobinHoodHashStore<Pair<T, R>>(capacity);

		for (var item1 : this) {
			for (var item2 : set) {
				final var pair = new Pair<>(item1, item2);
				store.insert(pair);
			}
		}

		store.removeExcessCapacity();
		return new Set<>(store);
	}

	@Override
	public Set<T> matchAll(Predicate<T> condition) {
		final var itemCount = getCount();
		final var store = new RobinHoodHashStore<T>(itemCount);

		for (var item : this) {
			if (condition.test(item)) {
				store.insert(item);
			}
		}

		store.removeExcessCapacity();
		return new Set<>(store);
	}

	@Override
	public Set<T> iterate(Consumer<T> operation) {
		return (Set<T>) Collection.super.iterate(operation);
	}

	@Override
	public <R> Set<R> convert(Function<T, R> converter) {
		final var itemCount = getCount();
		final var store = new RobinHoodHashStore<R>(itemCount);

		for (var item : this) {
			final var item2 = converter.apply(item);
			if (item2 != null) {
				store.insert(item2);
			}
		}

		store.removeExcessCapacity();
		return new Set<>(store);
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
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Set)) {
			return false;
		}

		final var set = (Set<?>) object;
		return store.equals(set.store);
	}

	@Override
	public int hashCode() {
		return store.hashCode();
	}

	@Override
	public String toString() {
		return "{" + join(", ") + "}";
	}

	@Override
	public Iterator<T> iterator() {
		return store.iterator();
	}
}
