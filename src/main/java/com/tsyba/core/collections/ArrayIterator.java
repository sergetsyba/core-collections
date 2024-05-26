package com.tsyba.core.collections;

import java.util.Iterator;

class ArrayIterator<T> implements Iterator<T> {
	private final T[] items;
	private int index;

	ArrayIterator(T[] items, int start) {
		this.items = items;
		this.index = start;
	}

	ArrayIterator(T[] items) {
		this.items = items;
		this.index = 0;
	}

	@Override
	public boolean hasNext() {
		return index < items.length;
	}

	@Override
	public T next() {
		final var next = items[index];
		index++;

		return next;
	}
}
