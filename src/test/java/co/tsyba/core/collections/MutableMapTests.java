package co.tsyba.core.collections;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MutableMapTests {
	@Nested
	@DisplayName(".get(K, V)")
	class GetTests {
		@Nested
		@DisplayName("when map is not empty")
		class NotEmptyMapTests {
			private final MutableMap<String, Integer> entries = new MutableMap<>(
				new List<>("F", "v", "R", "q"),
				new List<>(5, 3, 2, 1));

			@Test
			@DisplayName("when key is present, returns stored value")
			void returnsStoredValueWhenKeyPresent() {
				final var value = entries.get("R", 0);

				assert 2 == value;
				assert !entries.getValues()
					.contains(0);
			}

			@Test
			@DisplayName("when key is present and value is null, returns stored value")
			void returnsValueWhenKeyPresentValueNull() {
				final var value = entries.get("F", null);
				assert 5 == value;
			}

			@Test
			@DisplayName("when key is absent, stores and returns backup value")
			void returnsBackupValueWhenKeyAbsent() {
				final var value = entries.get("r", 0);

				assert 0 == value;
				assert entries.contains("r", 0);
			}

			@Test
			@DisplayName("when key is absent and value is null, returns null")
			void returnsNullValueWhenKeyAbsentValueNull() {
				final var value = entries.get("X", null);
				assert null == value;
			}

			@Test
			@DisplayName("when key is null, does not store but returns backup value")
			void returnsBackupValueWhenKeyNull() {
				final var value = entries.get(null, 9);

				assert 9 == value;
				assert !entries.getValues()
					.contains(9);
			}

			@Test
			@DisplayName("when key and value are null, does not store and returns null")
			void returnsNullWhenKeyValueNull() {
				final var value = entries.get(null, null);
				assert null == value;
			}
		}
	}

	@Nested
	@DisplayName(".set(K, V)")
	class SetTests {
		@Test
		@DisplayName("when key is absent, inserts new entry")
		void insertsEntryWhenKeyAbsent() {
			final var entries = new MutableMap<>(
				new List<>("G", "x"),
				new List<>(7, 0));

			final var returned = entries.set("B", 4);

			final var expected = new Set<>(
				new Map.Entry<>("G", 7),
				new Map.Entry<>("x", 0),
				new Map.Entry<>("B", 4));

			assert entries == returned;
			assert new Set<>(entries.store)
				.equals(expected);
		}

		@Test
		@DisplayName("when key already present, replaces its value")
		void replacesValueWhenKeyPresent() {
			final var entries = new MutableMap<>(
				new List<>("G", "x"),
				new List<>(7, 0));

			final var returned = entries.set("x", 4);

			final var expected = new Set<>(
				new Map.Entry<>("G", 7),
				new Map.Entry<>("x", 4));

			assert entries == returned;
			assert new Set<>(entries.store)
				.equals(expected);
		}

		@Test
		@DisplayName("when key is null, does nothing")
		void doesNothingWhenKeyNull() {
			final var entries = new MutableMap<>(
				new List<>("G", "x"),
				new List<>(7, 0));

			final var returned = entries.set(null, 4);

			final var expected = new Set<>(
				new Map.Entry<>("G", 7),
				new Map.Entry<>("x", 0));

			assert entries == returned;
			assert new Set<>(entries.store)
				.equals(expected);
		}

		@Test
		@DisplayName("when value is null, does nothing")
		void doesNothingWhenValueNull() {
			final var entries = new MutableMap<>(
				new List<>("G", "x"),
				new List<>(7, 0));

			final var returned = entries.set("H", null);

			final var expected = new Set<>(
				new Map.Entry<>("G", 7),
				new Map.Entry<>("x", 0));

			assert entries == returned;
			assert new Set<>(entries.store)
				.equals(expected);
		}
	}

	@Nested
	@DisplayName(".set(Map<K, V>)")
	class SetMapTests {
		@Test
		@DisplayName("when all keys are absent, inserts new entries")
		void insertsEntriesWhenAllKeysAbsent() {
			final var entries1 = new MutableMap<>(
				new List<>("G", "x"),
				new List<>(7, 0));

			final var entries2 = new MutableMap<>(
				new List<>("B", "p"),
				new List<>(7, 3));

			final var returned = entries1.set(entries2);

			final var expected = new Set<>(
				new Map.Entry<>("G", 7),
				new Map.Entry<>("x", 0),
				new Map.Entry<>("B", 7),
				new Map.Entry<>("p", 3));

			assert entries1 == returned;
			assert new Set<>(entries1.store)
				.equals(expected);
		}

		@Test
		@DisplayName("when some keys already present, replaces their values")
		void replacesValuesWhenSomeKeysPresent() {
			final var entries1 = new MutableMap<>(
				new List<>("G", "x", "K"),
				new List<>(7, 0, 1));

			final var entries2 = new MutableMap<>(
				new List<>("x", "p", "G"),
				new List<>(7, 3, 8));

			final var returned = entries1.set(entries2);

			final var expected = new Set<>(
				new Map.Entry<>("G", 8),
				new Map.Entry<>("x", 7),
				new Map.Entry<>("K", 1),
				new Map.Entry<>("p", 3));

			assert entries1 == returned;
			assert new Set<>(entries1.store)
				.equals(expected);
		}
	}
}


/*
 * Created by Serge Tsyba <tsyba@me.com> on Sep 1, 2019.
 */
class LegacyMutableMapTests {
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
