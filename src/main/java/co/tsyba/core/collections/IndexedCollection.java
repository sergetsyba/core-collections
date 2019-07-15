package co.tsyba.core.collections;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jul 3, 2019.
 */
public interface IndexedCollection<T> extends Collection<T> {
	Iterator<T> reverseIterator();

	/**
	 * Returns the first item in this collection. Returns an empty
	 * {@link Optional} when this collection is empty.
	 *
	 * @return
	 */
	public default Optional<T> getFirst() {
		final var iterator = iterator();
		if (!iterator.hasNext()) {
			return Optional.empty();
		}

		final var item = iterator.next();
		return Optional.of(item);
	}

	/**
	 * Returns the last item in this collection. Returns an empty
	 * {@link Optional} when this collection is empty.
	 *
	 * @return
	 */
	public default Optional<T> getLast() {
		final var iterator = reverseIterator();
		if (!iterator.hasNext()) {
			return Optional.empty();
		}

		final var item = iterator.next();
		return Optional.of(item);
	}

	/**
	 * Applies the specified {@link Consumer} to every item and its index in
	 * this collection.
	 *
	 * Returns itself.
	 *
	 * @param operation
	 * @return
	 */
	public default IndexedCollection<T> enumerate(BiConsumer<Integer, T> operation) {
		var index = 0;
		for (var item : this) {
			operation.accept(index, item);
			index += 1;
		}

		return this;
	}
}
