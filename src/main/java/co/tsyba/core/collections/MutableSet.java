package co.tsyba.core.collections;

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

	/**
	 * Adds the specified item to this set. Returns itself.
	 * <p>
	 * When the specified item is {@code null}, does nothing.
	 */
	public MutableSet<T> add(T item) {
		if (item != null) {
			insert(item);
		}

		return this;
	}

	/**
	 * Adds the specified items to this set. Returns itself.
	 * <p>
	 * Ignores any {@code null} values among the specified items.
	 */
	public MutableSet<T> add(T... items) {
		for (var item : items) {
			if (item != null) {
				insert(item);
			}
		}

		return this;
	}

	/**
	 * Adds the specified items to this set. Returns itself.
	 */
	public MutableSet<T> add(Collection<T> items) {
		for (var item : items) {
			insert(item);
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
				insert(item);
			}
		}

		return this;
	}

	/**
	 * Removed the specified item from this set, if present. Returns itself.
	 * <p>
	 * When the specified item is {@code null}, does nothing.
	 */
	public MutableSet<T> remove(T item) {
		if (item != null) {
			delete(item);
		}

		return this;
	}
}
