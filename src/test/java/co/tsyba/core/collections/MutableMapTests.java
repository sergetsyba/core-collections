package co.tsyba.core.collections;

import org.junit.Test;

/*
 * Created by Serge Tsyba <tsyba@me.com> on Sep 1, 2019.
 */
public class MutableMapTests {
	@Test
	public void returnsValueWithBackup() {
		final var entries = map(
				"n", "ananjak",
				"o", "nanaji",
				"i", "annah");

		// key is present;
		// does not insert entry and returns existing value
		assert entries.get("i", "ofijfd")
				.equals("annah");

		assert entries.equals(map(
				"n", "ananjak",
				"o", "nanaji",
				"i", "annah"));

		// key is null;
		// does not insert entry and returns backup value
		assert entries.get(null, "nanjao")
				.equals("nanjao");

		assert entries.equals(map(
				"n", "ananjak",
				"o", "nanaji",
				"i", "annah"));

		// key is present, backup value is null;
		// does not insert entry and returns existing value
		assert entries.get("n", null)
				.equals("ananjak");

		assert entries.equals(map(
				"n", "ananjak",
				"o", "nanaji",
				"i", "annah"));

		// key is absent, backup value is null;
		// does not insert entry and returns backup value
		assert entries.get("p", null) == null;

		assert entries.equals(map(
				"n", "ananjak",
				"o", "nanaji",
				"i", "annah"));

		// key is absent;
		// inserts entry and returns backup value
		assert entries.get("y", "ididus")
				.equals("ididus");

		assert entries.equals(map(
				"n", "ananjak",
				"o", "nanaji",
				"i", "annah",
				"y", "ididus"));
	}

	@Test
	public void setsEntry() {
		final var entries = map(
				"g", "akannw",
				"R", "amana",
				"a", "amananqj");

		// inserts new entry
		assert entries.set("v", "qqmmqn") == entries;
		assert entries.equals(map(
				"g", "akannw",
				"R", "amana",
				"a", "amananqj",
				"v", "qqmmqn"));

		// key is present; replaces value of an existing entry
		assert entries.set("R", "nhdhdi") == entries;
		assert entries.equals(map(
				"g", "akannw",
				"R", "nhdhdi",
				"a", "amananqj",
				"v", "qqmmqn"));

		// key is null; does nothing
		assert entries.set(null, "anjakka") == entries;
		assert entries.equals(map(
				"g", "akannw",
				"R", "nhdhdi",
				"a", "amananqj",
				"v", "qqmmqn"));

		// value is null; does nothing
		assert entries.set("z", null) == entries;
		assert entries.equals(map(
				"g", "akannw",
				"R", "nhdhdi",
				"a", "amananqj",
				"v", "qqmmqn"));
	}

	@Test
	public void mergesEntry() {
		final var entries = map(
				"g", "aanjjakka",
				"p", "pokkisis",
				"r", "nbfbhd");

		// key is absent; inserts new entry
		assert entries.set("f", "nsjjsn",
				(oldValue, newValue) -> newValue) == entries;

		assert entries.equals(map(
				"g", "aanjjakka",
				"p", "pokkisis",
				"r", "nbfbhd",
				"f", "nsjjsn"));

		// key is present; replaces entry value with resolved one
		assert entries.set("p", "nndnjjd",
				(oldValue, newValue) -> "jjsisi") == entries;

		assert entries.equals(map(
				"g", "aanjjakka",
				"p", "jjsisi",
				"r", "nbfbhd",
				"f", "nsjjsn"));

		// key is null; does nothing
		assert entries.set(null, "oodidijd",
				(oldValue, newValue) -> "mnndmdk") == entries;

		assert entries.equals(map(
				"g", "aanjjakka",
				"p", "jjsisi",
				"r", "nbfbhd",
				"f", "nsjjsn"));

		// value is null; does nothing
		assert entries.set("p", null,
				(oldValue, newValue) -> "bdbdgd") == entries;

		assert entries.equals(map(
				"g", "aanjjakka",
				"p", "jjsisi",
				"r", "nbfbhd",
				"f", "nsjjsn"));

		// key is present, but resolved value is null;
		// does nothing
		assert entries.set("p", "ndndbdh",
				(oldValue, newValue) -> null) == entries;

		assert entries.equals(map(
				"g", "aanjjakka",
				"p", "jjsisi",
				"r", "nbfbhd",
				"f", "nsjjsn"));

		// key is present, but resolver is null;
		// does nothing
		assert entries.set("p", "ndndbdh", null) == entries;
		assert entries.equals(map(
				"g", "aanjjakka",
				"p", "jjsisi",
				"r", "nbfbhd",
				"f", "nsjjsn"));
	}

	@Test
	public void setsEntries() {
		final var entries = map(
				"f", "nanabj",
				"p", "annja",
				"c", "apokmmw");

		// inserts new entries and replaces values of entries with matching keys
		final var entries1 = map(
				"J", "iwiwunw",
				"c", "nwnwbjw",
				"i", "ananabj");

		assert entries.set(entries1) == entries;
		assert entries.equals(map(
				"f", "nanabj",
				"p", "annja",
				"c", "nwnwbjw",
				"J", "iwiwunw",
				"i", "ananabj"));

		// inserted entries are empty; does nothing
		final var entries2 = new Map<String, String>();
		assert entries.set(entries2) == entries;
		assert entries.equals(map(
				"f", "nanabj",
				"c", "nwnwbjw",
				"i", "ananabj",
				"p", "annja",
				"J", "iwiwunw"));

		// inserted entries is null; does nothing
		final var entries3 = (Map<String, String>) null;
		assert entries.set(entries3) == entries;
		assert entries.equals(map(
				"f", "nanabj",
				"c", "nwnwbjw",
				"i", "ananabj",
				"p", "annja",
				"J", "iwiwunw"));
	}

	@Test
	public void removesEntry() {
		final var entries = map(
				"f", "ananjai",
				"p", "kdmn",
				"l", "ndndjjkk",
				"y", "babhhaj");

		// key is present, removes entry
		assert entries.remove("p") == entries;
		assert entries.equals(map(
				"f", "ananjai",
				"l", "ndndjjkk",
				"y", "babhhaj"));

		// key is absent, does nothing
		assert entries.remove("z") == entries;
		assert entries.equals(map(
				"f", "ananjai",
				"l", "ndndjjkk",
				"y", "babhhaj"));

		// key is null, does nothing
		assert entries.remove((String) null) == entries;
		assert entries.equals(map(
				"f", "ananjai",
				"l", "ndndjjkk",
				"y", "babhhaj"));
	}

	@Test
	public void removesEntries() {
		final var entries = map(
				"k", "nndjjsk",
				"o", "nsnsjiis",
				"i", "smnsiiso",
				"w", "bbdvhhd");

		// removes entries with matching keys
		final var keys1 = new List<String>("w", "h", "Z", "o");
		assert entries.remove(keys1) == entries;
		assert entries.equals(map(
				"k", "nndjjsk",
				"i", "smnsiiso"));

		// removed keys is empty; does nothing
		final var keys2 = new List<String>();
		assert entries.remove(keys2) == entries;
		assert entries.equals(map(
				"k", "nndjjsk",
				"i", "smnsiiso"));

		// removed keys is null; does nothing
		final var keys3 = (Collection<String>) null;
		assert entries.remove(keys3) == entries;
		assert entries.equals(map(
				"k", "nndjjsk",
				"i", "smnsiiso"));
	}

	@Test
	public void removesEntriesVariardic() {
		final var entries = map(
				"n", "annajjai",
				"l", "oodokj",
				"j", "mmkdood",
				"o", "pppd");

		// removes entries with matching keys
		assert entries.remove("z", "j", "o", "p") == entries;
		assert entries.equals(map(
				"n", "annajjai",
				"l", "oodokj"));

		// removed keys is empty; does nothing
		assert entries.remove() == entries;
		assert entries.equals(map(
				"n", "annajjai",
				"l", "oodokj"));

		// removed keys is null; does nothing
		assert entries.remove((String[]) null) == entries;
		assert entries.equals(map(
				"n", "annajjai",
				"l", "oodokj"));
	}

	@Test
	public void clearsEntries() {
		final var entries = map(
				"n", "snnjsi",
				"p", "snnjsi",
				"r", "nnnjs");

		assert entries.clear() == entries;
		assert entries.equals(map());
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
