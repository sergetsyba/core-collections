package co.tsyba.core.collections;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Jul 3, 2019.
 */
public interface OrderedCollection<T> extends Collection<T> {
    /**
     * Returns the first item of this collection.
     *
     * When this collection is empty, returns an empty {@link Optional}.
     *
     * @return
     */
    default Optional<T> getFirst() {
        final var iterator = iterator();
        if (!iterator.hasNext()) {
            return Optional.empty();
        }

        final var item = iterator.next();
        return Optional.of(item);
    }

    /**
     * Returns the last item of this collection.
     *
     * When this collection is empty, returns an empty {@link Optional}.
     *
     * @return
     */
    default Optional<T> getLast() {
        final var iterator = reverseIterator();
        if (!iterator.hasNext()) {
            return Optional.empty();
        }

        final var item = iterator.next();
        return Optional.of(item);
    }

    /**
     * Returns {@code true} when the this collection contains the specified
     * items, accounting for their order; returns {@code false} otherwise.
     *
     * @param items
     * @return
     */
    default boolean contains(OrderedCollection<T> items) {
        return find(items)
                .isPresent();
    }

    /**
     * Returns index of the first occurrence of the specified item in this
     * collection.
     *
     * Returns an empty {@link Optional} when the specified item is not in this
     * collection.
     *
     * @param item
     * @return
     */
    default Optional<Integer> find(T item) {
        return match(storedItem -> storedItem.equals(item));
    }

    /**
     * Returns index of the first occurrence of the specified items in this
     * collection.
     *
     * Returns an empty {@link Optional} when the specified items are not in
     * this collection.
     *
     * @param items
     * @return
     */
    default Optional<Integer> find(OrderedCollection<T> items) {
        var iterator1 = iterator();
        if (!iterator1.hasNext()) {
            // this collection is empty
            return Optional.empty();
        }

        indexSearch:
        for (var index = 0;;) {
            final var iterator2 = items.iterator();

            while (iterator2.hasNext()) {
                final var item1 = iterator1.next();
                final var item2 = iterator2.next();

                if (!item1.equals(item2)) {
                    // found different items
                    if (iterator1.hasNext()) {
                        // there are more items after the differnt one in
                        // this collection, restart search from the next
                        // index
                        iterator1 = iterator(++index);
                        continue indexSearch;
                    } else {
                        // there are no more items after the different one
                        // in this collection, but more in the specified
                        // collection; i.e. the specified collection extends
                        // beyond this one, thus it is not contained
                        return Optional.empty();
                    }
                }
            }

            // reached end of the specified collection mathcing all items
            // with items in this collection
            return Optional.of(index);
        }
    }

    /**
     * Returns index of the first occurrence of an item in this collection,
     * which satisfies the specified {@link Predicate}.
     *
     * Returns an empty {@link Optional} when no item in this collection
     * satisfies the specified {@link Predicate}.
     *
     * @param predicate
     * @return
     */
    default Optional<Integer> match(Predicate<T> predicate) {
        var index = 0;
        for (var item : this) {
            if (predicate.test(item)) {
                return Optional.of(index);
            }

            index += 1;
        }

        return Optional.empty();
    }

    /**
     * Returns items of this collection in reverse order.
     *
     * @return
     */
    OrderedCollection<T> reverse();

    /**
     * Returns items of this collection in random order.
     *
     * @return
     */
    OrderedCollection<T> shuffle();

    /**
     * Applies the specified {@link Consumer} to every item and its index in
     * this collection.
     *
     * Returns itself.
     *
     * @param operation
     * @return
     */
    default OrderedCollection<T> enumerate(BiConsumer<T, Integer> operation) {
        var index = 0;
        for (var item : this) {
            operation.accept(item, index);
            index += 1;
        }

        return this;
    }

    /**
     * Returns iterator over items of this collection, starting from the
     * specified index.
     *
     * @param startIndex
     * @return
     */
    Iterator<T> iterator(int startIndex);

    /**
     * Returns iterator over items of this collection in reverse order.
     *
     * @return
     */
    Iterator<T> reverseIterator();
}