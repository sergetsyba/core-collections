package co.tsyba.core.collections;

import org.junit.Test;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Sep 9, 2019.
 */
public class EmptyMutableMapTests {
	private static final MutableMap<String, String> emptyEntries = new MutableMap<>();

	@Test
	public void returnsValueWithBackup() {
		// key is absent; inserts entry and returns backup value
		final var entries1 = new MutableMap<String, String>();
		assert entries1.get("i", "ofijfd")
				.equals("ofijfd");

		assert entries1.equals(map(
				"i", "ofijfd"));

		// key is null; does not insert entry and returns backup value
		final var entries2 = new MutableMap<String, String>();
		assert entries2.get(null, "nanjao")
				.equals("nanjao");

		assert entries2.equals(emptyEntries);

		// backup value is null;
		// does not insert entry and returns backup value
		assert entries2.get("n", null) == null;
		assert entries2.equals(emptyEntries);
	}

	@Test
	public void setsEntry() {
		// inserts new entry
		final var entries1 = new MutableMap<String, String>();
		assert entries1.set("v", "qqmmqn") == entries1;
		assert entries1.equals(map(
				"v", "qqmmqn"));

		// key is null; does nothing
		final var entries2 = new MutableMap<String, String>();
		assert entries2.set(null, "anjakka") == entries2;
		assert entries2.equals(emptyEntries);

		// value is null; does nothing
		final var entries3 = new MutableMap<String, String>();
		assert entries3.set("z", null) == entries3;
		assert entries3.equals(emptyEntries);
	}

	@Test
	public void mergesEntry() {
		// key is absent; inserts new entry
		final var entries1 = new MutableMap<String, String>();
		assert entries1.set("f", "nsjjsn", (oldValue, newValue)
				-> newValue) == entries1;

		assert entries1.equals(map(
				"f", "nsjjsn"));

		// key is null; does nothing
		final var entries2 = new MutableMap<String, String>();
		assert entries2.set(null, "oodidijd", (oldValue, newValue)
				-> "mnndmdk") == entries2;

		assert entries2.equals(emptyEntries);

		// value is null; does nothing
		final var entries3 = new MutableMap<String, String>();
		assert entries3.set("p", null, (oldValue, newValue)
				-> "bdbdgd") == entries3;

		assert entries3.equals(emptyEntries);

		// key is absent, but resolver is null;
		// does nothing
		final var entries4 = new MutableMap<String, String>();
		assert entries4.set("p", "ndndbdh", null) == entries4;
		assert entries4.equals(emptyEntries);
	}

	@Test
	public void setsEntries() {
		// inserts new entries
		final var entries1 = new MutableMap<String, String>();
		assert entries1.set(map(
				"J", "iwiwunw",
				"c", "nwnwbjw",
				"i", "ananabj")) == entries1;

		assert entries1.equals(map(
				"c", "nwnwbjw",
				"J", "iwiwunw",
				"i", "ananabj"));

		// inserted entries are empty; does nothing
		final var entries2 = new MutableMap<String, String>();
		assert entries2.set(emptyEntries) == entries2;
		assert entries2.equals(emptyEntries);

		// inserted entries is null; does nothing
		final var entries3 = new MutableMap<String, String>();
		assert entries3.set(null) == entries3;
		assert entries3.equals(emptyEntries);
	}

	@Test
	public void removesEntry() {
		// key is absent, does nothing
		final var entries1 = new MutableMap<String, String>();
		assert entries1.remove("p") == entries1;
		assert entries1.equals(emptyEntries);

		// key is null, does nothing
		final var entries2 = new MutableMap<String, String>();
		assert entries2.remove((String) null) == entries2;
		assert entries2.equals(emptyEntries);
	}

	@Test
	public void removesEntries() {
		// does nothing
		final var entries1 = new MutableMap<String, String>();
		assert entries1.remove(
				new List<>("w", "h", "Z", "o")) == entries1;

		assert entries1.equals(emptyEntries);

		// removed keys is empty; does nothing
		final var entries2 = new MutableMap<String, String>();
		assert entries2.remove(
				new List<>()) == entries2;

		assert entries2.equals(emptyEntries);

		// removed keys is null; does nothing
		final var entries3 = new MutableMap<String, String>();
		assert entries3.remove(
				(Collection<String>) null) == entries3;

		assert entries3.equals(emptyEntries);
	}

	@Test
	public void removesEntriesVariardic() {
		// does nothing
		final var entries1 = new MutableMap<String, String>();
		assert entries1.remove("z", "j", "o", "p") == entries1;
		assert entries1.equals(emptyEntries);

		// removed keys is empty; does nothing
		final var entries2 = new MutableMap<String, String>();
		assert entries2.remove() == entries2;
		assert entries2.equals(emptyEntries);

		// removed keys is null; does nothing
		final var entries3 = new MutableMap<String, String>();
		assert entries3.remove((String[]) null) == entries3;
		assert entries3.equals(emptyEntries);
	}

	@Test
	public void clearsEntries() {
		final var entries = new MutableMap<String, String>();
		assert entries.clear() == entries;
		assert entries.equals(emptyEntries);
	}

	private static <T> MutableMap<T, T> map(T... items) {
		final var entries = new RobinHoodHashStore<Map.Entry<T, T>>(items.length / 2);
		for (var index = 0; index < items.length; index += 2) {
			final var key = items[index];
			final var value = items[index + 1];
			final var entry = new Map.Entry<T, T>(key, value);

			entries.insert(entry);
		}

		return new MutableMap<>(entries);
	}
}
