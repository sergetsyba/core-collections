package com.tsyba.core.collections.converter;

import com.tsyba.core.collections.Collection;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;

public class NonComparableCollection implements Collection<Object> {
	private final Object[] items;

	public NonComparableCollection(Object... items) {
		this.items = items;
	}

	@Override
	public Collection<Object> matchAll(Predicate<Object> condition) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<Object> getDistinct() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <R> Collection<R> convert(Function<Object, R> converter) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Object> iterator() {
		return Arrays.asList(items)
			.iterator();
	}

	@Override
	public String toString() {
		return Arrays.toString(items);
	}
}
