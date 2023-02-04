package co.tsyba.core.collections;

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
}
