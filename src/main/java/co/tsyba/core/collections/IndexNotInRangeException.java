package co.tsyba.core.collections;

import static java.lang.String.format;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Feb 17, 2019.
 */
public class IndexNotInRangeException extends RuntimeException {
	private final int index;
	private final IndexRange indexRange;

	public IndexNotInRangeException(int index, IndexRange indexRange) {
		this.index = index;
		this.indexRange = indexRange;
	}

	@Override
	public String getMessage() {
		return format("Index %d is out of valid range %s.",
				index, indexRange);
	}
}
