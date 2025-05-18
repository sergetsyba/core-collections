package com.tsyba.core.collections.converter;

import com.tsyba.core.collections.Collection;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;

public class NonComparableCollection implements Collection<Predicate<?>> {
	private final Predicate<?>[] items;

	public NonComparableCollection() {
		this.items = new Predicate[]{
			(item) -> ((String) item).isBlank(),
			(item) -> ((String) item).isEmpty()
		};
	}

	@Override
	public Collection<Predicate<?>> matchAll(Predicate<Predicate<?>> condition) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<Predicate<?>> getDistinct() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <R> Collection<R> convert(Function<Predicate<?>, R> converter) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Predicate<?>> iterator() {
		return Arrays.asList(items)
			.iterator();
	}
}
