package co.tsyba.core.collections;

import java.util.Objects;

/**
 * An ordered pair of items.
 */
public class Pair<T, R> {
	public final T item1;
	public final R item2;

	public Pair(T item1, R item2) {
		this.item1 = item1;
		this.item2 = item2;
	}

	@Override
	public int hashCode() {
		return Objects.hash(item1, item2);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Pair)) {
			return false;
		}

		final var pair = (Pair<?, ?>) object;
		return item1.equals(pair.item1)
			&& item2.equals(pair.item2);
	}

	@Override
	public String toString() {
		return "(" + item1 + ", " + item2 + ")";
	}
}
