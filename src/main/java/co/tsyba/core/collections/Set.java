package co.tsyba.core.collections;

import java.util.Comparator;
import java.util.Iterator;
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

	@Override
	public int getCount() {
		return entryCount;
	}

	@Override
	public Collection<T> getDistinct() {
		return this;
	}

	@Override
	public List<T> sort(Comparator<T> comparator) {
		return null;
	}

	@Override
	public Collection<T> filter(Predicate<T> condition) {
		return null;
	}

	@Override
	public <R> Collection<R> convert(Function<T, R> converter) {
		return null;
	}

	@Override
	public Iterator<T> iterator() {
		return null;
	}
}
