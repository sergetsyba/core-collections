package com.tsyba.core.collections;

import java.util.Objects;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Apr 14, 2019.
 */
public class IndexRangeNotInRangeException extends RuntimeException {
	public final IndexRange indexRange;
	public final IndexRange validRange;

	public IndexRangeNotInRangeException(IndexRange indexRange, IndexRange validRange) {
		this.indexRange = indexRange;
		this.validRange = validRange;
	}

	@Override
	public String getMessage() {
		return validRange.isEmpty()
			? "Index range " + indexRange + " is out of empty range."
			: "Index range " + indexRange + " is out of valid range " + validRange + ".";
	}

	@Override
	public int hashCode() {
		return Objects.hash(indexRange, validRange);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof IndexRangeNotInRangeException)) {
			return false;
		}

		final var exception = (IndexRangeNotInRangeException) object;
		return indexRange.equals(exception.indexRange)
			&& validRange.equals(exception.validRange);
	}
}
