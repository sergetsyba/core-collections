package co.tsyba.core.collections;

import java.util.function.BiConsumer;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Aug 13, 2019.
 */
public class Lists {
	private Lists() {
		assert false;
	}

	public static <K, V> void iterate(Iterable<K> iterable1, Iterable<V> iterable2,
			BiConsumer<K, V> operation) {

		final var iterator1 = iterable1.iterator();
		final var iterator2 = iterable2.iterator();

		while (iterator1.hasNext() && iterator2.hasNext()) {
			final var item1 = iterator1.next();
			final var item2 = iterator2.next();
			operation.accept(item1, item2);
		}
	}
}
