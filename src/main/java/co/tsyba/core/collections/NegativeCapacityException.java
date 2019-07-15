package co.tsyba.core.collections;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Feb 16, 2019.
 */
public class NegativeCapacityException extends RuntimeException {
	public NegativeCapacityException(int capacity) {
		super("Capacity is negative: " + capacity + ".");
	}
}
