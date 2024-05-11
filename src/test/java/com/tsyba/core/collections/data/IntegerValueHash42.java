package com.tsyba.core.collections.data;

/*
 * Created by Serge Tsyba <serge.tsyba@tsyba.com> on Jan 7, 2019.
 */
public class IntegerValueHash42 {
	public final int value;

	public IntegerValueHash42(int value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		return 42;
	}

	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		else if (object == null) {
			return false;
		}
		else if (object instanceof IntegerValueHash42) {
			final var integerValue = (IntegerValueHash42) object;
			return value == integerValue.value;
		}
		else {
			return false;
		}
	}
}
