package co.tsyba.core.collections;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Feb 17, 2019.
 */
public class IndexNotInRangeException extends RuntimeException {
	public final int index;
	public final IndexRange validRange;

	public IndexNotInRangeException(int index, IndexRange validRange) {
		this.index = index;
		this.validRange = validRange;
	}

	@Override
	public String getMessage() {
		return validRange.isEmpty()
			? "Index " + index + " is out of valid empty range."
			: "Index " + index + " is out of valid range " + validRange + ".";
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof IndexNotInRangeException)) {
			return false;
		}

		final var exception = (IndexNotInRangeException) object;
		return index == exception.index
			&& validRange.equals(exception.validRange);
	}
}
