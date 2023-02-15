package co.tsyba.core.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class MapTests {
	@Nested
	@DisplayName(".isEmpty()")
	class IsEmptyTests {
		@Test
		@DisplayName("when map is not empty, returns false")
		void returnsFalseWhenNotEmpty() {
			final var entries = new MutableMap<Integer, String>()
				.set(4, "a")
				.set(5, "g")
				.set(9, "F")
				.toImmutable();

			assert !entries.isEmpty();
		}

		@Test
		@DisplayName("when map is empty, returns true")
		void returnsTrueWhenEmpty() {
			final var entries = new Map<Integer, String>();
			assert entries.isEmpty();
		}
	}

	@Nested
	@DisplayName(".getCount()")
	class GetCountTests {
		@Test
		@DisplayName("when map is not empty, returns entry count")
		void returnsEntryCountWhenNotEmpty() {
			final var entries = new MutableMap<Integer, String>()
				.set(4, "a")
				.set(5, "g")
				.set(9, "F")
				.set(0, "Q")
				.toImmutable();

			final var count = entries.getCount();
			assert 4 == count;
		}

		@Test
		@DisplayName("when map is empty, returns 0")
		void returnsZeroWhenEmpty() {
			final var entries = new Map<Integer, String>();
			final var count = entries.getCount();

			assert 0 == count;
		}
	}

	@Nested
	@DisplayName(".contains(K, V)")
	class ContainsTests {
		private final Map<String, Integer> entries = new MutableMap<String, Integer>()
			.set("g", 5)
			.set("n", 2)
			.set("L", 0)
			.set("Q", 9)
			.set("t", 5)
			.toImmutable();

		@Test
		@DisplayName("when entry is present, returns true")
		void returnsTrueWhenPresent() {
			assert entries.contains("Q", 9);
		}

		@Test
		@DisplayName("when key is absent, returns false")
		void returnsFalseWhenKeyAbsent() {
			assert !entries.contains("N", 2);
		}

		@Test
		@DisplayName("when key is present but value is different, returns false")
		void returnsFalseWhenValueDifferent() {
			assert !entries.contains("t", 9);
		}

		@Test
		@DisplayName("when key is null, returns false")
		void returnsFalseWhenKeyNull() {
			assert !entries.contains(null, 9);
		}

		@Test
		@DisplayName("when value is null, returns false")
		void returnsFalseWhenValueNull() {
			assert !entries.contains("Q", null);
		}

		@Test
		@DisplayName("when map is empty, returns false")
		void returnsFalseWhenEmpty() {
			final var entries = new Map<String, Integer>();
			assert !entries.contains("Q", 9);
		}
	}

	@Nested
	@DisplayName(".contains(Predicate<K, V>)")
	class ContainsPredicateTests {
		private final Map<String, Integer> entries = new MutableMap<String, Integer>()
			.set("L", 0)
			.set("Q", 9)
			.set("t", 5)
			.toImmutable();

		@Test
		@DisplayName("when entry is present, returns true")
		void returnsTrueWhenPresent() {
			assert entries.contains((key, value) ->
				value > 5);
		}

		@Test
		@DisplayName("when entry is absent, returns false")
		void returnsFalseWhenAbsent() {
			assert !entries.contains((key, value) ->
				value > 10);
		}

		@Test
		@DisplayName("when map is empty, returns false")
		void returnsFalseWhenEmpty() {
			final var entries = new Map<String, Integer>();
			assert !entries.contains((key, value) ->
				value > 5);
		}
	}

	@Nested
	@DisplayName(".contains(Map<K, V>)")
	class ContainsMapTests {
		@Nested
		@DisplayName("when map is not empty")
		class NotEmptyTests {
			private final Map<Integer, String> entries1 = new MutableMap<Integer, String>()
				.set(4, "O")
				.set(6, "b")
				.set(9, "Q")
				.toImmutable();

			@Test
			@DisplayName("when all entries are present, returns true")
			void returnsTrueWhenAllPresent() {
				final var entries2 = new MutableMap<Integer, String>()
					.set(9, "Q")
					.set(6, "b")
					.toImmutable();

				assert entries1.contains(entries2);
			}

			@Test
			@DisplayName("when some entries are absent, returns false")
			void returnsFalseWhenSomeAbsent() {
				final var entries2 = new MutableMap<Integer, String>()
					.set(8, "M")
					.set(6, "b")
					.set(1, "G")
					.toImmutable();

				assert !entries1.contains(entries2);
			}

			@Test
			@DisplayName("when all entries are absent, returns false")
			void returnsFalseWhenAllAbsent() {
				final var entries2 = new MutableMap<Integer, String>()
					.set(3, "P")
					.set(9, "q")
					.toImmutable();

				assert !entries1.contains(entries2);
			}

			@Test
			@DisplayName("when searched map is empty, returns true")
			void returnsTrueWhenOtherEmpty() {
				final var entries2 = new Map<Integer, String>();
				assert entries1.contains(entries2);
			}
		}

		@Nested
		@DisplayName("when map is empty")
		class EmptyTests {
			private final Map<Integer, String> entries1 = new Map<>();

			@Test
			@DisplayName("when searched map is not empty, return false")
			void returnsFalseWhenOtherNotEmpty() {
				final var entries2 = new MutableMap<Integer, String>()
					.set(9, "Q")
					.set(6, "b")
					.toImmutable();

				assert !entries1.contains(entries2);
			}

			@Test
			@DisplayName("when searched map is empty, return true")
			void returnsTrueWhenOtherEmpty() {
				final var entries2 = new MutableMap<Integer, String>()
					.set(9, "Q")
					.set(6, "b")
					.toImmutable();

				assert !entries1.contains(entries2);
			}
		}
	}

	@Nested
	@DisplayName(".get(K)")
	class GetTests {
		private final Map<String, Integer> entries = new MutableMap<String, Integer>()
			.set("B", 5)
			.set("N", 2)
			.set("a", 0)
			.toImmutable();

		@Test
		@DisplayName("when key is present, returns value")
		void returnsValueWhenPresent() {
			final var value = entries.get("a");

			assert Optional.of(0)
				.equals(value);
		}

		@Test
		@DisplayName("when key is absent, returns empty optional")
		void returnsEmptyWhenAbsent() {
			final var value = entries.get("A");
			assert value.isEmpty();
		}

		@Test
		@DisplayName("when key is null, returns empty optional")
		void returnsEmptyWhenNull() {
			final var value = entries.get((String) null);
			assert value.isEmpty();
		}

		@Test
		@DisplayName("when map is empty, returns empty optional")
		void returnsEmptyWhenEmpty() {
			final var entries = new Map<String, Integer>();
			final var value = entries.get((String) null);

			assert value.isEmpty();
		}
	}
}

/*
 * Created by Serge Tsyba <tsyba@me.com> on Aug 4, 2019.
 */
class MapLegacyTests {
	@Test
	public void checksEmpty() {
		final var entries = map(
			"F", "kkmrnkd",
			"d", "pwowkam",
			"w", "aiams");

		assert entries.isEmpty() == false;
	}

	@Test
	public void returnsEntryCount() {
		final var entries = map(
			"R", "mddnbs",
			"T", "poeoiem");

		assert entries.getCount() == 2;
	}

	@Test
	public void checksKeyContainment() {
		final var entries = map(
			"G", "nndbd",
			"d", "asasjd",
			"e", "asaooes",
			"w", "nfbfbsus");

		// key is present
		assert entries.contains("e");
		// key is absent
		assert entries.contains("U") == false;
		// null key
		assert entries.contains((String) null) == false;
	}

	@Test
	public void checksEntryContainment() {
		final var entries = map(
			"E", "amamanajs",
			"e", "pspoldld",
			"D", "ms",
			"d", "aodoke");

		// entry is present
		assert entries.contains("D", "ms");
		// key is present
		assert entries.contains("D", "amsmsn") == false;
		// value is present
		assert entries.contains("F", "pspoldld") == false;
		// key and value are present, but not in same entry
		assert entries.contains("E", "ms") == false;
		// entry is absent
		assert entries.contains("F", "asmanan") == false;
		// null key
		assert entries.contains(null, "ms") == false;
		// null value
		assert entries.contains("D", null) == false;
	}

	@Test
	public void returnsValue() {
		final var entries = map(
			"U", "manna",
			"i", "appaos",
			"O", "asmkdime",
			"P", "amsna");

		// key is present
		assert entries.get("O")
			.get()
			.equals("asmkdime");

		// key is absent
		assert entries.get("o")
			.isEmpty();
	}

	@Test
	public void returnsEntries() {
		final var entries1 = map(
			"F", "annammja",
			"f", "poqmma",
			"Q", "osiem",
			"z", "znznaha");

		// some keys are absent;
		// retuns those, which are present
		final var entries2 = entries1.get(
			new List<>("F", "Z", "z"));

		entries2.equals(map(
			"F", "annammja",
			"z", "znznaha"));

		// empty keys; returns nothing
		final var entries3 = entries1.get(
			new List<>());

		assert entries3.equals(
			map());

		// null keys; returns nothing
		final var entries4 = entries1.get((Collection<String>) null);
		assert entries4.equals(
			map());
	}

	@Test
	public void returnsEntriesVariadic() {
		final var entries1 = map(
			"F", "annammja",
			"f", "poqmma",
			"Q", "osiem",
			"z", "znznaha");

		// some keys are absent;
		// retuns those, which are present
		entries1.get("F", "Z", "z")
			.equals(map(
				"F", "annammja",
				"z", "znznaha"));

		// empty keys; returns nothing
		assert entries1.get()
			.equals(map());

		// null keys; returns nothing
		assert entries1.get((String[]) null)
			.equals(map());
	}

	@Test
	public void filtersEntries() {
		final var entries = map(
			"p", "mnnnq",
			"k", "kananj",
			"n", "osiem",
			"u", "uajana");

		// entries where keys are value prefixes
		// filters out some entries
		final var entries2 = entries.filter((key, value)
			-> value.startsWith(key));

		assert entries2.equals(map(
			"k", "kananj",
			"u", "uajana"));

		// entries where values are keys prefixes
		// filters out all entries
		final var entries3 = entries.filter((key, value)
			-> key.startsWith(value));

		assert entries3.equals(
			map());
	}

	@Test
	public void collectsEntries() {
		final var entries = map(
			"R", "manna",
			"t", "maaij",
			"g", "po");

		// collects into key-value abbreviations
		// filters out no entries
		final var items2 = entries.collect((key, value)
			-> key.substring(0, 1).toUpperCase()
			+ value.substring(0, 1).toUpperCase());

		assert items2.getCount() == 3;
		assert items2.contains(
			new List<>("RM", "TM", "GP"));

		// collects all upper case keys
		// filters out some entries
		final var items3 = entries.collect((key, value)
			-> isUpperCase(key) ? key : null);

		assert items3.equals(
			new List<>("R"));

		// collects all upper case values
		// filters out all entries
		final var items4 = entries.collect((key, value)
			-> isUpperCase(value) ? value : null);

		assert items4.isEmpty();
	}

	@Test
	public void convertsEntries() {
		final var entries = map(
			"R", "mamannj",
			"e", "papla",
			"C", "amamkkq");

		// converts keys to lower case and values to upper case values
		// filters out no entries
		final var entries2 = entries.convert((key, value)
			-> new Map.Entry<>(key.toLowerCase(), value.toUpperCase()));

		assert entries2.equals(map(
			"r", "MAMANNJ",
			"e", "PAPLA",
			"c", "AMAMKKQ"));

		// converts values to uppercase only when key in upper case
		// filters out some entries by returning null keys
		final var entries3 = entries.convert((key, value)
			-> new Map.Entry<>(isUpperCase(key) ? key : null, value));

		assert entries3.equals(map(
			"R", "MAMANNJ",
			"C", "AMAMKKQ"));

		// converts keys to uppercase only when value in uppercase
		// filters out all entries by returning null values
		final var entries4 = entries.convert((key, value)
			-> new Map.Entry<>(key, isUpperCase(value) ? value : null));

		assert entries4.equals(map());

		// converts keys and values to lower case when they are in upper case
		// filters out all entries by returning null entries
		final var entries5 = entries.convert((key, value)
			-> isUpperCase(key) && isUpperCase(value)
			? new Map.Entry<>(key.toLowerCase(), value.toLowerCase())
			: null);

		assert entries5.equals(map());
	}

	private static boolean isUpperCase(String string) {
		return string.toUpperCase()
			.equals(string);
	}

	private static <T> Map<T, T> map(T... items) {
		final var entries = new RobinHoodHashStore<Map.Entry<T, T>>(items.length / 2);
		for (var index = 0; index < items.length; index += 2) {
			final var key = items[index];
			final var value = items[index + 1];
			final var entry = new Map.Entry<T, T>(key, value);

			entries.insert(entry);
		}

		return new Map<>(entries);
	}
}
