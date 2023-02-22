package co.tsyba.core.collections;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Optional;

class MapTests {
	@Nested
	@DisplayName("Map(Entry<K, V>...)")
	class ConstructorVarargsTests {
		@Test
		@DisplayName("creates map")
		void createsMap() {
			final var entries = new Map<>(
				new Map.Entry<>("R", 2),
				new Map.Entry<>("J", 3),
				new Map.Entry<>("b", 7));

			final var expected = new Set<>(
				new Map.Entry<>("R", 2),
				new Map.Entry<>("J", 3),
				new Map.Entry<>("b", 7));

			assert new Set<>(entries.store)
				.equals(expected);
		}

		@Test
		@DisplayName("creates empty map")
		void createsEmptyMap() {
			final var entries = new Map<>();

			final var expected = new Set<>();
			assert new Set<>(entries.store)
				.equals(expected);
		}
	}

	@Nested
	@DisplayName("Map(List<K>, List<V>)")
	class ConstructorListsTests {
		@Test
		@DisplayName("creates map")
		void createsMap() {
			final var entries = new Map<>(
				new List<>("G", "b", "Q", "f"),
				new List<>(4, 5, 2, 1));

			final var expected = new Set<>(
				new Map.Entry<>("G", 4),
				new Map.Entry<>("b", 5),
				new Map.Entry<>("Q", 2),
				new Map.Entry<>("f", 1));

			assert new Set<>(entries.store)
				.equals(expected);
		}

		@Test
		@DisplayName("when keys list is shorter, ignores extra values")
		void createsMapWhenKeysShorterThanValues() {
			final var entries = new Map<>(
				new List<>("G", "b"),
				new List<>(4, 5, 2, 1));

			final var expected = new Set<>(
				new Map.Entry<>("G", 4),
				new Map.Entry<>("b", 5));

			assert new Set<>(entries.store)
				.equals(expected);
		}

		@Test
		@DisplayName("when keys list is empty, creates empty map")
		void createsEmptyMapWhenKeysEmpty() {
			final var entries = new Map<>(
				new List<>(),
				new List<>(2, 7, 3, 2, 4));

			final var expected = new Set<>();
			assert new Set<>(entries.store)
				.equals(expected);
		}

		@Test
		@DisplayName("when values list is shorter, ignores extra keys")
		void createsMapWhenValuesShorterThanKeys() {
			final var entries = new Map<>(
				new List<>("V", "s"),
				new List<>(2, 7, 3, 2, 4));

			final var expected = new Set<>(
				new Map.Entry<>("V", 2),
				new Map.Entry<>("s", 7));

			assert new Set<>(entries.store)
				.equals(expected);
		}

		@Test
		@DisplayName("when values list is empty, creates empty map")
		void createsEmptyMapWhenValuesEmpty() {
			final var entries = new Map<>(
				new List<>("V", "s"),
				new List<>());

			final var expected = new Set<>();
			assert new Set<>(entries.store)
				.equals(expected);
		}

		@Test
		@DisplayName("creates empty map")
		void createsEmptyMap() {
			final var entries = new Map<>(
				new List<>(),
				new List<>());

			final var expected = new Set<>();
			assert new Set<>(entries.store)
				.equals(expected);
		}

		@Test
		@DisplayName("when keys contain repeated items, uses last occurrence")
		void keepsLastOccurrenceWhenKeysRepeat() {
			final var entries = new Map<>(
				new List<>("V", "a", "s", "V", "b"),
				new List<>(2, 3, 4, 5, 6));

			final var expected = new Set<>(
				new Map.Entry<>("V", 5),
				new Map.Entry<>("a", 3),
				new Map.Entry<>("s", 4),
				new Map.Entry<>("b", 6));

			assert new Set<>(entries.store)
				.equals(expected);
		}
	}

	@Nested
	@DisplayName("Map(Map<K, V>)")
	class ConstructorMapTests {
		@Test
		@DisplayName("creates map")
		void createsMap() {
			final var entries = new Map<>(
				new Map<>(
					new Map.Entry<>("B", 2),
					new Map.Entry<>("b", 1),
					new Map.Entry<>("O", 0),
					new Map.Entry<>("o", 1)));

			final var expected = new Set<>(
				new Map.Entry<>("B", 2),
				new Map.Entry<>("b", 1),
				new Map.Entry<>("O", 0),
				new Map.Entry<>("o", 1));

			assert new Set<>(entries.store)
				.equals(expected);
		}

		@Test
		@DisplayName("creates empty map")
		void createsEmptyMapWhenMapEmpty() {
			final var entries = new Map<String, Integer>();

			final var expected = new Set<>();
			assert new Set<>(entries.store)
				.equals(expected);
		}
	}

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
		@DisplayName("when key and value are present, returns true")
		void returnsTrueWhenPresent() {
			assert entries.contains("Q", 9);
		}

		@Test
		@DisplayName("when key is absent, returns false")
		void returnsFalseWhenKeyAbsent() {
			assert !entries.contains("N", 2);
		}

		@Test
		@DisplayName("when key is present but value is absent, returns false")
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
	@DisplayName(".getKeys()")
	class GetKeysTests {
		@Test
		@DisplayName("when map is not empty, returns all keys")
		void returnsKeysWhenNotEmpty() {
			final var entries = new MutableMap<String, Integer>()
				.set("T", 4)
				.set("L", 3)
				.set("m", 0)
				.set("X", 2);

			final var keys = entries.getKeys();
			assert new Set<>("T", "L", "m", "X")
				.equals(keys);
		}

		@Test
		@DisplayName("when map is empty, returns empty set")
		void returnsEmptySetWhenEmpty() {
			final var entries = new Map<String, Integer>();
			final var keys = entries.getKeys();

			assert new Set<>()
				.equals(keys);
		}
	}

	@Nested
	@DisplayName(".getValues()")
	class GetValuesTests {
		@Disabled // todo: re-enable
		@Test
		@DisplayName("when map is not empty, returns all values")
		void returnsKeysWhenNotEmpty() {
			final var entries = new MutableMap<String, Integer>()
				.set("T", 4)
				.set("L", 3)
				.set("m", 0)
				.set("X", 2);

			final var values = entries.getValues();
			assert new List<>(4, 3, 0, 2)
				.equals(values);
		}

		@Test
		@DisplayName("when map is empty, returns empty collection")
		void returnsEmptySetWhenEmpty() {
			final var entries = new Map<String, Integer>();
			final var values = entries.getValues();

			assert new List<>()
				.equals(values);
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

	@Nested
	@DisplayName(".get(K...)")
	class GetVarargsTests {
		@Nested
		@DisplayName("when map is not empty")
		class NotEmptyMapTests {
			private final Map<String, Integer> entries1 = new MutableMap<String, Integer>()
				.set("B", 5)
				.set("N", 2)
				.set("O", 5)
				.set("a", 0)
				.toImmutable();

			@Test
			@DisplayName("when all keys are present, returns present entries")
			void returnsPresentWhenAllKeysPresent() {
				final var entries2 = entries1.get("a", "B", "N");
				final var expected = new MutableMap<String, Integer>()
					.set("a", 0)
					.set("B", 5)
					.set("N", 2)
					.toImmutable();

				assert expected.equals(entries2);
			}

			@Test
			@DisplayName("when some keys are present, returns present entries")
			void returnsPresentWhenSomeKeysPresent() {
				final var entries2 = entries1.get("b", "O", "q", "U", "a");
				final var expected = new MutableMap<String, Integer>()
					.set("O", 5)
					.set("a", 0)
					.toImmutable();

				assert expected.equals(entries2);
			}

			@Test
			@DisplayName("when some keys are null, returns present values")
			void returnsPresentWhenSomeKeysNull() {
				final var entries2 = entries1.get(null, "O", null, null, "N", null);
				final var expected = new MutableMap<String, Integer>()
					.set("O", 5)
					.set("N", 2)
					.toImmutable();

				assert expected.equals(entries2);
			}

			@Test
			@DisplayName("when all keys are absent, returns empty map")
			void returnsEmptyWhenAllKeysAbsent() {
				final var entries2 = entries1.get("L", "M", "d", "I");

				final var expected = new Map<String, Integer>();
				assert expected.equals(entries2);
			}

			@Test
			@DisplayName("when keys is empty, returns empty map")
			void returnsEmptyWhenKeysEmpty() {
				final var entries2 = entries1.get();

				final var expected = new Map<String, Integer>();
				assert expected.equals(entries2);
			}
		}

		@Nested
		@DisplayName("when map is empty")
		class EmptyMapTests {
			private final Map<String, Integer> entries1 = new Map<>();

			@Test
			@DisplayName("when keys are not empty, returns empty map")
			void returnsEmptyWhenKeysNotEmpty() {
				final var entries2 = entries1.get("a", "B", "N");

				assert new Map<String, Integer>()
					.equals(entries2);
			}

			@Test
			@DisplayName("when some keys are null, returns empty map")
			void returnsEmptyWhenSomeKeysNull() {
				final var entries2 = entries1.get(null, "B", null, "N");

				assert new Map<String, Integer>()
					.equals(entries2);
			}

			@Test
			@DisplayName("when keys are empty, returns empty map")
			void returnsEmptyWhenKeysEmpty() {
				final var entries2 = entries1.get();

				assert new Map<String, Integer>()
					.equals(entries2);
			}
		}
	}

	@Nested
	@DisplayName(".get(Collection<K>)")
	class GetCollectionTests {
		@Nested
		@DisplayName("when map is not empty")
		class NotEmptyMapTests {
			private final Map<String, Integer> entries1 = new MutableMap<String, Integer>()
				.set("B", 5)
				.set("N", 2)
				.set("O", 5)
				.set("a", 0)
				.toImmutable();

			@Test
			@DisplayName("when all keys are present, returns present entries")
			void returnsPresentWhenAllKeysPresent() {
				final var entries2 = entries1.get(
					new Set<>("a", "B", "N"));

				final var expected = new MutableMap<String, Integer>()
					.set("a", 0)
					.set("B", 5)
					.set("N", 2)
					.toImmutable();

				assert expected.equals(entries2);
			}

			@Test
			@DisplayName("when some keys are present, returns present entries")
			void returnsPresentWhenSomeKeysPresent() {
				final var entries2 = entries1.get(
					new Set<>("b", "O", "q", "U", "a"));

				final var expected = new MutableMap<String, Integer>()
					.set("O", 5)
					.set("a", 0)
					.toImmutable();

				assert expected.equals(entries2);
			}

			@Test
			@DisplayName("when all keys are absent, returns empty map")
			void returnsEmptyWhenAllKeysAbsent() {
				final var entries2 = entries1.get(
					new Set<>("L", "M", "d", "I"));

				final var expected = new Map<String, Integer>();
				assert expected.equals(entries2);
			}

			@Test
			@DisplayName("when keys is empty, returns empty map")
			void returnsEmptyWhenKeysEmpty() {
				final var entries2 = entries1.get(
					new Set<>());

				final var expected = new Map<String, Integer>();
				assert expected.equals(entries2);
			}
		}

		@Nested
		@DisplayName("when map is empty")
		class EmptyMapTests {
			private final Map<String, Integer> entries1 = new Map<>();

			@Test
			@DisplayName("when keys are not empty, returns empty map")
			void returnsEmptyWhenKeysNotEmpty() {
				final var entries2 = entries1.get(
					new Set<>("a", "B", "N"));

				assert new Map<String, Integer>()
					.equals(entries2);
			}

			@Test
			@DisplayName("when keys are empty, returns empty map")
			void returnsEmptyWhenKeysEmpty() {
				final var entries2 = entries1.get(
					new Set<>());

				assert new Map<String, Integer>()
					.equals(entries2);
			}
		}
	}

	@Nested
	@DisplayName(".anyMatches(BiPredicate<K, V>)")
	class AnyMatchesTests {
		@Nested
		@DisplayName("when map is not empty")
		class NotEmptyMapTests {
			private final Map<String, Integer> entries = new MutableMap<String, Integer>()
				.set("B", 3)
				.set("V", 9)
				.set("e", 1)
				.set("N", 0)
				.toImmutable();

			@Test
			@DisplayName("when an entry matches, returns true")
			void returnsTrueWhenAnyMatches() {
				assert entries.anyMatches((key, value) ->
					value > 5);
			}

			@Test
			@DisplayName("when no entry matches, returns false")
			void returnsFalseWhenNoneMatches() {
				assert !entries.anyMatches((key, value) ->
					value > 10);
			}
		}

		@Nested
		@DisplayName("when map is empty")
		class EmptyMapTests {
			private final Map<String, Integer> entries = new Map<>();

			@Test
			@DisplayName("returns false")
			void returnsFalse() {
				assert !entries.anyMatches((key, value) ->
					value > 5);
			}
		}
	}

	@Nested
	@DisplayName(".noneMatches(BiPredicate<K, V>)")
	class NoneMatchesTests {
		@Nested
		@DisplayName("when map is not empty")
		class NotEmptyMapTests {
			private final Map<String, Integer> entries = new MutableMap<String, Integer>()
				.set("B", 3)
				.set("V", 9)
				.set("e", 1)
				.set("N", 0)
				.toImmutable();

			@Test
			@DisplayName("when all entries do not match, returns true")
			void returnsTrueWhenAllDoNotMatch() {
				assert entries.noneMatches((key, value) ->
					value > 10);
			}

			@Test
			@DisplayName("when any entry matches, returns false")
			void returnsFalseWhenAnyMatches() {
				assert !entries.noneMatches((key, value) ->
					value > 5);
			}
		}

		@Nested
		@DisplayName("when map is empty")
		class EmptyMapTests {
			private final Map<String, Integer> entries = new Map<>();

			@Test
			@DisplayName("returns true")
			void returnsTrue() {
				assert entries.noneMatches((key, value) ->
					value > 10);
			}
		}
	}

	@Nested
	@DisplayName(".allMatch(BiPredicate<K, V>)")
	class AllMatchTests {
		@Nested
		@DisplayName("when map is not empty")
		class NotEmptyMapTests {
			private final Map<String, Integer> entries = new MutableMap<String, Integer>()
				.set("B", 3)
				.set("V", 9)
				.set("e", 1)
				.set("N", 0)
				.toImmutable();

			@Test
			@DisplayName("when all entries match, returns true")
			void returnsTrueWhenAllMatch() {
				assert entries.allMatch((key, value) ->
					value < 10);
			}

			@Test
			@DisplayName("when any entry does not match, returns false")
			void returnsFalseWhenAnyDoesNotMatch() {
				assert !entries.allMatch((key, value) ->
					value > 5);
			}
		}

		@Nested
		@DisplayName("when map is empty")
		class EmptyMapTests {
			private final Map<String, Integer> entries = new Map<>();

			@Test
			@DisplayName("returns true")
			void returnsTrue() {
				assert entries.allMatch((key, value) ->
					value < 10);
			}
		}
	}

	@Nested
	@DisplayName(".matchAny(BiPredicate<K, V>)")
	class MatchAnyTests {
		@Nested
		@DisplayName("when map is not empty")
		class NotEmptyMapTests {
			private final Map<String, Integer> entries = new MutableMap<String, Integer>()
				.set("G", 0)
				.set("V", 2)
				.set("v", 3)
				.toImmutable();

			@Test
			@DisplayName("when some entry match, returns value of matched entry")
			void returnsMatchedEntryWhenMatch() {
				final var matched = entries.matchAny((key, value) ->
					value < 2);

				assert Optional.of(0)
					.equals(matched);
			}

			@Test
			@DisplayName("when no entries match, returns empty optional")
			void returnsEmptyOptionalWhenNoneMatches() {
				final var matched = entries.matchAny((key, value) ->
					value > 5);

				assert Optional.empty()
					.equals(matched);
			}
		}

		@Nested
		@DisplayName("when map is empty")
		class EmptyMapTests {
			private final Map<String, Integer> entries = new Map<>();

			@Test
			@DisplayName("returns empty optional")
			void returnsEmptyOptional() {
				final var matched = entries.matchAny((key, value) ->
					value < 2);

				assert Optional.empty()
					.equals(matched);
			}
		}
	}

	@Nested
	@DisplayName(".filter(BiPredicate<K, V>)")
	class FilterTests {
		@Nested
		@DisplayName("when map is not empty")
		class NotEmptyMapTests {
			private final Map<String, Integer> entries = new MutableMap<String, Integer>()
				.set("G", 0)
				.set("V", 2)
				.set("v", 3)
				.set("O", 0)
				.set("q", 1)
				.set("e", 2)
				.toImmutable();

			@Test
			@DisplayName("when entries match, returns matched entries")
			void returnsMatchedEntriesWhenMatch() {
				final var matched = entries.filter((key, value) ->
					value < 2);

				final var expected = new MutableMap<String, Integer>()
					.set("G", 0)
					.set("O", 0)
					.set("q", 1)
					.toImmutable();

				assert expected.equals(matched);
			}

			@Test
			@DisplayName("when no entries match, returns empty map")
			void returnsEmptyMapWhenNoneMatches() {
				final var matched = entries.filter((key, value) ->
					value > 5);

				assert new Map<String, Integer>()
					.equals(matched);
			}
		}

		@Nested
		@DisplayName("when map is empty")
		class EmptyMapTests {
			private final Map<String, Integer> entries = new Map<>();

			@Test
			@DisplayName("returns empty map")
			void returnsEmptyMap() {
				final var matched = entries.filter((key, value) ->
					value < 2);

				assert new Map<String, Integer>()
					.equals(matched);
			}
		}
	}

	@Nested
	@DisplayName(".iterate(BiConsumer<K, V>)")
	class IterateTests {
		@Test
		@DisplayName("when map is not empty, iterates entries")
		void iteratesEntriesWhenNotEmpty() {
			final var entries = new MutableMap<String, Integer>()
				.set("n", 0)
				.set("B", 1)
				.set("P", 2)
				.set("L", 9)
				.toImmutable();

			final var iterated = new HashMap<String, Integer>();
			final var returned = entries.iterate(iterated::put);

			final var expected = java.util.Map.of(
				"n", 0,
				"B", 1,
				"P", 2,
				"L", 9);

			assert expected.equals(iterated);
			assert returned == entries;
		}

		@Test
		@DisplayName("when map is empty, does nothing")
		void doesNothingWhenEmpty() {
			final var entries = new Map<String, Integer>();

			final var iterated = new HashMap<String, Integer>();
			final var returned = entries.iterate(iterated::put);

			final var expected = java.util.Map.of();
			assert expected.equals(iterated);
			assert returned == entries;
		}
	}

	@Nested
	@DisplayName(".convert(BiFunction<K, V, Entry<L, W>>)")
	class ConvertTests {
		@Nested
		@DisplayName("when map is not empty")
		class NotEmptyTests {
			private final Map<String, Integer> entries = new MutableMap<String, Integer>()
				.set("B", 4)
				.set("V", 2)
				.set("n", 9)
				.set("X", 0)
				.toImmutable();

			@Test
			@DisplayName("returns converted entries")
			void returnsConvertedEntries() {
				final var converted = entries.convert((key, value) ->
					new Map.Entry<>(key, value / 2));

				final var expected = new MutableMap<String, Integer>()
					.set("B", 2)
					.set("V", 1)
					.set("n", 4)
					.set("X", 0)
					.toImmutable();

				assert expected.equals(converted);
			}

			@Test
			@DisplayName("when entry converted to null, ignores entry")
			void ignoresEntriesConvertedToNull() {
				final var converted = entries.convert((key, value) ->
					value % 2 == 0
						? new Map.Entry<>(key, value / 2)
						: null);

				final var expected = new MutableMap<String, Integer>()
					.set("B", 2)
					.set("V", 1)
					.set("X", 0)
					.toImmutable();

				assert expected.equals(converted);
			}

			@Test
			@DisplayName("when key converted to null, ignores entry")
			void ignoresKeysConvertedToNull() {
				final var converted = entries.convert((key, value) ->
					value % 2 == 0
						? new Map.Entry<>(null, 0)
						: new Map.Entry<>(key + "1", value));

				final var expected = new MutableMap<String, Integer>()
					.set("n1", 9)
					.toImmutable();

				assert expected.equals(converted);
			}

			@Test
			@DisplayName("when value converted to null, ignores entry")
			void ignoresValuesConvertedToNull() {
				final var converted = entries.convert((key, value) ->
					value % 3 == 0
						? new Map.Entry<>(key, value - 1)
						: new Map.Entry<>(key, null));

				final var expected = new MutableMap<String, Integer>()
					.set("n", 8)
					.set("X", -1)
					.toImmutable();

				assert expected.equals(converted);
			}

			@Test
			@DisplayName("when all entries converted to null, returns empty map")
			void returnsEmptyMapWhenAllEntriesConvertedToNull() {
				final var converted = entries.convert((key, value) -> null);

				assert new Map<String, Integer>()
					.equals(converted);
			}
		}

		@Nested
		@DisplayName("when map is empty")
		class EmptyMapTests {
			private final Map<String, Integer> entries = new Map<>();

			@Test
			@DisplayName("returns empty map")
			void returnsEmptyMap() {
				final var converted = entries.convert((key, value) ->
					new Map.Entry<>(key + "1", value + 1));

				assert new Map<String, Integer>()
					.equals(converted);
			}
		}
	}

	@Nested
	@DisplayName(".combine(TriFunction<R, K, V, R>)")
	class CombineTests {
		@Test
		@DisplayName("when map is not empty, returns combined entries")
		void returnsCombinedWhenNotEmpty() {
			final var entries = new MutableMap<String, Integer>()
				.set("V", 9)
				.set("c", 0)
				.set("W", 7)
				.set("x", 4)
				.toImmutable();

			final var combined = entries.combine("Map", (current, key, value) ->
				String.format("%s %s:%d", current, key, value));

			assert "Map V:9 W:7 c:0 x:4"
				.equals(combined);
		}

		@Test
		@DisplayName("when map is empty, returns initial value")
		void returnsInitialWhenEmpty() {
			final var entries = new Map<String, Integer>();
			final var combined = entries.combine("Empty Map", (current, key, value) ->
				String.format("%s %s:%d", current, key, value));

			assert "Empty Map"
				.equals(combined);
		}
	}

	@Nested
	@DisplayName(".join(String, String)")
	class JoinTests {
		@Test
		@DisplayName("when map is not empty, returns joined entries")
		void returnsJoinedEntries() {
			final var entries = new MutableMap<String, Integer>()
				.set("B", 2)
				.set("V", 1)
				.set("X", 0)
				.toImmutable();

			final var joined = entries.join(":", ", ");
			assert "B:2, V:1, X:0"
				.equals(joined);
		}

		@Test
		@DisplayName("when map is empty, returns empty string")
		void returnsEmptyStringWhenMapEmpty() {
			final var entries = new Map<String, Integer>();
			final var joined = entries.join(":", ", ");

			assert ""
				.equals(joined);
		}
	}
}
