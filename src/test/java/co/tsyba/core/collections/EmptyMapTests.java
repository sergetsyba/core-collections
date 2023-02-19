package co.tsyba.core.collections;

import org.junit.Test;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Sep 15, 2019.
 */
public class EmptyMapTests {
	final Map<String, String> emptyEntries = new Map<>();

	@Test
	public void checksEmpty() {
		assert emptyEntries.isEmpty();
	}

	@Test
	public void returnsEntryCount() {
		assert emptyEntries.getCount() == 0;
	}

	@Test
	public void checksKeyContainment() {
//		assert emptyEntries.contains("e") == false;
	}

	@Test
	public void checksEntryContainment() {
		assert emptyEntries.contains("r", "ananjau") == false;
	}

	@Test
	public void returnsValue() {
		assert emptyEntries.get("O")
				.isEmpty();
	}

	@Test
	public void returnsEntries() {
		final var entries1 = emptyEntries.get(
				new List<>("g", "e", "p"));

		assert entries1.equals(
				new Map<>());

		// empty keys
		final var entries2 = emptyEntries.get(
				new List<>());

		assert entries2.equals(
				new Map<>());
	}

	@Test
	public void filtersEntries() {
		// entries where values start with keys
//		assert emptyEntries.filter((key, value) -> value.startsWith(key))
//				.equals(new Map<>());
	}

	@Test
	public void collectsEntries() {
		// joins keys with values
		final var items1 = emptyEntries.collect((key, value) -> key + value);
		assert items1.equals(new List<>());

		// removes all values
		final var items2 = emptyEntries.collect((key, value) -> null);
		assert items2.equals(new List<>());
	}

	@Test
	public void convertsEntries() {
		// converts keys to lower case and values to upper case values
		// filters out no values
		final var entries1 = emptyEntries.convert((key, value)
				-> new Map.Entry<>(key.toLowerCase(), value.toUpperCase()));

		assert entries1.equals(
				new Map<>());

		// filters out all entries by returning null key
		final var entries2 = emptyEntries.convert((key, value)
				-> new Map.Entry<>(null, value));

		assert entries2.equals(
				new Map<>());

		// filters out all entries by returning null value
		final var entries3 = emptyEntries.convert((key, value)
				-> new Map.Entry<>(key, null));

		assert entries3.equals(
				new Map<>());

		// filters out all entries by returning null entries
		final var entries4 = emptyEntries.convert((key, value) -> null);
		assert entries4.equals(
				new Map<>());

	}
}
