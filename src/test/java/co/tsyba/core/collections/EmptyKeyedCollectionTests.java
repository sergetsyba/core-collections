package co.tsyba.core.collections;

import co.tsyba.core.collections.data.MapCollection;
import java.util.ArrayList;
import org.junit.Test;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Aug 14, 2019.
 */
public class EmptyKeyedCollectionTests {
	final KeyedCollection<String, String> emptyEntries = new MapCollection<>();

	@Test
	public void verifiesEmpty() {
		assert emptyEntries.isEmpty();
	}

	@Test
	public void returnsEntryCount() {
		assert emptyEntries.getCount() == 0;
	}

	@Test
	public void verifiesNoKeyContainment() {
		assert emptyEntries.contains("e") == false;
	}

	@Test
	public void verifiesNoEntryContainment() {
		assert emptyEntries.contains("P", "tbdbdhs") == false;
	}

	@Test
	public void verifiesNoEntriesContainment() {
		final var entries = collect(
				"T", "mdndnbs",
				"w", "azzxxyssssa");

		assert emptyEntries.contains(entries) == false;

		// unlike indexed collection, empty keyed collection 
		// contains itself since there is no find operation
		assert emptyEntries.contains(emptyEntries);
	}

	@Test
	public void verifiesNoEntryMatch() {
		// doesn not contain entry with empty key or value
		assert emptyEntries.contains((key, value)
				-> key.isBlank() || value.isBlank()) == false;
	}

	@Test
	public void verifiesNoEachEntryMatch() {
		// does not contain entry where value starts with key
		assert emptyEntries.eachMatches((key, value)
				-> value.startsWith(key)) == false;
	}

	@Test
	public void iteratesNoEntries() {
		final var keys = new ArrayList<String>();
		final var values = new ArrayList<String>();
		emptyEntries.iterate((key, value) -> {
			keys.add(key);
			values.add(value);
		});

		assert keys.isEmpty() && values.isEmpty();
	}

	@Test
	public void combinesNoEntries() {
		final var combination = emptyEntries.combine("empty",
				(combined, key, value) -> combined + key + value);

		assert combination.equals("empty");
	}

	@Test
	public void joinsNoEntries() {
		assert emptyEntries.join(": ", ", ")
				.equals("");
	}

	private static <T> KeyedCollection<T, T> collect(T... values) {
		return new MapCollection<>(values);
	}
}
