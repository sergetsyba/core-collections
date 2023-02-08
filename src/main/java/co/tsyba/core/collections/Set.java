package co.tsyba.core.collections;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;

public class Set<T> extends RobinHoodHashStore<T> implements Collection<T> {
	@SafeVarargs
	public Set(T... items) {
		super(items.length);
		for (var item : items) {
			if (item != null) {
				insert(item);
			}
		}
	}

	private Set(int capacity) {
		super(capacity);
	}

	@Override
	public int getCount() {
		return entryCount;
	}

	@Override
	public List<T> sort(Comparator<T> comparator) {
		return null;
	}

	@Override
	public Set<T> filter(Predicate<T> condition) {
		final var count = getCount();
		final var filtered = new Set<T>(count);

		for (var item : this) {
			if (condition.test(item)) {
				filtered.insert(item);
			}
		}

		// todo: remove excess capacity
		return filtered;
	}

	@Override
	public <R> Set<R> convert(Function<T, R> converter) {
		final var count = getCount();
		final var converted = new Set<R>(count);

		for (var item : this) {
			final var item2 = converter.apply(item);
			if (item2 != null) {
				converted.insert(item2);
			}
		}

		// todo: remove excess capacity
		return converted;
	}
}
