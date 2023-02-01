package co.tsyba.core.collections;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Feb 17, 2019.
 */
public class IndexNotInRangeException extends RuntimeException {
	public final int index;
	public final IndexRange indexRange;

	public IndexNotInRangeException(int index, IndexRange indexRange) {
		this.index = index;
		this.indexRange = indexRange;
	}

	public IndexNotInRangeException(int index) {
		this.index = index;
		this.indexRange = null;
	}

	@Override
	public String getMessage() {
		return indexRange == null
			? "Index " + index + " is out of empty range."
			: "Index " + index + " is out of valid range " + indexRange + ".";
	}
}
