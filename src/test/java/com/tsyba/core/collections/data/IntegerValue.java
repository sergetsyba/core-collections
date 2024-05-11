package com.tsyba.core.collections.data;

/*
 * Created by Serge Tsyba <serge.tsyba@tsyba.com> on Dec 24, 2018.
 */
public class IntegerValue {
	public final int value;

	public IntegerValue(int value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		return value;
	}

	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		else if (object == null) {
			return false;
		}
		else if (object instanceof IntegerValue) {
			final var integerValue = (IntegerValue) object;
			return value == integerValue.value;
		}
		else {
			return false;
		}
	}
}
