package co.tsyba.core.collections;

import static java.lang.String.format;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Apr 14, 2019.
 */
public class IndexRangeNotInRangeException extends RuntimeException {
	private final IndexRange indexSubRange;
	private final IndexRange indexRange;

	public IndexRangeNotInRangeException(IndexRange indexSubRange, IndexRange indexRange) {
		this.indexSubRange = indexSubRange;
		this.indexRange = indexRange;
	}

	@Override
	public String getMessage() {
		return format("Index range %s is out of valid range %s.",
				indexSubRange, indexRange);
	}
}
