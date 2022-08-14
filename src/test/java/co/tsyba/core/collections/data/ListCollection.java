package co.tsyba.core.collections.data;

import co.tsyba.core.collections.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;
import co.tsyba.core.collections.OrderedCollection;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Aug 11, 2019.
 */
public class ListCollection<T> implements OrderedCollection<T> {
	private final java.util.List<T> items;

	public ListCollection(T... items) {
		this.items = java.util.List.of(items);
	}

	@Override
	public Collection<T> getDistinct() {
		throw new UnsupportedOperationException();
	}

	@Override
	public OrderedCollection<T> reverse() {
		throw new UnsupportedOperationException();
	}

	@Override
	public OrderedCollection<T> shuffle() {
		throw new UnsupportedOperationException();
	}

	@Override
	public OrderedCollection<T> sort(Comparator<T> comparator) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<T> filter(Predicate<T> condition) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <R> Collection<R> convert(Function<T, R> converter) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<T> iterator() {
		return items.iterator();
	}

	@Override
	public Iterator<T> iterator(int startIndex) {
		return items.listIterator(startIndex);
	}

    @Override
    public Iterator<T> reverseIterator() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
